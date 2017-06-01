package com.bjut.servicedog.servicedog.po;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apple02 on 16/7/22.
 */
public class Customization_data {
    private Customizationg_csDto csDto;
    private Customization_repDto repDto;
    private double cost;

    public Customizationg_csDto getCsDto() {
        return csDto;
    }
    public void setCsDto(Customizationg_csDto csDto) {
        this.csDto = csDto;
    }

    public Customization_repDto getRepDto() {
        return repDto;
    }

    public void setRepDto(Customization_repDto repDto) {
        this.repDto = repDto;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
