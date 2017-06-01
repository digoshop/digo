package com.bjut.servicedog.servicedog.rc_adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/2/15.
 */

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    public ProductAdapter() {
        super(R.layout.item_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {

        TextView tvStatus = helper.getView(R.id.tv_product_status);
        TextView tvMarketPrice = helper.getView(R.id.tv_product_market_price);
        Button btnUse = helper.getView(R.id.btn_is_use);
        Button btn_is_tui = helper.getView(R.id.btn_is_tui);
        ImageView imgUpdate = helper.getView(R.id.img_update);
        ImageView img_delete = helper.getView(R.id.img_delete);

        String pppr = item.getPppr();
        String ppr = item.getPpr();
        int status = item.getPst();
        int prcmd = item.getPrcmd();

        if (prcmd== Constant.TUI_NO) {
            btn_is_tui.setText(mContext.getString(R.string.btn_tuijian));
        }else{
            btn_is_tui.setText(mContext.getString(R.string.btn_cancle));
        }

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

        helper.setText(R.id.tv_product_name, item.getPna());


        helper.addOnClickListener(R.id.img_delete)
                .addOnClickListener(R.id.img_update)
                .addOnClickListener(R.id.btn_is_use)
                .addOnClickListener(R.id.btn_is_tui);

        switch (status) {
            case Constant.PRODUCT_STATUS_AUDIT:
                tvStatus.setText("审核中");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                btn_is_tui.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_NOT_PASSED:
                tvStatus.setText("已驳回");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.VISIBLE);
                btn_is_tui.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_PASSED:
                tvStatus.setText("已通过");
                btnUse.setVisibility(View.VISIBLE);
                btnUse.setText(mContext.getString(R.string.btn_shangjia));
                imgUpdate.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.VISIBLE);
                btn_is_tui.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_SHELVES:
                tvStatus.setText("上架中");
                btnUse.setVisibility(View.VISIBLE);
                btnUse.setText(mContext.getString(R.string.btn_xiajia));
                btn_is_tui.setVisibility(View.VISIBLE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_SHELF:
                tvStatus.setText("已下架");
                btnUse.setVisibility(View.VISIBLE);
                btn_is_tui.setVisibility(View.GONE);
                btnUse.setText(mContext.getString(R.string.btn_shangjia));
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
                break;
            case Constant.PRODUCT_STATUS_DELETE:

                break;
        }

        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        MyImageLoder.getInstance().disImageF(mContext, item.getPpi(), (ImageView) helper.getView(R.id.img_product_url), 0);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getPid();
    }
}
