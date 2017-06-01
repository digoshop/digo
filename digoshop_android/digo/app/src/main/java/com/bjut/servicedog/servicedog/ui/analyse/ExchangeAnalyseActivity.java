package com.bjut.servicedog.servicedog.ui.analyse;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.GoodsExchangeStat;
import com.bjut.servicedog.servicedog.po.GoodsExchangeStat_data;
import com.bjut.servicedog.servicedog.rc_adapter.ExchangeAnalyseAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeAnalyseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ExchangeAnalyseAdapter mAdapter;
    private int page = 1;
    private String mt = "";
    private TextView tv_title;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_analyse_activity);
        mt = getIntent().getStringExtra("mt");
        init();
        requestlist(false);
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_spdhtj));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0,0,20,0));
        mAdapter = new ExchangeAnalyseAdapter();
        mAdapter.setEmptyView(noDataView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        loadType = 1;
        page = 1;
        requestlist(false);
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestlist(true);
    }

    private void requestlist(final boolean isLoadMore) {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "product/queryProductStat.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("productType", "2");
        map.put("mt", mt);
        map.put("page", page + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
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
                            GoodsExchangeStat goodList = JSON.parseObject(json, GoodsExchangeStat.class);
                            if (goodList.getE().getCode() == 0) {
                                List<GoodsExchangeStat_data> data = goodList.getData();
                                if (data.size() >= 0) {
                                    if (!isLoadMore) {
                                        mAdapter.setNewData(data);
                                    }else{
                                        mAdapter.addData(data);
                                    }
                                    if (data.size() < Constant.PAGE_SIZE) {
                                        mAdapter.loadMoreEnd(false);
                                    } else {
                                        mAdapter.loadMoreComplete();
                                    }
                                }else{
                                    mAdapter.loadMoreEnd(false);
                                }
                            } else {
                                toast(goodList.getE().getDesc());
                                mAdapter.loadMoreEnd(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAdapter.loadMoreEnd(false);
                        } finally {
                            finishLoad();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                finishLoad();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void finishLoad(){
        if (loadType == 0) {
            closeProgressDialog();
        } else if (loadType == 1) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
        }
    }
}
