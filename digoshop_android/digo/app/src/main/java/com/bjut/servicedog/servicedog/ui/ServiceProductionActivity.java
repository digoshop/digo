//package com.bjut.servicedog.servicedog.ui;
//
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.adapter.ServiceProductionAdapter;
//import com.bjut.servicedog.servicedog.po.ServiceProduction;
//import com.bjut.servicedog.servicedog.po.ServiceProduction_data;
//import com.bjut.servicedog.servicedog.utils.Constant;
//import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;
//import com.bjut.servicedog.servicedog.view.My2BDialog;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshGridView;
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
//public class ServiceProductionActivity extends BaseActivity implements ListItemClickHelp {
//    private ServiceProductionAdapter mAdapter;
//    private List<ServiceProduction_data> mData = new ArrayList<>();
//    private long pid;
//    private TextView tv_title;
//    private TextView checkmore;
//    private Button btn_right;
//    private ListItemClickHelp callback;
//    private PullToRefreshGridView gridView;
//    private int page = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_service_production);
//        init();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mData.clear();
//        page = 1;
//        requestlist();
//    }
//
//    private void init() {
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.title_fwcp));
//        btn_right = (Button) findViewById(R.id.btn_right);
//        btn_right.setText(getString(R.string.btn_xz));
//        btn_right.setVisibility(View.VISIBLE);
//        btn_right.setOnClickListener(this);
//        gridView = (PullToRefreshGridView) findViewById(R.id.service_gridview);
//        mAdapter = new ServiceProductionAdapter(this, mData, this);
//        gridView.setAdapter(mAdapter);
//        gridView.setEmptyView(noDataView);
//        gridView.setMode(PullToRefreshBase.Mode.BOTH);
//        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
//            // Pulling Down
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<GridView> refreshView) {
//                mData.clear();
//                page = 1;
//                requestlist();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//            }
//
//            // Pulling Up
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<GridView> refreshView) {
//                page++;
//                requestlist();
//                mAdapter.notifyDataSetChanged();
//                new FinishRefresh().execute();
//            }
//        });
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
//            gridView.onRefreshComplete();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_right:
//                myIntentR(NewAddProductionActivity.class);
//                break;
//        }
//    }
//
//    @Override
//    public void onclick(View item, View widget, int position, int which) {
//        switch (which) {
//            case R.id.service_production_delete:
//                pid = mData.get(position).getPid();
//                //toast(pid+"");
//                showAlertDialog(mData, position);
//                break;
//        }
//    }
//
//    private void requestDelete(long pid, final int position) {
//        showProgressDialog();
//        String urlString = "product/del_product.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        Map<String, String> map = new HashMap<>();
//        map.put("pid", pid + "");
//        map.put("sid", pref.getString("sid", ""));
//        map.put("pt", "3");
//        map.put("ps", "99");
//        RequestParams params = sortMapByKey(map);
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
//                            JSONObject jsonObject = new JSONObject(json);
//                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
//                                mData.remove(position);
//                                mAdapter.notifyDataSetChanged();
//                                toast("删除成功");
//                            } else {
//                                toast("删除失败");
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
//    private void requestlist() {
//        showProgressDialog();
//        String urlString = "product/getProductsByTargetId.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("targetId", pref.getString("sid", ""));
//        Log.i("result", pref.getString("sid", ""));
//        map.put("targetType", "2");
//        //map.put("targetType",2+"");
//        map.put("productType", "3");
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
//                            ServiceProduction productionlist = JSON.parseObject(json, ServiceProduction.class);
//                            if (productionlist.getE().getCode() == 0) {
//                                List<ServiceProduction_data> data = productionlist.getData();
//                                mData.addAll(data);
//                                mAdapter.notifyDataSetChanged();
//                            } else {
//                                toast(productionlist.getE().getDesc());
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
//    public void showAlertDialog(final List<?> data, final int position) {
//        if (mMyDialog == null) {
//            mMyDialog =new My2BDialog(this);
//        }
//        mMyDialog.setTitle("温馨提示");
//        mMyDialog.setMessage("确定要删除吗？");
//        mMyDialog.setYesOnclickListener("确定", new My2BDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick() {
//                requestDelete(pid, position);
//                mMyDialog.dismiss();
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
//}
