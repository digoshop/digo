package com.bjut.servicedog.servicedog.ui.settlement;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ListChoseAdapter;
import com.bjut.servicedog.servicedog.po.ListChoseHelp;
import com.bjut.servicedog.servicedog.po.SaleRecord;
import com.bjut.servicedog.servicedog.po.SaleRecordList;
import com.bjut.servicedog.servicedog.rc_adapter.SettlementRecordAdapter;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

public class SettlementRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SettlementRecordAdapter mSettlementRecordAdapter;

    private String max_id = "", min_id = "";
    private AutofitTextView total_amount, total, average;
    private LinearLayout runkind, time, userkind;
    private TextView username, timename, runname;

    private PopupWindow popupWindow, popupWindow2;
    private View view, view2;
    private ListView lv_group, lv_group2;
    private List<ListChoseHelp> listChoseHelps = new ArrayList<>();
    private List<ListChoseHelp> timeListHelps = new ArrayList<>();
    private List<ListChoseHelp> listChoseHelps2 = new ArrayList<>();
    private TextView tv_title;

    private String stime = "", etime = "", lid = "-1", userid = "-1";

    private int dimens270 = 0;
    private int dimens360 = 0;
    private long total_num = 0;

    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_record);
        init();
        requestSaleRecordList(max_id, min_id, false);
    }

    public void init() {

        dimens270 = getResources().getDimensionPixelOffset(R.dimen.base_dimen_270);
        dimens360 = getResources().getDimensionPixelOffset(R.dimen.base_dimen_360);
        try {
            stime = DateUtil.currentDateParserLongYMD() / 1000 + "";
            etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_jsjl));

        runkind = (LinearLayout) findViewById(R.id.runkind);
        runkind.setOnClickListener(this);
        time = (LinearLayout) findViewById(R.id.time);
        time.setOnClickListener(this);
        userkind = (LinearLayout) findViewById(R.id.userkind);
        userkind.setOnClickListener(this);
        username = (TextView) findViewById(R.id.username);
        timename = (TextView) findViewById(R.id.timename);
        runname = (TextView) findViewById(R.id.runname);
        //初始化会员种类
        ListChoseHelp l1 = new ListChoseHelp();
        l1.setId("-1");
        l1.setName("全部时间");
        timeListHelps.add(l1);
        ListChoseHelp l2 = new ListChoseHelp();
        l2.setId("1");
        l2.setName("当日记录");
        timeListHelps.add(l2);
        ListChoseHelp l3 = new ListChoseHelp();
        l3.setId("0");
        l3.setName("近一个月");
        timeListHelps.add(l3);

        ListChoseHelp listChoseHelp = new ListChoseHelp();
        listChoseHelp.setId("-1");
        listChoseHelp.setName("全部客户");
        listChoseHelps.add(listChoseHelp);
        ListChoseHelp listChoseHelp1 = new ListChoseHelp();
        listChoseHelp1.setId("1");
        listChoseHelp1.setName("会员客户");
        listChoseHelps.add(listChoseHelp1);
        ListChoseHelp listChoseHelp2 = new ListChoseHelp();
        listChoseHelp2.setId("0");
        listChoseHelp2.setName("非会员客户");
        listChoseHelps.add(listChoseHelp2);

        ListChoseHelp listChoseHelp3 = new ListChoseHelp();
        listChoseHelp3.setId("-1");
        listChoseHelp3.setName("全部结算");
        listChoseHelps2.add(listChoseHelp3);
        ListChoseHelp listChoseHelp4 = new ListChoseHelp();
        listChoseHelp4.setId("1");
        listChoseHelp4.setName("销售结算");
        listChoseHelps2.add(listChoseHelp4);
        ListChoseHelp listChoseHelp5 = new ListChoseHelp();
        listChoseHelp5.setId("2");
        listChoseHelp5.setName("认购结算");
        listChoseHelps2.add(listChoseHelp5);
        ListChoseHelp listChoseHelp6 = new ListChoseHelp();
        listChoseHelp6.setId("4");
        listChoseHelp6.setName("签约折扣");
        listChoseHelps2.add(listChoseHelp6);

        total_amount = (AutofitTextView) findViewById(R.id.total_amount);
        total = (AutofitTextView) findViewById(R.id.total);
        average = (AutofitTextView) findViewById(R.id.average);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mSettlementRecordAdapter = new SettlementRecordAdapter();
        mSettlementRecordAdapter.setEmptyView(noDataView);
        mSettlementRecordAdapter.setEnableLoadMore(true);
        mSettlementRecordAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mSettlementRecordAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mSettlementRecordAdapter);

//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                mData.clear();
//                requestSaleRecordList("", "");
//                new FinishRefresh().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                requestSaleRecordList(min_id, "");
//                new FinishRefresh().execute();
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.runkind:
                showRunWindow(v);
                break;
            case R.id.userkind:
                showUserWindow(v);
                break;
            case R.id.time:
                showTimeWindow(v);
                break;
        }
    }

    public void requestSaleRecordList(String max, String min, final boolean isLoadMore) {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "pay/merchant_get.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        final Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("start", stime);
        map.put("end", etime);
        map.put("e_type", lid);// -1 全部 0 消费 1兑换 2竞拍
        map.put("u_type", userid);// 0普通顾客 1 会员
        map.put("moid", "");//品类id,非必须
        map.put("max_id", max);
        map.put("min_id", min);
//        map.put("count","3");

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
                            SaleRecordList saleRecordList = JSON.parseObject(json, SaleRecordList.class);
                            if (saleRecordList.getE().getCode() == 0) {
                                if (!isLoadMore) {
//                                    NumAnim.startAnim(total_amount, saleRecordList.getData().getStat().getTotal_amount());
//                                    NumAnim.startAnim(average, saleRecordList.getData().getStat().getAverage());
                                    total_amount.setText(saleRecordList.getData().getStat().getTotal_amount());
                                    average.setText(saleRecordList.getData().getStat().getAverage());
                                    total_num =saleRecordList.getData().getStat().getTotal();
                                    total.setText(total_num + "");
                                }
//                                average.setText(saleRecordList.getData().getStat().getAverage() + "");
                                List<SaleRecord> expenses = saleRecordList.getData().getExpenses();
                                max_id = saleRecordList.getData().getMax_id() + "";
                                min_id = saleRecordList.getData().getMin_id() + "";
                                if (expenses.size() > 0) {
                                    if (!isLoadMore) {
                                        mSettlementRecordAdapter.setNewData(expenses);
                                    } else {
                                        mSettlementRecordAdapter.addData(expenses);
                                    }
                                    if (mSettlementRecordAdapter.getData().size() >= total_num) {
                                        mSettlementRecordAdapter.loadMoreEnd(false);
                                    } else {
                                        mSettlementRecordAdapter.loadMoreComplete();
                                    }
                                } else {
                                    mSettlementRecordAdapter.loadMoreEnd(false);
                                }
                            } else {
                                if ("".equals(max_id)) {
                                    total_amount.setText("0");
                                    total.setText("0");
                                    average.setText("0");
                                    mSettlementRecordAdapter.setNewData(new ArrayList<SaleRecord>());
                                }else{
                                    mSettlementRecordAdapter.loadMoreEnd(false);
                                }
//                                toast(saleRecordList.getE().getDesc());
                            }
                        } catch (Exception e) {
                            if ("".equals(max_id)) {
                                total_amount.setText("0");
                                total.setText("0");
                                average.setText("0");
                                mSettlementRecordAdapter.setNewData(new ArrayList<SaleRecord>());
                            } else {
                                mSettlementRecordAdapter.loadMoreEnd(false);
                            }
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mSettlementRecordAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mSettlementRecordAdapter.loadMoreFail();
            }
        });
    }

    private void showTimeWindow(View parent) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.group_viplevel, null);
        lv_group = (ListView) view.findViewById(R.id.lvGroup);
        ListChoseAdapter groupAdapter = new ListChoseAdapter(this, timeListHelps);
        lv_group.setAdapter(groupAdapter);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, dimens270);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        stime = "";
                        etime = "";
                        break;
                    case 1:
                        try {
                            stime = DateUtil.currentDateParserLongYMD() / 1000 + "";
                            etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
                            stime = DateUtil.currentDateParserLongYMD() / 1000 - 3600 * 24 * 30 + "";

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                timename.setText(timeListHelps.get(position).getName());
                max_id = "";
                min_id = "";
                requestSaleRecordList(max_id, min_id, false);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void showUserWindow(View parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.group_viplevel, null);
        lv_group = (ListView) view.findViewById(R.id.lvGroup);
        ListChoseAdapter groupAdapter = new ListChoseAdapter(this, listChoseHelps);
        lv_group.setAdapter(groupAdapter);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, dimens270);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                username.setText(listChoseHelps.get(position).getName());
                userid = listChoseHelps.get(position).getId() + "";
                max_id = "";
                min_id = "";
                requestSaleRecordList(max_id, min_id, false);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void showRunWindow(View parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view2 = layoutInflater.inflate(R.layout.group_viplevel, null);
        lv_group2 = (ListView) view2.findViewById(R.id.lvGroup);
        ListChoseAdapter groupAdapter = new ListChoseAdapter(this, listChoseHelps2);
        lv_group2.setAdapter(groupAdapter);
        popupWindow2 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, dimens360);
        popupWindow2.setFocusable(true);
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
        popupWindow2.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
        lv_group2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                runname.setText(listChoseHelps2.get(position).getName());
                lid = listChoseHelps2.get(position).getId() + "";
                max_id = "";
                min_id = "";
                requestSaleRecordList(max_id, min_id, false);
                if (popupWindow2 != null) {
                    popupWindow2.dismiss();
                }
            }
        });
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void onRefresh() {
        max_id = "";
        min_id = "";
        loadType = 1;
        requestSaleRecordList(max_id, min_id, false);
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        requestSaleRecordList(min_id, "", true);
    }

}
