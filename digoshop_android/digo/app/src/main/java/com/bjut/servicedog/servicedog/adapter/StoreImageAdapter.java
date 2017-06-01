package com.bjut.servicedog.servicedog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.utils.CustomConstants;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreImageAdapter extends BaseAdapter implements Serializable {
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private Context mContext;
    private String imageurl;
    boolean isFang = false;

    public StoreImageAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public void setFang(boolean fang) {
        isFang = fang;
    }

    public int getCount() {
        //多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == CustomConstants.STORE_MAX_IMAGE_SIZE) {
            return CustomConstants.STORE_MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
        //return mDataList.size();
    }

    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == CustomConstants.STORE_MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0
                || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public void setDate(List<ImageItem> dataList){
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_publish, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_grid_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView imageIv = viewHolder.imageView;
        int width = 0;
        int height = 0;
        int dimens144 = mContext.getResources().getDimensionPixelOffset(R.dimen.base_dimen_144);
        int dimens96 = mContext.getResources().getDimensionPixelOffset(R.dimen.base_dimen_96);

        if (!isFang) {
            width = dimens144;
            height = dimens96;
        } else {
            width = dimens144;

            height = dimens144;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        imageIv.setLayoutParams(layoutParams);
        if (isShowAddItem(position)) {
            if (!isFang) {
                MyImageLoder.getInstance().disImage(mContext, R.drawable.icon_addpic, imageIv);
            } else {
                MyImageLoder.getInstance().disImage(mContext, R.drawable.photoer, imageIv);
            }
        } else {
            ImageItem item = mDataList.get(position);
            if (!item.getImageUrl().equals("")) {
                MyImageLoder.getInstance().disImage(mContext, item.getImageUrl(), imageIv);
            } else {
                MyImageLoder.getInstance().disImage(mContext, item.getSourcePath(), imageIv);
            }

        }

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }


    private class ViewHolder {
        ImageView imageView;
    }
}
