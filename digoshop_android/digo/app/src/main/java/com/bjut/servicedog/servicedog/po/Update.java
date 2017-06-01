package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/9/13.
 */
public class Update {
    private express e;
    private UpdateInfo data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public UpdateInfo getData() {
        return data;
    }

    public void setData(UpdateInfo data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
