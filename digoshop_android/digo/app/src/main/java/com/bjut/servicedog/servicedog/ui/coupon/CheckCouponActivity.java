package com.bjut.servicedog.servicedog.ui.coupon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CouponDetail;
import com.bjut.servicedog.servicedog.po.CouponDownload;
import com.bjut.servicedog.servicedog.po.CouponDownloadList;
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


public class CheckCouponActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private TextView downloadtv, detailtv, couponsType;
    private TextView tv_title;
    private LinearLayout choosell;
    private ScrollView sbSv;
    private int status, cbas;
    private int page = 1;
    private LinearLayout ll_cbcf;
    private LinearLayout ll_limit;
    private TextView tv_limit;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CouponDownloadListAdapter mCouponDownloadListAdapter;

    private String couponBatchId = "";
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_coupon_activity_r);
        init();
        requestCouponDetail();
    }

    public void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ll_cbcf = (LinearLayout) findViewById(R.id.ll_cbcf);
        downloadtv = (TextView) findViewById(R.id.download);
        detailtv = (TextView) findViewById(R.id.detail);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_yhqck));
        choosell = (LinearLayout) findViewById(R.id.choose);
        sbSv = (ScrollView) findViewById(R.id.sb);
        detailtv.setOnClickListener(this);
        downloadtv.setOnClickListener(this);

        TextView shopScope = (TextView) findViewById(R.id.tv_shopScope);
        couponsType = (TextView) findViewById(R.id.tv_coupons_type);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mCouponDownloadListAdapter = new CouponDownloadListAdapter();
        mCouponDownloadListAdapter.setEmptyView(noDataView);
        mCouponDownloadListAdapter.setEnableLoadMore(true);
        mCouponDownloadListAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mCouponDownloadListAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mCouponDownloadListAdapter);
        ll_limit = (LinearLayout) findViewById(R.id.ll_limit);
        tv_limit = (TextView) findViewById(R.id.tv_limit);


        String auditType = getIntent().getStringExtra("auditType");
        String rule = getIntent().getStringExtra("rule");
        couponBatchId = getIntent().getStringExtra("couponBatchId");
        Log.i(TAG, "couponBatchId===========接受：" + couponBatchId);
        status = getIntent().getIntExtra("status", 0);
        cbas = getIntent().getIntExtra("cbas", 0);
        Log.i("zzzr", rule);
        if (rule.equals("1")) {
            shopScope.setText("全国连锁");
        } else {
            shopScope.setText("只限本店");
        }
        if (auditType.equals("1")) {
            sbSv.setVisibility(View.VISIBLE);
            choosell.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                sbSv.setVisibility(View.GONE);
                choosell.setBackgroundResource(R.drawable.xiazaiqkbg);
                downloadtv.setTextColor(getResources().getColor(R.color.white));
                detailtv.setTextColor(getResources().getColor(R.color.orange));

                break;
            case R.id.detail:
                sbSv.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                choosell.setBackgroundResource(R.drawable.quanxiangqingbg);
                detailtv.setTextColor(getResources().getColor(R.color.white));
                downloadtv.setTextColor(getResources().getColor(R.color.orange));
                break;
        }


    }

    public void requestCouponDetail() {
        showProgressDialog();
        String urlString = "coupon/query_batch_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("couponBatchId", couponBatchId);
        RequestParams params  = sortMapByKey(map);
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

    public void setView(CouponDetail couponDetail) {

        TextView ad = (TextView) findViewById(R.id.ad);
        TextView count = (TextView) findViewById(R.id.count);

        TextView kind = (TextView) findViewById(R.id.kind);
        TextView pre = (TextView) findViewById(R.id.pre);
        TextView money = (TextView) findViewById(R.id.money);
        TextView condition = (TextView) findViewById(R.id.condition);
        TextView cbcftop = (TextView) findViewById(R.id.cbcftop);
        TextView t4 = (TextView) findViewById(R.id.t4);
        TextView yuan = (TextView) findViewById(R.id.yuan);
        TextView cbd = (TextView) findViewById(R.id.tv_cbd);
        TextView categorys = (TextView) findViewById(R.id.tv_categorys);
        TextView cbca = (TextView) findViewById(R.id.cbca);
        TextView time = (TextView) findViewById(R.id.time);
        ImageView img_left = (ImageView) findViewById(R.id.img_left);
        ImageView img_right_info = (ImageView) findViewById(R.id.img_right_info);
        String[] category = couponDetail.getData().getCouponBatch().getCategorys();
        cbd.setText(couponDetail.getData().getCouponBatch().getCbd());
        ad.setText(couponDetail.getData().getCouponBatch().getCbn());
        String createTime = DateUtil.strFormatStr(couponDetail.getData().getCouponBatch().getCreatTime());
        count.setText(createTime);

        String start = DateUtil.strFormatStr(couponDetail.getData().getCouponBatch().getStarTime());
        String end = DateUtil.strFormatStr(couponDetail.getData().getCouponBatch().getEndTime());
        time.setText(start + "至" + end);
        condition.setText(couponDetail.getData().getCouponBatch().getCategoryStr());
        categorys.setText(couponDetail.getData().getCouponBatch().getCategoryStrDun());

        int limit = couponDetail.getData().getCouponBatch().getCbrm();
        if (limit==1) {
            ll_limit.setVisibility(View.GONE);
        }else{
            tv_limit.setText(couponDetail.getData().getCouponBatch().getCbrnu());
            ll_limit.setVisibility(View.VISIBLE);
        }

        if ("1000002" == (couponDetail.getData().getCouponType().getCtid())) {
            ll_cbcf.setVisibility(View.VISIBLE);
            kind.setText("满减券");
            kind.setTextColor(Color.parseColor("#51c7ff"));
            pre.setText("元");
            // pre.setTextSize(16);
            money.setText(couponDetail.getData().getCouponBatch().getCbca());
            // money.setTextSize(30);
            condition.setTextColor(Color.parseColor("#51c7ff"));
            img_left.setBackgroundResource(R.drawable.shape_manjianquan_left);
            img_right_info.setBackgroundResource(R.drawable.shape_manjianquan_right);
            yuan.setText("元");
            cbca.setText(couponDetail.getData().getCouponBatch().getCbca());
            cbcftop.setVisibility(View.VISIBLE);
//            holder.title17.setText("满" + couponDetail.getData().getCouponBatch().getCbnu() + "可用");

        } else if ("1000001" == (couponDetail.getData().getCouponType().getCtid())) {
            ll_cbcf.setVisibility(View.VISIBLE);
            kind.setText("折扣券");
            kind.setTextColor(Color.parseColor("#ef7a99"));
            pre.setText("折");
//            pre.setTextSize(16);
            money.setText(couponDetail.getData().getCouponBatch().getCbrStr());
            //  money.setTextSize(15);
            t4.setText("折扣率");
            yuan.setText("折");
            condition.setTextColor(Color.parseColor("#ef7a99"));
            cbca.setText(couponDetail.getData().getCouponBatch().getCbrStr());
            img_left.setBackgroundResource(R.drawable.shape_zhekouquan_left);
            img_right_info.setBackgroundResource(R.drawable.shape_zhekouquan_right);
            cbcftop.setVisibility(View.VISIBLE);
        } else if ("1000000" == (couponDetail.getData().getCouponType().getCtid())) {
            ll_cbcf.setVisibility(View.GONE);
            kind.setText("代金券");
            kind.setTextColor(Color.parseColor("#99CC00"));
            pre.setText("元");
//            pre.setTextSize(16);
            money.setText(couponDetail.getData().getCouponBatch().getCbca());
            // money.setTextSize(30);
            condition.setTextColor(Color.parseColor("#99CC00"));
            cbca.setText(couponDetail.getData().getCouponBatch().getCbca());
            img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);
            img_right_info.setBackgroundResource(R.drawable.shape_daijinquan_right);
            t4.setText("券面金额");
            yuan.setText("元");
            cbcftop.setVisibility(View.VISIBLE);
            cbcftop.setText("无条件抵用");
        }
        TextView ctn = (TextView) findViewById(R.id.ctn);
        TextView cbn = (TextView) findViewById(R.id.tv_cbn);

        TextView cbcf = (TextView) findViewById(R.id.cbcf);
        TextView cbnu = (TextView) findViewById(R.id.cbnu);
        TextView y_number = (TextView) findViewById(R.id.y_number);
        TextView cbea = (TextView) findViewById(R.id.cbea);
        TextView shopScope = (TextView) findViewById(R.id.tv_shopScope);


        TextView ed_rili_one = (TextView) findViewById(R.id.ed_rili_one);
        TextView ed_rili_two = (TextView) findViewById(R.id.ed_rili_two);

        ctn.setText(couponDetail.getData().getCouponType().getCtn());
        cbn.setText(couponDetail.getData().getCouponBatch().getCbn());

        //shopScope.setText(couponDetail.getData().getCouponBatch());
        cbea.setText(couponDetail.getData().getCouponBatch().getCbea() + "");
        if (couponDetail.getData().getCouponBatch().getCbcf() != null) {
            cbcf.setText(couponDetail.getData().getCouponBatch().getCbcf());
            cbcftop.setText("满" + couponDetail.getData().getCouponBatch().getCbcf() + "元可用");
        }
        cbnu.setText(couponDetail.getData().getCouponBatch().getCbnu());
        y_number.setText(couponDetail.getData().getCouponBatch().getCbnu());
        ed_rili_one.setText(end);
        ed_rili_two.setText(start);

//状态

        Log.i("getCbas", couponDetail.getData().getCouponBatch().getCbas() + "");
        if (status == 0) {
            couponsType.setText("已过期");

        } else {
            if (cbas == 1 || cbas == 2) {
                couponsType.setText("审核中");
            } else if (cbas == 3 || cbas == 4) {
                couponsType.setText("未通过");
            } else {
                if (couponDetail.getData().getCouponBatch().getCbs() == 2 && status == 1) {
                    couponsType.setText("未发布");
                } else if (couponDetail.getData().getCouponBatch().getCbs() == 1 && status == 1) {
                    couponsType.setText("已发布");
                }
            }
        }

        requestDownList();

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
