package com.fast.rpc.rpc;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.cluster.Cluster;
import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.google.common.collect.ArrayListMultimap;

import java.util.List;

/**
 * @InterfaceName ConfigHandler
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:09
 * @Version 1.0
 **/
@SPI(value = Constants.DEFAULT_VALUE)
public interface ConfigHandler {

    <T> Cluster<T> buildCluster(Class<T> interfaceClass, URL refUrl, List<URL> registryUrls);

    /**
     * 引用服务
     *
     * @param interfaceClass
     * @param cluster
     * @param <T>
     * @return
     */
    <T> T refer(Class<T> interfaceClass, List<Cluster<T>> cluster, String proxyType);

    /**
     * 暴露服务
     *
     * @param interfaceClass
     * @param ref
     * @param serviceUrl
     * @param registryUrls
     * @param <T>
     * @return
     */
    <T> Exporter<T> export(Class<T> interfaceClass, T ref, URL serviceUrl, List<URL> registryUrls);

    <T> void unexport(List<Exporter<T>> exporters, ArrayListMultimap<URL, URL> registryUrls);

}
