package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/27.
 */
public class SaleStatistics_expenses {
    private int amount;
    private String uid;
    private String sid;
    private int is_comment;
    private int intg;
    private int is_vip;
    private long create_time;
    private SaleStatistics_userinfo userinfo;
    private String eid;
    private int incentives_amout;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public int getIntg() {
        return intg;
    }

    public void setIntg(int intg) {
        this.intg = intg;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public SaleStatistics_userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(SaleStatistics_userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public int getIncentives_amout() {
        return incentives_amout;
    }

    public void setIncentives_amout(int incentives_amout) {
        this.incentives_amout = incentives_amout;
    }
}
