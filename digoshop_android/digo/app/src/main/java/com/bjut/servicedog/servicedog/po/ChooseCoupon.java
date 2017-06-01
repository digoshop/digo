package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by apple02 on 16/8/16.
 */
public class ChooseCoupon {
    private express e;
    private List<ChooseCoupon_data> data;
    private ChooseCoupon_info couponInfo;
    private double cost;

    public express getE() {
        return e;
    }
    public void setE(express e) {
        this.e = e;
    }

    public List<ChooseCoupon_data> getData() {
        return data;
    }

    public void setData(List<ChooseCoupon_data> data) {
        this.data = data;
    }

    public ChooseCoupon_info getCouponInfo() {
        return couponInfo;
    }
    public void setCouponInfo(ChooseCoupon_info couponInfo) {
        this.couponInfo = couponInfo;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
}
