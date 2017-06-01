package com.bjut.servicedog.servicedog.ui.vip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.GroupVipLevelListAdapter;
import com.bjut.servicedog.servicedog.po.VipInfo;
import com.bjut.servicedog.servicedog.po.VipLevel;
import com.bjut.servicedog.servicedog.po.VipLevelList;
import com.bjut.servicedog.servicedog.po.VipList;
import com.bjut.servicedog.servicedog.po.VipSearch;
import com.bjut.servicedog.servicedog.rc_adapter.VipAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.PopupMenu;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjut.servicedog.servicedog.R.string.title_gzyh;

public class VipManageActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private TextView tv_title, levelname;
    private Button btn_right;
    private TextView add_new_vip, timename, tv_search, vip_number;
    private List<VipLevel> vipLevels = new ArrayList<>();
    private int page = 1;
    private LinearLayout linear_time_search, linear_vip_class;
    private PopupMenu popupMenu;
    private String stime = "", etime = "", lid = "0";
    private CalendarPickerView dialogView;
    private AlertDialog theDialog;
    private Calendar ca;
    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private EditText et_search;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private VipAdapter mVipAdapter;
    private View mHeaderView;
    private int dimens270 = 0;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_manage);
        init();
        setListener();
    }

    private void setListener() {

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, VipChangeInfoActivity.class);
                intent.putExtra("vipId", mVipAdapter.getItem(position).getVip_id() + "");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        page = 1;
        requestVipLevelList();
    }


    private void init() {
        dimens270 = getResources().getDimensionPixelOffset(R.dimen.base_dimen_270);

        mHeaderView = getLayoutInflater().inflate(R.layout.header_vip_list, null);
        et_search = (EditText) mHeaderView.findViewById(R.id.et_search);
        tv_search = (TextView) mHeaderView.findViewById(R.id.tv_search);
        vip_number = (TextView) findViewById(R.id.vip_number);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        popupMenu = new PopupMenu(this);


        levelname = (TextView) findViewById(R.id.levelname);
        timename = (TextView) findViewById(R.id.timename);
        ca = Calendar.getInstance();
        tv_search.setOnClickListener(this);


        linear_time_search = (LinearLayout) findViewById(R.id.linear_time_search);
        linear_vip_class = (LinearLayout) findViewById(R.id.linear_vip_class);
        linear_vip_class.setOnClickListener(this);
        linear_time_search.setOnClickListener(this);


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_hygl));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(title_gzyh));
        btn_right.setVisibility(View.VISIBLE);
        add_new_vip = (TextView) findViewById(R.id.add_new_vip);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vipLevels.size() > 1) {
                    myIntentR(AttentionUserActivity.class);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VipManageActivity.this);
                    builder.setTitle("您尚未设置会员级别，请先设置会员级别！");
                    builder.setCancelable(false);
                    builder.setMessage("是否设置?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myIntentR(VipClassConfigActivity.class);

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }
        });
        add_new_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (vipLevels.size() > 1) {
                    myIntentR(AddNewVipActivity.class);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VipManageActivity.this);
                    builder.setTitle("您尚未设置会员级别，请先设置会员级别！");
                    builder.setCancelable(false);
                    builder.setMessage("是否设置?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myIntentR(VipClassConfigActivity.class);

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mVipAdapter = new VipAdapter();
        mVipAdapter.setEmptyView(noDataView);
        mVipAdapter.setHeaderView(mHeaderView);
        mVipAdapter.setEnableLoadMore(true);
        mVipAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mVipAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mVipAdapter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    page = 1;
                    requestVipList();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                if (et_search.getText().toString().equals("")) {
                    toast("请输入手机号");
                } else {
                    requestSearchVip();
                }
                break;
            case R.id.linear_vip_class:
                showWindow(v);
                break;
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
                                page = 1;
                                requestVipList();
                                break;
                            case ITEM2:
                                timename.setText("近一个月");
                                etime = ca.getTime().getTime() / 1000 + "";
                                long lastMonth = ca.getTime().getTime() / 1000 - 3600 * 24 * 30L;
                                stime = lastMonth + "";
                                page = 1;
                                requestVipList();
                                break;
                            case ITEM3:
                                timename.setText("近三个月");
                                etime = ca.getTime().getTime() / 1000 + "";
                                stime = (ca.getTime().getTime() / 1000 - 3600 * 90 * 24L) + "";
                                page = 1;
                                requestVipList();
                                break;
                            case ITEM4:
                                showCalendarInDialog();
                                break;
                        }

                    }
                });
                break;
        }
    }

    private void requestVipList() {
        if (page==0) {
            showProgressDialog();
        }
        String urlString = "relation/merchant_vips.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("page", page + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
        if (lid.equals("0")) {
            map.put("lid", "");
        } else {
            map.put("lid", lid);
        }
        map.put("stime", stime);
        map.put("etime", etime);
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
                            VipList vipList = JSON.parseObject(json, VipList.class);
                            if (page == 1) {
                                vip_number.setText(vipList.getTotal() + "");
                            }
                            if (vipList.getE().getCode() == 0) {
                                List<VipInfo> data = vipList.getData();// 填充数据
                                if (page == 1) {
                                    mVipAdapter.setNewData(data);
                                } else {
                                    mVipAdapter.addData(data);
                                }
                                if (data.size() < Constant.PAGE_SIZE) {
                                    mVipAdapter.loadMoreEnd(false);
                                } else {
                                    mVipAdapter.loadMoreComplete();
                                }
                            } else {
                                toast(vipList.getE().getDesc());
                                if (page == 1) {
                                    mVipAdapter.setNewData(new ArrayList<VipInfo>());
                                } else {
                                    mVipAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            if (page == 1) {
                                vip_number.setText("0");
                                mVipAdapter.setNewData(new ArrayList<VipInfo>());
                            } else {
                                mVipAdapter.loadMoreEnd(false);
                            }
                            e.printStackTrace();
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mVipAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mVipAdapter.loadMoreFail();
            }
        });
    }

    private void requestSearchVip() {
        showProgressDialog();
        String urlString = "relation/query_vips.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mobile", et_search.getText().toString());
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
                            VipSearch vipSearch = JSON.parseObject(json, VipSearch.class);
                            if (vipSearch.getE().getCode() == 0) {
                                VipInfo data = vipSearch.getData();
                                ArrayList<VipInfo> vipInfos = new ArrayList<>();
                                vipInfos.add(data);
                                mVipAdapter.setNewData(vipInfos);
                            } else {
                                toast(vipSearch.getE().getDesc());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mVipAdapter.notifyDataSetChanged();
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

    private void requestVipLevelList() {
        showProgressDialog();
        String urlString = "relation/query_intg_vips.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("page", "1");
        map.put("page_length", "50");
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
                            VipLevelList vipLevelList = JSON.parseObject(json, VipLevelList.class);
                            if (vipLevelList.getE().getCode() == 0) {
                                vipLevels = vipLevelList.getData();
                                VipLevel vipl = new VipLevel();
                                vipl.setVip_level_name("全部");
                                vipl.setVip_level_id(0);
                                vipl.setVip_level_desc("全部级别");
                                vipLevels.add(0, vipl);

                                requestVipList();

                            } else {
                                toast(vipLevelList.getE().getDesc());
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
    public void onRefresh() {
        loadType = 1;
        page = 1;
        requestVipList();
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestVipList();
    }

    protected void showCalendarInDialog() {

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.dialog_time, null, false);
        theDialog = new AlertDialog.Builder(this) //
                .setTitle("请选择时间段")
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
                        page = 1;
                        requestVipList();
                    }
                }).create();
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

    private void showWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_viplevel, null);
            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            GroupVipLevelListAdapter groupAdapter = new GroupVipLevelListAdapter(this, vipLevels);
            lv_group.setAdapter(groupAdapter);
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, dip2px(this, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                levelname.setText(vipLevels.get(position).getVip_level_name());
                lid = vipLevels.get(position).getVip_level_id() + "";
                page = 1;
                requestVipList();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

