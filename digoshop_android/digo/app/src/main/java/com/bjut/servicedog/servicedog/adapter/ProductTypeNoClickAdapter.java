package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ProductTypeModel;
import com.bjut.servicedog.servicedog.utils.ArithUtil;

/**
 * Created by beibeizhu on 16/11/9.
 */

public class ProductTypeNoClickAdapter extends MyBaseAdpter<ProductTypeModel> {

    public ProductTypeNoClickAdapter(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.item_product_type;
    }

    @Override
    public void onInitView(View view, int position) {
        TextView tv_name = get(view,R.id.tv_name);
        TextView tv_money = get(view,R.id.tv_money);

        ProductTypeModel item = getItem(position);

        tv_name.setText(item.getName());
        tv_money.setText("¥"+item.getMoney()+"元");
    }


    public Double getMoney(){
        Double totalMoney = 0.00;
        for (ProductTypeModel productTypeModel : getList()) {
            totalMoney = ArithUtil.add(totalMoney, Double.parseDouble(productTypeModel.getMoney()));
        }
        return totalMoney;
    }
}
