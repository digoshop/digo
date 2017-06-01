package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.LimitTimeAuction_data;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

import java.util.List;

/**
 * Created by apple02 on 16/7/6.
 */
public class LimitTimeAuctionAnalyseAdapter extends BaseAdapter {
    private List<LimitTimeAuction_data> mData;
    private LayoutInflater mInflater;
    private int status;
    private Context mContext;

    public LimitTimeAuctionAnalyseAdapter(Context context, List<LimitTimeAuction_data> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        mContext  = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_limit_time_auction_analyse_list, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.limit_auction_name);
            holder.title2 = (TextView) convertView.findViewById(R.id.limit_auction_undermoney);
            holder.title3 = (TextView) convertView.findViewById(R.id.limit_auction_originalmoney);
            holder.title4 = (TextView) convertView.findViewById(R.id.limit_auction_dealmoney);
            holder.title5 = (TextView) convertView.findViewById(R.id.limit_auction_date);
            holder.title6 = (TextView) convertView.findViewById(R.id.limit_auction_status);
            holder.title14 = (TextView) convertView.findViewById(R.id.limit_auction_checknumber);
            holder.title15 = (TextView) convertView.findViewById(R.id.limit_auction_auctionnumber);
            holder.title16 = (TextView) convertView.findViewById(R.id.limit_auction_person);
            holder.title7 = (ImageView) convertView.findViewById(R.id.limit_auction_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getPna());//商品名称
        holder.title2.setText(mData.get(position).getApp() + "");//竞拍底价
        holder.title3.setText("¥" + mData.get(position).getPpr() + "");//商品原价
        holder.title4.setText(mData.get(position).getAphg() + "");//竞拍成交价
        long apsd = mData.get(position).getApsd();
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(apsd * 1000));
        holder.title5.setText(date);//竞拍开始时间

        status = mData.get(position).getAps();
        if (status == 6) {
            holder.title6.setText("已结束");
        } else if (status == 99) {
            holder.title6.setText("已禁用");
        } else {
            holder.title6.setText("已删除");
        }
        holder.title14.setText("(" + mData.get(position).getBc() + "" + ")");//查看人数
        holder.title15.setText("(" + mData.get(position).getAc() + "" + ")");//竞拍人数
        holder.title16.setText("(" + mData.get(position).getNk() + "" + ")");//昵称
        MyImageLoder.getInstance().disImage(mContext,mData.get(position).getPpi(),holder.title7);//商品图片
        holder.title3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }

}
