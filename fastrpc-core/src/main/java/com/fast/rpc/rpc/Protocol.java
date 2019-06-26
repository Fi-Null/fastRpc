package com.fast.rpc.rpc;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;

/**
 * @ClassName Protocol
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:46
 * @Version 1.0
 **/
@SPI(value = Constants.FRAMEWORK_NAME)
public interface Protocol {

    <T> Reference<T> refer(Class<T> clz, URL url, URL serviceUrl);

    <T> Exporter<T> export(Provider<T> provider, URL url);

    void destroy();
}
