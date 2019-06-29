package com.fast.rpc.registry;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.common.URL;
import com.fast.rpc.enums.Scope;

/**
 * @ClassName RegistryFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 15:44
 * @Version 1.0
 **/
@SPI(scope = Scope.SINGLETON)
public interface RegistryFactory {

    Registry getRegistry(URL url);

}
