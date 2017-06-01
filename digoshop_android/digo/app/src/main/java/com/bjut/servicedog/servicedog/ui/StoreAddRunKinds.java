package com.bjut.servicedog.servicedog.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.RunAdapter;
import com.bjut.servicedog.servicedog.adapter.RunKindGridViewAdapter;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.RunKindList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.StoreManagerActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreAddRunKinds extends BaseActivity {

    private ListView mListView;
    private GridView gview;
    private RunKindGridViewAdapter runKindGridViewAdapter;
    private List<RunKind> runKinds = new ArrayList<>();
    private RunAdapter mRunAdapter;


    private List<RunKind> saveRunkindlist = new ArrayList<>();//用于向上一个activity传递数据.
    private List<RunKind> choiseRunkindlist = new ArrayList<>();//用于向上一个activity传递数据.


    //    private TextView getmore;
    private TextView saveView;
    private String moid = "";
    private Button btn_right;
    private TextView tv_title;
    private String saveSecondid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_run_kinds);
        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_tianjiajinyingpinlei));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(R.string.btn_baocun));
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        gview = (GridView) findViewById(R.id.gridview);
        mRunAdapter = new RunAdapter(this);
        mListView.setAdapter(mRunAdapter);
        runKindGridViewAdapter = new RunKindGridViewAdapter(this, runKinds);
        gview.setAdapter(runKindGridViewAdapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                long moid = runKinds.get(position).getMoid();
                String name = runKinds.get(position).getName();
                for (int i = 0; i < saveRunkindlist.size(); i++) {
                    if ((saveRunkindlist.get(i).getMoid() + "").equals(moid+"")) {
                        saveRunkindlist.remove(i);
                        runKinds.get(position).setChosenow(0);
                        runKindGridViewAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                RunKind runKind = new RunKind();
                runKind.setMoid(moid);
                runKind.setName(name);
                saveRunkindlist.add(runKind);
                runKinds.get(position).setChosenow(1);
                runKindGridViewAdapter.notifyDataSetChanged();
            }
        });

        saveRunkindlist = (List<RunKind>) getIntent().getSerializableExtra("saveRunkindlisttoadd");
        requestSecondlist();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moid = mRunAdapter.getItem(i).getMoid()+"";
                mRunAdapter.setChecked(i);
                runKinds.clear();
                requestRunKindsList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, StoreManagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.i("saveRunkindlistpreinadd", saveRunkindlist + "");
                intent.putExtra("saveRunkindlisttostore", (Serializable) saveRunkindlist);
                intent.putExtra("from", "runkind");
                setResult(Activity.RESULT_OK,intent);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                finish();
                break;
        }
    }

    private void requestSecondlist() {
        showProgressDialog();
        String urlString = "business/shop_cate.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
//        map.put("sid","1000045");
        map.put("level", "2");
        map.put("page_length", "30");
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
                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                            List<RunKind> rk = runKindList.getData();
                            for (int i = 0; i < rk.size(); i++) {
                                saveSecondid += rk.get(i).getMoid() + ",";
                            }
                            editor.putString("saveSecondid", saveSecondid);
                            editor.commit();
                            mRunAdapter.setList(rk);
                            mRunAdapter.setChecked(0);
                            moid = mRunAdapter.getItem(0).getMoid()+"";
                            requestRunKindsList();
                        } catch (Exception e) {
                            toast("没有更多信息");
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

    private void requestRunKindsList() {

        showProgressDialog();
        String urlString = "business/queryCategory.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", pref.getString("shoptype", ""));
        map.put("level", "3");
        map.put("moid", moid);
        map.put("child", "");
        map.put("page", "");
        map.put("page_length", "30");

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
                        Log.i("zzr二级", json);
                        try {
                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                            if (runKindList.getE().getCode() == 0) {
                                List<RunKind> data = runKindList.getData();
                                runKinds.addAll(data);
                                changeView(runKinds);
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            runKindGridViewAdapter.notifyDataSetChanged();
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

    private void changeView(List<RunKind> rk) {

        for (int j = 0; j < saveRunkindlist.size(); j++) {
            for (int i = 0; i < rk.size(); i++) {
                if ((saveRunkindlist.get(j).getMoid() + "").equals(rk.get(i).getMoid() + "")) {
                    rk.remove(i);
//                    runKindGridViewAdapter.removeItem(i);
                }
            }
        }
        runKindGridViewAdapter.notifyDataSetChanged();
//        for (RunKind runKind : choiseRunkindlist) {
//
//        }
    }

    public void selftDestruct(View view) {
        TextView v = (TextView) view;
        String n = view.getId() + "";
        Log.i("runkindId", n);
        for (int i = 0; i < saveRunkindlist.size(); i++) {
            if ((saveRunkindlist.get(i).getMoid() + "").equals(n)) {
                saveRunkindlist.remove(i);
                view.setBackgroundResource(R.drawable.weixuanzhongkuang);
                return;
            }
        }
        RunKind runKind = new RunKind();
        runKind.setMoid(view.getId());
        runKind.setName(((TextView) view).getText().toString());
        saveRunkindlist.add(runKind);
        view.setBackgroundResource(R.drawable.xuanzhongkuang);
    }

}
