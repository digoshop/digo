package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/8/9.
 */
public class NewVip_data {
    private String uid;//用户ID
    private int intg;//用户积分
    private String nick;//用户昵称
    private boolean is_vip;//是否是会员
    private String vip_level_name;//会员级别
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIntg() {
        return intg;
    }

    public void setIntg(int intg) {
        this.intg = intg;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean is_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public String getVip_level_name() {
        return vip_level_name;
    }

    public void setVip_level_name(String vip_level_name) {
        this.vip_level_name = vip_level_name;
    }
}
