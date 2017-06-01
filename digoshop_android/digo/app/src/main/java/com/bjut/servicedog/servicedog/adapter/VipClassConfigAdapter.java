package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Vip_class_config_data;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by apple02 on 16/7/2.
 */
public class VipClassConfigAdapter extends BaseAdapter {
    private List<Vip_class_config_data> mData;
    private LayoutInflater mInflater;
    private ListItemClickHelp callback;

    public VipClassConfigAdapter(Context context, List<Vip_class_config_data> mData2,ListItemClickHelp callback) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        this.callback=callback;
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

    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_vip_class_config_list, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.config_vip_class);
            holder.title2 = (TextView) convertView.findViewById(R.id.config_vip_jifen);
            holder.title3 = (TextView) convertView.findViewById(R.id.config_vip_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText((String)mData.get(position).getVip_level_name());
        holder.title2.setText((int) mData.get(position).getVip_intg()+"");
        final int one=holder.title3.getId();
        holder.title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view,parent,position,one);
            }
        });


        return convertView;
    }
}
