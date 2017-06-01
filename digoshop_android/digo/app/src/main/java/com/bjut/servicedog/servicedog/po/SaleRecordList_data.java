package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by Xran on 16/7/21.
 */
public class SaleRecordList_data {
    private long max_id;
    private long min_id;
    private List<SaleRecord> expenses;
    private SaleStat stat;

    public SaleStat getStat() {
        return stat;
    }

    public void setStat(SaleStat stat) {
        this.stat = stat;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public long getMin_id() {
        return min_id;
    }

    public void setMin_id(long min_id) {
        this.min_id = min_id;
    }

    public List<SaleRecord> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<SaleRecord> expenses) {
        this.expenses = expenses;
    }
}
