package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/14.
 */
public class GoodsDetail {
    private express e;
    private GoodsDetail_data data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public GoodsDetail_data getData() {
        return data;
    }

    public void setData(GoodsDetail_data data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
