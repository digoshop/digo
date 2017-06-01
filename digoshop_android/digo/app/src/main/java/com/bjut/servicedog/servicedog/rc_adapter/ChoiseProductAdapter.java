package com.bjut.servicedog.servicedog.rc_adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/2/15.
 */

public class ChoiseProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    public ChoiseProductAdapter(List<Product> data) {
        super(R.layout.item_choise_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {

        String pppr = item.getPppr();
        String ppr = item.getPpr();

        helper.setText(R.id.tv_product_name, item.getPna());
//        helper.setText(R.id.tv_product_favorable_price, "¥" + item.getPppr());
//        helper.setText(R.id.tv_product_market_price, "¥" + item.getPpr());
        TextView tvMarketPrice = helper.getView(R.id.tv_product_market_price);


        if (StringUtils.isEmpty(pppr)) {
            helper.setText(R.id.tv_product_favorable_price, "¥" + ppr);
            tvMarketPrice.setVisibility(View.INVISIBLE);
        } else {
            helper.setText(R.id.tv_product_favorable_price, "¥" + ppr);
            tvMarketPrice.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_product_market_price, "¥" + pppr);
        }
        if (StringUtils.isEmpty(ppr)) {
            helper.setText(R.id.tv_product_favorable_price, "暂无报价");
        }else{
            helper.setText(R.id.tv_product_favorable_price, "¥" + ppr);
        }



        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        MyImageLoder.getInstance().disImageF(mContext, item.getPpi(), (ImageView) helper.getView(R.id.img_product_url),0);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getPid();
    }
}
