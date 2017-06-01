package com.bjut.servicedog.servicedog.po;

import java.util.List;

/**
 * Created by apple02 on 16/7/28.
 */
public class Shopcomment_comments {
    private int total;//评论数量总数
    private String uid;
    private List<String> imgs;//评论上传的图片
    private int shop_id;//商铺ID
    private int expense_id;//
    private long create_time;//评论时间
    private int type;
    private String text;//评论内容
    private Shopcomment_userinfo userinfo;
    private String temp;
    private int mcid;
    private int success;//0代表成功
    private int status;
    private ReplyEntity reply;
    
    
    public class ReplyEntity{


        public long getMcid() {
            return mcid;
        }

        public void setMcid(long mcid) {
            this.mcid = mcid;
        }

        public long getReply_time() {
            return reply_time;
        }

        public void setReply_time(long reply_time) {
            this.reply_time = reply_time;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getRid() {
            return rid;
        }

        public void setRid(long rid) {
            this.rid = rid;
        }

        public long getSid() {
            return sid;
        }

        public void setSid(long sid) {
            this.sid = sid;
        }

        private long mcid;
        private long reply_time;
        private String text;
        private long rid;
        private long sid;


    }


    public ReplyEntity getReply() {
        return reply;
    }

    public void setReply(ReplyEntity reply) {
        this.reply = reply;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Shopcomment_userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Shopcomment_userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getMcid() {
        return mcid;
    }

    public void setMcid(int mcid) {
        this.mcid = mcid;
    }
}
