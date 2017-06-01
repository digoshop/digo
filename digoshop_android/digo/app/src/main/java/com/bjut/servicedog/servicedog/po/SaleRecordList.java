package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/21.
 */
public class SaleRecordList {
    private int total;
    private express e;
    private SaleRecordList_data data;
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

    public SaleRecordList_data getData() {
        return data;
    }

    public void setData(SaleRecordList_data data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
