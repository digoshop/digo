package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Message_data;
import com.bjut.servicedog.servicedog.utils.ListItemClickHelp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by apple02 on 16/7/4.
 */
public class InformationManageAdapter_f extends BaseAdapter{
    private List<Message_data> mData;
    private LayoutInflater mInflater;
    private ListItemClickHelp callback;



    public InformationManageAdapter_f(Context context, List<Message_data> mData2,ListItemClickHelp callback) {
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
            convertView = mInflater.inflate(R.layout.item_information_manage_list_f, null);
            holder.title1=(TextView)convertView.findViewById(R.id.list_information_title);
            holder.title2=(TextView)convertView.findViewById(R.id.list_information_date);
            holder.title3=(TextView)convertView.findViewById(R.id.list_information_class);
            holder.imageView1=(ImageView)convertView.findViewById(R.id.Ldelete);
            holder.imageView2=(ImageView)convertView.findViewById(R.id.Ledit);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title1.setText(mData.get(position).getTitle());
        String time="";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(mData.get(position).getCreate_time()*1000);
        time=sdf.format(date);

        holder.title2.setText(time);

        if(mData.get(position).getType()==0){
            holder.title3.setText("全部会员");
        }else if (mData.get(position).getType()==1){
            holder.title3.setText("新会员");
        }else if (mData.get(position).getType()==2){
            String str="";

            for(int i=0;i<mData.get(position).getTargets().size();i++) {
                 str += mData.get(position).getTargets().get(i)+" ";
            }

            holder.title3.setText(str);
        }

        final int one=holder.imageView1.getId();
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view, parent, position, one);
            }
        });
        final int two=holder.imageView2.getId();
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onclick(view,parent,position,two);
            }
        });


        return convertView;
    }
}
