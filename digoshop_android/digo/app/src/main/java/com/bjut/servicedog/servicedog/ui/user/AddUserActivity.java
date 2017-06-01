package com.bjut.servicedog.servicedog.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Permission;
import com.bjut.servicedog.servicedog.po.Permission_data;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.TimerCount;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.wheelview.UserWheel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserActivity extends BaseActivity implements UserWheel.OnUserChangeListener,View.OnClickListener {
    private TextView spinner;
    private ArrayAdapter<String> arr_adapter;
    private List<String> auth_name = new ArrayList<>();
    private List<Permission_data> data = new ArrayList<>();
    private int permission_id = 0;
    private EditText phonenumber, initpassword, ed_user_code, username;
    private Button save, cancel;
    private String ePassword;
    private Button btn_code;
    private UserWheel mUserWheel = null;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        spinner = (TextView) findViewById(R.id.spinner);

        init();
        initWheel();
    }

    private void init() {
        btn_code = (Button) findViewById(R.id.btn_code);
        username = (EditText) findViewById(R.id.ed_user_name);
        phonenumber = (EditText) findViewById(R.id.ed_user_phonenumber);
        initpassword = (EditText) findViewById(R.id.ed_init_password);
        ed_user_code = (EditText) findViewById(R.id.ed_user_code);
        save = (Button) findViewById(R.id.add_save);
        cancel = (Button) findViewById(R.id.add_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_tjyh));
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btn_code.setOnClickListener(this);
        spinner.setOnClickListener(this);
        getPermission();
    }

    private void initWheel() {
        mUserWheel = new UserWheel(this);
        mUserWheel.setOnUserChangeListener(this);
        mUserWheel.setUserList(data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_save:
                if (username.getText().toString().length() == 0) {
                    toast("用户名不能为空!");
                    return;
                } else if (phonenumber.getText().toString().length() == 0) {
                    toast("手机号不能为空");
                    return;
                } else if (ed_user_code.getText().toString().length() == 0) {
                    toast("验证码不能为空");
                    return;
                } else if (initpassword.getText().toString().length() < 6 || initpassword.getText().toString().length() > 18) {
                    toast("初始化密码不能少于6位以及大于18位");
                    return;
                }
                if (!Utils.isNotFastClick()) {
                    return;
                }
                addUser();
                break;
            case R.id.spinner:

                mUserWheel.show(v);
                break;
            case R.id.add_cancel:
                finish();
                break;
            case R.id.btn_code:
                if (phonenumber.getText().toString().length() == 0) {
                    toast("手机号不能为空");
                    return;
                }
                checkaccountnumber();
                break;
        }
    }

    private void checkaccountnumber() {
        String urlString = "passport/s_exist.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("token",pref.getString("token", ""));
        map.put("passport", phonenumber.getText().toString());
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
                                toast("您输入的帐号已注册，请重新输入帐号名或返回登录页面");
                            } else {
                                sendSms();

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

    private void sendSms() {
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", phonenumber.getText().toString());
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("已发送");
                                TimerCount timerCount = new TimerCount(60000, 1000, btn_code);
                                timerCount.start();
                            } else {
                                toast(j.getJSONObject("e").getString("desc"));
                            }
                        } catch (Exception e) {
                            //smscode
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

    private void addUser() {
        try {
            ePassword = MD5Encryption.md5crypt(initpassword.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String urlString = "passport/sadd_user.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("name", username.getText().toString());//姓名
        map.put("mobile", phonenumber.getText().toString());//手机号
        map.put("pwd", ePassword);//初始化密码,md5加密
        map.put("auth", permission_id + "");//传入角色ID
        map.put("smscode", ed_user_code.getText().toString());
        map.put("tid", pref.getString("sid", ""));
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
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                toast("提交资料成功!");
                                finish();
                            } else if (jsonObject.getJSONObject("e").getInt("code") == -97) {
                                toast("手机号格式错误");
                            } else if (jsonObject.getJSONObject("e").getInt("code") == -101) {
                                toast("验证码错误");
                            }else{
                                toast(jsonObject.getJSONObject("e").getString("desc"));
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

    private void getPermission() {
        String urlString = "passport/shop_auth.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));//用户ID
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            Permission permission = JSON.parseObject(json, Permission.class);
                            if (permission.getE().getCode() == 0) {
                                if (permission.getData().size() > 0) {

                                    data.addAll(permission.getData());

                                } else {
                                    toast("无更多信息");
                                }
                            } else {
                                toast(permission.getE().getDesc());
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

    @Override
    public void onUserChange(String userName, int auth) {
        spinner.setText(userName);
        permission_id = auth;
    }

//    private class PermissionSelectedListener implements AdapterView.OnItemSelectedListener {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            for (Permission_data w : data) {
//                if (data.get(i).getAuth_name().equals((String) adapterView.getSelectedItem())) {
//                    permission_id = data.get(i).getAuth();
//                    System.out.println("IIIIIIIId" + permission_id);
//                    return;
//                }
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//    }

}
