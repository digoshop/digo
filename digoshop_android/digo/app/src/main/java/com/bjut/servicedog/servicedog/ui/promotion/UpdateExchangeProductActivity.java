package com.bjut.servicedog.servicedog.ui.promotion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.po.GoodsDetail;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.view.DecimalEditText;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bjut.servicedog.servicedog.utils.DateUtil.strToDate;

/**
 * Created by beibeizhu on 17/2/14.
 */

public class UpdateExchangeProductActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_product_name;
    private Button btn_cancel;
    private Button btn_submit;
    /*兑换*/
    private EditText et_exchange_integral;
    private DecimalEditText et_exchange_money;
    private ImageView img_date_end;
    private TextView tv_end_time;
    private ImageView img_date_start;
    private TextView tv_start_time;
    private RadioButton rb_limit_yes;
    private RadioButton rb_limit_no;
    private LinearLayout ll_limit;
    private EditText et_number;
    private EditText et_total_number;
    private EditText et_exchange_guize;

    private int year = 0, month = 0, day = 0;
    private int gapCount = 0;

    private int type = Constant.EXCHANGE_PRODUCT;
    private int limit = 1;//是否限制每天兑换数量 1否 2是
    private String productId = "";
    private Product mProduct;
    private String stime = "";
    private String etime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_exchange);
        initViews();
        setListener();
        getProductInfo();
    }

    private void setListener() {
        img_date_end.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        img_date_start.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        rb_limit_yes.setOnClickListener(this);
        rb_limit_no.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    int num = Integer.parseInt(charSequence.toString());
                    Date sDate = DateUtil.strToDate(tv_start_time.getText().toString());
                    Date eDate = DateUtil.strToDate(tv_end_time.getText().toString());
                    gapCount = DateUtil.getGapCount(sDate, eDate) + 1;
                    et_total_number.setText(num * gapCount + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_exchange_integral = (EditText) findViewById(R.id.et_exchange_integral);
        et_exchange_money = (DecimalEditText) findViewById(R.id.et_exchange_money);
        img_date_end = (ImageView) findViewById(R.id.img_date_end);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        img_date_start = (ImageView) findViewById(R.id.img_date_start);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        img_date_end = (ImageView) findViewById(R.id.img_date_end);
        rb_limit_yes = (RadioButton) findViewById(R.id.rb_limit_yes);
        rb_limit_no = (RadioButton) findViewById(R.id.rb_limit_no);
        ll_limit = (LinearLayout) findViewById(R.id.ll_limit);
        et_number = (EditText) findViewById(R.id.et_number);
        et_total_number = (EditText) findViewById(R.id.et_total_number);
        et_exchange_guize = (EditText) findViewById(R.id.et_exchange_guize);

        tv_title.setText("编辑兑换");
        productId = getIntent().getStringExtra("productId");

        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH);
        day = instance.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (exchangeInputCheck()) {
                    updateExchangeProduct();
                }

            case R.id.btn_cancel:
                this.finish();
                break;

            case R.id.rb_limit_no:
                limit = 1;
                ll_limit.setVisibility(View.GONE);
                et_number.setText("");
                et_total_number.setText("");
                et_total_number.setEnabled(true);
                break;
            case R.id.rb_limit_yes:
                String startTime = tv_start_time.getText().toString();
                if ("".equals(startTime)) {
                    toast("请先选择开始时间");
                    rb_limit_yes.setChecked(false);
                    rb_limit_no.setChecked(true);
                    return;
                }
                if ("".equals(tv_end_time.getText().toString())) {
                    toast("请先选择结束时间");
                    rb_limit_yes.setChecked(false);
                    rb_limit_no.setChecked(true);
                    return;
                }
                limit = 2;
                et_total_number.setText("");
                et_total_number.setEnabled(false);
                ll_limit.setVisibility(View.VISIBLE);
                break;

            case R.id.img_date_start:
            case R.id.tv_start_time:
                etime = tv_end_time.getText().toString();
                stime = tv_end_time.getText().toString();
                DatePickerDialog dpdp = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                if (monthOfYear < 9) {
                                    mo = "0" + (monthOfYear + 1);
                                } else {
                                    mo = (monthOfYear + 1) + "";
                                }
                                //将天数转换为两位数
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                String startTime = year + "-" + mo + "-" + da;
                                tv_start_time.setText(startTime);
                                if (!etime.equals("")) {
                                    Date sDate = DateUtil.strToDate(startTime);
                                    Date eDate = DateUtil.strToDate(etime);
                                    gapCount = DateUtil.getGapCount(sDate, eDate) + 1;
                                    String s = et_number.getText().toString();
                                    if (!"".equals(s)) {
                                        int num = Integer.parseInt(s);
                                        et_total_number.setText(num * gapCount + "");
                                    }
                                }
                            }
                        }, year, month, day);
                dpdp.setHasOptionsMenu(true);
                dpdp.setVersion(DatePickerDialog.Version.VERSION_2);
                dpdp.setOkColor(getResources().getColor(R.color.blue));
                dpdp.setAccentColor(getResources().getColor(R.color.blue));
                dpdp.setTitle("开始日期");
                dpdp.setOkText("");
                Calendar startMin = Calendar.getInstance();
                if (!stime.equals("")) {
                    long stimeL = DateUtil.strToDate(stime).getTime();
                    startMin.setTimeInMillis(stimeL);
                } else {
                    startMin.set(Calendar.DAY_OF_MONTH, day);  //设置日期
                    startMin.set(Calendar.MONTH, month);
                    startMin.set(Calendar.YEAR, year);
                }
                dpdp.setMinDate(startMin);
                if (!etime.equals("")) {
                    Calendar startMax = Calendar.getInstance();
                    long eTimeL = DateUtil.strToDate(etime).getTime() + 24 * 3600 * 1000 - 1000;
                    startMax.setTimeInMillis(eTimeL);
                    dpdp.setMaxDate(startMax);
                }

                dpdp.setCancelable(false);
                dpdp.autoDismiss(true);
                dpdp.show(getFragmentManager(), "startDate");
                break;
            case R.id.img_date_end:
            case R.id.tv_end_time:
                stime = tv_start_time.getText().toString();
                etime = tv_end_time.getText().toString();
                if ("".equals(tv_start_time.getText().toString())) {
                    toast("请先选择开始时间");
                    return;
                }
                DatePickerDialog endDpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                if (monthOfYear < 9) {
                                    mo = "0" + (monthOfYear + 1);
                                } else {
                                    mo = (monthOfYear + 1) + "";
                                }
                                //将天数转换为两位数
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                String endTime = year + "-" + mo + "-" + da;
                                tv_end_time.setText(endTime);
                                Date sDate = DateUtil.strToDate(tv_start_time.getText().toString());
                                Date eDate = DateUtil.strToDate(endTime);
                                gapCount = DateUtil.getGapCount(sDate, eDate) + 1;
                                String s = et_number.getText().toString();
                                if (!"".equals(s)) {
                                    int num = Integer.parseInt(s);
                                    et_total_number.setText(num * gapCount + "");
                                }

                            }
                        }, year, month, day);
                endDpd.setHasOptionsMenu(true);
                endDpd.setVersion(DatePickerDialog.Version.VERSION_2);
                endDpd.setOkColor(getResources().getColor(R.color.blue));
                endDpd.setAccentColor(getResources().getColor(R.color.blue));
                endDpd.setTitle("结束日期");
                endDpd.setOkText("");
                Calendar min = Calendar.getInstance();
                long stimeL = DateUtil.strToDate(stime).getTime();
                min.setTimeInMillis(stimeL);
                endDpd.setMinDate(min);
                endDpd.setCancelable(false);
                endDpd.autoDismiss(true);
                endDpd.show(getFragmentManager(), "endDate");
                break;
        }
    }


    private void updateExchangeProduct() {
        String urlString = "product/updateProduct.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("productType", Constant.EXCHANGE_PRODUCT + "");
        map.put("productId", productId);
//        map.put("productName", tv_product_name.getText().toString());//宝贝名称
        map.put("exchangeNumber", et_total_number.getText().toString());//兑换数量
        map.put("exchangeDesc", et_exchange_guize.getText().toString());//兑换规则
        map.put("exchangeGold", et_exchange_integral.getText().toString());//兑换积分
        map.put("exchangePrice", et_exchange_money.getText().toString());//换购金额
        map.put("exchangeMode", limit + "");//是否每日限量
        if (limit == 2) {
            map.put("epnu", et_number.getText().toString() + "");//每日限量
        } else {
            map.put("epnu", "");//每日限量
        }

        map.put("startDate", (strToDate(tv_start_time.getText().toString()).getTime()) / 1000 + "");//兑换截止时间
        map.put("endDate", (strToDate(tv_end_time.getText().toString()).getTime() + 24 * 3600 * 1000 - 1000) / 1000 + "");//兑换截止时间
//        map.put("operateId","2");
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                closeProgressDialog();
                String json = responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                        toast("修改成功");
                        EventBus.getDefault().post(new OnRefreshEvent(true));
//                        Intent intent = new Intent();
//                        intent.putExtra("type", type);
//                        setResult(5, intent);
                        finish();
                    } else {
                        toast("修改失败");
                    }
                } catch (Exception e) {
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


    private boolean exchangeInputCheck() {
        if (productId.equals("")) {
            toast("请选择商品");
            return false;
        }
        if (et_exchange_integral.getText().toString().equals("")) {
            toast("请填写兑换积分");
            return false;
        }
        if (et_exchange_money.getText().toString().equals("")) {
            toast("请填写换购金额");
            return false;
        }
        if (tv_start_time.getText().toString().equals("")) {
            toast("请选择开始时间");
            return false;
        }
        if (tv_end_time.getText().toString().equals("")) {
            toast("请选择结束时间");
            return false;
        }
        if (limit == 1) {
            if (et_total_number.getText().toString().equals("")) {
                toast("请填写换购数量");
                return false;
            }
        } else {
            if (et_number.getText().toString().equals("")) {
                toast("请填写每天限量");
                return false;
            }
        }
        if (et_exchange_guize.getText().toString().equals("")) {
            toast("请填写兑换规则");
            return false;
        }
        return true;
    }

    private void getProductInfo() {

        showProgressDialog();
        String urlString = "product/productDetail.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", productId);
        map.put("pt", Constant.EXCHANGE_PRODUCT + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                closeProgressDialog();
                String json = responseInfo.result;
                try {
                    GoodsDetail goodsDetail = JSON.parseObject(json, GoodsDetail.class);
                    if (goodsDetail.getE().getCode() == 0) {
                        mProduct = goodsDetail.getData().getExchangeProductInfo();
                        setView();
                    } else {
                        toast(goodsDetail.getE().getDesc());
                    }
                } catch (Exception e) {
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

    private void setView() {
        long epsd = mProduct.getEpsd();
        long eped = mProduct.getEped();
        int epm = mProduct.getEpm();

        tv_product_name.setText(mProduct.getPna());
        et_exchange_integral.setText(mProduct.getEpg() + "");
        et_exchange_money.setText(mProduct.getEpp() + "");
        if (epsd == 0) {
            tv_start_time.setText("");
        } else {
            String startTime = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(epsd * 1000));
            tv_start_time.setText(startTime);
        }
        if (eped == 0) {
            tv_end_time.setText("");
        } else {
            String startTime = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(eped * 1000));
            tv_end_time.setText(startTime);
        }
        switch (epm) {
            case Constant.LIMIT_NO:
                limit = 1;
                rb_limit_no.setChecked(true);
                ll_limit.setVisibility(View.GONE);
                et_total_number.setText(mProduct.getEnu() + "");
                break;
            case Constant.LIMIT_YES:
                limit = 2;
                rb_limit_yes.setChecked(true);
                ll_limit.setVisibility(View.VISIBLE);
                et_number.setText(mProduct.getEpnu() + "");
                et_total_number.setText(mProduct.getEnu() + "");
                break;
        }
        et_exchange_guize.setText(mProduct.getEpd());
    }
}
