package com.bjut.servicedog.servicedog.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.bjut.servicedog.servicedog.utils.ToastUtils;

/**
 * Created by beibeizhu on 17/3/29.
 */

public class DecimalEditText extends EditText implements TextWatcher {
    public DecimalEditText(Context context) {
        super(context);
        this.addTextChangedListener(this);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(this);
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                this.setText(s);
                this.setSelection(s.length());
                ToastUtils.showShortToastSafe("最多输入两位小数");
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            this.setText(s);
            this.setSelection(2);
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                this.setText(s.subSequence(0, 1));
                this.setSelection(1);
                return;
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {


    }
}
