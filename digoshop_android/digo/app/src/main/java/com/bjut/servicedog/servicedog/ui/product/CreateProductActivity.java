package com.bjut.servicedog.servicedog.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ImagePublishAdapter;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.po.Brand;
import com.bjut.servicedog.servicedog.po.BrandList;
import com.bjut.servicedog.servicedog.po.Product;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.RunKindList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.CustomConstants;
import com.bjut.servicedog.servicedog.utils.IntentConstants;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.DecimalEditText;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
import com.bjut.servicedog.servicedog.view.MyGridView;
import com.bjut.servicedog.servicedog.view.listener.OnCategortChangeListener;
import com.bjut.servicedog.servicedog.view.pop.PhotoWindows;
import com.bjut.servicedog.servicedog.view.wheelview.ChooseBrandWheel;
import com.bjut.servicedog.servicedog.view.wheelview.ChooseCategoryWheel;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by beibeizhu on 17/2/14.
 */

public class CreateProductActivity extends BaseActivity implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {

    private Button btn_next;
    private Button btn_cancle;
    private TextView tv_type;
    private TextView tv_brand;
    private TextView tv_title;
    private EditText et_name;
    private EditText et_no;
    private DecimalEditText et_market_price;
    private DecimalEditText et_favorable_price;
    private EditText et_stock;
    private EditText et_property;
    private EditText et_introduce;
    //    private EditText et_notice;
    private MyGridView mGridView;

    private PhotoWindows mPhotoWindows;
    private ImagePublishAdapter mAdapter;
    private UploadAli uploadAli;

    private ChooseCategoryWheel mChooseCategoryWheel = null;
    private ChooseBrandWheel mChooseBrandWheel = null;

    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    private List<RunKind> mRunKindList = new ArrayList<>();
    private List<Brand> mBrandList = new ArrayList<>();

    private CustomHelper mCustomHelper;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();

    private Product mProduct;
    private String brandId = "";
    private String operateId = "";
    private String photoUrls = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        initViews();
        setListener();
        initWheelCategory();
        initWheelBrand();
        requestKinds();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IntentConstants.CHECK_PHOTO:
                    mPhotoList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                    mAdapter.setDate(mPhotoList);
                    break;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mTakePhoto.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putSerializable("mPhotoList", (Serializable) mPhotoList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPhotoList = (List<ImageItem>) savedInstanceState.getSerializable("mPhotoList");
        mAdapter = new ImagePublishAdapter(mContext, mPhotoList);
        mAdapter.setFang(true);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void setListener() {
        btn_next.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_brand.setOnClickListener(this);

        mPhotoWindows.setOnPhotoChioseListener(new PhotoWindows.OnPhotoChioseListener() {
            @Override
            public void onTakePhoto() {
                int size = mPhotoList.size();
                if (size < CustomConstants.MAX_IMAGE_SIZE) {
                    int limit = CustomConstants.MAX_IMAGE_SIZE - size;
                    mCustomHelper.setLimit(limit);
                    mCustomHelper.onClick(Constant.PHOTO_TAKE, mTakePhoto);
                } else {

                }
            }

            @Override
            public void onChoisePhoto() {
                int size = mPhotoList.size();
                if (size < CustomConstants.MAX_IMAGE_SIZE) {
                    int limit = CustomConstants.MAX_IMAGE_SIZE - size;
                    mCustomHelper.setLimit(limit);
                    mCustomHelper.onClick(Constant.PHOTO_CHOISE, mTakePhoto);
                } else {

                }
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    mPhotoWindows.showAtLocation(mGridView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(mContext,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) mPhotoList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivityForResult(intent, IntentConstants.CHECK_PHOTO);
                }


            }

        });
    }

    private int getDataSize() {
        return mPhotoList == null ? 0 : mPhotoList.size();
    }

    private void initViews() {
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_name = (EditText) findViewById(R.id.et_name);
        et_no = (EditText) findViewById(R.id.et_no);
        et_market_price = (DecimalEditText) findViewById(R.id.et_market_price);
        et_favorable_price = (DecimalEditText) findViewById(R.id.et_favorable_price);
        et_stock = (EditText) findViewById(R.id.et_stock);
        et_property = (EditText) findViewById(R.id.et_property);
        et_introduce = (EditText) findViewById(R.id.et_introduce);
//        et_notice = (EditText) findViewById(et_notice);
        mGridView = (MyGridView) findViewById(R.id.gridview);

        tv_title.setText("添加商品");
        mPhotoWindows = new PhotoWindows(this);
        mAdapter = new ImagePublishAdapter(this, mPhotoList);
        mAdapter.setFang(true);
        mGridView.setAdapter(mAdapter);
        mProduct = new Product();
        mCustomHelper = new CustomHelper();
        mCustomHelper.setEtCropWidth(1);
        mCustomHelper.setEtCropHeight(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (checkedInput()) {
                    addProduct();
                }
                break;
            case R.id.btn_cancle:
                this.finish();
                break;
            case R.id.tv_type:
                mChooseCategoryWheel.show(v);
                break;
            case R.id.tv_brand:
                if (mBrandList.size() <= 0) {
                    toast("暂无品牌");
                    return;
                }
                mChooseBrandWheel.show(v);
                break;
        }
    }

    private void initWheelCategory() {
        mChooseCategoryWheel = new ChooseCategoryWheel(this);
        mChooseCategoryWheel.setOnFoorChangeListener(new OnCategortChangeListener() {
            @Override
            public void onFoorChange(String floorName, long pid) {
                tv_type.setText(floorName);
                tv_brand.setText("");
                requestBrandsList(pid + "");
                operateId = pid + "";
                brandId = "";
            }
        });
    }

    private void initWheelBrand() {
        mChooseBrandWheel = new ChooseBrandWheel(this);
        mChooseBrandWheel.setOnFoorChangeListener(new OnCategortChangeListener() {
            @Override
            public void onFoorChange(String floorName, long pid) {
                tv_brand.setText(floorName);
                brandId = pid + "";
                Log.i(TAG, "brandId====" + brandId);
            }
        });
    }

    private boolean checkedInput() {
        String name = et_name.getText().toString();
        String no = et_no.getText().toString();
        String type = tv_type.getText().toString();
        String brand = tv_brand.getText().toString();
        String marketPrice = et_market_price.getText().toString();
        String favorablePrice = et_favorable_price.getText().toString();
        String stock = et_stock.getText().toString();
        String property = et_property.getText().toString();
        String introduce = et_introduce.getText().toString();
//        String notice = et_notice.getText().toString();
        if (StringUtils.isEmpty(name)) {
            toast("请填写商品名称");
            return false;
        }
        if (StringUtils.isEmpty(type)) {
            toast("请选择所属品类");
            return false;
        }
//        if (StringUtils.isEmpty(brand)) {
//            toast("请选择所属品牌");
//            return false;
//        }
        if (!StringUtils.isEmpty(marketPrice) && !StringUtils.isEmpty(favorablePrice)) {
            double market = Double.parseDouble(marketPrice);
            double favorable = Double.parseDouble(favorablePrice);
            if (market>favorable) {
                toast("现价不能大于原价");
                return false;
            }
        }
//        if (StringUtils.isEmpty(favorablePrice)) {
//            toast("请填写优惠价格");
//            return false;
//        }
//        if (StringUtils.isEmpty(stock)) {
//            toast("请填写库存数量");
//            return false;
//        }
//        if (StringUtils.isEmpty(property)) {
//            toast("请填写商品属性");
//            return false;
//        }
//        if (StringUtils.isEmpty(introduce)) {
//            toast("请填写商品详情");
//            return false;
//        }
//        if (StringUtils.isEmpty(notice)) {
//            toast("请填写售后须知");
//            return false;
//        }

        if (mPhotoList.size() <= 0) {
            toast("请选择商品图片");
            return false;
        }


        mProduct.setPna(name);
        mProduct.setPno(no);
        mProduct.setPpr(marketPrice);
        mProduct.setMon(type);
        mProduct.setBn(brand);
        mProduct.setPpr(marketPrice);
        mProduct.setPppr(favorablePrice);
        if (stock.equals("")) {
            mProduct.setPnu(0);
        } else {
            mProduct.setPnu(Integer.parseInt(stock));
        }
        mProduct.setPattr(property);
        mProduct.setPd(introduce);
        mProduct.setPasa("");
        mProduct.setPt(Constant.PRODUCT);


        return true;
    }

    private void addProduct() {
        showProgressDialogPic();
        Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/product/";

        uploadAli = new UploadAli(this, mPhotoList, hud);
        uploadAli.execute(1000);
        uploadAli.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(String s) {
                if (s.length() > 0) {
                    photoUrls = s.substring(0, s.length() - 1);
                    String[] split = photoUrls.split(",");
                    mProduct.setPpi(split[0]);
                    sendRequese();
                }

            }

            @Override
            public void onDataReceivedFailed() {
                closeProgressDialog();
                toast("上传图片失败");
            }
        });
    }

    private void sendRequese() {
        String urlString = "product/addProduct.json";
        urlString = String.format(urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();

        map.put("productName", mProduct.getPna());//商品名称
        map.put("productNo", mProduct.getPno());//商品编号
        map.put("prefPrice", mProduct.getPppr());//优惠价格
        map.put("productPrice", mProduct.getPpr());//市场价值
        map.put("productNumber", mProduct.getPnu() + "");//商品库存
        map.put("attributes", mProduct.getPattr());//宝贝属性
        map.put("productDesc", mProduct.getPd());//商品详情
        map.put("productAlbum", photoUrls);//商品图片
        map.put("afterSales", mProduct.getPasa());//售后须知
        map.put("brandId", brandId);//商品品牌
        Log.i(TAG, "brandId====" + brandId);
        map.put("operateId", operateId);//商品品类
        map.put("productType", Constant.PRODUCT + "");
        map.put("targetId", pref.getString("sid", ""));
        map.put("targetType", "2");
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
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
                                mProduct.setPst(Constant.PRODUCT_STATUS_AUDIT);
                                toast("添加商品成功");
                                Intent intent = new Intent();
                                intent.putExtra("product", mProduct);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                toast("提交失败!");
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


    private void requestKinds() {
        showProgressDialog();
        String urlString = "business/shop_cate.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("level", "3");
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
                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
                            if (runKindList.getE().getCode() == 0) {
                                if (runKindList.getData().size() > 0) {
                                    mRunKindList = runKindList.getData();
                                    mChooseCategoryWheel.setProvince(mRunKindList);
                                } else {
                                    mRunKindList.clear();
                                    mChooseCategoryWheel.setProvince(mRunKindList);
                                }
                            } else {
                                toast(runKindList.getE().getDesc());
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

    private void requestBrandsList(String moid) {
        showProgressDialog();
        String urlString = "business/query_brands.json";
        urlString = String.format(urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("moids", moid);
//        map.put("page", "");
        map.put("page_length", "30");

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

                        String json = responseInfo.result;
                        try {
                            BrandList brandList = JSON.parseObject(json, BrandList.class);
                            if (brandList.getE().getCode() == 0) {
                                mBrandList = brandList.getData();
                                mChooseBrandWheel.setProvince(mBrandList);
                            } else {
                                mBrandList.clear();
                            }
                        } catch (Exception e) {
                            mBrandList.clear();
                            e.printStackTrace();
                        } finally {
                            closeProgressDialog();
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

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return mTakePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        for (TImage image : images) {
            ImageItem item = new ImageItem();
            item.sourcePath = image.getCompressPath();
            mPhotoList.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        toast(msg);
    }

    @Override
    public void takeCancel() {

    }
}
