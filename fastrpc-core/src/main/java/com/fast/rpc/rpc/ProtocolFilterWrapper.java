package com.fast.rpc.rpc;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcFrameworkException;

import java.util.List;

/**
 * @ClassName ProtocolFilterWrapper
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:51
 * @Version 1.0
 **/
public class ProtocolFilterWrapper implements Protocol {

    private Protocol protocol;

    public ProtocolFilterWrapper(Protocol protocol) {
        if (protocol == null) {
            throw new RpcFrameworkException("Protocol is null when construct " + this.getClass().getName());
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Reference<T> refer(Class<T> clz, URL url, URL serviceUrl) {
        return buildReferenceChain(protocol.refer(clz, url, serviceUrl), url);
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL url) {
        return protocol.export(buildProviderChain(provider, url), url);
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }

    private <T> Reference<T> buildReferenceChain(Reference<T> reference, URL url) {

        return null;
    }

    private <T> Provider<T> buildProviderChain(Provider<T> provider, URL url) {
        List<Filter> filters = getFilters(url, Constants.PROVIDER);
        if (filters == null || filters.size() == 0) {
            return provider;
        }
        Provider<T> lastProvider = provider;
        for (Filter filter : filters) {
            final Filter f = filter;
            final Provider<T> lp = lastProvider;
            lastProvider = new Provider<T>() {
                @Override
                public Response call(Request request) {
                    return f.filter(lp, request);
                }

                @Override
                public String desc() {
                    return lp.desc();
                }

                @Override
                public void destroy() {
                    lp.destroy();
                }

                @Override
                public Class<T> getInterface() {
                    return lp.getInterface();
                }

                @Override
                public URL getUrl() {
                    return lp.getUrl();
                }

                @Override
                public void init() {
                    lp.init();
                }

                @Override
                public boolean isAvailable() {
                    return lp.isAvailable();
                }
            };
        }
        return lastProvider;
    }

    private List<Filter> getFilters(URL url, String category) {
        return null;
    }
}

