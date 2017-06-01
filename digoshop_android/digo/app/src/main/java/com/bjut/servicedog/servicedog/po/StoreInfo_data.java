package com.bjut.servicedog.servicedog.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xran on 16/8/22.
 */
public class StoreInfo_data {
    private String shop_cover;
    private List<Brand> brands;
    private String sid;
    private List<String> shop_album = new ArrayList<>();
    private String address;
    private List<RunKind> operates;
    private String description;
    private List<String> contact_phone;
    private String shop_name;
    private String open_time;
    private String shop_spr;
    private String nation;
    private String nid;
    private String shop_detail_address;

    public String getShop_detail_address() {
        return shop_detail_address;
    }

    public void setShop_detail_address(String shop_detail_address) {
        this.shop_detail_address = shop_detail_address;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }


    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }


    public String getShop_spr() {
        return shop_spr;
    }

    public void setShop_spr(String shop_spr) {
        this.shop_spr = shop_spr;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getShop_cover() {
        return shop_cover;
    }

    public void setShop_cover(String shop_cover) {
        this.shop_cover = shop_cover;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<String> getShop_album() {
        return shop_album;
    }

    public void setShop_album(List<String> shop_album) {
        this.shop_album = shop_album;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RunKind> getOperates() {
        return operates;
    }

    public void setOperates(List<RunKind> operates) {
        this.operates = operates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(List<String> contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }


}
