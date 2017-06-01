package com.bjut.servicedog.servicedog.ui.vip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.VipClassConfigAdapter;
import com.bjut.servicedog.servicedog.po.Vip_class_config;
import com.bjut.servicedog.servicedog.po.Vip_class_config_data;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;
import com.bjut.servicedog.servicedog.view.My2BDialog;
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

public class VipClassConfigActivity extends BaseActivity implements ListItemClickHelp {
    private VipClassConfigAdapter mAdapter;

    private ListView listView;
    private List<Vip_class_config_data> mData = new ArrayList<>();
    private TextView tv_title;
    private Button btn_right;
    private ListItemClickHelp callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_class_config);
        init();
        requestList();
//        listView.setMode(PullToRefreshBase.Mode.DISABLED);
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                mData.clear();
//                page = 1;
//                requestlist(page + "");
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                page++;
//                requestlist(page + "");
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//            }
//        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mData.clear();
        requestList();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.vip_class_config_listview);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_hydjpz));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(R.string.btn_tjdj));
        btn_right.setVisibility(View.VISIBLE);
        mAdapter = new VipClassConfigAdapter(this, mData, this);
        listView.setAdapter(mAdapter);
        btn_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_right:
                myIntentR(ModifyClassConfigActivity.class);
                break;
        }
    }

    @Override
    public void onclick(View item, View widget, final int position, int which) {
        switch (which) {
            case R.id.config_vip_delete:
                showAlertDialog(mData, position);
                break;
        }
    }

    private void requestList() {
        showProgressDialog();
        String urlString = "relation/query_intg_vips.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
//        map.put("page",page);
//        map.put("page_length","10");

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
                            Vip_class_config configList = JSON.parseObject(json, Vip_class_config.class);
                            if (configList.getE().getCode() == 0) {
                                if (configList.getData().size() > 0) {
                                    List<Vip_class_config_data> data = configList.getData();
                                    if (data.size() >= 0) {
                                        mData.addAll(data);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    toast("无更多信息");
                                }
                            } else {
                                toast(configList.getE().getDesc());
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

    private void Vipdelete(final int position) {
        String urlString = "relation/del_v_set.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        final Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));//获取店铺ID
        map.put("vip_level_id", mData.get(position).getVip_level_id());
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
                                if (jsonObject.getString("data").equals("true")) {
                                    mData.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    toast("会员等级删除成功!");
                                }

                            } else if (jsonObject.getJSONObject("e").getInt("code") == -1708) {
                                toast("抱歉，因关联字据受损，无法删除！");
                            } else if (jsonObject.getJSONObject("e").getInt("code") == -1201) {
                                toast("店铺已关闭！");
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

    public void showAlertDialog(final List<?> data, final int position) {

        if (mMyDialog == null) {
            mMyDialog = new My2BDialog(mContext);
        }
        mMyDialog.setTitle("温馨提示");
        mMyDialog.setMessage("确定要删除吗？");
        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                mMyDialog.dismiss();
                Vipdelete(position);
            }
        });
        mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                mMyDialog.dismiss();
            }
        });
        mMyDialog.show();
    }
}
