package com.bjut.servicedog.servicedog.ui.user;

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
import com.bjut.servicedog.servicedog.po.UserManage;
import com.bjut.servicedog.servicedog.po.UserManage_data;
import com.bjut.servicedog.servicedog.rc_adapter.UserAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.bjut.servicedog.servicedog.view.MyDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private TextView tv_title;
    private Button btn_right;
    private int page = 1;
    private int loadType = 0;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserAdapter mUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        init();
        setListener();
        requestList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        requestList();

    }

    private void setListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.usermanage_status:
                        operateStatus(position, false);
                        break;
                    case R.id.delete:

                        if (mMyDialog == null) {
                            mMyDialog = new My2BDialog(mContext);
                        }
                        mMyDialog.setTitle("温馨提示");
                        mMyDialog.setMessage("确定要删除吗？");
                        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                mMyDialog.dismiss();
                                operateStatus(position, true);
                            }
                        });
                        mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                mMyDialog.dismiss();
                            }
                        });
                        mMyDialog.show();

                        break;
                }
            }
        });
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_yhgl));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(R.string.title_tjyh));
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mUserAdapter = new UserAdapter();
        mUserAdapter.setEmptyView(noDataView);
        mUserAdapter.setEnableLoadMore(true);
        mUserAdapter.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mUserAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mUserAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                myIntentR(AddUserActivity.class);
                break;
        }
    }

    private void requestList() {
        showProgressDialog();

        String urlString = "passport/suser_list.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("page", page + "");
        map.put("page_length", Constant.PAGE_SIZE + "");
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
                            UserManage userManage = JSON.parseObject(json, UserManage.class);
                            if (userManage.getE().getCode() == 0) {
                                if (userManage.getData().size() > 0) {
                                    List<UserManage_data> data = userManage.getData();
                                    if (data.size() >= 0) {
                                        if (page == 1) {
                                            mUserAdapter.setNewData(data);
                                        } else {
                                            mUserAdapter.addData(data);
                                        }
                                        if (data.size() < Constant.PAGE_SIZE) {
                                            mUserAdapter.loadMoreEnd(false);
                                        } else {
                                            mUserAdapter.loadMoreComplete();
                                        }
                                    }
                                } else {
                                    if (page == 1) {
                                        mUserAdapter.setNewData(new ArrayList<UserManage_data>());
                                    } else {
                                        mUserAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                if (page == 1) {
                                    mUserAdapter.setNewData(new ArrayList<UserManage_data>());
                                } else {
                                    mUserAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (page == 1) {
                                mUserAdapter.setNewData(new ArrayList<UserManage_data>());
                            } else {
                                mUserAdapter.loadMoreEnd(false);
                            }
                        } finally {
                            if (loadType == 0) {
                                closeProgressDialog();
                            } else if (loadType == 1) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                            }
                            mUserAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
                mSwipeRefreshLayout.setRefreshing(false);
                mUserAdapter.loadMoreFail();
            }
        });
    }

    private void operateStatus(final int position, final boolean isDelete) {
        String urlString = "passport/sop_user.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();

        map.put("sid", pref.getString("sid", ""));//商铺ID
        map.put("opuid", pref.getString("uid", ""));//操作UID
        if (!isDelete) {
            if (mUserAdapter.getItem(position).getStatus() == 1) {
                map.put("status", "0");
            } else if (mUserAdapter.getItem(position).getStatus() == 0) {
                map.put("status", "1");
            }
        } else {
            map.put("status", "-1");
        }

        params = sortMapByKey(map);
        params.addBodyParameter("uid", mUserAdapter.getItem(position).getUid());//用户ID

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
//                                if (jsonObject.getString("data").equals("true")) {
                                if (isDelete) {
                                    mUserAdapter.remove(position);
                                    mUserAdapter.notifyDataSetChanged();
                                } else {
                                    UserManage_data item = mUserAdapter.getItem(position);
                                    if (item.getStatus() == 0) {
                                        toast("禁用成功!");
                                        item.setStatus(1);
//                                            mUserAdapter.notifyItemChanged(position, item);
                                    } else if (item.getStatus() == 1) {
                                        toast("启用成功!");
                                        item.setStatus(0);
//                                            mUserAdapter.notifyItemChanged(position, item);
                                    }
                                }
//                                }
                            } else {
                                toast("操作失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mUserAdapter.notifyDataSetChanged();
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
        requestList();
    }

    @Override
    public void onLoadMoreRequested() {
        loadType = 2;
        page++;
        requestList();
    }
}
