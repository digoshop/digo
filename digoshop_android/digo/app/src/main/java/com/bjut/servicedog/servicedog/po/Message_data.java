package com.bjut.servicedog.servicedog.po;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple02 on 16/7/23.
 */
public class Message_data implements Serializable {
    private String content;
    private String sid;//店铺ID
    private String title;//消息标题
    private long create_time;//消息创建时间
    private int notice_id;//消息id
    private int type;//类型
    private List<String>targets;//会员级别



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }
}
