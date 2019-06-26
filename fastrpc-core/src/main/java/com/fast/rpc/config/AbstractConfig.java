package com.fast.rpc.config;

import java.io.Serializable;


public class AbstractConfig implements Serializable {

    private static final long serialVersionUID = -3618251339903241483L;

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
