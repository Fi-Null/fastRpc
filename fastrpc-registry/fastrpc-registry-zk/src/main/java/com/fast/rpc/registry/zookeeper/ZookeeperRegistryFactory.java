package com.fast.rpc.registry.zookeeper;

import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.registry.AbstractRegistryFactory;
import com.fast.rpc.registry.Registry;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;

/**
 * @ClassName ZookeeperRegistryFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 15:44
 * @Version 1.0
 **/
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry createRegistry(URL registryUrl) {
        try {
            int timeout = registryUrl.getIntParameter(URLParam.registryConnectTimeout.getName(), URLParam.registryConnectTimeout.getIntValue());
            int sessionTimeout =
                    registryUrl.getIntParameter(URLParam.registrySessionTimeout.getName(),
                            URLParam.registrySessionTimeout.getIntValue());
            ZkClient zkClient = new ZkClient(registryUrl.getParameter(URLParam.registryAddress.getName()), sessionTimeout, timeout);
            return new ZookeeperRegistry(registryUrl, zkClient);
        } catch (ZkException e) {
            throw e;
        }
    }
}
