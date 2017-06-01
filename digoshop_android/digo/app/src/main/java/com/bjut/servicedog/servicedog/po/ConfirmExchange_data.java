package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/8/2.
 */
public class ConfirmExchange_data {
    private long id;//礼品id
    private String no;//订单号
    private long pid;//商品id
    private String pna;//商品名称
    private String pat;//商品属性
    private double ppr;//市场价格
    private double epr;//兑换价格(实际结算)
    private int epg;//兑换所需迪币
    private int intg;//积分
    private int pt;//类型(1竞拍 2换购  3销售)
    private long ct;//兑换时间

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    public String getPat() {
        return pat;
    }

    public void setPat(String pat) {
        this.pat = pat;
    }

    public double getPpr() {
        return ppr;
    }

    public void setPpr(double ppr) {
        this.ppr = ppr;
    }

    public double getEpr() {
        return epr;
    }

    public void setEpr(double epr) {
        this.epr = epr;
    }

    public int getEpg() {
        return epg;
    }

    public void setEpg(int epg) {
        this.epg = epg;
    }
    public int getIntg() {
        return intg;
    }
    public void setIntg(int intg) {
        this.intg = intg;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
