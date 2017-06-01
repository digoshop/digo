package com.bjut.servicedog.servicedog.ui.coupon;

import android.os.Bundle;
import android.support.annotation.IdRes;
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
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.po.CouponBatch;
import com.bjut.servicedog.servicedog.po.CouponDetail;
import com.bjut.servicedog.servicedog.po.CouponType;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.StringUtils;
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
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by beibeizhu on 17/5/24.
 */

public class UpdateCouponActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_coupon_type;
    private RadioGroup rg_coupon_zeng;
    private RadioButton rb_normal;
    private RadioButton rb_condition;
    private RadioGroup rg_limit;
    private RadioButton rb_limit_no;
    private RadioButton rb_limit_yes;
    private RadioGroup rg_range;
    private RadioButton rb_only_shop;
    private RadioButton rb_all_shop;
    private LinearLayout ll_zeng;
    private EditText et_coupun_theme;
    private LinearLayout ll_full;
    private TextView tv_money_title;
    private DecimalEditText et_coupun_money;
    private EditText et_coupon_zeng;
    private TextView tv_danwei;
    private EditText et_coupun_full;
    private LinearLayout ll_limit;
    private EditText et_limit_number;
    private EditText et_coupun_number;
    private EditText et_coupun_amount;
    private TextView tv_date_start;
    private TextView tv_date_end;
    private ImageView img_date_start;
    private ImageView img_date_end;
    private EditText et_coupun_explain;
    private Button btn_cancle;
    private Button btn_submit;

    private String couponType = Constant.COUPON_DAI;
    private int limit = Constant.LIMIT_NO;
    private int payment = Constant.PAYMENT_NORMAL;
    private int year, month, day;
    private String zheKouStr = "";
    private String startDate = "";
    private String endDate = "";
    private int range = Constant.RANGE_ONLY_SHOP;
    private String couponBatchId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initViews();
        setListener();
        requestCouponDetail();
    }

    private int getViewId() {
        return R.layout.activity_update_coupon;
    }

    private void setListener() {
        img_date_start.setOnClickListener(this);
        img_date_end.setOnClickListener(this);
        tv_date_start.setOnClickListener(this);
        tv_date_end.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        rg_coupon_zeng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_normal:
                        payment = Constant.PAYMENT_NORMAL;
                        ll_zeng.setVisibility(View.GONE);
                        break;
                    case R.id.rb_condition:
                        payment = Constant.PAYMENT_ZENG;
                        ll_zeng.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        rg_limit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_limit_no:
                        limit = Constant.LIMIT_NO;
                        ll_limit.setVisibility(View.GONE);
                        et_limit_number.setText("");
                        et_coupun_number.setText("");
                        et_coupun_number.setEnabled(true);
                        break;
                    case R.id.rb_limit_yes:
                        String startTime = tv_date_start.getText().toString();
                        if (StringUtils.isEmpty(startTime)) {
                            toast("请先选择开始时间");
                            rb_limit_yes.setChecked(false);
                            rb_limit_no.setChecked(true);
                            return;
                        }
                        if (StringUtils.isEmpty(tv_date_end.getText().toString())) {
                            toast("请先选择结束时间");
                            rb_limit_yes.setChecked(false);
                            rb_limit_no.setChecked(true);
                            return;
                        }
                        limit = Constant.LIMIT_YES;
                        ll_limit.setVisibility(View.VISIBLE);
                        et_limit_number.setText("");
                        et_coupun_number.setText("");
                        et_coupun_number.setEnabled(false);
                        break;
                }
            }
        });

        rg_range.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_only_shop:
                        range = Constant.RANGE_ONLY_SHOP;
                        break;
                    case R.id.rb_all_shop:
                        range = Constant.RANGE_ALL_SHOP;
                        break;
                }
            }
        });

        et_limit_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    String startTime = tv_date_start.getText().toString();
                    String endTime = tv_date_end.getText().toString();
                    setTotal(startTime, endTime);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initViews() {
        tv_title = $(R.id.tv_title);
        tv_coupon_type = $(R.id.tv_coupon_type);
        rg_coupon_zeng = $(R.id.rg_coupon_zeng);
        rb_normal = $(R.id.rb_normal);
        rb_condition = $(R.id.rb_condition);
        rg_limit = $(R.id.rg_limit);
        ll_limit = $(R.id.ll_limit);
        rb_limit_no = $(R.id.rb_limit_no);
        rb_limit_yes = $(R.id.rb_limit_yes);
        rg_range = $(R.id.rg_range);
        rb_only_shop = $(R.id.rb_only_shop);
        rb_all_shop = $(R.id.rb_all_shop);
        et_coupun_theme = $(R.id.et_coupun_theme);
        ll_full = $(R.id.ll_full);
        ll_zeng = $(R.id.ll_zeng);
        tv_danwei = $(R.id.tv_danwei);
        tv_money_title = $(R.id.tv_money_title);
        et_coupun_money = $(R.id.et_coupun_money);
        et_coupun_full = $(R.id.et_coupun_full);
        et_coupon_zeng = $(R.id.et_coupon_zeng);
        et_limit_number = $(R.id.et_limit_number);
        et_coupun_number = $(R.id.et_coupun_number);
        et_coupun_amount = $(R.id.et_coupun_amount);
        tv_date_start = $(R.id.tv_date_start);
        tv_date_end = $(R.id.tv_date_end);
        img_date_start = $(R.id.img_date_start);
        img_date_end = $(R.id.img_date_end);
        et_coupun_explain = $(R.id.et_coupun_explain);
        btn_cancle = $(R.id.btn_cancle);
        btn_submit = $(R.id.btn_submit);

        tv_title.setText(getString(R.string.title_bjyhq));
        couponBatchId = getIntent().getStringExtra("couponBatchId");

        String cbafr = getIntent().getStringExtra("cbafr");
        if (!StringUtils.isEmpty(cbafr)) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_date_start:
            case R.id.tv_date_start:
                Calendar calendar = Calendar.getInstance();
                //取得日历的信息
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
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
                                String startTime = year + "-" + mo + "-" + da;
                                tv_date_start.setText(startTime);
                                String endTime = tv_date_end.getText().toString();
                                if (!StringUtils.isEmpty(tv_date_end.getText().toString())) {
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

                if (!StringUtils.isEmpty(tv_date_end.getText().toString())) {
                    Calendar max = Calendar.getInstance();
                    long end = DateUtil.getStringToDate(tv_date_end.getText().toString()) + 24 * 3600 * 1000L - 1000;
                    max.setTimeInMillis(end);
                    dpd.setMaxDate(max);
                }
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "startDate");
                break;
            case R.id.img_date_end:
            case R.id.tv_date_end:
                if (StringUtils.isEmpty(tv_date_start.getText().toString())) {
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
                                tv_date_end.setText(endTime);
                                setTotal(tv_date_start.getText().toString(), endTime);
                            }
                        }, year, month, day);
                endDpd.setHasOptionsMenu(true);
                endDpd.setVersion(DatePickerDialog.Version.VERSION_2);
                endDpd.setOkColor(getResources().getColor(R.color.blue));
                endDpd.setAccentColor(getResources().getColor(R.color.blue));
                endDpd.setTitle("结束日期");
                endDpd.setOkText("");
                if (!StringUtils.isEmpty(tv_date_start.getText().toString())) {
                    Calendar endMin = Calendar.getInstance();
                    long end = DateUtil.getStringToDate(tv_date_start.getText().toString()) + 24 * 3600 * 1000L - 1000;
                    endMin.setTimeInMillis(end);
                    endDpd.setMinDate(endMin);
                }
                endDpd.setCancelable(false);
                endDpd.autoDismiss(true);
                endDpd.show(getFragmentManager(), "endDate");
                break;

            case R.id.btn_cancle:
                this.finish();
                break;
            case R.id.btn_submit:
                if (checkinput()) {
                    updateCouponInfo();
                }
                break;
        }
    }

    private void setTotal(String startTime, String endTime) {
        Date sDate = DateUtil.strToDate(startTime);
        Date eDate = DateUtil.strToDate(endTime);
        int days = DateUtil.getGapCount(sDate, eDate) + 1;
        String s = et_limit_number.getText().toString();
        if (!StringUtils.isEmpty(s)) {
            int num = Integer.parseInt(s);
            et_coupun_number.setText(num * days + "");
        }
    }

    private Boolean checkinput() {

        if (StringUtils.isEmpty(et_coupun_theme.getText().toString())) {
            toast("请输入主题");
            return false;
        }

        if (StringUtils.isEmpty(et_coupun_money.getText().toString())) {
            toast("请输入券面金额");
            return false;
        } else {
            String couponNum = et_coupun_money.getText().toString();
            Double num = Double.parseDouble(couponNum);
            if (num <= 0) {
                toast("券面金额必须大于0");
                return false;
            }
        }

        if (payment != Constant.PAYMENT_NORMAL) {
            if (StringUtils.isEmpty(et_coupon_zeng.getText().toString())) {
                toast("请输入满足额度");
                return false;
            } else {
                String couponNum = et_coupon_zeng.getText().toString();
                int num = Integer.parseInt(couponNum);
                if (num <= 0) {
                    toast("满足额度必须大于0");
                    return false;
                }
            }
        }

        if (couponType != Constant.COUPON_DAI) {
            if (StringUtils.isEmpty(et_coupun_full.getText().toString())) {
                toast("请输入满减额度");
                return false;
            } else {
                String couponNum = et_coupun_full.getText().toString();
                int num = Integer.parseInt(couponNum);
                if (num <= 0) {
                    toast("满减额度必须大于0");
                    return false;
                }
            }
        }

        if (limit != Constant.LIMIT_NO) {
            if (StringUtils.isEmpty(et_limit_number.getText().toString())) {
                toast("请输入每日限量");
                return false;
            } else {
                String couponNum = et_limit_number.getText().toString();
                int num = Integer.parseInt(couponNum);
                if (num <= 0) {
                    toast("每日限量必须大于0");
                    return false;
                }
            }
        } else {
            if (StringUtils.isEmpty(et_coupun_number.getText().toString())) {
                toast("请输入发行数量");
                return false;
            } else {
                String couponNum = et_coupun_number.getText().toString();
                int num = Integer.parseInt(couponNum);
                if (num <= 0) {
                    toast("发行数量必须大于0");
                    return false;
                }
            }
        }

        if (et_coupun_amount.getText().toString().equals("")) {
            toast("请输入兑换价值");
            return false;
        } else {
            String couponNum = et_coupun_amount.getText().toString();
            int num = Integer.parseInt(couponNum);
            if (num <= 0) {
                toast("兑换价值必须大于0");
                return false;
            }
        }

        if (tv_date_start.getText().toString().equals("")) {
            toast("请选择开始时间");
            return false;
        } else {
            startDate = (DateUtil.getStringToDate(tv_date_start.getText().toString())) / 1000 + "";
        }

        if (tv_date_end.getText().toString().equals("")) {
            toast("请选择结束时间");
            return false;
        } else {

            endDate = (DateUtil.getStringToDate(tv_date_end.getText().toString()) + 24 * 3600 * 1000L - 1000) / 1000 + "";
        }

        if ("".equals(et_coupun_explain.getText().toString())) {
            toast("请输入使用说明");
            return false;
        }


        if (couponType == Constant.COUPON_ZHE) {
            if (StringUtils.isEmpty(et_coupun_money.getText().toString())) {
                toast("请输入折扣率");
                return false;
            } else {
                String zhe = et_coupun_money.getText().toString();
                double zheKou = Double.parseDouble(zhe);
                if (zheKou < 0 || zheKou > 10 || zheKou == 0 || zheKou == 10) {
                    toast("折扣率请输入0～10之间数字!");
                    return false;
                }
                if (zhe.length() > 3) {
                    toast("折扣率最多一位小数");
                    return false;
                }
                zheKou = zheKou / 10;
                DecimalFormat df = new DecimalFormat("#.##");
                zheKouStr = df.format(zheKou);
            }
        }

        if (Constant.COUPON_MAN.equals(couponType)) {
            int amount = Integer.parseInt(et_coupun_money.getText().toString());
            int full = Integer.parseInt(et_coupun_full.getText().toString());

            if (amount > full) {
                toast("抱歉，券面金额不能大于购物满额，请重新设置");
                return false;
            }
        }
        return true;
    }

    private void setView(CouponDetail couponDetail) {
        CouponBatch couponBatch = couponDetail.getData().getCouponBatch();
        CouponType couponTypes = couponDetail.getData().getCouponType();

        int cbrm = couponBatch.getCbrm();
        String ctid = couponTypes.getCtid();
        String dateStart = DateUtil.strFormatStr(couponBatch.getStarTime());
        String dateEnd = DateUtil.strFormatStr(couponBatch.getEndTime());
        int cbrt = couponBatch.getCbrt();
        int rule = couponBatch.getRule();

        switch (ctid) {
            case Constant.COUPON_DAI:
                tv_coupon_type.setText(getString(R.string.daijinquan));
                couponType = Constant.COUPON_DAI;
                tv_money_title.setText("券面金额");
                et_coupun_money.setText(couponBatch.getCbca());
                tv_danwei.setText(getString(R.string.yuan));
                ll_full.setVisibility(View.GONE);
                et_coupun_money.setHint("券面金额最多4位");
                break;
            case Constant.COUPON_ZHE:
                tv_coupon_type.setText(getString(R.string.zhekouquan));
                couponType = Constant.COUPON_ZHE;
                tv_money_title.setText("折扣率");
                et_coupun_money.setText(couponBatch.getCbrStr());
                tv_danwei.setText(getString(R.string.zhe));
                ll_full.setVisibility(View.VISIBLE);
                et_coupun_full.setText(couponBatch.getCbcf());
                et_coupun_money.setHint("折扣率最多一位小数");
                break;
            case Constant.COUPON_MAN:
                tv_coupon_type.setText(getString(R.string.manjianquan));
                couponType = Constant.COUPON_MAN;
                tv_money_title.setText("券面金额");
                et_coupun_money.setText(couponBatch.getCbca());
                tv_danwei.setText(getString(R.string.yuan));
                ll_full.setVisibility(View.VISIBLE);
                et_coupun_full.setText(couponBatch.getCbcf());
                et_coupun_money.setHint("券面金额多高4位");
                break;
        }

        switch (cbrt) {
            case Constant.PAYMENT_NORMAL:
                ll_zeng.setVisibility(View.GONE);
                payment = Constant.PAYMENT_NORMAL;
                rb_normal.setChecked(true);
                break;
            case Constant.PAYMENT_ZENG:
                ll_zeng.setVisibility(View.VISIBLE);
                payment = Constant.PAYMENT_ZENG;
                et_coupon_zeng.setText(couponBatch.getCbcpa());
                rb_condition.setChecked(true);
                break;
            default:
                ll_zeng.setVisibility(View.GONE);
                payment = Constant.PAYMENT_NORMAL;
                rb_normal.setChecked(true);
                break;
        }

        et_coupun_theme.setText(couponBatch.getCbn());
        tv_date_start.setText(dateStart);
        tv_date_end.setText(dateEnd);


        switch (cbrm) {
            case Constant.LIMIT_YES:
                rb_limit_yes.setChecked(true);
                limit = Constant.LIMIT_YES;
                ll_limit.setVisibility(View.VISIBLE);
                et_limit_number.setText(couponBatch.getCbrnu());
                break;
            case Constant.LIMIT_NO:
                rb_limit_no.setChecked(true);
                limit = Constant.LIMIT_NO;
                ll_limit.setVisibility(View.GONE);
                break;
            default:
                rb_limit_no.setChecked(true);
                limit = Constant.LIMIT_NO;
                ll_limit.setVisibility(View.GONE);
                break;
        }

        switch (rule) {
            case Constant.RANGE_ONLY_SHOP:
                range = Constant.RANGE_ONLY_SHOP;
                rb_only_shop.setChecked(true);
                break;
            case Constant.RANGE_ALL_SHOP:
                rb_all_shop.setChecked(true);
                range = Constant.RANGE_ALL_SHOP;
                break;
        }


        et_coupun_number.setText(couponBatch.getCbnu() + "");
        et_coupun_amount.setText(couponBatch.getCbea() + "");

        et_coupun_explain.setText(couponBatch.getCbd());
    }

    private void updateCouponInfo() {
        showProgressDialog();
        String urlString = "coupon/update_coupon.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("cid", couponBatchId);
        map.put("couponBatchTargetId", pref.getString("sid", ""));
        map.put("couponTypeId", couponType);//优惠券类型
        map.put("couponBatchName", et_coupun_theme.getText().toString());//主题
        map.put("couponBatchNumber", et_coupun_number.getText().toString());

        if (couponType.equals(Constant.COUPON_ZHE)) {
            map.put("couponBatchRebate", zheKouStr);//折扣
            map.put("couponBatchCreditFull", et_coupun_full.getText().toString());//折扣最高抵用
        } else {
            map.put("couponBatchCreditAmount", et_coupun_money.getText().toString());//代金金额
            map.put("couponBatchCreditFull", et_coupun_full.getText().toString());
        }
        map.put("couponBatchExchangeAmount", et_coupun_amount.getText().toString());
        map.put("couponBatchValidStartDate", startDate + "");
        map.put("couponBatchValidEndDate", endDate + "");
        map.put("rule", range + "");//只是本店or全部 0 本店  1全部连锁
        map.put("couponBatchDesc", et_coupun_explain.getText().toString());
        map.put("couponBatchTargetType", "2");
        map.put("couponBatchReceiveMode", limit + "");
        if (limit == Constant.LIMIT_YES) {
            map.put("couponBatchReceiveNumber", et_limit_number.getText().toString() + "");//每日限量
        } else {
            map.put("couponBatchReceiveNumber", "");//每日限量
        }

        map.put("couponBatchReceiveType", payment + "");
        if (payment == Constant.PAYMENT_ZENG) {
            map.put("couponBatchConsumptionAmount", et_coupon_zeng.getText().toString() + "");//消费满多少送
        } else {
            map.put("couponBatchConsumptionAmount", "");//正常领取
        }
        map.put("categorys", "");
        map.put("couponBatchAvailableTargetIds", pref.getString("sid", ""));

        RequestParams params = sortMapByKey(map);

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


    public void requestCouponDetail() {
        showProgressDialog();
        String urlString = "coupon/query_batch_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("couponBatchId", couponBatchId);
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
                        Log.i("yyyy", json);
                        try {
                            CouponDetail couponDetail = JSON.parseObject(json, CouponDetail.class);
                            if (couponDetail.getE().getCode() == 0) {
                                Log.i("yyyy", couponDetail.getData().getCouponBatch().getStatus() + "");
                                setView(couponDetail);
                            } else {
                                toast(couponDetail.getE().getDesc());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
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
}
