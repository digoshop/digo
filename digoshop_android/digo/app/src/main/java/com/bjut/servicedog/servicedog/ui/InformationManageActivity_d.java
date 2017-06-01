//package com.bjut.servicedog.servicedog.ui;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.adapter.GroupVipLevelListAdapter;
//import com.bjut.servicedog.servicedog.adapter.InformationManageAdapter_f;
//import com.bjut.servicedog.servicedog.po.Message;
//import com.bjut.servicedog.servicedog.po.Message_data;
//import com.bjut.servicedog.servicedog.po.VipLevel;
//import com.bjut.servicedog.servicedog.po.VipLevelList;
//import com.bjut.servicedog.servicedog.utils.Constant;
//import com.bjut.servicedog.servicedog.utils.DateUtil;
//import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;
//import com.bjut.servicedog.servicedog.utils.PopupMenu;
//import com.bjut.servicedog.servicedog.view.My2BDialog;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.squareup.timessquare.CalendarPickerView;
//
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class InformationManageActivity_d extends BaseActivity implements ListItemClickHelp,View.OnClickListener {
//    private InformationManageAdapter_f mAdapter;
//    private PullToRefreshListView MessagelistView;
//    private List<Message_data> mData = new ArrayList<>();
//    private TextView  levelname, timename, vip_number;
//    private int page = 1;
//    private LinearLayout linear_time_search, linear_vip_class;
//    private List<VipLevel> vipLevels = new ArrayList<>();
//    private String stime = "", etime = "", lid = "";
//    private CalendarPickerView dialogView;
//    private AlertDialog theDialog;
//    private Calendar ca;
//    private PopupWindow popupWindow;
//    private View view;
//    private ListView lv_group;
//    private PopupMenu popupMenu;
//    private TextView tv_title;
//    private Button btn_right;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_information_manage_activity_d);
//        init();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mData.clear();
//        page = 1;
//        requestMessageList();
//    }
//
//    private void init() {
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.title_xxgl));
//
//        VipLevel vipl = new VipLevel();
//        vipl.setVip_level_name("全部会员");
//        vipl.setVip_level_id(0);
//        vipl.setVip_level_desc("全部级别");
//        vipLevels.add(vipl);
//        requestVipLevelList();
//        linear_time_search = (LinearLayout) findViewById(R.id.linear_time_search);
//        linear_vip_class = (LinearLayout) findViewById(R.id.linear_vip_class);
//        linear_vip_class.setOnClickListener(this);
//        linear_time_search.setOnClickListener(this);
//        popupMenu = new PopupMenu(this);
//        levelname = (TextView) findViewById(R.id.levelname);
//        timename = (TextView) findViewById(R.id.timename);
//        ca = Calendar.getInstance();
//
//        vip_number = (TextView) findViewById(R.id.vip_number);
//        MessagelistView = (PullToRefreshListView) findViewById(R.id.Information_manage_listview);
//        btn_right = (Button) findViewById(R.id.btn_right);
//        btn_right.setText(getString(R.string.title_cjxx));
//        btn_right.setVisibility(View.VISIBLE);
//        btn_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (vipLevels.size() <= 1) {
//                    toast("店铺会员级别为空，请先到设置中添加会员级别!");
//                } else {
//                    myIntentR(CreateMessageActivity_f.class);
//                }
//
//            }
//        });
//        mAdapter = new InformationManageAdapter_f(this, mData, this);
//        MessagelistView.setAdapter(mAdapter);
//        requestMessageList();
//        MessagelistView.setEmptyView(noDataView);
//        MessagelistView.setMode(PullToRefreshBase.Mode.BOTH);
//        MessagelistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                mData.clear();
//                page = 1;
//                requestMessageList();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                page++;
//                requestMessageList();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//
//            }
//
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.linear_vip_class:
//                showWindow(v);
//                break;
//            case R.id.linear_time_search:
//                popupMenu.showLocation(R.id.linear_time_search);// 设置弹出菜单弹出的位置
//                popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
//                    @Override
//                    public void onClick(PopupMenu.MENUITEM item) {
//                        switch (item) {
//                            case ITEM1:
//                                timename.setText("全部时间");
//                                stime = "";
//                                etime = "";
//                                mData.clear();
//                                page = 1;
//                                requestMessageList();
//                                break;
//                            case ITEM2:
//                                try {
//                                    timename.setText("近一个月");
//                                    etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
//                                    long lastMonth = DateUtil.currentDateParserLongYMD() / 1000 - (3600 * 24 * 30L);
//                                    stime = lastMonth + "";
//                                    mData.clear();
//                                    page = 1;
//                                    requestMessageList();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                break;
//                            case ITEM3:
//
//                                try {
//                                    timename.setText("近三个月");
//                                    etime = DateUtil.currentDateParserLongYMD() / 1000 + 3600 * 24 - 1 + "";
//                                    stime = (DateUtil.currentDateParserLongYMD() / 1000 - 3600 * 24 * 90L) + "";
//                                    mData.clear();
//                                    page = 1;
//                                    requestMessageList();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                break;
//                            case ITEM4:
//                                showCalendarInDialog();
//                                break;
//                        }
//
//                    }
//                });
//                break;
//        }
//    }
//
//    public void requestMessageList() {
//
//        Log.i("aaaaaaa", "sss" + stime);
//        Log.i("aaaaaaa", "sss" + etime);
//        vip_number.setText("0");
//        showProgressDialog();
//        String urlString = "notice/shop_list.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));
//        if (lid.equals("0")) {
//            map.put("lid", "");
//        } else {
//            map.put("lid", lid);
//        }
//        map.put("stime", stime);
//        map.put("etime", etime);
//        map.put("page", page + "");
//        map.put("page_length", "15");
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        String json = responseInfo.result;
//                        try {
//                            Message messagelist = JSON.parseObject(json, Message.class);
//                            if (messagelist.getE().getCode() == 0) {
//                                if (messagelist.getData().size() > 0) {
//                                    List<Message_data> data = messagelist.getData();
//                                    if (data.size() >= 0) {
//                                        mData.addAll(data);
//                                        vip_number.setText(messagelist.getTotal() + "");
//                                    }
//                                } else {
//                                    toast("无更多信息");
//                                }
//                            } else {
//                                mAdapter.notifyDataSetChanged();
//                                toast("暂无消息");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }finally {
//                            mAdapter.notifyDataSetChanged();
//                        }
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
//
//    protected void showCalendarInDialog() {
//
//        final Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 1);
//        final Calendar lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, -1);
//
//        dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.dialog_time, null, false);
//        theDialog = new AlertDialog.Builder(this) //
//                .setTitle("请选择时间段")
//                .setView(dialogView)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        List<Date> dates = dialogView.getSelectedDates();
//                        long firstdate = dates.get(0).getTime() / 1000;
//                        long lastdate = dates.get(dates.size() - 1).getTime() / 1000;
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                        String startime = format.format(dates.get(0).getTime());
//                        String endtime = format.format(dates.get(dates.size() - 1).getTime());
//                        stime = firstdate + "";
//                        etime = lastdate + "";
//                        timename.setText(startime + "至" + endtime);
//                        timename.setTextSize(12);
//                        dialogInterface.dismiss();
//                        mData.clear();
//                        page = 1;
//                        requestMessageList();
//                    }
//                })
//                .create();
//        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                dialogView.fixDialogDimens();
//            }
//        });
//        theDialog.setCanceledOnTouchOutside(true);
//        theDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//
//            }
//        });
//        theDialog.show();
//
//        dialogView.init(lastYear.getTime(), nextYear.getTime()) //
//                .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                .withSelectedDate(new Date());
//
//    }
//
//    private void showWindow(View parent) {
//        if (popupWindow == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = layoutInflater.inflate(R.layout.group_viplevel, null);
//            lv_group = (ListView) view.findViewById(R.id.lvGroup);
//            GroupVipLevelListAdapter groupAdapter = new GroupVipLevelListAdapter(this, vipLevels);
//            lv_group.setAdapter(groupAdapter);
//            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, dip2px(this, LinearLayout.LayoutParams.WRAP_CONTENT));
//        }
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.showAsDropDown(parent, dip2px(this, 0), dip2px(this, 10));
//        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                levelname.setText(vipLevels.get(position).getVip_level_name());
//                lid = vipLevels.get(position).getVip_level_id() + "";
//                mData.clear();
//                page = 1;
//                requestMessageList();
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//            }
//        });
//    }
//
//    private void requestVipLevelList() {
//        showProgressDialog();
//        String urlString = "relation/query_intg_vips.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));
//        map.put("page", "1");
//        map.put("page_length", "50");
//
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        String json = responseInfo.result;
//                        try {
//                            VipLevelList vipLevelList = JSON.parseObject(json, VipLevelList.class);
//                            if (vipLevelList.getE().getCode() == 0) {
//                                vipLevels.addAll(vipLevelList.getData());
//                            } else {
//                                toast(vipLevelList.getE().getDesc());
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
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
//
//    public int dip2px(Context context, float dipValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
//
//    @Override
//    public void onclick(final View item, final View widget, final int position, int which) {
//        switch (which) {
//            case R.id.Ldelete:
//                showAlertDialog(mData, position);
//                break;
//            case R.id.Ledit:
//                Log.i("pppp", position + "");
//                Intent intent = new Intent();
//                intent.setClass(this, EditMessageActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("msg", mData.get(position));
//                intent.putExtras(bundle);
//                this.startActivity(intent);
//                break;
//        }
//    }
//
//    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            MessagelistView.onRefreshComplete();
//        }
//    }
//
//    private void MessageDelete(final int position) {
//        String urlString = "notice/shop_del.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));//获取店铺ID
//        map.put("nid", mData.get(position).getNotice_id() + "");//获取消息ID
//        params = sortMapByKey(map);
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String json = responseInfo.result;
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
//                                toast("消息删除成功");
//                                mData.remove(position);
//                                mAdapter.notifyDataSetChanged();
//                            } else {
//                                toast("消息删除失败");
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
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
//
//    }
//
//    public void showAlertDialog(final List<?> data, final int position) {
//
//        if (mMyDialog == null) {
//            mMyDialog = new My2BDialog(mContext);
//        }
//        mMyDialog.setTitle("温馨提示");
//        mMyDialog.setMessage("确定要删除吗？");
//        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick() {
//                mMyDialog.dismiss();
//                MessageDelete(position);
//            }
//        });
//        mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                mMyDialog.dismiss();
//            }
//        });
//        mMyDialog.show();
//    }
//
//}
