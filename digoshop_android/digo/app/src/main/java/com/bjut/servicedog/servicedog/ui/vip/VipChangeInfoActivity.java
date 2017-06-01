package com.bjut.servicedog.servicedog.ui.vip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.VipInfo;
import com.bjut.servicedog.servicedog.po.VipSearch;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.RegularUtils;
import com.bjut.servicedog.servicedog.view.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VipChangeInfoActivity extends BaseActivity {

    private EditText vip_name, vip_job, vip_email, vip_address, vip_phone;
    private TextView cancel, save, vip_birthday, vip_level, vip_code, vip_codetop, status, vip_leveltop, shopname;
    private RadioGroup vip_sex;
    private RadioButton no, nan, nv;
    private int sex = 3, year, month, day, hours, minute, second, ifedit = 1;//0不可编辑,1可编辑
    private Calendar c;
    private String timepre;
    private CircleImageView circleImageView;
    private TextView tv_title;
    private long data;
    private String vipId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_change_info);
        init();
        requestSearchVip();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xghyxx));
        circleImageView = (CircleImageView) findViewById(R.id.vip_logo);
        MyImageLoder.getInstance().loadCircle(mContext,pref.getString("shopimage", ""), circleImageView);
        shopname = (TextView) findViewById(R.id.shop_name);
        shopname.setText(pref.getString("shopname", ""));
        vip_code = (TextView) findViewById(R.id.vip_jifen);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        vip_leveltop = (TextView) findViewById(R.id.vip_leveltop);
        vip_codetop = (TextView) findViewById(R.id.vip_codetop);
        vip_phone = (EditText) findViewById(R.id.vip_phone);
        vip_name = (EditText) findViewById(R.id.vip_name);
        vip_birthday = (TextView) findViewById(R.id.vip_birthday);
        vip_job = (EditText) findViewById(R.id.vip_job);
        vip_email = (EditText) findViewById(R.id.vip_email);
        vip_address = (EditText) findViewById(R.id.vip_address);
//        vip_birthday.setOnClickListener(this);
        vip_sex = (RadioGroup) findViewById(R.id.vip_sex);
        no = (RadioButton) findViewById(R.id.no);
        nan = (RadioButton) findViewById(R.id.nan);
        nv = (RadioButton) findViewById(R.id.nv);
        vip_sex.setOnCheckedChangeListener(mChangeRadio);
//        vip_phone.setEnabled(false);

        vipId = getIntent().getStringExtra("vipId");
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        setEdit(true);
    }

    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == nan.getId()) {
                sex = 1;
            } else if (checkedId == nv.getId()) {
                sex = 2;
            } else {
                sex = 0;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vip_birthday:
                DatePickerDialog dpd = DatePickerDialog.newInstance(
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
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                String time = year + mo + da;
                                vip_birthday.setText(time);
                            }
                        }, 1990, 0, 1);
                dpd.showYearPickerFirst(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setOkColor(getResources().getColor(R.color.blue));
                dpd.setAccentColor(getResources().getColor(R.color.blue));
                dpd.setTitle("出生日期");
                dpd.setOkText("");
                dpd.setCancelable(false);
                dpd.setMaxDate(c);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "startDate");
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                if (ifedit == 1) {
                    if (vip_phone.getText().toString().equals("")) {
                        toast("手机号不能为空");
                    } else if (vip_name.getText().toString().equals("")) {
                        toast("姓名不能为空");
                    } else if (!RegularUtils.isMobileNO(vip_phone.getText().toString())) {
                        toast("手机号格式不正确");
                    } else {
                        requestEditVip();
                    }
                }
                break;
        }
    }

    private void setEdit(boolean a) {
        vip_phone.setEnabled(a);
        vip_name.setEnabled(a);
        no.setEnabled(a);
        nan.setEnabled(a);
        nv.setEnabled(a);
        if (a) {
            vip_birthday.setOnClickListener(this);
        } else {
            vip_birthday.setOnClickListener(null);
        }
        vip_job.setEnabled(a);
        vip_email.setEnabled(a);
        vip_address.setEnabled(a);

    }

    private void requestSearchVip() {
        showProgressDialog();
        String urlString = "relation/get_vipinfo.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("vip_id", vipId);

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
                            VipSearch vipSearch = JSON.parseObject(json, VipSearch.class);
                            if (vipSearch.getE().getCode() == 0) {
                                setView(vipSearch.getData());

                            } else {

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

    private void requestEditVip() {

        String sid = pref.getString("sid", "");
        showProgressDialog();
        String urlString = "relation/update_vipinfo.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("user_name", vip_name.getText().toString());
        map.put("gen", sex + "");
        map.put("birthday", vip_birthday.getText().toString());
        map.put("occupation", vip_job.getText().toString());
        map.put("mobile", vip_phone.getText().toString());
        map.put("email", vip_email.getText().toString());
        map.put("address", vip_address.getText().toString());
        map.put("opuid", pref.getString("uid", ""));
        map.put("sid", sid);
        map.put("vid", vipId);

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
                                toast("保存成功");
                                ifedit = 0;
                                save.setBackgroundResource(R.color.line);
                                cancel.setBackgroundResource(R.color.line);
                                finish();
                            } else {
                                toast(j.getJSONObject("e").getString("desc"));
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

    private void setView(VipInfo vipInfo) {

        if (vipInfo.getGen() == 2) {
            nv.setChecked(true);
        } else if (vipInfo.getGen() == 1) {
            nan.setChecked(true);
        } else {
            no.setChecked(true);
        }
        vip_code.setText(vipInfo.getIntg() + "");
        vip_name.setText(vipInfo.getUser_name());
        vip_phone.setText(vipInfo.getMobile());
        String birthday = vipInfo.getBirthday();
        vip_birthday.setText(birthday);
        vip_job.setText(vipInfo.getOccupation());
        vip_email.setText(vipInfo.getEmail());
        vip_address.setText(vipInfo.getAddress());
        vip_leveltop.setText(vipInfo.getVip_level_name());

        vip_codetop.setText(vipInfo.getVip_code());


    }
}
