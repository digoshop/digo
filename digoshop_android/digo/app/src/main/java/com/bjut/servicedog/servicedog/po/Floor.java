package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/31.
 */
public class Floor {
    //楼层
    private String tag="";
    private String name="";
    private String merchantId="";
    private String number="";
    private String type="";
    private String mflid="";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMflid() {
        return mflid;
    }

    public void setMflid(String mflid) {
        this.mflid = mflid;
    }
}
