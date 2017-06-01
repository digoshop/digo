//package com.bjut.servicedog.servicedog.ui;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.adapter.AuctionGoodsListAdapter;
//import com.bjut.servicedog.servicedog.adapter.ExchangeGoodsListAdapter;
//import com.bjut.servicedog.servicedog.adapter.ViewPagerAdapter;
//import com.bjut.servicedog.servicedog.po.Good;
//import com.bjut.servicedog.servicedog.po.GoodsList;
//import com.bjut.servicedog.servicedog.utils.Constant;
//import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;
//import com.bjut.servicedog.servicedog.view.My2BDialog;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SaleGoodsManageActivity extends BaseActivity implements View.OnClickListener, ListItemClickHelp {//CompoundButton.OnCheckedChangeListener
//    private MyViewPager viewPager;
//    private ArrayList<View> lists = new ArrayList<View>();
//    private ViewPagerAdapter adapter;
//    private int currentItem;
//    private TextView exchange;
//    private TextView auction;
//    private TextView tv1, tv2, delete;
//    private PullToRefreshListView exchange_list;
//    private PullToRefreshListView auction_list;
//    private ExchangeGoodsListAdapter mAdapter;
//    private AuctionGoodsListAdapter Madapter;
//    private List<Good> mData = new ArrayList<>();
//    private List<Good> mDataOk = new ArrayList<>();
//    private List<Good> mDataAuction = new ArrayList<>();
//    private List<Good> mDataOKAuction = new ArrayList<>();
//    private View view1, view2;
//    private TextView exchange_add_goods, auction_add_goods;
//    private TextView tv_title;
//    private int page = 1, page2 = 1;
//    private CheckBox checkBox;
//
//    private final String TAG = this.getClass().getSimpleName();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sale_goods_manage);
//        exchange = (TextView) findViewById(R.id.tv_exchange);
//        auction = (TextView) findViewById(R.id.tv_auction);
//        tv1 = (TextView) findViewById(R.id.tv_left);
//        tv2 = (TextView) findViewById(R.id.tv_right);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.title_shangpinguanli));
//        exchange_add_goods = (TextView) findViewById(R.id.bottombutton_exchange);
//        auction_add_goods = (TextView) findViewById(R.id.bottombutton_auction);
//        view1 = this.getLayoutInflater().inflate(R.layout.activity_exchange_goods, null);
//        view2 = this.getLayoutInflater().inflate(R.layout.activity_auction_goods, null);
//        lists.add(view1);
//        lists.add(view2);
//        adapter = new ViewPagerAdapter(lists);
//        viewPager = (MyViewPager) findViewById(R.id.viewPager);
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            // 当滑动式，顶部的imageView是通过animation缓慢的滑动 243,152,0     196,196,196
//            @Override
//            public void onPageSelected(int arg0) {
//                switch (arg0) {
//                    case 0:
//                        if (currentItem == 1) {
//                            tv2.setBackgroundResource(R.color.divide_color);
//                            auction.setTextColor(getResources().getColor(R.color.cancel_word_color));
//                            tv1.setBackgroundResource(R.color.orange);
//                            exchange.setTextColor(Color.rgb(243, 152, 0));
//                            auction_add_goods.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 1:
//                        if (currentItem == 0) {
//                            tv1.setBackgroundResource(R.color.divide_color);
//                            exchange.setTextColor(getResources().getColor(R.color.cancel_word_color));
//                            tv2.setBackgroundResource(R.color.orange);
//                            auction.setTextColor(Color.rgb(243, 152, 0));
//                            auction_add_goods.setVisibility(View.VISIBLE);
//                            auction_add_goods.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    myIntentR(AddAuctionGoodsActivity_f.class);
//                                }
//                            });
//                        }
//                        break;
//                }
//                currentItem = arg0;
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//
//            }
//        });
//        exchange_list = (PullToRefreshListView) view1.findViewById(R.id.exchange_listview);
//        auction_list = (PullToRefreshListView) view2.findViewById(R.id.auction_listview);
//        mAdapter = new ExchangeGoodsListAdapter(this, mData, this);
//        Madapter = new AuctionGoodsListAdapter(this, mDataAuction, this);
//
//        auction_list.setAdapter(Madapter);
//        exchange_list.setEmptyView(noDataView);
//        exchange_list.setAdapter(mAdapter);
//        exchange_add_goods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myIntentR(AddExchangeGoodsActivity_F.class);
//            }
//        });
//        exchange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(0);
//                exchange_list.setEmptyView(noDataView);
//
//            }
//        });
//
//        auction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(1);
//                auction_list.setEmptyView(noDataView);
//            }
//        });
//        requestGoodsList();
//        exchange_list.setMode(PullToRefreshBase.Mode.BOTH);
//        exchange_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                Log.i("whichlist", "exchange_list");
//                mData.clear();
//                page = 1;
//                requestGoodsList();
//                new FinishRefresh().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                Log.i("whichlist", "exchange_list");
//                page++;
//                requestGoodsList();
//                new FinishRefresh().execute();
//            }
//
//        });
//        requestAuctionGoodsList();
//        auction_list.setMode(PullToRefreshBase.Mode.BOTH);
//        auction_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                Log.i("whichlist", "auction_list");
//                mDataAuction.clear();
//                page2 = 1;
//                requestAuctionGoodsList();
//                Madapter.notifyDataSetChanged();
//                new FinishRefresh_auction().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                Log.i("whichlist", "auction_list");
//                page2++;
//                requestAuctionGoodsList();
//                Madapter.notifyDataSetChanged();
//                new FinishRefresh_auction().execute();
//
//            }
//
//        });
//    }
//
//    public void requestGoodsList() {
//        showProgressDialog();
//
//        String urlString = "product/getProductsByTargetId.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("targetId", pref.getString("sid", ""));
//        map.put("targetType", "2");
//        map.put("productType", "2");
//        map.put("page", page + "");
//        map.put("page_length", "10");
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
//
//                        String json = responseInfo.result;
//                        try {
//                            GoodsList goodsList = JSON.parseObject(json, GoodsList.class);
//                            if (goodsList.getE().getCode() == 0) {
//                                if (goodsList.getData().size() > 0) {
//                                    mDataOk = goodsList.getData();// 填充数据
//                                    System.out.println("mdataok" + mDataOk);
//                                    if (mDataOk.size() >= 0) {
//                                        mData.addAll(mDataOk);
//                                        mDataOk.clear();
//                                        mAdapter.notifyDataSetChanged();
////                                        mAdapter_edit.notifyDataSetChanged();
//                                    }
//                                } else {
//                                    toast("无更多信息");
//                                }
//                            } else {
//                                toast(goodsList.getE().getDesc());
//                            }
//                            closeProgressDialog();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                            closeProgressDialog();
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
//        exchange_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplication(), CheckGood_ExchangeActivity_r.class);
//                intent.putExtra("picureUrl", mData.get(i - 1).getPpi());
//                intent.putExtra("babyname", mData.get(i - 1).getPna());
//                intent.putExtra("babymoney", mData.get(i - 1).getEpp() + "+" + mData.get(i - 1).getEpg() + "");
//                intent.putExtra("pid", mData.get(i - 1).getPid() + "");
//                intent.putExtra("time", mData.get(i - 1).getEndTime());
//                intent.putExtra("state", mData.get(i - 1).getEpsStr());
//
//                startActivity(intent);
//            }
//        });
//
//        auction_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i("whcihcheck", "auction_tv_check");
//                Intent intent1 = new Intent(SaleGoodsManageActivity.this, CheckGood_AuctionActivity.class);
//                intent1.putExtra("auctionpicureUrl", mDataAuction.get(i - 1).getPpi());
//                intent1.putExtra("auctionbabyname", mDataAuction.get(i - 1).getPna());
//                intent1.putExtra("auctionbabymoney", mDataAuction.get(i - 1).getAplg() + "");
//                intent1.putExtra("auctionmoney", mDataAuction.get(i - 1).getPpr() + "");
//                intent1.putExtra("pid", mDataAuction.get(i - 1).getPid() + "");
//                intent1.putExtra("app", mDataAuction.get(i - 1).getApp() + "");
//                intent1.putExtra("time", mDataAuction.get(i - 1).getApsdTime() + "");
//                intent1.putExtra("state", mDataAuction.get(i - 1).getApsStr());
//                startActivity(intent1);
//            }
//        });
//
//    }
//
//    public void requestAuctionGoodsList() {
//        showProgressDialog();
//        String urlString = "product/getProductsByTargetId.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("targetId", pref.getString("sid", ""));
//        map.put("targetType", "2");
//        map.put("productType", "1");
//        map.put("page", page2 + "");
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
//                            GoodsList goodsList = JSON.parseObject(json, GoodsList.class);
//                            if (goodsList.getE().getCode() == 0) {
//                                if (goodsList.getData().size() > 0) {
//                                    mDataOKAuction = goodsList.getData();// 填充数据
//                                    System.out.println("mdataok" + mDataOKAuction);
//                                    if (mDataOKAuction.size() >= 0) {
//                                        mDataAuction.addAll(mDataOKAuction);
//                                        mDataOKAuction.clear();
//                                        Madapter.notifyDataSetChanged();
////                                        Madapter_edit.notifyDataSetChanged();
//                                    }
//                                } else {
//                                    toast("无更多信息");
//                                }
//                            } else {
//                                toast(goodsList.getE().getDesc());
//                            }
//                        } catch (Exception e) {
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
//    @Override
//    public void onClick(View v) {
//    }
//
//    @Override
//    public void onclick(View item, View widget, final int position, int which) {
//        switch (which) {
//            case R.id.img_delete:
//                if (mMyDialog == null) {
//                    mMyDialog =new My2BDialog(mContext);
//                }
//                mMyDialog.setTitle("温馨提示");
//                mMyDialog.setMessage("确定要删除吗？");
//                mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
//                    @Override
//                    public void onYesClick() {
//                        deleteExchangegoods(mData.get(position).getPid(), "2", "99");
//                        mMyDialog.dismiss();
//                    }
//                });
//                mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
//                    @Override
//                    public void onNoClick() {
//                        mMyDialog.dismiss();
//                    }
//                });
//                mMyDialog.show();
//
//                break;
//            case R.id.img_update:
//                Intent intent = new Intent(this, EditExchangeGoodActivity.class);
//                intent.putExtra("goodId", mData.get(position).getPid() + "");
//                String eps = mData.get(position).getEps();
//                if ("2".equals(eps)) {
//                    String pafr = mData.get(position).getPafr();
//                    if (pafr != null && !"".equals(pafr)) {
//                        intent.putExtra("reason", pafr);
//                    }
//                } else {
//                }
//                startActivity(intent);
//                break;
//            case R.id.tv_is_use:
//                String status = ((TextView) item).getText().toString();
//                if (status.equals("启用")) {
//                    deleteExchangegoods(mData.get(position).getPid(), "2", "1");
//                } else {
//                    deleteExchangegoods(mData.get(position).getPid(), "2", "2");
//                }
//                break;
//            case R.id.an_img_update:
//                Intent intent1 = new Intent(this, UpdateAuctionGoodsActivity.class);
//                intent1.putExtra("pid", mDataAuction.get(position).getPid() + "");
//                String au_eps = mDataAuction.get(position).getAps();
//                if ("2".equals(au_eps)) {
//                    String reason = mDataAuction.get(position).getPafr();
//                    if (reason != null && !"".equals(reason)) {
//                        intent1.putExtra("reason", reason);
//                    }
//                } else {
//                }
//                startActivity(intent1);
//                break;
//            case R.id.an_img_delete:
//                if (mMyDialog == null) {
//                    mMyDialog =new My2BDialog(mContext);
//                }
//                mMyDialog.setTitle("温馨提示");
//                mMyDialog.setMessage("确定要删除吗？");
//                mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
//                    @Override
//                    public void onYesClick() {
//                        deleteExchangegoods(mDataAuction.get(position).getPid(), "1", "99");
//                        mMyDialog.dismiss();
//                    }
//                });
//                mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
//                    @Override
//                    public void onNoClick() {
//                        mMyDialog.dismiss();
//                    }
//                });
//                mMyDialog.show();
//
//                break;
//            case R.id.an_tv_is_use:
//                String an_status = ((TextView) item).getText().toString();
//                if (an_status.equals("启用")) {
//                    deleteExchangegoods(mDataAuction.get(position).getPid(), "1", "1");
//                } else {
//                    deleteExchangegoods(mDataAuction.get(position).getPid(), "1", "2");
//                }
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
//            exchange_list.onRefreshComplete();
//        }
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
//            auction_list.onRefreshComplete();
//        }
//    }
//
//    private void deleteExchangegoods(final long id, final String flag, final String ps) {
//        String urlString = "product/del_product.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("pid", id + "");//获取商品ID
//        map.put("sid", pref.getString("sid", ""));
//        map.put("pt", flag);//1代表竞拍,2代表兑换
//        map.put("ps", ps);//状态(2禁用 99删除)
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
//                                if (flag.equals("2")) {
//                                    if ("1".equals(ps)) {
//                                        mAdapter.setStatus(id, 8);
//                                    } else if ("2".equals(ps)) {
//                                        mAdapter.setStatus(id, 99);
//                                    } else {
//                                        mAdapter.delete(id);
//                                        mAdapter.notifyDataSetChanged();
//                                    }
//                                } else if (flag.equals("1")) {
//                                    if ("1".equals(ps)) {
//                                        Madapter.setStatus(id, 4);
//                                    } else if ("2".equals(ps)) {
//                                        Madapter.setStatus(id, 99);
//                                    } else {
//                                        Madapter.delete(id);
//                                        mAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            } else {
//                                toast("操作失败");
//                                Log.i("flag", flag);
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
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (currentItem == 0) {
//            mData.clear();
//            page = 1;
//            requestGoodsList();
//        } else if (currentItem == 1) {
//            mDataAuction.clear();
//            page2 = 1;
//            requestAuctionGoodsList();
//        }
//    }
//}
//
//
//
//
