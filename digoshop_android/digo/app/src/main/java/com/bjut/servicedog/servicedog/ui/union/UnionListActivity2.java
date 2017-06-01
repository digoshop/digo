package com.bjut.servicedog.servicedog.ui.union;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.base.BaseDateActivity;
import com.bjut.servicedog.servicedog.model.UnionModel;
import com.bjut.servicedog.servicedog.rc_adapter.UnionListAdapter;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/5/4.
 */

public class UnionListActivity2 extends BaseDateActivity implements SwipeRefreshLayout.OnRefreshListener {

    private UnionListAdapter unionListAdapter;

    @Override
    protected void onFailure() {
        closeProgressDialog();
        toast(Constant.CHECK_NETWORK);
        mSwipeRefreshLayout.setRefreshing(false);
        unionListAdapter.loadMoreFail();
    }

    @Override
    protected void onSuccess(String result) {
        try {
            UnionModel unionModel = JSON.parseObject(result, UnionModel.class);
            if (unionModel.getE().getCode() == 0) {
                if (unionModel.getTotal() >= 15) {
                } else {
                }
                List<UnionModel.DataBean> data = unionModel.getData();
                if (data.size() > 0) {
                    unionListAdapter.setNewData(data);
                    if (data.size() < Constant.PAGE_SIZE) {
                        unionListAdapter.loadMoreEnd(false);
                    } else {
                        unionListAdapter.loadMoreComplete();
                    }
                } else {
                    unionListAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
                }
            } else {
                unionListAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
            }
        } catch (Exception e) {
            unionListAdapter.setNewData(new ArrayList<UnionModel.DataBean>());
            e.printStackTrace();
        } finally {
            if (loadType == 0) {
                closeProgressDialog();
            } else if (loadType == 1) {
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
            }
            unionListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected String getUrls() {
        String urlString = "business/query_shop_alliance.json";
        return Constant.HOST_URL + urlString;
    }

    @Override
    protected RequestParams getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        RequestParams params = sortMapByKey(map);
        return params;
    }

    @Override
    protected BaseQuickAdapter initAdapter() {
        unionListAdapter = new UnionListAdapter();
        return unionListAdapter;
    }

    @Override
    protected void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    protected void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        UnionModel.DataBean dataBean = unionListAdapter.getData().get(position);
        toast(dataBean.getSn());
    }

    @Override
    protected void initTitle(TextView tv_title, Button btn_right) {
        tv_title.setText("测试页面");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText("测试");
    }

    @Override
    public void onClick(View v) {
    }
}
