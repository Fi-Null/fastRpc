package com.fast.rpc.cluster;

import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.DefaultResponse;
import com.fast.rpc.core.ExtensionLoader;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.exception.RpcServiceException;
import com.fast.rpc.registry.NotifyListener;
import com.fast.rpc.registry.Registry;
import com.fast.rpc.registry.RegistryFactory;
import com.fast.rpc.rpc.Protocol;
import com.fast.rpc.rpc.ProtocolFilterWrapper;
import com.fast.rpc.rpc.Reference;
import com.fast.rpc.util.CollectionUtil;
import com.fast.rpc.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DefaultCluster
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:07
 * @Version 1.0
 **/
public class DefaultCluster<T> implements Cluster<T>, NotifyListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<URL> registryUrls;

    private URL refUrl;

    private Class<T> interfaceClass;

    private Protocol protocol;

    private HaStrategy<T> haStrategy;

    private LoadBalance<T> loadBalance;

    private volatile List<Reference<T>> references;

    private ConcurrentHashMap<URL, List<Reference<T>>> registryReferences = new ConcurrentHashMap<>();

    private volatile boolean available;

    public DefaultCluster(Class<T> interfaceClass, URL refUrl, List<URL> registryUrls) {
        this.registryUrls = registryUrls;
        this.interfaceClass = interfaceClass;
        this.refUrl = refUrl;
        protocol = new ProtocolFilterWrapper(ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(refUrl.getProtocol()));
    }

    @Override
    public void init() {

        URL subscribeUrl = refUrl.clone0();
        for (URL ru : registryUrls) {

            Registry registry = getRegistry(ru);
            try {
                notify(ru, registry.discover(subscribeUrl));
            } catch (Exception e) {
                logger.error(String.format("Cluster init discover for the reference:%s, registry:%s", this.refUrl, ru), e);
            }
            // client 注册自己，同时订阅service列表
            registry.subscribe(subscribeUrl, this);
        }

        logger.info("Cluster init over, refUrl:{}, references size:{}", refUrl, references != null ? references.size() : 0);
        boolean check = Boolean.parseBoolean(refUrl.getParameter(URLParam.check.getName(), URLParam.check.getValue()));
        if (CollectionUtil.isEmpty(references)) {
            logger.warn(String.format("Cluster No service urls for the reference:%s, registries:%s",
                    this.refUrl, registryUrls));
            if (check) {
                throw new RpcFrameworkException(String.format("Cluster No service urls for the reference:%s, registries:%s",
                        this.refUrl, registryUrls));
            }
        }
        available = true;
    }

    @Override
    public void destroy() {
        URL subscribeUrl = refUrl.clone0();
        for (URL ru : registryUrls) {
            try {
                Registry registry = getRegistry(ru);
                registry.unsubscribe(subscribeUrl, this);
                registry.unregister(refUrl);
            } catch (Exception e) {
                logger.warn(String.format("Unregister or unsubscribe false for refUrl (%s), registry= %s", refUrl, ru), e);
            }
        }
        if (references != null) {
            for (Reference<T> reference : this.references) {
                reference.destroy();
            }
        }
        available = false;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String desc() {
        return null;
    }

    @Override
    public URL getUrl() {
        return refUrl;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public Response call(Request request) {
        if (available) {
            try {
                return haStrategy.call(request, loadBalance);
            } catch (Exception e) {
                if (ExceptionUtil.isBizException(e)) {
                    throw (RuntimeException) e;
                }
                return buildErrorResponse(request, e);
            }
        }
        return buildErrorResponse(request, new RpcServiceException("service not available"));
    }

    private Response buildErrorResponse(Request request, Exception motanException) {
        DefaultResponse rs = new DefaultResponse();
        rs.setException(motanException);
        rs.setRequestId(request.getRequestId());
        return rs;
    }

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public void setHaStrategy(HaStrategy<T> haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public List<Reference<T>> getReferences() {
        return references;
    }

    @Override
    public LoadBalance<T> getLoadBalance() {
        return loadBalance;
    }

    @Override
    public synchronized void notify(URL registryUrl, List<URL> urls) {
        if (CollectionUtil.isEmpty(urls)) {
            logger.warn("Cluster config change notify, urls is empty: registry={} service={} urls=[]", registryUrl.getUri(),
                    refUrl, urls);
            return;
        }

        logger.info("Cluster config change notify: registry={} refUrl={} urls={}", registryUrl.getUri(), refUrl,
                urls);

        List<Reference<T>> newReferences = new ArrayList<>();
        for (URL u : urls) {
            if (!u.canServe(refUrl)) {
                continue;
            }
            Reference<T> reference = getExistingReference(u, registryReferences.get(registryUrl));
            if (reference == null) {
                URL referenceURL = u.clone0();
                reference = protocol.refer(interfaceClass, referenceURL, u);
            }
            if (reference != null) {
                newReferences.add(reference);
            }
        }
        registryReferences.put(registryUrl, newReferences);

        refresh();
    }

    private void refresh() {
        List<Reference<T>> references = new ArrayList<>();
        for (List<Reference<T>> refs : registryReferences.values()) {
            references.addAll(refs);
        }
        this.references = references;
        this.loadBalance.setReferences(references);
    }

    private Reference<T> getExistingReference(URL url, List<Reference<T>> referers) {
        if (referers == null) {
            return null;
        }
        for (Reference<T> r : referers) {
            if (url.equals(r.getUrl()) || url.equals(r.getServiceUrl())) {
                return r;
            }
        }
        return null;
    }

    private Registry getRegistry(URL registryUrl) {
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(registryUrl.getProtocol());
        if (registryFactory == null) {
            throw new RpcFrameworkException("register error! Could not find extension for registry protocol:" + registryUrl.getProtocol()
                    + ", make sure registry module for " + registryUrl.getProtocol() + " is in classpath!");
        }
        return registryFactory.getRegistry(registryUrl);
    }
}
