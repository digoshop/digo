package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CheckAuctionRecord_data;

import java.util.List;

/**
 * Created by apple02 on 16/7/20.
 */
public class CheckAuctionGoodRecordAdapter_f extends BaseAdapter{
    private List<CheckAuctionRecord_data> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public CheckAuctionGoodRecordAdapter_f(Context context,List<CheckAuctionRecord_data> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_check_auction_record_list, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.auction_record_phonenumber);
            holder.title2 = (TextView) convertView.findViewById(R.id.auction_record_time);
            holder.title3 = (TextView) convertView.findViewById(R.id.auction_record_status);
            holder.title4 = (TextView) convertView.findViewById(R.id.tv_status);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        if (position==0) {
            holder.title4.setText("领先");
            holder.title4.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.title4.setBackgroundResource(R.drawable.border_orange_2dp);
        }else{
            holder.title4.setText("出局");
            holder.title4.setTextColor(mContext.getResources().getColor(R.color.blue));
            holder.title4.setBackgroundResource(R.drawable.border_blue_2dp);
        }
        holder.title1.setText(mData.get(position).getNk());
        holder.title2.setText(mData.get(position).getCtime());
        holder.title3.setText(mData.get(position).getPpr()+"");
        return convertView;
    }
}

