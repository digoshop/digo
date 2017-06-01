package com.bjut.servicedog.servicedog.po;

import android.util.Log;

import java.util.List;

/**
 * Created by Xran on 16/7/19.
 */
public class Shop {
    private String sn;
    private String scov;
    private String sbn;
    private long sid;
    private List<String> shop_album;
    private String shop_name;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public List<String> getShop_album() {
        return shop_album;
    }

    public void setShop_album(List<String> shop_album) {
        this.shop_album = shop_album;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getScov() {
        return scov;
    }

    public void setScov(String scov) {
        this.scov = scov;
    }

    public String getSbn() {
        return sbn;
    }

    public void setSbn(String sbn) {
        this.sbn = sbn;
    }
}
