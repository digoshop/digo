package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by apple02 on 16/7/24.
 */
public class Vip_class_config {
    private express e;
    private List<Vip_class_config_data> data;
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<Vip_class_config_data> getData() {
        return data;
    }

    public void setData(List<Vip_class_config_data> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
