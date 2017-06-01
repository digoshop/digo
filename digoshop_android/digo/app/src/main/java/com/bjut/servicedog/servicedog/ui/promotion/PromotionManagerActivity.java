package com.bjut.servicedog.servicedog.ui.promotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.bjut.servicedog.servicedog.adapter.ViewPagerAdapter;
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.model.ProductInfo;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.rc_adapter.AuctionProductAdapter;
import com.bjut.servicedog.servicedog.rc_adapter.ExchangeProductAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.MyViewPager;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/2/16.
 */

public class PromotionManagerActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private Button btn_right;
    private MyViewPager mMyViewPager;
    private TextView tv_left;
    private TextView tv_right;

    private View mExchangeView;
    private View mAuctionView;
    private List<View> mViewList = new ArrayList<View>();

    private RecyclerView mExchangeRecyclerView;
    private RecyclerView mAuctionRecyclerView;
    private SwipeRefreshLayout mExchangeSwipeRefreshLayout;
    private SwipeRefreshLayout mAuctionSwipeRefreshLayout;

    private ExchangeProductAdapter mExchangeProductAdapter;
    private AuctionProductAdapter mAuctionProductAdapter;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Product> mAuctionList = new ArrayList<>();
    private int exchangeIndex = 1;
    private int auctionIndex = 1;
    private int choise = 0;

    private int loadType = 0;
    private boolean isExchange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_manager);
        EventBus.getDefault().register(this);
        initViews();
        setListener();
        requestProductList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.ADD_PROMOTION:
                    int type = data.getIntExtra("type", Constant.EXCHANGE_PRODUCT);
                    switch (type) {
                        case Constant.EXCHANGE_PRODUCT:
                            isExchange = true;
                            mMyViewPager.setCurrentItem(0);
                            exchangeIndex = 1;
                            requestProductList();
                            break;
                        case Constant.AUCTION_PRODUCT:
                            auctionIndex = 1;
                            if (!isExchange) {
                                requestProductList();
                            } else {
                                mMyViewPager.setCurrentItem(1);
                            }
                            isExchange = false;
                            break;
                    }
                    break;
                case Constant.UPDATE_PRODUCT:
                    isExchange = true;
                    exchangeIndex = 1;
                    requestProductList();
                    break;
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(OnRefreshEvent onRefreshEvent) {
        if (onRefreshEvent.isRefresh()) {
            isExchange = true;
            exchangeIndex = 1;
            requestProductList();
        }
    }

    private void setListener() {
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        mMyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        isExchange = true;
                        tv_left.setBackgroundResource(R.drawable.switch_button_left_checked);
                        tv_right.setBackgroundResource(R.drawable.switch_button_right);
                        tv_left.setTextColor(getResources().getColor(R.color.white));
                        tv_right.setTextColor(getResources().getColor(R.color.orange));
                        mMyViewPager.setCurrentItem(0);
                        break;
                    case 1:
                        isExchange = false;
                        tv_left.setBackgroundResource(R.drawable.switch_button_left);
                        tv_right.setBackgroundResource(R.drawable.switch_button_right_checked);
                        tv_left.setTextColor(getResources().getColor(R.color.orange));
                        tv_right.setTextColor(getResources().getColor(R.color.white));
                        mMyViewPager.setCurrentItem(1);
                        if (mAuctionList.size() == 0) {
                            requestProductList();
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mExchangeRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ExchangeProductDetailActivity.class);
                intent.putExtra("productId", mExchangeProductAdapter.getItem(position).getPid() + "");
                startActivity(intent);
            }
        });

        mAuctionRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, AuctionProductDetailActivity.class);
                intent.putExtra("productId", mAuctionProductAdapter.getItem(position).getPid() + "");
                startActivity(intent);
            }
        });

        mExchangeRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_update:
                        choise = position;
                        Intent intent = new Intent(mContext, UpdateExchangeProductActivity.class);
                        intent.putExtra("productId", mExchangeProductAdapter.getItem(position).getPid() + "");
                        startActivityForResult(intent, Constant.UPDATE_PRODUCT);
                        break;
                    case R.id.img_delete:
                        deleteProduct(position, mExchangeProductAdapter.getItem(position).getPid());
                        break;
                    case R.id.tv_is_use:
                        Product item = mExchangeProductAdapter.getItem(position);
                        String status = ((TextView) view).getText().toString();
                        if (mContext.getString(R.string.btn_shangjia).equals(status)) {
                            operationExchange(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELVES);
                        } else  if (mContext.getString(R.string.btn_xiajia).equals(status)){
                            operationExchange(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELF);
                        }
                    case R.id.btn_is_tui:
                        Product product = mExchangeProductAdapter.getItem(position);
                        String str = ((TextView) view).getText().toString();
                        if (mContext.getString(R.string.btn_tuijian).equals(str)) {
                            tuiExchangeProduct(position, product.getPid(), Constant.TUI_YES);
                        } else if (mContext.getString(R.string.btn_cancle).equals(str)){
                            tuiExchangeProduct(position, product.getPid(), Constant.TUI_NO);
                        }
                        break;
                }
            }
        });

        mAuctionRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_update:
                        choise = position;
                        Intent intent = new Intent(mContext, UpdateAuctionProductActivity.class);
                        intent.putExtra("productId", mAuctionProductAdapter.getItem(position).getPid() + "");
                        startActivityForResult(intent, Constant.UPDATE_PRODUCT);
                        break;
                    case R.id.img_delete:
                        deleteProduct(position, mAuctionProductAdapter.getItem(position).getPid());
                        break;
                    case R.id.tv_is_use:
                        Product item = mAuctionProductAdapter.getItem(position);
                        String status = ((TextView) view).getText().toString();
                        if (status.equals("上架")) {
                            operationAuction(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELVES);
                        } else {
                            operationAuction(position, item.getPid(), Constant.PRODUCT_OPERATION_SHELF);
                        }
                        break;
                }
            }
        });
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText("新增促销");
        btn_right.setVisibility(View.VISIBLE);
        mMyViewPager = (MyViewPager) findViewById(R.id.myViewPager);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);

        initExchangeViews();
        initAuctionViews();
        mViewPagerAdapter = new ViewPagerAdapter(mViewList);
        mMyViewPager.setAdapter(mViewPagerAdapter);
        tv_title.setText("促销管理");
        mMyViewPager.setNoScroll(true);
    }

    private void initExchangeViews() {
        mExchangeView = this.getLayoutInflater().inflate(R.layout.layout_product_list, null);
        mExchangeSwipeRefreshLayout = (SwipeRefreshLayout) mExchangeView.findViewById(R.id.swipeRefreshLayout);
        mExchangeRecyclerView = (RecyclerView) mExchangeView.findViewById(R.id.recyclerView);


        mExchangeProductAdapter = new ExchangeProductAdapter();
        mExchangeProductAdapter.setEmptyView(noDataView);
        mExchangeProductAdapter.setEnableLoadMore(true);
        mExchangeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadType = 2;
                exchangeIndex++;
                requestProductList();
            }
        });
//        mExchangeProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mExchangeProductAdapter.isFirstOnly(true);

        mExchangeSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mExchangeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadType = 1;
                exchangeIndex = 1;
//                mExchangeList.clear();
                requestProductList();
            }
        });
        mExchangeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mExchangeRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mExchangeRecyclerView.setAdapter(mExchangeProductAdapter);
        mViewList.add(mExchangeView);
    }

    private void initAuctionViews() {
        mAuctionView = this.getLayoutInflater().inflate(R.layout.layout_product_list, null);
        mAuctionSwipeRefreshLayout = (SwipeRefreshLayout) mAuctionView.findViewById(R.id.swipeRefreshLayout);
        mAuctionRecyclerView = (RecyclerView) mAuctionView.findViewById(R.id.recyclerView);

        mAuctionProductAdapter = new AuctionProductAdapter(mAuctionList);
//        mAuctionProductAdapter.setEmptyView(noDataView);
        mAuctionProductAdapter.setEnableLoadMore(true);
        mAuctionProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadType = 2;
                auctionIndex++;
                requestProductList();
            }
        });
//        mAuctionProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAuctionProductAdapter.isFirstOnly(true);

        mAuctionSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mAuctionSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadType = 1;
                auctionIndex = 1;
//                mAuctionList.clear();
                requestProductList();
            }
        });
        mAuctionRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAuctionRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAuctionRecyclerView.setAdapter(mAuctionProductAdapter);
        mViewList.add(mAuctionView);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                isExchange = true;
                tv_left.setBackgroundResource(R.drawable.switch_button_left_checked);
                tv_right.setBackgroundResource(R.drawable.switch_button_right);
                tv_left.setTextColor(getResources().getColor(R.color.white));
                tv_right.setTextColor(getResources().getColor(R.color.orange));
                mMyViewPager.setCurrentItem(0);
                break;
            case R.id.tv_right:
                isExchange = false;
                tv_left.setBackgroundResource(R.drawable.switch_button_left);
                tv_right.setBackgroundResource(R.drawable.switch_button_right_checked);
                tv_left.setTextColor(getResources().getColor(R.color.orange));
                tv_right.setTextColor(getResources().getColor(R.color.white));
                mMyViewPager.setCurrentItem(1);
//                if (mAuctionList.size() == 0) {
//                    requestProductList();
//                }
                break;
            case R.id.btn_right:
                Intent intent = new Intent(mContext, CreatePromotionActivity.class);
                startActivityForResult(intent, Constant.ADD_PROMOTION);
                break;
        }
    }

    public void requestProductList() {
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
        if (isExchange) {
            map.put("productType", Constant.EXCHANGE_PRODUCT + "");
        } else {
            map.put("productType", Constant.AUCTION_PRODUCT + "");
        }
        if (isExchange) {
            map.put("page", exchangeIndex + "");
        } else {
            map.put("page", auctionIndex + "");
        }
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
                        final List<Product> data = productBaseResult.getData();
                        if (data.size() > 0) {
                            if (isExchange) {
                                if (exchangeIndex == 1) {
                                    mExchangeProductAdapter.setNewData(data);
                                } else {
                                    mExchangeProductAdapter.addData(data);
                                }
                                if (data.size() < Constant.PAGE_SIZE) {
                                    mExchangeProductAdapter.loadMoreEnd(false);
                                } else {
                                    mExchangeProductAdapter.loadMoreComplete();
                                }
                            } else {
                                if (exchangeIndex == 1) {
                                    mAuctionProductAdapter.setNewData(data);
                                } else {
                                    mAuctionList.addAll(data);
                                }
                                if (data.size() < Constant.PAGE_SIZE) {
                                    mAuctionProductAdapter.loadMoreEnd(false);
                                } else {
                                    mAuctionProductAdapter.loadMoreComplete();
                                }
                            }
                        } else {
                            if (isExchange) {
                                if (exchangeIndex == 1) {
                                    mExchangeProductAdapter.setNewData(new ArrayList<Product>());
                                } else {
                                    mExchangeProductAdapter.loadMoreEnd(false);
                                }
                            } else {
                            }
                        }
                    } else {
                        if (isExchange) {
                            if (exchangeIndex == 1) {
                                mExchangeProductAdapter.setNewData(new ArrayList<Product>());
                            } else {
                                mExchangeProductAdapter.loadMoreEnd(false);
                            }
                        } else {
                        }
                        toast(productBaseResult.getE().getDesc());
                    }
                } catch (Exception e) {
                    if (isExchange) {
                        if (exchangeIndex == 1) {
                            mExchangeProductAdapter.setNewData(new ArrayList<Product>());
                        } else {
                            mExchangeProductAdapter.loadMoreEnd(false);
                        }
                    } else {
                        mAuctionProductAdapter.loadMoreEnd(false);
                    }
                    e.printStackTrace();
                } finally {
                    if (loadType == 0) {
                        closeProgressDialog();
                    } else if (loadType == 1) {
                        if (isExchange) {
                            mExchangeSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            mAuctionSwipeRefreshLayout.setRefreshing(false);
                        }

                    } else {
                    }
                    if (isExchange) {
                        mExchangeProductAdapter.notifyDataSetChanged();
                    } else {
                        mAuctionProductAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                if (isExchange) {
                    mExchangeSwipeRefreshLayout.setRefreshing(false);
                    mExchangeProductAdapter.loadMoreFail();
                } else {
                    mAuctionSwipeRefreshLayout.setRefreshing(false);
                    mAuctionProductAdapter.loadMoreFail();
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
                if (isExchange) {
                    operationExchange(position, pid, Constant.PRODUCT_OPERATION_DELETE);
                } else {
                    operationAuction(position, pid, Constant.PRODUCT_OPERATION_DELETE);
                }
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

    private void operationExchange(final int position, final long id, final int ps) {
        String urlString = "product/del_product.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", id + "");//获取商品ID
        map.put("sid", pref.getString("sid", ""));
        map.put("pt", Constant.EXCHANGE_PRODUCT + "");//1代表竞拍,2代表兑换,3普通商品
        map.put("ps", ps + "");//状态(2禁用 99删除)
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                        if (Constant.PRODUCT_OPERATION_SHELVES == ps) {
                            Product item = mExchangeProductAdapter.getItem(position);
                            item.setEps(Constant.PRODUCT_STATUS_EXCHANGE_NOTSTART);
//                            mExchangeProductAdapter.notifyItemChanged(position, item);
                        } else if (Constant.PRODUCT_OPERATION_SHELF == ps) {
                            Product item = mExchangeProductAdapter.getItem(position);
                            item.setEps(Constant.PRODUCT_STATUS_EXCHANGE_SHELF);
//                            mExchangeProductAdapter.notifyItemChanged(position, item);
                        } else if (Constant.PRODUCT_OPERATION_DELETE == ps) {
                            mExchangeProductAdapter.remove(position);
                        }
                    } else {
                        toast("操作失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    mExchangeProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void operationAuction(final int position, final long id, final int ps) {
        String urlString = "product/del_product.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", id + "");//获取商品ID
        map.put("sid", pref.getString("sid", ""));
        map.put("pt", Constant.AUCTION_PRODUCT + "");//1代表竞拍,2代表兑换,3普通商品
        map.put("ps", ps + "");//状态(2禁用 99删除)
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                        if (Constant.PRODUCT_OPERATION_SHELVES == ps) {
                            Product item = mAuctionList.get(position);
                            item.setAps(Constant.PRODUCT_STATUS_AUCTION_NOTSTART);
//                            mAuctionProductAdapter.notifyItemChanged(position, item);
                        } else if (Constant.PRODUCT_OPERATION_SHELF == ps) {
                            Product item = mAuctionList.get(position);
                            item.setAps(Constant.PRODUCT_STATUS_EXCHANGE_SHELF);
//                            mAuctionProductAdapter.notifyItemChanged(position, item);
                        } else if (Constant.PRODUCT_OPERATION_DELETE == ps) {
                            mAuctionProductAdapter.remove(position);
                        }
                    } else {
                        toast("操作失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    mAuctionProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void tuiExchangeProduct(final int position, final long id, final int prcmd) {
        String urlString = "product/rcmd_product.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("pid", id + "");//获取商品ID
        map.put("sid", pref.getString("sid", ""));
        map.put("pt", Constant.EXCHANGE_PRODUCT + "");//1代表竞拍,2代表兑换,3普通商品
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
                                    Product item = mExchangeProductAdapter.getItem(position);
                                    item.setPrcmd(Constant.TUI_NO);
                                } else if (Constant.TUI_YES == prcmd) {
                                    Product item = mExchangeProductAdapter.getItem(position);
                                    item.setPrcmd(Constant.TUI_YES);
                                    mExchangeProductAdapter.notifyItemChanged(position, item);
                                }
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("desc"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mExchangeProductAdapter.notifyDataSetChanged();
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
}
