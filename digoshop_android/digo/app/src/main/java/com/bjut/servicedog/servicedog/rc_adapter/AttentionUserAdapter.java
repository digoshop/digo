package com.bjut.servicedog.servicedog.rc_adapter;

import android.widget.Button;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Attention;
import com.bjut.servicedog.servicedog.po.UserInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/21.
 */

public class AttentionUserAdapter extends BaseQuickAdapter<Attention, BaseViewHolder> {
    public AttentionUserAdapter() {
        super(R.layout.item_attention_user_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Attention item) {

        TextView user_phonenumber = helper.getView(R.id.user_phonenumber);
        TextView user_date = helper.getView(R.id.user_date);
        Button fuck = helper.getView(R.id.fuck);
        helper.addOnClickListener(R.id.fuck);

        UserInfo userinfo = item.getUserinfo();
        if (null != userinfo) {
            user_phonenumber.setText(userinfo.getNick());
        }else{
            user_phonenumber.setText("");
        }
        if (null != item.getCreatime()) {
            user_date.setText(item.getCreatime());
        }

        if (item.getInvite() == 0) {
            fuck.setText("点击邀请");
            fuck.setBackgroundResource(R.color.orange);
        } else if (item.getInvite() == 1) {
            fuck.setText("已邀请");
            fuck.setBackgroundResource(R.color.line);
        } else if (item.getInvite() == 2) {
            fuck.setText("已是会员");
            fuck.setBackgroundResource(R.color.line);
        }
    }
}
