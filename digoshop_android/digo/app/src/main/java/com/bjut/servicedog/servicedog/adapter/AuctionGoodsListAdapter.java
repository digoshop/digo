package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Good;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

import java.util.List;


/**
 * Created by apple02 on 16/7/2.
 */
public class AuctionGoodsListAdapter extends BaseAdapter {
    private List<Good> mData;
    private LayoutInflater mInflater;
    private ListItemClickHelp callback;
    private int ma = 0;
    private Context context;

    public AuctionGoodsListAdapter(Context context, List<Good> mData2, ListItemClickHelp callback) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_auction_goods_list, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.auction_tv_name);
            holder.title2 = (TextView) convertView.findViewById(R.id.auction_under_value);
            holder.title3 = (TextView) convertView.findViewById(R.id.auction_original_value);
            holder.title4 = (TextView) convertView.findViewById(R.id.auction_tv_status);
            holder.title5 = (TextView) convertView.findViewById(R.id.tv_auction_date);
//            holder.title6=(TextView)convertView.findViewById(R.id.auction_tv_check);
            holder.title7 = (ImageView) convertView.findViewById(R.id.iv_auction_image);

            holder.an_tv_is_use = (TextView) convertView.findViewById(R.id.an_tv_is_use);
            holder.an_img_update = (ImageView) convertView.findViewById(R.id.an_img_update);
            holder.an_img_delete = (ImageView) convertView.findViewById(R.id.an_img_delete);

//            holder.title21 = (CheckBox) convertView.findViewById(R.id.iv_auction_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title4.setText(mData.get(position).getApsStr());
        String time = mData.get(position).getApsdTime();
        holder.title5.setText(time);
        holder.title1.setText(mData.get(position).getPna());
        holder.title2.setText("¥" + mData.get(position).getApp() + "");
        holder.title3.setText("原价¥" + mData.get(position).getPpr() + "");
        holder.title3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String aps = mData.get(position).getAps();
        if (aps != null) {
            if ("1".equals(aps)) {//审核中
                holder.an_img_delete.setVisibility(View.GONE);
                holder.an_tv_is_use.setVisibility(View.GONE);
                holder.an_img_update.setVisibility(View.GONE);
            } else if ("2".equals(aps)) {//已驳回
                holder.an_img_delete.setVisibility(View.VISIBLE);
                holder.an_tv_is_use.setVisibility(View.GONE);
                holder.an_img_update.setVisibility(View.VISIBLE);
            } else if ("3".equals(aps)) {//已通过
                holder.an_img_delete.setVisibility(View.VISIBLE);
                holder.an_tv_is_use.setVisibility(View.VISIBLE);
                holder.an_tv_is_use.setText("启用");
                holder.an_img_update.setVisibility(View.VISIBLE);
            } else if ("4".equals(aps)) {//未开始
                holder.an_tv_is_use.setText("禁用");
                holder.an_img_delete.setVisibility(View.GONE);
                holder.an_tv_is_use.setVisibility(View.VISIBLE);
                holder.an_img_update.setVisibility(View.GONE);
            } else if ("5".equals(aps)) {//竞拍中
                holder.an_img_delete.setVisibility(View.GONE);
                holder.an_tv_is_use.setVisibility(View.GONE);
                holder.an_img_update.setVisibility(View.GONE);
            } else if ("6".equals(aps)) {//已结束
                holder.an_img_delete.setVisibility(View.VISIBLE);
                holder.an_tv_is_use.setVisibility(View.GONE);
                holder.an_img_update.setVisibility(View.GONE);
            } else {//已禁用
                holder.an_tv_is_use.setText("启用");
                holder.an_img_delete.setVisibility(View.VISIBLE);
                holder.an_tv_is_use.setVisibility(View.VISIBLE);
                holder.an_img_update.setVisibility(View.GONE);
            }
        }
        MyImageLoder.getInstance().disImage(context, mData.get(position).getPpi(), holder.title7);


        final int an_tv_is_use_id = holder.an_tv_is_use.getId();
        holder.an_tv_is_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, an_tv_is_use_id);
            }
        });

        final int an_img_update_id = holder.an_img_update.getId();
        holder.an_img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, an_img_update_id);
            }
        });

        final int an_img_delete_id = holder.an_img_delete.getId();
        holder.an_img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, an_img_delete_id);
            }
        });

        return convertView;
    }

    public void delete(long id) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getPid() == id) {
                mData.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public void setStatus(long id, int type) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getPid() == id) {
                mData.get(i).setAps(type + "");
            }
        }
        notifyDataSetChanged();
    }

}
