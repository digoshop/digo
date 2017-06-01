package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by apple02 on 16/8/11.
 */
public class Request {
    private int total;
    private express e;
    private List<Time> data;
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

    public List<Time> getData() {
        return data;
    }

    public void setData(List<Time> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
