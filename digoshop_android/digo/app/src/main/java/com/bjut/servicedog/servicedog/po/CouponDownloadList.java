package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/19.
 */
public class CouponDownloadList {
    private express e;
    private List<CouponDownload> data;
    private double cost;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<CouponDownload> getData() {
        return data;
    }

    public void setData(List<CouponDownload> data) {
        this.data = data;
    }

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }
}
