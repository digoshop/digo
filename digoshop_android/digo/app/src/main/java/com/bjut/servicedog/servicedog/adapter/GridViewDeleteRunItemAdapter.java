package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.RunKind;

import java.util.List;

/**
 * Created by Xran on 16/9/4.
 */
public class GridViewDeleteRunItemAdapter extends BaseAdapter{
    private List<RunKind> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public GridViewDeleteRunItemAdapter(Context context, List<RunKind> Data) {
        mData=Data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.textview_deletekind, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getName());
        holder.title1.setId((int) mData.get(position).getMoid());
        return convertView;
    }
}
