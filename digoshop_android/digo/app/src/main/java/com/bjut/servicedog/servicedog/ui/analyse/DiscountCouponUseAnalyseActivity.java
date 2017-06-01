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
import com.bjut.servicedog.servicedog.po.DiscountCouponAna;
import com.bjut.servicedog.servicedog.po.DiscountCouponAna_data;
import com.bjut.servicedog.servicedog.rc_adapter.DiscountCouponAnalyseAdapter;
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

public class DiscountCouponUseAnalyseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private DiscountCouponAnalyseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    private String mt = "";
    private TextView tv_title;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_coupon_use_analyse_activity);
        mt = getIntent().getStringExtra("mt");
        init();
        requestlist(false);
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_yhqsyfx));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0,0,20,0));
        mAdapter = new DiscountCouponAnalyseAdapter();
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

    private void requestlist(final boolean isLoadMore) {

        if (loadType==0) {
            showProgressDialog();
        }
        String urlString = "coupon/couponstat.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        final Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
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
                            DiscountCouponAna goodsList = JSON.parseObject(json, DiscountCouponAna.class);
                            if (goodsList.getE().getCode() == 0) {
                                List<DiscountCouponAna_data> data = goodsList.getData();
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
                                toast(goodsList.getE().getDesc());
                                mAdapter.loadMoreEnd(false);
                            }
                        } catch (Exception e) {
                            if (!isLoadMore) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }else{
                                mAdapter.loadMoreEnd(false);
                            }
                            e.printStackTrace();
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (loadType == 0) {
                    closeProgressDialog();
                } else if (loadType == 1) {
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mAdapter.loadMoreEnd(false);
                }
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadType =1;
        requestlist(false);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadType =2;
        requestlist(true);
    }
}
