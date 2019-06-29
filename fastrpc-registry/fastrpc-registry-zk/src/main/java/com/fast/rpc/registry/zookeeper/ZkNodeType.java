package com.fast.rpc.registry.zookeeper;

/**
 * @ClassName ZkNodeType
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 15:42
 * @Version 1.0
 **/
public enum ZkNodeType {
    SERVER("providers"),
    CLIENT("consumers");

    private String value;

    ZkNodeType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
