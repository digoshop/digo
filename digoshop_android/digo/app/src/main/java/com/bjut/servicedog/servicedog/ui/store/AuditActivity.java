package com.bjut.servicedog.servicedog.ui.store;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.view.MyOnlyDialog;

public class AuditActivity extends BaseActivity {
    private TextView tv_title;
    private ImageView img_right;
    private ImageView to_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_main));
        img_right = (ImageView) findViewById(R.id.img_right);
        to_back = (ImageView) findViewById(R.id.to_back);
        to_back.setVisibility(View.GONE);
        img_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        // 弹出PopupWindow的具体代码
        showPopwindow();
    }


    /**
     * 显示popupWindow
     */
    private void showPopwindow() {

        if (mMyOnlyDialog == null) {
            mMyOnlyDialog = new MyOnlyDialog(mContext);
        }
        mMyOnlyDialog.setTitle("温馨提示");
        mMyOnlyDialog.setMessage("您店铺提交的资质,系统正在审核中，请您稍后再次登录");
        mMyOnlyDialog.setOkOnclickListener("退出系统", new MyOnlyDialog.onOkOnclickListener() {
            @Override
            public void onOkClick() {
                ActivityCollector.finishAll();
                System.exit(0);
            }
        });
        mMyOnlyDialog.show();

    }


    @Override
    public void onClick(View view) {

    }
}
