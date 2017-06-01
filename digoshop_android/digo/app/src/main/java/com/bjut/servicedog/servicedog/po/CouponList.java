package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xran on 16/7/18.
 */
public class CouponList {
    private express e;
    private List<Coupon> data =new ArrayList<Coupon>();
    private int cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<Coupon> getData() {
        return data;
    }

    public void setData(List<Coupon> data) {
        this.data = data;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
