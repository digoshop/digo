package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.VipLevel;

import java.util.List;

/**
 * Created by Xran on 16/7/23.
 */
public class GroupVipLevelListAdapter extends BaseAdapter {
    private Context context;
    private List<VipLevel> list;
    public GroupVipLevelListAdapter(Context context, List<VipLevel> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.group_viplevel_item, null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.title1=(TextView) convertView.findViewById(R.id.groupItem);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.title1.setText(list.get(position).getVip_level_name());

        return convertView;
    }


}
