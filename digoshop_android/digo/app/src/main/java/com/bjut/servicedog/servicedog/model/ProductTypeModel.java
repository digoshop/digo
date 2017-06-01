package com.bjut.servicedog.servicedog.model;

import java.io.Serializable;

/**
 * Created by beibeizhu on 16/11/9.
 */

public class ProductTypeModel implements Serializable{

    private long id;
    private String name;
    private String money;

    public long getId() {
        return id;
    }

    public ProductTypeModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductTypeModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getMoney() {
        return money;
    }

    public ProductTypeModel setMoney(String money) {
        this.money = money;
        return this;
    }
}
