package com.bjut.servicedog.servicedog.rc_adapter;

import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CheckAuctionRecord_data;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/3/2.
 */

public class AuctionRecoedAdapter extends BaseQuickAdapter<CheckAuctionRecord_data, BaseViewHolder> {
    public AuctionRecoedAdapter(List<CheckAuctionRecord_data> data) {
        super(R.layout.item_check_auction_record_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckAuctionRecord_data item) {
        helper.setText(R.id.auction_record_phonenumber, item.getNk());
        helper.setText(R.id.auction_record_time, item.getCtime());
        helper.setText(R.id.auction_record_status, item.getPpr());
        TextView tv_status = helper.getView(R.id.tv_status);
        int itemCount = getItemCount();
        if (itemCount == 0) {
            tv_status.setText("领先");
        } else {
            tv_status.setText("出局");
        }
        tv_status.setTextColor(mContext.getResources().getColor(R.color.orange));
        tv_status.setBackgroundResource(R.drawable.border_orange_2dp);
    }
}
