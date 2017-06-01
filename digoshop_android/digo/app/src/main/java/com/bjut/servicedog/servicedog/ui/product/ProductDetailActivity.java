package com.bjut.servicedog.servicedog.ui.product;

import android.os.Bundle;
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
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.ScreenUtil;
import com.bjut.servicedog.servicedog.view.LocalImageHolderView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjut.servicedog.servicedog.R.id.convenientBanner;

/**
 * Created by beibeizhu on 17/2/17.
 */

public class ProductDetailActivity extends BaseActivity {

//    private ImageView img_product_url;
//
//    private TextView tv_product_favorable_price;
//    private TextView tv_product_market_price;
//    private TextView tv_product_status;

    private ConvenientBanner mConvenientBanner;
    //    private TextView tv_product_name;
    private TextView tv_type;
    private TextView tv_brand;
    private TextView tv_title;
    private TextView tv_name;
    private TextView tv_no;
    private TextView tv_market_price;
    private TextView tv_favorable_price;
    private TextView tv_stock;
    private TextView tv_property;
    private TextView tv_introduce;
//    private TextView tv_notice;
//    private RecyclerView mRecyclerView;

    private Product mProduct;
    private String productId = "";
    private List<String> imageList = new ArrayList<>();
//    private ImageAdapter mImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initViews();
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


    @Override
    public void onClick(View view) {

    }

    private void initViews() {
        mConvenientBanner = (ConvenientBanner) findViewById(convenientBanner);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_no = (TextView) findViewById(R.id.tv_no);
        tv_market_price = (TextView) findViewById(R.id.tv_market_price);
        tv_favorable_price = (TextView) findViewById(R.id.tv_favorable_price);
        tv_stock = (TextView) findViewById(R.id.tv_stock);
        tv_property = (TextView) findViewById(R.id.tv_property);
        tv_introduce = (TextView) findViewById(R.id.tv_introduce);

        int width = ScreenUtil.getScreenWidth(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        mConvenientBanner.setLayoutParams(params);
        mConvenientBanner.setCanLoop(true);

        tv_title.setText("商品详情");

        productId = getIntent().getStringExtra("productId");
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Uri uri = intent.getData();
//            if (uri != null) {
//                productId = uri.getQueryParameter("pid");
//            }
//        } else {
//            productId = getIntent().getStringExtra("productId");
//        }

        Log.i(TAG, "productId====" + productId);
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
        map.put("pt", Constant.PRODUCT + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            GoodsDetail goodsDetail = JSON.parseObject(json, GoodsDetail.class);
                            if (goodsDetail.getE().getCode() == 0) {
                                mProduct = goodsDetail.getData().getProductInfoDto();
                                setView();
                            } else {
                                toast(goodsDetail.getE().getDesc());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
//        tv_product_name.setText(mProduct.getPna());
//        tv_product_market_price.setText("¥" + mProduct.getPpr());
//        tv_product_favorable_price.setText("¥" + mProduct.getPppr());
//        imageList = mProduct.getPa();
//        mImageAdapter.setNewData(imageList);
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
//            case Constant.PRODUCT_STATUS_SHELVES:
//                tv_product_status.setText("上架中");
//                break;
//            case Constant.PRODUCT_STATUS_SHELF:
//                tv_product_status.setText("下架");
//                break;
//            case Constant.PRODUCT_STATUS_DELETE:
//                tv_product_status.setText("已删除");
//                break;
//        }

        imageList = mProduct.getPa();

        tv_name.setText(mProduct.getPna());
        tv_no.setText(mProduct.getPno());
        tv_type.setText(mProduct.getMon());
        tv_brand.setText(mProduct.getBn());
        tv_market_price.setText(mProduct.getPpr());
        tv_favorable_price.setText(mProduct.getPppr());
        tv_stock.setText(mProduct.getPnu() + "");
        tv_property.setText(mProduct.getPattr());
        tv_introduce.setText(mProduct.getPd());
//        tv_notice.setText(mProduct.getPasa());

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

        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响
    }
}
