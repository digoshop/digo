package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/13.
 */
public class GoodsList {
    private express e;
    private double cost;
    private List<Good> data;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Good> getData() {
        return data;
    }

    public void setData(List<Good> data) {
        this.data = data;
    }
}
