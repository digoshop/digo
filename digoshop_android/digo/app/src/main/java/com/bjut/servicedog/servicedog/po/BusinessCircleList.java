package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/31.
 */
public class BusinessCircleList {
    private express e;
    private List<BusinessCircle> data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<BusinessCircle> getData() {
        return data;
    }

    public void setData(List<BusinessCircle> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
