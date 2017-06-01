package com.bjut.servicedog.servicedog.view.wheelview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.UserWheelAdapter;
import com.bjut.servicedog.servicedog.po.Permission_data;
import com.bjut.servicedog.servicedog.utils.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zzr on 16/12/1.
 */

public class UserWheel implements MyOnWheelChangedListener {
    private MyWheelView myUserWheel;
    private OnUserChangeListener  mOnUserChangeListener=null;
    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;
    private List<Permission_data> permissions = null;
    public UserWheel(Activity context) {
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
        parentView = layoutInflater.inflate(R.layout.user_change_layout, null);
        ButterKnife.bind(this, parentView);
        myUserWheel= (MyWheelView) parentView.findViewById(R.id.user_view);
        myUserWheel.setVisibleItems(7);
        myUserWheel.addChangingListener(this);
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
    public void setUserList(List<Permission_data> permissions) {
        this.permissions = permissions;
        bindData();
    }

    private void bindData() {
        int base_dimen_40 = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_40);
        UserWheelAdapter userWheelAdapter = new UserWheelAdapter(context, permissions);
        userWheelAdapter.setTextSize(base_dimen_40);
        myUserWheel.setViewAdapter(userWheelAdapter);
    }
    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }
    @OnClick(R.id.confirm_button)
    public void confirm() {
        if (mOnUserChangeListener!= null) {
            int userIndex = myUserWheel.getCurrentItem();
            
            String userName = null;
            int auth=0;
            if (permissions != null && permissions.size() > userIndex) {
                // AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                userName = permissions.get(userIndex).getAuth_name();
                auth= permissions.get(userIndex).getAuth();
            }

            mOnUserChangeListener.onUserChange(userName,auth);
        }
        cancel();
    }
    
    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        
    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
    public void setOnUserChangeListener(OnUserChangeListener onUserChangeListener) {
        this.mOnUserChangeListener = onUserChangeListener;
    }
    public interface OnUserChangeListener {
        void onUserChange(String userName, int auth);
    }
}
