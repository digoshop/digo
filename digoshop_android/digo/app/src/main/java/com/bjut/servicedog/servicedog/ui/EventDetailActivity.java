package com.bjut.servicedog.servicedog.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Event_data;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by beibeizhu on 16/11/28.
 */

public class EventDetailActivity extends BaseActivity {


    @Bind(R.id.img_pic)
    ImageView mImgPic;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_zhuti)
    TextView mTvZhuTi;
    @Bind(R.id.tv_quyu)
    TextView mTvQuyu;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_start_time)
    TextView mTvStartTime;
    @Bind(R.id.tv_end_time)
    TextView mTvEndTime;
    @Bind(R.id.tv_content)
    TextView mTvContent;

    private Event_data mEvent_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews(){
        mTvTitle.setText(getString(R.string.title_ckhdxx));
        mEvent_data = (Event_data) getIntent().getSerializableExtra("data");
        if (mEvent_data != null) {
            int width = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(context,20);
            int height = width / 7 * 3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
            mImgPic.setLayoutParams(params);
            mImgPic.setScaleType(ImageView.ScaleType.FIT_XY);
            MyImageLoder.getInstance().disImage(this, mEvent_data.getMnp(), R.drawable.default700_300, R.drawable.default700_300, mImgPic, 0);
            mTvZhuTi.setText(mEvent_data.getMnti());
            mTvQuyu.setText(mEvent_data.getCity()+mEvent_data.getArea());
            mTvAddress.setText(mEvent_data.getMnad());
            mTvStartTime.setText(mEvent_data.getStarttime());
            mTvEndTime.setText(mEvent_data.getEndttime());
            mTvContent.setText(mEvent_data.getMnc());
        }
    }

    @Override
    public void onClick(View view) {
        
    }
}
