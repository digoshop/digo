package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.VipLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xran on 16/6/29.
 */
public class VipLevelGridViewAdapter extends BaseAdapter {
    private List<VipLevel> mData;
    private LayoutInflater mInflater;
    private List<Map<Integer, Boolean>> list = new ArrayList<>();


    public VipLevelGridViewAdapter(Context context, List<VipLevel> Data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = Data;
//        init();
    }

    public void setmData(List<VipLevel> mData) {
        this.mData = mData;

        init();
        notifyDataSetChanged();
    }

    public void init() {
        for (int i = 0; i < mData.size(); i++) {
            Map<Integer, Boolean> map = new HashMap<>();
            if (i == 0) {
                map.put(i, false);
            } else {
                map.put(i, false);
            }
            list.add(map);
        }

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
            convertView = mInflater.inflate(R.layout.item_choosekind, null);
            holder.title21 = (CheckBox) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title21.setText(mData.get(position).getVip_level_name());
        holder.title21.setId((int) mData.get(position).getVip_level_id());

        holder.title21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = list.get(position).get(position);
//                if (!list.get(position).get(position)) {
                setCheck(position, isCheck);
//                }

            }
        });
        if (list.size() > 0) {
            Boolean ischeck = list.get(position).get(position);
            holder.title21.setChecked(ischeck);
        }

        return convertView;
    }


    public List<Long> getCheck() {
        List<Long> saveVipLevel = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            Boolean isTrue = list.get(i).get(i);
            if (isTrue) {
                long isLevelId = mData.get(i).getVip_level_id();
                saveVipLevel.add(isLevelId);
            } else {

            }
        }
        return saveVipLevel;
    }

    public void setCheck(int position, boolean isCheck) {
        if (position == 0 && isCheck == false) {
            list.get(0).put(0, true);
            list.get(1).put(1, false);
            for (int i = 2; i < list.size(); i++) {
                list.get(i).put(i, false);
            }
        } else if (position == 0 && isCheck == true) {
            list.get(0).put(0, false);
        } else if (position == 1 && isCheck == false) {
            list.get(1).put(1, true);
            list.get(0).put(0, false);
            for (int i = 2; i < list.size(); i++) {
                list.get(i).put(i, false);
            }

        } else if (position == 1 && isCheck == true) {
            list.get(1).put(1, false);

        } else if (position != 1 && position != 0 && isCheck == false) {
            list.get(0).put(0, false);
            list.get(1).put(1, false);
            list.get(position).put(position, true);
        } else if (position != 1 && position != 0 && isCheck == true) {
            list.get(position).put(position, false);
        }
        notifyDataSetChanged();
    }

    public void setCheck(String target){
        list.get(0).put(0, false);
        list.get(1).put(1, false);
        for (int i = 0; i < mData.size(); i++) {
            if (target.equals(mData.get(i).getVip_level_name())){
                list.get(i).put(i, true);
            }
        }
    }

}
