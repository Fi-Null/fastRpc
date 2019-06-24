package com.fast.rpc.registry;

import com.fast.rpc.common.URL;

import java.util.List;

/**
 * @ClassName DiscoveryService
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:45
 * @Version 1.0
 **/
public interface DiscoveryService {


    void subscribe(URL url, NotifyListener listener);

    void unsubscribe(URL url, NotifyListener listener);

    /**
     * 查询符合条件的已注册数据，与订阅的推模式相对应，这里为拉模式，只返回一次结果。
     * @param url
     * @return
     */
    List<URL> discover(URL url) throws Exception;
}
