package com.bjut.servicedog.servicedog.model;

import java.util.List;

/**
 * Created by beibeizhu on 16/11/25.
 */

public class CouponStatisticsModel {


    /**
     * cost : 0.0010000000474974513
     * data : [{"use_ratio":"100.00%","not_used":0,"average":0,"issue_count":1,"name":"测试优惠券","down_ratio":"100.00%","down_count":1,"used":1,"couponBacthId":1000092,"sales":0}]
     * e : {"code":0,"provider":"communityunion","desc":""}
     */

    private double cost;
    private EBean e;
    private List<DataBean> data;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class EBean {
        /**
         * code : 0
         * provider : communityunion
         * desc :
         */

        private int code;
        private String provider;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class DataBean {
        /**
         * use_ratio : 100.00%
         * not_used : 0
         * average : 0
         * issue_count : 1
         * name : 测试优惠券
         * down_ratio : 100.00%
         * down_count : 1
         * used : 1
         * couponBacthId : 1000092
         * sales : 0
         */

        private String use_ratio;
        private String not_used;
        private String average;
        private String issue_count;
        private String name;
        private String down_ratio;
        private String down_count;
        private String used;
        private String couponBacthId;
        private String sales;

        public String getUse_ratio() {
            return use_ratio;
        }

        public void setUse_ratio(String use_ratio) {
            this.use_ratio = use_ratio;
        }

        public String getNot_used() {
            return not_used;
        }

        public void setNot_used(String not_used) {
            this.not_used = not_used;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getIssue_count() {
            return issue_count;
        }

        public void setIssue_count(String issue_count) {
            this.issue_count = issue_count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDown_ratio() {
            return down_ratio;
        }

        public void setDown_ratio(String down_ratio) {
            this.down_ratio = down_ratio;
        }

        public String getDown_count() {
            return down_count;
        }

        public void setDown_count(String down_count) {
            this.down_count = down_count;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getCouponBacthId() {
            return couponBacthId;
        }

        public void setCouponBacthId(String couponBacthId) {
            this.couponBacthId = couponBacthId;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }
    }
}
