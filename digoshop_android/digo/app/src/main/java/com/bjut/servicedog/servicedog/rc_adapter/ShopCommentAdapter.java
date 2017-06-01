package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Shopcomment_comments;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by beibeizhu on 17/3/17.
 */

public class ShopCommentAdapter extends BaseQuickAdapter<Shopcomment_comments,BaseViewHolder> {
    public ShopCommentAdapter() {
        super(R.layout.item_shop_comment_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Shopcomment_comments item) {

        TextView comment_name = helper.getView(R.id.comment_name);
        TextView comment_kind = helper.getView(R.id.comment_kind);
        TextView comment_date = helper.getView(R.id.comment_date);
        TextView comment_detail = helper.getView(R.id.comment_detail);
        TextView reply_save = helper.getView(R.id.reply_save);
        TextView ed_reply_detail = helper.getView(R.id.ed_reply_detail);

        ImageView comment_imageone = helper.getView(R.id.comment_imageone);
        ImageView comment_imagetwo = helper.getView(R.id.comment_imagetwo);
        ImageView comment_imagethree = helper.getView(R.id.comment_imagethree);
        ImageView comment_imagefour = helper.getView(R.id.comment_imagefour);
        ImageView comment_imagefive = helper.getView(R.id.comment_imagefive);

        ImageView comment_headimg = helper.getView(R.id.comment_headimg);
        ImageView comment_image_kind = helper.getView(R.id.comment_image_kind);

        HorizontalScrollView c_horizion = helper.getView(R.id.c_horizion);

        LinearLayout ll_reply = helper.getView(R.id.ll_reply);
        View my_view = helper.getView(R.id.my_view);

        helper.addOnClickListener(R.id.reply_save);

        long str = item.getCreate_time();
        Date date = new Date(str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(str);

        comment_date.setText(time);
        comment_name.setText(item.getUserinfo().getNick());
        comment_detail.setText(item.getText());

        int success = item.getStatus();
        Shopcomment_comments.ReplyEntity reply = item.getReply();
        if (success == 1) {
            reply_save.setText("");
            reply_save.setEnabled(false);
            ll_reply.setVisibility(View.VISIBLE);
            ed_reply_detail.setVisibility(View.VISIBLE);
            ed_reply_detail.setText(reply.getText());
            my_view.setVisibility(View.VISIBLE);

        } else {
            ll_reply.setVisibility(View.GONE);
            reply_save.setText("回复");
            reply_save.setEnabled(true);
            ed_reply_detail.setVisibility(View.GONE);
            my_view.setVisibility(View.GONE);
        }

        int kind = 0;
        kind = item.getType();
        if (kind == 0) {
            comment_image_kind.setImageResource(R.drawable.dppj_hao);
            comment_kind.setText("好评");
        }
        if (kind == 1) {
            comment_image_kind.setImageResource(R.drawable.dppj_zhong);
            comment_kind.setText("中评");
        }
        if (kind == 2) {
            comment_image_kind.setImageResource(R.drawable.dppj_cha);
            comment_kind.setText("差评");
        }
        comment_imageone.setVisibility(View.GONE);
        comment_imagetwo.setVisibility(View.GONE);
        comment_imagethree.setVisibility(View.GONE);
        comment_imagefour.setVisibility(View.GONE);
        comment_imagefive.setVisibility(View.GONE);

        int size = item.getImgs().size();
        if (size > 0) {
            c_horizion.setVisibility(View.VISIBLE);
            switch (size){
                case 1:
                    comment_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext,item.getImgs().get(0),comment_imageone);
                    break;
                case 2:
                    comment_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext,item.getImgs().get(0),comment_imageone);
                    comment_imagetwo.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(1), comment_imagetwo);
                    break;
                case 3:
                    comment_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext,item.getImgs().get(0),comment_imageone);
                    comment_imagetwo.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(1), comment_imagetwo);
                    comment_imagethree.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(2), comment_imagethree);
                    break;
                case 4:
                    comment_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext,item.getImgs().get(0),comment_imageone);
                    comment_imagetwo.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(1), comment_imagetwo);
                    comment_imagethree.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(2), comment_imagethree);
                    comment_imagefour.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(3), comment_imagefour);
                    break;
                case 5:
                    comment_imageone.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext,item.getImgs().get(0),comment_imageone);
                    comment_imagetwo.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(1), comment_imagetwo);
                    comment_imagethree.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(2), comment_imagethree);
                    comment_imagefour.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(3), comment_imagefour);
                    comment_imagefive.setVisibility(View.VISIBLE);
                    MyImageLoder.getInstance().disImage(mContext, item.getImgs().get(4), comment_imagefive);
                    break;
            }
        } else {
            c_horizion.setVisibility(View.GONE);
        }

        MyImageLoder.getInstance().loadCircle(mContext,item.getUserinfo().getAvatar(), comment_headimg);
    }
}
