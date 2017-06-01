package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple02 on 16/7/14.
 */
public class Product implements Serializable {

//    productInfo		商品信息
//    pid		商品ID
//    pno		商品编码
//    pna		商品名称
//    pnu		商品数量
//    pt		商品类型(1竞拍2换购3普通)
//    pa		商品相册
//    ppr		商品价格
//    pppr		商品优惠价
//    pat		商品参数
//    pas		商品售后说明
//    pd		商品描述
//    bn		品牌名称
//    bid		品牌ID
//    mon		品类名称
//    moid		品类ID

    private String pd;
    private List<String> pat;
    private String epd;
    private List<String> pa;
    private int epg;
    private long eped;
    private String epp;

    private int pnu;
    private int pid;
    private int pt;
    private int pst;
    private int eps;
    private int aps;
    private int epm;
    private int enu;
    private int prcmd;//是否推荐
    private String pno;
    private String ppr;
    private String pppr;
    private String pas;
    private String bn;
    private String mon;
    private String ppi;
    private String moid;
    private String bid;
    private String pafr;
    private int epr;
    private String pna;
    private int plnu;
    private String aplg;
    private String app;
    private long apsd;
    private String apd;
    private String apg;
    private String pasa;
    private String pattr;
    private int limit;//是否限量
    private int epnu;//限量数量
    private long epsd;//兑换的开始时间

    public int getPrcmd() {
        return prcmd;
    }

    public void setPrcmd(int prcmd) {
        this.prcmd = prcmd;
    }

    public String getPafr() {
        return pafr;
    }

    public void setPafr(String pafr) {
        this.pafr = pafr;
    }

    public int getEnu() {
        return enu;
    }

    public void setEnu(int enu) {
        this.enu = enu;
    }

    public String getPasa() {
        return pasa;
    }

    public void setPasa(String pasa) {
        this.pasa = pasa;
    }

    public int getEpm() {
        return epm;
    }

    public void setEpm(int epm) {
        this.epm = epm;
    }

    public int getAps() {
        return aps;
    }

    public void setAps(int aps) {
        this.aps = aps;
    }

    public int getEps() {
        return eps;
    }

    public void setEps(int eps) {
        this.eps = eps;
    }

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getPst() {
        return pst;
    }

    public void setPst(int pst) {
        this.pst = pst;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public String getPno() {
        return pno;
    }

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPppr() {
        return pppr;
    }

    public void setPppr(String pppr) {
        this.pppr = pppr;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public int getLimit() {
        return limit;
    }

    public Product setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getEpnu() {
        return epnu;
    }

    public void setEpnu(int epnu) {
        this.epnu = epnu;
    }

    public long getEpsd() {
        return epsd;
    }

    public void setEpsd(long epsd) {
        this.epsd = epsd;
    }

    public int getPnu() {
        return pnu;
    }

    public void setPnu(int pnu) {
        this.pnu = pnu;
    }

    public String getPattr() {
        return pattr;
    }

    public Product setPattr(String pattr) {
        this.pattr = pattr;
        return this;
    }

    public Product setEped(long eped) {
        this.eped = eped;
        return this;
    }

    public String getEpp() {
        return epp;
    }

    public Product setEpp(String epp) {
        this.epp = epp;
        return this;
    }

    public int getPlnu() {
        return plnu;
    }

    public Product setPlnu(int plnu) {
        this.plnu = plnu;
        return this;
    }

    public String getApg() {
        return apg;
    }

    public void setApg(String apg) {
        this.apg = apg;
    }

    public String getApd() {
        return apd;
    }

    public void setApd(String apd) {
        this.apd = apd;
    }

    public long getApsd() {
        return apsd;
    }

    public void setApsd(long apsd) {
        this.apsd = apsd;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAplg() {
        return aplg;
    }

    public void setAplg(String aplg) {
        this.aplg = aplg;
    }

    public int getEplu() {
        return plnu;
    }

    public void setEplu(int eplu) {
        this.plnu = eplu;
    }

    public List<String> getPa() {
        return pa;
    }

    public void setPa(List<String> pa) {
        this.pa = pa;
    }

    public long getEped() {
        return eped;
    }

    public String getEndTime() {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(eped * 1000));
        return date;
    }

    public String getApsdTime() {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(apsd * 1000));
        return date;
    }

    public void setEped(int eped) {
        this.eped = eped;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public List<String> getPat() {
        return pat;
    }

    public void setPat(List<String> pat) {
        this.pat = pat;
    }

    public String getEpd() {
        return epd;
    }

    public void setEpd(String epd) {
        this.epd = epd;
    }


    public int getEpg() {
        return epg;
    }

    public void setEpg(int epg) {
        this.epg = epg;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }

    public int getEpr() {
        return epr;
    }

    public void setEpr(int epr) {
        this.epr = epr;
    }

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }
}
