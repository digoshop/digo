package com.bjut.servicedog.servicedog.rc_adapter;

import android.widget.ImageView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.UnionModel;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/5/4.
 */

public class UnionListAdapter extends BaseQuickAdapter<UnionModel.DataBean, BaseViewHolder> {
    public UnionListAdapter() {
        super(R.layout.item_union);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnionModel.DataBean item) {
        helper.setText(R.id.tv_union_name, item.getSn())
                .setText(R.id.tv_union_phone, item.getSom())
//                .setText(R.id.tv_union_time, item.getCt())
                .addOnClickListener(R.id.img_delete);

        MyImageLoder.getInstance().disImage(mContext, item.getScov(), (ImageView) helper.getView(R.id.img_union_url));
    }
}
