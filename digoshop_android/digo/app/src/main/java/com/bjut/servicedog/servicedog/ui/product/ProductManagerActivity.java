package com.bjut.servicedog.servicedog.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ProductInfo;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.rc_adapter.ProductAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.My2BDialog;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/2/15.
 */

public class ProductManagerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private TextView tv_title;
    //    private Button btn_add;
    private Button btn_right;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProductAdapter mProductAdapter;
    private int index = 1;
    private int choise = 0;

    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initViews();
        setListener();
        requestProductList(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.ADD_PRODUCT:
                    index = 1;
                    loadType = 0;
                    requestProductList(false);
                    break;
                case Constant.UPDATE_PRODUCT:
                    index = 1;
                    loadType = 0;
                    requestProductList(false);
                    break;
            }
        }
    }

    private void setListener() {
        btn_right.setOnClickListener(this);
//        btn_add.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("productId",mProductAdapter.getItem(position).getPid() + "");
                startActivity(intent);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_update:
                        choise = position;
                        Intent intent = new Intent(mContext, UpdateProductActivity.class);
                        if (mProductAdapter.getItem(position).getPst() == Constant.PRODUCT_STATUS_NOT_PASSED) {
                            intent.putExtra("pafr", mProductAdapter.getItem(position).getPafr());
                        }
                        intent.putExtra("productId", mProductAdapter.getItem(position).getPid() + "");
                        startActivityForResult(intent, Constant.UPDATE_PRODUCT);
                        break;
                    case R.id.img_delete:
                        deleteProduct(position, mProductAdapter.getItem(position).getPid());
                        break;
                    case R.id.btn_is_use:
                        Product item = mProductAdapter.getItem(position);
                        String status = ((TextView) view).getText().toString();
                        if (mContext.getString(R.string.btn_shangjia).equals(status)) {
                            operationProduct(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELVES);
                        } else  if (mContext.getString(R.string.btn_xiajia).equals(status)){
                            operationProduct(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELF);
                        }
                    case R.id.btn_is_tui:
                        Product product = mProductAdapter.getItem(position);
                        String str = ((TextView) view).getText().toString();
                        if (mContext.getString(R.string.btn_tuijian).equals(str)) {
                            tuiProduct(position, product.getPid(), Constant.TUI_YES);
                        } else if (mContext.getString(R.string.btn_cancle).equals(str)){
                            tuiProduct(position, product.getPid(), Constant.TUI_NO);
                        }
                        break;
                }
            }
        });
    }

    private void deleteProduct(final int position, final long pid) {
        final My2BDialog mMyDialog = new My2BDialog(mContext);
        mMyDialog.setTitle("温馨提示");
        mMyDialog.setMessage("确定要删除吗？");
        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                mMyDialog.dismiss();
                operationProduct(position, pid, Constant.PRODUCT_OPERATION_DELETE);
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

    private void operationProduct(final int position, final long id, final int ps) {
        String urlString = "product/del_product.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", id + "");//获取商品ID
        map.put("sid", pref.getString("sid", ""));
        map.put("pt", Constant.PRODUCT + "");//1代表竞拍,2代表兑换,3普通商品
        map.put("ps", ps + "");//状态(2禁用 99删除)
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
                                if (Constant.PRODUCT_OPERATION_SHELVES == ps) {
                                    Product item = mProductAdapter.getItem(position);
                                    //当前商品状态改成"上架中"
                                    item.setPst(Constant.PRODUCT_STATUS_SHELVES);
                                    //当前商品推荐状态改成"未推荐"
                                    item.setPrcmd(Constant.TUI_NO);
//                                    mProductAdapter.notifyItemChanged(position, item);
                                } else if (Constant.PRODUCT_OPERATION_SHELF == ps) {
                                    Product item = mProductAdapter.getItem(position);
                                    //当前商品状态改成"已下架"
                                    item.setPst(Constant.PRODUCT_STATUS_SHELF);
//                                    mProductAdapter.notifyItemChanged(position, item);
                                } else if (Constant.PRODUCT_OPERATION_DELETE == ps) {
                                    mProductAdapter.remove(position);
                                }
                            } else {
                                toast("操作失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            mProductAdapter.notifyDataSetChanged();
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

    private void tuiProduct(final int position, final long id, final int prcmd) {
        String urlString = "product/rcmd_product.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("pid", id + "");//获取商品ID
        map.put("sid", pref.getString("sid", ""));
        map.put("pt", Constant.PRODUCT + "");//2代表店铺
        map.put("prcmd", prcmd + "");//状态(1不推荐 2推荐)
        RequestParams params = sortMapByKey(map);
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
                                if (Constant.TUI_NO == prcmd) {
                                    Product item = mProductAdapter.getItem(position);
                                    item.setPrcmd(Constant.TUI_NO);
                                } else if (Constant.TUI_YES == prcmd) {
                                    Product item = mProductAdapter.getItem(position);
                                    item.setPrcmd(Constant.TUI_YES);
                                    mProductAdapter.notifyItemChanged(position, item);
                                }
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("desc"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mProductAdapter.notifyDataSetChanged();
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


    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_right = (Button) findViewById(R.id.btn_right);
//        btn_add = (Button) findViewById(R.id.btn_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        tv_title.setText("商品管理");
        btn_right.setText("新增商品");
        btn_right.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mProductAdapter = new ProductAdapter();
        mProductAdapter.setEmptyView(noDataView);
        mProductAdapter.setEnableLoadMore(true);
        mProductAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mProductAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                startActivityForResult(new Intent(this, CreateProductActivity.class), Constant.ADD_PRODUCT);
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        index = 1;
        requestProductList(false);
    }

    public void requestProductList(final boolean isLoadMore) {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "product/productListByTargetId.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("productType", Constant.PRODUCT + "");
        map.put("page", index + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                try {
                    ProductInfo productBaseResult = JSON.parseObject(json, ProductInfo.class);
                    if (productBaseResult.getE().getCode() == 0) {
                        List<Product> data = productBaseResult.getData();
                        if (data.size() > 0) {
                            if (!isLoadMore){
                                mProductAdapter.setNewData(data);
                            }else{
                                mProductAdapter.addData(data);
                            }
                            if (data.size() < Constant.PAGE_SIZE) {
                                mProductAdapter.loadMoreEnd(false);
                            } else {
                                mProductAdapter.loadMoreComplete();
                            }
                        } else {
                            if (!isLoadMore){
                                mProductAdapter.setNewData(new ArrayList<Product>());
                            }else{
                                mProductAdapter.loadMoreEnd(false);
                            }
                        }
                    } else {
                        if (!isLoadMore){
                            mProductAdapter.setNewData(new ArrayList<Product>());
                        }else{
                            mProductAdapter.loadMoreEnd(false);
                        }
                    }
                } catch (Exception e) {
                    if (!isLoadMore){
                        mProductAdapter.setNewData(new ArrayList<Product>());
                    }else{
                        mProductAdapter.loadMoreEnd(false);
                    }
                    e.printStackTrace();
                } finally {
                    if (loadType == 0) {
                        closeProgressDialog();
                    } else if (loadType == 1) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                    }
                    mProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mProductAdapter.loadMoreFail();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        index++;
        requestProductList(true);
    }
}
