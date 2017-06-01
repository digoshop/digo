package com.bjut.servicedog.servicedog.po;

/**
 * Created by Xran on 16/7/22.
 */
public class VipLevel {
    private String vip_level_desc;
    private String vip_level_name;
    private long vip_level_id;
    private int ifchoose;

    public int getIfchoose() {
        return ifchoose;
    }

    public void setIfchoose(int ifchoose) {
        this.ifchoose = ifchoose;
    }

    public String getVip_level_desc() {
        return vip_level_desc;
    }

    public void setVip_level_desc(String vip_level_desc) {
        this.vip_level_desc = vip_level_desc;
    }

    public String getVip_level_name() {
        return vip_level_name;
    }

    public void setVip_level_name(String vip_level_name) {
        this.vip_level_name = vip_level_name;
    }

    public long getVip_level_id() {
        return vip_level_id;
    }

    public void setVip_level_id(long vip_level_id) {
        this.vip_level_id = vip_level_id;
    }
}
