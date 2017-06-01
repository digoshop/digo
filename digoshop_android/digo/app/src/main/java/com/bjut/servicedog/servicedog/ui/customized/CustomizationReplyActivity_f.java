package com.bjut.servicedog.servicedog.ui.customized;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ImagePublishAdapter;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.CustomConstants;
import com.bjut.servicedog.servicedog.utils.IntentConstants;
import com.bjut.servicedog.servicedog.utils.KeyboardUtils;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomizationReplyActivity_f extends BaseActivity implements View.OnClickListener,TakePhoto.TakeResultListener, InvokeListener {
    private EditText reply;
    private TextView submit;
    private TextView tv_title;
    private ImageView replyone;
    private String fileurl, filepath;
    private UploadAli uploadAli;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private InputMethodManager im;
    private String cid = "";
    private PhotoWindows photoWindows;

    private CustomHelper mCustomHelper;
    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_reply_activity_f);
        initData();
        init();
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
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void initData() {
        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent().getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        if (incomingDataList != null) {
            mPhotoList.addAll(incomingDataList);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case IntentConstants.CHECK_PHOTO:
                    mPhotoList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                    mAdapter.setDate(mPhotoList);
                    break;
            }
        }
    }


    private void init() {
        mCustomHelper = new CustomHelper();
        mCustomHelper.setEtCropWidth(3);
        mCustomHelper.setEtCropHeight(2);

        cid = getIntent().getStringExtra("cid");
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        reply = (EditText) findViewById(R.id.customization_reply);
        submit = (TextView) findViewById(R.id.customization_submit);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText(getString(R.string.title_dzhf));
        submit.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        //mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mPhotoList);
        mGridView.setAdapter(mAdapter);
        photoWindows = new PhotoWindows(mContext);
        photoWindows.setOnPhotoChioseListener(new PhotoWindows.OnPhotoChioseListener() {
            @Override
            public void onTakePhoto() {
                int size = mPhotoList.size();
                if (size < CustomConstants.MAX_IMAGE_SIZE) {
                    int limit =  CustomConstants.MAX_IMAGE_SIZE- size;
                    mCustomHelper.setLimit(limit);
                    mCustomHelper.onClick(Constant.PHOTO_TAKE, mTakePhoto);
                }else{

                }
            }

            @Override
            public void onChoisePhoto() {

                int size = mPhotoList.size();
                if (size < CustomConstants.MAX_IMAGE_SIZE) {
                    int limit =  CustomConstants.MAX_IMAGE_SIZE- size;
                    mCustomHelper.setLimit(limit);
                    mCustomHelper.onClick(Constant.PHOTO_CHOISE, mTakePhoto);
                }else{

                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    KeyboardUtils.hideSoftInput(CustomizationReplyActivity_f.this);
                    im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customization_submit:
                if (reply.getText().toString().length() == 0) {
                    toast("回复内容不能为空!");
                    return;
                } else if (mPhotoList.size() == 0) {
                    toast("请选择照片");
                    return;
                } else {
                    showProgressDialogPic();
                    Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/custom/";
                    uploadAli = new UploadAli(this, mPhotoList, hud);
                    uploadAli.execute(1000);
                    uploadAli.setOnAsyncResponse(new AsyncResponse() {
                        @Override
                        public void onDataReceivedSuccess(String s) {
                            fileurl = s.substring(0, s.length() - 1);
                            sendRequese();
                        }

                        @Override
                        public void onDataReceivedFailed() {
                            toast("上传图片失败");
                        }
                    });
                }
                break;
        }
    }

    private void sendRequese() {
        String urlString = "custservice/createCustomReply.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("cid", cid);
        map.put("sid", pref.getString("sid", ""));//店铺
        map.put("content", reply.getText().toString());//回复内容
        map.put("pictures", fileurl);//图片
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
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getJSONObject("e").getInt("code") == 0) {

                                toast("回复成功");
                                finish();

                            } else {
                                toast("发布失败,请检查各项是否为空!");
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
