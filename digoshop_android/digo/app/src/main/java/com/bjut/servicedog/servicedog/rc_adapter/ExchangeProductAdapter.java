package com.bjut.servicedog.servicedog.rc_adapter;

import android.graphics.Paint;
import android.view.View;
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

public class ExchangeProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    public ExchangeProductAdapter() {
        super(R.layout.item_exchange_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_name, item.getPna());
        helper.setText(R.id.tv_money, "¥" + item.getEpp() + "+" + item.getEpg() + "币");
        helper.setText(R.id.tv_old, "原价" + item.getPpr()+"元");
        helper.setText(R.id.tv_exchange_date, item.getEndTime());
        helper.setText(R.id.title_date, "结束日期:");
        helper.addOnClickListener(R.id.img_delete)
                .addOnClickListener(R.id.img_update)
                .addOnClickListener(R.id.tv_is_use)
                .addOnClickListener(R.id.btn_is_tui);

        TextView tvStatus = helper.getView(R.id.tv_status);
        TextView tvMarketPrice = helper.getView(R.id.tv_old);
        TextView btnUse = helper.getView(R.id.tv_is_use);
        TextView btn_is_tui = helper.getView(R.id.btn_is_tui);
        ImageView imgUpdate = helper.getView(R.id.img_update);
        ImageView img_delete = helper.getView(R.id.img_delete);

        int status = item.getEps();
        String ppr = item.getPpr();
        int prcmd = item.getPrcmd();

        if (prcmd== Constant.TUI_NO) {
            btn_is_tui.setText(mContext.getString(R.string.btn_tuijian));
        }else{
            btn_is_tui.setText(mContext.getString(R.string.btn_cancle));
        }
        if (StringUtils.isEmpty(ppr)) {
            helper.setText(R.id.tv_old, "暂无报价");
        } else {
            helper.setText(R.id.tv_old, "原价" + ppr + "元");
        }

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
                btn_is_tui.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
                break;
            case Constant.PRODUCT_STATUS_EXCHANGE:
                tvStatus.setText("兑换中");
                btnUse.setVisibility(View.VISIBLE);
                btnUse.setText(mContext.getString(R.string.btn_xiajia));
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                btn_is_tui.setVisibility(View.VISIBLE);
                break;
            case Constant.PRODUCT_STATUS_EXCHANGE_STOP:
                tvStatus.setText("已结束");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
                btn_is_tui.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_EXCHANGE_NOTSTART:
                tvStatus.setText("未开始");
                btnUse.setText(mContext.getString(R.string.btn_xiajia));
                btnUse.setVisibility(View.VISIBLE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                btn_is_tui.setVisibility(View.VISIBLE);
                break;
//            case Constant.PRODUCT_STATUS_EXCHANGE_SHELF:
//                tvStatus.setText("已下架");
//                btnUse.setText("上架");
//                btnUse.setVisibility(View.VISIBLE);
//                imgUpdate.setVisibility(View.GONE);
//                img_delete.setVisibility(View.VISIBLE);
//                break;
            default:
                tvStatus.setText("已下架");
                btnUse.setText(mContext.getString(R.string.btn_shangjia));
                btnUse.setVisibility(View.VISIBLE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
                btn_is_tui.setVisibility(View.GONE);
                break;
        }

        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        MyImageLoder.getInstance().disImageF(mContext, item.getPpi(), (ImageView) helper.getView(R.id.iv_exchange_image),0);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getPid();
    }
}
