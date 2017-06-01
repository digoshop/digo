package com.bjut.servicedog.servicedog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.OperateAnalyse;
import com.bjut.servicedog.servicedog.ui.analyse.DiscountCouponUseAnalyseActivity;
import com.bjut.servicedog.servicedog.ui.analyse.ExchangeAnalyseActivity;
import com.bjut.servicedog.servicedog.ui.analyse.SaleStatisticsAnalyseActivity;
import com.bjut.servicedog.servicedog.ui.analyse.ShopCommentActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;


public class OperationAnalyseActivity_f extends BaseActivity implements View.OnClickListener {
    private TextView Chose_month;

    private RelativeLayout discountuse;
    private RelativeLayout goodsexchange;
    private RelativeLayout limittime;
    private RelativeLayout eventbrowse;
    private RelativeLayout usercomment;
    private RelativeLayout storebrowse;
    private RelativeLayout consumestatics;
    private LinearLayout ll_month;
    private TextView chooseMonth;
    private TextView tv_title;
    private int year, month, day, hours, minute, second;
    private Calendar c;
    private TextView phone_count, shop_view, relation, salecount, ordercount, newvip_number;

    private String mt = "";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_analyse_activity_f);
        init();
    }

    private void init() {
        phone_count = (TextView) findViewById(R.id.phone_count);
        tv_title = (TextView) findViewById(R.id.tv_title);
        shop_view = (TextView) findViewById(R.id.shop_view_count);
        relation = (TextView) findViewById(R.id.relation_count);
        salecount = (TextView) findViewById(R.id.sale_count);
        ordercount = (TextView) findViewById(R.id.order_count);
        newvip_number = (TextView) findViewById(R.id.new_add_vip_number);
        discountuse = (RelativeLayout) findViewById(R.id.rel_discount_coupon);
        goodsexchange = (RelativeLayout) findViewById(R.id.rel_goods_exchange_statics);
        limittime = (RelativeLayout) findViewById(R.id.rel_limit_time);
        usercomment = (RelativeLayout) findViewById(R.id.user_comment);
        consumestatics = (RelativeLayout) findViewById(R.id.rel_sale_statics_analyse);
        chooseMonth = (TextView) findViewById(R.id.tv_month);
        ll_month = (LinearLayout) findViewById(R.id.ll_month);

        tv_title.setText(getString(R.string.title_jyfx));

        discountuse.setOnClickListener(this);
        goodsexchange.setOnClickListener(this);
        limittime.setOnClickListener(this);
        usercomment.setOnClickListener(this);
        consumestatics.setOnClickListener(this);
        ll_month.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = new Date(System.currentTimeMillis());
        mt = sdf.format(date);

        c = Calendar.getInstance();
        //取得日历的信息
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("M");
        String str = sdf1.format(date1);
        chooseMonth.setText(str);
        requestOperatedata(mt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_discount_coupon:
                mIntent = new Intent(this, DiscountCouponUseAnalyseActivity.class);
                mIntent.putExtra("mt", mt);
                startActivity(mIntent);
                break;
            case R.id.rel_goods_exchange_statics:
                mIntent = new Intent(this, ExchangeAnalyseActivity.class);
                mIntent.putExtra("mt", mt);
                startActivity(mIntent);
                break;
//            case R.id.rel_limit_time:
//                mIntent = new Intent(this, LimitTimeAuctionAnalyseActivity_f.class);
//                mIntent.putExtra("mt", mt);
//                startActivity(mIntent);
//                break;
            case R.id.user_comment:
                mIntent = new Intent(this, ShopCommentActivity.class);
                mIntent.putExtra("mt", mt);
                startActivity(mIntent);
                break;
            case R.id.rel_sale_statics_analyse:
                mIntent = new Intent(this, SaleStatisticsAnalyseActivity.class);
                mIntent.putExtra("mt", mt);
                startActivity(mIntent);
                break;
            case R.id.ll_month:
                DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH);
//                picker.setRangeStart(2016, 8);//开始范围
//                picker.setRangeEnd(year, month);//结束范围
                picker.setRangeStart(2010, 1, 1);
                picker.setRangeEnd(2100,12,31);
                picker.setFillScreen(false);
                picker.setSelectedItem(year, month);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        chooseMonth.setText(month);
                        mt = year + month;
                        requestOperatedata(mt);
                    }

                });
                picker.show();
                break;
        }
    }

    private void requestOperatedata(String time) {
        showProgressDialog();
        String urlString = "business/merchant_stat.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("month", time);
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            OperateAnalyse analyse = JSON.parseObject(json, OperateAnalyse.class);
                            if (analyse.getE().getCode() == 0) {
                                setView(analyse, true);
                            } else {
                                setView(analyse, false);
                                toast(analyse.getE().getDesc());
                            }
                        } catch (Exception e) {
                            setView(null, false);
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void setView(OperateAnalyse analyse, boolean isHasData) {
        phone_count = (TextView) findViewById(R.id.phone_count);
        shop_view = (TextView) findViewById(R.id.shop_view_count);
        relation = (TextView) findViewById(R.id.relation_count);
        salecount = (TextView) findViewById(R.id.sale_count);
        ordercount = (TextView) findViewById(R.id.order_count);
        newvip_number = (TextView) findViewById(R.id.new_add_vip_number);
        try {

            if (isHasData) {
                phone_count.setText(analyse.getData().getPhone_cunt());
                shop_view.setText(analyse.getData().getShop_view());
                relation.setText(analyse.getData().getRelation());
                salecount.setText(analyse.getData().getAmount());
                ordercount.setText(analyse.getData().getSales());
                newvip_number.setText(analyse.getData().getNew_vip());
            } else {
                phone_count.setText("0");
                shop_view.setText("0");
                relation.setText("0");
                salecount.setText("0");
                ordercount.setText("0");
                newvip_number.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
