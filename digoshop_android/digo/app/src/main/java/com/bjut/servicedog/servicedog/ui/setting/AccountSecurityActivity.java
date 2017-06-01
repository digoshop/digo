package com.bjut.servicedog.servicedog.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.ResetPassword;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.GlideCacheUtil;

public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout reset_phone, resetpassword, clear_cache;
    private TextView cachesize;
    private TextView tv_current_phone;
    private TextView tv_title;
    private Toast mToast;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        init();
    }

    private void init() {
        cachesize = (TextView) findViewById(R.id.cachesize);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_phone = (TextView) findViewById(R.id.tv_current_phone);
        resetpassword = (RelativeLayout) findViewById(R.id.reset_password);
        reset_phone = (RelativeLayout) findViewById(R.id.reset_phone);
        clear_cache = (RelativeLayout) findViewById(R.id.clear_cache);
        clear_cache.setOnClickListener(this);
        resetpassword.setOnClickListener(this);
        reset_phone.setOnClickListener(this);

        tv_title.setText(getString(R.string.title_zhanghuanquan));

        String phoneNumber = pref.getString("usermobile", "");
        if (phoneNumber.length() == 11) {
            tv_current_phone.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11));
        } else {
            tv_current_phone.setText(phoneNumber);
        }

        String manager = pref.getString("manager", "1");  //1  是店铺申请者的账号    0是添加的
        if (manager.equals("1")) {
            reset_phone.setVisibility(View.VISIBLE);
        } else {
            reset_phone.setVisibility(View.GONE);
        }

        cachesize.setText(GlideCacheUtil.getInstance().getCacheSize(mContext));
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mToast = Toast.makeText(AccountSecurityActivity.this, "", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setText("清理缓存成功");
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();


            cachesize.setText(GlideCacheUtil.getInstance().getCacheSize(mContext));
            closeProgressDialog();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_password:
                mIntent = new Intent(this, ResetPassword.class);
                mIntent.putExtra("ifshow", 0);
                startActivity(mIntent);
                break;
            case R.id.reset_phone:
                mIntent = new Intent(this, VerificationPhoneActivity.class);
                startActivity(mIntent);
                break;
            case R.id.clear_cache:
//                showProgressDialogDelete();
                GlideCacheUtil.getInstance().clearImageAllCache(mContext);
                mToast = Toast.makeText(AccountSecurityActivity.this, "", Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.setText("清理缓存成功");
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.show();
                cachesize.setText(GlideCacheUtil.getInstance().getCacheSize(mContext));
                break;
        }
    }


}
