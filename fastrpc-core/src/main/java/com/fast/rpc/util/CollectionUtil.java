package com.fast.rpc.util;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName CollectionUtil
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 22:57
 * @Version 1.0
 **/
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
