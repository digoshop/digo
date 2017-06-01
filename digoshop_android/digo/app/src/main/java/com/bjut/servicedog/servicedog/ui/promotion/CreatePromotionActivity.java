package com.bjut.servicedog.servicedog.ui.promotion;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.po.Request;
import com.bjut.servicedog.servicedog.po.Time;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.view.DecimalEditText;
import com.bjut.servicedog.servicedog.view.listener.OnStringChangeListener;
import com.bjut.servicedog.servicedog.view.wheelview.ChoiseStringWheel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjut.servicedog.servicedog.utils.DateUtil.strToDate;

/**
 * Created by beibeizhu on 17/2/14.
 */

public class CreatePromotionActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_product_name;
    private LinearLayout ll_exchange;
    private LinearLayout ll_auction;
    private RadioGroup rg_product;
    private Button btn_cancel;
    private Button btn_submit;
    /*兑换*/
    private EditText et_exchange_integral;
    private DecimalEditText et_exchange_money;
    private ImageView img_date_end;
    private TextView tv_end_time;
    private ImageView img_date_start;
    private TextView tv_start_time;
    private RadioGroup rg_limit;
    private RadioButton rb_limit_yes;
    private RadioButton rb_limit_no;
    private LinearLayout ll_limit;
    private EditText et_number;
    private EditText et_total_number;
    private EditText et_exchange_guize;

    /*竞拍*/
    private EditText et_auction_integral;
    private EditText et_auction_money;
    private ImageView img_auction_time;
    private TextView tv_auction_time;
    private ImageView img_auction_date;
    private TextView tv_auction_date;
    private TextView tv_num;
    private ChoiseStringWheel mChoiseStringWheel;
    private Button btn_jian;
    private Button btn_jia;
    private EditText et_auction_guize;

    private int year = 0, month = 0, day = 0;
    private int year2 = 0, month2 = 0, day2 = 0;
    private int year3 = 0, month3 = 0, day3 = 0;
    private int gapCount = 0;

    private int type = Constant.EXCHANGE_PRODUCT;
    private int limit = 1;//是否限制每天兑换数量 1否 2是
    private String productId = "";
    private long auctionDate = 0;
    private long auctionTime = 0;
    private int currentNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promotion);
        initViews();
        initWheelTime();
        setListener();
        getAuctionTime();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOISE_PRODUCT:
                    Product product = (Product) data.getSerializableExtra("product");
                    productId = product.getPid() + "";
                    tv_product_name.setText(product.getPna());
                    break;
            }
        }
    }

    private void initWheelTime() {
        mChoiseStringWheel = new ChoiseStringWheel(this);
        mChoiseStringWheel.setOnFoorChangeListener(new OnStringChangeListener() {
            @Override
            public void onFoorChange(String item) {
                tv_auction_time.setText(item);
                auctionTime = (Integer.parseInt(item.substring(0, 2)) * 60 + Integer.parseInt(item.substring(3, 5))) * 60L;
            }
        });
    }

    private void setListener() {
        tv_product_name.setOnClickListener(this);
        img_date_end.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        img_date_start.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        rb_limit_yes.setOnClickListener(this);
        rb_limit_no.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_auction_time.setOnClickListener(this);
        img_auction_time.setOnClickListener(this);
        tv_auction_date.setOnClickListener(this);
        img_auction_date.setOnClickListener(this);
        btn_jian.setOnClickListener(this);
        btn_jia.setOnClickListener(this);

        rg_product.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_exchange:
                        type = Constant.EXCHANGE_PRODUCT;
                        ll_exchange.setVisibility(View.VISIBLE);
                        ll_auction.setVisibility(View.GONE);
                        break;

                    case R.id.rb_auction:
                        type = Constant.AUCTION_PRODUCT;
                        ll_exchange.setVisibility(View.GONE);
                        ll_auction.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    int num = Integer.parseInt(charSequence.toString());
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
        ll_exchange = (LinearLayout) findViewById(R.id.ll_exchange);
        ll_auction = (LinearLayout) findViewById(R.id.ll_auction);
        rg_product = (RadioGroup) findViewById(R.id.rg_product);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_exchange_integral = (EditText) findViewById(R.id.et_exchange_integral);
        et_exchange_money = (DecimalEditText) findViewById(R.id.et_exchange_money);
        img_date_end = (ImageView) findViewById(R.id.img_date_end);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        img_date_start = (ImageView) findViewById(R.id.img_date_start);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        img_date_end = (ImageView) findViewById(R.id.img_date_end);
        rg_limit = (RadioGroup) findViewById(R.id.rg_limit);
        rb_limit_yes = (RadioButton) findViewById(R.id.rb_limit_yes);
        rb_limit_no = (RadioButton) findViewById(R.id.rb_limit_no);
        ll_limit = (LinearLayout) findViewById(R.id.ll_limit);
        et_number = (EditText) findViewById(R.id.et_number);
        et_total_number = (EditText) findViewById(R.id.et_total_number);
        et_exchange_guize = (EditText) findViewById(R.id.et_exchange_guize);

        et_auction_integral = (EditText) findViewById(R.id.et_auction_integral);
        et_auction_money = (EditText) findViewById(R.id.et_auction_money);
        img_auction_time = (ImageView) findViewById(R.id.img_auction_time);
        tv_auction_time = (TextView) findViewById(R.id.tv_auction_time);
        img_auction_date = (ImageView) findViewById(R.id.img_auction_date);
        tv_auction_date = (TextView) findViewById(R.id.tv_auction_date);
        tv_num = (TextView) findViewById(R.id.tv_num);
        btn_jian = (Button) findViewById(R.id.btn_jian);
        btn_jia = (Button) findViewById(R.id.btn_jia);
        et_auction_guize = (EditText) findViewById(R.id.et_auction_guize);

        tv_title.setText("新增促销");

        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH);
        day = instance.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_product_name:
                Intent intent = new Intent(mContext, ChoiseProductActivity.class);
                startActivityForResult(intent, Constant.CHOISE_PRODUCT);
                break;

            case R.id.tv_auction_time:
            case R.id.img_auction_time:
                mChoiseStringWheel.show(view);
                break;

            case R.id.tv_auction_date:
            case R.id.img_auction_date:
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";
                                String day = "";
                                if (monthOfYear < 9) {
                                    month = "0" + (monthOfYear + 1);
                                } else {
                                    month = (monthOfYear + 1) + "";
                                }
                                if (dayOfMonth < 10) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = dayOfMonth + "";
                                }
                                String endTime = year + "-" + month + "-" + day;
                                auctionDate = DateUtil.strToDate(endTime).getTime();
                                tv_auction_date.setText(endTime);
                            }
                        }, year, month, day);
                dpd.setHasOptionsMenu(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setOkColor(getResources().getColor(R.color.blue));
                dpd.setAccentColor(getResources().getColor(R.color.blue));
                dpd.setTitle("开拍日期");
                dpd.setOkText("");
                Calendar min = Calendar.getInstance();
                min.set(Calendar.DAY_OF_MONTH, day + 1);  //设置日期
                min.set(Calendar.MONTH, month);
                min.set(Calendar.YEAR, year);
                dpd.setMinDate(min);
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "endDate");
                break;

            case R.id.btn_submit:
                switch (type) {
                    case Constant.EXCHANGE_PRODUCT:
                        if (exchangeInputCheck()) {
                            addExchangeProduct();
                        }
                        break;
                    case Constant.AUCTION_PRODUCT:
                        if (auctionInputCheck()) {
                            addAuctionProduct();
                        }
                        break;
                }
                break;

            case R.id.btn_cancel:
                this.finish();
                break;

            case R.id.btn_jia:
                currentNum = Integer.parseInt(tv_num.getText().toString());
                currentNum += 10;
                tv_num.setText(currentNum + "");
                break;
            case R.id.btn_jian:
                currentNum = Integer.parseInt(tv_num.getText().toString());
                if (currentNum != 10) {
                    currentNum -= 10;
                    tv_num.setText(currentNum + "");
                }
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
                final String etime = tv_end_time.getText().toString();
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
                                year2 = year;
                                month2 = monthOfYear;
                                day2 = dayOfMonth;

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
                startMin.set(Calendar.DAY_OF_MONTH, day + 1);  //设置日期
                startMin.set(Calendar.MONTH, month);
                startMin.set(Calendar.YEAR, year);
                dpdp.setMinDate(startMin);

                if (!etime.equals("")) {
                    Calendar startMax = Calendar.getInstance();
                    startMax.set(Calendar.DAY_OF_MONTH, day3);  //设置日期
                    startMax.set(Calendar.MONTH, month3);
                    startMax.set(Calendar.YEAR, year3);
                    dpdp.setMaxDate(startMax);
                }

                dpdp.setCancelable(false);
                dpdp.autoDismiss(true);
                dpdp.show(getFragmentManager(), "startDate");
                break;
            case R.id.img_date_end:
            case R.id.tv_end_time:
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
                                year3 = year;
                                month3 = monthOfYear;
                                day3 = dayOfMonth;
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
                Calendar min_end = Calendar.getInstance();
                min_end.set(Calendar.DAY_OF_MONTH, day2);  //设置日期
                min_end.set(Calendar.MONTH, month2);
                min_end.set(Calendar.YEAR, year2);
                endDpd.setMinDate(min_end);
                endDpd.setCancelable(false);
                endDpd.autoDismiss(true);
                endDpd.show(getFragmentManager(), "endDate");
                break;
        }
    }

    private void getAuctionTime() {
        String urlString = "product/queryMerchantAuctionPeriodList.json";
        urlString = String.format(urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
//        map.put("page",1+"");
//        map.put("page_length","10");
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    Request request = JSON.parseObject(json, Request.class);
                    if (request.getE().getCode() == 0) {
                        if (request.getData().size() > 0) {
                            List<String> data = new ArrayList<String>();
                            for (Time time : request.getData()) {
                                data.add(time.getTime());
                            }
                            mChoiseStringWheel.setData(data);
                        } else {
                        }
                    } else {
                        toast(request.getE().getDesc());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void addExchangeProduct() {
        String urlString = "product/addProduct.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("productType", Constant.EXCHANGE_PRODUCT + "");
        map.put("pid", productId);
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
                        toast("兑换商品已成功提交!");
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        toast("提交失败!");
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

    private void addAuctionProduct() {
        String urlString = "product/addProduct.json";
        urlString = String.format(urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", productId);
        map.put("auctionGold", tv_num.getText().toString());//加价幅度
        map.put("auctionLowGold", et_auction_integral.getText().toString());//起拍积分
        map.put("auctionPrice", et_auction_money.getText().toString());//领取金额
        map.put("startDate", (auctionDate / 1000 + auctionTime) + "");//开拍时间
        map.put("auctionDesc", et_auction_guize.getText().toString());//竞拍规则
        map.put("productType", Constant.AUCTION_PRODUCT + "");
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
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
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                toast("竞拍商品已成功提交!");
                                Intent intent = new Intent();
                                intent.putExtra("type", type);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                toast("提交失败!");
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
            String totalNum = et_total_number.getText().toString();
            if ("".equals(totalNum)) {
                toast("请填写换购数量");
                return false;
            }else {
                int num = Integer.parseInt(totalNum);
                if (num==0) {
                    toast("换购数量必须大于0");
                    return false;
                }
            }
        } else {
            String number = et_number.getText().toString();
            if ("".equals(number)) {
                toast("请填写每天限量");
                return false;
            }else{
                int num = Integer.parseInt(number);
                if (num==0) {
                    toast("每天限量必须大于0");
                    return false;
                }
            }
        }
        if (et_exchange_guize.getText().toString().equals("")) {
            toast("请填写兑换规则");
            return false;
        }
        return true;
    }

    private boolean auctionInputCheck() {
        if (productId.equals("")) {
            toast("请选择商品");
            return false;
        }
        if (et_auction_integral.getText().toString().equals("")) {
            toast("请填写起拍积分");
            return false;
        }
        if (et_auction_money.getText().toString().equals("")) {
            toast("请填写领取金额");
            return false;
        }
        if (tv_auction_time.getText().toString().equals("")) {
            toast("请选择竞拍时间");
            return false;
        }
        if (tv_auction_date.getText().toString().equals("")) {
            toast("请选择竞拍时间点");
            return false;
        }
        if (et_auction_guize.getText().toString().equals("")) {
            toast("请填写竞拍规则");
            return false;
        }
        return true;
    }
}
