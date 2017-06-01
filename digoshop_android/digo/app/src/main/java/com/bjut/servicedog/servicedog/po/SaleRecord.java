package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by Xran on 16/7/21.
 */
public class SaleRecord {
    private String amount;
    private String uid;
    private long sid;
    private int is_comment;
    private int is_vip;
    private String[] categorys;
    private int intg;
    private long create_time;
    private UserInfo userinfo;
    private long eid;
    private int incentives_amount;
    private String nick;
    private int type;

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    private String pna;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKinds() {
        String s = "";
        for (int i = 0; i < categorys.length; i++) {
            s += categorys[i] + " ";
        }
        return s;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public String[] getCategorys() {
        return categorys;
    }

    public void setCategorys(String[] categorys) {
        this.categorys = categorys;
    }

    public int getIntg() {
        return intg;
    }

    public void setIntg(int intg) {
        this.intg = intg;
    }

    public long getCreate_time() {
        return create_time;
    }

    public String getCreattimeStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(create_time * 1000);
        return time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public int getIncentives_amount() {
        return incentives_amount;
    }

    public void setIncentives_amount(int incentives_amount) {
        this.incentives_amount = incentives_amount;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
