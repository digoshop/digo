package com.bjut.servicedog.servicedog.ui.vip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModifyClassConfigActivity extends BaseActivity {
    private EditText jifen;
    private Button save, cancel;
    private EditText classname;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_class_config);
        classname = (EditText) findViewById(R.id.ed_classname);
        jifen = (EditText) findViewById(R.id.ed_configjifen);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xgdjpz));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (checkEmpty()) {
                    if (!Utils.isNotFastClick()) {
                        return;
                    }
                    senSubmit();
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private boolean checkEmpty() {
        if (classname.getText().toString().isEmpty()) {
            toast("请输入等级名称");
            return false;
        }

        if (jifen.getText().toString().isEmpty()) {
            toast("请输入所需积分");
            return false;
        }

        return true;
    }

    private void senSubmit() {

        String urlString = "relation/add_intg_vip.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));//传入店铺ID
        map.put("intg", jifen.getText().toString());//填写所需积分
        map.put("vip_name", classname.getText().toString());//填写会员名称


        params = sortMapByKey(map);

        params.addBodyParameter("uid", pref.getString("uid", ""));
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
                                toast("新增会员等级成功!");
                                finish();
                            } else {
                                toast(jsonObject.getJSONObject("e").get("desc").toString());
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
