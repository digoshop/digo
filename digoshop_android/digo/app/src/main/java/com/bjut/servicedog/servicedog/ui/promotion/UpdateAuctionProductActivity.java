package com.bjut.servicedog.servicedog.ui.promotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.GoodsDetail;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.po.Request;
import com.bjut.servicedog.servicedog.po.Time;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/2/14.
 */

public class UpdateAuctionProductActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_product_name;
    private Button btn_cancel;
    private Button btn_submit;

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

    private int type = Constant.EXCHANGE_PRODUCT;
    private long auctionDate = 0;
    private long auctionTime = 0;
    private int currentNum = 0;
    private Product mProduct;
    private String productId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_auction);
        initViews();
        initWheelTime();
        setListener();
        getProductInfo();
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
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_auction_time.setOnClickListener(this);
        img_auction_time.setOnClickListener(this);
        tv_auction_date.setOnClickListener(this);
        img_auction_date.setOnClickListener(this);
        btn_jian.setOnClickListener(this);
        btn_jia.setOnClickListener(this);
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_submit = (Button) findViewById(R.id.btn_submit);

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

        tv_title.setText("编辑竞拍");
        productId = getIntent().getStringExtra("productId");

        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH);
        day = instance.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
                if (auctionInputCheck()) {
                    updateAuctionProduct();
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

    private void updateAuctionProduct() {
        String urlString = "product/updateProduct.json";
        urlString = String.format(urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
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
                                toast("竞拍商品已成功提交，系统会在24小时内进行审核，请耐心等候，介时查阅！");
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

    private void getProductInfo() {

        showProgressDialog();
        String urlString = "product/productDetail.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", productId);
        map.put("pt", Constant.AUCTION_PRODUCT + "");
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
                        mProduct = goodsDetail.getData().getAuctionProductInfo();
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
        tv_product_name.setText(mProduct.getPna());
        et_auction_integral.setText(mProduct.getAplg() + "");
        et_auction_money.setText(mProduct.getApp() + "");
        String apsdTime = mProduct.getApsdTime();
        String[] times = apsdTime.split(" ");
        tv_auction_date.setText(times[0]);
        tv_auction_time.setText(times[1]);
        tv_num.setText(mProduct.getApg());
        et_auction_guize.setText(mProduct.getApd());
        auctionDate = DateUtil.strToDate(times[0]).getTime();
        auctionTime = (Integer.parseInt(times[1].substring(0, 2)) * 60 + Integer.parseInt(times[1].substring(3, 5))) * 60L;
        getAuctionTime();
    }
}
