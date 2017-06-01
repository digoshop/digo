package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/15.
 */
public class q_SMS {
    private express e;
    private q_SMS_data q_sms_data;
    private Double cost;

    public express getE() {
        return e;
    }

    public void setE(express e) {
        this.e = e;
    }

    public q_SMS_data getQ_sms_data() {
        return q_sms_data;
    }

    public void setQ_sms_data(q_SMS_data q_sms_data) {
        this.q_sms_data = q_sms_data;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
