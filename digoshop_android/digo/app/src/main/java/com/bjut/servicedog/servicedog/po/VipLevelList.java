package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/22.
 */
public class VipLevelList {
    private express e;
    private List<VipLevel> data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<VipLevel> getData() {
        return data;
    }

    public void setData(List<VipLevel> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
