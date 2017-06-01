package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/17.
 */
public class CreateShop {
    private express e;
    private CreateShop_data data;
    private Double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public CreateShop_data getData() {
        return data;
    }

    public void setData(CreateShop_data data) {
        this.data = data;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
