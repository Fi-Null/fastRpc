package com.fast.rpc.core;

import java.util.Map;

/**
 * @ClassName Response
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:35
 * @Version 1.0
 **/
public interface Response {
    Long getRequestId();

    Exception getException();

    Object getResult();

    Map<String, String> getAttachments();

    String getAttachment(String key);

    String getAttachment(String key, String defaultValue);

    void setAttachment(String key, String value);
}
