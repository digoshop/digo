package com.bjut.servicedog.servicedog.rc_adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Coupon;
import com.bjut.servicedog.servicedog.po.CouponBatch;
import com.bjut.servicedog.servicedog.po.CouponType;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/2/27.
 */

public class CouponAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {
    public CouponAdapter(List<Coupon> data) {
        super(R.layout.item_moneycoupon_manage_r, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {
        TextView moneydesc = helper.getView(R.id.moneydesc);
        TextView couponkind = helper.getView(R.id.couponkind);
        TextView moneytitle = helper.getView(R.id.moneytitle);
        TextView condition = helper.getView(R.id.condition);
        TextView desc = helper.getView(R.id.desc);
        TextView tv_textType = helper.getView(R.id.tv_textType);
        TextView coupon_details = helper.getView(R.id.coupon_details);
        TextView coupon_stop = helper.getView(R.id.coupon_stop);
        ImageView delete = helper.getView(R.id.delete);
        ImageView pen = helper.getView(R.id.pen);
        ImageView img_left = helper.getView(R.id.img_left);
        ImageView img_right = helper.getView(R.id.img_right);

//        CouponBatch couponInfo = item.getCouponInfo();
        CouponBatch couponBatch = item.getCouponBatch();
        CouponType couponType = item.getCouponType();
        String ctid = couponType.getCtid();
        String cbca = couponBatch.getCbca();
        String cbrStr = couponBatch.getCbrStr();
        String cbn = couponBatch.getCbn();
        String cbcf = couponBatch.getCbcf();
        String starTime = couponBatch.getStarTime();
        String endTime = couponBatch.getEndTime();
        Long cbvsd = couponBatch.getCbvsd();
        Long cbved = couponBatch.getCbved();
        String startDate = DateUtil.formatDate(cbvsd * 1000);
        String endDate = DateUtil.formatDate(cbved * 1000);
        String cbnu = couponBatch.getCbnu();
        String categoryStr = couponBatch.getCategoryStr();
        int status = couponBatch.getStatus();
        int show = couponBatch.getShow();
        int cbas = couponBatch.getCbas();
        int cbs = couponBatch.getCbs();
        String createTime = DateUtil.strFormatStr(couponBatch.getCreatTime());

        helper.setText(R.id.ad, cbn)
                .setText(R.id.usetime, startDate + "至" + endDate)
                .setText(R.id.couponcount, cbnu)
                .setText(R.id.creattime, createTime)
                .setText(R.id.desc, categoryStr)
                .addOnClickListener(R.id.coupon_stop)
                .addOnClickListener(R.id.pen)
                .addOnClickListener(R.id.delete)
                .addOnClickListener(R.id.coupon_details);

        if ("1000002" == ctid) {
            couponkind.setText("满减券");
            couponkind.setTextColor(Color.parseColor("#51c7ff"));
            moneytitle.setText("元");
            moneydesc.setText(cbca);

            desc.setTextColor(Color.parseColor("#51c7ff"));
            img_right.setBackgroundResource(R.drawable.shape_manjianquan_right);
            img_left.setBackgroundResource(R.drawable.shape_manjianquan_left);
            condition.setVisibility(View.VISIBLE);
            if (cbcf == null || "0".equals(cbcf)) {
                condition.setText("无条件抵用");
            } else {
                condition.setText("满" + cbcf + "可用");
            }
        } else if ("1000001" == ctid) {
            couponkind.setText("折扣券");
            couponkind.setTextColor(Color.parseColor("#ef7a99"));
            moneydesc.setText(cbrStr);
            moneytitle.setText("折");
            desc.setTextColor(Color.parseColor("#ef7a99"));
            img_right.setBackgroundResource(R.drawable.shape_zhekouquan_right);
            img_left.setBackgroundResource(R.drawable.shape_zhekouquan_left);
            condition.setVisibility(View.VISIBLE);

            if (null == cbcf || "0".equals(cbcf)) {
                condition.setText("无条件抵用");
            } else {
                condition.setText("满" + cbcf + "可用");
            }
        } else if ("1000000" == ctid) {
            couponkind.setText("代金券");
            couponkind.setTextColor(Color.parseColor("#99CC00"));
            moneytitle.setText("元");
            moneydesc.setText(cbca);
            desc.setTextColor(Color.parseColor("#99CC00"));
            img_right.setBackgroundResource(R.drawable.shape_daijinquan_right);
            img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);
            condition.setVisibility(View.VISIBLE);
            condition.setText("无条件抵用");
        }


        if (status == 0) {
            if (show == 0) {
                tv_textType.setText("未发布");
                coupon_details.setVisibility(View.GONE);
            } else {
                tv_textType.setText("已过期");
                coupon_details.setVisibility(View.VISIBLE);
            }

            delete.setVisibility(View.VISIBLE);
            coupon_stop.setVisibility(View.GONE);
            pen.setVisibility(View.GONE);

        } else {
            if (1 == cbas || 2 == cbas) {
                tv_textType.setEnabled(false);
                coupon_stop.setVisibility(View.GONE);
                tv_textType.setText("审核中");
                tv_textType.setVisibility(View.VISIBLE);
                pen.setVisibility(View.GONE);
                coupon_details.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            } else if (3 == cbas || 4 == cbas) {
                coupon_stop.setVisibility(View.GONE);
                tv_textType.setVisibility(View.VISIBLE);
                tv_textType.setText("未通过");
                pen.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                coupon_details.setVisibility(View.GONE);
            } else {
                if (cbs == 2 && status == 1) {
                    tv_textType.setEnabled(false);
                    coupon_stop.setVisibility(View.VISIBLE);
                    coupon_stop.setText("启用");
                    tv_textType.setText("未发布");
                    pen.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    coupon_details.setVisibility(View.GONE);
                    if (show == 1) {
                        pen.setVisibility(View.GONE);
                    }
                } else if (cbs == 1 && status == 1) {
                    tv_textType.setEnabled(false);
                    tv_textType.setText("已发布");
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
