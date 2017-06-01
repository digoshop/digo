package com.bjut.servicedog.servicedog.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;

/**
 * Created by beibeizhu on 17/2/23.
 */

public class LocalImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
//        List<String> pat = data.getPat();
        MyImageLoder.getInstance().disImageF(context, data, imageView,0);
    }
}
