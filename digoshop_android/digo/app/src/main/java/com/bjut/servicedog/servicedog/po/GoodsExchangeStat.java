package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/27.
 */
public class GoodsExchangeStat {
    private int total;
    private express e;
    private List<GoodsExchangeStat_data> data=new ArrayList<>();
    private double cost;
    private int count;
    private int page;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<GoodsExchangeStat_data> getData() {
        return data;
    }

    public void setData(List<GoodsExchangeStat_data> data) {
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

    public void setTotal(int total) {
        this.total = total;
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
}
