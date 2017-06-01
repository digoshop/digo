package com.bjut.servicedog.servicedog.ui.store;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.QuickLoginActivity_f;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView login, quicklogin, register;
    private String ip;
    private int Phone_width, Phone_height, registerstatus, userstatus;
    private EditText login_usernameEt, login_passwordEt;
    private String shopManage, shopStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

    //界面初始化
    public void init() {
        login_usernameEt = (EditText) findViewById(R.id.login_username);
        login_usernameEt.setText(pref.getString("login_name", ""));
        login_passwordEt = (EditText) findViewById(R.id.login_password);
        login = (TextView) findViewById(R.id.login);
        quicklogin = (TextView) findViewById(R.id.quick_login);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        quicklogin.setOnClickListener(this);
        Log.i("time", timeStamp());
    }

    public void judgeLogin() {
        if (login_usernameEt.getText().toString().equals("")) {
            toast("用户名不能为空");
        } else if (login_passwordEt.getText().toString().equals("")) {
            toast("密码不能为空");
        } else {
            try {
                String password = MD5Encryption.md5crypt(login_passwordEt.getText().toString());
                requestLogin(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestLogin(final String password) {
        showProgressDialog();
        String urlString = "passport/shop_login.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<String, String>();
        map.put("login_name", login_usernameEt.getText().toString());
        map.put("password", password);
        map.put("ip", pref.getString("ip", ""));
//        map.put("ip","172.168.0.1");
        RequestParams params = sortMapByKey(map);

//        String urlString = "pc/getNurse?phone=%s&personPwd=%s&personId=%s";
//        urlString = String.format(urlString, phone, personPwd, personId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                Log.i("result", responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        Log.i("result", json);
                        try {
                            final JSONObject j = new JSONObject(json);

                            if (j.getJSONObject("e").getInt("code") == 0) {

                                String data = j.getString("data");
                                JSONObject jsonData = new JSONObject(data);
//                             
//                                
                                boolean isUser = jsonData.has("user_info");
                                Log.i("bbbbb", isUser + "");
                                if (isUser == false) {
                                    userstatus = j.getJSONObject("data").getJSONObject("user_info").getInt("status");//判断商户是否禁用
                                    editor.putString("registeruid", j.getJSONObject("data").getJSONObject("user_info").getJSONObject("cookie").getString("uid"));
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, ChooseKindActivity.class);
                                    intent.putExtra("audit", 2);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    boolean isShop = jsonData.has("shop_info");
                                    Log.d("login", isShop + "");
                                    if (isShop) {
                                        JSONObject shop_info = jsonData.getJSONObject("shop_info");
                                        shopStatus = j.getJSONObject("data").getJSONObject("shop_info").getString("shop_status");
                                        shopManage = j.getJSONObject("data").getJSONObject("shop_info").getString("shop_manage");
                                        editor.putString("token", j.getJSONObject("data").getJSONObject("user_info").getJSONObject("cookie").getString("token"));
                                        editor.putString("uid", j.getJSONObject("data").getJSONObject("user_info").getJSONObject("cookie").getString("uid"));
                                        editor.putString("usermobile", j.getJSONObject("data").getJSONObject("user_info").getString("mobile"));
                                        String manager = j.getJSONObject("data").getJSONObject("user_info").getString("manager");
                                        editor.putString("manager", manager);
                                        editor.putBoolean("shop_merchant", j.getJSONObject("data").getJSONObject("shop_info").getBoolean("shop_merchant"));
                                        editor.putString("name", j.getJSONObject("data").getJSONObject("user_info").getString("name"));
                                        editor.putString("ifLogin", "true");
                                        editor.putString("password", password);
                                        editor.putString("login_name", login_usernameEt.getText().toString());
                                        editor.putString("auth", j.getJSONObject("data").getJSONObject("auth").toString());
                                        Log.i("auth", j.getJSONObject("data").getJSONObject("auth").toString());
                                        try {
                                            registerstatus = j.getJSONObject("data").getJSONObject("shop_info").getInt("shop_audit_status");
                                            editor.putString("sid", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_id"));
                                            Log.i("zzzzz", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_phone"));
                                            editor.putString("shopphonenumber", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_phone"));
                                            editor.putString("shopname", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_name"));
                                            editor.putString("shoptype", j.getJSONObject("data").getJSONObject("shop_info").getInt("shop_type") + "");
                                            editor.putString("shop_status", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_audit_status"));
                                            boolean isHasImage = j.getJSONObject("data").getJSONObject("shop_info").has("shop_avatar");
                                            if (isHasImage) {
                                                editor.putString("shopimage", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_avatar"));
                                            } else {
                                                editor.putString("shopimage", "");
                                            }
                                            editor.putString("togglebutton", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_scs"));
                                            editor.putString("shopChain", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_chain"));

                                        } catch (Exception e) {

                                        }
                                        editor.commit();

                                        if (registerstatus == 1 || registerstatus == 2) {
                                            myIntentR(AuditActivity.class);
                                            finish();
                                        } else if (registerstatus == 3 || registerstatus == 4) {
                                            editor.putString("registeruid", j.getJSONObject("data").getJSONObject("user_info").getJSONObject("cookie").getString("uid"));
                                            boolean isReason = shop_info.has("shop_reason");
                                            if (isReason) {
                                                editor.putString("shop_reason", j.getJSONObject("data").getJSONObject("shop_info").getString("shop_reason"));
                                            } else {
                                                editor.putString("shop_reason", "");
                                            }

                                            editor.commit();

                                            Intent intent = new Intent(LoginActivity.this, ChooseKindActivity.class);
                                            intent.putExtra("audit", 1);
                                            intent.putExtra("sid", shop_info.getString("shop_id") + "");
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // myIntentR(MainActivity.class);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("shop_manage", shopManage);
                                            intent.putExtra("shopStatus", shopStatus);
                                            intent.putExtra("headflag", 0);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Log.i("登录前", pref.getString("sid", ""));
                                        editor.putString("sid", "");
                                        editor.putString("shopphonenumber", "");
                                        editor.putString("shopname", "");
                                        editor.putString("shoptype", "");
                                        editor.putString("shop_status", "");
                                        editor.putString("shopimage", "");
                                        editor.putString("togglebutton", "");
                                        editor.putString("registeruid", j.getJSONObject("data").getJSONObject("user_info").getJSONObject("cookie").getString("uid"));
                                        editor.commit();
                                        Log.i("登录后", "aaa" + pref.getString("sid", ""));
                                        Intent intent = new Intent(LoginActivity.this, ChooseKindActivity.class);
                                        intent.putExtra("audit", 2);
                                        intent.putExtra("sid", "");
                                        startActivity(intent);
                                        finish();

                                    }

                                }
                            } else if (j.getJSONObject("e").getInt("code") == -102) {
                                toast("您的帐户已被禁用，请与掌柜的联系！");
                                editor.putString("ifLogin", "false");
                                editor.commit();
                            } else if (j.getJSONObject("e").getInt("code") == -110) {
                                toast("当前用户名或密码错误，请重新输入！");
                                editor.putString("ifLogin", "false");
                                editor.commit();
                            } else if (j.getJSONObject("e").getInt("code") == -113) {
                                toast("当前用户名或密码错误，请重新输入！");
                                editor.putString("ifLogin", "false");
                                editor.commit();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent intent = getIntent();
                String stringValue = intent.getStringExtra("phoneInfo");
                judgeLogin();
                break;
            case R.id.quick_login:
                myIntentR(QuickLoginActivity_f.class);
                break;
            case R.id.register:
                myIntentR(MerchantCorporationActivity.class);
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "上传图片需要打开相机和相册的权限", REQUEST_PERMISSIONS, perms);
        }
    }

}
