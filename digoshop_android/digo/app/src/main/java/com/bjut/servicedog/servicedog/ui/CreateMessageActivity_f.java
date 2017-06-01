package com.bjut.servicedog.servicedog.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.VipLevelGridViewAdapter;
import com.bjut.servicedog.servicedog.po.VipLevel;
import com.bjut.servicedog.servicedog.po.VipLevelList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.MyGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMessageActivity_f extends BaseActivity implements View.OnClickListener{

    private MyGridView gview;
    private VipLevelGridViewAdapter vipLevelGridViewAdapter;
    private List<VipLevel> vipLevels = new ArrayList<>();
    private List<VipLevel> vipLevelsOk = new ArrayList<>();
    private List<Long> saveVipLevel = new ArrayList<>();
    private TextView send;
    private TextView tv_title;
    private EditText title, detail;
    private int t_type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message_activity_f);
        init();
        requestVipLevelList();

    }

    private void init() {
        send = (TextView) findViewById(R.id.send);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_cjxx));
        send.setOnClickListener(this);
        gview = (MyGridView) findViewById(R.id.gridview);
        vipLevelGridViewAdapter = new VipLevelGridViewAdapter(this, vipLevels);
        gview.setAdapter(vipLevelGridViewAdapter);
        title = (EditText) findViewById(R.id.Message_title);
        detail = (EditText) findViewById(R.id.Message_detail);
        VipLevel vipall = new VipLevel();
        vipall.setVip_level_name("全部会员");
        vipall.setVip_level_id(0);
        VipLevel vipnew = new VipLevel();
        vipnew.setVip_level_name("新会员");
        vipnew.setVip_level_id(1);
        vipLevels.add(vipall);
        vipLevels.add(vipnew);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                if (title.getText().toString().length() == 0) {
                    toast("消息标题填写不能为空!");
                    return;
                }
                if (detail.getText().toString().length() == 0) {
                    toast("消息内容填写不能为空");
                    return;
                }
                if (vipLevelGridViewAdapter.getCheck().size() == 0) {
                    toast("请您选择接收消息的会员类别");
                    return;
                }
                if (!Utils.isNotFastClick()) {
                    return;
                }
                sendMessage();
                break;
        }
    }

//    public void selftDestruct(View view) {
//        String n = view.getId() + "";
//        Log.i("zzzzzzz", n);
//        for (int i = 0; i < saveVipLevel.size(); i++) {
//            if (saveVipLevel.get(i).equals(n)) {
//                saveVipLevel.remove(i);
//                view.setBackgroundResource(R.drawable.weixuanzhongkuang);
//                return;
//            }
//        }
//        saveVipLevel.add(n);
//        view.setBackgroundResource(R.drawable.xuanzhongkuang);
//    }

    private String getSaveVipLevel() {
        String s = "";
        for (int i = 0; i < saveVipLevel.size(); i++) {
            s += saveVipLevel.get(i) + ",";
        }
        String a = s;
        return a.substring(0, s.length() - 1);
    }

    private void requestVipLevelList() {
        showProgressDialog();
        String urlString = "relation/query_intg_vips.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("page", "1");
        map.put("page_length", "50");

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
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            VipLevelList vipLevelList = JSON.parseObject(json, VipLevelList.class);
                            if (vipLevelList.getE().getCode() == 0) {
                                vipLevelsOk = vipLevelList.getData();
                                vipLevels.addAll(vipLevelsOk);
                                vipLevelGridViewAdapter.setmData(vipLevels);

//                                vipLevelGridViewAdapter.notifyDataSetChanged();
                            } else {
                                toast(vipLevelList.getE().getDesc());
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

    private void sendMessage() {
        int tType=0;
        saveVipLevel = vipLevelGridViewAdapter.getCheck();
        Log.i("zzzz", saveVipLevel.size() + "");
        for (int i = 0; i < saveVipLevel.size(); i++) {
            Long vipId = saveVipLevel.get(i);
            if (vipId == 0) {
                tType=0;
            } else if (vipId == 1) {
                tType=1;
            }else {
                tType=2;
            }
        }

        String urlString = "notice/create.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Log.i("sid",pref.getString("sid", "")+"--"+title.getText().toString()+"--"+detail.getText().toString()+"--"+getSaveVipLevel());
        Map<String,String> map=new HashMap<>();
        map.put("origin",pref.getString("sid", ""));//传店铺id
        map.put("title", title.getText().toString());//消息标题
        map.put("content",detail.getText().toString());//
        map.put("type","98");//
        map.put("t_type",tType+"");//0全部 1新会员 2会员类别
        if(tType==2){
            map.put("targets",getSaveVipLevel());//
        }else{
            map.put("targets","");//
        }

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
                                toast(" 发布消息成功!");
                                finish();
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("des"));
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