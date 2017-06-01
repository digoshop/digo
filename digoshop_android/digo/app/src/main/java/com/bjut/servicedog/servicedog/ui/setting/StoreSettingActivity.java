package com.bjut.servicedog.servicedog.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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

public class StoreSettingActivity extends BaseActivity {
    private String fileurl, status = "1", i;
    private ToggleButton toggleButton, toggleButton_c;
    private LinearLayout linearLayout;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);
        init();

    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        toggleButton_c = (ToggleButton) findViewById(R.id.togglebutton_cc);
        toggleButton_c.setOnClickListener(this);
        toggleButton = (ToggleButton) findViewById(R.id.togglebutton);
        toggleButton.setOnClickListener(this);

        tv_title.setText(getString(R.string.title_dpsz));
        String shop_status = pref.getString("shopStatus", "");
        String shop_scs = pref.getString("togglebutton", "");
        if (shop_status.equals("1")) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
        if (shop_scs.equals("0")) {
            toggleButton_c.setChecked(false);
        } else {
            toggleButton_c.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.togglebutton:
                if (!toggleButton.isChecked()) {
                    status = "2";
                    setTogglebutton(status);
                } else {
                    status = "1";
                    setTogglebutton(status);
                }
                break;
            case R.id.togglebutton_cc:
                if (!toggleButton_c.isChecked()) {
                    i = "0";
                    setTogglebutton_c(i);

                } else {
                    i = "1";
                    setTogglebutton_c(i);
                }
                break;
        }
    }

    /**
     * 这个接口用来改变
     *
     * @param status
     * @return
     */
    private boolean setTogglebutton(final String status) {
        String urlString = "business/s_status.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("status", status);
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
                                if (status == "1") {
                                    toast("掌柜的，您现在可以正式开张营业啦！");
                                    editor.putString("shopStatus", "1");
                                    editor.commit();
                                } else if (status == "2") {
                                    toast("伙计们太累啦，歇业休息几天！");
                                    editor.putString("shopStatus", "2");
                                    editor.commit();
                                }

                            } else {
                                toast("设置失败 ");
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
        return true;
    }

    private boolean setTogglebutton_c(final String i) {
        String urlString = "business/update_shop_custom.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("scs", i);
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
                                if (i == 1 + "") {
                                    toast("开启定制服务，迎接更多机遇！");
                                    editor.putString("togglebutton", "1");
                                    editor.commit();
                                } else if (i == 0 + "") {
                                    toast("关闭定制回复，研发企业精品！");
                                    editor.putString("togglebutton", "0");
                                    editor.commit();
                                }

                            } else {
                                toast("设置失败 ");
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
        return true;
    }

}
