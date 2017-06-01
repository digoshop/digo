package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by apple02 on 16/7/21.
 */
public class Event_data implements Serializable{
    private long mnct;//列表创建时间
    private long mnid;//活动ID
    private String mnp;//活动图片url
    private long mnvsd;//活动开始时间
    private long mnved;//活动结束时间
    private String createtime;//列表创建时间
    private String starttime;//活动开始时间
    private String endttime;//活动结束时间
    private int mnas;//活动列表审核状态1 新建待审核 2更新待审核  3 新建审核失败 4 更新审核失败 5 通过  优惠券和活动这是统一的
    //mnas为 3 4 时可以直接删除 也可以编辑  mnas==5&&mnst==2可以删除 可以编辑      mnas==5&&mnst==1  只能禁用   不能删除和编辑
    private String mnc;//列表优惠活动内容
    private String mnti;//列表优惠活动标题
    private String mnad;//活动地点
    private String filepath;//图片文件路径
    private String mnst;
    private String area;
    private String city;
    private String show;
    private String mnafr;
    private int naid;

    public int getNaid() {
        return naid;
    }

    public Event_data setNaid(int naid) {
        this.naid = naid;
        return this;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getMnst() {
        return mnst;
    }

    public void setMnst(String mnst) {
        this.mnst = mnst;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getMnp() {
        return mnp;
    }

    public void setMnp(String mnp) {
        this.mnp = mnp;
    }

    public int getMnas() {
        return mnas;
    }

    public void setMnas(int mnas) {
        this.mnas = mnas;
    }

    public long getMnct() {
        return mnct;
    }

    public void setMnct(long mnct) {
        this.mnct = mnct;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMnti() {
        return mnti;
    }

    public void setMnti(String mnti) {
        this.mnti = mnti;
    }

    public long getMnid() {
        return mnid;
    }

    public void setMnid(long mnid) {
        this.mnid = mnid;
    }

    public String getCreateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createtime = sdf.format(getMnct() * 1000);
        return createtime;
    }

    public long getMnvsd() {
        return mnvsd;
    }

    public void setMnvsd(long mnvsd) {
        this.mnvsd = mnvsd;
    }

    public long getMnved() {
        return mnved;
    }

    public void setMnved(long mnved) {
        this.mnved = mnved;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStarttime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        starttime = sdf.format(getMnvsd() * 1000);

        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndttime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endttime = sdf.format(getMnved() * 1000);
        return endttime;
    }

    public void setEndttime(String endttime) {
        this.endttime = endttime;
    }

    public String getMnad() {
        return mnad;
    }

    public void setMnad(String mnad) {
        this.mnad = mnad;
    }

    public String getMnafr() {
        return mnafr;
    }

    public Event_data setMnafr(String mnafr) {
        this.mnafr = mnafr;
        return this;
    }
}
