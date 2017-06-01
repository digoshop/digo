package com.bjut.servicedog.servicedog.ui.store;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.po.Menu;
import com.bjut.servicedog.servicedog.po.Update;
import com.bjut.servicedog.servicedog.po.UpdateInfo;
import com.bjut.servicedog.servicedog.ui.setting.AccountSecurityActivity;
import com.bjut.servicedog.servicedog.ui.ShopQRImageActivity;
import com.bjut.servicedog.servicedog.ui.setting.StoreSettingActivity;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.user.UserManageActivity;
import com.bjut.servicedog.servicedog.ui.vip.VipClassConfigActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.bjut.servicedog.servicedog.view.pop.PhotoWindows;
import com.chiclam.download.ApkUpdateUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopSettingActivity extends BaseActivity implements PhotoWindows.OnPhotoChioseListener, TakePhoto.TakeResultListener, InvokeListener {
    private LinearLayout account, service, vipmanage;
    private LinearLayout usermanage, banben, back, storesetting;
    //    private PopupMenu popupMenu;
    private ImageView shopma;
    private AlertDialog.Builder builder;
    private Button tuichu, zhuxiao;
    private TextView shop_name, shop_phonenumber;
    public static List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private ImageView circleImage;
    private static final int FLAG_MODIFY_FINISH = 7;
    private UploadAli uploadAli;
    private String filepath, fileurl;
    private TextView tv_version_text;
    private TextView tv_title;
    private My2BDialog mMyUpdateDialog;
    private PhotoWindows mPhotoWindows;

    private static final int TAKE_PICTURE = 0x000000;
    private static final int CHOOSE_PICTURE = 10000000;

    private String path = "";
    private String manager = "";

    private CustomHelper mCustomHelper;
    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_setting);
        init();
        requestpermission();
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
    }

    private void init() {
        mCustomHelper = new CustomHelper();
        mCustomHelper.setEtCropWidth(1);
        mCustomHelper.setEtCropHeight(1);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_sz));
        circleImage = (ImageView) findViewById(R.id.showpImage);
        circleImage.setOnClickListener(this);
        shopma = (ImageView) findViewById(R.id.shopma);
        shopma.setOnClickListener(this);
        shop_phonenumber = (TextView) findViewById(R.id.shop_phonenumber);
        shop_name = (TextView) findViewById(R.id.shop_name);
        storesetting = (LinearLayout) findViewById(R.id.rel_store_setting);
        account = (LinearLayout) findViewById(R.id.rel_zhanghao);
        service = (LinearLayout) findViewById(R.id.rel_service);
        vipmanage = (LinearLayout) findViewById(R.id.rel_vip);
        usermanage = (LinearLayout) findViewById(R.id.rel_user);
        banben = (LinearLayout) findViewById(R.id.rel_banben);
        back = (LinearLayout) findViewById(R.id.rel_tuichu);
        tv_version_text = (TextView) findViewById(R.id.tv_version_text);
        mPhotoWindows = new PhotoWindows(mContext);
        mPhotoWindows.setOnPhotoChioseListener(this);
        try {
            tv_version_text.setText("当前版本：" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        storesetting.setOnClickListener(this);
        account.setOnClickListener(this);
        service.setOnClickListener(this);
        vipmanage.setOnClickListener(this);
        usermanage.setOnClickListener(this);
        banben.setOnClickListener(this);
        back.setOnClickListener(this);

        shop_name.setText(pref.getString("shopname", ""));
        String phoneNumber = pref.getString("usermobile", "");
        if (phoneNumber.length()==11) {
            shop_phonenumber.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11));
        }else{
            shop_phonenumber.setText(phoneNumber);
        }
        String shopimage = pref.getString("shopimage", "");
        if (shopimage != null && shopimage.length() > 0) {
            MyImageLoder.getInstance().loadCircle(this, shopimage, circleImage);
        } else {
            circleImage.setImageResource(R.drawable.default_108_108);
        }
        builder = new AlertDialog.Builder(this);

        manager = pref.getString("manager", "1");  //1  是店铺申请者的账号    0是添加的
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopma:
                myIntentR(ShopQRImageActivity.class);
                break;
            case R.id.rel_store_setting:
                myIntentR(StoreSettingActivity.class);
                break;
            case R.id.rel_zhanghao:
                myIntentR(AccountSecurityActivity.class);
                break;
            case R.id.rel_service:
//                myIntentR(ServiceProductionActivity.class);
                break;
            case R.id.rel_vip:
                myIntentR(VipClassConfigActivity.class);
                break;
            case R.id.rel_user:
                myIntentR(UserManageActivity.class);
                break;
            case R.id.rel_tuichu:
                if (mMyDialog == null) {
                    mMyDialog = new My2BDialog(mContext);
                }

                mMyDialog.setTitle("温馨提示");
                mMyDialog.setMessage("确定退出登录？");
                mMyDialog.setYesOnclickListener("退出", new My2BDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        mMyDialog.dismiss();
                        ActivityCollector.finishAll();
                        System.exit(0);
                    }
                });
                mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        mMyDialog.dismiss();
                    }
                });
                mMyDialog.show();
                break;
            case R.id.rel_banben:
                requestupdate();
                break;
            case R.id.showpImage:
                mPhotoWindows.showAtLocation(circleImage, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (mPhotoList.size() < 2 && resultCode == -1 && !TextUtils.isEmpty(path)) {
//                    ImageItem item = new ImageItem();
//                    item.sourcePath = path;
//                    List<ImageItem> incomingDataList = new ArrayList<ImageItem>();
//                    incomingDataList.add(item);
//                    Intent intent = new Intent(this, ClipPictureActivity.class);
//                    intent.putExtra("path", (Serializable) incomingDataList);
//                    intent.putExtra("data", 8);
//                    intent.putExtra("squre", "1");
//                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
//                }
//                break;
//        }
//        if (requestCode == 123 && resultCode == 456) {
//            List<ImageItem> incomingDataList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//            if (incomingDataList != null) {
//                Intent intent = new Intent(this, ClipPictureActivity.class);
//                intent.putExtra("path", (Serializable) incomingDataList);
//                intent.putExtra("data", 8);
//                intent.putExtra("squre", "1");
//                startActivityForResult(intent, FLAG_MODIFY_FINISH);
//            }
//        }
//        if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
//            Log.i("FLAG_MODIFY_FINISH", "1111111");
//            Log.i("FLAG_MODIFY_FINISH", mPhotoList.get(0).getThumbnailPath());
//            Log.i("FLAG_MODIFY_FINISH", mPhotoList.get(0).getSourcePath());
//            showProgressDialogPic();
//            mPhotoList.get(0).setImageUrl("");
//            Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/avatar/";
//            uploadAli = new UploadAli(this, mPhotoList, hud);
//            uploadAli.execute(1000);
//            uploadAli.setOnAsyncResponse(new AsyncResponse() {
//                @Override
//                public void onDataReceivedSuccess(String s) {
//                    if (!"".equals(s)) {
//                        fileurl = s.substring(0, s.length() - 1);
//                        Log.i("urllist", fileurl);
//                        sendImage();
//                    } else {
//
//                    }
//                }
//
//                @Override
//                public void onDataReceivedFailed() {
//                    toast("上传图片失败");
//                }
//            });
//        }
        mTakePhoto.onActivityResult(requestCode, resultCode, data);

    }

    private void setView(List<Menu> menuList) {
        for (int i = 0; i < menuList.size(); i++) {
            if (storesetting.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                storesetting.setVisibility(View.VISIBLE);
            } else if (account.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                account.setVisibility(View.VISIBLE);
            } else if (service.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                service.setVisibility(View.GONE);
            }
//            else if (dingzhi.getTag().toString().equals(menuList.get(i).getMenu_tag())){
//                dingzhi.setVisibility(View.VISIBLE);
//            }
            else if (vipmanage.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                if (manager.equals("1")) {
                    vipmanage.setVisibility(View.VISIBLE);
                } else {
                    vipmanage.setVisibility(View.GONE);
                }
            } else if (usermanage.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                usermanage.setVisibility(View.VISIBLE);
            } else if (banben.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                banben.setVisibility(View.VISIBLE);
            } else if (back.getTag().toString().equals(menuList.get(i).getMenu_tag())) {
                back.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setViewone(List<Menu> menuListone) {

        for (int i = 0; i < menuListone.size(); i++) {
            if (storesetting.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                storesetting.setVisibility(View.VISIBLE);
            } else if (account.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                account.setVisibility(View.VISIBLE);
            } else if (service.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                service.setVisibility(View.GONE);
            }
//            else if (dingzhi.getTag().toString().equals(menuListone.get(i).getMenu_tag())){
//                dingzhi.setVisibility(View.VISIBLE);
//            }
            else if (vipmanage.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                if (manager.equals("1")) {
                    vipmanage.setVisibility(View.VISIBLE);
                } else {
                    vipmanage.setVisibility(View.GONE);
                }
            } else if (usermanage.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                usermanage.setVisibility(View.VISIBLE);
            } else if (banben.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                banben.setVisibility(View.VISIBLE);
            } else if (back.getTag().toString().equals(menuListone.get(i).getMenu_tag())) {
                back.setVisibility(View.VISIBLE);
            }
        }
    }

    /***
     * 用来获取权限菜单的接口
     */
    public void requestpermission() {
        showProgressDialog();
        String urlString = "passport/s_auth.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<String, String>();
        map.put("sid", pref.getString("sid", ""));
        //map.put("uid",pref.getString("uid",""));
        RequestParams params = sortMapByKey(map);
        params.addBodyParameter("uid", pref.getString("uid", ""));
//        String urlString = "pc/getNurse?phone=%s&personPwd=%s&personId=%s";
//        urlString = String.format(urlString, phone, personPwd, personId);
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
                        List<Menu> menuList = new ArrayList<Menu>();
                        List<Menu> menuListone = new ArrayList<Menu>();
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                menuList = JSON.parseArray(j.getJSONObject("data").getJSONObject("auth").getJSONArray("public").toString(), Menu.class);
                                menuListone = JSON.parseArray(j.getJSONObject("data").getJSONObject("auth").getJSONArray("setting").toString(), Menu.class);
                                setView(menuList);
                                setViewone(menuListone);
                            } else {
                                toast("获取菜单失败");
                            }
                        } catch (JSONException e) {
                            setView(menuList);
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    public void requestupdate() {
        showProgressDialog();
        String urlString = "setting/version.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appkey", "digo");
        map.put("version_code", pref.getString("version_name", ""));
        map.put("md5", pref.getString("version_md5", ""));
        map.put("v", "1");
        RequestParams params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                Log.i("result", responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        Update update = JSON.parseObject(json, Update.class);
                        if (update.getE().getCode() == 0 && update.getData().isUpdate()) {
                            askUpdate(update.getData());
                        } else {
                            toast("暂无新版本");
                        }

                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                toast(Constant.CHECK_NETWORK);
            }
        });
    }

    private void askUpdate(final UpdateInfo updateInfo) {

        mMyUpdateDialog = new My2BDialog(mContext);
        mMyUpdateDialog.setTitle("版本更新");
        mMyUpdateDialog.setMessage("新版本 " + updateInfo.getNew_version() + " ,大小" + updateInfo.getTarget_size() + " ," + updateInfo.getU_log());
        mMyUpdateDialog.setYesOnclickListener("下载安装", new My2BDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                downloadNew(updateInfo.getDown_url());
                editor.putString("update_if", "false");
                editor.commit();
                mMyUpdateDialog.dismiss();
            }
        });
        mMyUpdateDialog.setNoOnclickListener("暂不更新", new My2BDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editor.putString("update_if", "false");
                editor.commit();
                mMyUpdateDialog.dismiss();
            }
        });
        mMyUpdateDialog.show();
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

    private void sendImage() {
        String urlString = "business/avatar.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("sid", pref.getString("sid", ""));
        map.put("avatar", fileurl);//图片地址
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                editor.putString("shopimage", fileurl);
                                editor.putString("ifshopimgchange", "1");
                                editor.commit();
                                MyImageLoder.getInstance().loadCircle(mContext,pref.getString("shopimage", ""), circleImage);
                            } else {
                                toast("头像上传失败,请重试");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
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

    private String getVersionName() throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public void onTakePhoto() {
//        takePhoto();
        mCustomHelper.setLimit(1);
        mCustomHelper.onClick(Constant.PHOTO_TAKE, mTakePhoto);
    }

    @Override
    public void onChoisePhoto() {

        mCustomHelper.setLimit(1);
        mCustomHelper.onClick(Constant.PHOTO_CHOISE, mTakePhoto);

//        Intent intent = new Intent(ShopSettingActivity.this, ImageBucketChooseActivity.class);
//        intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, 1);
//        startActivityForResult(intent, 123);
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
        mPhotoList.clear();
        ArrayList<TImage> images = result.getImages();
        for (TImage image : images) {
            ImageItem item = new ImageItem();
            item.sourcePath = image.getCompressPath();
            mPhotoList.add(item);
        }

        showProgressDialogPic();
        mPhotoList.get(0).setImageUrl("");
        Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/avatar/";
        uploadAli = new UploadAli(this, mPhotoList, hud);
        uploadAli.execute(1000);
        uploadAli.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(String s) {
                if (!"".equals(s)) {
                    fileurl = s.substring(0, s.length() - 1);
                    Log.i("urllist", fileurl);
                    sendImage();
                } else {

                }
            }

            @Override
            public void onDataReceivedFailed() {
                toast("上传图片失败");
            }
        });
    }

    @Override
    public void takeFail(TResult result, String msg) {
        toast(msg);
    }

    @Override
    public void takeCancel() {

    }
}