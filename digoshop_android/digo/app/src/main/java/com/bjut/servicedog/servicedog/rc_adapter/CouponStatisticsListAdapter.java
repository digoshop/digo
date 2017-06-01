package com.bjut.servicedog.servicedog.rc_adapter;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CouponDownload;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/22.
 */

public class CouponStatisticsListAdapter extends BaseQuickAdapter<CouponDownload, BaseViewHolder> {
    public CouponStatisticsListAdapter() {
        super(R.layout.item_coupon_statistics_list_r);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponDownload item) {
        helper.setText(R.id.phonenumber, item.getNick())
                .setText(R.id.money, item.getAmount() + "")
                .setText(R.id.time, item.getUseTimeDate());
    }
}
