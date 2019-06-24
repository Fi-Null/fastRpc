package com.fast.rpc.config;

/**
 * @ClassName RegistryConfig
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:17
 * @Version 1.0
 **/
public class RegistryConfig extends AbstractConfig {
    private static final long serialVersionUID = -8358809338589687068L;
    private String protocol;
    private String address;

    private String username;
    private String password;

    private Integer sessionTimeout;
    private Integer connectTimeout;

    private Boolean isDefault = Boolean.TRUE;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
