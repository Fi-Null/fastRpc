package com.fast.rpc.annotation;


import com.fast.rpc.enums.Scope;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * 缺省扩展点名。
     */
    String value() default "";

    Scope scope() default Scope.SINGLETON;
}
