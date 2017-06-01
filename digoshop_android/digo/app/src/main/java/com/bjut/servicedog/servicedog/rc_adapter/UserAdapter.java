package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.ImageView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.UserManage_data;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/22.
 */

public class UserAdapter extends BaseQuickAdapter<UserManage_data, BaseViewHolder> {
    public UserAdapter() {
        super(R.layout.item_user_manage_list_start);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserManage_data item) {

        long ct = item.getCt() * 1000;
        String time = DateUtil.formatDateTime(ct);
        String status = "";

        ImageView delete = helper.getView(R.id.delete);

        if (item.getStatus() == 0) {
            status = "禁用";
            delete.setVisibility(View.GONE);
        } else {
            status = "启用";
            delete.setVisibility(View.VISIBLE);
        }

        helper.setText(R.id.usermanage_name, item.getName())
                .setText(R.id.usermanage_phonenumber, item.getMobile())
                .setText(R.id.usermanage_kind, item.getAuth())
                .setText(R.id.usermanage_status, status)
                .setText(R.id.creattime, time);
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.usermanage_status);
    }

}
