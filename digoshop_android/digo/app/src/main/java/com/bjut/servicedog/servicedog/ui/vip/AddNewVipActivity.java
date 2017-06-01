package com.bjut.servicedog.servicedog.ui.vip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.VipInfo;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddNewVipActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView circleImageView;
    private TextView shopname, codenumber, vipjifen, cancel, save, vip_level, vip_birthday;
    private EditText phonenumber, name, vip_job, vip_email, vip_addrdss;
    private Calendar c;
    private int year, month, day, hours, minute, second, sex = 3, canChoose = 0, intg = 0;
    private String timepre = "", str, vip_phonenumber, uid, nick, vip_level_name, mobile;//用来记录时间
    private boolean is_vip, ifadd = false;
    private RadioGroup vip_sex;
    private RadioButton no, nan, nv;
    private TextView tv_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vip_activity);
        init();
    }

    private void init() {

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xzhy));
        circleImageView = (CircleImageView) findViewById(R.id.vip_logo);
        MyImageLoder.getInstance().loadCircle(mContext,pref.getString("shopimage", ""), circleImageView);
        shopname = (TextView) findViewById(R.id.shop_name);
        shopname.setText(pref.getString("shopname", ""));
        codenumber = (TextView) findViewById(R.id.vip_codetop);
        vipjifen = (TextView) findViewById(R.id.vip_jifen);
        vip_level = (TextView) findViewById(R.id.vip_leveltop);
        cancel = (TextView) findViewById(R.id.cancel);
        save = (TextView) findViewById(R.id.save);
        phonenumber = (EditText) findViewById(R.id.vip_phone);
        name = (EditText) findViewById(R.id.vip_name);
        vip_sex = (RadioGroup) findViewById(R.id.vip_sex);
        no = (RadioButton) findViewById(R.id.no);
        nan = (RadioButton) findViewById(R.id.nan);
        nv = (RadioButton) findViewById(R.id.nv);
        vip_birthday = (TextView) findViewById(R.id.vip_birthday);
        vip_job = (EditText) findViewById(R.id.vip_job);
        vip_email = (EditText) findViewById(R.id.vip_email);
        vip_addrdss = (EditText) findViewById(R.id.vip_address);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        vip_birthday.setOnClickListener(this);

        phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ifadd = false;
                vipjifen.setText("0");
                vip_level.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (phonenumber.getText().toString().length() == 11) {
                    jundgeVip();
                }
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

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
                                timepre = year + mo + da;
                                vip_birthday.setText(timepre);
                            }
                        }, 1990, 0, 1);
                dpd.showYearPickerFirst(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setOkColor(getResources().getColor(R.color.blue));
                dpd.setAccentColor(getResources().getColor(R.color.blue));
                dpd.setTitle("出生日期");
                dpd.setOkText("");
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.setMaxDate(c);
                dpd.show(getFragmentManager(), "startDate");
                break;
            case R.id.save:
                jundgeData();
                break;
            case R.id.cancel:
                dialog();
                break;
        }
    }

    private void jundgeVip() {
        String urlString = "passport/user_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phonenumber.getText().toString());
        map.put("sid", pref.getString("sid", ""));
        map.put("v", 1 + "");
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
                        long sid = Long.parseLong(pref.getString("sid", "")) << 2;
                        Log.i("hhhh", sid + "");
                        try {
                            str = sid + "" + DateUtil.currentLong();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        codenumber.setText(str);

                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                is_vip = j.getJSONObject("data").getBoolean("is_vip");
                                if (is_vip) {
                                    ifadd = false;
                                    vipjifen.setText(intg + "");
                                    vip_level.setText(vip_level_name);
                                    toast("您的手机号已经注册过会员了,请重试");
                                } else {
                                    uid = j.getJSONObject("data").getString("uid");
                                    intg = j.getJSONObject("data").getInt("intg");
                                    nick = j.getJSONObject("data").getString("nick");
                                    vip_level_name = j.getJSONObject("data").getString("vip_level_name");
                                    mobile = j.getJSONObject("data").getString("mobile");

                                    ifadd = true;
                                    vipjifen.setText(intg + "");
                                    vip_level.setText(vip_level_name);
                                    VipInfo vipInfo = JSON.parseObject(j.getJSONObject("data").getJSONObject("user_info").toString(), VipInfo.class);
                                    setInfo(vipInfo);
                                    toast("手机号有效,可以添加会员");
                                }
                            } else if (j.getJSONObject("e").getInt("code") == -1704) {
                                toast("积分不足,无法成为会员");
                            } else if (j.getJSONObject("e").getInt("code") == -109) {
                                toast("您尚未注册成为系统会员，请先下载迪购APP注册或关注迪购商家微信号");
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

    private void setInfo(VipInfo vipInfo) {
        name.setText(vipInfo.getReal_name());
        sex = vipInfo.getGender();
        switch (vipInfo.getGender()) {
            case 0:
                no.setChecked(true);
                break;
            case 1:
                nan.setChecked(true);
                break;
            case 2:
                nv.setChecked(true);
                break;
        }
        vip_addrdss.setText(vipInfo.getAddress());
        vip_job.setText(vipInfo.getOccupation());
        vip_email.setText(vipInfo.getEmail());
        vip_birthday.setText(vipInfo.getBirthday());
    }

    private void jundgeData() {
        String email = vip_email.getText().toString();
        if (!"".equals(email)) {
            if (!checkEmaile(email)) {
                toast("邮箱格式不正确");
                return;
            }
        }

        if (!ifadd) {
            toast("请重新确认手机号码");
        } else if (phonenumber.getText().toString().equals("")) {
            toast("手机号码填写不能为空");
        } else if (!phonenumber.getText().toString().equals(mobile)) {
            toast("请确认手机号码后重试");
        } else if (name.getText().toString().equals("")) {
            toast("请填写您的真实姓名");
        } else if (timepre.equals("")) {
            toast("出生日期不能为空");
        } else if (ifadd) {
            if (!Utils.isNotFastClick()) {
                return;
            }
            addVipdata();
        }
    }

    private void addVipdata() {
        showProgressDialog();
        String urlString = "relation/add_vip.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("user_name", name.getText().toString());
        map.put("gen", sex + "");
        map.put("birthday", timepre);
        map.put("occupation", vip_job.getText().toString());
        map.put("mobile", phonenumber.getText().toString());
        map.put("email", vip_email.getText().toString());
        map.put("address", vip_addrdss.getText().toString());
        map.put("opuid", pref.getString("uid", ""));
        map.put("sid", pref.getString("sid", ""));
        map.put("code", str);
        params = sortMapByKey(map);
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
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("添加成功");
                                finish();
                            } else if (j.getJSONObject("e").getInt("code") == -109) {
                                toast("用户不存在");
                            } else if (j.getJSONObject("e").getInt("code") == -1712) {
                                toast("店铺会员等级设置不存在");
                            } else if (j.getJSONObject("e").getInt("code") == -1704) {
                                toast("用户积分不足，无法成为会员");
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

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewVipActivity.this);
        builder.setMessage("信息尚未填写或保存,您确定要取消吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 正则表达式校验邮箱
     *
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private boolean checkEmaile(String emaile) {
        String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        //正则表达式的模式
        Pattern p = Pattern.compile(REGEX_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }
}
