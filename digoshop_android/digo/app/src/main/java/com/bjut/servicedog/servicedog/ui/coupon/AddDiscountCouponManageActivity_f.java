package com.bjut.servicedog.servicedog.ui.coupon;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ProductAdapter;
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.RunKindList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.KeyboardUtils;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.bjut.servicedog.servicedog.utils.ToastUtils;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.DecimalEditText;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDiscountCouponManageActivity_f extends BaseActivity implements View.OnClickListener{
    private EditText ed_time_one, ed_time_two, couponBatchName, couponBatchNumber, couponBatchCreditFull, couponBatchExchangeAmount, couponBatchDesc;
    private DecimalEditText couponBatchCreditAmount;
    private int year, month, day, hours, minute, second, year2, month2, day2, editflag, rule;
    private Calendar c, c2;
    private TextView kindone, kindtwo, kindthree, kindmoney, kindkcondition, y_title, y_number, y_pre, y_money, y_kind, y_condition, y_desc, y_time;
    private String chooseKind = "null", chooseShop = "0", runkindIdstr = "", runkindNamestr = "";
    private List<RunKind> rk = new ArrayList<>();
    private List<Long> kindsid = new ArrayList<>();
    private List<String> kindsname = new ArrayList<String>();
    private ScrollView scll;
    private TextView send, look;
    private TextView tv_title;
    private TextView yuan, starttime, endtime;
    private LinearLayout coupontype, line;
    private ImageView img_left, img_right;
    private EditText rilistart, riliend;
    private long start, end;//优惠卷开始时间结束时间
    private GridView gvproduct;
    private ProductAdapter productAdapter;
    private String moids;
    private String couponNum;
    private RadioButton onlyshop, allshop;
    private ImageView image_rili_one;
    private ImageView image_rili_two;
    private ImageView to_back;
    private LinearLayout ll_full;
    private CheckBox allProduct;
    private RadioButton rb_limit_yes;
    private RadioButton rb_limit_no;
    private LinearLayout ll_limit;
    private EditText et_number;

    private boolean isOnClick = false;//表示全部品类是否为点击

    private long choiseStartTime = 0;
    private boolean isAll = false;
    private int limit = 1;
    private int total = 0;

    private List<String> types = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount_coupon_manage_activity_f);
        init();

        productAdapter.setOnCheckedChangeListener(new ProductAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheck(boolean isChecked, RunKind kind) {
                if (isChecked) {
                    for (int i = 0; i < kindsid.size(); i++) {
                        if (kind.getMoid() == kindsid.get(i)) {
                            return;
                        }

                    }
                    kindsid.add(kind.getMoid());
                    types.add(kind.getName());
                    if (kindsid.size() == rk.size()) {
                        isOnClick = true;
                        allProduct.setChecked(true);
                        y_desc.setText("全部品类");
                    } else {
                        String kinds = "";
                        for (String type : types) {
                            kinds = kinds + type + " ";
                        }
                        if (kinds.length() > 0) {
                            kinds = kinds.substring(0, kinds.length() - 1);
                        }
                        y_desc.setText(kinds);
                    }
                } else {
                    isOnClick = false;
                    for (int i = 0; i < kindsid.size(); i++) {
                        if (kind.getMoid() == kindsid.get(i)) {
                            kindsid.remove(i);
                            types.remove(i);
                        }
                    }
                    allProduct.setChecked(false);
                    if (types.size() != 0) {
                        String kinds = "";
                        for (String type : types) {
                            kinds = kinds + type + " ";
                        }
                        if (kinds.length() > 0) {
                            kinds = kinds.substring(0, kinds.length() - 1);
                        }
                        y_desc.setText(kinds);
                    } else {
                        y_desc.setText("优惠券使用品类");
                    }
                }

            }
        });


        allProduct = (CheckBox) findViewById(R.id.allProduct);
        allProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    productAdapter.setCheckAll(isChecked);
                    y_desc.setText("全部品类");
                    types.clear();
                    kindsid.clear();
                    for (RunKind runKind : rk) {
                        types.add(runKind.getName());
                        kindsid.add(runKind.getMoid());
                    }
                } else {
                    if (isOnClick) {
                        types.clear();
                        kindsid.clear();
                        y_desc.setText("优惠卷使用品类");
                        productAdapter.setCheckAll(isChecked);
                    }
                }

                isAll = isChecked;
            }
        });

        allProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOnClick = true;
            }
        });

    }

    private void init() {

        c = Calendar.getInstance();
        //取得日历的信息
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xzyhq));
        ll_full = (LinearLayout) findViewById(R.id.ll_full);
        gvproduct = (GridView) findViewById(R.id.gv_product);
        productAdapter = new ProductAdapter(this);
        gvproduct.setAdapter(productAdapter);

        to_back = (ImageView) findViewById(R.id.to_back);
        to_back.setOnClickListener(this);

        editflag = getIntent().getIntExtra("couponedit", 0);
        rilistart = (EditText) findViewById(R.id.ed_rili_one);
        rilistart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                starttime.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        riliend = (EditText) findViewById(R.id.ed_rili_two);
        riliend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                endtime.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        image_rili_one = (ImageView) findViewById(R.id.image_rili_one);
        image_rili_two = (ImageView) findViewById(R.id.image_rili_two);
        y_title = (TextView) findViewById(R.id.y_title);
        y_number = (TextView) findViewById(R.id.y_number);
        y_pre = (TextView) findViewById(R.id.y_pre);
        y_money = (TextView) findViewById(R.id.y_money);
        y_kind = (TextView) findViewById(R.id.y_kind);
        y_condition = (TextView) findViewById(R.id.y_condition);
        y_desc = (TextView) findViewById(R.id.y_desc);
        starttime = (TextView) findViewById(R.id.y_starttime);
        endtime = (TextView) findViewById(R.id.y_endtime);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right_info);
        line = (LinearLayout) findViewById(R.id.linear_line);
        coupontype = (LinearLayout) findViewById(R.id.linear_coupontype);
        yuan = (TextView) findViewById(R.id.yuan);
        ed_time_one = (EditText) findViewById(R.id.ed_rili_one);
        ed_time_two = (EditText) findViewById(R.id.ed_rili_two);
        kindone = (TextView) findViewById(R.id.kindone);
        kindtwo = (TextView) findViewById(R.id.kindtwo);
        kindthree = (TextView) findViewById(R.id.kindthree);
        kindmoney = (TextView) findViewById(R.id.t4);
        kindkcondition = (TextView) findViewById(R.id.t5);
        onlyshop = (RadioButton) findViewById(R.id.onlyshop);
        allshop = (RadioButton) findViewById(R.id.allshop);
        rb_limit_yes = (RadioButton) findViewById(R.id.rb_limit_yes);
        rb_limit_no = (RadioButton) findViewById(R.id.rb_limit_no);
        ll_limit = (LinearLayout) findViewById(R.id.ll_limit);
        et_number = (EditText) findViewById(R.id.et_number);

        String shopChain = pref.getString("shopChain", "");
        if (shopChain != null && shopChain.equals("0")) {
            allshop.setVisibility(View.GONE);
        } else {
            allshop.setVisibility(View.VISIBLE);
        }

        couponBatchCreditAmount = (DecimalEditText) findViewById(R.id.couponBatchCreditAmount);
        couponBatchCreditFull = (EditText) findViewById(R.id.couponBatchCreditFull);
        couponBatchCreditFull.setText("0");
        couponBatchCreditAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        couponBatchCreditAmount.setText(s);
                        couponBatchCreditAmount.setSelection(s.length());
                        ToastUtils.showShortToastSafe("最多输入两位小数");
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    couponBatchCreditAmount.setText(s);
                    couponBatchCreditAmount.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        couponBatchCreditAmount.setText(s.subSequence(0, 1));
                        couponBatchCreditAmount.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = couponBatchCreditAmount.getText().toString();
                y_money.setText(str);
            }
        });
        couponBatchCreditFull.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = couponBatchCreditFull.getText().toString();
                if (str.equals("0")) {
                    if ("1000002".equals(chooseKind)) {
                        couponBatchCreditFull.setText("");
                        toast("购物满额不能为0");
                    } else {
                        y_condition.setText("无条件抵用");
                    }
                } else {
                    y_condition.setText("满" + str + "可用");
                }
            }
        });
        couponBatchName = (EditText) findViewById(R.id.couponBatchName);
        couponBatchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = couponBatchName.getText().toString();
                y_title.setText(str);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        couponBatchNumber = (EditText) findViewById(R.id.couponBatchNumber);
        couponBatchNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = couponBatchNumber.getText().toString();
                y_number.setText(str);

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    String startTime = ed_time_one.getText().toString();
                    String endTime = ed_time_two.getText().toString();
                    setTotal(startTime, endTime);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        couponBatchExchangeAmount = (EditText) findViewById(R.id.couponBatchExchangeAmount);
        couponBatchDesc = (EditText) findViewById(R.id.couponBatchDesc);
        allshop = (RadioButton) findViewById(R.id.allshop);
        send = (TextView) findViewById(R.id.send);
        look = (TextView) findViewById(R.id.look);

        scll = (ScrollView) findViewById(R.id.scll);

        send.setOnClickListener(this);
        look.setOnClickListener(this);

        onlyshop.setOnClickListener(this);
        allshop.setOnClickListener(this);
        kindone.setOnClickListener(this);
        kindtwo.setOnClickListener(this);
        kindthree.setOnClickListener(this);
        ed_time_one.setOnClickListener(this);
        ed_time_two.setOnClickListener(this);
        image_rili_one.setOnClickListener(this);
        image_rili_two.setOnClickListener(this);
        rb_limit_yes.setOnClickListener(this);
        rb_limit_no.setOnClickListener(this);
        try {
            //获取经营类型
            requestrunkindlist();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Boolean judegnull() {

        if (chooseKind.equals("null")) {
            toast("选择优惠券类别");
            return false;
        }
        if (couponBatchName.getText().toString().equals("")) {
            toast("请输入主题");
            return false;
        }
        if (couponBatchCreditFull.getText().toString().equals("")) {
            toast("请输入满减额度");
            return false;
        }
        if (couponBatchNumber.getText().toString().equals("")) {
            toast("请输入发行数量");
            return false;
        } else {
            String couponNum = couponBatchNumber.getText().toString();
            int num = Integer.parseInt(couponNum);
            if (num == 0) {
                toast("发行数量必须大于0");
                return false;
            }
        }
        if (couponBatchExchangeAmount.getText().toString().equals("")) {
            toast("请输入兑换价值");
            return false;
        }
        if (ed_time_one.getText().toString().equals("")) {
            toast("请选择开始时间");
            return false;
        }
        if (ed_time_two.getText().toString().equals("")) {
            toast("请选择结束时间");
            return false;
        }
        if (chooseShop.equals("null")) {
            toast("请选择使用范围");
            return false;
        }
        if (getkindstr().length() == 0) {
            toast("请选择适用类别");
            return false;
        }
        if ("".equals(couponBatchDesc.getText().toString())) {
            toast("请输入使用说明");
            return false;
        }

        if (couponBatchCreditAmount.getText().toString().equals("")) {
            if ("1000001".equals(chooseKind)) {
                toast("请输入卷面金额");
                return false;
            } else {
                toast("请输入卷面金额");
                return false;
            }

        } else {
            if ("1000001".equals(chooseKind)) {
                double num = Double.parseDouble(couponBatchCreditAmount.getText().toString());
                if (num < 0 || num > 10 || num == 0|| num == 10) {
                    toast("折扣率请输入0～10之间数字!");
                    return false;
                }
                if (couponBatchCreditAmount.length() > 3) {
                    toast("折扣率最多一位小数");
                    return false;
                }
            } else {
                double num = Double.parseDouble(couponBatchCreditAmount.getText().toString());
                if (num == 0) {
                    toast("卷面金额必须大于0");
                    return false;
                }
            }

        }

        if ("1000002".equals(chooseKind)) {
            int amount = Integer.parseInt(couponBatchCreditAmount.getText().toString());
            int full = Integer.parseInt(couponBatchCreditFull.getText().toString());

            if (amount > full) {
                toast("抱歉，券面金额不能大于购物满额，请重新设置");
                return false;
            }
        }

        double zhekou = Double.parseDouble(couponBatchCreditAmount.getText().toString());
        double newnum = zhekou / 10;
        DecimalFormat df = new DecimalFormat("#.##");
        couponNum = df.format(newnum);
        start = (DateUtil.getStringToDate(ed_time_one.getText().toString())) / 1000;
        end = (DateUtil.getStringToDate(ed_time_two.getText().toString()) + 24 * 3600 * 1000L - 1000) / 1000;
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_back:
                KeyboardUtils.hideSoftInput(this);
                finish();
                break;
            case R.id.image_rili_one:
            case R.id.ed_rili_one:
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                //将月份转换为两位数
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
                                year2 = year;
                                month2 = monthOfYear;
                                day2 = dayOfMonth;
                                String startTime = year + "-" + mo + "-" + da;
                                ed_time_one.setText(startTime);
                                String endTime = ed_time_two.getText().toString();
                                if (!StringUtils.isEmpty(ed_time_two.getText().toString())) {
                                    setTotal(startTime, endTime);
                                }
                            }
                        }, year, month, day);
                dpd.setHasOptionsMenu(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setOkColor(getResources().getColor(R.color.blue));
                dpd.setAccentColor(getResources().getColor(R.color.blue));
                dpd.setTitle("开始日期");
                dpd.setOkText("");
                Calendar min = Calendar.getInstance();
                min.set(Calendar.DAY_OF_MONTH, day + 1);  //设置日期
                min.set(Calendar.MONTH, month);
                min.set(Calendar.YEAR, year);

                dpd.setMinDate(min);

                if (!StringUtils.isEmpty(ed_time_two.getText().toString())) {
                    Calendar max = Calendar.getInstance();
                    end = DateUtil.getStringToDate(ed_time_two.getText().toString()) + 24 * 3600 * 1000L - 1000;
                    max.setTimeInMillis(end);
                    dpd.setMaxDate(max);
                }
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "startDate");
                break;
            case R.id.image_rili_two:
            case R.id.ed_rili_two:
                if (StringUtils.isEmpty(ed_time_one.getText().toString())) {
                    toast("请先选择开始时间");
                    return;
                }

                DatePickerDialog endDpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                //将月份转换为两位数
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
                                //将设置的时间保存到Editext控件中
                                String endTime = year + "-" + mo + "-" + da;
                                ed_time_two.setText(endTime);
                                setTotal(ed_time_one.getText().toString(), endTime);
                            }
                        }, year, month, day);
                endDpd.setHasOptionsMenu(true);
                endDpd.setVersion(DatePickerDialog.Version.VERSION_2);
                endDpd.setOkColor(getResources().getColor(R.color.blue));
                endDpd.setAccentColor(getResources().getColor(R.color.blue));
                endDpd.setTitle("结束日期");
                endDpd.setOkText("");
                Calendar endMin = Calendar.getInstance();
                endMin.set(Calendar.DAY_OF_MONTH, day2);  //设置日期
                endMin.set(Calendar.MONTH, month2);
                endMin.set(Calendar.YEAR, year2);
                endDpd.setMinDate(endMin);
                endDpd.setCancelable(false);
                endDpd.autoDismiss(true);
                endDpd.show(getFragmentManager(), "endDate");
                break;
            case R.id.kindone:
                y_condition.setVisibility(View.VISIBLE);
                chooseKind = "1000002";
                y_kind.setText("满减券");
                y_kind.setTextColor(Color.parseColor("#51c7ff"));
                y_desc.setTextColor(Color.parseColor("#51c7ff"));
                kindmoney.setText("券面金额");
                kindkcondition.setText("购物满额");
                couponBatchCreditAmount.setHint("请填写券面金额");
                couponBatchCreditAmount.setMaxEms(4);
//                couponBatchCreditAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                yuan.setText("元");
                y_pre.setText("元");
                couponBatchCreditFull.setText("");
                img_left.setBackgroundResource(R.drawable.shape_manjianquan_left);
                img_right.setBackgroundResource(R.drawable.shape_manjianquan_right);
                ll_full.setVisibility(View.VISIBLE);
                break;
            case R.id.kindtwo:
                y_condition.setVisibility(View.VISIBLE);
                chooseKind = "1000001";
                y_kind.setText("折扣券");
                y_kind.setTextColor(Color.parseColor("#ef7a99"));
                y_desc.setTextColor(Color.parseColor("#ef7a99"));

                kindmoney.setText("折 扣 率");
                kindkcondition.setText("购物满额");
                couponBatchCreditAmount.setHint("请填写折扣率");
                couponBatchCreditAmount.setMaxEms(3);
//                couponBatchCreditAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                yuan.setText("折");
                y_pre.setText("折");
                couponBatchCreditFull.setText("");
                img_left.setBackgroundResource(R.drawable.shape_zhekouquan_left);
                img_right.setBackgroundResource(R.drawable.shape_zhekouquan_right);
                ll_full.setVisibility(View.VISIBLE);
                break;
            case R.id.kindthree:
                y_condition.setVisibility(View.VISIBLE);
                chooseKind = "1000000";
                y_kind.setText("代金券");
                img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);
                img_right.setBackgroundResource(R.drawable.shape_daijinquan_right);
                y_kind.setTextColor(Color.parseColor("#99CC00"));
                y_desc.setTextColor(Color.parseColor("#99CC00"));
                kindmoney.setText("券面金额");
                kindkcondition.setText("购物满额");
                couponBatchCreditAmount.setHint("请填写券面金额");
                couponBatchCreditAmount.setMaxEms(4);
//                couponBatchCreditAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                yuan.setText("元");
                y_pre.setText("元");
                couponBatchCreditFull.setText("0");
                ll_full.setVisibility(View.GONE);
                y_condition.setText("无条件抵用");
                break;
            case R.id.onlyshop:
                chooseShop = "0";
                break;
            case R.id.allshop:
                chooseShop = "1";
                break;
            case R.id.look:
                finish();
                break;
            case R.id.send:

                if (judegnull()) {
                    if (Utils.isNotFastClick()) {
                        if (editflag == 1) {
                            seneEditCoupon();
                        } else {
                            requestCreatCoupon();
                        }
                    }
                }
            case R.id.rb_limit_no:
                limit = 1;
                ll_limit.setVisibility(View.GONE);
                et_number.setText("");
                couponBatchNumber.setText("");
                couponBatchNumber.setEnabled(true);
                break;
            case R.id.rb_limit_yes:
                String startTime = rilistart.getText().toString();
                if ("".equals(startTime)) {
                    toast("请先选择开始时间");
                    rb_limit_yes.setChecked(false);
                    rb_limit_no.setChecked(true);
                    return;
                }
                if ("".equals(riliend.getText().toString())) {
                    toast("请先选择结束时间");
                    rb_limit_yes.setChecked(false);
                    rb_limit_no.setChecked(true);
                    return;
                }
                limit = 2;
                couponBatchNumber.setText("");
                couponBatchNumber.setEnabled(false);
                ll_limit.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setTotal(String startTime, String endTime) {
        Date sDate = DateUtil.strToDate(startTime);
        Date eDate = DateUtil.strToDate(endTime);
        int days = DateUtil.getGapCount(sDate, eDate) + 1;
        String s = et_number.getText().toString();
        if (!StringUtils.isEmpty(s)) {
            int num = Integer.parseInt(s);
            couponBatchNumber.setText(num * days + "");
        }
    }


    private void isedit() {
        editflag = getIntent().getIntExtra("couponedit", 0);
        chooseKind = getIntent().getStringExtra("coupontypeId");
        String cbafr = getIntent().getStringExtra("cbafr");
        String cbrnu = getIntent().getStringExtra("cbrnu");
        limit = getIntent().getIntExtra("cbrm", 1);
        String[] categorys = getIntent().getStringArrayExtra("categorys");
        if (categorys != null && categorys.length > 0) {
            productAdapter.setCheck(categorys);
            isAll = false;
        } else {
            isOnClick = true;
            isAll = true;
            allProduct.setChecked(true);
        }

        rule = getIntent().getIntExtra("rule", 0);
        if (rule == 0) {
            onlyshop.setChecked(true);
        } else if (rule == 1) {
            allshop.setChecked(true);
        }

        if (editflag == 1) {
            tv_title.setText(getString(R.string.title_bjyhq));
            coupontype.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            if (chooseKind.equals("1000000")) {
                y_condition.setVisibility(View.VISIBLE);
                ll_full.setVisibility(View.GONE);
                img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);
                img_right.setBackgroundResource(R.drawable.shape_daijinquan_right);
                y_kind.setText("代金券");
                y_pre.setText("元");
                y_kind.setTextColor(Color.parseColor("#99CC00"));
                y_desc.setTextColor(Color.parseColor("#99CC00"));
                kindmoney.setText("券面金额");
                kindkcondition.setText("购物满额");
                yuan.setText("元");
                y_money.setText(getIntent().getStringExtra("couponvalue"));//优惠券面额
                y_condition.setText("无条件抵用");//满减金额
                y_desc.setText(getIntent().getStringExtra("couponcategory"));//优惠券用途
                starttime.setText(getIntent().getStringExtra("couponstarttime"));//优惠券开始时间
                endtime.setText(getIntent().getStringExtra("couponendtime"));//优惠券使用截止时间
                couponBatchName.setText(getIntent().getStringExtra("coupontopic"));//优惠券主题
                couponBatchCreditAmount.setText(getIntent().getStringExtra("couponvalue"));//优惠券面额
                couponBatchNumber.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                y_number.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                couponBatchExchangeAmount.setText(getIntent().getStringExtra("couponexchange"));//优惠券兑换价值
                rilistart.setText(getIntent().getStringExtra("couponstarttime"));//开始时间
                riliend.setText(getIntent().getStringExtra("couponendtime"));//结束时间
                if (getIntent().getStringExtra("coupondesc").equals("null")) {
                    couponBatchDesc.setText("");
                } else {
                    couponBatchDesc.setText(getIntent().getStringExtra("coupondesc"));//优惠券使用说明
                }
            } else if (chooseKind.equals("1000001")) {
                y_condition.setVisibility(View.VISIBLE);
                ll_full.setVisibility(View.VISIBLE);
                img_left.setBackgroundResource(R.drawable.shape_zhekouquan_left);
                img_right.setBackgroundResource(R.drawable.shape_zhekouquan_right);
                y_pre.setText("折");
                y_kind.setText("折扣券");
                y_kind.setTextColor(Color.parseColor("#ef7a99"));
                y_desc.setTextColor(Color.parseColor("#ef7a99"));
                kindmoney.setText("折扣率");
                kindkcondition.setText("购物满额");
                yuan.setText("折");
                y_money.setText(getIntent().getStringExtra("coupondiscount"));//优惠折扣率
                //y_condition.setText(getIntent().getStringExtra("couponcondition"));//满减金额
                y_desc.setText(getIntent().getStringExtra("couponcategory"));//优惠券用途
                starttime.setText(getIntent().getStringExtra("couponstarttime"));//优惠券开始时间
                endtime.setText(getIntent().getStringExtra("couponendtime"));//优惠券使用截止时间
                couponBatchName.setText(getIntent().getStringExtra("coupontopic"));//优惠券主题
                couponBatchCreditAmount.setText(getIntent().getStringExtra("coupondiscount"));//优惠券折扣率
                couponBatchNumber.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                y_number.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                couponBatchExchangeAmount.setText(getIntent().getStringExtra("couponexchange"));//优惠券兑换价值
                rilistart.setText(getIntent().getStringExtra("couponstarttime"));//开始时间
                riliend.setText(getIntent().getStringExtra("couponendtime"));//结束时间
                couponBatchDesc.setText(getIntent().getStringExtra("coupondesc"));//优惠券使用说明
                if (getIntent().getStringExtra("couponcondition").equals("null")) {
                    couponBatchCreditFull.setText("该代金券暂时无消费额度限制");
                    couponBatchCreditFull.setFocusable(false);
                    couponBatchCreditFull.setTextColor(getResources().getColor(R.color.editext_word_color));
                    y_condition.setVisibility(View.GONE);//满减金额
                } else {
                    couponBatchCreditFull.setText(getIntent().getStringExtra("couponcondition"));//满减额度
                    y_condition.setText("满" + getIntent().getStringExtra("couponcondition") + "可用");//满减金额
                }
                if (getIntent().getStringExtra("coupondesc").equals("null")) {
                    couponBatchDesc.setText("");

                } else {
                    couponBatchDesc.setText(getIntent().getStringExtra("coupondesc"));//优惠券使用说明
                }
            } else if (chooseKind.equals("1000002")) {
                y_condition.setVisibility(View.VISIBLE);
                ll_full.setVisibility(View.VISIBLE);
                img_left.setBackgroundResource(R.drawable.shape_manjianquan_left);
                img_right.setBackgroundResource(R.drawable.shape_manjianquan_right);
                y_pre.setText("元");
                y_kind.setText("满减券");
                y_kind.setTextColor(Color.parseColor("#51c7ff"));
                y_desc.setTextColor(Color.parseColor("#51c7ff"));
                kindmoney.setText("面额");
                kindkcondition.setText("购物满额");
                yuan.setText("元");

                y_money.setText(getIntent().getStringExtra("couponvalue"));//优惠券面额
                //y_condition.setText(getIntent().getStringExtra("couponcondition"));//满减金额
                y_desc.setText(getIntent().getStringExtra("couponcategory"));//优惠券用途
                starttime.setText(getIntent().getStringExtra("couponstarttime"));//优惠券开始时间
                endtime.setText(getIntent().getStringExtra("couponendtime"));//优惠券使用截止时间
                couponBatchName.setText(getIntent().getStringExtra("coupontopic"));//优惠券主题
                couponBatchCreditAmount.setText(getIntent().getStringExtra("couponvalue"));//优惠券面额
                couponBatchNumber.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                y_number.setText(getIntent().getStringExtra("couponnumber") + "");//优惠券发行量
                couponBatchExchangeAmount.setText(getIntent().getStringExtra("couponexchange"));//优惠券兑换价值
                rilistart.setText(getIntent().getStringExtra("couponstarttime"));//开始时间
                riliend.setText(getIntent().getStringExtra("couponendtime"));//结束时间
                couponBatchDesc.setText(getIntent().getStringExtra("coupondesc"));//优惠券使用说明
                if (getIntent().getStringExtra("couponcondition").equals("null")) {
                    couponBatchCreditFull.setText("该代金券暂时无消费额度限制");
                    couponBatchCreditFull.setFocusable(false);
                    couponBatchCreditFull.setTextColor(getResources().getColor(R.color.editext_word_color));
                    y_condition.setVisibility(View.GONE);
                } else {
                    couponBatchCreditFull.setText(getIntent().getStringExtra("couponcondition"));//满减额度
                    y_condition.setText("满" + getIntent().getStringExtra("couponcondition") + "可用");//满减金额
                }
                if (getIntent().getStringExtra("coupondesc").equals("null")) {
                    couponBatchDesc.setText("");

                } else {
                    couponBatchDesc.setText(getIntent().getStringExtra("coupondesc"));//优惠券使用说明
                }
            }
        }

        if (limit == 1) {
            rb_limit_no.setChecked(true);
            et_number.setText("");
            ll_limit.setVisibility(View.GONE);
        } else if (limit == 2) {
            et_number.setText(cbrnu);
            ll_limit.setVisibility(View.VISIBLE);
            rb_limit_yes.setChecked(true);
        }

        if (cbafr != null) {

            if (mMyDialog == null) {
                mMyDialog = new My2BDialog(mContext);
            }
            mMyDialog.setTitle("驳回原因");
            mMyDialog.setMessage(cbafr);
            mMyDialog.setYesOnclickListener("确认修改", new My2BDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    mMyDialog.dismiss();
                }
            });
            mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    mMyDialog.dismiss();
                    finish();
                }
            });
            mMyDialog.show();
        }
    }

    /**
     * 适用类型
     */
    private void requestrunkindlist() {
        showProgressDialog();
        String urlString = "business/shop_cate.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("level", "3");//优惠券批次ID必选
        map.put("page", "1");//主题
        map.put("page_length", "50");//主题
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
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                                rk = runKindList.getData();
                                productAdapter.setList(rk);
                                if (editflag == 1) {
                                    isedit();
                                } else {
                                    //默认代金券
                                    chooseKind = "1000000";
                                    y_kind.setText("代金券");
                                    img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);
                                    img_right.setBackgroundResource(R.drawable.shape_daijinquan_right);
                                    y_kind.setTextColor(Color.parseColor("#99CC00"));
                                    y_desc.setTextColor(Color.parseColor("#99CC00"));
                                    kindmoney.setText("券面金额");
                                    kindkcondition.setText("满减额度");
                                    yuan.setText("元");
                                }
                            } else {

                            }
                        } catch (JSONException e) {
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

    private String getkindstr() {
        String s = "";
        for (int i = 0; i < kindsid.size(); i++) {
            s += kindsid.get(i) + ",";
        }
        return s;
    }

    private void seneEditCoupon() {
        showProgressDialog();
        String urlString = "coupon/update_coupon.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("cid", getIntent().getStringExtra("couponId"));
        Log.i("cid", getIntent().getStringExtra("couponId"));
        map.put("couponBatchTargetId", pref.getString("sid", ""));
        map.put("couponTypeId", chooseKind);//优惠券批次ID必选
        map.put("couponBatchName", couponBatchName.getText().toString());//主题
        map.put("couponBatchNumber", couponBatchNumber.getText().toString());
        if (chooseKind.equals("1000001")) {

            map.put("couponBatchRebate", couponNum);//折扣
            map.put("couponBatchCreditFull", couponBatchCreditFull.getText().toString());//折扣最高抵用
        } else {
            if (getIntent().getStringExtra("couponcondition").equals("null")) {
                map.put("couponBatchCreditFull", "");
            } else {
                map.put("couponBatchCreditFull", couponBatchCreditFull.getText().toString());
            }
            map.put("couponBatchCreditAmount", couponBatchCreditAmount.getText().toString());//代金金额
        }
        map.put("couponBatchExchangeAmount", couponBatchExchangeAmount.getText().toString());
        map.put("couponBatchValidStartDate", start + "");
        map.put("couponBatchValidEndDate", end + "");
        map.put("rule", chooseShop);//只是本店or全部 0 本店  1全部连锁
        if (getIntent().getStringExtra("coupondesc").equals("null")) {
            map.put("couponBatchDesc", "");
        } else {
            map.put("couponBatchDesc", couponBatchDesc.getText().toString());
        }
        map.put("couponBatchTargetType", "2");
        map.put("couponBatchReceiveMode", limit + "");
        if (limit == 2) {
            map.put("couponBatchReceiveNumber", et_number.getText().toString() + "");//每日限量
        } else {
            map.put("couponBatchReceiveNumber", "");//每日限量
        }
        if (isAll) {
            map.put("categorys", "");
        } else {
            map.put("categorys", getkindstr().substring(0, getkindstr().length() - 1));
        }
        map.put("couponBatchAvailableTargetIds", pref.getString("sid", ""));

        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                Log.i("paramssssss", responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("修改成功");
                                EventBus.getDefault().post(new OnRefreshEvent(true));
                                finish();
                            } else {
                                toast("修改失败");
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

    private void requestCreatCoupon() {
        showProgressDialog();
        String urlString = "coupon/createCouponBatch.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("couponBatchTargetId", pref.getString("sid", ""));
        map.put("couponTypeId", chooseKind);//优惠券批次ID必选
        map.put("couponBatchName", couponBatchName.getText().toString());//主题
        map.put("couponBatchNumber", couponBatchNumber.getText().toString());
        if (chooseKind.equals("1000001")) {
            map.put("couponBatchRebate", couponNum);//折扣
            map.put("couponBatchCreditFull", couponBatchCreditFull.getText().toString());//折扣最高抵用
        } else {
            map.put("couponBatchCreditFull", couponBatchCreditFull.getText().toString());
            map.put("couponBatchCreditAmount", couponBatchCreditAmount.getText().toString());//代金金额
        }
        map.put("couponBatchExchangeAmount", couponBatchExchangeAmount.getText().toString());

        map.put("couponBatchValidStartDate", start + "");
        map.put("couponBatchValidEndDate", end + "");
        map.put("rule", chooseShop);//只是本店or全部 0 本店  1全部连锁
        map.put("couponBatchDesc", couponBatchDesc.getText().toString());
        map.put("couponBatchTargetType", "2");
        map.put("couponBatchReceiveMode", limit + "");
        if (limit == 2) {
            map.put("couponBatchReceiveNumber", et_number.getText().toString() + "");//每日限量
        } else {
            map.put("couponBatchReceiveNumber", "");//每日限量
        }
        if (isAll) {
            map.put("categorys", "");
        } else {
            map.put("categorys", getkindstr().substring(0, getkindstr().length() - 1));
        }
        map.put("couponBatchAvailableTargetIds", pref.getString("sid", ""));

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
                                toast("创建成功");
                                EventBus.getDefault().post(new OnRefreshEvent(true));
                                finish();
                            } else {
                                toast("创建失败");
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
