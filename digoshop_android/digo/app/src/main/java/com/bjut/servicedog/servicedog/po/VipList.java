package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/22.
 */
public class VipList
{
    private int total;
    private express e;
    private List<VipInfo> data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<VipInfo> getData() {
        return data;
    }

    public void setData(List<VipInfo> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
