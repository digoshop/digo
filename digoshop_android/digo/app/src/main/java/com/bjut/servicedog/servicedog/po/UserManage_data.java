package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/24.
 */
public class UserManage_data {
    private String uid;//用户ID
    private int status;//禁用或者启用的状态
    private String name;//角色名字
    private String auth;//权限
    private String mobile;
    private long ct;

    public long getCt() {
        return ct;
    }

    public UserManage_data setCt(long ct) {
        this.ct = ct;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
