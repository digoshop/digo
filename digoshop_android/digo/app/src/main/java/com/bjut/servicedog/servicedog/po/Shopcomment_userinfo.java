package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/28.
 */
public class Shopcomment_userinfo {
    private String uid;
    private String village;//用户所在区域
    private String nick;//用户昵称
    private int status;
    private int gender;//用户性别
    private String avatar;//用户头像

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
