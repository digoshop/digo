package com.bjut.servicedog.servicedog.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CommitEvent;
import com.bjut.servicedog.servicedog.ui.ResetPhoneActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.TimerCount;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VerificationPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvCurrentPhone;
    private TextView mTvOldCode;
    private TextView mTvSubmit;
    private EditText mEdtOldCode;
    private TextView tv_title;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commitOk(CommitEvent commitEvent) {
        if (commitEvent.isCommitOK()) {
            finish();
        }
    }

    private void initEvents() {
        mTvOldCode.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    private void initViews() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        mTvCurrentPhone = (TextView) findViewById(R.id.tv_current_phone);
        mTvOldCode = (TextView) findViewById(R.id.tv_old_code);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
        mEdtOldCode = (EditText) findViewById(R.id.edt_old_code);

        tv_title.setText(getString(R.string.title_sjyz));

        phoneNumber = pref.getString("usermobile", "");
        if (phoneNumber.length() == 11) {
            mTvCurrentPhone.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11));
        } else {
            mTvCurrentPhone.setText(phoneNumber);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_old_code:
                getCode();
                break;
            case R.id.tv_submit:
                String code = mEdtOldCode.getText().toString();
                if ("".equals(code)) {
                    toast("请填写验证码");
                    return;
                }
                checkCode(code);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        showProgressDialog();
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", phoneNumber);
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
                                    TimerCount timer = new TimerCount(60000, 1000, mTvOldCode);
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


    /**
     * 判断验证码是否正确
     */
    private void checkCode(final String smsCode) {
        showProgressDialog();
        String urlString = "passport/verifysmscode.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("smscode", smsCode);
        map.put("mobile", phoneNumber);
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
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String data = jsonObject.getString("data");
                            JSONObject eJson = jsonObject.getJSONObject("e");
                            int code = eJson.getInt("code");
                            if (code == 0) {
                                Intent intent = new Intent(VerificationPhoneActivity.this, ResetPhoneActivity.class);
                                intent.putExtra("oldPhone", phoneNumber);
                                startActivity(intent);
                            } else {
                                toast("验证码错误");
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
