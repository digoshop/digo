package com.bjut.servicedog.servicedog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.utils.CustomConstants;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

import java.util.ArrayList;
import java.util.List;

public class CheckEventImageAdapter extends BaseAdapter {
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private Context mContext;

    public CheckEventImageAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }


    public int getCount() {
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == CustomConstants.ONLY_ONE) {
            return CustomConstants.ONLY_ONE;
        } else {
            return mDataList.size() + 1;
        }
    }

    public void setDate(List<ImageItem> dataList){
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == CustomConstants.ONLY_ONE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0
                || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
//        //所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
//        convertView = View.inflate(mContext, R.layout.item_publish, null);

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

        if (isShowAddItem(position)) {
            MyImageLoder.getInstance().disImage(mContext, R.drawable.icon_addpic, imageIv);
        } else {
            final ImageItem item = mDataList.get(position);

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
