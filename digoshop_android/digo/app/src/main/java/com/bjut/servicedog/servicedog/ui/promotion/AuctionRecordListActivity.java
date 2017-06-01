package com.bjut.servicedog.servicedog.ui.promotion;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CheckAuctionRecord;
import com.bjut.servicedog.servicedog.po.CheckAuctionRecord_data;
import com.bjut.servicedog.servicedog.rc_adapter.AuctionRecoedAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

/**
 * Created by beibeizhu on 17/3/2.
 */

public class AuctionRecordListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private TextView tv_title;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AuctionRecoedAdapter mAuctionRecoedAdapter;

    private List<CheckAuctionRecord_data> mData = new ArrayList<>();
    private String productId = "";
    private int index = 1;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_record_list);
        initViews();
        getAuctionRecord();
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAuctionRecoedAdapter = new AuctionRecoedAdapter(mData);
        mAuctionRecoedAdapter.setEmptyView(noDataView);
        mAuctionRecoedAdapter.setEnableLoadMore(true);
        mAuctionRecoedAdapter.setOnLoadMoreListener(this);
        mAuctionRecoedAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAuctionRecoedAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAuctionRecoedAdapter);

        tv_title.setText("兑换记录");
        productId = getIntent().getStringExtra("productId");
        Log.i(TAG, "productId====" + productId);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        loadType = 1;
        mData.clear();
        index = 1;
        getAuctionRecord();
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        index++;
        getAuctionRecord();
    }

    public void getAuctionRecord() {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "product/getAuctionLogListByPId.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", productId);
        map.put("page", index + "");
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
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            CheckAuctionRecord record = JSON.parseObject(json, CheckAuctionRecord.class);
                            if (record.getE().getCode() == 0) {
                                List<CheckAuctionRecord_data> data = record.getData();
                                if (data.size() > 0) {
                                    mData.addAll(data);
                                    if (data.size() < Constant.PAGE_SIZE) {
                                        mAuctionRecoedAdapter.loadMoreEnd(false);
                                    } else {
                                        mAuctionRecoedAdapter.loadMoreComplete();
                                    }
                                } else {
                                }
                            } else {
                                toast(record.getE().getDesc());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAuctionRecoedAdapter.loadMoreEnd(false);
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mAuctionRecoedAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mAuctionRecoedAdapter.loadMoreFail();
            }
        });
    }
}
