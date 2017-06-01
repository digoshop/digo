package com.bjut.servicedog.servicedog.po;

/**
 * Cd by Xran on 16/7/21.
 */
public class SaleStat {
    private String total_amount;
    private long total;
    private String average;

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
