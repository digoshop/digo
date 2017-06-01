package com.bjut.servicedog.servicedog.ui.customized;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Customization;
import com.bjut.servicedog.servicedog.po.Customization_data;
import com.bjut.servicedog.servicedog.rc_adapter.CustomizationAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
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
 * Created by apple02 on 16/7/6.
 */
public class Customizationactivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private CustomizationAdapter mCustomizationAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_title;
    private int page = 1;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_accept_activity_f);
        init();
        setListener();
    }

    private void setListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.currnet_reply:
                        Intent intent = new Intent(mContext, CustomizationReplyActivity_f.class);
                        intent.putExtra("cid", mCustomizationAdapter.getItem(position).getCsDto().getCid());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        page = 1;
        requestList();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tv_title.setText(getString(R.string.title_yhdz));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mCustomizationAdapter = new CustomizationAdapter();
        mCustomizationAdapter.setEmptyView(noDataView);
        mCustomizationAdapter.setEnableLoadMore(true);
        mCustomizationAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mCustomizationAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mCustomizationAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    private void requestList() {

        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "custservice/queryCustServiceListBySid.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
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
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            Customization custolist = JSON.parseObject(json, Customization.class);
                            if (custolist.getE().getCode() == 0) {
                                List<Customization_data> data = custolist.getData();
                                if (data.size() > 0) {
                                    if (page == 1) {
                                        mCustomizationAdapter.setNewData(data);
                                    } else {
                                        mCustomizationAdapter.addData(data);
                                    }
                                    if (data.size() < Constant.PAGE_SIZE) {
                                        mCustomizationAdapter.loadMoreEnd(false);
                                    } else {
                                        mCustomizationAdapter.loadMoreComplete();
                                    }
                                } else {
                                    if (page == 1) {
                                        mCustomizationAdapter.setNewData(new ArrayList<Customization_data>());
                                    } else {
                                        mCustomizationAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                if (page == 1) {
                                    mCustomizationAdapter.setNewData(new ArrayList<Customization_data>());
                                } else {
                                    mCustomizationAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (page == 1) {
                                mCustomizationAdapter.setNewData(new ArrayList<Customization_data>());
                            } else {
                                mCustomizationAdapter.loadMoreEnd(false);
                            }
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mCustomizationAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mCustomizationAdapter.loadMoreFail();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        page = 1;
        requestList();
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestList();
    }
}
