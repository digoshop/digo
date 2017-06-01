package com.bjut.servicedog.servicedog.ui.settlement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.SaleFinish;
import com.bjut.servicedog.servicedog.ui.ScanActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费结算
 */
public class ConsumeFinishActivity_r extends BaseActivity implements View.OnClickListener {

    private TextView search;
    private LinearLayout consumesettle, exchangesettle, scan;
    private String mobile, desc;
    private LinearLayout ll_info;
    private TextView name, vip_kind, consume_phone;
    private EditText editText;
    private InputMethodManager mInputMethodManager;
    private TextView tv_title;
    private Button btn_right;

    private String userId = "";
    private String userName = "";
    private String userPhone = "";
    private String userVip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_finish_r);
        init();
    }

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xsjsgl));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText(getString(R.string.title_jsjl));
        ll_info = (LinearLayout) findViewById(R.id.ll_info);
        name = (TextView) findViewById(R.id.consume_nick);//用户昵称
        vip_kind = (TextView) findViewById(R.id.consume_vip_kind);
        consume_phone = (TextView) findViewById(R.id.consume_phone);
        String sType = pref.getString("shoptype", "");
        editText = (EditText) findViewById(R.id.editText);

        search = (TextView) findViewById(R.id.search);
        search.setOnClickListener(this);
        consumesettle = (LinearLayout) findViewById(R.id.linear_consume_settle);
        exchangesettle = (LinearLayout) findViewById(R.id.linear_exchange_settle);
        scan = (LinearLayout) findViewById(R.id.scan);
        scan.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        consumesettle.setOnClickListener(this);
        exchangesettle.setOnClickListener(this);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

//        requestSaleRecordList(max_id, min_id, "post");
    }


    @Override
    public void onClick(View v) {
        mInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                0);
        switch (v.getId()) {
            case R.id.btn_right:
                myIntentR(SettlementRecordActivity.class);
                break;
            case R.id.linear_consume_settle:
                if (name.getText().toString().equals("")) {
                    toast("请先查找用户");
                } else {
                    Intent intent = new Intent(this, MoneySalePlatActivity_r.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    intent.putExtra("userVip", userVip);
                    startActivity(intent);
                }
                break;
            case R.id.linear_exchange_settle:
                if (name.getText().toString().equals("")) {
                    toast("请先查找用户");
                } else {
                    Intent intent = new Intent(this, MoneyPlatformActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPhone", userPhone);
                    intent.putExtra("userVip", userVip);
                    startActivity(intent);
                }
                break;
            case R.id.search:
                if (editText.getText().toString().equals("")) {
                    toast("请输入手机号");
                } else {
                    requestDataForMobile();
                }
                break;
            case R.id.scan:
                startActivityForResult(new Intent(this, ScanActivity.class), 0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                JSONObject jsonObject = new JSONObject(result);
                String uid = jsonObject.getString("uid");
                if (uid.equals("")) {
                    toast("系统无法识别非本系统二维码");
                } else {
                    requestDataForScan(uid);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                toast("系统无法识别非本系统二维码");
            }

        }
    }

    private void requestDataForScan(String uid) {
        showProgressDialog();

        String urlString = "passport/user_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        RequestParams params = sortMapByKey(map);
        params.addBodyParameter("uid", uid);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String json = responseInfo.result;
                        try {
                            SaleFinish saleFinish = JSON.parseObject(json, SaleFinish.class);
                            if (saleFinish.getE().getCode() == 0) {
                                setView(saleFinish);
                            } else {
                                toast(saleFinish.getE().getDesc());
                            }
                            closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            toast("查询失败");
                            closeProgressDialog();
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


    private void requestDataForMobile() {
        showProgressDialog();

        String urlString = "passport/user_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("mobile", editText.getText().toString());
        RequestParams params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String json = responseInfo.result;
                        try {
                            SaleFinish saleFinish = JSON.parseObject(json, SaleFinish.class);
                            if (saleFinish.getE().getCode() == 0) {
                                setView(saleFinish);
                            } else if (saleFinish.getE().getCode() == -109) {
                                toast("用户不存在");

                            }
                            closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            toast("查询失败");
                            closeProgressDialog();
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

    public void setView(SaleFinish saleFinish) {

        try {
            userId = saleFinish.getData().getUid();
            userName = saleFinish.getData().getNick();
            userPhone = saleFinish.getData().getMobile();
            ll_info.setVisibility(View.VISIBLE);
            name.setText(saleFinish.getData().getNick());
            mobile = saleFinish.getData().getMobile();
            consume_phone.setText(mobile + "");
            boolean isVip = saleFinish.getData().is_vip();
            if (isVip) {
                vip_kind.setText("会员");
                userVip = "会员";
            } else {
                userVip = "非会员";
                vip_kind.setText("非会员");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

