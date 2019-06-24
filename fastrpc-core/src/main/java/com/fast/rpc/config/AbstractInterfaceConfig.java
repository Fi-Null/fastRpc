package com.fast.rpc.config;


import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.registry.Registry;
import com.fast.rpc.util.NetUtils;
import com.fast.rpc.util.StringUtils;

import java.util.*;

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


    protected List<URL> loadRegistryUrls() {
        List<URL> registryList = new ArrayList<URL>();

        if (registries != null && !registries.isEmpty()) {
            for (RegistryConfig config : registries) {
                String address = config.getAddress();
                String protocol = config.getProtocol();

                if (StringUtils.isBlank(address)) {
                    address = NetUtils.LOCALHOST + Constants.HOST_PORT_SEPARATOR + Constants.DEFAULT_INT_VALUE;
                    protocol = Constants.REGISTRY_PROTOCOL_LOCAL;
                }

                Map<String, String> parameters = new HashMap<>();
                parameters.put(URLParam.path.getName(), Registry.class.getName());
                parameters.put(URLParam.registryAddress.getName(), String.valueOf(address));
                parameters.put(URLParam.registryProtocol.getName(), String.valueOf(protocol));
                parameters.put(URLParam.timestamp.getName(), String.valueOf(System.currentTimeMillis()));
                parameters.put(URLParam.protocol.getName(), protocol);

                Integer connectTimeout = URLParam.registryConnectTimeout.getIntValue();
                if (config.getConnectTimeout() != null) {
                    connectTimeout = config.getConnectTimeout();
                }
                parameters.put(URLParam.registryConnectTimeout.getName(), String.valueOf(connectTimeout));

                Integer sessionTimeout = URLParam.registrySessionTimeout.getIntValue();
                if (config.getSessionTimeout() != null) {
                    sessionTimeout = config.getSessionTimeout();
                }
                parameters.put(URLParam.registrySessionTimeout.getName(), String.valueOf(sessionTimeout));

                String[] arr = address.split(Constants.HOST_PORT_SEPARATOR);
                URL url = new URL(protocol, arr[0], Integer.parseInt(arr[1]), Registry.class.getName(), parameters);
                registryList.add(url);
            }
        }


        return registryList;
    }


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
