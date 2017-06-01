package com.bjut.servicedog.servicedog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.LoginActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.TimerCount;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends BaseActivity {
    private EditText inputyanzheng, resetpassword, confirmpassword;
    private TextView phonenumber, getyanzheng, confirm;
    private boolean temp1;
    private String isyanzheng;//是否显示验证
    private LinearLayout relyanzheng;
    private int flag = 0;
    private String md5password;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();

    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_mmcz));
        inputyanzheng = (EditText) findViewById(R.id.ed_yanzheng);
        resetpassword = (EditText) findViewById(R.id.ed_resetpassword);
        confirmpassword = (EditText) findViewById(R.id.ed_confirmpassword);
        phonenumber = (TextView) findViewById(R.id.current_phonenumber);
        getyanzheng = (TextView) findViewById(R.id.r_get_yanzheng);
        confirm = (TextView) findViewById(R.id.r_confirm);
        relyanzheng = (LinearLayout) findViewById(R.id.relthree);
        getyanzheng.setOnClickListener(this);
        confirm.setOnClickListener(this);
//        isyanzheng=getIntent().getStringExtra("noyanzheng");
//        if(isyanzheng.equals("no")){
//            relyanzheng.setVisibility(View.GONE);
//        }
        flag = getIntent().getIntExtra("ifshow", 0);
        if (flag == 1) {
            relyanzheng.setVisibility(View.GONE);

            phonenumber.setText(getIntent().getStringExtra("currentphonenumber").substring(0, 3) + "****" + getIntent().getStringExtra("currentphonenumber").substring(7, 11));
        } else {
            //relyanzheng.setVisibility(View.VISIBLE);
            phonenumber.setText(pref.getString("usermobile", "").substring(0, 3) + "****" + pref.getString("usermobile", "").substring(7, 11));//从登录里拿到mobile
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.r_get_yanzheng:
                TimerCount timer = new TimerCount(60000, 1000, getyanzheng);
                timer.start();
                getyanzheng();
                break;
            case R.id.r_confirm:
                if (flag == 0 && "".equals(inputyanzheng.getText().toString())) {
                    toast("请填写验证码");
                    return;
                }
                if (resetpassword.getText().toString().equals("")) {
                    toast("请输入密码!");
                    return;
                } else if (confirmpassword.getText().toString().equals("")) {
                    toast("请输入密码!");
                    return;
                } else if (resetpassword.getText().toString().length() < 6 || resetpassword.getText().toString().length() > 16) {
                    toast("请输入6-16位密码");
                    return;
                } else if (confirmpassword.getText().toString().length() < 6 || confirmpassword.getText().toString().length() > 16) {
                    toast("请输入6-16位密码");
                    return;
                } else if (resetpassword.getText().toString().equals(confirmpassword.getText().toString()) == false) {
                    toast("密码前后不一致,请重新操作");
                    resetpassword.setText("");
                    confirmpassword.setText("");
                    return;
                }

                try {
                    md5password = MD5Encryption.md5crypt(confirmpassword.getText().toString());
                    resetpassword(md5password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void resetpassword(String md5password) {
        //String urlString ="passport/resetpwd.json";
        String urlString = "passport/r_shop.json";
        urlString = String.format(urlString);
        Log.i("zzr", pref.getString("ip", "") + "---" + getIntent().getStringExtra("currentphonenumber") + "--" + confirmpassword.getText().toString() + "--" + pref.getString("token", ""));
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("new_pwd", md5password);
        if (flag == 1) {
            map.put("smscode", getIntent().getStringExtra("smsCode"));
        } else {
            map.put("smscode", inputyanzheng.getText().toString());
        }
        if (flag == 1) {
            map.put("mobile", getIntent().getStringExtra("currentphonenumber"));
        } else {
            map.put("mobile", pref.getString("usermobile", ""));
        }
        RequestParams params = sortMapByKey(map);
        params.addBodyParameter("uid", pref.getString("uid", ""));
        params.addBodyParameter("token", pref.getString("token", ""));

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
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {

                                toast("重置密码成功!!");
                                Intent intent = new Intent();
                                intent.setClass(ResetPassword.this, LoginActivity.class);
                                startActivity(intent);
                                ActivityCollector.finishAll();
                            } else {
                                toast("重置密码失败!");
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

    private void getyanzheng() {
        showProgressDialog();
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", pref.getString("usermobile", ""));
        map.put("check", "0");
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
                        String json = responseInfo.result;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String data = jsonObject.getString("data");
                            JSONObject eJson = jsonObject.getJSONObject("e");
                            int code = eJson.getInt("code");
//                            q_SMS smslist = JSON.parseObject(json, q_SMS.class);
                            if (code == 0) {
                                closeProgressDialog();
//                                temp1 = smslist.getQ_sms_data().isQuicklogin();
                                if (data.equals("true")) {
                                    toast("正在发送请求,请稍后...");
                                } else {
                                    toast("验证码发送失败");
                                }
                            } else if (code == -13) {
                                toast("同号码发送达到上限");
                                closeProgressDialog();
                            } else {
                                toast("验证码发送失败");
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

    private void verifysmscode() {
        String urlString = "passport/verifysmscode.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phonenumber.getText().toString());
        map.put("ip", pref.getString("ip", ""));
        map.put("smscode", inputyanzheng.getText().toString());
        params = sortMapByKey(map);

//        params.addBodyParameter("mobile", q_phonenumber.getText().toString());
//        params.addBodyParameter("smscode", q_yanzheng.getText().toString());
//        params.addBodyParameter("ip","10.1.1.1");

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
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                if (jsonObject.getString("data").equals("true")) {
//                                    editor.putString("isLogin","true");
//                                    editor.commit();
//                                    myIntentRtoTop(MainActivity.class);
                                    toast("验证成功!");
                                } else {
                                    toast("验证失败请重试");
                                }
                            } else {
                                toast("获取验证码失败");
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

}
