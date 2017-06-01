package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by apple02 on 16/7/20.
 */
public class CheckAuctionRecord_data {
    private String nk;
    private String ppr;
    private long ct;

    public String getCtime(){
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(ct*1000));
        return date;
    }
    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }
}
