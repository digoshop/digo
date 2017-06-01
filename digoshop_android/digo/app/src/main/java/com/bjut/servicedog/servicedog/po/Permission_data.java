package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/25.
 */
public class Permission_data {
    private String auth_name;//角色名称
    private int auth;//编号

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }
}
