package com.bjut.servicedog.servicedog.ui.coupon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.CouponStatisticsModel;
import com.bjut.servicedog.servicedog.po.CouponDownload;
import com.bjut.servicedog.servicedog.po.CouponDownloadList;
import com.bjut.servicedog.servicedog.rc_adapter.CouponDownloadListAdapter;
import com.bjut.servicedog.servicedog.rc_adapter.CouponStatisticsListAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

public class CouponStatisticsActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout srl_used;
    private SwipeRefreshLayout srl_not_used;
    private RecyclerView rv_used;
    private RecyclerView rv_not_used;

    private CouponDownloadListAdapter mCouponDownloadListAdapter;
    private CouponStatisticsListAdapter mCouponStatisticsListAdapter;

    private TextView usedtv, nousetv;
    private TextView tv_title;
    private LinearLayout choosell;
    private int pageUsed = 1, pageNot = 1;

    private final int TYPE_USED = 2;
    private final int TYPE_NOT_USED = 3;

    private Gson mGson;

    private String bid = "";
    private int loadType = 0;

    private final int GET_COUPON_INFO = 0;
    private final int GET_COUPON_INFO_ERROR = 1;
    private final int GET_COUPON_INFO_NULL = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_COUPON_INFO:
                    CouponStatisticsModel.DataBean dataBean = (CouponStatisticsModel.DataBean) msg.obj;
                    setView(dataBean);
                    requestDownList(pageUsed, TYPE_USED);
                    break;
                case GET_COUPON_INFO_ERROR:
                    toast(msg.obj.toString());
                    closeProgressDialog();
                    break;
                case GET_COUPON_INFO_NULL:
                    closeProgressDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_statistics_activity_r);

        init();
        requestStatics();
    }

    public void init() {
        mGson = new Gson();
        bid = getIntent().getStringExtra("bid");

        rv_used = (RecyclerView) findViewById(R.id.rv_used);
        srl_used = (SwipeRefreshLayout) findViewById(R.id.srl_used);

        rv_not_used = (RecyclerView) findViewById(R.id.rv_not_used);
        srl_not_used = (SwipeRefreshLayout) findViewById(R.id.srl_not_used);
        usedtv = (TextView) findViewById(R.id.used);
        tv_title = (TextView) findViewById(R.id.tv_title);
        nousetv = (TextView) findViewById(R.id.nouse);
        choosell = (LinearLayout) findViewById(R.id.choose);

        tv_title.setText(getString(R.string.title_yhqtj));
        usedtv.setOnClickListener(this);
        nousetv.setOnClickListener(this);


        srl_used.setColorSchemeResources(R.color.title_background);
        rv_used.setLayoutManager(new LinearLayoutManager(mContext));
        rv_used.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mCouponDownloadListAdapter = new CouponDownloadListAdapter();
        mCouponDownloadListAdapter.setEmptyView(noDataView);
        mCouponDownloadListAdapter.setEnableLoadMore(true);
        mCouponDownloadListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadType = 2;
                pageUsed++;
                requestDownList(pageUsed, TYPE_USED);
            }
        });
        srl_used.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadType = 1;
                pageUsed = 1;
                requestDownList(pageUsed, TYPE_USED);
            }
        });
//        mAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mCouponDownloadListAdapter.isFirstOnly(true);
        rv_used.setAdapter(mCouponDownloadListAdapter);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_nodate, null, false);
        srl_not_used.setColorSchemeResources(R.color.title_background);
        rv_not_used.setLayoutManager(new LinearLayoutManager(mContext));
        rv_not_used.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mCouponStatisticsListAdapter = new CouponStatisticsListAdapter();
        mCouponStatisticsListAdapter.setEmptyView(emptyView);
        mCouponStatisticsListAdapter.setEnableLoadMore(true);
        mCouponStatisticsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNot++;
                loadType = 2;
                requestDownList(pageNot, TYPE_NOT_USED);
            }
        });
        srl_not_used.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNot = 1;
                loadType = 1;
                requestDownList(pageNot, TYPE_NOT_USED);
            }
        });
//        mAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mCouponStatisticsListAdapter.isFirstOnly(true);
        rv_not_used.setAdapter(mCouponStatisticsListAdapter);

        srl_used.setVisibility(View.VISIBLE);
        srl_not_used.setVisibility(View.GONE);
    }

    public void requestDownList(final int page, final int type) {
        String urlString = "coupon/coupon_history.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("bid", bid);//优惠券批次ID必选
        map.put("type", type + "");//type   -1全部   1 下载历史 2 使用历史 3 下载未使用
        map.put("page", page + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
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
                            CouponDownloadList couponDownloadList = JSON.parseObject(json, CouponDownloadList.class);
                            if (couponDownloadList.getE().getCode() == 0) {
                                if (couponDownloadList.getData().size() > 0) {
                                    List<CouponDownload> data = couponDownloadList.getData();
                                    if (type == TYPE_USED) {
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
                                            mCouponStatisticsListAdapter.setNewData(data);
                                        } else {
                                            mCouponStatisticsListAdapter.addData(data);
                                        }
                                        if (data.size() < Constant.PAGE_SIZE) {
                                            mCouponStatisticsListAdapter.loadMoreEnd(false);
                                        } else {
                                            mCouponStatisticsListAdapter.loadMoreComplete();
                                        }
                                    }
                                } else {
                                }
                            } else {
                                if (type == TYPE_USED) {
                                    if (page == 1) {
                                        mCouponDownloadListAdapter.setNewData(new ArrayList<CouponDownload>());
                                    } else {
                                        mCouponDownloadListAdapter.loadMoreEnd(false);
                                    }
                                } else {
                                    if (page == 1) {
                                        mCouponStatisticsListAdapter.setNewData(new ArrayList<CouponDownload>());
                                    } else {
                                        mCouponStatisticsListAdapter.loadMoreEnd(false);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (type == TYPE_USED) {
                                if (page == 1) {
                                    mCouponDownloadListAdapter.setNewData(new ArrayList<CouponDownload>());
                                } else {
                                    mCouponDownloadListAdapter.loadMoreEnd(false);
                                }
                            } else {
                                if (page == 1) {
                                    mCouponStatisticsListAdapter.setNewData(new ArrayList<CouponDownload>());
                                } else {
                                    mCouponStatisticsListAdapter.loadMoreEnd(false);
                                }
                            }
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                if (type == TYPE_USED) {
                                    srl_used.setRefreshing(false);
                                } else {
                                    srl_not_used.setRefreshing(false);
                                }
                            } else {
                            }
                            if (type == TYPE_USED) {
                                mCouponDownloadListAdapter.notifyDataSetChanged();
                            } else {
                                mCouponStatisticsListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                if (type == TYPE_USED) {
                    srl_used.setRefreshing(false);
                    mCouponDownloadListAdapter.loadMoreFail();
                } else {
                    srl_not_used.setRefreshing(false);
                    mCouponStatisticsListAdapter.loadMoreFail();
                }
            }
        });
    }

    public void requestStatics() {
        showProgressDialog();
        String urlString = "coupon/couponstat.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap();
        map.put("sid", pref.getString("sid", ""));
        map.put("bid", bid);//优惠券批次ID必选
        RequestParams params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                try {
                    CouponStatisticsModel couponStatisticsModel = mGson.fromJson(responseInfo.result, CouponStatisticsModel.class);
                    if (couponStatisticsModel.getE().getCode() == 0) {
                        List<CouponStatisticsModel.DataBean> data = couponStatisticsModel.getData();
                        if (data != null && data.size() > 0) {
                            Message msg = new Message();
                            msg.what = GET_COUPON_INFO;
                            msg.obj = data.get(0);
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = GET_COUPON_INFO_NULL;
                            mHandler.sendMessage(msg);
                        }
                    } else {
                        Message msg = new Message();
                        msg.what = GET_COUPON_INFO;
                        msg.obj = couponStatisticsModel.getE().getDesc();
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = GET_COUPON_INFO_NULL;
                    mHandler.sendMessage(msg);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void setView(CouponStatisticsModel.DataBean couponDetail) {
        TextView issue_count = (TextView) findViewById(R.id.issue_count);
        TextView down_count = (TextView) findViewById(R.id.down_count);
        TextView down_percentage = (TextView) findViewById(R.id.down_percentage);
        TextView not_used = (TextView) findViewById(R.id.not_used);
        TextView usedcount = (TextView) findViewById(R.id.usedcount);
        TextView average = (TextView) findViewById(R.id.average);
        TextView consumption_ratio = (TextView) findViewById(R.id.consumption_ratio);
        TextView spending = (TextView) findViewById(R.id.spending);
        issue_count.setText(couponDetail.getIssue_count());
        down_count.setText(couponDetail.getDown_count());
        down_percentage.setText(couponDetail.getDown_ratio());
        not_used.setText(couponDetail.getNot_used());
        usedcount.setText(couponDetail.getUsed());
        average.setText(couponDetail.getAverage());
        consumption_ratio.setText(couponDetail.getUse_ratio());
        spending.setText(couponDetail.getSales());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.used:
                srl_used.setVisibility(View.VISIBLE);
                srl_not_used.setVisibility(View.GONE);
                choosell.setBackgroundResource(R.drawable.xiazaiqkbg);
                usedtv.setTextColor(getResources().getColor(R.color.white));
                nousetv.setTextColor(getResources().getColor(R.color.orange));
                loadType = 0;
                showProgressDialog();
                pageUsed = 1;
                requestDownList(pageUsed, TYPE_USED);
                break;
            case R.id.nouse:
                srl_used.setVisibility(View.GONE);
                srl_not_used.setVisibility(View.VISIBLE);
                choosell.setBackgroundResource(R.drawable.quanxiangqingbg);
                nousetv.setTextColor(getResources().getColor(R.color.white));
                usedtv.setTextColor(getResources().getColor(R.color.orange));
                loadType = 0;
                pageNot = 1;
                showProgressDialog();
                requestDownList(pageNot, TYPE_NOT_USED);
                break;
        }
    }
}
