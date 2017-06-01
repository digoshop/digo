package com.bjut.servicedog.servicedog.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;

/**
 * Created by beibeizhu on 17/1/6.
 */

public class MyOnlyDialog extends Dialog {

    private Button ok;//确定按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String okStr;

    private onOkOnclickListener mOnOkOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param okOnclickListener
     */
    public void setOkOnclickListener(String str, onOkOnclickListener okOnclickListener) {
        if (str != null) {
            okStr = str;
        }
        this.mOnOkOnclickListener = okOnclickListener;
    }

    public MyOnlyDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_onlyone_button);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOkOnclickListener != null) {
                    mOnOkOnclickListener.onOkClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (okStr != null) {
            ok.setText(okStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ok = (Button) findViewById(R.id.btn_ok);
        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (TextView) findViewById(R.id.message);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮被点击的接口
     */
    public interface onOkOnclickListener {
        public void onOkClick();
    }

}