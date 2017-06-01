package com.bjut.servicedog.servicedog.ui.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.GoodsDetail;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.product.ProductDetailActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.ScreenUtil;
import com.bjut.servicedog.servicedog.view.LocalImageHolderView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beibeizhu on 17/2/17.
 */

public class AuctionProductDetailActivity extends BaseActivity {

    private TextView tv_title;
    private LinearLayout ll_product_info;
//    private ImageView img_product_url;

//    private TextView tv_product_favorable_price;
//    private TextView tv_product_market_price;
//    private TextView tv_product_status;

    private ConvenientBanner mConvenientBanner;
    private TextView tv_product_name;
    private TextView tv_product_record;
    private TextView tv_auction_integral;
    private TextView tv_auction_money;
    private TextView tv_auction_date;
    private TextView tv_num;
    private TextView tv_auction_guize;

    private Product mProduct;
    private String productId = "";

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_product_detail);
        initViews();
        setListener();
    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(Constant.AUTO_TIME);
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        mConvenientBanner.stopTurning();
    }

    private void setListener() {
        tv_product_name.setOnClickListener(this);
        tv_product_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_product_name:
                intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
            case R.id.tv_product_record:
                intent = new Intent(mContext, AuctionRecordListActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
        }
    }

    private void initViews() {
        ll_product_info = (LinearLayout) findViewById(R.id.ll_product_info);
//        img_product_url = (ImageView) findViewById(img_product_url);
//        tv_product_favorable_price = (TextView) findViewById(tv_product_favorable_price);
//        tv_product_market_price = (TextView) findViewById(R.id.tv_product_market_price);
//        tv_product_status = (TextView) findViewById(tv_product_status);

        tv_title = (TextView) findViewById(R.id.tv_title);
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_record = (TextView) findViewById(R.id.tv_product_record);
        tv_auction_integral = (TextView) findViewById(R.id.tv_auction_integral);
        tv_auction_money = (TextView) findViewById(R.id.tv_auction_money);
        tv_auction_date = (TextView) findViewById(R.id.tv_auction_date);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_auction_guize = (TextView) findViewById(R.id.tv_auction_guize);

//        tv_product_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//        int width = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(context,20);
        int width = ScreenUtil.getScreenWidth(this);
//        int height = width / 3 * 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        mConvenientBanner.setLayoutParams(params);

//        img_product_url.setScaleType(ImageView.ScaleType.FIT_XY);

        tv_title.setText("竞拍详情");
        productId = getIntent().getStringExtra("productId");
        Log.i(TAG, "productId====" + productId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        getProductInfo();
    }

    private void getProductInfo() {

        showProgressDialog();
        String urlString = "product/productDetail.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pid", productId);
        map.put("pt", Constant.AUCTION_PRODUCT + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                closeProgressDialog();
                String json = responseInfo.result;
                try {
                    GoodsDetail goodsDetail = JSON.parseObject(json, GoodsDetail.class);
                    if (goodsDetail.getE().getCode() == 0) {
                        mProduct = goodsDetail.getData().getAuctionProductInfo();
                        setView();
                    } else {
                        toast(goodsDetail.getE().getDesc());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                    }
//                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeProgressDialog();
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void setView() {
//        MyImageLoder.getInstance().disImage(mContext, mProduct.getPpi(), img_product_url);

//        tv_product_market_price.setText("¥" + mProduct.getPpr());
//        tv_product_favorable_price.setText("¥" + mProduct.getEpp() + "+" + mProduct.getEpg() + "币");
//
//        int status = mProduct.getPst();
//        switch (status) {
//            case Constant.PRODUCT_STATUS_AUDIT:
//                tv_product_status.setText("审核中");
//                break;
//            case Constant.PRODUCT_STATUS_NOT_PASSED:
//                tv_product_status.setText("未通过");
//                break;
//            case Constant.PRODUCT_STATUS_PASSED:
//                tv_product_status.setText("已通过");
//                break;
//            case Constant.PRODUCT_STATUS_EXCHANGE:
//                tv_product_status.setText("兑换中");
//                break;
//            case Constant.PRODUCT_STATUS_EXCHANGE_STOP:
//                tv_product_status.setText("已结束");
//                break;
//            case Constant.PRODUCT_STATUS_EXCHANGE_NOTSTART:
//                tv_product_status.setText("未开始");
//                break;
////            case Constant.PRODUCT_STATUS_EXCHANGE_SHELF:
////                tvStatus.setText("已下架");
////                btnUse.setText("上架");
////                btnUse.setVisibility(View.VISIBLE);
////                imgUpdate.setVisibility(View.GONE);
////                img_delete.setVisibility(View.VISIBLE);
////                break;
//            default:
//                tv_product_status.setText("已下架");
//                break;
//        }
        List<String> imageList = mProduct.getPa();

        if (imageList.size() > 1) {
            mConvenientBanner.setCanLoop(true);
            //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            mConvenientBanner.
                    setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        } else {
            mConvenientBanner.setCanLoop(false);
        }

        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, imageList);
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响

        tv_product_name.setText(mProduct.getPna());
        tv_auction_integral.setText(mProduct.getAplg() + "");
        tv_auction_money.setText(mProduct.getApp() + "");
        tv_auction_date.setText(mProduct.getApsdTime());
        tv_num.setText(mProduct.getApg());
        tv_auction_guize.setText(mProduct.getApd());
    }
}
