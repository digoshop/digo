package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/24.
 */
public class UserManage {
    private express e;
    private List<UserManage_data> data=new ArrayList<>();
    private double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public List<UserManage_data> getData() {
        return data;
    }

    public void setData(List<UserManage_data> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
