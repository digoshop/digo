package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ChooseCouponModel;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

import java.util.List;

/**
 * Created by beibeizhu.
 */
public class ChoseCouponAdapter extends MyBaseAdpter<ChooseCouponModel.DataBean> {

    private MyImageLoder mMyImageLoder;
    private Context mContext;

    public ChoseCouponAdapter(Context context) {
        super(context);
        mContext = context;
        mMyImageLoder = MyImageLoder.getInstance();
    }

    @Override
    public int getContentView() {
        return R.layout.item_chose_coupon;
    }

    @Override
    public void onInitView(View view, int position) {
        TextView y_use = get(view, R.id.y_use);
        TextView y_money = get(view, R.id.y_money);
        TextView y_desc = get(view, R.id.y_desc);
        TextView y_condition = get(view, R.id.y_condition);
//        LinearLayout relat = get(view, R.id.relat);
        TextView y_topic = get(view, R.id.y_topic);
        TextView y_starttime = get(view, R.id.y_starttime);
        TextView y_endtime = get(view, R.id.y_endtime);
        TextView y_pre = get(view, R.id.y_pre);
        TextView y_kind = get(view, R.id.y_kind);
        ImageView img_left = get(view, R.id.img_left);
        ImageView img_right = get(view, R.id.img_right);

        ChooseCouponModel.DataBean item = getItem(position);

        String type = item.getCouponType().getCtn();
        int status = item.getCouponInfo().getStatus();
        int cs = item.getCouponInfo().getCs();
        List<String> categorys = item.getCouponInfo().getCategorys();
        String category = "";
        String startTime = DateUtil.formatDate(item.getCouponInfo().getCbvsd() * 1000);
        String endTime = DateUtil.formatDate(item.getCouponInfo().getCbved() * 1000);

        if (categorys.size() == 0) {
            category = "全场通用";
        } else {
            for (String s : categorys) {
                category = category + s + " ";
            }
            category = category.substring(0, category.length() - 1);
        }

        if ("满减券".equals(type)) {

            if (cs == 3) {
                if (status == 0) {
                    y_use.setText("可用");
                    img_left.setBackgroundResource(R.drawable.shape_manjianquan_left);//优惠券的背景
                    img_right.setBackgroundResource(R.drawable.shape_manjianquan_right);//优惠券的背景

                } else {
                    y_use.setText("不可用");
                    y_use.setEnabled(false);
                    img_left.setAlpha((float) 0.4);
                    img_right.setAlpha((float) 0.4);
                }
            } else if (cs == 5) {
                y_use.setText("过期");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);//优惠券的背景
                img_right.setBackgroundResource(R.color.bg_gray);//优惠券的背景
            } else {
                y_use.setText("未生效");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);
                img_right.setBackgroundResource(R.color.bg_gray);
            }
            y_pre.setText("元");

            y_kind.setText(type);//优惠券的类型
            y_kind.setTextColor(Color.parseColor("#51c7ff"));//优惠券类型字体颜色
            y_money.setText(item.getCouponInfo().getCbca() + "");//券面价值
            int cbcf = item.getCouponInfo().getCbcf();
            if (cbcf == 0) {
                y_condition.setText("无条件抵用");
            } else {
                y_condition.setText("满" + cbcf + "可用");//优惠券满多少可用
            }

            y_desc.setText(category);//优惠券的用途
            y_desc.setTextColor(Color.parseColor("#51c7ff"));
            y_topic.setText(item.getCouponInfo().getCbn());
        } else if ("折扣券".equals(type)) {
            if (cs == 3) {
                if (status == 0) {
                    y_use.setText("可用");
//                    relat.setBackgroundResource(R.drawable.zhekouquani);//
                    img_left.setBackgroundResource(R.drawable.shape_zhekouquan_left);//优惠券的背景
                    img_right.setBackgroundResource(R.drawable.shape_zhekouquan_right);//优惠券的背景

                } else {
                    y_use.setText("不可用");
                    y_use.setEnabled(false);
                    img_left.setAlpha((float) 0.4);
                    img_right.setAlpha((float) 0.4);
                }
            } else if (cs == 5) {
                y_use.setText("过期");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);
                img_right.setBackgroundResource(R.color.bg_gray);
            } else {
                y_use.setText("未生效");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);
                img_right.setBackgroundResource(R.color.bg_gray);
            }
            y_pre.setText("折");

            y_kind.setText(type);//优惠券的类型
            y_kind.setTextColor(Color.parseColor("#ef7a99"));//优惠券类型字体颜色
            float cbr = item.getCouponInfo().getCbr() * 10;
            y_money.setText(cbr + "");//券面价值
            int cbcf = item.getCouponInfo().getCbcf();
            if (cbcf == 0) {
                y_condition.setText("无条件抵用");
            } else {
                y_condition.setText("满" + cbcf + "可用");//优惠券满多少可用
            }

            y_desc.setText(category);//优惠券的用途
            y_desc.setTextColor(Color.parseColor("#ef7a99"));
            y_topic.setText(item.getCouponInfo().getCbn());


        } else if ("代金券".equals(type)) {
            if (cs == 3) {
                if (status == 0) {
                    y_use.setText("可用");
                    img_left.setBackgroundResource(R.drawable.shape_daijinquan_left);//优惠券的背景
                    img_right.setBackgroundResource(R.drawable.shape_daijinquan_right);//优惠券的背景

                } else {
                    y_use.setText("不可用");
                    y_use.setEnabled(false);
                    img_left.setAlpha((float) 0.4);
                    img_right.setAlpha((float) 0.4);
                }
            } else if (cs == 5) {
                y_use.setText("过期");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);
                img_right.setBackgroundResource(R.color.bg_gray);
            } else {
                y_use.setText("未生效");
                y_use.setEnabled(false);
                img_left.setBackgroundResource(R.color.bg_gray);
                img_right.setBackgroundResource(R.color.bg_gray);
            }
            y_pre.setText("元");

            y_kind.setText(type);//优惠券的类型
            y_kind.setTextColor(Color.parseColor("#99CC00"));//优惠券类型字体颜色
            y_money.setText(item.getCouponInfo().getCbca() + "");//券面价值
            y_condition.setText("无条件抵用");//优惠券满多少可用
            y_desc.setText(category);//优惠券的用途
            y_desc.setTextColor(Color.parseColor("#99CC00"));
            y_topic.setText(item.getCouponInfo().getCbn());
        }
        y_starttime.setText(startTime);
        y_endtime.setText(endTime);
    }

}
