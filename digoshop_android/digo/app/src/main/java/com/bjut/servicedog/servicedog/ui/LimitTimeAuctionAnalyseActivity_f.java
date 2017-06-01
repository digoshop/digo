//package com.bjut.servicedog.servicedog.ui;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.adapter.LimitTimeAuctionAnalyseAdapter;
//import com.bjut.servicedog.servicedog.po.LimitTimeAuction;
//import com.bjut.servicedog.servicedog.po.LimitTimeAuction_data;
//import com.bjut.servicedog.servicedog.utils.Constant;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class LimitTimeAuctionAnalyseActivity_f extends BaseActivity {
//    private LimitTimeAuctionAnalyseAdapter mAdapter;
//    private PullToRefreshListView listView;
//    private List<LimitTimeAuction_data> mData = new ArrayList<>();
//    private List<LimitTimeAuction_data> mDataOk = new ArrayList<>();
//    private int page = 1;
//
//    private String mt = "";
//
//    private TextView tv_title;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_limit_time_auction_analyse_activity_f);
//        mt = getIntent().getStringExtra("mt");
//        init();
//        requestlist();
//        listView.setEmptyView(noDataView);
//        listView.setMode(PullToRefreshBase.Mode.BOTH);
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                mData.clear();
//                page = 1;
//                requestlist();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh_auction().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                page++;
//                requestlist();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh_auction().execute();
//            }
//        });
//    }
//
//    public void init() {
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.title_xsjpfx));
//
//        listView = (PullToRefreshListView) findViewById(R.id.limit_time_listview);
//        mAdapter = new LimitTimeAuctionAnalyseAdapter(this, mData);
//        listView.setAdapter(mAdapter);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//
//    private class FinishRefresh_auction extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            listView.onRefreshComplete();
//        }
//    }
//
//    private void requestlist() {
//        showProgressDialog();
//        String urlString = "product/queryProductStat.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("targetId", pref.getString("sid", ""));
//        //map.put("targetId",1000035+"");
//        map.put("targetType", "2");
//        map.put("productType", "1");
//        map.put("mt", mt);
//        map.put("page", page + "");
//        map.put("page_length", "10");
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
//                        closeProgressDialog();
//                        String json = responseInfo.result;
//                        try {
//                            LimitTimeAuction auctionlist = JSON.parseObject(json, LimitTimeAuction.class);
//                            if (auctionlist.getE().getCode() == 0) {
//                                if (auctionlist.getData().size() > 0) {
//                                    mDataOk = auctionlist.getData();// 填充数据
//                                    System.out.println("mdataok" + mDataOk);
//                                    if (mDataOk.size() >= 0) {
//                                        mData.addAll(mDataOk);
//                                        mDataOk.clear();
//                                        mAdapter.notifyDataSetChanged();
//                                    }
//                                } else {
//                                    toast("无更多信息");
//                                }
//                            } else {
//                                toast(auctionlist.getE().getDesc());
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
//}
