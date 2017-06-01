package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;

import java.util.List;

/**
 * Created by Littlelassock on 2016/8/8.
 */
public class ChoseKindsAdapter extends BaseAdapter{
    private List<RunKind> mData;
    private LayoutInflater mInflater;
    private ListItemClickHelp callback;


    public ChoseKindsAdapter(Context context, List<RunKind> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        this.callback=callback;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_run_kinds0808, null);
            holder.title1=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getName());
        if (mData.get(position).getChosenow()==0){
            holder.title1.setTextColor(Color.parseColor("#454545"));
            holder.title1.setBackgroundColor(Color.parseColor("#f8f8f8"));
        }else {
            holder.title1.setTextColor(Color.parseColor("#f39800"));
            holder.title1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return convertView;
    }
}
