package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/19.
 */
public class CouponDetail {
    private express e;
    private Coupon data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public Coupon getData() {
        return data;
    }

    public void setData(Coupon data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
