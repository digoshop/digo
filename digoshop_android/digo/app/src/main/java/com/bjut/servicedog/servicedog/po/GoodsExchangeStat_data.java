package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/27.
 */
public class GoodsExchangeStat_data {
    //    private String epn;//商品名称
//    private String imgurl;//商品图片
//    private int epr;//兑换价格
//    private int epg;//所需迪币
//    private int ppr;//商品原价
//    private String eped;//兑换结束时间
//    private int est;//兑换状态( 0-未开始  1兑换中 -1已结束)
//    private int exnum;//兑换数量
//    private int rnum;//领取数量
//    private int inum;//发行数量
//    private int amount;//原价金额
//    private int eamount;//兑换金额
//    private int rate;//折扣率
    private String pna;//商品名称
    private String ppi;//商品图片
    private int ppr;//商品原价
    private int pnu;//发行数量
    private int epp;//兑换价格
    private int epg;//所需迪币
    private String endDate;//兑换结束时间
    private int est;//兑换状态( 0-未开始  1兑换中 -1已结束)
    private int eps;//兑换状态( 0-未开始  1兑换中 -1已结束)
    private int ept;//兑换数量
    private int epsat;//领取数量
    private int amount;//原价金额
    private int eamount;//兑换金额
    private String rate;//折扣率

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public int getPpr() {
        return ppr;
    }

    public void setPpr(int ppr) {
        this.ppr = ppr;
    }

    public int getPnu() {
        return pnu;
    }

    public void setPnu(int pnu) {
        this.pnu = pnu;
    }

    public int getEpp() {
        return epp;
    }

    public void setEpp(int epp) {
        this.epp = epp;
    }

    public int getEpg() {
        return epg;
    }

    public void setEpg(int epg) {
        this.epg = epg;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getEst() {
        return est;
    }

    public void setEst(int est) {
        this.est = est;
    }

    public int getEpt() {
        return ept;
    }

    public void setEpt(int ept) {
        this.ept = ept;
    }

    public int getEpsat() {
        return epsat;
    }

    public void setEpsat(int epsat) {
        this.epsat = epsat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getEamount() {
        return eamount;
    }

    public void setEamount(int eamount) {
        this.eamount = eamount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getEps() {
        return eps;
    }

    public GoodsExchangeStat_data setEps(int eps) {
        this.eps = eps;
        return this;
    }
}
