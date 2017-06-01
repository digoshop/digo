package com.bjut.servicedog.servicedog.rc_adapter;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CouponDownload;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/22.
 */

public class CouponDownloadListAdapter extends BaseQuickAdapter<CouponDownload, BaseViewHolder> {
    public CouponDownloadListAdapter() {
        super(R.layout.item_checkcoupunlist_r);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponDownload item) {
        helper.setText(R.id.phonenumber, item.getNick())
                .setText(R.id.time, item.getTime());
    }
}
