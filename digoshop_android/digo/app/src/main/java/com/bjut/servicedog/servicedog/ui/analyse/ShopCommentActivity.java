package com.bjut.servicedog.servicedog.ui.analyse;

import android.content.Intent;
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
import com.bjut.servicedog.servicedog.po.Shopcomment;
import com.bjut.servicedog.servicedog.po.Shopcomment_comments;
import com.bjut.servicedog.servicedog.rc_adapter.ShopCommentAdapter;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.ReplyActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
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

public class ShopCommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ShopCommentAdapter mAdapter;
    private LinearLayout allcomments, goodcomments, zhongcomments, badcomments;
    private TextView tv_allcomments, tv_zhongcomments, tv_goodcomments, tv_badcomments;
    private TextView allcomments_count, zhongcomments_count, goodcomments_count, chacomments_count;
    private String max_id = "", min_id = "";
    private String type = "";
    private TextView tv_title;

    private int loadType = 0;
    private String mt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_comment);
        mt = getIntent().getStringExtra("mt");
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCommentlist("", "", type,false);
    }

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_dppj));
        allcomments = (LinearLayout) findViewById(R.id.linear_allcomment);
        goodcomments = (LinearLayout) findViewById(R.id.linear_goodcomments);
        zhongcomments = (LinearLayout) findViewById(R.id.linear_zhongcomments);
        badcomments = (LinearLayout) findViewById(R.id.linear_badcomments);
        allcomments.setOnClickListener(this);
        goodcomments.setOnClickListener(this);
        zhongcomments.setOnClickListener(this);
        badcomments.setOnClickListener(this);
        tv_allcomments = (TextView) findViewById(R.id.allcomments);
        tv_zhongcomments = (TextView) findViewById(R.id.zhongcommens);
        tv_goodcomments = (TextView) findViewById(R.id.goodcomments);
        tv_badcomments = (TextView) findViewById(R.id.badcomments);
        tv_allcomments.setTextColor(getResources().getColor(R.color.word_color));
        tv_goodcomments.setTextColor(getResources().getColor(R.color.cache_count));
        tv_badcomments.setTextColor(getResources().getColor(R.color.cache_count));
        tv_zhongcomments.setTextColor(getResources().getColor(R.color.cache_count));
        allcomments_count = (TextView) findViewById(R.id.allcomments_number);
        zhongcomments_count = (TextView) findViewById(R.id.zhongcomments_number);
        goodcomments_count = (TextView) findViewById(R.id.goodcomments_number);
        chacomments_count = (TextView) findViewById(R.id.badcomments_number);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.title_background);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0,0,20,0));
        mAdapter = new ShopCommentAdapter();
        mAdapter.setEmptyView(noDataView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this);
//        mProductAdapter.openLoadAnimation(Constant.LOAD_ANIMATION);
        mAdapter.isFirstOnly(true);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.reply_save:
                        Intent intent = new Intent(mContext, ReplyActivity.class);
                        intent.putExtra("mcid", mAdapter.getItem(position).getMcid());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadType =1;
        requestCommentlist("", "", type,false);
    }

    @Override
    public void onLoadMoreRequested() {
        loadType =2;
        requestCommentlist(min_id, "", type,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_allcomment:
                type = "";
                requestCommentlist("", "", type,false);

                tv_goodcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_badcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_zhongcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_allcomments.setTextColor(getResources().getColor(R.color.word_color));


                break;
            case R.id.linear_goodcomments:
                type = "0";
                requestCommentlist("", "", type,false);

                tv_allcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_badcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_zhongcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_goodcomments.setTextColor(getResources().getColor(R.color.word_color));
                break;
            case R.id.linear_zhongcomments:
                type = "1";
                requestCommentlist("", "", type,false);

                tv_goodcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_badcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_allcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_zhongcomments.setTextColor(getResources().getColor(R.color.word_color));

                break;
            case R.id.linear_badcomments:
                type = "2";
                requestCommentlist("", "", type,false);

                tv_goodcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_allcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_zhongcomments.setTextColor(getResources().getColor(R.color.cache_count));
                tv_badcomments.setTextColor(getResources().getColor(R.color.word_color));

                break;
        }
    }


    private void requestCommentlist(final String max, String min, final String type, final boolean isLoadMore) {
        if (loadType==0) {
            showProgressDialog();
        }

        String urlString = "merchant_comment/merchant_list.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("max_id", max);
        map.put("min_id", min);
        map.put("count", "15");
        map.put("mt", mt);
        map.put("type", type);
        map.put("sort", "1");
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
                            Shopcomment commentlist = JSON.parseObject(json, Shopcomment.class);
                            if (commentlist.getE().getCode() == 0) {
                                max_id = commentlist.getData().getMax_id() + "";
                                min_id = commentlist.getData().getMin_id() + "";
                                if (type.equals("")) {
                                    allcomments_count.setText(commentlist.getTotal() + "");
                                    goodcomments_count.setText(commentlist.getData().getGood() + "");
                                    chacomments_count.setText(commentlist.getData().getBad() + "");
                                    zhongcomments_count.setText(commentlist.getData().getNormal() + "");
                                }

                                List<Shopcomment_comments> data = commentlist.getData().getComments();
                                if (commentlist.getData().getComments().size() > 0) {
                                    if (!isLoadMore) {
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
                                    if ("".equals(max)) {
                                        if (type.equals("")) {
                                            goodcomments_count.setText("0");
                                            chacomments_count.setText("0");
                                            zhongcomments_count.setText("0");
                                        } else if (type.equals("0")) {
                                            goodcomments_count.setText("0");
                                        } else if (type.equals("1")) {
                                            zhongcomments_count.setText("0");
                                        } else if (type.equals("2")) {
                                            chacomments_count.setText("0");
                                        }
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                if ("".equals(max)) {
                                    if (type.equals("")) {
                                        goodcomments_count.setText("0");
                                        chacomments_count.setText("0");
                                        zhongcomments_count.setText("0");
                                    } else if (type.equals("0")) {
                                        goodcomments_count.setText("0");
                                    } else if (type.equals("1")) {
                                        zhongcomments_count.setText("0");
                                    } else if (type.equals("2")) {
                                        chacomments_count.setText("0");
                                    }
                                    mAdapter.setNewData(new ArrayList<Shopcomment_comments>());
                                }else{
                                    mAdapter.loadMoreEnd(false);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAdapter.loadMoreEnd(false);
                        } finally {
                            finishLoad();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                finishLoad();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void finishLoad() {
        if (loadType == 0) {
            closeProgressDialog();
        } else if (loadType == 1) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
        }
    }

}
