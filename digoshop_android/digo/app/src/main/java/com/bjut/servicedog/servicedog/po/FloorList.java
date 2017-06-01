package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/31.
 */
public class FloorList {
    private int total;
    private express e;
    private List<Floor> data;
    private double cost;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<Floor> getData() {
        return data;
    }

    public void setData(List<Floor> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
