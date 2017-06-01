package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/18.
 */
public class Coupon {
    private CouponType couponType;
    private CouponBatch couponBatch;
    private CouponBatch couponInfo;
    private Shop shop;
    private int couponStatus;
    private int down_count;
    private String down_percentage;
    private int issue_count;
    private int not_used;
    private int used;
    private int average;
    private String consumption_ratio;
    private int spending;


    public CouponBatch getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CouponBatch couponInfo) {
        this.couponInfo = couponInfo;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(int couponStatus) {
        this.couponStatus = couponStatus;
    }

    public int getDown_count() {
        return down_count;
    }

    public void setDown_count(int down_count) {
        this.down_count = down_count;
    }

    public String getDown_percentage() {
        return down_percentage;
    }

    public void setDown_percentage(String down_percentage) {
        this.down_percentage = down_percentage;
    }

    public int getIssue_count() {
        return issue_count;
    }

    public void setIssue_count(int issue_count) {
        this.issue_count = issue_count;
    }

    public int getNot_used() {
        return not_used;
    }

    public void setNot_used(int not_used) {
        this.not_used = not_used;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getConsumption_ratio() {
        return consumption_ratio;
    }

    public void setConsumption_ratio(String consumption_ratio) {
        this.consumption_ratio = consumption_ratio;
    }

    public int getSpending() {
        return spending;
    }

    public void setSpending(int spending) {
        this.spending = spending;
    }

    public CouponType getCouponType() {
        return couponType;
    }
    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }
    public CouponBatch getCouponBatch() {
        return couponBatch;
    }

    public void setCouponBatch(CouponBatch couponBatch) {
        this.couponBatch = couponBatch;
    }
}
