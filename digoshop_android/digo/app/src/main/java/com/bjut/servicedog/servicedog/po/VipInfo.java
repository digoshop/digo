package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by Xran on 16/7/22.
 */
public class VipInfo {
    //添加会员查询相关
    private String uid;
    private String birthday;
    private String occupation;
    private int gen;//0baomi 1nan 2nv
    private String nick;
    private String address;
    private String email;
    private String real_name;
    private long create_time;
    private String mobile;
    //会员列表相关
    private String user_name;
    private String vip_code;
    private String vip_level_name;
    private long vip_id;
    private int user_status;//
    private int intg;
    private int total;
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVip_code() {
        return vip_code;
    }

    public void setVip_code(String vip_code) {
        this.vip_code = vip_code;
    }

    public String getVip_level_name() {
        return vip_level_name;
    }

    public void setVip_level_name(String vip_level_name) {
        this.vip_level_name = vip_level_name;
    }

    public long getVip_id() {
        return vip_id;
    }

    public void setVip_id(long vip_id) {
        this.vip_id = vip_id;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public String getCreatimestr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String starTime = format.format(create_time * 1000);
        return starTime;
    }

    public String getStatus() {
        String a = "";
        if (user_status == 0) {
            a = "";
        } else {
            a = "解绑";
        }
        return a;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthdaystr() {
        String Time = "";
        if (birthday != null && !"0".equals(birthday)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Time = format.format(Long.parseLong(birthday) * 1000L);
        } else {
            Time = "";
        }

        return Time;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIntg() {
        return intg;
    }

    public void setIntg(int intg) {
        this.intg = intg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
