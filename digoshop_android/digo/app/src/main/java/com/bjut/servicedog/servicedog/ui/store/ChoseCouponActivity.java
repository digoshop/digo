package com.bjut.servicedog.servicedog.ui.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ChoseCouponAdapter;
import com.bjut.servicedog.servicedog.model.ChooseCouponModel;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChoseCouponActivity extends BaseActivity {

    private ListView listView;
    private List<ChooseCouponModel.DataBean> mDataBeen = new ArrayList<>();
    private ChoseCouponAdapter mAdapter;
    private int page = 1;
    private TextView tv_title;
    private String userId = "";

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_coupon);
        init();
//        requestCouponList();

    }

    private void init() {
        setEmptyText("暂无优惠券可用");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_xuanzeyhq));

        mDataBeen = (List<ChooseCouponModel.DataBean>) getIntent().getSerializableExtra("data");

        mAdapter = new ChoseCouponAdapter(this);
        mAdapter.setList(mDataBeen);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(noDataView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChooseCouponModel.DataBean couponInfo = mDataBeen.get(i);
                Intent intent = new Intent();
                intent.putExtra("couponInfo",couponInfo);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

}
