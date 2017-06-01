package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;

/**
 * Created by Xran on 16/7/26.
 */
public class Brand implements Serializable {
    private long brand_id;
    private String brand_name;
    private String brand_logo;
    private int chosenow=0;//0未选中1选中

    public int getChosenow() {
        return chosenow;
    }

    public void setChosenow(int chosenow) {
        this.chosenow = chosenow;
    }

    public long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(long brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }
}
