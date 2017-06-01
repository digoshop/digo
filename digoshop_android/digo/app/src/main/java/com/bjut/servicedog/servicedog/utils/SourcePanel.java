package com.bjut.servicedog.servicedog.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by apple02 on 16/8/18.
 */
public class SourcePanel extends GridView {
    private Context context;

    public SourcePanel(Context context) {

        super(context);

    }

    public SourcePanel(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public SourcePanel(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            return true;  //禁止GridView滑动

        }


        return super.dispatchTouchEvent(ev);


    }
}
