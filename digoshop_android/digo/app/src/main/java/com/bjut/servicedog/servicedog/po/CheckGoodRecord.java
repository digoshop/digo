package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/20.
 */
public class CheckGoodRecord {
    private int total;
    private express e;
    private List<CheckGoodRecord_data> data=new ArrayList<>();
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<CheckGoodRecord_data> getData() {
        return data;
    }

    public void setData(List<CheckGoodRecord_data> data) {
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

    public CheckGoodRecord setTotal(int total) {
        this.total = total;
        return this;
    }
}
