package com.bjut.servicedog.servicedog.ui.vip;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.bjut.servicedog.servicedog.po.Attention;
import com.bjut.servicedog.servicedog.po.AttentionList;
import com.bjut.servicedog.servicedog.rc_adapter.AttentionUserAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.PopupMenu;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttentionUserActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String max_id = "", min_id = "", type = "";
    private LinearLayout linear_time_search, linear_condition;
    private PopupMenu popupMenu, popupMenu2;
    private String stime = "", etime = "", lid = "";
    private TextView timename, condition, vip_number;
    private Calendar ca;
    private CalendarPickerView dialogView;
    private AlertDialog theDialog;
    private TextView tv_title;

    private AttentionUserAdapter mAttentionUserAdapter;
    private int loadType = 0;
    private long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_user);
        init();
        requestAttentionList("", "");
        setListener();
    }

    private void setListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.fuck:
                if (mAttentionUserAdapter.getItem(position).getInvite() == 0) {
                    requestInvite(mAttentionUserAdapter.getItem(position).getUid(), position);
                } else if (mAttentionUserAdapter.getItem(position).getInvite() == 1) {
                    toast("已邀请状态");
                } else if (mAttentionUserAdapter.getItem(position).getInvite() == 2) {
                    toast("已是会员");
                }
                break;
                }
            }
        });
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ca = Calendar.getInstance();
        popupMenu = new PopupMenu(this);
        popupMenu2 = new PopupMenu(this, "全部关注", "未邀请", "已邀请");

        vip_number = (TextView) findViewById(R.id.vip_number);

        timename = (TextView) findViewById(R.id.timename);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_gzyh));
        condition = (TextView) findViewById(R.id.condition);
        linear_time_search = (LinearLayout) findViewById(R.id.linear_time_search);
        linear_condition = (LinearLayout) findViewById(R.id.linear_condition);
        linear_condition.setOnClickListener(this);
        linear_time_search.setOnClickListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAttentionUserAdapter = new AttentionUserAdapter();
        mAttentionUserAdapter.setEmptyView(noDataView);
        mAttentionUserAdapter.setEnableLoadMore(true);
        mAttentionUserAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAttentionUserAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAttentionUserAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_time_search:
                popupMenu.showLocation(R.id.linear_time_search);// 设置弹出菜单弹出的位置
                popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
                    @Override
                    public void onClick(PopupMenu.MENUITEM item) {
                        switch (item) {
                            case ITEM1:
                                timename.setText("全部时间");
                                stime = "";
                                etime = "";
                                requestAttentionList("", "");
                                break;
                            case ITEM2:
                                timename.setText("近一个月");
                                try {
                                    etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
                                    stime = DateUtil.currentDateParserLongYMD() / 1000 - 3600 * 24 * 30 + "";

                                    requestAttentionList("", "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case ITEM3:
                                timename.setText("近三个月");
                                try {
                                    etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
                                    stime = DateUtil.currentDateParserLongYMD() / 1000 - 3600 * 24 * 90 + "";
                                    requestAttentionList("", "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case ITEM4:
                                showCalendarInDialog();
                                break;
                        }

                    }
                });
                break;
            case R.id.linear_condition:
                popupMenu2.showLocation(R.id.linear_condition);// 设置弹出菜单弹出的位置
                popupMenu2.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
                    @Override
                    public void onClick(PopupMenu.MENUITEM item) {
                        switch (item) {
                            case ITEM1:
                                condition.setText("全部关注");
                                type = "";
                                requestAttentionList("", "");
                                break;
                            case ITEM2:
                                condition.setText("未邀请");
                                type = "0";
                                requestAttentionList("", "");
                                break;
                            case ITEM3:
                                condition.setText("已邀请");
                                type = "1";
                                requestAttentionList("", "");
                                break;
                        }

                    }
                });

                break;
        }
    }

    public void requestInvite(String uid, final int position) {
        showProgressDialog();
        String urlString = "relation/invite_user.json";
        urlString = String.format(urlString);
        Log.i("out", Constant.HOST_URL + urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("uid", uid);

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
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("邀请成功");
                                Attention item = mAttentionUserAdapter.getItem(position);
                                item.setInvite(1);
//                                mAttentionUserAdapter.notifyItemChanged(position, item);
                            } else {
                                toast("邀请失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            mAttentionUserAdapter.notifyDataSetChanged();
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

    public void requestAttentionList(final String max, final String min) {

        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "relation/merchant_get.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("tid", pref.getString("sid", ""));
        map.put("stime", stime);
        map.put("etime", etime);
        map.put("max_id", max);
        map.put("min_id", min);
//        map.put("count", Constant.PAGE_SIZE + "");
        map.put("type", type);

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
                            AttentionList attentionList = JSON.parseObject(json, AttentionList.class);
                            if (attentionList.getE().getCode() == 0) {
                                if (StringUtils.isEmpty(max)) {
                                    total = attentionList.getTotal();
                                    vip_number.setText(total + "");
                                    max_id = attentionList.getData().getMax_id() + "";
                                    min_id = attentionList.getData().getMin_id() + "";
                                }
                                List<Attention> relations = attentionList.getData().getRelations();
                                if (relations.size() > 0) {
                                    if (StringUtils.isEmpty(max)) {
                                        mAttentionUserAdapter.setNewData(relations);
                                    } else {
                                        mAttentionUserAdapter.addData(relations);
                                    }
                                    if (mAttentionUserAdapter.getData().size() >= total) {
                                        mAttentionUserAdapter.loadMoreEnd(false);
                                    } else {
                                        mAttentionUserAdapter.loadMoreComplete();
                                    }
                                } else {
                                    if (StringUtils.isEmpty(max)) {
                                        mAttentionUserAdapter.setNewData(new ArrayList<Attention>());
                                    } else {
                                        mAttentionUserAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                if (StringUtils.isEmpty(max)) {
                                    vip_number.setText("0");
                                    mAttentionUserAdapter.setNewData(new ArrayList<Attention>());
                                } else {
                                    mAttentionUserAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            if (StringUtils.isEmpty(max)) {
                                vip_number.setText("0");
                                mAttentionUserAdapter.setNewData(new ArrayList<Attention>());
                            } else {
                                mAttentionUserAdapter.loadMoreEnd(false);
                            }
                            e.printStackTrace();
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mAttentionUserAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mAttentionUserAdapter.loadMoreFail();
            }
        });
    }

    protected void showCalendarInDialog() {

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.dialog_time, null, false);
        theDialog = new AlertDialog.Builder(this) //
                .setTitle("全部时间")
                .setView(dialogView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Date> dates = dialogView.getSelectedDates();
                        long firstdate = dates.get(0).getTime() / 1000;
                        long lastdate = dates.get(dates.size() - 1).getTime() / 1000;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String startime = format.format(dates.get(0).getTime());
                        String endtime = format.format(dates.get(dates.size() - 1).getTime());
                        stime = firstdate + "";
                        etime = lastdate + "";
                        timename.setText(startime + "至" + endtime);
                        timename.setTextSize(12);
                        dialogInterface.dismiss();
                        requestAttentionList("", "");
                    }
                })

                .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialogView.fixDialogDimens();
            }
        });
        theDialog.setCanceledOnTouchOutside(true);
        theDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        theDialog.show();

        dialogView.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDate(new Date());

    }

    @Override
    public void onRefresh() {
        loadType = 1;
        requestAttentionList("", "");
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        requestAttentionList(min_id, "");
    }

}
