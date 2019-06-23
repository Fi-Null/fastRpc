package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;

/**
 * @InterfaceName Node
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:28
 * @Version 1.0
 **/
public interface Node {

    void init();

    void destroy();

    boolean isAvailable();

    String desc();

    URL getUrl();
}
