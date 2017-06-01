package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apple02 on 16/9/2.
 */
public class Customizationg_csDto {
    private int id;
    private String tn;//定制标题
    private List<String> iurls=new ArrayList<>();//调取用户上传图片
    private String desc;//具体定制的要求
    private long et;//回复截止日期
    private int mc;//标记数
    private int rc;//回复数
    private long ct;//用户发表列表的时间
    private String cid;
    private String st;//-1已结束 0 已发布 1已回复
    private boolean isClick;
    private boolean isClicked=false;

    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getEtTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(getEt()*1000);
        String str=sdf.format(date);
        return str;
    }
    public String getCtTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(getCt()*1000);
        String str=sdf.format(date);
        return str;
    }

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public List<String> getIurls() {
        return iurls;
    }

    public void setIurls(List<String> iurls) {
        this.iurls = iurls;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getEt() {
        return et;
    }

    public void setEt(long et) {
        this.et = et;
    }

    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }
}
