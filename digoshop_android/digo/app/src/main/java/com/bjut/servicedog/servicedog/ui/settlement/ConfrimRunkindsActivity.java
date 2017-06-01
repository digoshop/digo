package com.bjut.servicedog.servicedog.ui.settlement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ConfirmRunkindsAdapter;
import com.bjut.servicedog.servicedog.model.ProductTypeModel;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.RunKindList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.DecimalEditText;
import com.bjut.servicedog.servicedog.view.MyGridView;
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

public class ConfrimRunkindsActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private List<RunKind> rk = new ArrayList<>();
    List<RunKind> rkOk = new ArrayList<>();
    private ConfirmRunkindsAdapter mAdapter;
    private MyGridView gview;
    private TextView tv_kind;
    private TextView submit;
    private DecimalEditText consume;
    private TextView tv_title;

    private long id;

    private ProductTypeModel mProductTypeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrim_runkinds);
        init();
        requestrunkindlist();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_qrplyje));
        consume = (DecimalEditText) findViewById(R.id.ed_consume);
        submit = (TextView) findViewById(R.id.submit_confirm);
        submit.setOnClickListener(this);
        tv_kind = (TextView) findViewById(R.id.tv_kind);
        mAdapter = new ConfirmRunkindsAdapter(this, rk);
        gview = (MyGridView) findViewById(R.id.gridview);
        gview.setOnItemClickListener(this);
        gview.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_confirm:
                Intent intent = new Intent(this, MoneySalePlatActivity_r.class);
                if (tv_kind.getText().toString().length() == 0) {
                    toast("请选择商品品类");
                    return;
                }
                if (consume.getText().toString().length() == 0) {
                    toast("请填写消费金额");
                    return;
                }
                double money = Double.parseDouble(consume.getText().toString());
                if (money>=1000000) {
                    toast("本商品太贵了，改改价吧");
                    return;
                }
                mProductTypeModel =new ProductTypeModel();
                mProductTypeModel.setId(id);
                mProductTypeModel.setName(tv_kind.getText().toString());
                mProductTypeModel.setMoney(consume.getText().toString());
                intent.putExtra("productTypeModel",mProductTypeModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void requestrunkindlist() {
        showProgressDialog();
        String urlString = "business/shop_cate.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("level", "3");//优惠券批次ID必选
        map.put("page", "1");//主题
        map.put("page_length", "15");//主题
        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                        RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                        rkOk = runKindList.getData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeProgressDialog();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rkOk != null && rkOk.size() > 0) {
                            rk.addAll(rkOk);
                            mAdapter.notifyDataSetChanged();
                        }
                        closeProgressDialog();


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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mAdapter.setSeclection(position);
        tv_kind.setVisibility(View.VISIBLE);
        tv_kind.setText(rk.get(position).getName());
        id = rk.get(position).getMoid();
        mAdapter.notifyDataSetChanged();
    }

}



