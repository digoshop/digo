package com.bjut.servicedog.servicedog.ui.settlement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ExchangeListAdapter;
import com.bjut.servicedog.servicedog.model.ChoiseExchangeModel;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjut.servicedog.servicedog.R.id.listView;

/**
 * Created by beibeizhu on 16/11/10.
 */

public class ExchangeListActivity extends BaseActivity {


    private ListView mPullToRefreshListView;

    private ExchangeListAdapter mExchangeListAdapter;

    private TextView tv_title;
    private TextView tv_empty;

    private List<ChoiseExchangeModel.DataBean> mDataBeen = new ArrayList<>();

    private final int GET_OK = 0;
    private final int GET_NULL = 1;

    private String userId = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_OK:
                    mExchangeListAdapter.setList(mDataBeen);
                    break;
                case GET_NULL:
                    toast(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_list);
        initViews();
        setListener();
        getExchangeList();
    }

    private void setListener() {

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("item", mExchangeListAdapter.getItem(i));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void initViews() {

        userId = getIntent().getStringExtra("userId");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_title.setText(getString(R.string.title_xzjssp));

        mPullToRefreshListView = (ListView) findViewById(listView);

        mExchangeListAdapter = new ExchangeListAdapter(this);

        mPullToRefreshListView.setAdapter(mExchangeListAdapter);
        mPullToRefreshListView.setEmptyView(tv_empty);
    }

    private void getExchangeList() {
        showProgressDialog();
        String urlString = "user/user_gift.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);//用户ID
        map.put("sid", pref.getString("sid", ""));//商铺ID
        RequestParams params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                try {
                    String json = responseInfo.result;
                    Log.d(TAG, json);
                    Gson gson = new Gson();
                    ChoiseExchangeModel exchange = gson.fromJson(json, ChoiseExchangeModel.class);
                    if (exchange.getE().getCode() == 0) {
                        mDataBeen = exchange.getData();
                        mHandler.sendEmptyMessage(GET_OK);
                    } else if (exchange.getE().getCode() != 0 && exchange.getE().getCode() != -99) {
                        Message msg = new Message();
                        msg.what = GET_NULL;
                        msg.obj = exchange.getE().getDesc();
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }
}
