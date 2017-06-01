package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/25.
 */
public class DiscountCouponAna_data {
    private int down_count;//下载量,领取量
    private int issue_count;//发行量
    private String down_ratio;//下载比例
    private int sales;//销售总额
    private String use_ratio;//消费占比
    private int not_used;//未使用
    private int used;//已使用
    private int average;//客单价
    private String name;//优惠券的名称
    public int getDown_count() {
        return down_count;
    }
    public void setDown_count(int down_count) {
        this.down_count = down_count;
    }

    public int getIssue_count() {
        return issue_count;
    }

    public void setIssue_count(int issue_count) {
        this.issue_count = issue_count;
    }

    public String getDown_ratio() {
        return down_ratio;
    }

    public void setDown_ratio(String down_ratio) {
        this.down_ratio = down_ratio;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getUse_ratio() {
        return use_ratio;
    }

    public void setUse_ratio(String use_ratio) {
        this.use_ratio = use_ratio;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
