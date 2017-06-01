package com.bjut.servicedog.servicedog.rc_adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Event_data;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by beibeizhu on 17/3/6.
 */

public class ActivitiesAdapter extends BaseQuickAdapter<Event_data, BaseViewHolder> {
    public ActivitiesAdapter() {
        super(R.layout.item_discount_event_list_f);
    }

    @Override
    protected void convert(BaseViewHolder helper, Event_data item) {

        String date = DateUtil.strFormatStr(item.getCreateTime());
        long endTime = item.getMnved();
        long time = System.currentTimeMillis() / 1000;
        int mnas = item.getMnas();
        String mnst = item.getMnst();
        String show = item.getShow();

        ImageView imageView = helper.getView(R.id.event_ad);
        LinearLayout linearLayout2 = helper.getView(R.id.Lput);
        LinearLayout linearLayout3 = helper.getView(R.id.Ledit);
        LinearLayout linearLayout4 = helper.getView(R.id.Ldelete);
        LinearLayout linearLayout5 = helper.getView(R.id.Loff);

        String s = "";
        if (endTime < time) {
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout5.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.VISIBLE);
            s = "已过期";
        } else {

            if (mnas == 1 || mnas == 2) {
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);
                linearLayout5.setVisibility(View.GONE);
                s = "审核中";

            } else if (mnas == 3 || mnas == 4) {
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);
                linearLayout5.setVisibility(View.GONE);
                s = "已驳回";
            } else if (mnas == 5) {

                if ("1".equals(mnst)) {
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    linearLayout5.setVisibility(View.VISIBLE);
                } else {
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout4.setVisibility(View.VISIBLE);
                    linearLayout5.setVisibility(View.GONE);
                    if ("1".equals(show)) {
                        linearLayout3.setVisibility(View.GONE);
                    } else {
                        linearLayout3.setVisibility(View.VISIBLE);
                    }
                }
                s = "已通过";
            }
        }

        helper.setText(R.id.event_title, item.getMnti())
                .setText(R.id.discount_date, date)
                .setText(R.id.discount_status, s)
                .addOnClickListener(R.id.Lput)
                .addOnClickListener(R.id.Ledit)
                .addOnClickListener(R.id.Ldelete)
                .addOnClickListener(R.id.Loff);

        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dip2px(context, 20);
        int height = width / 7 * 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        imageView.setLayoutParams(params);
        MyImageLoder.getInstance().disImageH(mContext, item.getMnp(), (ImageView) helper.getView(R.id.event_ad), 0);
    }
}
