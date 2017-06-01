package com.bjut.servicedog.servicedog.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReplyActivity extends BaseActivity implements View.OnClickListener {
    private int mcid;
    private EditText etReply;
    private Button btn_right;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        init();


    }

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_plhf));
        etReply = (EditText) findViewById(R.id.et_reply);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(R.string.btn_fs));
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        mcid = getIntent().getIntExtra("mcid", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                if (etReply.getText().toString().equals("")) {
                    toast("请输入回复的内容");
                    return;
                }
                sendReply();
                break;
        }

    }


    private void sendReply() {

        String urlString = "merchant_comment/reply.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();

        map.put("mcid", mcid + "");
        map.put("sid", pref.getString("sid", ""));
        map.put("text", etReply.getText().toString());
//        map.put("uid", pref.getString("uid", ""));
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
                                toast(" 回复成功!");
                                finish();
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("desc"));
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
