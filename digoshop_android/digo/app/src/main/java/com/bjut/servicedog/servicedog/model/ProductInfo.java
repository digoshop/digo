package com.bjut.servicedog.servicedog.model;

import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.po.express;

import java.io.Serializable;
import java.util.List;

/**
 * Created by beibeizhu on 17/2/16.
 */

public class ProductInfo implements Serializable {

    private express e;
    private double cost;
    private List<Product> data;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
