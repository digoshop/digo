package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/22.
 */
public class OperateAnalyse_data {
    private String amount;//客单量
    private String shop_view;//商铺浏览量
    private String sales;//销售总量
    private String relation;//关注程度
    private String new_vip;//新增会员数量
    private String phone_cunt;//来电数量统计

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShop_view() {
        return shop_view;
    }

    public void setShop_view(String shop_view) {
        this.shop_view = shop_view;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNew_vip() {
        return new_vip;
    }

    public void setNew_vip(String new_vip) {
        this.new_vip = new_vip;
    }

    public String getPhone_cunt() {
        return phone_cunt;
    }

    public void setPhone_cunt(String phone_cunt) {
        this.phone_cunt = phone_cunt;
    }
}
