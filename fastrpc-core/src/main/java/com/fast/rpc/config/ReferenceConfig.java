package com.fast.rpc.config;

import com.fast.rpc.cluster.Cluster;

import java.util.List;

/**
 * @ClassName ReferenceConfig
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:41
 * @Version 1.0
 **/
public class ReferenceConfig<T> extends AbstractInterfaceConfig {
    private static final long serialVersionUID = 3259358868568571457L;
    private Class<T> interfaceClass;
    protected transient volatile T proxy;

    private transient volatile boolean initialized;
    private List<Cluster<T>> clusters;
}
