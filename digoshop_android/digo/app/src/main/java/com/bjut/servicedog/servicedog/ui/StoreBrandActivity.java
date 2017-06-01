package com.bjut.servicedog.servicedog.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.BrandGridViewAdapter;
import com.bjut.servicedog.servicedog.adapter.RunAdapter;
import com.bjut.servicedog.servicedog.po.Brand;
import com.bjut.servicedog.servicedog.po.BrandList;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.StoreManagerActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreBrandActivity extends BaseActivity {

    private GridView gview;
    private TextView saveView;
    private TextView tv_title;
    private String moids = "", moid = "", saveBrandSecondid = "";
    private Button btn_right;
    private List<Brand> mData = new ArrayList<>();
    private List<Brand> choiseData = new ArrayList<>();
    private BrandGridViewAdapter brandGridViewAdapter;
    private List<RunKind> saveData = new ArrayList<>();
    private RunKind runKind;
    private ListView mListView;
    private RunAdapter mRunAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_brand);
        init();

    }

    private void init() {
        moids = getIntent().getStringExtra("moids");
        choiseData = (List<Brand>) getIntent().getSerializableExtra("brandIds");

//        toast(moids);
        btn_right = (Button) findViewById(R.id.btn_right);
        mListView = (ListView) findViewById(R.id.listView);
        btn_right.setText(getString(R.string.btn_baocun));
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        gview = (GridView) findViewById(R.id.gridview);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_tianjiapinpai));

        brandGridViewAdapter = new BrandGridViewAdapter(this, mData);
        gview.setAdapter(brandGridViewAdapter);
        gview.setEmptyView(noDataView);
        saveData = (List<RunKind>) getIntent().getSerializableExtra("saveBrandtoadd");
        mRunAdapter = new RunAdapter(this);
        mListView.setAdapter(mRunAdapter);
        mRunAdapter.setList(saveData);
//        requestSecondlist();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moid = mRunAdapter.getItem(i).getMoid()+"";
                mRunAdapter.setChecked(i);
                mData.clear();
                requestBrandsList();
            }
        });

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Brand brand = mData.get(position);
//                TextView v = (TextView) view;
                Log.i("brandid", brand.getBrand_id() + "");
                for (int i = 0; i < choiseData.size(); i++) {
                    if (choiseData.get(i).getBrand_id() == brand.getBrand_id()) {
                        choiseData.remove(i);
                        brand.setChosenow(0);
//                        view.setBackgroundResource(R.drawable.weixuanzhongkuang);
                        brandGridViewAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                choiseData.add(brand);
                brand.setChosenow(1);
//                view.setBackgroundResource(R.drawable.xuanzhongkuang);
                brandGridViewAdapter.notifyDataSetChanged();
            }
        });

        mRunAdapter.setChecked(0);
        moid = mRunAdapter.getItem(0).getMoid()+"";
        requestBrandsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, StoreManagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("saveDatatostore", (Serializable) choiseData);
                intent.putExtra("from", "brand");
                setResult(Activity.RESULT_OK,intent);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                finish();
                break;
        }
    }

    private void requestBrandsList() {
        showProgressDialog();
        String urlString = "business/query_brands.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("moids", moid);
//        map.put("page", "");
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
                        try {
                            BrandList brandList = JSON.parseObject(json, BrandList.class);
                            if (brandList.getE().getCode() == 0) {
                                List<Brand> data = brandList.getData();
                                mData.addAll(data);
                                changeView(mData);
                            } else {
                                // toast(brandList.getE().getDesc());
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            closeProgressDialog();
                            brandGridViewAdapter.notifyDataSetChanged();
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

    private void changeView(List<Brand> rk) {
//        choiseData.clear();
        for (int i = 0; i < rk.size(); i++) {
            if (choiseData.size() > 0) {
                for (int j = 0; j < choiseData.size(); j++) {
                    if ((choiseData.get(j).getBrand_id() + "").equals(rk.get(i).getBrand_id() + "")) {
                        rk.get(i).setChosenow(1);
//                        choiseData.add(rk.get(i));
                    }
                }
            }
        }
        brandGridViewAdapter.notifyDataSetChanged();
    }

//    public void selftDestruct(View view) {
//        TextView v = (TextView) view;
//        Log.i("brandid", view.getId() + "");
//        for (int i = 0; i < choiseData.size(); i++) {
//
//            if (choiseData.get(i).getBrand_id() == view.getId()) {
//                choiseData.remove(i);
//                view.setBackgroundResource(R.drawable.weixuanzhongkuang);
//                return;
//            }
//        }
//        Brand brand = new Brand();
//        brand.setBrand_id(view.getId());
//        brand.setBrand_name(v.getText().toString());
//        choiseData.add(brand);
//        view.setBackgroundResource(R.drawable.xuanzhongkuang);
//    }
}
