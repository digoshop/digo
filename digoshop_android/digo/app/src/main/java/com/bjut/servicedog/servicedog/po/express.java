package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;

/**
 * Created by Xran on 16/7/13.
 */
public class express implements Serializable {
    private String desc;
    private String provider;
    private int code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
