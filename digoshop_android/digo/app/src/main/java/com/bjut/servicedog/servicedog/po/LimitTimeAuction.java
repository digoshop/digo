package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by apple02 on 16/7/27.
 */
public class LimitTimeAuction {
    private int total;
    private express e;
    private int count;
    private int page;
    private List<LimitTimeAuction_data> data;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<LimitTimeAuction_data> getData() {
        return data;
    }

    public void setData(List<LimitTimeAuction_data> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
