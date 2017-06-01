package com.bjut.servicedog.servicedog.ui.settlement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ChoiseExchangeModel;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyPlatformActivity extends BaseActivity {
    private TextView order_sn, name, kind, phonenumber, tv_date;
    private TextView babydetail, exchangeshelian, date, societyvalue, submit, factjisuan, bencijifen;
    private String ordersn, id = "";
    private TextView tv_name;
    //    private EditText babymoney;
    private TextView shopname, shopmobile, shopshouyin, tv_save;
    private TextView tv_type;
    private TextView title_bi;
    private TextView tv_title;
    private LinearLayout ll_save;

    private final int CHOISE_PRODUCT = 0;

    private String userId = "";
    private String userName = "";
    private String userPhone = "";
    private String userVip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_platform);
        init();
        requestordersn();
    }

    private void init() {
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userPhone = getIntent().getStringExtra("userPhone");
        userVip = getIntent().getStringExtra("userVip");


        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_title = (TextView) findViewById(R.id.tv_title);
        order_sn = (TextView) findViewById(R.id.exchange_consume_order);
        name = (TextView) findViewById(R.id.exchange_consume_nick);
        kind = (TextView) findViewById(R.id.exchange_consume_kind);
        phonenumber = (TextView) findViewById(R.id.exchange_consume_phonenumber);
        tv_date = (TextView) findViewById(R.id.exchange_consume_date);
//        babymoney = (EditText) findViewById(R.id.exchange_consume_baby_money);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_save = (TextView) findViewById(R.id.tv_save);
        title_bi = (TextView) findViewById(R.id.title_bi);
        babydetail = (TextView) findViewById(R.id.exchange_consume_detail);
        exchangeshelian = (TextView) findViewById(R.id.exchange_consume_shelian);
        date = (TextView) findViewById(R.id.exchange_consume_duihuan_date);
        societyvalue = (TextView) findViewById(R.id.exchange_consume_societyvalue);
        submit = (TextView) findViewById(R.id.exchange_consume_submit);
        factjisuan = (TextView) findViewById(R.id.fact_jisuan);
        bencijifen = (TextView) findViewById(R.id.bencijifen);
        shopname = (TextView) findViewById(R.id.shop_name);
        shopmobile = (TextView) findViewById(R.id.contant_phonenumber);
        shopshouyin = (TextView) findViewById(R.id.shouyinname);
        ll_save = (LinearLayout) findViewById(R.id.ll_save);

        shopmobile.setText(pref.getString("contact_phone", ""));
        shopname.setText(pref.getString("shopname", ""));
        shopshouyin.setText(pref.getString("name", ""));

        tv_title.setText(getString(R.string.title_rgjs));
        name.setText(userName);
        phonenumber.setText(userPhone);
        kind.setText(userVip);

        submit.setOnClickListener(this);
        tv_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_consume_submit:
                if ("".equals(id)) {
                    toast("请选择兑换的宝贝");
                    return;
                }
                if (!Utils.isNotFastClick()) {
                    return;
                }
                sendExchangeconfirm();
                break;
            case R.id.tv_name:
                Intent intent = new Intent(MoneyPlatformActivity.this, ExchangeListActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, CHOISE_PRODUCT);
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
            case CHOISE_PRODUCT:
                ChoiseExchangeModel.DataBean dataBean = (ChoiseExchangeModel.DataBean) data.getSerializableExtra("item");
                tv_name.setHint(dataBean.getPna());
//                babymoney.setText(dataBean.getEpp() + "");
                exchangeshelian.setText(dataBean.getEpg() + "");
                List<String> pat = dataBean.getPat();
                if (pat != null && pat.size() > 0) {
                    String patStr = "";
                    for (String s : pat) {
                        patStr += s + "、";
                    }
                    patStr = patStr.substring(0, patStr.length() - 1);
                    babydetail.setText(patStr);
                } else {
                    babydetail.setText("");
                }


                int pt = dataBean.getPt();
                if (pt == 1) {
                    tv_type.setText("竞拍");
                    tv_type.setTextColor(getResources().getColor(R.color.orange));
                    tv_type.setBackgroundResource(R.drawable.border_orange_2dp);
                    title_bi.setText("竞拍消耗:");
                } else {
                    tv_type.setText("兑换");
                    tv_type.setTextColor(getResources().getColor(R.color.blue));
                    tv_type.setBackgroundResource(R.drawable.border_blue_2dp);
                    title_bi.setText("兑换消耗:");
                }

                long time = dataBean.getCt() * 1000;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = new Date(time);
                date.setText(sdf.format(d));
                String ppr = dataBean.getPpr() + "";
                if (StringUtils.isEmpty(ppr)) {
                    societyvalue.setText("暂无报价");
                    ll_save.setVisibility(View.GONE);
//                    tv_save.setText(0- dataBean.getEpp() + "");
                } else {
                    societyvalue.setText(dataBean.getPpr() + "");
                    ll_save.setVisibility(View.VISIBLE);
                    tv_save.setText(dataBean.getPpr() - dataBean.getEpp() + "");
                }
                id = dataBean.getId() + "";
                factjisuan.setText(dataBean.getEpp() + "");
//                tv_save.setText(dataBean.getPpr() - dataBean.getEpp() + "");
                requestjifen(dataBean.getEpp());
//                bencijifen.setText(dataBean.getIntg() + "币");//本次积分
                break;
        }
    }

    private void requestjifen(double actualMoney) {
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
                                bencijifen.setText(jifen + " 币");
                            } else {
                                toast(j.getJSONObject("e").getString("desc"));
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

    private void requestordersn() {
        showProgressDialog();
        String urlString = "pay/orderNum.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));//订单编号


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
                                ordersn = j.getString("data");
                                order_sn.setText(ordersn);
                                Date date = new Date(System.currentTimeMillis());
                                String str = "";
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                str = format.format(date);
                                tv_date.setText(str);

                                Log.i("user_nick", name.getText().toString());

                            } else {
                                toast("提交失败");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
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

    private void sendExchangeconfirm() {
        showProgressDialog();
        String urlString = "product/approveMerchantExchange.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("sid",pref.getString("sid",""));
        map.put("sid", pref.getString("sid", ""));
        //map.put("gid",pid);
        map.put("opuid", pref.getString("uid", ""));
        //map.put("intg",jifen);
        map.put("gid", id);

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

                                toast("兑换结算成功");
                                finish();

                            } else {
                                toast("提交失败");
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
}
