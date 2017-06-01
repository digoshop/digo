package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.SaleRecord;
import com.bjut.servicedog.servicedog.po.UserInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/17.
 */

public class SaleStaticsAnalyseAdapter extends BaseQuickAdapter<SaleRecord, BaseViewHolder> {
    public SaleStaticsAnalyseAdapter() {
        super(R.layout.item_sale_statics_analyse_list_f);
    }

    @Override
    protected void convert(BaseViewHolder helper, SaleRecord item) {

        TextView statis_nick = helper.getView(R.id.statis_nick);
        TextView statics_date = helper.getView(R.id.statics_date);
        TextView statics_amount = helper.getView(R.id.statics_amount);
        TextView workkinds = helper.getView(R.id.workkinds);
        TextView y_vip_level = helper.getView(R.id.y_vip_level);
        TextView textView24 = helper.getView(R.id.textView24);
        TextView tv_protype = helper.getView(R.id.tv_protype);

        int is_vip = item.getIs_vip();
        if (is_vip == 1) {
            y_vip_level.setText("会员");
        } else {
            y_vip_level.setText("非会员");
        }
        UserInfo userinfo = item.getUserinfo();
        if (userinfo != null) {
            statis_nick.setText(userinfo.getNick());
        } else {
            statis_nick.setText("");
        }

        statics_date.setText(item.getCreattimeStr());
        statics_amount.setText(item.getAmount() + "");
//            holder.title4.setText("-¥"+ item.getIncentives_amount());

        int typeId = item.getType();
        if (typeId == 1) {
            workkinds.setText(item.getKinds());
            textView24.setText("销售结算");
            tv_protype.setVisibility(View.GONE);
        } else if (typeId == 2) {
            workkinds.setText(item.getPna());
            textView24.setText("认购结算");
            tv_protype.setText("兑换");
            tv_protype.setTextColor(mContext.getResources().getColor(R.color.orange));
            tv_protype.setBackgroundResource(R.drawable.border_orange_2dp);
            tv_protype.setVisibility(View.VISIBLE);
        } else if (typeId == 4) {
            workkinds.setText(item.getPna());
            textView24.setText("签约折扣");
            tv_protype.setVisibility(View.GONE);
        } else {
            tv_protype.setVisibility(View.VISIBLE);
            workkinds.setText(item.getPna());
            textView24.setText("兑换结算");
            tv_protype.setText("拍品");
            tv_protype.setTextColor(mContext.getResources().getColor(R.color.orange));
            tv_protype.setBackgroundResource(R.drawable.border_orange_2dp);
        }
    }
}
