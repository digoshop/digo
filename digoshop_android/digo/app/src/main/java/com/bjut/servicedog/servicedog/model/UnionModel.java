package com.bjut.servicedog.servicedog.model;

import java.util.List;

/**
 * Created by beibeizhu on 17/5/4.
 */

public class UnionModel {

    /**
     * total : 1
     * cost : 0.003
     * data : [{"som":"010-8584821","scov":"http://img.digoshop.com/android_shop/store/2017-03-14/f9e84813-fd21-4d0f-933b-0831c0dd1c3f.jpg","sn":"刚毅体育","sid":1000059}]
     * e : {"code":0,"desc":"success"}
     */

    private int total;
    private double cost;
    private EBean e;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

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
         * desc : success
         */

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
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
         * som : 010-8584821
         * scov : http://img.digoshop.com/android_shop/store/2017-03-14/f9e84813-fd21-4d0f-933b-0831c0dd1c3f.jpg
         * sn : 刚毅体育
         * sid : 1000059
         */

        private String som;
        private String scov;
        private String sn;
        private String sid;

        public String getSom() {
            return som;
        }

        public void setSom(String som) {
            this.som = som;
        }

        public String getScov() {
            return scov;
        }

        public void setScov(String scov) {
            this.scov = scov;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }
}
