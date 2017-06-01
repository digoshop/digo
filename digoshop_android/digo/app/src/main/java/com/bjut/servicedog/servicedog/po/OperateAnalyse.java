package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/22.
 */
public class OperateAnalyse {
    private express e;
    private OperateAnalyse_data data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public OperateAnalyse_data getData() {
        return data;
    }

    public void setData(OperateAnalyse_data data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
