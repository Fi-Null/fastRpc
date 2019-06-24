package com.fast.rpc.core;

import java.util.Map;

/**
 * @InterfaceName Request
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:22
 * @Version 1.0
 **/
public interface Request {
    Long getRequestId();

    String getInterfaceName();

    String getMethodName();

    Object[] getArguments();

    Class<?>[] getParameterTypes();

    Map<String, String> getAttachments();

    String getAttachment(String key);

    String getAttachment(String key, String defaultValue);

    void setAttachment(String key, String value);
}
