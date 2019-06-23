package com.fast.rpc.config;


import java.util.Collections;
import java.util.List;

/**
 * @ClassName AbstractInterfaceConfig
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:13
 * @Version 1.0
 **/
public class AbstractInterfaceConfig {

    private static final long serialVersionUID = 3928005245888186559L;

    protected String interfaceName;
    protected String group;
    protected String version;
    protected Integer timeout;
    protected Integer retries;

    //server暴露服务使用的协议，暴露可以使用多种协议，但client只能用一种协议进行访问，原因是便于client的管理
    protected List<ProtocolConfig> protocols;
    // 注册中心的配置列表
    protected List<RegistryConfig> registries;

    // 是否进行check，如果为true，则在监测失败后抛异常
    protected Boolean check = Boolean.TRUE;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocols = Collections.singletonList(protocol);
    }

    public void setRegistry(RegistryConfig registry) {
        this.registries = Collections.singletonList(registry);
    }
}
