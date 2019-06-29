package com.fast.rpc.registry.zookeeper;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.registry.AbstractRegistry;
import com.fast.rpc.registry.NotifyListener;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ZookeeperRegistry
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 16:13
 * @Version 1.0
 **/
public class ZookeeperRegistry extends AbstractRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReentrantLock clientLock = new ReentrantLock();
    private final ReentrantLock serverLock = new ReentrantLock();
    private final ConcurrentHashMap<URL, ConcurrentHashMap<NotifyListener, IZkChildListener>> serviceListeners = new ConcurrentHashMap<>();

    private ZkClient zkClient;

    public ZookeeperRegistry(URL url, ZkClient zkClient) {
        super(url);
        this.zkClient = zkClient;
        IZkStateListener zkStateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                // do nothing
            }

            @Override
            public void handleNewSession() throws Exception {
                logger.info("zkRegistry get new session notify.");

            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {

            }
        };
        this.zkClient.subscribeStateChanges(zkStateListener);
    }

    @Override
    protected void doRegister(URL url) {
        try {
            serverLock.lock();
            // 防止旧节点未正常注销
            removeNode(url, ZkNodeType.SERVER);
            createNode(url, ZkNodeType.SERVER);
        } catch (Throwable e) {
            throw new RpcFrameworkException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doUnregister(URL url) {
        try {
            serverLock.lock();
            removeNode(url, ZkNodeType.SERVER);
        } catch (Throwable e) {
            throw new RpcFrameworkException(String.format("Failed to unregister %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doSubscribe(final URL url, final NotifyListener listener) {
        try {
            clientLock.lock();

            ConcurrentHashMap<NotifyListener, IZkChildListener> childChangeListeners = serviceListeners.get(url);
            if (childChangeListeners == null) {
                serviceListeners.putIfAbsent(url, new ConcurrentHashMap());
                childChangeListeners = serviceListeners.get(url);
            }
            IZkChildListener zkChildListener = childChangeListeners.get(listener);
            if (zkChildListener == null) {
                childChangeListeners.putIfAbsent(listener, (parentPath, currentChilds) -> {
                    listener.notify(getUrl(), childrenNodeToUrls(parentPath, currentChilds));
                    logger.info(String.format("[ZookeeperRegistry] service list change: path=%s, currentChilds=%s", parentPath, currentChilds.toString()));
                });
                zkChildListener = childChangeListeners.get(listener);
            }

            // 防止旧节点未正常注销
            removeNode(url, ZkNodeType.CLIENT);
            createNode(url, ZkNodeType.CLIENT);

            String serverTypePath = ZkUtils.toNodeTypePath(url, ZkNodeType.SERVER);
            zkClient.subscribeChildChanges(serverTypePath, zkChildListener);
            logger.info(String.format("[ZookeeperRegistry] subscribe service: path=%s, info=%s", ZkUtils.toNodePath(url, ZkNodeType.SERVER), url.toFullUri()));
        } catch (Throwable e) {
            throw new RpcFrameworkException(String.format("Failed to subscribe %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            clientLock.unlock();
        }
    }

    @Override
    protected void doUnsubscribe(URL url, NotifyListener listener) {
        try {
            clientLock.lock();
            Map<NotifyListener, IZkChildListener> childChangeListeners = serviceListeners.get(url);
            if (childChangeListeners != null) {
                IZkChildListener zkChildListener = childChangeListeners.get(listener);
                if (zkChildListener != null) {
                    zkClient.unsubscribeChildChanges(ZkUtils.toNodeTypePath(url, ZkNodeType.CLIENT), zkChildListener);
                    childChangeListeners.remove(listener);
                }
            }
        } catch (Throwable e) {
            throw new RpcFrameworkException(String.format("Failed to unsubscribe service %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            clientLock.unlock();
        }
    }

    @Override
    protected List<URL> doDiscover(URL url) {
        return discoverService(url);
    }

    private void createNode(URL url, ZkNodeType nodeType) {
        String nodeTypePath = ZkUtils.toNodeTypePath(url, nodeType);
        if (!zkClient.exists(nodeTypePath)) {
            zkClient.createPersistent(nodeTypePath, true);
        }
        String nodePath = ZkUtils.toNodePath(url, nodeType);
        logger.info(String.format("[ZookeeperRegistry] createNode: nodeTypePath=%s,nodePath=%s,fullUrl=%s", nodeTypePath, nodePath, url.toFullUri()));
        //zkClient.createEphemeral(ZkUtils.toNodePath(url, nodeType), url.toFullUri());
        zkClient.createPersistent(nodePath, url.toFullUri());
    }

    private void removeNode(URL url, ZkNodeType nodeType) {
        String nodePath = ZkUtils.toNodePath(url, nodeType);
        if (zkClient.exists(nodePath)) {
            zkClient.delete(nodePath);
        }
    }

    private List<URL> discoverService(URL url) {
        try {
            String parentPath = ZkUtils.toNodeTypePath(url, ZkNodeType.SERVER);
            logger.info("discoverService parentPath:" + parentPath);

            List<String> children = new ArrayList<String>();
            if (zkClient.exists(parentPath)) {
                children = zkClient.getChildren(parentPath);
            }
            return childrenNodeToUrls(parentPath, children);
        } catch (Throwable e) {
            throw new RpcFrameworkException(String.format("Failed to discover service %s from zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        }
    }

    private List<URL> childrenNodeToUrls(String parentPath, List<String> children) {
        List<URL> urls = new ArrayList();
        if (children != null) {
            for (String node : children) {
                String nodePath = parentPath + Constants.PATH_SEPARATOR + node;
                logger.info("children:" + children + "node: " + nodePath);
                String data = zkClient.readData(nodePath, true);
                try {
                    URL url = URL.parse(data);
                    urls.add(url);
                } catch (Exception e) {
                    logger.warn(String.format("Found malformed urls from ZookeeperRegistry, path=%s", nodePath), e);
                }
            }
        }
        return urls;
    }

    @Override
    public void close() {
        this.zkClient.close();
    }
}
