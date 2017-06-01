package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;

import java.util.List;
import java.util.Map;

/**
 * Created by apple02 on 16/7/7.
 */
public class StoreBrowseAnalyseAdapter_f extends BaseAdapter{
    private List<Map<String, Object>> mData;
    private LayoutInflater mInflater;



    public StoreBrowseAnalyseAdapter_f(Context context, List<Map<String, Object>> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_store_browse_analyse_list_f, null);
            holder.title1=(TextView)convertView.findViewById(R.id.location);
            holder.title2=(TextView)convertView.findViewById(R.id.person);
            holder.title3=(TextView)convertView.findViewById(R.id.percent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText((String)mData.get(position).get("location"));
        holder.title2.setText((String)mData.get(position).get("person"));
        holder.title3.setText((String)mData.get(position).get("percent"));

        return convertView;
    }
}
