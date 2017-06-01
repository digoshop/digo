package com.bjut.servicedog.servicedog.ui.analyse;

import android.content.Context;
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
import com.bjut.servicedog.servicedog.po.SaleRecord;
import com.bjut.servicedog.servicedog.po.SaleRecordList;
import com.bjut.servicedog.servicedog.po.SaleStat;
import com.bjut.servicedog.servicedog.rc_adapter.SaleStaticsAnalyseAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
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

public class SaleStatisticsAnalyseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SaleStaticsAnalyseAdapter mAdapter;
    private String max_id = "", min_id = "";
    private TextView salemoney;
    private TextView average;
    private TextView ordertoal;
    private TextView tv_title;
    private String lid = "", userid = "";

    private String mt = "";
    private int loadType = 0;
    private long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_statistics_analyse_activity_f);
        mt = getIntent().getStringExtra("mt");
        init();
        requestlist(max_id, min_id, false);
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        salemoney = (TextView) findViewById(R.id.statis_salemoney);
        average = (TextView) findViewById(R.id.statis_average);
        ordertoal = (TextView) findViewById(R.id.statis_ordertotal);
        tv_title.setText(getString(R.string.title_xstjfx));
//        runkind = (LinearLayout) findViewById(R.id.runkind);
//        runkind.setOnClickListener(this);
//        userkind = (LinearLayout) findViewById(R.id.userkind);
//        userkind.setOnClickListener(this);
//        username = (TextView) findViewById(R.id.username);
//        runname = (TextView) findViewById(R.id.runname);
        //初始化会员种类
//        ListChoseHelp listChoseHelp = new ListChoseHelp();
//        listChoseHelp.setId("");
//        listChoseHelp.setName("全部客户");
//        listChoseHelps.add(listChoseHelp);
//        ListChoseHelp listChoseHelp1 = new ListChoseHelp();
//        listChoseHelp1.setId("0");
//        listChoseHelp1.setName("非会员");
//        listChoseHelps.add(listChoseHelp1);
//        ListChoseHelp listChoseHelp2 = new ListChoseHelp();
//        listChoseHelp2.setId("1");
//        listChoseHelp2.setName("会员");
//        listChoseHelps.add(listChoseHelp2);
//        RunKind runKind1 = new RunKind();
//        runKind1.setName("全部");
//        runKind1.setIsAll(1);
//        runKinds.add(runKind1);
//        requestThirdlist();

//        String sType = pref.getString("shoptype", "");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAdapter = new SaleStaticsAnalyseAdapter();
        mAdapter.setEmptyView(noDataView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAdapter);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.runkind:
//                showRunWindow(v);
//                break;
//            case R.id.userkind:
//                showUserWindow(v);
//                break;
//        }
//    }

    public void setView(SaleStat analyse) {

        total = analyse.getTotal();
        salemoney.setText(analyse.getTotal_amount() + "");
        average.setText(analyse.getAverage() + "");
        ordertoal.setText(analyse.getTotal() + "");
    }

    private void requestlist(String max, String min, final boolean isLoadMore) {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "pay/merchant_get.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("e_type", "");//消费类型 -1 全部 0 消费 1兑换 2竞拍
        map.put("u_type", userid);//用户类型 0普通顾客 1 会员
        map.put("max_id", max);
        map.put("min_id", min);
        map.put("mt", mt);
        map.put("count", "");//条数
        map.put("moid", lid);//品类ID

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
                                max_id = saleRecordList.getData().getMax_id() + "";
                                min_id = saleRecordList.getData().getMin_id() + "";
                                List<SaleRecord> data = saleRecordList.getData().getExpenses();
                                if (!isLoadMore) {
                                    SaleStat stat = saleRecordList.getData().getStat();
                                    setView(stat);
                                }
                                if (!isLoadMore) {
                                    mAdapter.setNewData(data);
                                } else {
                                    mAdapter.addData(data);
                                }
                                if (mAdapter.getData().size() >= total) {
                                    mAdapter.loadMoreEnd(false);
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                if (!isLoadMore) {
                                    salemoney.setText("0");
                                    average.setText("0");
                                    ordertoal.setText("0");
                                    mAdapter.setNewData(new ArrayList<SaleRecord>());
                                } else {
                                    mAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            if (!isLoadMore) {
                                salemoney.setText("0");
                                average.setText("0");
                                ordertoal.setText("0");
                                mAdapter.setNewData(new ArrayList<SaleRecord>());
                            } else {
                                mAdapter.loadMoreEnd(false);
                            }
                            e.printStackTrace();
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
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

//    private void showUserWindow(View parent) {
//        if (popupWindow == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = layoutInflater.inflate(R.layout.group_viplevel, null);
//            lv_group = (ListView) view.findViewById(R.id.lvGroup);
//            ListChoseAdapter groupAdapter = new ListChoseAdapter(this, listChoseHelps);
//            lv_group.setAdapter(groupAdapter);
//            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, dip2px(this, 135));
//        }
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
//        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//               // username.setText(listChoseHelps.get(position).getName());
//                userid = listChoseHelps.get(position).getId() + "";
//                mData.clear();
//                max_id = "";
//                min_id = "";
//                requestlist("", "");
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//    }
//
//    private void showRunWindow(View parent) {
//        if (popupWindow2 == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view2 = layoutInflater.inflate(R.layout.group_viplevel, null);
//            lv_group2 = (ListView) view2.findViewById(R.id.lvGroup);
//            GroupRunListAdapter groupRunListAdapter = new GroupRunListAdapter(this, runKinds);
//            lv_group2.setAdapter(groupRunListAdapter);
//            popupWindow2 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, dip2px(this, LinearLayout.LayoutParams.WRAP_CONTENT));
//        }
//        popupWindow2.setFocusable(true);
//        popupWindow2.setOutsideTouchable(true);
//        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow2.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
//        lv_group2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                runname.setText(runKinds.get(position).getName());
//                if (runKinds.get(position).getIsAll() == 1) {
//                    lid = "";
//                } else {
//                    lid = runKinds.get(position).getMoid() + "";
//                }
//                mData.clear();
//                max_id = "";
//                min_id = "";
//                requestlist("", "");
//                if (popupWindow2 != null) {
//                    popupWindow2.dismiss();
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

//    private void requestThirdlist() {
//        showProgressDialog();
//        String urlString = "business/shop_cate.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));
//       String sType=pref.getString("shoptype", "");
//        if(sType!=null){
//            if(sType.equals("2")){
//                map.put("level", "2");
//            }else {
//                map.put("level", "3");//优惠券批次ID必选
//            }
//        }
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println("rrrrrr"+responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        String json = responseInfo.result;
//                        try {
//                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
//
//                            runKinds.addAll(runKindList.getData());
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                closeProgressDialog();
//                toast(Constant.CHECK_NETWORK);
//            }
//        });
//    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        loadType = 1;
        requestlist("", "", false);
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        requestlist(min_id, "", true);
    }
}
