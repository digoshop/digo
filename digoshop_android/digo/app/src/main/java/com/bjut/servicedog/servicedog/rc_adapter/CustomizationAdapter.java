package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Customization_data;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by beibeizhu on 17/3/22.
 */

public class CustomizationAdapter extends BaseQuickAdapter<Customization_data, BaseViewHolder> {

    public CustomizationAdapter() {
        super(R.layout.item_customization_accept_list_f);
    }

    @Override
    protected void convert(BaseViewHolder helper, Customization_data item) {
        TextView currnet_reply = helper.getView(R.id.currnet_reply);
        HorizontalScrollView c_horizion = helper.getView(R.id.c_horizion);
        ImageView customi_imageone = helper.getView(R.id.customi_imageone);
        ImageView customi_imagetwo = helper.getView(R.id.customi_imagetwo);
        ImageView customi_imagethree = helper.getView(R.id.customi_imagethree);
        ImageView customi_imagefour = helper.getView(R.id.customi_imagefour);
        ImageView customi_imagefive = helper.getView(R.id.customi_imagefive);
        c_horizion.setVisibility(View.GONE);
        customi_imageone.setVisibility(View.GONE);
        customi_imagetwo.setVisibility(View.GONE);
        customi_imagethree.setVisibility(View.GONE);
        customi_imagefour.setVisibility(View.GONE);
        customi_imagefive.setVisibility(View.GONE);

        LinearLayout ll_rerly = helper.getView(R.id.ll_rerly);
        TextView tv_replycontent = helper.getView(R.id.tv_replycontent);
        HorizontalScrollView re_horizon = helper.getView(R.id.re_horizon);
        ImageView reply_imageone = helper.getView(R.id.reply_imageone);
        ImageView reply_imagetwo = helper.getView(R.id.reply_imagetwo);
        ImageView reply_imagethree = helper.getView(R.id.reply_imagethree);
        ImageView reply_imagefour = helper.getView(R.id.reply_imagefour);
        ImageView reply_imagefive = helper.getView(R.id.reply_imagefive);
        TextView tv_replytime = helper.getView(R.id.tv_replytime);
        reply_imageone.setVisibility(View.GONE);
        reply_imagetwo.setVisibility(View.GONE);
        reply_imagethree.setVisibility(View.GONE);
        reply_imagefour.setVisibility(View.GONE);
        reply_imagefive.setVisibility(View.GONE);
        re_horizon.setVisibility(View.GONE);

        helper.setText(R.id.customi_title, item.getCsDto().getTn())
                .setText(R.id.customi_request, item.getCsDto().getDesc())
                .setText(R.id.customi_date, item.getCsDto().getCtTime());
        helper.addOnClickListener(R.id.currnet_reply);

        int imageSize = 0;
        if (item.getCsDto().getIurls() != null) {
            c_horizion.setVisibility(View.VISIBLE);
            imageSize = item.getCsDto().getIurls().size();
        } else {
            imageSize = 0;
        }

        if (imageSize > 0) {
            if (imageSize > 5) {
                imageSize = 5;
            }
            switch (imageSize) {
                case 1:
                    customi_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(0), customi_imageone);
                    break;
                case 2:
                    customi_imageone.setVisibility(View.VISIBLE);
                    customi_imagetwo.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(0), customi_imageone);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(1), customi_imagetwo);
                    break;
                case 3:
                    customi_imageone.setVisibility(View.VISIBLE);
                    customi_imagetwo.setVisibility(View.VISIBLE);
                    customi_imagethree.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(0), customi_imageone);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(1), customi_imagetwo);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(2), customi_imagethree);
                    break;
                case 4:
                    customi_imageone.setVisibility(View.VISIBLE);
                    customi_imagetwo.setVisibility(View.VISIBLE);
                    customi_imagethree.setVisibility(View.VISIBLE);
                    customi_imagefour.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(0), customi_imageone);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(1), customi_imagetwo);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(2), customi_imagethree);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(3), customi_imagefour);
                    break;
                case 5:
                    customi_imageone.setVisibility(View.VISIBLE);
                    customi_imagetwo.setVisibility(View.VISIBLE);
                    customi_imagethree.setVisibility(View.VISIBLE);
                    customi_imagefour.setVisibility(View.VISIBLE);
                    customi_imagefive.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(0), customi_imageone);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(1), customi_imagetwo);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(2), customi_imagethree);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(3), customi_imagefour);
                    MyImageLoder.getInstance().disImage(mContext, item.getCsDto().getIurls().get(4), customi_imagefive);
                    break;
            }
        } else {
        }

        if (item.getRepDto() == null && item.getCsDto().getSt().equals("0")) {
            currnet_reply.setText("回复");
            currnet_reply.setEnabled(true);
            currnet_reply.setTextColor(mContext.getResources().getColor(R.color.blue));
            ll_rerly.setVisibility(View.GONE);

        } else if (item.getCsDto().getSt().equals("1")) {
            currnet_reply.setText("已回复");
            currnet_reply.setTextColor(mContext.getResources().getColor(R.color.delete_word_color));
            currnet_reply.setEnabled(false);
            int replyImageSize = 0;
            if (item.getRepDto() != null) {
                tv_replycontent.setText(item.getRepDto().getRc());
                tv_replytime.setText(item.getRepDto().getRtTime());
                replyImageSize = item.getRepDto().getUrls().size();
                ll_rerly.setVisibility(View.VISIBLE);
            } else {
                replyImageSize = 0;
            }
            if (replyImageSize > 0) {
                re_horizon.setVisibility(View.VISIBLE);
                switch (replyImageSize) {
                    case 1:
                        reply_imageone.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        break;
                    case 2:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        break;
                    case 3:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        break;
                    case 4:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        reply_imagefour.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(3), reply_imagefour);
                        break;
                    case 5:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        reply_imagefour.setVisibility(View.VISIBLE);
                        reply_imagefive.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(3), reply_imagefour);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(4), reply_imagefive);
                        break;
                }
            } else {
            }

        } else if (item.getCsDto().getSt().equals("-1")) {
            currnet_reply.setText("已过期");
            currnet_reply.setTextColor(mContext.getResources().getColor(R.color.delete_word_color));
            currnet_reply.setEnabled(false);
            int replyImageSize = 0;
            if (item.getRepDto() != null) {
                tv_replycontent.setText(item.getRepDto().getRc());
                tv_replytime.setText(item.getRepDto().getRtTime());
                replyImageSize = item.getRepDto().getUrls().size();
                ll_rerly.setVisibility(View.VISIBLE);
            } else {
                replyImageSize = 0;
            }
            if (replyImageSize > 0) {
                re_horizon.setVisibility(View.VISIBLE);
                switch (replyImageSize) {
                    case 1:
                        reply_imageone.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        break;
                    case 2:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        break;
                    case 3:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        break;
                    case 4:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        reply_imagefour.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(3), reply_imagefour);
                        break;
                    case 5:
                        reply_imageone.setVisibility(View.VISIBLE);
                        reply_imagetwo.setVisibility(View.VISIBLE);
                        reply_imagethree.setVisibility(View.VISIBLE);
                        reply_imagefour.setVisibility(View.VISIBLE);
                        reply_imagefive.setVisibility(View.VISIBLE);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(0), reply_imageone);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(1), reply_imagetwo);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(2), reply_imagethree);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(3), reply_imagefour);
                        MyImageLoder.getInstance().disImage(mContext, item.getRepDto().getUrls().get(4), reply_imagefive);
                        break;
                }
            } else {
            }

        }
    }
}
