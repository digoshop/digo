package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;

/**
 * Created by Xran on 16/7/20.
 */
public class RunKind implements Serializable {
    private String icon;
    private String name;
    private int pid;
    private long moid;
    private long update_time;
    private long create_time;
    private int status;
    private int level;
    private int isAll;//1是全部


    public int getIsAll() {
        return isAll;
    }

    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

    private int chosenow;//0未选中 1选中

    public int getChosenow() {
        return chosenow;
    }

    public void setChosenow(int chosenow) {
        this.chosenow = chosenow;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMoid() {
        return moid;
    }

    public void setMoid(long moid) {
        this.moid = moid;
    }
}
