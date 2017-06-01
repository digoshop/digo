package com.bjut.servicedog.servicedog.ui.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.GridViewDeleteBrandItemAdapter;
import com.bjut.servicedog.servicedog.adapter.GridViewDeleteRunItemAdapter;
import com.bjut.servicedog.servicedog.adapter.StoreImageAdapter;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.po.Brand;
import com.bjut.servicedog.servicedog.po.RunKind;
import com.bjut.servicedog.servicedog.po.StoreInfo;
import com.bjut.servicedog.servicedog.ui.ShopQRImageActivity;
import com.bjut.servicedog.servicedog.ui.StoreAddRunKinds;
import com.bjut.servicedog.servicedog.ui.StoreBrandActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.CustomConstants;
import com.bjut.servicedog.servicedog.utils.IntentConstants;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.GridViewInScrollView;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.bjut.servicedog.servicedog.view.MyGridView;
import com.bjut.servicedog.servicedog.view.pop.PhotoWindows;
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjut.servicedog.servicedog.R.id.tv_start;

/**
 * Created by beibeizhu on 17/3/24.
 */

public class StoreManagerActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private ImageView img_right;
    private LinearLayout kind_addLl, brand_addLl, llGridview2, ll_time, ll_read_time;
    private TextView submit, storename, storeaddress, tvStart, tvEnd, tvReadTime;
    private UploadAli uploadAli;
    private String fileurl;
    private EditText phonenumber, produce, integral;
    private List<RunKind> saveRunkindlist = new ArrayList<>();//用于接受activity传递数据.
    private List<Brand> saveBrand = new ArrayList<>();//用于接受activity传递数据.
    private MyGridView mGridView;
    private StoreImageAdapter mAdapter;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private JSONObject jsonObject;
    private JSONObject brandJson;
    private GridViewInScrollView runGridView, brandGridView;
    private GridViewDeleteRunItemAdapter adapter1;
    private GridViewDeleteBrandItemAdapter adapter2;
    private String shoptype;
    private ImageView img_start_time;
    private ImageView img_end_time;
    private TextView tv_title;
    private boolean shopMerchant;
    private LinearLayout llHide;
    private EditText edt_address;

    private String startTime = "";
    private String endTime = "";
    private PhotoWindows photoWindows;
    private Calendar now;

    private CustomHelper mCustomHelper;
    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    private final int INTENT_KIND = 0;
    private final int INTENT_BRAND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mTakePhoto.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putSerializable("mPhotoList", (Serializable) mPhotoList);
        outState.putSerializable("introduce",produce.getText().toString());
        outState.putSerializable("integral",integral.getText().toString());
        outState.putSerializable("phone",phonenumber.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPhotoList = (List<ImageItem>) savedInstanceState.getSerializable("mPhotoList");

        String phone = savedInstanceState.getString("phone");
        String integralStr = savedInstanceState.getString("integral");
        String introduce = savedInstanceState.getString("introduce");

        phonenumber.setText(phone);
        integral.setText(integralStr);
        produce.setText(introduce);

        mAdapter = new StoreImageAdapter(mContext, mPhotoList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case INTENT_KIND:
                    saveRunkindlist.clear();
                    saveRunkindlist.addAll((List<RunKind>)data.getSerializableExtra("saveRunkindlisttostore"));
                    adapter1.notifyDataSetChanged();
                    break;
                case INTENT_BRAND:
                    saveBrand.clear();
                    saveBrand.addAll((List<Brand>) data.getSerializableExtra("saveDatatostore"));
                    adapter2.notifyDataSetChanged();
                    break;
                case IntentConstants.CHECK_PHOTO:
                    mPhotoList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                    mAdapter.setDate(mPhotoList);
                    break;
            }
        }
    }

    public void init() {
        mCustomHelper = new CustomHelper();
        mCustomHelper.setEtCropWidth(3);
        mCustomHelper.setEtCropHeight(2);

        now = Calendar.getInstance();
        img_start_time = (ImageView) findViewById(R.id.img_start_time);
        img_end_time = (ImageView) findViewById(R.id.img_end_time);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_read_time = (LinearLayout) findViewById(R.id.ll_read_time);
        llHide = (LinearLayout) findViewById(R.id.ll_hide);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_mdgl));

        runGridView = (GridViewInScrollView) findViewById(R.id.gridview1);
        adapter1 = new GridViewDeleteRunItemAdapter(this, saveRunkindlist);
        runGridView.setAdapter(adapter1);
        brandGridView = (GridViewInScrollView) findViewById(R.id.gridview2);
        adapter2 = new GridViewDeleteBrandItemAdapter(this, saveBrand);
        brandGridView.setAdapter(adapter2);

        img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.VISIBLE);
        img_right.setImageResource(R.drawable.spsz_erweima);
        img_right.setOnClickListener(this);
        kind_addLl = (LinearLayout) findViewById(R.id.kind_add);
        kind_addLl.setOnClickListener(this);
        brand_addLl = (LinearLayout) findViewById(R.id.brand_add);
        brand_addLl.setOnClickListener(this);
        submit = (TextView) findViewById(R.id.tv_submit);
        tvReadTime = (TextView) findViewById(R.id.tv_time);
        submit.setOnClickListener(this);
        storename = (TextView) findViewById(R.id.ed_storename);
        storeaddress = (TextView) findViewById(R.id.ed_storeaddress);
        llGridview2 = (LinearLayout) findViewById(R.id.ll_gridview2);
        tvStart = (TextView) findViewById(tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        edt_address = (EditText) findViewById(R.id.edt_address);

        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        img_start_time.setOnClickListener(this);
        img_end_time.setOnClickListener(this);
        shoptype = pref.getString("shoptype", "");
        if (shoptype.equals("2")) {
            kind_addLl.setVisibility(View.GONE);
            llHide.setVisibility(View.GONE);
            brandGridView.setVisibility(View.GONE);
            llGridview2.setVisibility(View.GONE);
        }
        shopMerchant = pref.getBoolean("shop_merchant", false);
        if (shopMerchant) {
            ll_time.setVisibility(View.GONE);
            ll_read_time.setVisibility(View.VISIBLE);
        } else {
            ll_read_time.setVisibility(View.GONE);
            ll_time.setVisibility(View.VISIBLE);
        }
        phonenumber = (EditText) findViewById(R.id.ed_phonenumber);
        integral = (EditText) findViewById(R.id.et_integral);
        produce = (EditText) findViewById(R.id.ed_store_produce);
        mGridView = (MyGridView) findViewById(R.id.gridview);
        //mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new StoreImageAdapter(this, mPhotoList);
        mGridView.setAdapter(mAdapter);

        photoWindows = new PhotoWindows(mContext);
        photoWindows.setOnPhotoChioseListener(new PhotoWindows.OnPhotoChioseListener() {
            @Override
            public void onTakePhoto() {
                int size = mPhotoList.size();
                if (size < CustomConstants.STORE_MAX_IMAGE_SIZE) {
                    int limit = CustomConstants.STORE_MAX_IMAGE_SIZE - size;
                    mCustomHelper.setLimit(limit);
                    mCustomHelper.onClick(Constant.PHOTO_TAKE, mTakePhoto);
                } else {

                }
            }

            @Override
            public void onChoisePhoto() {
                int size = mPhotoList.size();
                if (size < CustomConstants.STORE_MAX_IMAGE_SIZE) {
                    int limit = CustomConstants.STORE_MAX_IMAGE_SIZE - size;
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
                    photoWindows.showAtLocation(mGridView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(mContext,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) mPhotoList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivityForResult(intent,IntentConstants.CHECK_PHOTO);
                }

            }

        });
        runGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (shoptype.equals("2")) {
                    return;
                }
                if (mMyDialog == null) {
                    mMyDialog = new My2BDialog(mContext);
                }
                mMyDialog.setTitle("警告");
                mMyDialog.setMessage("您确认要删除所选品类吗？品类删除后，所选品牌将全部清空！");
                mMyDialog.setYesOnclickListener("确认", new My2BDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        saveRunkindlist.remove(i);
                        adapter1.notifyDataSetChanged();
                        saveBrand.clear();
                        adapter2.notifyDataSetChanged();
                        mMyDialog.dismiss();
                    }
                });
                mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        mMyDialog.dismiss();
                    }
                });
                mMyDialog.show();
            }
        });
        brandGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                saveBrand.remove(i);
                adapter2.notifyDataSetChanged();
            }
        });

        getStore();
    }

    private int getDataSize() {
        return mPhotoList == null ? 0 : mPhotoList.size();
    }

    public String getString(String s) {
        String path = null;
        if (s == null) return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    private boolean judgenull() {
        String start = tvStart.getText().toString();
        if (!shopMerchant) {
            if (startTime.equals("") && startTime.equals("")) {
                toast("请输入营业时间");
                return false;
            }
        }
        if (phonenumber.getText().toString().equals("")) {
            toast("请输入联系电话");
            return false;
        }
//
//        if (!RegularUtils.isMobileNO(phonenumber.getText().toString())) {
//            toast("请输入正确的手机号");
//            return false;
//        }

        if ("".equals(integral.getText().toString())) {
            toast("请输入积分设置");
            return false;
        }
        if (produce.getText().toString().equals("")) {
            toast("请输入商家介绍");
            return false;
        }
        if (saveRunkindlist.size() == 0) {
            toast("请选择经营品类");
            return false;
        }
//        else if (saveBrand.size()==0){
//            toast("请选择品牌");
//            return false;
//        }
        else if (mPhotoList.size() == 0) {
            toast("请添加相册图片");
            return false;
        }
        return true;
    }

    private String shopOperateIds() {
        String a = "";
        for (int i = 0; i < saveRunkindlist.size(); i++) {
            a += saveRunkindlist.get(i).getMoid() + ",";
        }
        return a.substring(0, a.length() - 1);
    }

    private String brandIds() {

        String a = "";
        if (saveBrand.size() > 0) {
            for (int i = 0; i < saveBrand.size(); i++) {
                a += saveBrand.get(i).getBrand_id() + ",";
            }
            return a.substring(0, a.length() - 1);
        } else {
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_right:
                myIntentR(ShopQRImageActivity.class);
                break;
            case R.id.kind_add:
                Intent intent = new Intent(this, StoreAddRunKinds.class);
                intent.putExtra("saveRunkindlisttoadd", (Serializable) saveRunkindlist);
                startActivityForResult(intent,INTENT_KIND);
                break;
            case R.id.brand_add:
                if (saveRunkindlist.size() == 0) {
                    toast("请先选择品类");
                    return;
                }
                Intent intent2 = new Intent(this, StoreBrandActivity.class);
                intent2.putExtra("saveBrandtoadd", (Serializable) saveRunkindlist);
                intent2.putExtra("moids", shopOperateIds());
                intent2.putExtra("brandIds", (Serializable) saveBrand);
                startActivityForResult(intent2,INTENT_BRAND);
                break;
            case R.id.tv_submit:
                if (judgenull()) {
                    if (!Utils.isNotFastClick()) {
                        return;
                    }
                    int num = Integer.parseInt(integral.getText().toString());
                    if (num < 1 || num > 1000) {
                        toast("请输入1～1000之内金额");
                        return;
                    }
                    showProgressDialog();
                    Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/store/";
                    uploadAli = new UploadAli(this, mPhotoList, hud);

                    uploadAli.execute(1000);
                    uploadAli.setOnAsyncResponse(new AsyncResponse() {
                        @Override
                        public void onDataReceivedSuccess(String s) {
                            if (!"".equals(s) && s.length() > 0) {
                                fileurl = s.substring(0, s.length() - 1);
                                Log.i("urllist", fileurl);
                                sendSubmit();
                            }else{
                                toast("上传图片失败");
                            }
                        }

                        @Override
                        public void onDataReceivedFailed() {
                            toast("上传图片失败");
                            closeProgressDialog();
                        }
                    });
                }
                break;
            case R.id.tv_start:
            case R.id.img_start_time:
                TimePickerDialog startTpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String hourStr = "";
                        String minuteStr = "";
                        if (hourOfDay < 10) {
                            hourStr = "0" + hourOfDay;
                        } else {
                            hourStr = hourOfDay + "";
                        }
                        if (minute < 10) {
                            minuteStr = "0" + minute;
                        } else {
                            minuteStr = minute + "";
                        }
                        startTime = hourStr + ":" + minuteStr;
                        tvStart.setText(startTime);
                    }
                }, now.get(Calendar.DAY_OF_MONTH), 9, 0, true);
                startTpd.setAccentColor(getResources().getColor(R.color.blue));
                startTpd.setCancelable(false);
                startTpd.setStartTime(9, 0);
                startTpd.setTitle("开始时间");
                startTpd.show(getFragmentManager(), "startTime");
                break;
            case R.id.tv_end:
            case R.id.img_end_time:
                TimePickerDialog endTpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String hourStr = "";
                        String minuteStr = "";
                        if (hourOfDay < 10) {
                            hourStr = "0" + hourOfDay;
                        } else {
                            hourStr = hourOfDay + "";
                        }
                        if (minute < 10) {
                            minuteStr = "0" + minute;
                        } else {
                            minuteStr = minute + "";
                        }
                        endTime = hourStr + ":" + minuteStr;
                        tvEnd.setText(endTime);
                    }
                }, now.get(Calendar.DAY_OF_MONTH), 21, 0, true);
                endTpd.setAccentColor(getResources().getColor(R.color.blue));
                endTpd.setCancelable(false);
                endTpd.setStartTime(21, 0);
                endTpd.setTitle("结束时间");
                endTpd.show(getFragmentManager(), "endTime");
                break;
        }
    }

    private void getStore() {
        showProgressDialog();
        String urlString = "business/shop_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        RequestParams params = sortMapByKey(map);

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
                            Log.i("zzzzr111", json);
                            jsonObject = new JSONObject(json);
                            String data = jsonObject.getString("data");
                            brandJson = new JSONObject(data);
                            StoreInfo storeInfo = JSON.parseObject(json, StoreInfo.class);
                            editor.putString("nid", storeInfo.getData().getNid());
                            editor.putString("nation", storeInfo.getData().getNation());
                            Log.i("zzrqq", "aaa" + storeInfo.getData().getNation());
                            setView(storeInfo);
                            editor.commit();

                        } catch (Exception e) {
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

    private void setView(final StoreInfo s) {
        storename.setText(s.getData().getShop_name());
        storeaddress.setText(s.getData().getAddress());
        produce.setText(s.getData().getDescription());
        edt_address.setText(s.getData().getShop_detail_address());
        String open_time = s.getData().getOpen_time();
        if (shopMerchant) {
            tvReadTime.setText(open_time);
        } else {
            if (open_time != null) {
                if (open_time.contains("-")) {
                    String[] timeStr = s.getData().getOpen_time().split("-");
                    tvStart.setText(timeStr[0]);
                    tvEnd.setText(timeStr[1]);
                    startTime = open_time;
                } else {
                    tvStart.setText(open_time);
                    tvEnd.setText("");
                    endTime = "";
                }
            } else {
                startTime = "";
                endTime = "";
                tvStart.setText("");
                tvEnd.setText("");
            }
        }


        integral.setText(s.getData().getShop_spr());
        String tel = "";
        if (s.getData().getContact_phone().size() != 0) {
            for (int i = 0; i < s.getData().getContact_phone().size(); i++) {
                tel += s.getData().getContact_phone().get(i) + " ";
            }
            phonenumber.setText(tel);
        }
        if (s.getData().getOperates().size() != 0) {
            saveRunkindlist.addAll(s.getData().getOperates());
            adapter1.notifyDataSetChanged();
        }

        if (!brandJson.isNull("brands")) {
            if (s.getData().getBrands().size() != 0 && s.getData().getBrands() != null) {
                saveBrand.addAll(s.getData().getBrands());
                adapter2.notifyDataSetChanged();
            }
        } else {
        }

        mPhotoList.clear();
        List<String> shop_album1 = s.getData().getShop_album();
        if (shop_album1.size() > 0) {
            for (String s1 : shop_album1) {
                ImageItem shop_album = new ImageItem();
                shop_album.setImageUrl(s1);
                mPhotoList.add(shop_album);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void sendSubmit() {

        String urlString = "business/add_shop_info.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("shopAlbums", fileurl);
        if (shopMerchant) {
            map.put("shopDoBusinessTime", "");
        } else {
            map.put("shopDoBusinessTime", tvStart.getText().toString() + "-" + tvEnd.getText().toString());
        }
        map.put("shopCover", "");
        map.put("spr", integral.getText().toString());
        map.put("shopOperateMobiles", phonenumber.getText().toString());
        map.put("shopDescription", produce.getText().toString());
//        map.put("shopOperateIds", pref.getString("saveSecondid", "") + shopOperateIds());
        map.put("shopOperateIds", shopOperateIds());
//        map.put("brandIds", pref.getString("saveBrandSecondid", "") + brandIds());
        map.put("brandIds", brandIds());
        map.put("shopDetailedAddress", edt_address.getText().toString());

        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                Log.i("responseInfo", "aa" + responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String json = responseInfo.result;
                        Log.i("zzr", json);
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("提交成功");
                                finish();
                                editor.putString("shopManage", "1");
                                editor.commit();
                            } else {
                                toast(j.getJSONObject("e").getString("desc"));
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
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
