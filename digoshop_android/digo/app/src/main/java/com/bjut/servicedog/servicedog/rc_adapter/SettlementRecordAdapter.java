package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.SaleRecord;
import com.bjut.servicedog.servicedog.po.UserInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/16.
 */

public class SettlementRecordAdapter extends BaseQuickAdapter<SaleRecord, BaseViewHolder> {


    public SettlementRecordAdapter() {
        super(R.layout.item_consume_finish_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SaleRecord item) {
        TextView tv_type = helper.getView(R.id.tv_type);
        TextView phonenumber = helper.getView(R.id.phonenumber);
        TextView money = helper.getView(R.id.money);
        TextView time = helper.getView(R.id.time);
        TextView workkinds = helper.getView(R.id.workkinds);
        TextView tv_protype = helper.getView(R.id.tv_protype);
        TextView textView24 = helper.getView(R.id.textView24);
        if (item.getIs_vip() == 0) {
            tv_type.setText("非会员");
        } else {
            tv_type.setText("会员");
        }
        UserInfo userinfo = item.getUserinfo();
        if (userinfo == null) {
            phonenumber.setText("");
        } else {
            String nick = item.getUserinfo().getNick();
            phonenumber.setText(nick);
        }


        money.setText(item.getAmount() + "");

        time.setText(item.getCreattimeStr());
        int typeId = item.getType();

        if (typeId == 1) {
            workkinds.setText(item.getKinds());
            tv_protype.setVisibility(View.GONE);
            textView24.setText("销售结算");
        } else if (typeId == 2) {
            tv_protype.setVisibility(View.VISIBLE);
            workkinds.setText(item.getPna());
            textView24.setText("认购结算");
            tv_protype.setText("兑换");
            tv_protype.setTextColor(mContext.getResources().getColor(R.color.orange));
            tv_protype.setBackgroundResource(R.drawable.border_orange_2dp);
        } else if (typeId == 4) {
            workkinds.setText(item.getKinds());
            tv_protype.setVisibility(View.GONE);
            textView24.setText("签约折扣");
        } else {
            tv_protype.setVisibility(View.VISIBLE);
            workkinds.setText(item.getPna());
            textView24.setText("认购结算");
            tv_protype.setText("拍品");
            tv_protype.setTextColor(mContext.getResources().getColor(R.color.orange));
            tv_protype.setBackgroundResource(R.drawable.border_orange_2dp);
        }
    }
}
