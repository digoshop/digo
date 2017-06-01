package com.bjut.servicedog.servicedog.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by beibeizhu on 16/11/10.
 */

public class ChoiseExchangeModel {


    /**
     * cost : 0.0020000000949949026
     * data : [{"pna":"Picture Path Test","pat":["XL"],"pt":2,"epp":100,"sas":0,"ppi":"http://zksxapp.img-cn-beijing.aliyuncs.com/ios_shop/product/2016-11-06/2DBBBFB8-16F4-4153-8963-F06560F5BFD7.jpg","intg":1000,"pid":1000017,"uid":"5823f65a0cf2c62a5cfaabec","ct":1478752708,"ppr":199,"sad":0,"id":1000015,"epg":20}]
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

    public static class DataBean implements Serializable {
        /**
         * pna : Picture Path Test
         * pat : ["XL"]
         * pt : 2  1是竞拍  2是兑换
         * epp : 100
         * sas : 0
         * ppi : http://zksxapp.img-cn-beijing.aliyuncs.com/ios_shop/product/2016-11-06/2DBBBFB8-16F4-4153-8963-F06560F5BFD7.jpg
         * intg : 1000
         * pid : 1000017
         * uid : 5823f65a0cf2c62a5cfaabec
         * ct : 1478752708
         * ppr : 199
         * sad : 0
         * id : 1000015
         * epg : 20
         */

        private String pna;
        private int pt;
        private Double epp;
        private int sas;
        private String ppi;
        private int intg;
        private int pid;
        private String uid;
        private long ct;
        private Double ppr;
        private int sad;
        private int id;
        private int epg;
        private List<String> pat;

        public String getPna() {
            return pna;
        }

        public void setPna(String pna) {
            this.pna = pna;
        }

        public int getPt() {
            return pt;
        }

        public void setPt(int pt) {
            this.pt = pt;
        }

        public Double getEpp() {
            return epp;
        }

        public void setEpp(Double epp) {
            this.epp = epp;
        }

        public int getSas() {
            return sas;
        }

        public void setSas(int sas) {
            this.sas = sas;
        }

        public String getPpi() {
            return ppi;
        }

        public void setPpi(String ppi) {
            this.ppi = ppi;
        }

        public int getIntg() {
            return intg;
        }

        public void setIntg(int intg) {
            this.intg = intg;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public long getCt() {
            return ct;
        }

        public void setCt(long ct) {
            this.ct = ct;
        }

        public Double getPpr() {
            return ppr;
        }

        public void setPpr(Double ppr) {
            this.ppr = ppr;
        }

        public int getSad() {
            return sad;
        }

        public void setSad(int sad) {
            this.sad = sad;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getEpg() {
            return epg;
        }

        public void setEpg(int epg) {
            this.epg = epg;
        }

        public List<String> getPat() {
            return pat;
        }

        public void setPat(List<String> pat) {
            this.pat = pat;
        }
    }
}
