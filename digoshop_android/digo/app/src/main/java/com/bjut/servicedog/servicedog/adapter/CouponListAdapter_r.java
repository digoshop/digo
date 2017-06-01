package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Coupon;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;

import java.util.List;

/**
 * Created by Xran on 16/7/6.
 */
public class CouponListAdapter_r extends BaseAdapter {
    private List<Coupon> mData;
    private LayoutInflater mInflater;
    private ListItemClickHelp callback;
    private int status;//判断优惠券是否过期,1未过期 0已过期
    private Context context;

    public CouponListAdapter_r(Context context, List<Coupon> mData2, ListItemClickHelp callback) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        this.callback = callback;
        this.context = context;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_moneycoupon_manage_r, null);
            //holder.title1 = (TextView) convertView.findViewById(R.id.coupon_check);
            holder.title2 = (TextView) convertView.findViewById(R.id.coupon_stop);
            holder.title3 = (TextView) convertView.findViewById(R.id.tv_textType);
            holder.title4 = (TextView) convertView.findViewById(R.id.couponkind);
            holder.title5 = (TextView) convertView.findViewById(R.id.moneytitle);
            holder.title6 = (TextView) convertView.findViewById(R.id.moneydesc);
            holder.title14 = (TextView) convertView.findViewById(R.id.couponcount);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.img_left);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.img_right);
            holder.title15 = (TextView) convertView.findViewById(R.id.desc);
            holder.title7 = (ImageView) convertView.findViewById(R.id.pen);
            holder.title8 = (ImageView) convertView.findViewById(R.id.delete);
            // holder.title16 = (TextView) convertView.findViewById(R.id.line);
            holder.title17 = (TextView) convertView.findViewById(R.id.condition);
            holder.title18 = (TextView) convertView.findViewById(R.id.ad);
            holder.title19 = (TextView) convertView.findViewById(R.id.usetime);
            holder.title20 = (TextView) convertView.findViewById(R.id.creattime);
            holder.coupondetails = (TextView) convertView.findViewById(R.id.coupon_details);
            // holder.title30=(TextView)convertView.findViewById(R.id.sb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        status = mData.get(position).getCouponBatch().getStatus();

        if ("1000002" == (mData.get(position).getCouponType().getCtid())) {
            holder.title4.setText("满减券");
            holder.title4.setTextColor(Color.parseColor("#51c7ff"));
            holder.title5.setText("元");
            holder.title6.setText(mData.get(position).getCouponBatch().getCbca());
            holder.title14.setText(mData.get(position).getCouponBatch().getCbnu() + "");
            holder.title15.setText(mData.get(position).getCouponBatch().getCategoryStr());
            holder.title15.setTextColor(Color.parseColor("#51c7ff"));
            holder.imageView2.setBackgroundResource(R.drawable.shape_manjianquan_right);
            holder.imageView1.setBackgroundResource(R.drawable.shape_manjianquan_left);
            holder.title17.setVisibility(View.VISIBLE);
            if (mData.get(position).getCouponBatch().getCbcf() == null || "0".equals(mData.get(position).getCouponBatch().getCbcf())) {
                holder.title17.setText("无条件抵用");
            } else {
                holder.title17.setText("满" + mData.get(position).getCouponBatch().getCbcf() + "可用");
            }
        } else if ("1000001" == (mData.get(position).getCouponType().getCtid())) {
            holder.title4.setText("折扣券");
            holder.title4.setTextColor(Color.parseColor("#ef7a99"));
            holder.title6.setText(mData.get(position).getCouponBatch().getCbrStr());
            holder.title5.setText("折");
            holder.title14.setText(mData.get(position).getCouponBatch().getCbnu() + "");
            holder.title15.setText(mData.get(position).getCouponBatch().getCategoryStr());
            holder.title15.setTextColor(Color.parseColor("#ef7a99"));
            holder.imageView2.setBackgroundResource(R.drawable.shape_zhekouquan_right);
            holder.imageView1.setBackgroundResource(R.drawable.shape_zhekouquan_left);
            holder.title17.setVisibility(View.VISIBLE);

            if (null == mData.get(position).getCouponBatch().getCbcf() || "0".equals(mData.get(position).getCouponBatch().getCbcf())) {
                holder.title17.setText("无条件抵用");
            } else {
                holder.title17.setText("满" + mData.get(position).getCouponBatch().getCbcf() + "可用");
            }
        } else if ("1000000" == (mData.get(position).getCouponType().getCtid())) {
            holder.title4.setText("代金券");
            holder.title4.setTextColor(Color.parseColor("#99CC00"));
            holder.title5.setText("元");
            holder.title6.setText(mData.get(position).getCouponBatch().getCbca());
            holder.title14.setText("" + mData.get(position).getCouponBatch().getCbnu());
            holder.title15.setText(mData.get(position).getCouponBatch().getCategoryStr());
            holder.title15.setTextColor(Color.parseColor("#99CC00"));
            holder.imageView2.setBackgroundResource(R.drawable.shape_daijinquan_right);
            holder.imageView1.setBackgroundResource(R.drawable.shape_daijinquan_left);
            holder.title17.setVisibility(View.VISIBLE);
            holder.title17.setText("无条件抵用");
        }

        if (status == 0) {
            if (mData.get(position).getCouponBatch().getShow() == 0) {
                holder.title3.setText("未发布");
                holder.coupondetails.setVisibility(View.GONE);
            } else {
                holder.title3.setText("已过期");
                holder.coupondetails.setVisibility(View.VISIBLE);
            }

            holder.title8.setVisibility(View.VISIBLE);
            holder.title2.setVisibility(View.GONE);
            holder.title7.setVisibility(View.GONE);

        } else {
            if (1 == mData.get(position).getCouponBatch().getCbas() || 2 == mData.get(position).getCouponBatch().getCbas()) {
                holder.title3.setEnabled(false);
                holder.title2.setVisibility(View.GONE);
                holder.title3.setText("审核中");
                holder.title3.setVisibility(View.VISIBLE);
                holder.title7.setVisibility(View.GONE);
                holder.coupondetails.setVisibility(View.GONE);
                holder.title8.setVisibility(View.GONE);
                holder.title14.setVisibility(View.VISIBLE);
            } else if (3 == mData.get(position).getCouponBatch().getCbas() || 4 == mData.get(position).getCouponBatch().getCbas()) {
                holder.title2.setVisibility(View.GONE);
                holder.title3.setVisibility(View.VISIBLE);
                holder.title3.setText("未通过");
                holder.title7.setVisibility(View.VISIBLE);
                holder.title8.setVisibility(View.VISIBLE);
                holder.coupondetails.setVisibility(View.GONE);
                holder.title14.setVisibility(View.VISIBLE);
            } else {


                if (mData.get(position).getCouponBatch().getCbs() == 2 && status == 1) {
                    holder.title3.setEnabled(false);
                    holder.title2.setVisibility(View.VISIBLE);
                    holder.title2.setText("启用");
                    holder.title3.setText("未发布");
                    holder.title7.setVisibility(View.VISIBLE);
                    holder.title8.setVisibility(View.VISIBLE);
                    holder.coupondetails.setVisibility(View.GONE);
                    if (mData.get(position).getCouponBatch().getShow() == 1) {
                        holder.title7.setVisibility(View.GONE);
                    }
                } else if (mData.get(position).getCouponBatch().getCbs() == 1 && status == 1) {
                    holder.title3.setEnabled(false);
                    holder.title3.setText("已发布");
                    holder.title7.setVisibility(View.GONE);
                    holder.title8.setVisibility(View.GONE);
                    holder.coupondetails.setVisibility(View.GONE);
                    holder.title2.setVisibility(View.VISIBLE);
                    holder.title2.setText("禁用");
                }
                holder.title14.setVisibility(View.VISIBLE);
            }
        }

        long start = (DateUtil.getStringToDate(mData.get(position).getCouponBatch().getStarTime()));
        long end = (DateUtil.getStringToDate(mData.get(position).getCouponBatch().getEndTime()));
        String startDate = DateUtil.formatDate(start);
        String endDate = DateUtil.formatDate(end);
        holder.title18.setText(mData.get(position).getCouponBatch().getCbn());
        holder.title19.setText(startDate + "至" + endDate);
        String createTime = DateUtil.strFormatStr(mData.get(position).getCouponBatch().getCreatTime());
        holder.title20.setText(createTime);
        final int two = holder.title2.getId();
        holder.title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, two);
            }
        });
        final int three = holder.coupondetails.getId();
        holder.coupondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, three);
            }
        });
        final int four = holder.title7.getId();
        holder.title7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, four);
            }
        });
        final int five = holder.title8.getId();
        holder.title8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, five);
            }
        });
        return convertView;
    }

}
