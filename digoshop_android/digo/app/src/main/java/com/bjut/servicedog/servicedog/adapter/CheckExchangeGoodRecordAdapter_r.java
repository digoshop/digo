package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.CheckGoodRecord_data;

import java.util.List;

/**
 * Created by Xran on 16/7/4.
 */
public class CheckExchangeGoodRecordAdapter_r extends BaseAdapter{
    private List<CheckGoodRecord_data> mData;
    private LayoutInflater mInflater;

    public CheckExchangeGoodRecordAdapter_r(Context context, List<CheckGoodRecord_data> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
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
            convertView = mInflater.inflate(R.layout.item_checkgood_list, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.exchange_record_phonenumber);
            holder.title2 = (TextView) convertView.findViewById(R.id.exchange_record_time);
//            holder.title3 = (TextView) convertView.findViewById(R.id.exchange_record_number);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getNick());
        holder.title2.setText(mData.get(position).getExchangetime());
//        holder.title3.setText(mData.get(position).getExchange_id()+"");





        return convertView;
    }
}
