package com.bjut.servicedog.servicedog.ui.settlement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ProductTypeNoClickAdapter;
import com.bjut.servicedog.servicedog.event.CloseEvent;
import com.bjut.servicedog.servicedog.model.ChooseCouponModel;
import com.bjut.servicedog.servicedog.model.ProductTypeModel;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.ChoseCouponActivity;
import com.bjut.servicedog.servicedog.utils.ArithUtil;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.NumUtil;
import com.bjut.servicedog.servicedog.view.MyGridView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/1/12.
 */

public class SettlementActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView consume_user_nick;
    private TextView consume_user_phone;
    private TextView consume_user_kind;
    private TextView consume_user_date;
    private TextView payment;
    private TextView reduce;
    private TextView grade;
    private TextView tv_money;
    private TextView tv_coupon_size;
    private TextView couponkind;
    private TextView tv_cancel;
    private TextView title;
    private TextView coupondesc;
    private TextView submit;
    private TextView title_bi;
    private MyGridView mGridView;
    private LinearLayout chosecoupon;
    private LinearLayout usecoupon;
    private LinearLayout zhekou;

    private List<ProductTypeModel> mProductTypeModelList;
    private ProductTypeNoClickAdapter mProductTypeAdapter;
    private List<ChooseCouponModel.DataBean> mDataBeen = new ArrayList<>();
    private List<ChooseCouponModel.DataBean> couponList = new ArrayList<>();

    private String userId = "";
    private String userName = "";
    private String userPhone = "";
    private String userVip = "";
    private String text = "";
    private String cid = "";
    private Double actualMoney = 0.0;
    private int total = 0;
    private Gson mGson;
    private ChooseCouponModel.DataBean mastDataBean = null;
    private boolean isCoupon = false;
    private boolean isNot = false;

    private final int GET_OK = 0;
    private final int GET_NULL = 1;
    private final int SET_CONSUME = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_OK:
                    if (total > 0) {
                        tv_coupon_size.setText(total + "张可用");
                    } else {
                        tv_coupon_size.setText("暂无可用");
                    }
                    compute(mastDataBean);
                    closeProgressDialog();
                    break;
                case GET_NULL:
                    tv_coupon_size.setText("暂无可用");
                    isCoupon  = true;
                    setActualMoney(actualMoney, 0, true, "0");
                    closeProgressDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        initViews();
        setListener();
        requestCouponList();
    }

    private void setListener() {
        chosecoupon.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initViews() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        consume_user_nick = (TextView) findViewById(R.id.consume_user_nick);
        consume_user_phone = (TextView) findViewById(R.id.consume_user_phone);
        consume_user_kind = (TextView) findViewById(R.id.consume_user_kind);
        consume_user_date = (TextView) findViewById(R.id.consume_user_date);
        couponkind = (TextView) findViewById(R.id.couponkind);
        coupondesc = (TextView) findViewById(R.id.coupondesc);
        payment = (TextView) findViewById(R.id.payment);
        reduce = (TextView) findViewById(R.id.reduce);
        grade = (TextView) findViewById(R.id.grade);
        tv_money = (TextView) findViewById(R.id.tv_money);
        title = (TextView) findViewById(R.id.title);
        tv_coupon_size = (TextView) findViewById(R.id.tv_coupon_size);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        submit = (TextView) findViewById(R.id.submit);
        title_bi = (TextView) findViewById(R.id.title_bi);
        mGridView = (MyGridView) findViewById(R.id.gridview);
        chosecoupon = (LinearLayout) findViewById(R.id.chosecoupon);
        usecoupon = (LinearLayout) findViewById(R.id.usecoupon);
        zhekou = (LinearLayout) findViewById(R.id.zhekou);

        TextPaint tp_nick = consume_user_nick.getPaint();
        tp_nick.setFakeBoldText(true);
        TextPaint tp_phone = consume_user_phone.getPaint();
        tp_phone.setFakeBoldText(true);
        TextPaint tp_money = tv_money.getPaint();
        tp_money.setFakeBoldText(true);

        mGson = new Gson();
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userPhone = getIntent().getStringExtra("userPhone");
        userVip = getIntent().getStringExtra("userVip");
        mProductTypeModelList = (List<ProductTypeModel>) getIntent().getSerializableExtra("dataList");
        String substring = userPhone.substring(0, 3) + "****" + userPhone.substring(7, 11);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);

        mTitle.setText("订单结算");
        consume_user_nick.setText(userName);
        consume_user_phone.setText(substring);
        consume_user_kind.setText(userVip);
        mProductTypeAdapter = new ProductTypeNoClickAdapter(this);
        mGridView.setAdapter(mProductTypeAdapter);
        mProductTypeAdapter.setList(mProductTypeModelList);
        actualMoney = mProductTypeAdapter.getMoney();
        String actualMoneyStr = NumUtil.Number2Double(actualMoney);
        tv_money.setText(actualMoneyStr + "");

        consume_user_date.setText(str);
//        setTotalMoney();
    }

    private void setActualMoney(double money, double province, boolean isIntegral, String bi) {
        String moneyStr = NumUtil.Number2Double(money);
        String provinceStr = NumUtil.Number2Double(province);
        payment.setText(moneyStr + "");
        reduce.setText("(已节省 " + provinceStr + " 元)");
        if (isIntegral) {
            requestjifen(money);
        } else {
            title_bi.setText("本次扣币");
            grade.setText(bi);
        }
    }

    private void requestjifen(Double actualMoney) {
        //showProgressDialog();
        String urlString = "pay/getintg.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("amount", actualMoney + "");//传实际结算的金额
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                String jifen = "";
                                jifen = j.getString("data");
                                title_bi.setText("本次积币");
                                grade.setText(jifen);
                            } else {
                                toast("结算失败,请核对数据");
                            }
                        } catch (Exception e) {
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

    public void requestCouponList() {
        showProgressDialog();
        text = getjsonArray();
        String urlString = "coupon/user_coupon.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));

        map.put("text", text);
//        map.put("page", page + "");
        map.put("page_length", "30");
        params = sortMapByKey(map);
        params.addBodyParameter("uid", userId);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                try {
                    System.out.println(responseInfo.result);
                    String json = null;
                    ChooseCouponModel chooseCoupon = null;
                    json = responseInfo.result;
                    chooseCoupon = mGson.fromJson(json, ChooseCouponModel.class);
                    if (chooseCoupon.getE().getCode() == 0) {
                        total = chooseCoupon.getTotal();
                        List<ChooseCouponModel.DataBean> data = chooseCoupon.getData();// 填充数据
                        if (data.size() > 0) {
                            mDataBeen.addAll(data);
                            for (ChooseCouponModel.DataBean dataBean : mDataBeen) {
                                if (dataBean.getCouponInfo().getTop() == 1) {
                                    mastDataBean = dataBean;
                                }
                                if (dataBean.getCouponInfo().getSigned() == 0) {
                                    couponList.add(dataBean);
                                }
                            }
                            mHandler.sendEmptyMessage(GET_OK);
                        } else {
                            mHandler.sendEmptyMessage(GET_NULL);
                        }
                    } else {
                        mHandler.sendEmptyMessage(GET_NULL);
                    }
                } catch (JsonSyntaxException e) {
                    mHandler.sendEmptyMessage(GET_NULL);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    //结算提交的接口
    private void requestSale() {
        showProgressDialog();
        String urlString = "pay/expense.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        if (isCoupon) {
            if (!isNot) {
                map.put("cid", cid);
            }
//            map.put("cid", cid);//优惠券ID
            map.put("signed", "0");
        } else {
            map.put("signed", "1");
        }
//        map.put("orden_sn", order_sn);//订单编号
        map.put("sid", pref.getString("sid", ""));//
        map.put("payment", tv_money.getText().toString());//消费价格
        map.put("amount", payment.getText().toString());//实际价格

        map.put("opuid", pref.getString("uid", ""));//操作人ID
        map.put("mopid", savemopid() + "");//品类ID
        map.put("intg", grade.getText().toString());//品类ID

        RequestParams params = sortMapByKey(map);
        params.addBodyParameter("uid", userId);//用户id


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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                EventBus.getDefault().post(new CloseEvent(true));
                                toast("结算成功");
                                finish();
                            } else {
                                toast("结算失败");
                            }
                        } catch (Exception e) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chosecoupon:
                if (total > 0) {
                    Intent intent = new Intent(this, ChoseCouponActivity.class);
                    intent.putExtra("data", (Serializable) couponList);
                    startActivityForResult(intent, SET_CONSUME);
                } else {
                    return;
                }

                break;
            case R.id.tv_cancel:
                if (mastDataBean != null) {
                    compute(mastDataBean);
                } else {
                    usecoupon.setVisibility(View.GONE);
                    cid = "";
                    setTotalMoney();
                }
                break;
            case R.id.submit:
                requestSale();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SET_CONSUME:
                ChooseCouponModel.DataBean couponInfo = (ChooseCouponModel.DataBean) data.getSerializableExtra("couponInfo");
                compute(couponInfo);
                break;
        }
    }

    /**
     * 使用优惠的计算
     *
     * @param couponInfo 优惠券的信息
     */
    private void compute(ChooseCouponModel.DataBean couponInfo) {
        isNot = false;

        cid = couponInfo.getCouponInfo().getCouponId() + "";
        String type = couponInfo.getCouponType().getCtn();//类型
        String total = couponInfo.getCouponInfo().getCbcf() + "";//满多少可用;//满多少可用
        int money = couponInfo.getCouponInfo().getCbca();//满多少可用;//满多少可用;
        int discount = (int) (couponInfo.getCouponInfo().getCbr() * 100);
        String[] categorys = couponInfo.getCouponInfo().getCategorys().toArray(new String[]{});
        int status = couponInfo.getCouponInfo().getStatus();

        Double new_actualMoney = 0.00;
        if (1 == status || 0 == status) {
            coupondesc.setText("满" + total + "元可用");
            if ("代金券".equals(type) || "满减券".equals(type)) {
                new_actualMoney = ArithUtil.sub(actualMoney, money);
//                new_actualMoney = actualMoney - money;
                if (new_actualMoney <= 0) {
                    setActualMoney(0, money, true, "0");
                } else {
                    setActualMoney(new_actualMoney, money, true, "0");
                }
                String moneyStr = NumUtil.Number2Double(money + 0.0);
                zhekou.setVisibility(View.GONE);
                usecoupon.setVisibility(View.VISIBLE);
                couponkind.setText("-¥" + moneyStr);
                isCoupon = true;

            } else {
                if (couponInfo.getCouponInfo().getSigned() == 0) {
                    zhekou.setVisibility(View.GONE);
                    usecoupon.setVisibility(View.VISIBLE);
                    double total_accord = 0.00;
                    double total_no_accord = 0.00;
                    if (categorys.length > 0) {

                        for (String category : categorys) {
                            for (ProductTypeModel typeModel : mProductTypeModelList) {
                                if (typeModel.getName().equals(category)) {
                                    total_accord = ArithUtil.add(total_accord, Double.parseDouble(typeModel.getMoney()));
                                }
                            }
                        }
                        total_no_accord = ArithUtil.sub(actualMoney, total_accord);
                        total_accord = ArithUtil.mul(total_accord, discount) / 100;

                        new_actualMoney = ArithUtil.add(total_accord, total_no_accord);
                        double province = ArithUtil.sub(actualMoney, new_actualMoney);
                        setActualMoney(new_actualMoney, province, true, "0");
                    } else {
                        new_actualMoney = ArithUtil.mul(actualMoney, discount) / 100;
//                        total_no_accord = actualMoney - new_actualMoney;
                        total_no_accord = ArithUtil.sub(actualMoney, new_actualMoney);
                        setActualMoney(new_actualMoney, total_no_accord, true, "0");
                    }
                    float dis=Float.parseFloat(discount + "") / 10f;
                    couponkind.setText(dis + "折");
                    isCoupon = true;
                } else {
                    usecoupon.setVisibility(View.GONE);
                    zhekou.setVisibility(View.VISIBLE);
                    int lack = couponInfo.getCouponInfo().getLack();
                    if (lack < 0) {
                        isCoupon = true;
                        isNot = true;
                        new_actualMoney = mProductTypeAdapter.getMoney();
                        setActualMoney(new_actualMoney, 0, true, "0");
                        title.setText("* 余额不足，本次需" + Math.abs(lack) + "币抵扣");
                    } else {
                        double total_accord = 0.00;
                        double total_no_accord = 0.00;
                        if (categorys.length > 0) {

                            for (String category : categorys) {
                                for (ProductTypeModel typeModel : mProductTypeModelList) {
                                    if (typeModel.getName().equals(category)) {
//                                        total_accord += Float.parseFloat(typeModel.getMoney());
                                        total_accord = ArithUtil.add(total_accord, Double.parseDouble(typeModel.getMoney()));
                                    }
                                }
                            }
//                            total_no_accord = actualMoney - total_accord;
//                            total_accord = total_accord * dis;
//
//                            new_actualMoney = total_accord + total_no_accord;
//                            float province = actualMoney - new_actualMoney;
                            total_no_accord = ArithUtil.sub(actualMoney, total_accord);
                            total_accord = ArithUtil.mul(total_accord, discount) / 100;

                            new_actualMoney = ArithUtil.add(total_accord, total_no_accord);
                            double province = ArithUtil.sub(actualMoney, new_actualMoney);
                            setActualMoney(new_actualMoney, province, false, lack + "");

                        } else {
//                            new_actualMoney = actualMoney * dis;
//                            total_no_accord = actualMoney - new_actualMoney;
                            new_actualMoney = ArithUtil.mul(actualMoney, discount) / 100;
//                            total_no_accord = ArithUtil.sub(actualMoney, new_actualMoney);
                            double province = ArithUtil.sub(actualMoney, new_actualMoney);
                            setActualMoney(new_actualMoney, province, false, lack + "");
                        }
                        float dis=Float.parseFloat(discount + "") / 10f;
                        title.setText("* 本次消费享受优惠" + dis + "折");
                        isCoupon = false;
                    }
                }


            }
        }
    }


    private void setTotalMoney() {

        String actualMoneyStr = NumUtil.Number2Double(actualMoney);
        tv_money.setText(actualMoneyStr + "");
        cid = "";
        setActualMoney(actualMoney, 0, false, "0");
    }

    private String getjsonArray() {
        JSONArray jsonArray = new JSONArray();
        String text = "";
        for (ProductTypeModel productTypeModel : mProductTypeModelList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("moid", productTypeModel.getId());
                jsonObject.put("amt", productTypeModel.getMoney());
                jsonArray.put(jsonObject);
                text += jsonObject.toString() + ",".trim();
            } catch (com.alibaba.fastjson.JSONException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        text = "[" + text.substring(0, text.length() - 1) + "]";
        return text;
    }

    public String savemopid() {
        String s = "";
        for (ProductTypeModel productTypeModel : mProductTypeModelList) {
            s = s + productTypeModel.getId() + ",";
        }
        return s.substring(0, s.length() - 1);
    }
}
