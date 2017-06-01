package com.bjut.servicedog.servicedog.rc_adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/2/15.
 */

public class AuctionProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {

    public AuctionProductAdapter(List<Product> data) {
        super(R.layout.item_exchange_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_name, item.getPna());
        helper.setText(R.id.tv_money, "¥" + item.getApp());
        helper.setText(R.id.tv_old, "¥" + item.getPpr());
        helper.setText(R.id.tv_exchange_date, item.getApsdTime());
        helper.setText(R.id.title_date, "开拍日期:");
        helper.addOnClickListener(R.id.img_delete)
                .addOnClickListener(R.id.img_update)
                .addOnClickListener(R.id.tv_is_use);

        TextView tvStatus = helper.getView(R.id.tv_status);
        TextView tvMarketPrice = helper.getView(R.id.tv_old);
        TextView btnUse = helper.getView(R.id.tv_is_use);
        ImageView imgUpdate = helper.getView(R.id.img_update);
        ImageView img_delete = helper.getView(R.id.img_delete);

        int status = item.getAps();

        switch (status) {
            case Constant.PRODUCT_STATUS_AUDIT:
                tvStatus.setText("审核中");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_NOT_PASSED:
                tvStatus.setText("已驳回");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.VISIBLE);
                break;
            case Constant.PRODUCT_STATUS_PASSED:
                tvStatus.setText("已通过");
                btnUse.setVisibility(View.VISIBLE);
                btnUse.setText("上架");
                imgUpdate.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.VISIBLE);
                break;
            case Constant.PRODUCT_STATUS_AUCTION_NOTSTART:
                tvStatus.setText("未开始");
                btnUse.setVisibility(View.GONE);
                btnUse.setText("下架");
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_AUCTION:
                tvStatus.setText("竞拍中");
                btnUse.setText("下架");
                btnUse.setVisibility(View.VISIBLE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                break;
            case Constant.PRODUCT_STATUS_AUCTION_STOP:
                tvStatus.setText("已结束");
                btnUse.setText("下架");
                btnUse.setVisibility(View.GONE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
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
                btnUse.setText("上架");
                btnUse.setVisibility(View.VISIBLE);
                imgUpdate.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
                break;
        }

        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        MyImageLoder.getInstance().disImageF(mContext, item.getPpi(), (ImageView) helper.getView(R.id.iv_exchange_image), 0);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getPid();
    }
}
