package com.fast.rpc.registry;

import com.fast.rpc.common.URL;

import java.util.List;

/**
 * @ClassName NotifyListener
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:45
 * @Version 1.0
 **/
public interface NotifyListener {
    void notify(URL registryUrl, List<URL> urls);

}
