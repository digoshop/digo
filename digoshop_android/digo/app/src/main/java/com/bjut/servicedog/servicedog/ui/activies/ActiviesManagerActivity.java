package com.bjut.servicedog.servicedog.ui.activies;

import android.content.Intent;
import android.os.Bundle;
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
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.po.Event;
import com.bjut.servicedog.servicedog.po.Event_data;
import com.bjut.servicedog.servicedog.rc_adapter.ActivitiesAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.EventDetailActivity;
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
 * Created by beibeizhu on 17/3/6.
 */

public class ActiviesManagerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private TextView tv_title;
    private Button btn_right;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ActivitiesAdapter mActivitiesAdapter;
    private int index = 1;
    private int loadType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_manager);
        initViews();
        setListener();
        requestEventList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(OnRefreshEvent onRefreshEvent) {
        if (onRefreshEvent.isRefresh()) {
            index = 1;
            requestEventList();
        }
    }


    private void setListener() {
        btn_right.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                Event_data event_data = mActivitiesAdapter.getItem(position);
                intent.putExtra("data", event_data);
                startActivity(intent);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.Ledit:
                        Intent intent = new Intent(mContext, EditDiscountEventActivity.class);
                        intent.putExtra("eventid", mActivitiesAdapter.getItem(position).getMnid() + "");//优惠活动ID
                        intent.putExtra("eventtile", mActivitiesAdapter.getItem(position).getMnti());//传入标题
                        intent.putExtra("eventdetail", mActivitiesAdapter.getItem(position).getMnc());//传入内容
                        intent.putExtra("eventstarttime", mActivitiesAdapter.getItem(position).getMnvsd() + "");//活动开始时间
                        intent.putExtra("eventendtime", mActivitiesAdapter.getItem(position).getMnved() + "");//活动结束时间
                        intent.putExtra("strstartime", mActivitiesAdapter.getItem(position).getStarttime());//活动结束时间
                        intent.putExtra("strendtime", mActivitiesAdapter.getItem(position).getEndttime());//活动结束时间
                        intent.putExtra("eventplace", mActivitiesAdapter.getItem(position).getMnad());//活动地点
                        intent.putExtra("eventpicture", mActivitiesAdapter.getItem(position).getMnp());//图片的路径
                        intent.putExtra("area", mActivitiesAdapter.getItem(position).getArea());
                        intent.putExtra("city", mActivitiesAdapter.getItem(position).getCity());
                        intent.putExtra("flag", "edit");
                        intent.putExtra("naid", mActivitiesAdapter.getItem(position).getNaid() + "");
                        if (mActivitiesAdapter.getItem(position).getMnas() == 3 || mActivitiesAdapter.getItem(position).getMnas() == 4) {
                            intent.putExtra("mnafr", mActivitiesAdapter.getItem(position).getMnafr());
                        }
                        startActivity(intent);
                        break;
                    case R.id.Ldelete:
                        deleteActivity(position, mActivitiesAdapter.getItem(position).getMnid());
                        break;
                    case R.id.Lput:
                        deleteData(mActivitiesAdapter.getItem(position).getMnid() + "", "1", position);
                        break;

                    case R.id.Loff:
                        deleteData(mActivitiesAdapter.getItem(position).getMnid() + "", "2", position);
                        break;
                }
            }
        });
    }

    private void deleteActivity(final int position, final long pid) {
        final My2BDialog mMyDialog = new My2BDialog(mContext);
        mMyDialog.setTitle("温馨提示");
        mMyDialog.setMessage("确定要删除吗？");
        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                mMyDialog.dismiss();
                deleteData(pid + "", Constant.PRODUCT_OPERATION_DELETE + "", position);
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


    private void initViews() {
        EventBus.getDefault().register(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_right = (Button) findViewById(R.id.btn_right);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        tv_title.setText("活动管理");
        btn_right.setText("新建活动");
        btn_right.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mActivitiesAdapter = new ActivitiesAdapter();
        mActivitiesAdapter.setEmptyView(noDataView);
        mActivitiesAdapter.setEnableLoadMore(true);
        mActivitiesAdapter.setOnLoadMoreListener(this);
//        mActivitiesAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mActivitiesAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mActivitiesAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                startActivity(new Intent(this, CreateDiscountEventActivity_f.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        index = 1;
        loadType = 1;
        requestEventList();
    }

    @Override
    public void onLoadMoreRequested() {
        index++;
        loadType = 2;
        requestEventList();
    }

    private void requestEventList() {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "business/query_news.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
        map.put("page", index + "");
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
                    Event eventlist = JSON.parseObject(json, Event.class);
                    if (eventlist.getE().getCode() == 0) {
                        List<Event_data> data = eventlist.getData();
                        if (data.size() > 0) {
                            if (index == 1) {
                                mActivitiesAdapter.setNewData(data);
                            } else {
                                mActivitiesAdapter.addData(data);
                            }
                            if (data.size() < Constant.PAGE_SIZE) {
                                mActivitiesAdapter.loadMoreEnd(false);
                            } else {
                                mActivitiesAdapter.loadMoreComplete();
                            }
                        } else {
                            if (index == 1) {
                                mActivitiesAdapter.setNewData(new ArrayList<Event_data>());
                            } else {
                                mActivitiesAdapter.loadMoreEnd(false);
                            }
                        }
                    } else {
                        if (index == 1) {
                            mActivitiesAdapter.setNewData(new ArrayList<Event_data>());
                        } else {
                            mActivitiesAdapter.loadMoreEnd(false);
                        }
                    }
                } catch (Exception e) {
                    mActivitiesAdapter.loadMoreEnd(false);
                    e.printStackTrace();
                } finally {
                    if (loadType == 0) {
                        closeProgressDialog();
                    } else if (loadType == 1) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                    }
                    mActivitiesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mActivitiesAdapter.loadMoreFail();
            }
        });
    }

    private void deleteData(final String id, final String status, final int position) {
        showProgressDialog();
        String urlString = "business/update_news_st.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        final Map<String, String> map = new HashMap<>();
        map.put("news_id", id);
        map.put("status", status);//status 状态 1启用 2禁用 99删除
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                        + urlString, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(final ResponseInfo<String> responseInfo) {
                        System.out.println(responseInfo.result);
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                toast("操作成功");
                                if (status.equals("99")) {
                                    mActivitiesAdapter.remove(position);
                                } else if (status.equals("1")) {
                                    Event_data item = mActivitiesAdapter.getItem(position);
                                    item.setMnst("1");
//                                    mActivitiesAdapter.notifyItemChanged(position, item);
                                } else if (status.equals("2")) {
                                    Event_data item = mActivitiesAdapter.getItem(position);
                                    item.setMnst("2");
//                                    mActivitiesAdapter.notifyItemChanged(position, item);
                                }
                            } else {
                                toast("操作失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally{
                            mActivitiesAdapter.notifyDataSetChanged();
                        }
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {
                        closeProgressDialog();
                        toast(Constant.CHECK_NETWORK);
                    }
                }

        );
    }
}
