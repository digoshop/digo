package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/20.
 */
public class CheckAuctionRecord {
    private int total;
    private express e;
    private List<CheckAuctionRecord_data> data=new ArrayList<>();
    private Double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<CheckAuctionRecord_data> getData() {
        return data;
    }

    public void setData(List<CheckAuctionRecord_data> data) {
        this.data = data;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getTotal() {
        return total;
    }

    public CheckAuctionRecord setTotal(int total) {
        this.total = total;
        return this;
    }
}
