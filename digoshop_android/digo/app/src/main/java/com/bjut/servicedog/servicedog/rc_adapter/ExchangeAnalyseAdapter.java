package com.bjut.servicedog.servicedog.rc_adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.GoodsExchangeStat_data;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/16.
 */

public class ExchangeAnalyseAdapter extends BaseQuickAdapter<GoodsExchangeStat_data, BaseViewHolder> {
    public ExchangeAnalyseAdapter() {
        super(R.layout.item_goods_exchange_statics_f);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsExchangeStat_data item) {

        ImageView exchangeStatis_image = helper.getView(R.id.exchangeStatis_image);
        TextView exchangeStatis_name = helper.getView(R.id.exchangeStatis_name);
        TextView exchangeStatis_shelian = helper.getView(R.id.exchangeStatis_shelian);
        TextView goods_society_value = helper.getView(R.id.goods_society_value);
        TextView exchangeStatis_date = helper.getView(R.id.exchangeStatis_date);
        TextView exchangeStatis_status = helper.getView(R.id.exchangeStatis_status);
        TextView exchangeStatis_number = helper.getView(R.id.exchangeStatis_number);
        TextView exchangeStatis_getNumber = helper.getView(R.id.exchangeStatis_getNumber);
        TextView exchangeStatis_releaseNumber = helper.getView(R.id.exchangeStatis_releaseNumber);
        TextView exchangeStatis_originalvalue = helper.getView(R.id.exchangeStatis_originalvalue);
        TextView exchangeStatis_money = helper.getView(R.id.exchangeStatis_money);
        TextView exchangeStatis_discount = helper.getView(R.id.exchangeStatis_discount);
        TextView exchangeStatis_value = helper.getView(R.id.exchangeStatis_value);

        MyImageLoder.getInstance().disImage(mContext,item.getPpi(),exchangeStatis_image);//商品图片
        int status = item.getEps();
        exchangeStatis_name.setText(item.getPna());//商品名称
        exchangeStatis_shelian.setText("+" + item.getEpg() + "");//所需迪币

        goods_society_value.setText(item.getPpr() + "");////商品原价
        exchangeStatis_date.setText(DateUtil.strFormatStr(item.getEndDate()));//兑换结束时间
        if (status == 5) {
            exchangeStatis_status.setText("已结束");
        } else if (status == 99) {
            exchangeStatis_status.setText("已禁用");
        } else if (status == 100) {
            exchangeStatis_status.setText("已删除");
        }
        exchangeStatis_number.setText("(" + item.getEpt() + ")");//兑换数量
        exchangeStatis_getNumber.setText("(" + item.getEpsat() + ")");//领取数量
        exchangeStatis_releaseNumber.setText("(" + item.getPnu() + ")");//发行数量
        exchangeStatis_originalvalue.setText("(" + item.getAmount() + ")");//原价金额
        exchangeStatis_money.setText("(" + item.getEamount() + ")");//兑换金额
        exchangeStatis_discount.setText("(" + item.getRate() + ")");//折扣率
        exchangeStatis_value.setText(item.getEpp() + "");//兑换价格
    }
}
