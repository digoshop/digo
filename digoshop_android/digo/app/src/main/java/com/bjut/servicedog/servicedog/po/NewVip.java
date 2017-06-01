package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/8/9.
 */
public class NewVip {
    private express e;
    private double cost;
    private NewVip_data data;

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

    public NewVip_data getData() {
        return data;
    }

    public void setData(NewVip_data data) {
        this.data = data;
    }
}
