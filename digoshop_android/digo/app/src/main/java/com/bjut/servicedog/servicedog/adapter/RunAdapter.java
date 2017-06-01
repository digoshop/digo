package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.RunKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beibeizhu on 16/12/30.
 */

public class RunAdapter extends MyBaseAdpter<RunKind> {

    private List<Boolean> mCheckedList;

    public RunAdapter(Context context) {
        super(context);
        mCheckedList = new ArrayList<Boolean>();
    }

    @Override
    public void setList(List<RunKind> list) {
        super.setList(list);
        for (int i = 0; i < list.size(); i++) {
            mCheckedList.add(false);
        }
    }

    public void setChecked(int position){
        for (int i = 0; i < mCheckedList.size(); i++) {
            if (i==position) {
                mCheckedList.set(i,true);
            }else{
                mCheckedList.set(i,false);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getContentView() {
        return R.layout.textview_addkinds_left;
    }

    @Override
    public void onInitView(View view, int position) {
        TextView tv_name = get(view, R.id.tv_name);

        Boolean aBoolean = mCheckedList.get(position);
        if (aBoolean) {
            tv_name.setTextColor(context.getResources().getColor(R.color.orange));
            tv_name.setBackgroundResource(R.drawable.border_bottom_white);
        }else{
            tv_name.setTextColor(context.getResources().getColor(R.color.word_color));
            tv_name.setBackgroundResource(R.drawable.border_bottom_grey);
        }
        RunKind item = getItem(position);
        tv_name.setText(item.getName());
    }
}
