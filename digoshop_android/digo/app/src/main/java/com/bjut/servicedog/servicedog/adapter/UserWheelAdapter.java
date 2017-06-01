package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;

import com.bjut.servicedog.servicedog.base.BaseWheelAdapter;
import com.bjut.servicedog.servicedog.po.Permission_data;

import java.util.List;

/**
 * Created by zzr on 16/12/1.
 */

public class UserWheelAdapter extends BaseWheelAdapter<Permission_data> {
    
        public UserWheelAdapter(Context context, List<Permission_data> list) {
            super(context,list);
        }

        @Override
        protected CharSequence getItemText(int index) {
            Permission_data data = getItemData(index);
            if(data != null){
                return data.getAuth_name();
            }
            return null;
        }
    
}
