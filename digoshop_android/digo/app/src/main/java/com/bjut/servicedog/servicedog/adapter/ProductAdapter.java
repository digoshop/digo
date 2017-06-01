package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.RunKind;

/**
 * Created by zzr on 16/11/2.
 */
public class ProductAdapter extends MyBaseAdpter<RunKind> {
    public ProductAdapter(Context context) {
        super(context);
    }

    private boolean isCheckAll = false;
    private String[] itemSelect;


    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    private OnCheckedChangeListener onCheckedChangeListener;


    public interface OnCheckedChangeListener {
        void onCheck(boolean isChecked, RunKind kind);
    }

    public void setCheckAll(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
        notifyDataSetChanged();
    }


    @Override
    public int getContentView() {
        return R.layout.textview_runkinds;
    }

    @Override
    public void onInitView(View view, final int position) {
        CheckBox cbKind = get(view, R.id.cb_kind);
        cbKind.setText(getItem(position).getName());
        cbKind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChangeListener.onCheck(isChecked, getItem(position));

            }
        });
        if (isCheckAll) {
            cbKind.setChecked(isCheckAll);
        } else {
            cbKind.setChecked(isCheckAll);
            if (itemSelect != null) {
                for (String s : itemSelect) {
                    if (getItem(position).getName().equals(s)) {
                        cbKind.setChecked(true);
                        break;
                    } else {
                        cbKind.setChecked(false);
                    }
                }
            }
        }


    }

    public void setCheck(String[] select) {
        this.itemSelect = select;
        notifyDataSetChanged();
    }
}
