package com.bjut.servicedog.servicedog.view.pop;

import android.Manifest;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bjut.servicedog.servicedog.R;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by beibeizhu on 17/1/17.
 */

public class PhotoWindows extends PopupWindow {

    private OnPhotoChioseListener mOnPhotoChioseListener;
    private Context mContext;

    private static final int RC_CAMERA_PERM = 124;
    private static final int RC_WRITE_EXTERNAL_STORAGE_PERM = 125;

    public void setOnPhotoChioseListener(OnPhotoChioseListener onPhotoChioseListener) {
        mOnPhotoChioseListener = onPhotoChioseListener;
    }

    public interface OnPhotoChioseListener {
        void onTakePhoto();

        void onChoisePhoto();
    }

    public PhotoWindows(Context context) {
        mContext = context;

        View view = View.inflate(mContext, R.layout.item_popupwindow, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        update();
//-------------------------------------点击事件----------------------------------
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
                cameraTask();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                albumTask();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
            mOnPhotoChioseListener.onTakePhoto();
        } else {
            EasyPermissions.requestPermissions(mContext, mContext.getString(R.string.rationale_camera),
                    RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @AfterPermissionGranted(RC_WRITE_EXTERNAL_STORAGE_PERM)
    public void albumTask() {
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mOnPhotoChioseListener.onChoisePhoto();
        } else {
            EasyPermissions.requestPermissions(mContext, mContext.getString(R.string.rationale_location_contacts),
                    RC_WRITE_EXTERNAL_STORAGE_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
