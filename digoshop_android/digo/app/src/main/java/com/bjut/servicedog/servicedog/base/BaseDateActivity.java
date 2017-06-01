package com.bjut.servicedog.servicedog.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by beibeizhu on 17/5/26.
 */

public abstract class BaseDateActivity<T> extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    protected TextView tv_title;
    protected Button btn_right;
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private BaseQuickAdapter baseQuickAdapter;

    private int page = 1;
    protected int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_data);
        initViews();
        initTitle(tv_title, btn_right);
        setListener();
        getData(page);
    }

    protected void getData(int page){
        RequestParams params =getParams();

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, getUrls(), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                BaseDateActivity.this.onSuccess(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
               BaseDateActivity.this.onFailure();
            }
        });
    }

    protected abstract void onFailure();

    protected abstract void onSuccess(String result);

    protected abstract String getUrls();

    protected abstract RequestParams getParams();

    protected abstract BaseQuickAdapter initAdapter();

    protected void initViews() {
        tv_title = $(R.id.tv_title);
        btn_right = (Button) findViewById(R.id.btn_right);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        baseQuickAdapter = initAdapter();
        baseQuickAdapter.setEmptyView(noDataView);
        baseQuickAdapter.setEnableLoadMore(true);
        baseQuickAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        baseQuickAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(baseQuickAdapter);
    }

    private void setListener() {
        btn_right.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClick(adapter, view, position);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                onItemChildClick(adapter, view, position);
            }
        });
    }

    protected abstract void onItemChildClick(BaseQuickAdapter adapter, View view, int position);

    protected abstract void onItemClick(BaseQuickAdapter adapter, View view, int position);

    protected abstract void initTitle(TextView tv_title, Button btn_right);

    @Override
    public void onRefresh() {
        page = 1;
        loadType = 1;
        getData(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadType = 2;
        getData(page);
    }
}
