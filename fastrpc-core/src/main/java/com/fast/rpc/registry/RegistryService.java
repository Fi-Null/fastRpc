package com.fast.rpc.registry;

import com.fast.rpc.common.URL;

/**
 * @ClassName RegistryService
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:44
 * @Version 1.0
 **/
public interface RegistryService {
    /**
     * register service to registry
     *
     * @param url
     */
    void register(URL url) throws Exception;

    /**
     * unregister service to registry
     *
     * @param url
     */
    void unregister(URL url) throws Exception;
}
