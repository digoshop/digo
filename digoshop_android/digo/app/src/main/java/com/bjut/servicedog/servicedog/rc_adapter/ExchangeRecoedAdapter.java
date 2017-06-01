package com.bjut.servicedog.servicedog.rc_adapter;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CheckGoodRecord_data;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/3/2.
 */

public class ExchangeRecoedAdapter extends BaseQuickAdapter<CheckGoodRecord_data, BaseViewHolder> {
    public ExchangeRecoedAdapter(List<CheckGoodRecord_data> data) {
        super(R.layout.item_checkgood_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckGoodRecord_data item) {
        helper.setText(R.id.exchange_record_phonenumber, item.getNick());
        helper.setText(R.id.exchange_record_time, item.getExchangetime());
    }
}
