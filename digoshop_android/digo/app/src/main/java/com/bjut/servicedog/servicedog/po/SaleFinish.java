package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/29.
 */
public class SaleFinish {
    private express e;
    private SaleFinish_data data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public SaleFinish_data getData() {
        return data;
    }

    public void setData(SaleFinish_data data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
