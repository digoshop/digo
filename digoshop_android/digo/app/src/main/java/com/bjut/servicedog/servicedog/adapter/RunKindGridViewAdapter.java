package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
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
 * Created by Xran on 16/7/24.
 */
public class RunKindGridViewAdapter extends BaseAdapter {
    private List<RunKind> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    LinearLayout.LayoutParams params;
    private int selectedPosition = -1;

    public RunKindGridViewAdapter(Context context, List<RunKind> Data) {
        mData = Data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        params = new LinearLayout.LayoutParams(100,100);

//        params.gravity = Gravity.CENTER;
    }

    public void clearSelection(int position) {
        selectedPosition = position;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mchoosekind, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getName());
        holder.title1.setId((int) mData.get(position).getMoid());

        if (mData.get(position).getChosenow() == 1) {
            holder.title1.setBackgroundResource(R.drawable.xuanzhongkuang);
        } else {
            holder.title1.setBackgroundResource(R.drawable.weixuanzhongkuang);
        }
        return convertView;
    }

    public void removeItem(int index) {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void remove(RunKind runKind) {
        mData.remove(runKind);
        notifyDataSetChanged();
    }

}
