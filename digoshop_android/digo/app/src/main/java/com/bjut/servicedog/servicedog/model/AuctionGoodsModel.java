package com.bjut.servicedog.servicedog.model;

import java.util.List;

/**
 * Created by beibeizhu on 16/11/6.
 */

public class AuctionGoodsModel {

    /**
     * cost : 0.001
     * data : {"auctionProductInfo":{"app":31,"pa":["http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-11-06/54E67C24-A5E6-4BA5-B924-14F5D227233C.jpg"],"pna":"测试名称","ppr":32,"apd":"竞拍规则","pd":"商品详情","pt":1,"aplg":30,"apg":10,"pattr":"属性","pid":1000011,"apsd":1478484000}}
     * e : {"code":0,"desc":""}
     */

    private double cost;
    /**
     * auctionProductInfo : {"app":31,"pa":["http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-11-06/54E67C24-A5E6-4BA5-B924-14F5D227233C.jpg"],"pna":"测试名称","ppr":32,"apd":"竞拍规则","pd":"商品详情","pt":1,"aplg":30,"apg":10,"pattr":"属性","pid":1000011,"apsd":1478484000}
     */

    private DataBean data;
    /**
     * code : 0
     * desc :
     */

    private EBean e;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public static class DataBean {
        /**
         * app : 31
         * pa : ["http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-11-06/54E67C24-A5E6-4BA5-B924-14F5D227233C.jpg"]
         * pna : 测试名称
         * ppr : 32
         * apd : 竞拍规则
         * pd : 商品详情
         * pt : 1
         * aplg : 30
         * apg : 10
         * pattr : 属性
         * pid : 1000011
         * apsd : 1478484000
         */

        private AuctionProductInfoBean auctionProductInfo;

        public AuctionProductInfoBean getAuctionProductInfo() {
            return auctionProductInfo;
        }

        public void setAuctionProductInfo(AuctionProductInfoBean auctionProductInfo) {
            this.auctionProductInfo = auctionProductInfo;
        }

        public static class AuctionProductInfoBean {
            private int app;
            private String pna;
            private int ppr;
            private String apd;
            private String pd;
            private int pt;
            private int aplg;
            private int apg;
            private String pattr;
            private int pid;
            private long apsd;
            private List<String> pa;

            public int getApp() {
                return app;
            }

            public void setApp(int app) {
                this.app = app;
            }

            public String getPna() {
                return pna;
            }

            public void setPna(String pna) {
                this.pna = pna;
            }

            public int getPpr() {
                return ppr;
            }

            public void setPpr(int ppr) {
                this.ppr = ppr;
            }

            public String getApd() {
                return apd;
            }

            public void setApd(String apd) {
                this.apd = apd;
            }

            public String getPd() {
                return pd;
            }

            public void setPd(String pd) {
                this.pd = pd;
            }

            public int getPt() {
                return pt;
            }

            public void setPt(int pt) {
                this.pt = pt;
            }

            public int getAplg() {
                return aplg;
            }

            public void setAplg(int aplg) {
                this.aplg = aplg;
            }

            public int getApg() {
                return apg;
            }

            public void setApg(int apg) {
                this.apg = apg;
            }

            public String getPattr() {
                return pattr;
            }

            public void setPattr(String pattr) {
                this.pattr = pattr;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public long getApsd() {
                return apsd;
            }

            public void setApsd(long apsd) {
                this.apsd = apsd;
            }

            public List<String> getPa() {
                return pa;
            }

            public void setPa(List<String> pa) {
                this.pa = pa;
            }

        }
    }

    public static class EBean {
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
}
