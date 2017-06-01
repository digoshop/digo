package com.bjut.servicedog.servicedog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CommitEvent;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.LoginActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
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


public class ResetPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvNewCode;
    private TextView mTvSubmit;
    private EditText mEdtNewPhone;
    private EditText mEdtNewCode;
    private TextView tv_title;

    private Intent mIntent;
    private String mOldCode = "";
    private String mOldPhone = "";
    private String mNewCode = "";
    private String mNewPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_phone);
        initViews();
        initEvents();
    }

    private void initEvents() {
        mTvNewCode.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    private void initViews() {
        mIntent = getIntent();
        mOldCode = mIntent.getStringExtra("code");
        mOldPhone = mIntent.getStringExtra("oldPhone");

        mTvNewCode = (TextView) findViewById(R.id.tv_new_code);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
        mEdtNewPhone = (EditText) findViewById(R.id.edt_new_phone);
        mEdtNewCode = (EditText) findViewById(R.id.edt_new_code);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText(getString(R.string.title_sjbd));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_code:
                mNewPhone = mEdtNewPhone.getText().toString();
                if ("".equals(mNewPhone)) {
                    toast("请填写新的手机号");
                    return;
                }
                if (!isMobileNO(mNewPhone)) {
                    toast("请检验手机号是否正确");
                    return;
                }
                checkPhone();
                break;

            case R.id.tv_submit:
                mNewPhone = mEdtNewPhone.getText().toString();
                mNewCode = mEdtNewCode.getText().toString();
                if ("".equals(mNewPhone)) {
                    toast("请填写新的手机号");
                    return;
                }
                if (!isMobileNO(mNewPhone)) {
                    toast("请检验手机号是否正确");
                    return;
                }
                if ("".equals(mNewCode)) {
                    toast("请填写验证码");
                    return;
                }
                resetPhone();
                break;
        }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", mNewPhone);
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
                            if (code == 0) {
                                if (data.equals("true")) {
                                    toast("正在发送请求,请稍后...");
                                    TimerCount timer = new TimerCount(60000, 1000, mTvNewCode);
                                    timer.start();
                                } else {
                                    toast("验证码发送失败");
                                }
                            } else if (code == -13) {
                                toast("同号码发送达到上限");
                            } else {
                                toast("验证码发送失败");
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

    private void checkPhone() {
        showProgressDialog();
        String urlString = "passport/s_exist.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("passport", mNewPhone);
        RequestParams params = sortMapByKey(map);
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
                                toast("您输入的帐号已注册，请重新输入");
                                closeProgressDialog();
                            } else if (j.getJSONObject("e").getInt("code") == 0) {
                                getCode();
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

    private void resetPhone() {
        //String urlString ="passport/resetpwd.json";
        String urlString = "passport/s_bind.json";
        urlString = String.format(urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("mobile", mOldPhone);
        map.put("nmobile", mNewPhone);
        map.put("smscode", mNewCode);
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
                                toast("绑定手机成功!");
                                CommitEvent commitEvent = new CommitEvent(true);
//                                EventBus.getDefault().post(commitEvent);
                                Intent intent = new Intent(ResetPhoneActivity.this, LoginActivity.class);
                                startActivity(intent);
//                                finish();
                                ActivityCollector.finishAll();
                            } else {
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

}
