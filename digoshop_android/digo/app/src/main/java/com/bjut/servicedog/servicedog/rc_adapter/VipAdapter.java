package com.bjut.servicedog.servicedog.rc_adapter;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.VipInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/21.
 */

public class VipAdapter extends BaseQuickAdapter<VipInfo, BaseViewHolder> {
    public VipAdapter() {
        super(R.layout.item_vip_manager_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipInfo item) {
        helper.setText(R.id.vip_list_item_name, item.getUser_name())
                .setText(R.id.vip_list_item_phonenumber, item.getMobile())
                .setText(R.id.vip_list_item_id, item.getVip_code())
                .setText(R.id.vip_list_item_class, item.getVip_level_name())
                .setText(R.id.vip_list_item_time, item.getCreatimestr())
                .setText(R.id.vip_list_item_lock, item.getStatus());
    }
}
