package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ChoiseExchangeModel;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.StringUtils;

/**
 * Created by beibeizhu on 16/11/10.
 */

public class ExchangeListAdapter extends MyBaseAdpter<ChoiseExchangeModel.DataBean>{

    private MyImageLoder mMyImageLoder;

    public ExchangeListAdapter(Context context) {
        super(context);
        mMyImageLoder = MyImageLoder.getInstance();
    }

    @Override
    public int getContentView() {
        return R.layout.item_exchange;
    }

    @Override
    public void onInitView(View view, int position) {
        ImageView img_pic = get(view,R.id.img_pic);
        TextView tv_name = get(view,R.id.tv_name);
        TextView tv_bi = get(view,R.id.tv_bi);
        TextView tv_money = get(view,R.id.tv_money);
        TextView tv_subscription = get(view,R.id.tv_subscription);
        TextView tv_type = get(view,R.id.tv_type);
        TextView tv_yuan = get(view,R.id.tv_yuan);

        ChoiseExchangeModel.DataBean dataBean = getItem(position);
        String ppr = dataBean.getPpr() + "";

        mMyImageLoder.disImage(context,dataBean.getPpi(),img_pic);

        if (StringUtils.isEmpty(ppr)) {
            tv_money.setText("暂无报价");
            tv_yuan.setVisibility(View.GONE);
        } else {
            tv_money.setText(ppr);
            tv_yuan.setVisibility(View.VISIBLE);
        }

        tv_name.setText(dataBean.getPna());
        tv_bi.setText(dataBean.getEpg()+"");
        tv_subscription.setText(dataBean.getEpp()+"");
        if (dataBean.getPt()==2) {
            tv_type.setText("积分换购");
        }else if (dataBean.getPt()==1) {
            tv_type.setText("竞拍商品");
        }else{
            tv_type.setText("");
        }
    }
}
