package com.bjut.servicedog.servicedog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.q_SMS;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.TimerCount;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bjut.servicedog.servicedog.R.id.q_login_username;

public class QuickLoginActivity_f extends BaseActivity {
    private TextView q_get,quick_login,to_back_login;
    private EditText q_phonenumber,q_yanzheng;
    boolean temp1;
    private int registerstatus,userstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login_activity_f);
        to_back_login=(TextView)findViewById(R.id.back_login);
        to_back_login.setOnClickListener(this);
        q_get=(TextView)findViewById(R.id.q_get_yanzheng);
        q_phonenumber=(EditText)findViewById(q_login_username);
        q_yanzheng=(EditText)findViewById(R.id.q_login_yanzheng);
        quick_login=(TextView)findViewById(R.id.q_quicklogin);
        q_get.setOnClickListener(this);
        quick_login.setOnClickListener(this);
    } 

    public void  quickLogin(){
        if(q_phonenumber.getText().toString().equals("")){
            toast("请输入用户名");
        }else if(isMobileNumber(q_phonenumber.getText().toString())==false){
            toast("清输入正确电话号!");
        } else if(q_yanzheng.getText().toString().equals("")){
            toast("请输入验证码");
        } else if(q_yanzheng.getText().toString().length()!=6){
            toast("请输入输入正确验证码");
        } else {
            verifysmscode();
        }
    }
    public static boolean isMobileNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    @Override
    public void onClick(View v) {
       switch (v.getId())
       {

           case R.id.q_get_yanzheng:
              
               if(q_phonenumber.getText().toString().equals(""))
               {
                   toast("账号不能为空!");
               }else if(isMobileNumber(q_phonenumber.getText().toString())==false){
                   toast("清输入正确电话号!");
               } else {
                   checkaccountphonenumber();
               }
             
               //temp=q_phonenumber.getText().toString();
               break;
           case R.id.q_quicklogin:
         
               quickLogin();

               break;
           case R.id.back_login:
               finish();
               break;
       }
    }

    private void sendSms()
    {
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String,String> map=new HashMap<>();
        map.put("ip",pref.getString("ip", ""));
        map.put("mobile", q_phonenumber.getText().toString());
        map.put("check","0");
        params=sortMapByKey(map);
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
                            q_SMS smslist = JSON.parseObject(json, q_SMS.class);
                            if (smslist.getE().getCode() == 0) {
                                    temp1=smslist.getQ_sms_data().isQuicklogin();
                                if (temp1)
                                {
                                    toast("正在发送请求,请稍后...");
                                }
                                else
                                {
                                    toast("异常");
                                }
                            } else {
                                closeProgressDialog();
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

    private void checkaccountphonenumber() {
        String urlString = "passport/s_exist.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("token",pref.getString("token", ""));
        map.put("passport", q_phonenumber.getText().toString());
        params = sortMapByKey(map);
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == -112) {
                                TimerCount timer=new TimerCount(60000,1000,q_get);
                                timer.start();
                                sendSms();
                            } else {
                                toast("您输入的手机号未注册，请先注册！");
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
    private boolean verifysmscode()
    {
        String urlString = "passport/verifysmscode.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String,String> map=new HashMap<>();
        map.put("mobile",q_phonenumber.getText().toString());
        map.put("ip", pref.getString("ip", ""));
        map.put("smscode", q_yanzheng.getText().toString());
        params=sortMapByKey(map);
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
                            JSONObject jsonObject=new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0){
                                if (jsonObject.getString("data").equals("true")){
                                    editor.putString("isLogin","true");
                                    editor.commit();
                                    Intent intent=new Intent(QuickLoginActivity_f.this,ResetPassword.class);
                                    intent.putExtra("currentphonenumber", q_phonenumber.getText().toString());
                                    intent.putExtra("smsCode", q_yanzheng.getText().toString());
                                    intent.putExtra("ifshow",1);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if(jsonObject.getJSONObject("e").getInt("code")==-101){
                                toast("手机号或验证码错误!");
                                editor.putString("ifLogin","false");
                                editor.commit();
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
        return true;
    }

    }



