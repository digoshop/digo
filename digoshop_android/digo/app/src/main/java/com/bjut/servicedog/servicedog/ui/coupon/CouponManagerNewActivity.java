package com.bjut.servicedog.servicedog.ui.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.po.Coupon;
import com.bjut.servicedog.servicedog.po.CouponList;
import com.bjut.servicedog.servicedog.rc_adapter.CouponNewAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.My2BDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/5/23.
 */

public class CouponManagerNewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    //标题
    private TextView tv_title;
    //优惠券状态
    private TextView left;
    private TextView middle;
    private TextView right;
    //优惠券类别
    private TextView kindall;
    private TextView kind1;
    private TextView kind2;
    private TextView kind3;
    //创建
    private Button btn_right;
    //列表
    private CouponNewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Coupon> mData = new ArrayList<>();

    private int page = 1;
    private String type = Constant.COUPON_ALL;
    private String status = Constant.COUPON_STATUS_NO;
    private int statusIndex = 0;
    private int typeIndex = 0;
    private int loadType = 0;


    private int typecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        EventBus.getDefault().register(this);
        initViews();
        setListener();
        requestCouponList(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(OnRefreshEvent onRefreshEvent) {
        if (onRefreshEvent.isRefresh()) {
            page = 1;
            requestCouponList(page);
        }
    }

    private void setListener() {
        btn_right.setOnClickListener(this);
        left.setOnClickListener(this);
        middle.setOnClickListener(this);
        right.setOnClickListener(this);
        kindall.setOnClickListener(this);
        kind1.setOnClickListener(this);
        kind2.setOnClickListener(this);
        kind3.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isNotFastClick()) {
                    Coupon item = mAdapter.getItem(position);
                    Intent auditIntent = new Intent(mContext, CouponDetailActivity.class);
                    String rule = String.valueOf(item.getCouponBatch().getRule());
                    int stus = item.getCouponBatch().getStatus();
                    int cbas = item.getCouponBatch().getCbas();
                    int couponBatchId = item.getCouponBatch().getCbid();
                    Log.i(TAG, "couponBatchId===========点击:" + couponBatchId);
                    auditIntent.putExtra("couponBatchId", couponBatchId + "");
                    auditIntent.putExtra("auditType", status);
                    auditIntent.putExtra("rule", rule);
                    auditIntent.putExtra("status", stus);
                    auditIntent.putExtra("cbas", cbas);
                    auditIntent.putExtra("couponimage", item.getCouponBatch().getCbi() + "");
                    startActivity(auditIntent);
//                    if (1 == item.getCouponBatch().getCbas() || 2 == item.getCouponBatch().getCbas()) {
//                        auditIntent.putExtra("rule", rule);
//                        auditIntent.putExtra("status", stus);
//                        auditIntent.putExtra("cbas", cbas);
//                        auditIntent.putExtra("couponimage", item.getCouponBatch().getCbi() + "");
//                        startActivity(auditIntent);
//                    } else {
//                        auditIntent.putExtra("rule", rule);
//                        auditIntent.putExtra("status", stus);
//                        auditIntent.putExtra("cbas", cbas);
//                        auditIntent.putExtra("couponimage", item.getCouponBatch().getCbi() + "");
//                        startActivity(auditIntent);
//                    }
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.coupon_stop:
                        if (mAdapter.getItem(position).getCouponBatch().getCbs() == 1) {
                            typecode = 2;//启用
                            coupon(position);
                        } else if (mAdapter.getItem(position).getCouponBatch().getCbs() == 2) {
                            typecode = 1;//禁用
                            coupon(position);
                        }
                        break;
                    case R.id.coupon_details:
                        Intent intent = new Intent(mContext, CouponStatisticsActivity.class);
                        intent.putExtra("bid", mAdapter.getItem(position).getCouponBatch().getCbid() + "");
                        startActivity(intent);
                        break;
                    case R.id.pen:
                        if (mAdapter.getItem(position).getCouponBatch().getStatus() == 0) {
                            toast("优惠券已经过期,请更新有效日期");
                        }
                        Intent updateIntent = new Intent(mContext, UpdateCouponActivity.class);
                        updateIntent.putExtra("couponBatchId",mAdapter.getItem(position).getCouponBatch().getCbid() + "");
                        if (3 == mAdapter.getData().get(position).getCouponBatch().getCbas() || 4 == mAdapter.getData().get(position).getCouponBatch().getCbas()) {
                            updateIntent.putExtra("cbafr", mAdapter.getData().get(position).getCouponBatch().getCbafr());
                        }
                        startActivity(updateIntent);
//                        jundgeStatus(position);

                        break;
                    case R.id.delete:
                        showAlertDialog(mData, position);
                        break;
                }
            }
        });
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        left = (TextView) findViewById(R.id.left);
        middle = (TextView) findViewById(R.id.middle);
        right = (TextView) findViewById(R.id.right);
        kindall = (TextView) findViewById(R.id.kindall);
        kind1 = (TextView) findViewById(R.id.kind1);
        kind2 = (TextView) findViewById(R.id.kind2);
        kind3 = (TextView) findViewById(R.id.kind3);
        btn_right = (Button) findViewById(R.id.btn_right);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        tv_title.setText(getString(R.string.title_yhqgl));
        btn_right.setText(getString(R.string.btn_xz));
        btn_right.setVisibility(View.VISIBLE);

//        changeView(statusIndex, typeIndex);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAdapter = new CouponNewAdapter(mData);
        mAdapter.setEmptyView(noDataView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this);
//        mAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int getViewId() {
        return R.layout.activity_coupon_manager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                loadType = 0;
                page = 1;
                status = Constant.COUPON_STATUS_NO;
                statusIndex = 0;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.middle:
                loadType = 0;
                page = 1;
                status = Constant.COUPON_STATUS_YES;
                statusIndex = 1;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.right:
                loadType = 0;
                page = 1;
                status = Constant.COUPON_STATUS_END;
                statusIndex = 2;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.kindall:
                loadType = 0;
                page = 1;
                type = Constant.COUPON_ALL;
                typeIndex = 0;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.kind1:
                loadType = 0;
                page = 1;
                type = Constant.COUPON_MAN;
                typeIndex = 1;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.kind2:
                loadType = 0;
                page = 1;
                type = Constant.COUPON_ZHE;
                typeIndex = 2;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.kind3:
                loadType = 0;
                page = 1;
                type = Constant.COUPON_DAI;
                typeIndex = 3;
                changeView(statusIndex, typeIndex);
                requestCouponList(page);
                break;
            case R.id.btn_right:
                myIntentR(CreateCouponActivity.class);
                break;
        }
    }

    public void changeView(int a, int b) {
        switch (a) {
            case 0:
                left.setBackgroundResource(R.drawable.switch_button_left_checked);
                middle.setBackgroundResource(R.drawable.switch_button_middle);
                right.setBackgroundResource(R.drawable.switch_button_right);

                left.setTextColor(getResources().getColor(R.color.white));
                middle.setTextColor(getResources().getColor(R.color.orange));
                right.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 1:
                left.setBackgroundResource(R.drawable.switch_button_left);
                middle.setBackgroundResource(R.drawable.switch_button_middle_checked);
                right.setBackgroundResource(R.drawable.switch_button_right);

                left.setTextColor(getResources().getColor(R.color.orange));
                middle.setTextColor(getResources().getColor(R.color.white));
                right.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 2:
                left.setBackgroundResource(R.drawable.switch_button_left);
                middle.setBackgroundResource(R.drawable.switch_button_middle);
                right.setBackgroundResource(R.drawable.switch_button_right_checked);

                left.setTextColor(getResources().getColor(R.color.orange));
                middle.setTextColor(getResources().getColor(R.color.orange));
                right.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        switch (b) {
            case 0:
                kindall.setBackgroundResource(R.drawable.border_orange_radius20dp);
                kind1.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind2.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind3.setBackgroundResource(R.drawable.border_black_radius20dp);

                kindall.setTextColor(getResources().getColor(R.color.orange));
                kind1.setTextColor(getResources().getColor(R.color.word_color));
                kind2.setTextColor(getResources().getColor(R.color.word_color));
                kind3.setTextColor(getResources().getColor(R.color.word_color));
                break;
            case 1:
                kindall.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind1.setBackgroundResource(R.drawable.border_orange_radius20dp);
                kind2.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind3.setBackgroundResource(R.drawable.border_black_radius20dp);

                kindall.setTextColor(getResources().getColor(R.color.word_color));
                kind1.setTextColor(getResources().getColor(R.color.orange));
                kind2.setTextColor(getResources().getColor(R.color.word_color));
                kind3.setTextColor(getResources().getColor(R.color.word_color));
                break;
            case 2:
                kindall.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind1.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind2.setBackgroundResource(R.drawable.border_orange_radius20dp);
                kind3.setBackgroundResource(R.drawable.border_black_radius20dp);

                kindall.setTextColor(getResources().getColor(R.color.word_color));
                kind1.setTextColor(getResources().getColor(R.color.word_color));
                kind2.setTextColor(getResources().getColor(R.color.orange));
                kind3.setTextColor(getResources().getColor(R.color.word_color));
                break;
            case 3:
                kindall.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind1.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind2.setBackgroundResource(R.drawable.border_black_radius20dp);
                kind3.setBackgroundResource(R.drawable.border_orange_radius20dp);

                kindall.setTextColor(getResources().getColor(R.color.word_color));
                kind1.setTextColor(getResources().getColor(R.color.word_color));
                kind2.setTextColor(getResources().getColor(R.color.word_color));
                kind3.setTextColor(getResources().getColor(R.color.orange));
                break;
        }
    }

    private void jundgeStatus(int position) {
        Coupon item = mAdapter.getItem(position);
        Intent intent = new Intent(this, AddDiscountCouponManageActivity_f.class);
        String[] categorys = item.getCouponBatch().getCategorys();
        intent.putExtra("categorys", categorys);
        String couponid = item.getCouponBatch().getCbid() + "";
        String coupontypeid = item.getCouponType().getCtid() + "";
        intent.putExtra("couponedit", 1);
        intent.putExtra("couponId", couponid);
        intent.putExtra("coupontypeId", coupontypeid);
        String str1, str2;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(item.getCouponBatch().getCbvsd() * 1000);
        Date date1 = new Date(item.getCouponBatch().getCbved() * 1000);
        str1 = sdf.format(date);
        str2 = sdf.format(date1);
        intent.putExtra("rule", item.getCouponBatch().getRule());
        intent.putExtra("couponstarttime", str1);
        intent.putExtra("couponendtime", str2);
        intent.putExtra("couponnumber", item.getCouponBatch().getCbnu() + "");
        intent.putExtra("couponcategory", item.getCouponBatch().getCategoryStr() + "");
        intent.putExtra("couponkind", item.getCouponType().getCtn() + "");
        intent.putExtra("couponcondition", item.getCouponBatch().getCbcf() + "");
        intent.putExtra("couponimage", item.getCouponBatch().getCbi() + "");
        intent.putExtra("coupontopic", item.getCouponBatch().getCbn() + "");
        intent.putExtra("couponvalue", item.getCouponBatch().getCbca() + "");
        intent.putExtra("coupondesc", item.getCouponBatch().getCbd() + "");
        intent.putExtra("couponexchange", item.getCouponBatch().getCbea() + "");
        intent.putExtra("cbrm", item.getCouponBatch().getCbrm());
        intent.putExtra("cbrnu", item.getCouponBatch().getCbrnu() + "");

        if (coupontypeid.equals("1000001")) {
            intent.putExtra("coupondiscount", item.getCouponBatch().getCbrStr());
        }

        if (3 == item.getCouponBatch().getCbas() || 4 == item.getCouponBatch().getCbas()) {
            intent.putExtra("cbafr", item.getCouponBatch().getCbafr());
        }

        startActivity(intent);

    }

    public void showAlertDialog(final List<?> data, final int position) {
        if (mMyDialog == null) {
            mMyDialog = new My2BDialog(mContext);
        }
        mMyDialog.setTitle("温馨提示");
        mMyDialog.setMessage("确定要删除吗？");
        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                typecode = 99;
                coupon(position);
                mMyDialog.dismiss();
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

    public void requestCouponList(final int page) {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "coupon/query_coupons.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("sid", 1000005 + "");
        map.put("sid", pref.getString("sid", ""));
        map.put("typeId", type + "");
        map.put("status", status + "");
        map.put("page", page + "");
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
                    CouponList couponList = JSON.parseObject(json, CouponList.class);
                    if (couponList.getE().getCode() == 0) {
                        List<Coupon> data = couponList.getData();
                        if (data.size() > 0) {
                            if (page == 1) {
                                mAdapter.setNewData(data);
                            } else {
                                mAdapter.addData(data);
                            }
                            if (data.size() < Constant.PAGE_SIZE) {
                                mAdapter.loadMoreEnd(false);
                            } else {
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                        }

                    } else {
                        toast(couponList.getE().getDesc());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (page == 1) {
                        mAdapter.setNewData(new ArrayList<Coupon>());
                    } else {
                        mAdapter.loadMoreEnd(false);
                    }
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

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.loadMoreFail();
            }
        });
    }

    /**
     * 1启用  2禁用  99删除
     */
    private void coupon(final int position) {
        String urlString = "coupon/update_status.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("cid", mAdapter.getItem(position).getCouponBatch().getCbid() + "");//优惠券ID
        map.put("status", typecode + "");//删除是99,2为禁用
        map.put("sid", pref.getString("sid", ""));
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

                                if (typecode == 1) {
                                    toast("已启用!");
                                    mAdapter.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                } else if (typecode == 2) {
                                    toast("已禁用!");
                                    mAdapter.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                } else if (typecode == 99) {
                                    toast("已删除!");
                                    mAdapter.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("desc"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.loadMoreFail();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        page = 1;
        requestCouponList(page);
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestCouponList(page);
    }
}
