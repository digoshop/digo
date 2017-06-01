package com.bjut.servicedog.servicedog.rc_adapter;

import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Coupon;
import com.bjut.servicedog.servicedog.po.CouponBatch;
import com.bjut.servicedog.servicedog.po.CouponType;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.grantland.widget.AutofitTextView;

import static com.bjut.servicedog.servicedog.R.id.tv_kind;

/**
 * Created by beibeizhu on 17/2/27.
 */

public class CouponNewAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {
    public CouponNewAdapter(List<Coupon> data) {
        super(R.layout.item_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {
        LinearLayout ll_left = helper.getView(R.id.ll_left);
        LinearLayout ll_middle = helper.getView(R.id.ll_middle);
        AutofitTextView atv_money = helper.getView(R.id.atv_money);
        TextView tv_type = helper.getView(R.id.tv_type);
        TextView tv_danwei = helper.getView(R.id.tv_danwei);
        AutofitTextView tv_condition = helper.getView(R.id.tv_condition);
        TextView tv_status = helper.getView(R.id.tv_status);

        TextView coupon_details = helper.getView(R.id.coupon_details);
        TextView coupon_stop = helper.getView(R.id.coupon_stop);
        ImageView delete = helper.getView(R.id.delete);
        ImageView pen = helper.getView(R.id.pen);

        CouponBatch couponBatch = item.getCouponBatch();
        CouponType couponType = item.getCouponType();
        String ctid = couponType.getCtid();
        String cbca = couponBatch.getCbca();
        String cbrStr = couponBatch.getCbrStr();
        String cbn = couponBatch.getCbn();
        String cbcf = couponBatch.getCbcf();
        Long cbvsd = couponBatch.getCbvsd();
        Long cbved = couponBatch.getCbved();
        String startDate = DateUtil.formatDate(cbvsd * 1000, DateUtil.D_YYYY_MM_DD);
        String endDate = DateUtil.formatDate(cbved * 1000, DateUtil.D_YYYY_MM_DD);
        String cbnu = couponBatch.getCbnu();
        String categoryStr = couponBatch.getCategoryStr();
        int status = couponBatch.getStatus();
        int show = couponBatch.getShow();
        int cbas = couponBatch.getCbas();
        int cbs = couponBatch.getCbs();
        String createTime = DateUtil.strFormatStr(couponBatch.getCreatTime());

        helper.setText(R.id.tv_theme, cbn)
                .setText(R.id.tv_date, startDate + "-" + endDate)
                .setText(R.id.atv_num, cbnu)
                .setText(R.id.creattime, createTime)
                .setText(tv_kind, categoryStr)
                .addOnClickListener(R.id.coupon_stop)
                .addOnClickListener(R.id.pen)
                .addOnClickListener(R.id.delete)
                .addOnClickListener(R.id.coupon_details);

        TextPaint tp = atv_money.getPaint();
        tp.setFakeBoldText(true);

        if (cbcf == null || "0".equals(cbcf)) {
            tv_condition.setVisibility(View.GONE);
        } else {
            String couponCondition = mContext.getResources().getString(R.string.coupon_condition);
            String condition = String.format(couponCondition, cbcf);
            tv_condition.setText(condition);
            tv_condition.setVisibility(View.VISIBLE);
        }

        switch (ctid) {
            case Constant.COUPON_MAN:
                tv_type.setText("满减券");
                ll_left.setBackgroundResource(R.drawable.shape_coupon_man_left);
                tv_danwei.setText("元");
                tv_danwei.setTextColor(mContext.getResources().getColor(R.color.red));
                atv_money.setText(cbca);
                atv_money.setTextColor(mContext.getResources().getColor(R.color.red));
                tv_condition.setTextColor(mContext.getResources().getColor(R.color.red));

                ll_middle.setBackgroundResource(R.color.color_FFF7F4);
                tv_condition.setVisibility(View.VISIBLE);
                break;
            case Constant.COUPON_ZHE:
                tv_type.setText("折扣券");
                ll_left.setBackgroundResource(R.drawable.shape_coupon_zhe_left);
                atv_money.setText(cbrStr);
                tv_danwei.setText("折");
                tv_danwei.setTextColor(mContext.getResources().getColor(R.color.color_61399B));
                atv_money.setTextColor(mContext.getResources().getColor(R.color.color_61399B));
                tv_condition.setTextColor(mContext.getResources().getColor(R.color.color_61399B));
                ll_middle.setBackgroundResource(R.color.color_F4F0FE);
                break;
            case Constant.COUPON_DAI:
                tv_type.setText("代金券");
                ll_left.setBackgroundResource(R.drawable.shape_coupon_dai_left);
                tv_danwei.setText("元");
                tv_danwei.setTextColor(mContext.getResources().getColor(R.color.color_FF9400));
                atv_money.setTextColor(mContext.getResources().getColor(R.color.color_FF9400));
                tv_condition.setTextColor(mContext.getResources().getColor(R.color.color_FF9400));
                atv_money.setText(cbca);
                ll_middle.setBackgroundResource(R.color.color_FDFAEC);
                break;
        }


        if (status == 0) {
            if (show == 0) {
                tv_status.setText("未发布");
                coupon_details.setVisibility(View.GONE);
            } else {
                tv_status.setText("已过期");
                coupon_details.setVisibility(View.VISIBLE);
            }

            delete.setVisibility(View.VISIBLE);
            coupon_stop.setVisibility(View.GONE);
            pen.setVisibility(View.GONE);

        } else {
            if (1 == cbas || 2 == cbas) {
                tv_status.setEnabled(false);
                coupon_stop.setVisibility(View.GONE);
                tv_status.setText("审核中");
                tv_status.setVisibility(View.VISIBLE);
                pen.setVisibility(View.GONE);
                coupon_details.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            } else if (3 == cbas || 4 == cbas) {
                coupon_stop.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
                tv_status.setText("未通过");
                pen.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                coupon_details.setVisibility(View.GONE);
            } else {
                if (cbs == 2 && status == 1) {
                    tv_status.setEnabled(false);
                    coupon_stop.setVisibility(View.VISIBLE);
                    coupon_stop.setText("启用");
                    tv_status.setText("未发布");
                    pen.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    coupon_details.setVisibility(View.GONE);
                    if (show == 1) {
                        pen.setVisibility(View.GONE);
                    }
                } else if (cbs == 1 && status == 1) {
                    tv_status.setEnabled(false);
                    tv_status.setText("已发布");
                    pen.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    coupon_details.setVisibility(View.GONE);
                    coupon_stop.setVisibility(View.VISIBLE);
                    coupon_stop.setText("禁用");
                }
            }
        }
    }
}
