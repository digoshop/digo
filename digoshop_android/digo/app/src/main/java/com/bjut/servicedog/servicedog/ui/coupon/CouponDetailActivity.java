package com.bjut.servicedog.servicedog.ui.coupon;

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
import com.bjut.servicedog.servicedog.po.CouponBatch;
import com.bjut.servicedog.servicedog.po.CouponDetail;
import com.bjut.servicedog.servicedog.po.CouponDownload;
import com.bjut.servicedog.servicedog.po.CouponDownloadList;
import com.bjut.servicedog.servicedog.po.CouponType;
import com.bjut.servicedog.servicedog.rc_adapter.CouponDownloadListAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
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
 * Created by beibeizhu on 17/5/25.
 */

public class CouponDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private TextView tv_left;
    private TextView tv_right;
    private TextView tv_title;
    private LinearLayout ll_classification;
    private LinearLayout ll_detail;
    private TextView tv_coupon_type;
    private TextView tv_coupon_theme;
    private TextView tv_money_title;
    private TextView tv_coupon_money;
    private TextView tv_danwei;
    private LinearLayout ll_limit;
    private TextView tv_coupon_limit;
    private LinearLayout ll_full;
    private TextView tv_coupon_full;
    private TextView tv_coupon_number;
    private TextView tv_coupon_value;
    private TextView tv_date_start;
    private TextView tv_date_end;
    private TextView tv_coupon_receive;
    private LinearLayout ll_zeng;
    private TextView tv_coupon_zeng;
    private TextView tv_coupon_scope;
    private TextView tv_coupon_category;
    private TextView tv_coupon_explain;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CouponDownloadListAdapter mCouponDownloadListAdapter;

    private String couponBatchId = "";
    private int loadType = 0;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initViews();
        setListener();
        requestCouponDetail();
    }

    public int getViewId() {
        return R.layout.activity_coupon_detail;
    }

    public void initViews() {

        tv_title = $(R.id.tv_title);

        ll_classification = $(R.id.ll_classification);
        tv_left = $(R.id.tv_left);
        tv_right = $(R.id.tv_right);


        mRecyclerView = $(R.id.recyclerView);
        mSwipeRefreshLayout = $(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mCouponDownloadListAdapter = new CouponDownloadListAdapter();
        mCouponDownloadListAdapter.setEmptyView(noDataView);
        mCouponDownloadListAdapter.setEnableLoadMore(true);
        mCouponDownloadListAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mCouponDownloadListAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mCouponDownloadListAdapter);

        ll_detail = $(R.id.ll_detail);
        tv_coupon_type = $(R.id.tv_coupon_type);
        tv_coupon_theme = $(R.id.tv_coupon_theme);
        tv_money_title = $(R.id.tv_money_title);
        tv_coupon_money = $(R.id.tv_coupon_money);
        tv_danwei = $(R.id.tv_danwei);
        ll_full = $(R.id.ll_full);
        tv_coupon_full = $(R.id.tv_coupon_full);
        ll_limit = $(R.id.ll_limit);
        tv_coupon_limit = $(R.id.tv_coupon_limit);
        tv_coupon_number = $(R.id.tv_coupon_number);
        tv_coupon_value = $(R.id.tv_coupon_value);
        tv_date_start = $(R.id.tv_date_start);
        tv_date_end = $(R.id.tv_date_end);
        tv_coupon_receive = $(R.id.tv_coupon_receive);
        ll_zeng = $(R.id.ll_zeng);
        tv_coupon_zeng = $(R.id.tv_coupon_zeng);
        tv_coupon_scope = $(R.id.tv_coupon_scope);
        tv_coupon_category = $(R.id.tv_coupon_category);
        tv_coupon_explain = $(R.id.tv_coupon_explain);


        tv_title.setText(getString(R.string.title_yhqck));
        String auditType = getIntent().getStringExtra("auditType");
        String rule = getIntent().getStringExtra("rule");
        couponBatchId = getIntent().getStringExtra("couponBatchId");
        Log.i(TAG, "couponBatchId===========接受：" + couponBatchId);
        if (rule.equals("1")) {
            tv_coupon_scope.setText("全国连锁");
        } else {
            tv_coupon_scope.setText("只限本店");
        }
        if (Constant.COUPON_STATUS_NO.equals(auditType)) {
            ll_classification.setVisibility(View.GONE);
        } else {
            ll_classification.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    private void setView(CouponDetail couponDetail) {
        CouponBatch couponBatch = couponDetail.getData().getCouponBatch();
        CouponType couponType = couponDetail.getData().getCouponType();

        int limit = couponBatch.getCbrm();
        String ctid = couponType.getCtid();
        String dateStart = DateUtil.strFormatStr(couponBatch.getStarTime());
        String dateEnd = DateUtil.strFormatStr(couponBatch.getEndTime());
        int cbrt = couponBatch.getCbrt();

        switch (cbrt) {
            case Constant.PAYMENT_NORMAL:
                ll_zeng.setVisibility(View.GONE);
                tv_coupon_receive.setText("正常领取");
                break;
            case Constant.PAYMENT_ZENG:
                ll_zeng.setVisibility(View.VISIBLE);
                tv_coupon_receive.setText("消费满赠");
                tv_coupon_zeng.setText(couponBatch.getCbcpa());
                break;
            default:
                ll_zeng.setVisibility(View.GONE);
                tv_coupon_receive.setText("正常领取");
                break;
        }

        switch (limit) {
            case Constant.LIMIT_YES:
                ll_limit.setVisibility(View.VISIBLE);
                tv_coupon_limit.setText(couponBatch.getCbrnu());
                break;
            case Constant.LIMIT_NO:
                ll_limit.setVisibility(View.GONE);
                break;
            default:
                ll_limit.setVisibility(View.GONE);
                break;
        }

        switch (ctid) {
            case Constant.COUPON_DAI:
                tv_coupon_type.setText(getString(R.string.daijinquan));
                tv_money_title.setText("券面金额");
                tv_coupon_money.setText(couponBatch.getCbca());
                tv_danwei.setText(getString(R.string.yuan));
                ll_full.setVisibility(View.GONE);
                break;
            case Constant.COUPON_ZHE:
                tv_coupon_type.setText(getString(R.string.zhekouquan));
                tv_money_title.setText("折扣率");
                tv_coupon_money.setText(couponBatch.getCbrStr());
                tv_danwei.setText(getString(R.string.zhe));
                ll_full.setVisibility(View.VISIBLE);
                tv_coupon_full.setText(couponBatch.getCbcf());
                break;
            case Constant.COUPON_MAN:
                tv_coupon_type.setText(getString(R.string.manjianquan));
                tv_money_title.setText("券面金额");
                tv_coupon_money.setText(couponBatch.getCbca());
                tv_danwei.setText(getString(R.string.yuan));
                ll_full.setVisibility(View.VISIBLE);
                tv_coupon_full.setText(couponBatch.getCbcf());
                break;
        }
        tv_coupon_theme.setText(couponBatch.getCbn());
        tv_coupon_number.setText(couponBatch.getCbnu());
        tv_coupon_value.setText(couponBatch.getCbea() + "");
        tv_date_start.setText(dateStart);
        tv_date_end.setText(dateEnd);
        tv_coupon_category.setText(couponBatch.getCategoryStr());
        tv_coupon_explain.setText(couponBatch.getCbd());
    }

    public void requestCouponDetail() {
        showProgressDialog();
        String urlString = "coupon/query_batch_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("couponBatchId", couponBatchId);
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
                        Log.i("yyyy", json);
                        try {
                            CouponDetail couponDetail = JSON.parseObject(json, CouponDetail.class);
                            if (couponDetail.getE().getCode() == 0) {
                                Log.i("yyyy", couponDetail.getData().getCouponBatch().getStatus() + "");
                                setView(couponDetail);
                                requestDownList();
                            } else {
                                toast(couponDetail.getE().getDesc());
                                closeProgressDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                tv_left.setBackgroundResource(R.drawable.switch_button_left_checked);
                tv_right.setBackgroundResource(R.drawable.switch_button_right);
                tv_left.setTextColor(getResources().getColor(R.color.white));
                tv_right.setTextColor(getResources().getColor(R.color.orange));
                mSwipeRefreshLayout.setVisibility(View.GONE);
                ll_detail.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_right:
                tv_left.setBackgroundResource(R.drawable.switch_button_left);
                tv_right.setBackgroundResource(R.drawable.switch_button_right_checked);
                tv_left.setTextColor(getResources().getColor(R.color.orange));
                tv_right.setTextColor(getResources().getColor(R.color.white));
                ll_detail.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void requestDownList() {
        String urlString = "coupon/coupon_history.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
//        map.put("bid", pref.getString("CouponId", ""));//优惠券批次ID必选
        map.put("bid", couponBatchId);//优惠券批次ID必选
        Log.i("bid", pref.getString("uid", ""));
        Log.i("bid", pref.getString("CouponId", ""));
        map.put("type", "1");//type   -1全部   1 下载历史 2 使用历史 3 下载未使用
        map.put("page", page + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
        params = sortMapByKey(map);
        params.addBodyParameter("uid", pref.getString("uid", ""));
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
                            CouponDownloadList couponDownloadList = JSON.parseObject(json, CouponDownloadList.class);
                            if (couponDownloadList.getE().getCode() == 0) {
                                List<CouponDownload> data = couponDownloadList.getData();

                                if (couponDownloadList.getData().size() > 0) {
                                    if (page == 1) {
                                        mCouponDownloadListAdapter.setNewData(data);
                                    } else {
                                        mCouponDownloadListAdapter.addData(data);
                                    }
                                    if (data.size() < Constant.PAGE_SIZE) {
                                        mCouponDownloadListAdapter.loadMoreEnd(false);
                                    } else {
                                        mCouponDownloadListAdapter.loadMoreComplete();
                                    }
                                } else {
                                    if (page == 1) {
                                        mCouponDownloadListAdapter.setNewData(new ArrayList<CouponDownload>());
                                    } else {
                                        mCouponDownloadListAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                if (page == 1) {
                                    mCouponDownloadListAdapter.setNewData(new ArrayList<CouponDownload>());
                                } else {
                                    mCouponDownloadListAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (page == 1) {
                                mCouponDownloadListAdapter.setNewData(new ArrayList<CouponDownload>());
                            } else {
                                mCouponDownloadListAdapter.loadMoreEnd(false);
                            }
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mCouponDownloadListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mCouponDownloadListAdapter.loadMoreFail();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        page = 1;
        requestDownList();
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestDownList();
    }
}
