package com.bjut.servicedog.servicedog.ui.settlement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ProductTypeAdapter;
import com.bjut.servicedog.servicedog.event.CloseEvent;
import com.bjut.servicedog.servicedog.model.ProductTypeModel;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.NumUtil;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MoneySalePlatActivity_r extends BaseActivity {
    private RelativeLayout confirm;
    private TextView submit, shopname, shopphone, shopuser;
    private TextView tv_money;
    private String order_sn;
    //    private List<String> savemopid = new ArrayList<>();//存储mopid的列表集合
    private TextView tv_title;

    private MyGridView mGridView;
    private List<ProductTypeModel> mProductTypeModelList;
    private ProductTypeAdapter mProductTypeAdapter;

    private Double actualMoney = 0.00;

    private final int SET_TYPE_MONEY = 0;

    private String userId = "";
    private String userName = "";
    private String userPhone = "";
    private String userVip = "";


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_sale_plat_r);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SET_TYPE_MONEY:
                boolean isHasd = false;
                ProductTypeModel productTypeModel = (ProductTypeModel) data.getSerializableExtra("productTypeModel");
                for (ProductTypeModel typeModel : mProductTypeModelList) {
                    if (typeModel.getName().equals(productTypeModel.getName())) {
                        float itemMoney = Float.parseFloat(typeModel.getMoney());
                        float productMoney = Float.parseFloat(productTypeModel.getMoney());
                        float itemNewMoney = itemMoney + productMoney;
                        String newMoney = NumUtil.getFloatNum(itemNewMoney);
                        typeModel.setMoney(newMoney);
                        isHasd = true;
                    }
                }
                if (!isHasd) {
                    mProductTypeModelList.add(productTypeModel);
                }
                mProductTypeAdapter.setList(mProductTypeModelList);
                setTotalMoney();
                break;
        }
    }


    private void init() {
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userPhone = getIntent().getStringExtra("userPhone");
        userVip = getIntent().getStringExtra("userVip");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_txdd));
        mGridView = (MyGridView) findViewById(R.id.gridview);
        mProductTypeModelList = new ArrayList<>();
        mProductTypeAdapter = new ProductTypeAdapter(this);
        mGridView.setAdapter(mProductTypeAdapter);

        confirm = (RelativeLayout) findViewById(R.id.rel_confirm);
        confirm.setOnClickListener(this);
        shopname = (TextView) findViewById(R.id.consume_shopname);
        shopphone = (TextView) findViewById(R.id.consume_shopphone);
        shopuser = (TextView) findViewById(R.id.consume_shopuser);
        shopname.setText(pref.getString("shopname", ""));
        shopphone.setText(pref.getString("contact_phone", ""));
        shopuser.setText(pref.getString("name", ""));
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        tv_money = (TextView) findViewById(R.id.tv_money);
        TextPaint tp = tv_money.getPaint();
        tp.setFakeBoldText(true);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mProductTypeAdapter.removeItem(i);
                setTotalMoney();
            }
        });

    }

    private void setTotalMoney() {
        actualMoney = mProductTypeAdapter.getMoney();
        String actualMoneyStr = NumUtil.Number2Double(actualMoney);
        tv_money.setText(actualMoneyStr + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (mProductTypeModelList.size() <= 0) {
                    toast("请先选择品类");
                    return;
                }
                if (!Utils.isNotFastClick()) {
                    return;
                }
                Double money = mProductTypeAdapter.getMoney();
                if (money>99999999) {
                    toast("本次消费额度超出系统限制，请重新输入");
                    return;
                }
                Intent intent1 = new Intent(MoneySalePlatActivity_r.this, SettlementActivity.class);
                intent1.putExtra("userId", userId);
                intent1.putExtra("userName", userName);
                intent1.putExtra("userPhone", userPhone);
                intent1.putExtra("userVip", userVip);
                intent1.putExtra("dataList", (Serializable) mProductTypeModelList);
                startActivity(intent1);
                break;
            case R.id.rel_confirm:
                Intent inte = new Intent(this, ConfrimRunkindsActivity.class);
                startActivityForResult(inte, SET_TYPE_MONEY);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commitOk(CloseEvent closeEvent) {
        if (closeEvent.isClose()) {
            finish();
        }
    }
}
