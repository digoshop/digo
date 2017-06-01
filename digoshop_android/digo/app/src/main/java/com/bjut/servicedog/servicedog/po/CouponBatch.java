package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by Xran on 16/7/18.
 */
public class CouponBatch {
    private String cbnu;//该批次优惠券数量
    private Long cbved;//结束时间
    private String cbca;//该批次优惠券低用金额
    private Long cbvsd;//优惠券有效期开始时间
    private int cbid;//批次ID
    private String cbcf;//该批次优惠券满减额度
    private String cbch;// 优惠券批次折扣最高抵用
    private String cbr;// 优惠券批次折扣
    private Long cbct;// 创建时间,uinix时间
    private int cbas;// 状态1 未审核 2 未通过 3已发布 4已过期
    private String[] categorys;//适用类型
    private String cbi;//卡券图片展示
    private String cbn;
    private String starTime;
    private String endTime;
    private String creatTime;
    private String cbafr;
    private int cbiec;
    private int ceba;
    private long couponId;
    private int cbs;//1 删除 2禁用 99删除
    private String cbd;
    private int cbea;
    private int status;//0未过期 1已过期
    private int show;
    private int rule;
    private int cbrm;//是否每日限量  1否 2是
    private String cbrnu;//每日限量
    private int cbrt;//领取方式  1正常领取 2消费赠送
    private String cbcpa;//消费满多少元赠

    public int getCbrt() {
        return cbrt;
    }

    public void setCbrt(int cbrt) {
        this.cbrt = cbrt;
    }

    public String getCbcpa() {
        return cbcpa;
    }

    public void setCbcpa(String cbcpa) {
        this.cbcpa = cbcpa;
    }

    public int getCbrm() {
        return cbrm;
    }

    public void setCbrm(int cbrm) {
        this.cbrm = cbrm;
    }

    public String getCbrnu() {
        return cbrnu;
    }

    public void setCbrnu(String cbrnu) {
        this.cbrnu = cbrnu;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }


    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getCbea() {
        return cbea;
    }

    public void setCbea(int cbea) {
        this.cbea = cbea;
    }


    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCbd() {
        return cbd;
    }

    public void setCbd(String cbd) {
        this.cbd = cbd;
    }

    public int getCbiec() {
        return cbiec;
    }

    public void setCbiec(int cbiec) {
        this.cbiec = cbiec;
    }

    public int getCeba() {
        return ceba;
    }

    public void setCeba(int ceba) {
        this.ceba = ceba;
    }

    public String getCbn() {
        return cbn;
    }

    public void setCbn(String cbn) {
        this.cbn = cbn;
    }

    public String getCbafr() {
        return cbafr;
    }

    public CouponBatch setCbafr(String cbafr) {
        this.cbafr = cbafr;
        return this;
    }

    public String[] getCategorys() {
        return categorys;
    }

    public String getCategoryStr() {
        String str = "";
        if (categorys.length == 0 || categorys == null) {
            str = "全场通用";
        } else {

            for (String category : categorys) {
                str += category + " ";
            }
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public String getCategoryStrDun() {
        String str = "";
        if (categorys.length == 0) {
            str = "全场通用";
        } else {

            for (String category : categorys) {
                str += category + "、";
            }
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public void setCategorys(String[] categorys) {
        this.categorys = categorys;
    }

    public String getCbcf() {
        return cbcf;
    }

    public void setCbcf(String cbcf) {
        this.cbcf = cbcf;
    }

    public String getCbch() {
        return cbch;
    }

    public void setCbch(String cbch) {
        this.cbch = cbch;
    }

    public String getCbr() {
        return cbr;
    }

    public String getCbrStr() {
        if (cbr != null && !"".equals(cbr)) {
            Double a = Double.parseDouble(cbr) * 10;
            String cbrStr = a.toString();
            if (cbrStr.length() > 3) {
                cbrStr = cbrStr.substring(0, 3);
            }
            return cbrStr;
        } else {
            return "0.0";
        }
    }

    public void setCbr(String cbr) {
        this.cbr = cbr;
    }

    public String getCbnu() {
        return cbnu;
    }

    public void setCbnu(String cbnu) {
        this.cbnu = cbnu;
    }


    public String getCbca() {
        return cbca;
    }

    public void setCbca(String cbca) {
        this.cbca = cbca;
    }

    public Long getCbved() {
        return cbved;
    }

    public void setCbved(Long cbved) {
        this.cbved = cbved;
    }

    public Long getCbvsd() {
        return cbvsd;
    }

    public void setCbvsd(Long cbvsd) {
        this.cbvsd = cbvsd;
    }

    public Long getCbct() {
        return cbct;
    }

    public void setCbct(Long cbct) {
        this.cbct = cbct;
    }

    public int getCbas() {
        return cbas;
    }

    public void setCbas(int cbas) {
        this.cbas = cbas;
    }

    public int getCbid() {
        return cbid;
    }

    public void setCbid(int cbid) {
        this.cbid = cbid;
    }

    public String getStarTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        starTime = format.format(cbvsd * 1000);
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = format.format(cbved * 1000);
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        creatTime = format.format(cbct * 1000);
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCbi() {
        return cbi;
    }

    public void setCbi(String cbi) {
        this.cbi = cbi;
    }

    public int getCbs() {
        return cbs;
    }

    public void setCbs(int cbs) {
        this.cbs = cbs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
