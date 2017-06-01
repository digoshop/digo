package com.bjut.servicedog.servicedog.ui.activies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.CheckEventImageAdapter;
import com.bjut.servicedog.servicedog.event.OnRefreshEvent;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.DateUtil;
import com.bjut.servicedog.servicedog.utils.IntentConstants;
import com.bjut.servicedog.servicedog.utils.StringUtils;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.ChooseAddressWheel;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
import com.bjut.servicedog.servicedog.view.listener.OnAddressChangeListener;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateDiscountEventActivity_f extends BaseActivity implements OnAddressChangeListener, View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
    private Calendar c;
    private EditText starTime, endTime, eventtitle, eventaddress, eventdetail;
    private TextView btnCancel;
    private int year, month, day;
    private String  fileurl;
    private TextView  submit, createDiscountAdd;
    private UploadAli uploadAli;
    private GridView mGridView;
    private CheckEventImageAdapter mAdapter;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private Long startDate = 0L, endDate = 0L;
    private ChooseAddressWheel chooseAddressWheel = null;
    private String nid = "";
    private String nation = "";
    private LinearLayout rlAddCity;
    private String areaId = "";
    private long choiseDate = 0;
    private ImageView img_start_time;
    private ImageView img_end_time;
    private TextView tv_title;
    private PhotoWindows photoWindows;

    private CustomHelper mCustomHelper;
    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discount_event_activity_f);
        init();
        initWheel();
    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
        chooseAddressWheel.setDistrict(nid);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mTakePhoto.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putSerializable("mPhotoList", (Serializable) mPhotoList);
        outState.putSerializable("startDate", startDate);
        outState.putSerializable("areaId", areaId);
        String address = createDiscountAdd.getText().toString();
        outState.putSerializable("address", address);
        outState.putSerializable("endDate", endDate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPhotoList = (List<ImageItem>) savedInstanceState.getSerializable("mPhotoList");
        startDate = savedInstanceState.getLong("startDate");
        endDate = savedInstanceState.getLong("endDate");
        areaId = savedInstanceState.getString("areaId");
        String address = savedInstanceState.getString("address");
        createDiscountAdd.setText(address);
        mAdapter = new CheckEventImageAdapter(mContext, mPhotoList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
        mCustomHelper.setEtCropWidth(7);
        mCustomHelper.setEtCropHeight(3);

        c = Calendar.getInstance();
        //取得日历的信息
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        img_start_time = (ImageView) findViewById(R.id.img_start_time);
        img_end_time = (ImageView) findViewById(R.id.img_end_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        submit = (TextView) findViewById(R.id.discount_submit);
        submit.setOnClickListener(this);
        starTime = (EditText) findViewById(R.id.create_discount_start);
        endTime = (EditText) findViewById(R.id.create_discount_end);
        eventtitle = (EditText) findViewById(R.id.create_ed_title);
        eventaddress = (EditText) findViewById(R.id.create_discount_address);
        eventdetail = (EditText) findViewById(R.id.create_discount_detail);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        nid = pref.getString("nid", "");
        nation = pref.getString("nation", "");
        rlAddCity = (LinearLayout) findViewById(R.id.rl_add_city);
        createDiscountAdd = (TextView) findViewById(R.id.create_discount_add);

        tv_title.setText(getString(R.string.title_xjyhhd));
//        createDiscountAdd.setText(nation);

        rlAddCity.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        starTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mAdapter = new CheckEventImageAdapter(this, mPhotoList);
        mGridView.setAdapter(mAdapter);
        //mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        photoWindows = new PhotoWindows(mContext);
        photoWindows.setOnPhotoChioseListener(new PhotoWindows.OnPhotoChioseListener() {
            @Override
            public void onTakePhoto() {
                mCustomHelper.setLimit(1);
                mCustomHelper.onClick(Constant.PHOTO_TAKE, mTakePhoto);
            }

            @Override
            public void onChoisePhoto() {

                mCustomHelper.setLimit(1);
                mCustomHelper.onClick(Constant.PHOTO_CHOISE, mTakePhoto);
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
        if (nid.equals("")) {
            rlAddCity.setVisibility(View.GONE);
        }

        img_start_time.setOnClickListener(this);
        img_end_time.setOnClickListener(this);

    }

    private boolean jundgedata() {
        if (eventtitle.getText().toString().equals("")) {
            toast("请输入消息标题");
            return false;
        } else if (eventdetail.getText().toString().equals("")) {
            toast("请输入活动内容");
            return false;
        } else if (starTime.getText().toString().equals("")) {
            toast("请输入活动的开始时间");
            return false;
        } else if (endTime.getText().toString().equals("")) {
            toast("请输入活动的结束时间");
            return false;
        } else if (eventaddress.getText().toString().equals("")) {
            toast("请输入详细地址");
            return false;
        } else if (mPhotoList.size() == 0) {
            toast("请选择图片");
            return false;
        }
        return true;
    }

    private void sendsubmit() {
        String urlString = "business/createNews.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("merchantNewsType", "1");//活动类型
        if (nid.equals("")) {
            map.put("nationID", "");//
        } else {
            map.put("nationID", areaId);//
        }

        map.put("merchantNewsTitle", eventtitle.getText().toString());//活动标题
        map.put("merchantNewsSummary", eventdetail.getText().toString());//活动概要
        map.put("merchantNewsContent", eventdetail.getText().toString());//活动内容
        map.put("merchantNewsValidStartDate", startDate / 1000 + "");//活动有效起始时间
        map.put("merchantNewsValidEndDate", endDate / 1000 + "");//活动有效结束时间
        map.put("merchantNewseEditor", eventdetail.getText().toString());//活动编辑
        map.put("merchantNewsTargetId", pref.getString("sid", ""));//活动目标类型ID
        map.put("merchantNewsTargetType", "2");// 1商家 2店铺
        map.put("merchantNewsPicture", fileurl);//活动图片
        map.put("merchantNewsPlace", eventaddress.getText().toString());//活动地点
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
                                toast(" 提交资料成功!");
                                EventBus.getDefault().post(new OnRefreshEvent(true));
                                finish();
                            } else {
                                toast("资料提交失败!");
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
    public void onAddressChange(String province, String city, String district, String cityId, String AreaId, String tag) {
        if (district != null && !"null".equals(district)) {
            createDiscountAdd.setText(nation + " " + district);
            areaId = AreaId;
        } else {
            createDiscountAdd.setText(nation);
            areaId = "";
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.discount_submit:

                if (jundgedata()) {
                    if (!Utils.isNotFastClick()) {
                        return;
                    }
                    Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/news/";
                    showProgressDialogPic();
                    uploadAli = new UploadAli(this, mPhotoList, hud);
                    uploadAli.execute(1000);
                    uploadAli.setOnAsyncResponse(new AsyncResponse() {
                        @Override
                        public void onDataReceivedSuccess(String s) {
                            fileurl = s.substring(0, s.length() - 1);
                            Log.i("urllist", fileurl);
                            sendsubmit();
                        }

                        @Override
                        public void onDataReceivedFailed() {
                            closeProgressDialog();
                            toast("上传图片失败");
                        }
                    });
                }
                break;

            case R.id.img_start_time:
            case R.id.create_discount_start:

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                if (monthOfYear < 9) {
                                    mo = "0" + (monthOfYear + 1);
                                } else {
                                    mo = (monthOfYear + 1) + "";
                                }
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                String startime = year + "-" + mo + "-" + da;
                                startDate = DateUtil.getStringToDate(startime);
                                choiseDate = DateUtil.getStringToDate(startime);
                                starTime.setText(startime);
                            }
                        }, year, month, day);
                dpd.setHasOptionsMenu(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setOkColor(getResources().getColor(R.color.blue));
                dpd.setAccentColor(getResources().getColor(R.color.blue));
                dpd.setTitle("开始日期");
                dpd.setOkText("");
                Calendar min = Calendar.getInstance();
                min.set(Calendar.DAY_OF_MONTH, day + 1);  //设置日期
                min.set(Calendar.MONTH, month);
                min.set(Calendar.YEAR, year);
                dpd.setMinDate(min);
                if (!StringUtils.isEmpty(endTime.getText().toString())) {
                    Calendar max = Calendar.getInstance();
                    endDate = DateUtil.getStringToDate(endTime.getText().toString()) + 24 * 3600 * 1000 - 1000;
                    max.setTimeInMillis(endDate);
                    dpd.setMaxDate(max);
                }
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "startDate");
                break;

            case R.id.img_end_time:
            case R.id.create_discount_end:
                if (starTime.getText().toString().isEmpty()) {
                    toast("请先选择活动开始时间");
                    return;
                }
                DatePickerDialog endDpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String mo = "";
                                String da = "";
                                if (monthOfYear < 9) {
                                    mo = "0" + (monthOfYear + 1);
                                } else {
                                    mo = (monthOfYear + 1) + "";
                                }
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                String endtime = year + "-" + mo + "-" + da;
                                endTime.setText(endtime);
                                endDate = DateUtil.getStringToDate(endtime) + 24 * 3600 * 1000 - 1000;
                            }
                        }, year, month, day);
                endDpd.setHasOptionsMenu(true);
                endDpd.setVersion(DatePickerDialog.Version.VERSION_2);
                endDpd.setOkColor(getResources().getColor(R.color.blue));
                endDpd.setAccentColor(getResources().getColor(R.color.blue));
                endDpd.setTitle("结束日期");
                endDpd.setOkText("");
                Calendar endMin = Calendar.getInstance();
                endMin.setTimeInMillis(choiseDate);
                endDpd.setMinDate(endMin);
                endDpd.setCancelable(false);
                endDpd.autoDismiss(true);
                endDpd.show(getFragmentManager(), "endDate");
                break;
            case R.id.rl_add_city:
                chooseAddressWheel.show(v);
                break;
        }

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
