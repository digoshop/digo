package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/27.
 */
public class LimitTimeAuction_data {
//    private String atn;//竞拍商品名称
//    private String ai;//商品图片
//    private int ap;//竞拍价
//    private int alp;//商品原价
//    private int ahp;//竞拍成交价
//    private String at;//兑换结束时间
//    private int ast;//竞拍状态( 0-未开始  1-竞拍中 -1已结束)
//    private int bronum;//查看人数
//    private int aucnum;//竞拍人数
//    private int inum;//发行数量
//    private String nk;
    private String pna;//商品名称
    private String ppi;//商品图片
    private int ppr;//商品原价
    private int app;//最终交易价格
    private int aplg;//竞拍底价
    private int aphg;//竞拍成交价
    private String startDate;//竞拍开始时间
    private long apsd;//竞拍开始时间
    private int ast;//竞拍状态( 0-未开始  1-竞拍中 -1已结束)
    private int bc;//查看人数
    private int ac;//竞拍人数
    private String nk;//昵称
    private int aps;

    public int getAps() {
        return aps;
    }

    public LimitTimeAuction_data setAps(int aps) {
        this.aps = aps;
        return this;
    }

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

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    public int getAplg() {
        return aplg;
    }

    public void setAplg(int aplg) {
        this.aplg = aplg;
    }

    public int getAphg() {
        return aphg;
    }

    public void setAphg(int aphg) {
        this.aphg = aphg;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getBc() {
        return bc;
    }

    public void setBc(int bc) {
        this.bc = bc;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }

    public long getApsd() {
        return apsd;
    }

    public LimitTimeAuction_data setApsd(long apsd) {
        this.apsd = apsd;
        return this;
    }
}
