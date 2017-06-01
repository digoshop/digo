package com.bjut.servicedog.servicedog.ui.promotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ProductInfo;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.rc_adapter.ChoiseProductAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
 * Created by beibeizhu on 17/2/21.
 */

public class ChoiseProductActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private TextView tv_title;
    private EditText et_productName;
    private Button btn_search;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ChoiseProductAdapter mChoiseProductAdapter;
    private List<Product> mProductLists = new ArrayList<>();
    private int index = 1;

    private int loadType = 0;
    private String productName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_product);
        initViews();
        setListener();
        requestProductList();
    }

    private void setListener() {
        btn_search.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                Product product = mChoiseProductAdapter.getItem(position);
                intent.putExtra("product", product);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_productName = (EditText) findViewById(R.id.et_productName);
        btn_search = (Button) findViewById(R.id.btn_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        tv_title.setText("选择商品");
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mChoiseProductAdapter = new ChoiseProductAdapter(mProductLists);
        mChoiseProductAdapter.setEmptyView(noDataView);
        mChoiseProductAdapter.setEnableLoadMore(true);
        mChoiseProductAdapter.setOnLoadMoreListener(this);
        mChoiseProductAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mChoiseProductAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                productName = et_productName.getText().toString();
                mProductLists.clear();
                index = 1;
                requestProductList();
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        mProductLists.clear();
        index = 1;
        requestProductList();
    }

    public void requestProductList() {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "product/productListByName.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("productName", productName);
        map.put("productType", Constant.PRODUCT + "");
        map.put("page", index + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    ProductInfo productBaseResult = JSON.parseObject(json, ProductInfo.class);
                    if (productBaseResult.getE().getCode() == 0) {
                        List<Product> data = productBaseResult.getData();
                        if (data.size() > 0) {
                            mProductLists.addAll(data);
                            if (data.size() < Constant.PAGE_SIZE) {
                                mChoiseProductAdapter.loadMoreEnd(false);
                            } else {
                                mChoiseProductAdapter.loadMoreComplete();
                            }
                        } else {
                            if (index==1) {

                            }else{
                                mChoiseProductAdapter.loadMoreEnd(false);
                            }
                        }
                    } else {
                        toast(productBaseResult.getE().getDesc());
                    }
                } catch (Exception e) {
                    mChoiseProductAdapter.loadMoreEnd(false);
                    e.printStackTrace();
                } finally {
                    if (loadType == 0) {
                        closeProgressDialog();
                    } else if (loadType == 1) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                    }
                    mChoiseProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mChoiseProductAdapter.loadMoreFail();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        index++;
        requestProductList();
    }
}
