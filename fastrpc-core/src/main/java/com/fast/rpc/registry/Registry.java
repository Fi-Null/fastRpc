package com.fast.rpc.registry;

import com.fast.rpc.common.URL;

/**
 * @ClassName Registry
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:43
 * @Version 1.0
 **/
public interface Registry extends RegistryService, DiscoveryService {
    URL getUrl();

    void close();
}
