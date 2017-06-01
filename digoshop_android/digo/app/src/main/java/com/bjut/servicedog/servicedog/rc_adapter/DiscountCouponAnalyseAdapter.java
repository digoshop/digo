package com.bjut.servicedog.servicedog.rc_adapter;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.DiscountCouponAna_data;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/16.
 */

public class DiscountCouponAnalyseAdapter extends BaseQuickAdapter<DiscountCouponAna_data, BaseViewHolder> {

    public DiscountCouponAnalyseAdapter() {
        super(R.layout.item_discount_coupon_analyse_list_f);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscountCouponAna_data item) {
        helper.setText(R.id.release_count, item.getIssue_count() + "");
        helper.setText(R.id.get_count, item.getDown_count() + "");
        helper.setText(R.id.use_number, item.getUsed() + "");
        helper.setText(R.id.nouse_count, item.getNot_used() + "");
        helper.setText(R.id.sale_money, item.getSales() + "");
        helper.setText(R.id.download_ratio, item.getDown_ratio() + "");
        helper.setText(R.id.consume_ratio, item.getUse_ratio() + "");
        helper.setText(R.id.coupon_average, item.getAverage() + "");
        helper.setText(R.id.coupon_name, item.getName() + "");
    }
}
