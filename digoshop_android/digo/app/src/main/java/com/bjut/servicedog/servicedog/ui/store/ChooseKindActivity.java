package com.bjut.servicedog.servicedog.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ChoseKindsAdapter;
import com.bjut.servicedog.servicedog.adapter.RunKindGridViewAdapter;
import com.bjut.servicedog.servicedog.po.CommitEvent;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.RunKindList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择经营品类
 */
public class ChooseKindActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout left, right, leftline, rightline;
    private TextView righttext, lefttext, tv_next;
    private GridView gview, gview2;
    private RunKindGridViewAdapter runKindGridViewAdapter, runKindGridViewAdapter2;
    private List<RunKind> runKinds = new ArrayList<>();
    private List<RunKind> runKinds2 = new ArrayList<>();
    private List<RunKind> runKindsOk = new ArrayList<>();
    private int type = 1, page = 1, page2 = 1, firstpage = 1, firstpage2 = 1;
    private ListView listView, listView2;
    private List<RunKind> saleKinds = new ArrayList<>();
    private List<RunKind> serviceKinds = new ArrayList<>();
    private List<String> saveSaleKinds = new ArrayList<>();
    private List<String> saveServiceKinds = new ArrayList<>();

    private ChoseKindsAdapter mAdapter, mAdapter2;
    private String moid = "", moid2 = "";
    private int flag;
    private TextView tv_title;
    private Button btn_right;
    private My2BDialog myDialog;
    private String sid = "";

    //    private SubmitInfo submitInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_kind);
        EventBus.getDefault().register(this);
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commitOk(CommitEvent commitEvent) {
        if (commitEvent.isCommitOK()) {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        flag = getIntent().getIntExtra("audit", 0);
        sid = getIntent().getStringExtra("sid");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xzpl));
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText(getString(R.string.btn_xyb));
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        lefttext = (TextView) findViewById(R.id.lefttext);
        righttext = (TextView) findViewById(R.id.righttext);
        left = (LinearLayout) findViewById(R.id.left);
        right = (LinearLayout) findViewById(R.id.right);
        leftline = (LinearLayout) findViewById(R.id.leftline);
        rightline = (LinearLayout) findViewById(R.id.rightline);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listview);
        mAdapter = new ChoseKindsAdapter(this, saleKinds);
        requestFirstKindsList();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView2 = (ListView) findViewById(R.id.listview2);
        mAdapter2 = new ChoseKindsAdapter(this, serviceKinds);
        listView2.setAdapter(mAdapter2);
        listView2.setOnItemClickListener(this);

        gview = (GridView) findViewById(R.id.gridview);
        gview2 = (GridView) findViewById(R.id.gridview2);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String n = runKinds.get(position).getMoid() + "";
                for (int i = 0; i < saveSaleKinds.size(); i++) {
                    if (saveSaleKinds.get(i).equals(n)) {
                        saveSaleKinds.remove(i);
                        runKinds.get(position).setChosenow(0);
                        runKindGridViewAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                saveSaleKinds.add(n);
                runKinds.get(position).setChosenow(1);
                runKindGridViewAdapter.notifyDataSetChanged();
            }
        });
        gview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String n = runKinds2.get(position).getMoid() + "";
                for (int i = 0; i < saveServiceKinds.size(); i++) {
                    if (saveServiceKinds.get(i).equals(n)) {
                        saveServiceKinds.remove(i);
                        runKinds2.get(position).setChosenow(0);
                        runKindGridViewAdapter2.notifyDataSetChanged();
                        return;
                    }
                }
                saveServiceKinds.add(n);
                runKinds2.get(position).setChosenow(1);
                runKindGridViewAdapter2.notifyDataSetChanged();
            }
        });

        runKindGridViewAdapter = new RunKindGridViewAdapter(this, runKinds);
        runKindGridViewAdapter2 = new RunKindGridViewAdapter(this, runKinds2);

        gview.setAdapter(runKindGridViewAdapter);
        gview2.setAdapter(runKindGridViewAdapter2);
        //  requestRunKindsList();
        gview2.setVisibility(View.GONE);
        listView2.setVisibility(View.GONE);
        if (flag == 1) {
            String shop_reason = pref.getString("shop_reason", "");
            if (mMyDialog == null) {
                mMyDialog = new My2BDialog(mContext);
            }
            mMyDialog.setTitle("温馨提示");
            if (shop_reason.equals("")) {
                mMyDialog.setMessage("您的资质认证审核未通过! 请重新填写信息进行提交");
            } else {
                mMyDialog.setMessage("您的资质认证审核未通过，原因是:" + shop_reason + "! 请重新填写信息进行提交");
            }
            mMyDialog.setYesOnclickListener("确认修改", new My2BDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    mMyDialog.dismiss();
                }
            });
            mMyDialog.setNoOnclickListener("退出系统", new My2BDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    mMyDialog.dismiss();
                    editor.putString("uid", "");
                    editor.putString("ifLogin", "false");
                    editor.putString("sid", "");
                    editor.commit();
                    ActivityCollector.finishAll();
                    System.exit(0);
                }
            });
            mMyDialog.show();
        }

        if (flag == 2) {
            if (mMyDialog == null) {
                mMyDialog = new My2BDialog(mContext);
            }
            mMyDialog.setTitle("温馨提示");
            mMyDialog.setMessage("您尚未完成资质认证,暂时无法登录");
            mMyDialog.setYesOnclickListener("继续完成", new My2BDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    mMyDialog.dismiss();
                }
            });
            mMyDialog.setNoOnclickListener("退出系统", new My2BDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    mMyDialog.dismiss();
                    editor.putString("uid", "");
                    editor.putString("ifLogin", "false");
                    editor.putString("sid", "");
                    editor.commit();
                    ActivityCollector.finishAll();
                    System.exit(0);
                }
            });
            mMyDialog.show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("iiiiii", "123");
        if (type == 1) {
            moid = saleKinds.get(position).getMoid() + "";
            runKinds.clear();
            for (int i = 0; i < saleKinds.size(); i++) {

                saleKinds.get(i).setChosenow(0);
            }
            saleKinds.get(position).setChosenow(1);
            mAdapter.notifyDataSetChanged();
        } else if (type == 2) {
            moid2 = serviceKinds.get(position).getMoid() + "";
            runKinds2.clear();
            for (int i = 0; i < serviceKinds.size(); i++) {
                serviceKinds.get(i).setChosenow(0);
            }
            serviceKinds.get(position).setChosenow(1);
            mAdapter2.notifyDataSetChanged();
        }
        requestRunKindsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                if (type == 2 && getSaveServiceKinds().equals("")) {
                    left.setTag(1);
                    right.setTag(0);
                    gview.setVisibility(View.VISIBLE);
                    gview2.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView2.setVisibility(View.GONE);
                    type = 1;
                    leftline.setBackgroundResource(R.color.orange);
                    lefttext.setTextColor(getResources().getColor(R.color.orange));
                    rightline.setBackgroundResource(R.color.line);
                    righttext.setTextColor(getResources().getColor(R.color.word_color));
                    runKinds.clear();
                    requestRunKindsList();

                } else if (type == 2 && (!getSaveServiceKinds().equals(""))) {

                    myDialog = new My2BDialog(mContext);
                    myDialog.setTitle("温馨提示");
                    myDialog.setMessage("您只能选择一种经营类型，继续查看将取消现在类型选择");
                    myDialog.setYesOnclickListener("继续查看", new My2BDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            if (type == 2) {
                                saveServiceKinds.clear();
                                runKinds2.clear();
                                runKindGridViewAdapter2.notifyDataSetChanged();
                                page2 = 1;
                                left.setTag(1);
                                right.setTag(0);
                                gview2.setVisibility(View.GONE);
                                gview.setVisibility(View.VISIBLE);
                                listView2.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                type = 1;
                                leftline.setBackgroundResource(R.color.orange);
                                lefttext.setTextColor(getResources().getColor(R.color.orange));
                                rightline.setBackgroundResource(R.color.line);
                                righttext.setTextColor(getResources().getColor(R.color.word_color));

                            }
                            myDialog.dismiss();
                        }
                    });
                    myDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                }

                break;
            case R.id.right:
                if (type == 1 && getSaveSaleKinds().equals("")) {
                    right.setTag(1);
                    left.setTag(0);
                    gview2.setVisibility(View.VISIBLE);
                    gview.setVisibility(View.GONE);
                    listView2.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    type = 2;
                    leftline.setBackgroundResource(R.color.line);
                    lefttext.setTextColor(getResources().getColor(R.color.word_color));
                    rightline.setBackgroundResource(R.color.orange);
                    righttext.setTextColor(getResources().getColor(R.color.orange));

                    if (serviceKinds.size() == 0) {
                        requestFirstKindsList();
                    } else {
                        runKinds2.clear();
                        requestRunKindsList();
                    }
                } else if (type == 1 && (!getSaveSaleKinds().equals(""))) {
                    myDialog = new My2BDialog(mContext);
                    myDialog.setTitle("温馨提示");
                    myDialog.setMessage("您只能选择一种经营类型，继续查看将取消现在类型选择");
                    myDialog.setYesOnclickListener("继续查看", new My2BDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            if (type == 1) {
                                saveSaleKinds.clear();
                                runKinds.clear();
                                runKindGridViewAdapter.notifyDataSetChanged();
                                page = 1;
                                right.setTag(1);
                                left.setTag(0);
                                gview.setVisibility(View.GONE);
                                gview2.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                listView2.setVisibility(View.VISIBLE);
                                type = 2;
                                leftline.setBackgroundResource(R.color.line);
                                lefttext.setTextColor(getResources().getColor(R.color.word_color));
                                rightline.setBackgroundResource(R.color.orange);
                                righttext.setTextColor(getResources().getColor(R.color.orange));
                                if (serviceKinds.size() == 0)
                                    requestFirstKindsList();
                            }
                            myDialog.dismiss();
                        }
                    });
                    myDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                }


                break;
            case R.id.btn_right:
//                if (getSaveSaleKinds().equals("") && getSaveServiceKinds().equals("")) {
//                    toast("请选择类别");
//                } else if (type == 1 && getSaveServiceKinds().equals("")) {
//                    editor.putString("shopkind", "1");
//                    editor.putString("runkinds", getSaveSaleKinds());
//                    editor.commit();
//                    Intent intent = new Intent(this, SubmitCorporationActivity.class);
//                    intent.putExtra("flag", flag);
//                    startActivity(intent);
//                } else if (type == 2 && getSaveSaleKinds().equals("")) {
//                    Log.i("11111servicekindIdstr", getSaveServiceKinds());
//                    editor.putString("shopkind", "2");
//                    editor.putString("runkinds", getSaveServiceKinds());
//                    editor.commit();
//                    Intent intent = new Intent(this, SubmitCorporationActivity.class);
//                    intent.putExtra("flag", flag);
//                    startActivity(intent);
//                    // myIntentR(SubmitCorporationActivityNew.class);
//                }
                if (getSaveSaleKinds().equals("") && getSaveServiceKinds().equals("")) {
                    toast("请选择类别");
                } else if (type == 1 && getSaveServiceKinds().equals("")) {
//                    editor.putString("shopkind", "1");
//                    editor.putString("runkinds", getSaveSaleKinds());
//                    editor.commit();
                    Intent intent = new Intent(this, SubmitCorporationActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("sid", sid);
                    intent.putExtra("shopkind", "1");
                    intent.putExtra("runkinds", getSaveSaleKinds());
                    Log.i("choose", "选择：shopkind=======1");
                    Log.i("choose", "选择：runkinds=======" + getSaveSaleKinds());
                    startActivity(intent);
                } else if (type == 2 && getSaveSaleKinds().equals("")) {
//                    Log.i("11111servicekindIdstr", getSaveServiceKinds());
//                    editor.putString("shopkind", "2");
//                    editor.putString("runkinds", getSaveServiceKinds());
//                    editor.commit();
                    Intent intent = new Intent(this, SubmitCorporationActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("sid", sid);
                    intent.putExtra("shopkind", "2");
                    intent.putExtra("runkinds", getSaveServiceKinds());
                    Log.i("choose", "选择：shopkind=======2");
                    Log.i("choose", "选择：runkinds=======" + getSaveServiceKinds());
                    startActivity(intent);
                    // myIntentR(SubmitCorporationActivityNew.class);
                }
                break;
        }
    }

//    public void selftDestruct(View view) {
//        String n = view.getId() + "";
//        if (type == 1) {
//            for (int i = 0; i < saveSaleKinds.size(); i++) {
//                if (saveSaleKinds.get(i).equals(n)) {
//                    saveSaleKinds.remove(i);
//                    view.setBackgroundResource(R.drawable.weixuanzhongkuang);
//                    return;
//                }
//            }
//            saveSaleKinds.add(n);
//        } else {
//            for (int i = 0; i < saveServiceKinds.size(); i++) {
//                if (saveServiceKinds.get(i).equals(n)) {
//                    saveServiceKinds.remove(i);
//                    view.setBackgroundResource(R.drawable.weixuanzhongkuang);
//                    return;
//                }
//            }
//            saveServiceKinds.add(n);
//        }
//        view.setBackgroundResource(R.drawable.xuanzhongkuang);
//    }


    private String getSaveSaleKinds() {
        String s = "";
        for (int i = 0; i < saveSaleKinds.size(); i++) {
            s += saveSaleKinds.get(i) + ",";
        }
        if (s.equals(""))
            return s;
        String a = s;
        return a.substring(0, s.length() - 1);
    }

    private String getSaveServiceKinds() {
        String s = "";
        for (int i = 0; i < saveServiceKinds.size(); i++) {
            s += saveServiceKinds.get(i) + ",";
        }
        if (s.equals(""))
            return s;
        String a = s;
        return a.substring(0, s.length() - 1);
    }

    private void requestFirstKindsList() {

        showProgressDialog();
        String urlString = "business/queryCategory.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", type + "");
        map.put("level", "1");
        map.put("moid", "");
//        if (type == 1) {
//            map.put("page", firstpage + "");
//        } else {
//            map.put("page", firstpage2 + "");
//        }
        map.put("page_length", "30");

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
                        Log.i("result2", json);
                        try {
                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                            if (runKindList.getE().getCode() == 0) {
                                runKindsOk = runKindList.getData();
                                if (type == 1) {
                                    saleKinds.addAll(runKindsOk);
                                    runKindsOk.clear();
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    serviceKinds.addAll(runKindsOk);
                                    runKindsOk.clear();
                                    mAdapter2.notifyDataSetChanged();
                                }

                            } else {
                                toast(runKindList.getE().getDesc());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
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

    private void requestRunKindsList() {
        showProgressDialog();
        String urlString = "business/queryCategory.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", type + "");
        map.put("level", "2");
//        if (type == 1) {
//            map.put("page", page + "");
//            map.put("moid", moid);
//
//        } else {
//            map.put("page", page2 + "");
//            map.put("moid", moid2);
//
//        }
        map.put("page_length", "30");
        if (type == 1) {
            map.put("moid", moid);

        } else {
            map.put("moid", moid2);
        }

        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(responseInfo.result);
                        String json = responseInfo.result;
                        Log.i("result3", json);
                        try {
                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                            if (runKindList.getE().getCode() == 0) {
                                runKindsOk = runKindList.getData();
                                if (type == 1) {
                                    runKinds.addAll(runKindsOk);
                                    runKindsOk.clear();

                                    changeView();
//                                    initChangeView();
//                                    runKindGridViewAdapter.notifyDataSetChanged();
                                } else {
                                    runKinds2.addAll(runKindsOk);
                                    runKindsOk.clear();
//                                    runKindGridViewAdapter2.notifyDataSetChanged();

                                    changeView();
//                                    initChangeView();
                                }

                            } else {
                                toast(runKindList.getE().getDesc());
                            }
                        } catch (Exception e) {
                            if (type == 1) {
                                runKindGridViewAdapter.notifyDataSetChanged();
                            } else {
                                runKindGridViewAdapter2.notifyDataSetChanged();
                            }
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
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

    private void changeView() {
        if (type == 1) {
            for (int i = 0; i < runKinds.size(); i++) {
                for (int j = 0; j < saveSaleKinds.size(); j++) {
                    if (saveSaleKinds.get(j).equals(runKinds.get(i).getMoid() + "")) {
                        runKinds.get(i).setChosenow(1);
                    }
                }
            }
            runKindGridViewAdapter.notifyDataSetChanged();

        } else {
            for (int i = 0; i < runKinds2.size(); i++) {
                for (int j = 0; j < saveServiceKinds.size(); j++) {
                    if (saveServiceKinds.get(j).equals(runKinds2.get(i).getMoid() + "")) {
                        runKinds2.get(i).setChosenow(1);
                    }
                }
            }
            runKindGridViewAdapter2.notifyDataSetChanged();
        }

    }
//    private void getSubmitInfo() {
//        String urlString = "business/query_submit_info.json";
//        urlString = String.format(urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                Log.i("result4",responseInfo.result);
//                Log.i("result4",saveServiceKinds.size()+"");
//                String json=responseInfo.result;
//                submitInfo = JSON.parseObject(json, SubmitInfo.class);
//                saveSaleKinds.addAll(submitInfo.getData().getShopOperateIds());
//                saveServiceKinds.addAll(submitInfo.getData().getShopOperateIds());
//                saveServiceKinds.size();
//                
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                toast(Constant.CHECK_NETWORK);
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
//            listView.onRefreshComplete();
//            listView2.onRefreshComplete();
//
//        }
//    }

}
