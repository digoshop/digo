package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/22.
 */
public class VipSearch {
    private express e;
    private VipInfo data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public VipInfo getData() {
        return data;
    }

    public void setData(VipInfo data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
