package com.bjut.servicedog.servicedog.po;

import android.graphics.Bitmap;

/**
 * Created by Xran on 16/7/13.
 */
public class Good {
    private String pd;//商品描述
    private String ppi;//商品图片
    private int epg;//兑换所需迪币
    private int pt;//商品类型
    private long pid;//商品id
    private String ppr;//商品价格
    private String epr;//兑换商品价格
    private String pna;//商品名称
    private String pafr;//驳回原因
    private Bitmap bm;
    private long eped;
    private String epp;
    private String app;
    private String aplg;
    private String show;
    private long apsd;
    private String eps;//兑换商品状态1审核中，2未通过，3兑换中，4已结束
    private String aps;//竞拍商品状态0未开始 1审核中，2未通过，3竞拍中，4已结束

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getPafr() {
        return pafr;
    }

    public Good setPafr(String pafr) {
        this.pafr = pafr;
        return this;
    }

    public String getApp() {
        return app;
    }

    public Good setApp(String app) {
        this.app = app;
        return this;
    }

    public String getAps() {
        return aps;
    }

    public void setAps(String aps) {
        this.aps = aps;
    }

    public String getApsStr() {
        String a = "";
        if (aps != null) {
            switch (aps) {
                case "1":
                    a = "审核中";
                    break;
                case "2":
                    a = "已驳回";
                    break;
                case "3":
                    a = "已通过";
                    break;
                case "4":
                    a = "未开始";
                    break;
                case "5":
                    a = "竞拍中";
                    break;
                case "6":
                    a = "已结束";
                    break;
                default:
                    a = "已禁用";
                    break;


            }
        }

        return a;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getEpsStr() {
        String a = "";
        if (eps != null) {
            switch (eps) {
                case "1":
                    a = "审核中";
                    break;
                case "2":
                    a = "已驳回";
                    break;
                case "3":
                    a = "已通过";
                    break;
                case "4":
                    a = "兑换中";
                    break;
                case "5":
                    a = "已结束";
                    break;
                case "8":
                    a = "未开始";
                    break;
                default:
                    a = "已禁用";
                    break;
            }
        }

        return a;
    }

    public long getApsd() {
        return apsd;
    }

    public void setApsd(long apsd) {
        this.apsd = apsd;
    }

    public String getAplg() {
        return aplg;
    }

    public void setAplg(String aplg) {
        this.aplg = aplg;
    }

    public String getEpp() {
        return epp;
    }

    public void setEpp(String epp) {
        this.epp = epp;
    }

    public long getEped() {
        return eped;
    }

    public void setEped(long eped) {
        this.eped = eped;
    }

    public String getEndTime() {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(eped * 1000));
        return date;
    }

    public String getApsdTime() {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(apsd * 1000));
        return date;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public int getEpg() {
        return epg;
    }

    public void setEpg(int epg) {
        this.epg = epg;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }

    public String getEpr() {
        return epr;
    }

    public void setEpr(String epr) {
        this.epr = epr;
    }

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}
