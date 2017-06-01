package com.bjut.servicedog.servicedog.ui.union;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.UnionModel;
import com.bjut.servicedog.servicedog.rc_adapter.UnionListAdapter;
import com.bjut.servicedog.servicedog.ui.ScanActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.StringUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/5/4.
 */

public class UnionListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tv_title;
    private ImageView img_right;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private UnionListAdapter mAdapter;
    private int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_list);
        initViews();
        setListener();
        help();
//        requestUnionList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.INTENT_SCAN:
                    try {
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString("result");
                        Log.i(TAG, "onActivityResult: result=====" + result);
                        JSONObject jsonObject = new JSONObject(result);
                        final String shop_id = jsonObject.getString("shop_id");
                        if (StringUtils.isEmpty(shop_id)) {
                            toast("二维码无效");
                        } else {
                            final My2BDialog mMyDialog = new My2BDialog(mContext);
                            mMyDialog.setTitle("温馨提示");
                            mMyDialog.setMessage("您是否要与该店铺成为联盟关系？");
                            mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    mMyDialog.dismiss();
                                    requestAddUnion(shop_id);

                                }
                            });
                            mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                                @Override
                                public void onNoClick() {
                                    mMyDialog.dismiss();
                                }
                            });
                            mMyDialog.show();
//                            requestDataForScan(uid);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast("二维码无效");
                    }
                    break;
            }
        }
    }


    private void help() {
        boolean union_help = pref.getBoolean("union_help", true);
        if (union_help) {
            final My2BDialog my2BDialog = new My2BDialog(mContext);
            my2BDialog.setTitle("帮助");
            my2BDialog.setCancelable(false);
            my2BDialog.setMessage("商家联盟是一种商家之间互相引流的手段，当两个商家联盟后，可在对方店铺首页上展示出自己的店铺，以吸引用户来浏览店铺信息。商家联盟目前最多可以联盟15家店铺，所以在联盟时请慎重选择。\n" +
                    "操作方式：点击右上角按钮打开扫描二维码，扫描对方商家二维码（在设置中可以查看），确认后双方正式成为联盟。");
            my2BDialog.setYesOnclickListener("不再提醒", new My2BDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    editor.putBoolean("union_help", false).commit();
                    my2BDialog.dismiss();
                    requestUnionList();
                }
            });
            my2BDialog.setNoOnclickListener("知道了", new My2BDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    my2BDialog.dismiss();
                    requestUnionList();
                }
            });
            my2BDialog.show();
        } else {
            requestUnionList();
        }
    }


    private void setListener() {
        img_right.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_delete:
                        deleteUnion(position, mAdapter.getData().get(position).getSid());
                        break;
                }
            }
        });
    }

    private void deleteUnion(final int position, final String sid) {
        final My2BDialog mMyDialog = new My2BDialog(mContext);
        mMyDialog.setTitle("温馨提示");
        mMyDialog.setMessage("您是否要与" + mAdapter.getData().get(position).getSn() + "店铺解除联盟关系？");
        mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                mMyDialog.dismiss();
                requestDeleteUnion(mAdapter.getData().get(position).getSid(), position);
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
        tv_title = $(R.id.tv_title);
        img_right = $(R.id.img_right);
        mRecyclerView = $(R.id.recyclerView);
        mSwipeRefreshLayout = $(R.id.swipeRefreshLayout);

        tv_title.setText(getString(R.string.title_sjlm));
        img_right.setImageResource(R.drawable.scan);
        img_right.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(mContext, LinearLayout.VERTICAL));
        mAdapter = new UnionListAdapter();
        mAdapter.setEmptyView(noDataView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_right:
                Intent intent = new Intent(mContext, ScanActivity.class);
                startActivityForResult(intent, Constant.INTENT_SCAN);
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadType = 1;
        requestUnionList();
    }

    public void requestAddUnion(String sids) {
        showProgressDialog();
        String urlString = "business/add_shop_alliance.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("sids", sids);
        RequestParams params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Log.i("result", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getJSONObject("e").getInt("code");
                    if (code == 0) {
                        requestUnionList();
                    } else {
                        toast("结盟失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    public void requestDeleteUnion(String sids, final int position) {
        showProgressDialog();
        String urlString = "business/cancel_shop_alliance.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        final Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("sids", sids);
        RequestParams params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Log.i("result", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getJSONObject("e").getInt("code");
                    if (code == 0) {
                        mAdapter.remove(position);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        toast("删除失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    public void requestUnionList() {
        if (loadType == 0) {
            showProgressDialog();
        }
        String urlString = "business/query_shop_alliance.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        RequestParams params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Log.i("result", json);
                try {
                    UnionModel unionModel = JSON.parseObject(json, UnionModel.class);
                    if (unionModel.getE().getCode() == 0) {
                        if (unionModel.getTotal() >= 15) {
                            img_right.setVisibility(View.GONE);
                        } else {
                            img_right.setVisibility(View.VISIBLE);
                        }
                        List<UnionModel.DataBean> data = unionModel.getData();
                        if (data.size() > 0) {
                            mAdapter.setNewData(data);
                            if (data.size() < Constant.PAGE_SIZE) {
                                mAdapter.loadMoreEnd(false);
                            } else {
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                            mAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
                        }
                    } else {
                        mAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
                    }
                } catch (Exception e) {
                    mAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
                    e.printStackTrace();
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
}
