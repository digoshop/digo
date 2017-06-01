package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.Gravity;
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
 * Created by apple02 on 16/8/15.
 */
public class ConfirmRunkindsAdapter extends BaseAdapter {
    private List<RunKind> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    LinearLayout.LayoutParams params;
    private int clickTemp=-1;//gridview状态标志位

    public void setSeclection(int position){
        clickTemp=position;
    }

    public ConfirmRunkindsAdapter(Context context, List<RunKind> Data, double itemwidth) {
        mData=Data;
        mContext = context;
        mInflater = LayoutInflater.from(context);

        params = new LinearLayout.LayoutParams((int)itemwidth, (int)itemwidth);
        params.gravity = Gravity.CENTER;
    }
    public ConfirmRunkindsAdapter(Context context, List<RunKind> Data) {
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
            convertView = mInflater.inflate(R.layout.item_confirm_runkinds, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(clickTemp==position){
            holder.title1.setBackgroundResource(R.drawable.xuanzhongkuang);
        }
        else{
            holder.title1.setBackgroundResource(R.drawable.weixuanzhongkuang);
        }
        holder.title1.setText(mData.get(position).getName());
        holder.title1.setId((int)mData.get(position).getMoid());
        return convertView;
    }
}
