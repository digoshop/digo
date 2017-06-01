package com.bjut.servicedog.servicedog.rc_adapter;

import android.widget.ImageView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by beibeizhu on 17/2/17.
 */

public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ImageAdapter(List<String> data) {
        super(R.layout.item_publish, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.item_grid_image);
        MyImageLoder.getInstance().disImage(mContext, item, imageView);
    }
}
