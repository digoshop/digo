package com.bjut.servicedog.servicedog.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by beibeizhu on 16/11/10.
 */

public class ChooseCouponModel implements Serializable{
    /**
     * cost : 0.003000000026077032
     * data : [{"couponInfo":{"cs":3,"categorys":[],"cbr":8,"cbved":1480435200,"cbvsd":1478275200,"couponId":1000014,"cbcf":500,"cbn":"斯莱德特惠","status":0},"couponType":{"ctid":1000001,"ctn":"折扣券"}}]
     * e : {"code":0,"provider":"communityunion","desc":""}
     * {"cost":0.0020000000949949026,
     * "data":[{"couponInfo":{"cs":3,"categorys":[],"cbr":0.5,"cbved":1480694400,"cbvsd":1479484800,"couponId":1000070,"cbcf":51,"cbn":"带我去","status":0},"couponType":{"ctid":1000001,"ctn":"折扣券"}}],
     * "e":{"code":0,"provider":"communityunion","desc":""}}
     */

    private double cost;
    private int total;
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

    public static class EBean implements Serializable{
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

    public static class DataBean implements Serializable{
        /**
         * couponInfo : {"cs":3,"categorys":[],"cbr":8,"cbved":1480435200,"cbvsd":1478275200,"couponId":1000014,"cbcf":500,"cbn":"斯莱德特惠","status":0}
         * couponType : {"ctid":1000001,"ctn":"折扣券"}
         */

        private CouponInfoBean couponInfo;
        private CouponTypeBean couponType;

        public CouponInfoBean getCouponInfo() {
            return couponInfo;
        }

        public void setCouponInfo(CouponInfoBean couponInfo) {
            this.couponInfo = couponInfo;
        }

        public CouponTypeBean getCouponType() {
            return couponType;
        }

        public void setCouponType(CouponTypeBean couponType) {
            this.couponType = couponType;
        }

        public static class CouponInfoBean implements Serializable{
            /**
             * cs : 3
             * categorys : []
             * cbr : 8
             * cbca : 100
             * cbved : 1480435200
             * cbvsd : 1478275200
             * couponId : 1000014
             * cbcf : 500
             * cbn : 斯莱德特惠
             * status : 0
             */

            private int cs;
            private float cbr;
            private int cbca;
            private long cbved;
            private long cbvsd;
            private int couponId;
            private int cbcf;
            private String cbn;
            private int status;
            private int signed;
            private int lack;
            private int top;
            private String cbi;
            private List<String> categorys;

            public String getCbi() {
                return cbi;
            }

            public void setCbi(String cbi) {
                this.cbi = cbi;
            }

            public int getSigned() {
                return signed;
            }

            public void setSigned(int signed) {
                this.signed = signed;
            }

            public int getLack() {
                return lack;
            }

            public void setLack(int lack) {
                this.lack = lack;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getCs() {
                return cs;
            }

            public void setCs(int cs) {
                this.cs = cs;
            }

            public float getCbr() {
                return cbr;
            }

            public void setCbca(int cbca) {
                this.cbca = cbca;
            }

            public int getCbca() {
                return cbca;
            }

            public void setCbr(float cbr) {
                this.cbr = cbr;
            }

            public long getCbved() {
                return cbved;
            }

            public void setCbved(long cbved) {
                this.cbved = cbved;
            }

            public long getCbvsd() {
                return cbvsd;
            }

            public void setCbvsd(long cbvsd) {
                this.cbvsd = cbvsd;
            }

            public int getCouponId() {
                return couponId;
            }

            public void setCouponId(int couponId) {
                this.couponId = couponId;
            }

            public int getCbcf() {
                return cbcf;
            }

            public void setCbcf(int cbcf) {
                this.cbcf = cbcf;
            }

            public String getCbn() {
                return cbn;
            }

            public void setCbn(String cbn) {
                this.cbn = cbn;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<String> getCategorys() {
                return categorys;
            }

            public void setCategorys(List<String> categorys) {
                this.categorys = categorys;
            }
        }

        public static class CouponTypeBean implements Serializable{
            /**
             * ctid : 1000001
             * ctn : 折扣券
             */

            private int ctid;
            private String ctn;

            public int getCtid() {
                return ctid;
            }

            public void setCtid(int ctid) {
                this.ctid = ctid;
            }

            public String getCtn() {
                return ctn;
            }

            public void setCtn(String ctn) {
                this.ctn = ctn;
            }
        }
    }
}
