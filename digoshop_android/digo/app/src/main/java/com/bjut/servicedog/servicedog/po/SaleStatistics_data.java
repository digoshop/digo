package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/27.
 */
public class SaleStatistics_data {
    private int max_id;
    private int min_id;
    private SaleStat stat;

    public int getMax_id() {
        return max_id;
    }

    public void setMax_id(int max_id) {
        this.max_id = max_id;
    }

    public int getMin_id() {
        return min_id;
    }

    public void setMin_id(int min_id) {
        this.min_id = min_id;
    }

    public SaleStat getStat() {
        return stat;
    }

    public void setStat(SaleStat stat) {
        this.stat = stat;
    }
}
