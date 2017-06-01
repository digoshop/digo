package com.bjut.servicedog.servicedog.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bjut.servicedog.servicedog.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by zzr on 16/10/26.
 */
public class MyImageLoder {

    private static MyImageLoder INSTANCE;

    private MyImageLoder() {
    }

    public static MyImageLoder getInstance() {
        if (INSTANCE == null) {
            synchronized (MyImageLoder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyImageLoder();
                }
            }
        }
        return INSTANCE;
    }

    public void disImage(Context context, String imgUrl, ImageView imageView) {
        disImage(context, imgUrl, R.drawable.default180_120, R.drawable.default180_120, imageView);
    }

    public void disImage(Context context, String imgUrl, ImageView imageView, int dp) {
        disImage(context, imgUrl, R.drawable.default180_120, R.drawable.default180_120, imageView, dp);
    }

    public void disImageF(Context context, String imgUrl, ImageView imageView, int dp) {
        disImage(context, imgUrl, R.drawable.default118_118, R.drawable.default118_118, imageView, dp);
    }

    public void disImageH(Context context, String imgUrl, ImageView imageView, int dp) {
        disImage(context, imgUrl, R.drawable.default700_300, R.drawable.default700_300, imageView, dp);
    }

    public void disImage(Context context, String imgUrl, int placeholder, int errorImg, ImageView imageView) {
        disImage(context, imgUrl, placeholder, errorImg, imageView, 0);
    }


    public void disImage(Context context, String imgUrl, int placeholder, int errorImg, ImageView imageView, int dp) {
        Glide.with(context).load(imgUrl).placeholder(placeholder).error(errorImg).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().transform(new GlideRoundTransform(context, dp)).into(imageView);
    }

    public void disImage(Context context, int resId,ImageView imageView) {
        Glide.with(context).load(resId).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(imageView);
    }

    /**
     * 加载圆角图
     *
     * @param context
     * @param url
     * @param iv
     */
    public void loadCircle(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransform(context)).into(iv);
    }
}
