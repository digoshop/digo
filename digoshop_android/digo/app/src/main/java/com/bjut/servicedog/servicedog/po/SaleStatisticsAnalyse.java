package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/27.
 */
public class SaleStatisticsAnalyse {
    private int total;
    private express e;
    private SaleStatistics_data data;
    private SaleStatistics_expenses expenses;
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

    public SaleStatistics_data getData() {
        return data;
    }

    public void setData(SaleStatistics_data data) {
        this.data = data;
    }

    public SaleStatistics_expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(SaleStatistics_expenses expenses) {
        this.expenses = expenses;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
