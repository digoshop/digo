package com.bjut.servicedog.servicedog.model;

import java.util.List;

/**
 * Created by zzr on 16/11/3.
 */
public class Province {


    /**
     * total : 0
     * cost : 0.0020
     * data : [{"nid":3925,"name":"天津市","tag":0},{"nid":625,"name":"吉林省","tag":0},{"nid":3063,"name":"陕西省","tag":0},{"nid":3924,"name":"北京市","tag":0}]
     * e : {"code":0,"desc":""}
     */

    private int total;
    private double cost;
    /**
     * code : 0
     * desc :
     */

    private EEntity e;
    /**
     * nid : 3925
     * name : 天津市
     * tag : 0
     */

    private List<DataEntity> data;

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

    public EEntity getE() {
        return e;
    }

    public void setE(EEntity e) {
        this.e = e;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class EEntity {
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

    public static class DataEntity {
        private int nid;
        private String name;
        private int tag;

        public int getNid() {
            return nid;
        }

        public void setNid(int nid) {
            this.nid = nid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }
    }
}
