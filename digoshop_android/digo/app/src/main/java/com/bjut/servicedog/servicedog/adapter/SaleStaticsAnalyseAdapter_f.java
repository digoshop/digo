package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.SaleRecord;

import java.util.List;

import static com.bjut.servicedog.servicedog.R.id.tv_protype;

/**
 * Created by apple02 on 16/7/7.
 */
public class SaleStaticsAnalyseAdapter_f extends BaseAdapter {
    private List<SaleRecord> mData;
    private LayoutInflater mInflater;
    private int Is_vip;//0是非会员
    private Context mContext;

    public SaleStaticsAnalyseAdapter_f(Context context, List<SaleRecord> mData2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData2;
        this.mContext = context;
    }


    public int getCount() {
        return mData.size();
    }


    public Object getItem(int position) {
        return null;
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_sale_statics_analyse_list_f, null);
            holder.title1=(TextView)convertView.findViewById(R.id.statis_nick);
            holder.title2=(TextView)convertView.findViewById(R.id.statics_date);
            holder.title3=(TextView)convertView.findViewById(R.id.statics_amount);
          //  holder.title4 = (TextView) convertView.findViewById(R.id.reducemoney);
            holder.title5 = (TextView) convertView.findViewById(R.id.workkinds);
            holder.title6=(TextView)convertView.findViewById(R.id.y_vip_level);
            holder.title30=(TextView)convertView.findViewById(R.id.textView24);
            holder.title15=(TextView)convertView.findViewById(tv_protype);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            Is_vip=mData.get(position).getIs_vip();
            if(Is_vip==1) {
                holder.title6.setText("会员");
            }else{
                holder.title6.setText("非会员");
            }
            holder.title1.setText(mData.get(position).getUserinfo().getNick());
            holder.title2.setText(mData.get(position).getCreattimeStr());
            holder.title3.setText(mData.get(position).getAmount()+"");
//            holder.title4.setText("-¥"+ mData.get(position).getIncentives_amount());

        int typeId=mData.get(position).getType();
        if(typeId==1){
            holder.title5.setText(mData.get(position).getKinds());
            holder.title30.setText("销售结算");
            holder.title15.setVisibility(View.GONE);
        }else if (typeId==2) {
            holder.title5.setText(mData.get(position).getPna());
            holder.title30.setText("认购结算");
            holder.title15.setText("兑换");
            holder.title15.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.title15.setBackgroundResource(R.drawable.border_orange_2dp);
            holder.title15.setVisibility(View.VISIBLE);
        }else if (typeId==4) {
            holder.title5.setText(mData.get(position).getPna());
            holder.title30.setText("签约折扣");
            holder.title15.setVisibility(View.GONE);
        }else{
            holder.title15.setVisibility(View.VISIBLE);
            holder.title5.setText(mData.get(position).getPna());
            holder.title30.setText("兑换结算");
            holder.title15.setText("拍品");
            holder.title15.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.title15.setBackgroundResource(R.drawable.border_orange_2dp);
        }
        return convertView;
    }
}
