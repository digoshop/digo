package com.bjut.servicedog.servicedog.ui.store;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.Menu;
import com.bjut.servicedog.servicedog.po.StoreInfo;
import com.bjut.servicedog.servicedog.po.Update;
import com.bjut.servicedog.servicedog.po.UpdateInfo;
import com.bjut.servicedog.servicedog.ui.OperationAnalyseActivity_f;
import com.bjut.servicedog.servicedog.ui.activies.ActiviesManagerActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.coupon.CouponManagerNewActivity;
import com.bjut.servicedog.servicedog.ui.customized.Customizationactivity;
import com.bjut.servicedog.servicedog.ui.product.ProductManagerActivity;
import com.bjut.servicedog.servicedog.ui.promotion.PromotionManagerActivity;
import com.bjut.servicedog.servicedog.ui.setting.StoreSettingActivity;
import com.bjut.servicedog.servicedog.ui.settlement.ConsumeFinishActivity_r;
import com.bjut.servicedog.servicedog.ui.union.UnionListActivity;
import com.bjut.servicedog.servicedog.ui.vip.VipManageActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.bjut.servicedog.servicedog.view.MyOnlyDialog;
import com.chiclam.download.ApkUpdateUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements View.OnTouchListener {
    private LinearLayout one, two, three, four;
    private LinearLayout linear_lianmeng;
    private ImageView  image4, img_cuxiao, shangping, kaquan, huodong, xiaofei, jingying, iv_one;
    private TextView four1, tv_cuxiao, tv_shangping, tv_kaquan, tv_huodong, tv_xiaofei, tv_jingying;
    private String image = "";
    private int s_flag = 0;//从店铺设置那里拿到图片
    private String shopManage, shopStatus;
    private Intent intent;
    private ImageView main_logo;
    private ImageView to_back;
    private ImageView img_right;
    private TextView tv_title;
    private My2BDialog mMyOpenDialog;
    //    private Button btn_mdgl;
    private LinearLayout ll_store;

    private View view;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    closeProgressDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStore();
        initViews();
        getShopType();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        iv_one.setImageResource(R.drawable.site01);
        String shopimage = pref.getString("shopimage", "");
        if (shopimage != null && shopimage.length() > 0) {
            MyImageLoder.getInstance().loadCircle(this, shopimage, main_logo);
        } else {
            main_logo.setImageResource(R.drawable.default_108_108);
        }
    }

    public void getShopType() {
        intent = getIntent();
        if (intent != null) {
            shopManage = intent.getStringExtra("shop_manage");
            shopStatus = intent.getStringExtra("shopStatus");
            if (shopManage != null) {
                editor.putString("shopManage", shopManage);
                editor.commit();
            }
            if (shopStatus != null) {
                editor.putString("shopStatus", shopStatus);
                editor.commit();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String sType = pref.getString("shopManage", "");
        String sStatus = pref.getString("shopStatus", "");
        if (sType.equals("0")) {

            if (mMyDialog == null) {
                mMyDialog = new My2BDialog(mContext);
            }
            mMyDialog.setTitle("未进行门店管理");
            mMyDialog.setMessage("是否管理?");
            mMyDialog.setYesOnclickListener("确定", new My2BDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    mMyDialog.dismiss();
                    myIntentR(StoreManagerActivity.class);
                }
            });
            mMyDialog.setNoOnclickListener("退出", new My2BDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    mMyDialog.dismiss();
                    ActivityCollector.finishAll();
                }
            });
            mMyDialog.show();
        } else {
            if (sStatus.equals("2")) {
                mMyOpenDialog = new My2BDialog(mContext);
                mMyOpenDialog.setTitle("未开启店铺");
                mMyOpenDialog.setMessage("您的店铺目前处于禁用状态，请及时修改，以免给您的正常使用带来不便！");
                mMyOpenDialog.setCancelable(false);
                mMyOpenDialog.setYesOnclickListener("确定", new My2BDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        mMyOpenDialog.dismiss();
                        myIntentR(StoreSettingActivity.class);
                    }
                });
                mMyOpenDialog.setNoOnclickListener("退出", new My2BDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        mMyOpenDialog.dismiss();
                        ActivityCollector.finishAll();
                    }
                });
                mMyOpenDialog.show();
            }


        }
    }


    private void initViews() {
        main_logo = (ImageView) findViewById(R.id.main_logo);
        to_back = (ImageView) findViewById(R.id.to_back);
        img_right = (ImageView) findViewById(R.id.img_right);
        tv_title = (TextView) findViewById(R.id.tv_title);
        one = (LinearLayout) findViewById(R.id.linear_one);
        two = (LinearLayout) findViewById(R.id.linear_two);
        three = (LinearLayout) findViewById(R.id.linear_three);
        four = (LinearLayout) findViewById(R.id.linear_four);
        linear_lianmeng = (LinearLayout) findViewById(R.id.linear_lianmeng);
        image4 = (ImageView) findViewById(R.id.iv_four);
        img_cuxiao = (ImageView) findViewById(R.id.img_cuxiao);
        kaquan = (ImageView) findViewById(R.id.kaquan);
        shangping = (ImageView) findViewById(R.id.shangping);
        huodong = (ImageView) findViewById(R.id.huodong);
        xiaofei = (ImageView) findViewById(R.id.xiaofei);
        jingying = (ImageView) findViewById(R.id.jingying);
        iv_one = (ImageView) findViewById(R.id.iv_one);
        four1 = (TextView) findViewById(R.id.tv_four);
        tv_cuxiao = (TextView) findViewById(R.id.tv_cuxiao);
        tv_shangping = (TextView) findViewById(R.id.tv_shangpin);
        tv_kaquan = (TextView) findViewById(R.id.tv_kaquan);
        tv_huodong = (TextView) findViewById(R.id.tv_huodong);
        tv_jingying = (TextView) findViewById(R.id.tv_jingying);
        tv_xiaofei = (TextView) findViewById(R.id.tv_xiaofei);
//        btn_mdgl = (Button) findViewById(btn_mdgl);
        ll_store = (LinearLayout) findViewById(R.id.ll_store);
        img_right.setOnTouchListener(this);

        linear_lianmeng.setAlpha(1);
        linear_lianmeng.setOnClickListener(this);
//        btn_mdgl.setOnTouchListener(this);

        initView();
        setView();
        if (pref.getString("update_if", "").equals("true")) {
            Update update = JSON.parseObject(pref.getString("update_json", ""), Update.class);
            askUpdate(update.getData());
        }
    }

    public void initView() {
        tv_title.setText(getString(R.string.title_main));
        to_back.setVisibility(View.GONE);
        main_logo.setVisibility(View.VISIBLE);
        img_right.setVisibility(View.VISIBLE);

        img_cuxiao.setImageResource(R.drawable.cxgl);
        tv_cuxiao.setTextColor(Color.parseColor("#cacaca"));
        tv_kaquan.setTextColor(Color.parseColor("#cacaca"));
        kaquan.setImageResource(R.drawable.kjgl);
        tv_shangping.setTextColor(Color.parseColor("#cacaca"));
        shangping.setImageResource(R.drawable.spgl);
        huodong.setImageResource(R.drawable.hdgl);
        tv_huodong.setTextColor(Color.parseColor("#cacaca"));
        xiaofei.setImageResource(R.drawable.xfjs);
        tv_xiaofei.setTextColor(Color.parseColor("#cacaca"));
        jingying.setImageResource(R.drawable.jyfx);
        tv_jingying.setTextColor(Color.parseColor("#cacaca"));

    }

    private void setView() {
        try {
            JSONObject auth = new JSONObject(pref.getString("auth", ""));
            List<Menu> homeList = JSON.parseArray(auth.getJSONArray("home").toString(), Menu.class);
            List<Menu> tabList = JSON.parseArray(auth.getJSONArray("tabbar").toString(), Menu.class);
            for (int i = 0; i < homeList.size(); i++) {

                if (img_cuxiao.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    img_cuxiao.setOnTouchListener(this);
                    tv_cuxiao.setTextColor(getResources().getColor(R.color.word_color));
                    img_cuxiao.setImageResource(R.drawable.cuxiaoguanli);
                }

                if (kaquan.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    kaquan.setOnTouchListener(this);
                    kaquan.setImageResource(R.drawable.kaquan);
                    tv_kaquan.setTextColor(getResources().getColor(R.color.word_color));
                }

                if (shangping.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    shangping.setOnTouchListener(this);
                    shangping.setImageResource(R.drawable.shangpinguanli);
                    tv_shangping.setTextColor(getResources().getColor(R.color.word_color));
                }

                if (huodong.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    huodong.setOnTouchListener(this);
                    huodong.setImageResource(R.drawable.huodong);
                    tv_huodong.setTextColor(getResources().getColor(R.color.word_color));
                }

                if (xiaofei.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    xiaofei.setOnTouchListener(this);
                    xiaofei.setImageResource(R.drawable.xiaofeijiesuan);
                    tv_xiaofei.setTextColor(getResources().getColor(R.color.word_color));
                }

                if (jingying.getTag().toString().equals(homeList.get(i).getMenu_tag())) {
                    jingying.setOnTouchListener(this);
                    jingying.setImageResource(R.drawable.jingyingfenxi);
                    tv_jingying.setTextColor(getResources().getColor(R.color.word_color));
                }
            }
            for (int i = 0; i < tabList.size(); i++) {
                if (two.getTag().toString().equals(tabList.get(i).getMenu_tag())) {
                    two.setOnClickListener(this);
                    two.setAlpha((float) 1);

                } else if (three.getTag().toString().equals(tabList.get(i).getMenu_tag())) {
                    three.setOnClickListener(this);
                    three.setAlpha((float) 1);

                } else if (four.getTag().toString().equals(tabList.get(i).getMenu_tag())) {
                    four.setAlpha((float) 1);
                    four.setOnTouchListener(this);
                } else if (ll_store.getTag().toString().equals(tabList.get(i).getMenu_tag())) {
                    ll_store.setAlpha((float) 1);
                    ll_store.setOnClickListener(this);
                }
            }
        } catch (JSONException e) {
            toast("获取权限失败");
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (pref.getString("ifshopimgchange", "").equals("1")) {
            editor.putString("ifshopimgchange", "0");
            editor.commit();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view.getId() == R.id.img_right) {
                    img_right.setImageResource(R.drawable.shezhi);
                    myIntentR(ShopSettingActivity.class);
                }
//                if (view.getId() == R.id.linear_two) {
//                    two1.setTextColor(Color.parseColor("#f39800"));
//                    image2.setImageResource(R.drawable.huiyuany);
//                    iv_one.setImageResource(R.drawable.site02);
//                    myIntentR(VipManageActivity.class);
//                }
//                if (view.getId() == R.id.linear_three) {
//                    three1.setTextColor(Color.parseColor("#f39800"));
//                    image3.setImageResource(R.drawable.dingzhiy);
//                    iv_one.setImageResource(R.drawable.site02);
//                    myIntentSign(Customizationactivity.class, "change");
//                }
                if (view.getId() == R.id.linear_four) {
                    four1.setTextColor(Color.parseColor("#f39800"));
                    image4.setImageResource(R.drawable.xiaoxiy);
//                    myIntentR(InformationManageActivity_d.class);
                }
                if (view.getId() == R.id.img_cuxiao) {
                    tv_cuxiao.setTextColor(Color.parseColor("#f39800"));
                    myIntentR(PromotionManagerActivity.class);
                }
                if (view.getId() == R.id.shangping) {
                    tv_shangping.setTextColor(Color.parseColor("#f39800"));
                    myIntentR(ProductManagerActivity.class);
                }
                if (view.getId() == R.id.kaquan) {
                    tv_kaquan.setTextColor(Color.parseColor("#f39800"));
//                    myIntentR(DiscountCouponActivity_r.class);
                    myIntentR(CouponManagerNewActivity.class);
//                    myIntentR(CouponManagerActivity.class);
                }
                if (view.getId() == R.id.huodong) {
                    tv_huodong.setTextColor(Color.parseColor("#f39800"));
                    myIntentR(ActiviesManagerActivity.class);
//                    myIntentR(DiscountEventActivity_f.class);
                }
                if (view.getId() == R.id.xiaofei) {
                    tv_xiaofei.setTextColor(Color.parseColor("#f39800"));
                    myIntentR(ConsumeFinishActivity_r.class);

                }
                if (view.getId() == R.id.jingying) {
                    tv_jingying.setTextColor(Color.parseColor("#f39800"));
                    myIntentR(OperationAnalyseActivity_f.class);
                }
                break;
            case MotionEvent.ACTION_UP:
                four1.setTextColor(Color.parseColor("#454545"));
                image4.setImageResource(R.drawable.xiaoxi);
                tv_cuxiao.setTextColor(Color.parseColor("#454545"));
                tv_shangping.setTextColor(Color.parseColor("#454545"));
                tv_kaquan.setTextColor(Color.parseColor("#454545"));
                tv_huodong.setTextColor(Color.parseColor("#454545"));
                tv_xiaofei.setTextColor(Color.parseColor("#454545"));
                tv_jingying.setTextColor(Color.parseColor("#454545"));
//                iv_one.setImageResource(R.drawable.site01);
                img_right.setImageResource(R.drawable.shezhi);
                break;
        }
        return true;
    }

    private void askUpdate(final UpdateInfo updateInfo) {

        final MyOnlyDialog mMyDialog = new MyOnlyDialog(mContext);
        mMyDialog.setTitle("版本更新");
        mMyDialog.setCancelable(false);
        mMyDialog.setMessage("新版本 " + updateInfo.getNew_version() + " ,大小" + updateInfo.getTarget_size() + " ," + updateInfo.getU_log());
        mMyDialog.setOkOnclickListener("下载安装", new MyOnlyDialog.onOkOnclickListener() {
            @Override
            public void onOkClick() {
                downloadNew(updateInfo.getDown_url());
                mMyDialog.dismiss();
                editor.putString("update_if", "false");
                editor.commit();
            }
        });
        mMyDialog.show();
    }

    private void downloadNew(String url) {
        if (!canDownloadState()) {
            toast("下载服务不用,请您启用");
            showDownloadSetting();
            return;
        }
        //测试所用url 注释即可
//        url = "http://img.digoshop.com/merchant/2016-09-13/app1.1.apk";
        ApkUpdateUtils.download(this, url, getResources().getString(com.chiclam.download.R.string.app_name));
    }

    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void getStore() {
        showProgressDialog();
        String urlString = "business/shop_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    JSONObject brandJson = new JSONObject(data);
                    StoreInfo storeInfo = JSON.parseObject(json, StoreInfo.class);
                    editor.putString("nid", storeInfo.getData().getNid());
                    editor.putString("nation", storeInfo.getData().getNation());
                    List<String> contactPhone = storeInfo.getData().getContact_phone();
                    editor.putString("contact_phone", contactPhone.get(0));
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                toast(Constant.CHECK_NETWORK);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_store:
                myIntentR(StoreManagerActivity.class);
                break;
            case R.id.linear_two:
                myIntentR(VipManageActivity.class);
                break;
            case R.id.linear_three:
                myIntentR(Customizationactivity.class);
                break;
            case R.id.linear_lianmeng:
                myIntentR(UnionListActivity.class);
                break;
        }
    }
}
