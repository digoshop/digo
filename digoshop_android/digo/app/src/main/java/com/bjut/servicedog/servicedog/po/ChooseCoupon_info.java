package com.bjut.servicedog.servicedog.po;

import com.bjut.servicedog.servicedog.utils.DateUtil;

/**
 * Created by apple02 on 16/8/16.
 */
public class ChooseCoupon_info {
    String[] categorys;
    private int status;//标记优惠券可用还是不可用
    private int cbca;//面额
    private int cbcf;//满多少可用
    private String cbn;//优惠券的主题
    private int couponId;//优惠券ID
    private String cbi;//优惠券的图片
    private String cbr;
    private int cs;
    private long cbvsd;
    private long cbved;
    private String startTimestr;
    private String endTimestr;
    private long cbct;
    private String createTimestr;

    public String getCreateTimestr() {
        return DateUtil.formatDate(cbct * 1000L);
    }

    public void setCreateTimestr(String createTimestr) {
        this.createTimestr = createTimestr;
    }

    public String getStartTimestr() {
        return DateUtil.formatDate(cbvsd * 1000L);
    }

    public void setStartTimestr(String startTimestr) {
        this.startTimestr = startTimestr;
    }

    public String getEndTimestr() {
        return DateUtil.formatDate(cbved * 1000L);
    }

    public void setEndTimestr(String endTimestr) {
        this.endTimestr = endTimestr;
    }

    public int getCs() {
        return cs;
    }

    public void setCs(int cs) {
        this.cs = cs;
    }

    public String getCategoryStr() {
        String str = "";
        if (categorys.length == 0) {
            str = "全场通用";
        } else {
            for (int i = 0; i < categorys.length; i++) {
                str += categorys[i] + ",";
                str = str.substring(0, str.length() - 1);
            }
            if (str.length() >= 12) {
                str.substring(0, 12);
                str += str + "...";
            } else {
                str.substring(0, str.length() - 2);
            }
        }

        return str;
    }

    public String getCbr() {
        return cbr;
    }

    public void setCbr(String cbr) {
        this.cbr = cbr;
    }

    public String getCbrStr() {
        Double a = Double.parseDouble(cbr) * 10;
        return a.toString();
    }

    public String[] getCategorys() {
        return categorys;
    }

    public void setCategorys(String[] categorys) {
        this.categorys = categorys;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCbca() {
        return cbca;
    }

    public void setCbca(int cbca) {
        this.cbca = cbca;
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

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCbi() {
        return cbi;
    }

    public void setCbi(String cbi) {
        this.cbi = cbi;
    }
}
