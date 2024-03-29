package com.fast.rpc.registry.zookeeper;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;

/**
 * @ClassName ZkUtils
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 15:35
 * @Version 1.0
 **/
public class ZkUtils {

    public static String toGroupPath(URL url) {
        return Constants.ZOOKEEPER_REGISTRY_NAMESPACE + Constants.PATH_SEPARATOR + url.getGroup();
    }

    public static String toServicePath(URL url) {
        return toGroupPath(url) + Constants.PATH_SEPARATOR + url.getInterfaceName();
    }

    public static String toNodeTypePath(URL url, ZkNodeType nodeType) {
        return toServicePath(url) + Constants.PATH_SEPARATOR + nodeType.getValue();
    }

    public static String toNodePath(URL url, ZkNodeType nodeType) {
        return toNodeTypePath(url, nodeType) + Constants.PATH_SEPARATOR + url.getServerAndPort();
    }
}
