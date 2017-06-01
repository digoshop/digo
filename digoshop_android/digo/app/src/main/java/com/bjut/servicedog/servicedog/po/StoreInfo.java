package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/8/22.
 */
public class StoreInfo {
    private express e;
    private StoreInfo_data data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public StoreInfo_data getData() {
        return data;
    }

    public void setData(StoreInfo_data data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
