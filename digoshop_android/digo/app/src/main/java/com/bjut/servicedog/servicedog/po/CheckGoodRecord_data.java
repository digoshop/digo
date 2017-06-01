package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;

/**
 * Created by apple02 on 16/7/20.
 */
public class CheckGoodRecord_data {

    private Long exchange_id;
    private String  nick;
    private Long exchange_time;
    private String time;


    public Long getExchange_id() {
        return exchange_id;
    }

    public void setExchange_id(Long exchange_id) {
        this.exchange_id = exchange_id;
    }

    public String getNick() {

        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getExchange_time() {
        return exchange_time;
    }

    public String getExchangetime() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=format.format(exchange_time);
        return time;
    }

    public void setExchange_time(Long exchange_time) {
        this.exchange_time = exchange_time;
    }
}
