package com.bjut.servicedog.servicedog.view.wheelview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.StringWheelAdapter;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.listener.OnStringChangeListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by beibeizhu on 16/11/18.
 */

public class ChoiseStringWheel implements MyOnWheelChangedListener {

    @Bind(R.id.category_wheel)
    MyWheelView categoryWheel;


    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;
    private MyWheelView myWheelView;
    private List<String> mStringList = null;
    private OnStringChangeListener mOnStringChangeListener = null;
    private int base_dimen_40 = 0;

    public ChoiseStringWheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();

    }

    private void initView() {
        base_dimen_40 = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_40);
        parentView = layoutInflater.inflate(R.layout.choose_category_layout, null);
        ButterKnife.bind(this, parentView);
        myWheelView = (MyWheelView) parentView.findViewById(R.id.category_wheel);

        categoryWheel.setVisibleItems(7);


        categoryWheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    private void bindData() {
        StringWheelAdapter stringWheelAdapter = new StringWheelAdapter(context, mStringList);
        stringWheelAdapter.setTextSize(base_dimen_40);
        categoryWheel.setViewAdapter(stringWheelAdapter);
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == categoryWheel) {
        }
    }


    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setData(List<String> stringList) {
        this.mStringList = stringList;
        bindData();
    }

    @OnClick(R.id.confirm_btn)
    public void confirm() {
        if (mOnStringChangeListener != null) {
            int floorIndex = categoryWheel.getCurrentItem();

            String item = null;
            long pid = 0;
            if (mStringList != null && mStringList.size() > floorIndex) {
                item = mStringList.get(floorIndex);
            }
            mOnStringChangeListener.onFoorChange(item);

        }
        cancel();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnFoorChangeListener(OnStringChangeListener onStringChangeListener) {
        this.mOnStringChangeListener = onStringChangeListener;
    }


}
