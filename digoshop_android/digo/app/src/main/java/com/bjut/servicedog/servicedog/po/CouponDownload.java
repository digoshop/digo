package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by Xran on 16/7/19.
 */
public class CouponDownload {
    private long down_time;
    private long mobile;
    private String time;
    private int amount;
    private long use_time;
    private String couponCode;
    private String nick;

    public String getUseTimeDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = format.format(use_time * 1000);
        return time;
    }

    public String getNick() {
        return nick;
    }

    public CouponDownload setNick(String nick) {
        this.nick = nick;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getUse_time() {
        return use_time;
    }

    public void setUse_time(long use_time) {
        this.use_time = use_time;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public long getDown_time() {
        return down_time;
    }

    public void setDown_time(long down_time) {
        this.down_time = down_time;
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = format.format(down_time * 1000);
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
}
