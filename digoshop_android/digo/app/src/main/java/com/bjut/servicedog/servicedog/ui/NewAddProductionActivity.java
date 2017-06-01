//package com.bjut.servicedog.servicedog.ui;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.adapter.ImagePublishAdapter;
//import com.bjut.servicedog.servicedog.model.ImageItem;
//import com.bjut.servicedog.servicedog.po.RunKind;
//import com.bjut.servicedog.servicedog.po.RunKindList;
//import com.bjut.servicedog.servicedog.utils.AsyncResponse;
//import com.bjut.servicedog.servicedog.utils.Constant;
//import com.bjut.servicedog.servicedog.utils.CustomConstants;
//import com.bjut.servicedog.servicedog.utils.IntentConstants;
//import com.bjut.servicedog.servicedog.utils.UploadAli;
//import com.bjut.servicedog.servicedog.utils.Utils;
//import com.bjut.servicedog.servicedog.view.ClipPictureActivity;
//import com.bjut.servicedog.servicedog.view.ImageBucketOneChooseActivity;
//import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
//import com.bjut.servicedog.servicedog.view.listener.OnCategortChangeListener;
//import com.bjut.servicedog.servicedog.view.pop.PhotoWindows;
//import com.bjut.servicedog.servicedog.view.wheelview.ChooseCategoryWheel;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class NewAddProductionActivity extends BaseActivity implements OnCategortChangeListener {
//    private final int REQUEST_CODE_GALLERY = 1001;
//    private String fileurl;
//    private UploadAli uploadAli;
//    private GridView mGridView;
//    private ImagePublishAdapter mAdapter;
//    public static List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
//    private RelativeLayout relrequest, rl_category;
//    private TextView save, tv_categorys;
//    private EditText productName, value;
//    private ArrayAdapter<String> arr_adapter;
//    private List<RunKind> data = new ArrayList<>();
//    private List<String> productname = new ArrayList<>();
//    private RunKind runkind;
//    private long pid;
//    private static final int FLAG_MODIFY_FINISH = 7;
//    private ChooseCategoryWheel mChooseCategoryWheel = null;
//    private TextView tv_title;
//    private PhotoWindows photoWindows;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_add_production);
//        initData();
//        init();
//        requestKinds();
//        initWheelCategory();
//    }
//
//    private void initWheelCategory() {
//        mChooseCategoryWheel = new ChooseCategoryWheel(this);
//        mChooseCategoryWheel.setOnFoorChangeListener(this);
//
//    }
//
//    protected void onPause() {
//        super.onPause();
//        saveTempToPref();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPhotoList.clear();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        saveTempToPref();
//    }
//
//    private void saveTempToPref() {
//        SharedPreferences sp = getSharedPreferences(
//                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
//        String prefStr = JSON.toJSONString(mPhotoList);
//        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();
//
//    }
//
//    private void initData() {
//        //getTempFromPref();
//        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
//                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//        if (incomingDataList != null) {
//            mPhotoList.addAll(incomingDataList);
//        }
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (mPhotoList.size() < CustomConstants.MAX_IMAGE_SIZE && resultCode == -1 && !TextUtils.isEmpty(path)) {
//                    ImageItem item = new ImageItem();
//                    item.sourcePath = path;
//                    List<ImageItem> incomingDataList = new ArrayList<ImageItem>();
//                    incomingDataList.add(item);
//                    Intent intent = new Intent(this, ClipPictureActivity.class);
//                    intent.putExtra("path", (Serializable) incomingDataList);
//                    intent.putExtra("data", 6);
//                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
//                }
//                break;
//        }
//        if (requestCode == 123 && resultCode == 456) {
//            List<ImageItem> incomingDataList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//            if (incomingDataList != null) {
//                Intent intent = new Intent(this, ClipPictureActivity.class);
//                intent.putExtra("path", (Serializable) incomingDataList);
//                intent.putExtra("data", 6);
//                startActivityForResult(intent, FLAG_MODIFY_FINISH);
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
//    }
//
//    private void init() {
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.title_xzcp));
//        tv_categorys = (TextView) findViewById(R.id.tv_categorys);
//        productName = (EditText) findViewById(R.id.ed_name_production);
//        value = (EditText) findViewById(R.id.ed_value);
//        save = (TextView) findViewById(R.id.add_production_save);
//        rl_category = (RelativeLayout) findViewById(R.id.linear3);
//        rl_category.setOnClickListener(this);
//        save.setOnClickListener(this);
//        // spinner = (Spinner) findViewById(R.id.production_spinner);
//        mGridView = (GridView) findViewById(R.id.gridview);
//        mAdapter = new ImagePublishAdapter(this, mPhotoList);
//        mGridView.setAdapter(mAdapter);
//
//        photoWindows = new PhotoWindows(mContext);
//        photoWindows.setOnPhotoChioseListener(new PhotoWindows.OnPhotoChioseListener() {
//            @Override
//            public void onTakePhoto() {
//                takePhoto();
//            }
//
//            @Override
//            public void onChoisePhoto() {
//                Intent intent = new Intent(NewAddProductionActivity.this, ImageBucketOneChooseActivity.class);
//                intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize());
//                startActivityForResult(intent, 123);
//            }
//        });
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == getDataSize()) {
//
//                    photoWindows.showAtLocation(mGridView, Gravity.BOTTOM, 0, 0);
//                } else {
//                    Intent intent = new Intent(NewAddProductionActivity.this,
//                            ImageZoomActivity.class);
//                    intent.putExtra("data", 6);
//                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
//                    startActivity(intent);
//                }
//
//
//            }
//
//        });
//    }
//
//    private int getDataSize() {
//        return mPhotoList == null ? 0 : mPhotoList.size();
//    }
//
//    private int getAvailableSize() {
//        int availSize = CustomConstants.ONLY_ONE - mPhotoList.size();
//        if (availSize >= 0) {
//            return availSize;
//        }
//        return 0;
//    }
//
//    public String getString(String s) {
//        String path = null;
//        if (s == null) return "";
//        for (int i = s.length() - 1; i > 0; i++) {
//            s.charAt(i);
//        }
//        return path;
//    }
//
//    @Override
//    public void onFoorChange(String floorName, long pid) {
//        tv_categorys.setText(floorName);
//        this.pid = pid;
//
//    }
//
//    private static final int TAKE_PICTURE = 0x000000;
//    private String path = "";
//
//    public void takePhoto() {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File vFile = new File(Environment.getExternalStorageDirectory()
//                + "/DIGO/", String.valueOf(System.currentTimeMillis())
//                + ".jpg");
//        if (!vFile.exists()) {
//            File vDirPath = vFile.getParentFile();
//            vDirPath.mkdirs();
//        } else {
//            if (vFile.exists()) {
//                vFile.delete();
//            }
//        }
//        path = vFile.getPath();
//        Uri cameraUri = Uri.fromFile(vFile);
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//    }
//
//
//    private void notifyDataChanged() {
//        mAdapter.notifyDataSetChanged();
//    }
//
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_submit:
////                if (judgenull()){
//
//            case R.id.add_production_save:
//                if (judgenull()) {
//                    if (Utils.isNotFastClick()) {
//                        showProgressDialogPic();
//                        Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/product/";
//                        uploadAli = new UploadAli(this, mPhotoList, hud);
//                        uploadAli.execute(1000);
//                        uploadAli.setOnAsyncResponse(new AsyncResponse() {
//                            @Override
//                            public void onDataReceivedSuccess(String s) {
//                                fileurl = s.substring(0, s.length() - 1);
//                                Log.i("urllist", fileurl);
//                                addProduction();
//                            }
//
//                            @Override
//                            public void onDataReceivedFailed() {
//                                toast("上传图片失败");
//                            }
//                        });
//                    }
//
//                }
//                break;
//            case R.id.linear3:
//
//                mChooseCategoryWheel.show(v);
//                break;
//        }
//    }
//
//    private void addProduction() {
//        String urlString = "product/createProduct.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("productName", productName.getText().toString());//宝贝名称
//        map.put("productPrice", value.getText().toString());//产品价格
//
//        map.put("productNumber", "0");//默认传0
//        map.put("productType", "3");//产品类型
//        map.put("operateId", pid + "");//所属品类
//        map.put("targetType", "2");
//        map.put("targetId", pref.getString("sid", ""));
//        map.put("productAlbum", fileurl);//产品照片
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String json = responseInfo.result;
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {
//                                toast("提交成功");
//                                finish();
//                            } else {
//                                toast("提交失败!");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                closeProgressDialog();
//                toast(Constant.CHECK_NETWORK);
//            }
//        });
//    }
//
//    private void requestKinds() {
//        showProgressDialog();
//        String urlString = "business/shop_cate.json";
//        urlString = String.format(urlString);
//        Log.i("out", urlString);
//        RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("sid", pref.getString("sid", ""));
//        map.put("level", "3");//优惠券批次ID必选
//        params = sortMapByKey(map);
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
//                + urlString, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(final ResponseInfo<String> responseInfo) {
//                System.out.println(responseInfo.result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        String json = responseInfo.result;
//                        try {
//                            RunKindList runKindList = JSON.parseObject(json, RunKindList.class);
//                            if (runKindList.getE().getCode() == 0) {
//                                if (runKindList.getData().size() > 0) {
//                                    data = runKindList.getData();
//                                    for (int i = 0; i < runKindList.getData().size(); i++) {
//                                        productname.add(runKindList.getData().get(i).getName());
//                                    }
//                                    mChooseCategoryWheel.setProvince(data);
//                                } else {
//                                    toast("无更多信息");
//                                }
//                            } else {
//                                toast(runKindList.getE().getDesc());
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                closeProgressDialog();
//                toast(Constant.CHECK_NETWORK);
//            }
//        });
//    }
//
//    private boolean judgenull() {
//        if (productName.getText().toString().equals("")) {
//            toast("请输入产品名称");
//            return false;
//        }
//        if (value.getText().toString().equals("")) {
//            toast("请输入产品价格");
//            return false;
//        }
//        if (tv_categorys.getText().toString().equals("")) {
//            toast("请选择所属品类");
//            return false;
//        }
//        if (mPhotoList.size() == 0) {
//            toast("请选择产品图片");
//            return false;
//        }
//
//        return true;
//    }
//
//}
