package com.bjut.servicedog.servicedog.ui.activies;

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
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.ChooseAddressWheel;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
import com.bjut.servicedog.servicedog.view.My2BDialog;
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

public class EditDiscountEventActivity extends BaseActivity implements OnAddressChangeListener,View.OnClickListener,TakePhoto.TakeResultListener, InvokeListener {

    private Calendar c;
    private EditText starTime, endTime, eventtitle, eventaddress, eventdetail;
    private TextView btnCancel;

    private int year, month, day;
    private String fileurl, flag;
    private TextView  submit, piscountAdd, createDiscountAdd;
    private UploadAli uploadAli;
    private GridView mGridView;
    private CheckEventImageAdapter mAdapter;
    private String imageurl, merchantNewsId;
    private Long startDate, endDate, choiseDate;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private InputMethodManager im;
    private ChooseAddressWheel chooseAddressWheel = null;
    private String nid;
    private String nation;
    private LinearLayout rlAddCity;
    private String areaId;
    private String mnafr;
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

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
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

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_bjyhhd));

        c = Calendar.getInstance();
        //取得日历的信息
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        flag = getIntent().getStringExtra("flag");
        mnafr = getIntent().getStringExtra("mnafr");
        submit = (TextView) findViewById(R.id.discount_submit);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        submit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        starTime = (EditText) findViewById(R.id.create_discount_start);
        endTime = (EditText) findViewById(R.id.create_discount_end);
        eventtitle = (EditText) findViewById(R.id.create_ed_title);
        eventaddress = (EditText) findViewById(R.id.create_discount_address);
        eventdetail = (EditText) findViewById(R.id.create_discount_detail);
        starTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        piscountAdd = (TextView) findViewById(R.id.create_discount_add);
        createDiscountAdd = (TextView) findViewById(R.id.create_discount_add);
        nid = pref.getString("nid", "");
        nation = pref.getString("nation", "");
        rlAddCity = (LinearLayout) findViewById(R.id.rl_add_city);
        rlAddCity.setOnClickListener(this);
        areaId = getIntent().getStringExtra("naid");
        merchantNewsId = getIntent().getStringExtra("eventid");
        imageurl = getIntent().getStringExtra("eventpicture");
        eventtitle.setText(getIntent().getStringExtra("eventtile"));
        startDate = Long.parseLong(getIntent().getStringExtra("eventstarttime"));
        endDate = Long.parseLong(getIntent().getStringExtra("eventendtime"));
        starTime.setText(getIntent().getStringExtra("strstartime"));
        endTime.setText(getIntent().getStringExtra("strendtime"));
        eventaddress.setText(getIntent().getStringExtra("eventplace"));
        eventdetail.setText(getIntent().getStringExtra("eventdetail"));
        Log.i("zzzzr", getIntent().getStringExtra("city") + " " + getIntent().getStringExtra("area"));
        piscountAdd.setText(getIntent().getStringExtra("city") + " " + getIntent().getStringExtra("area"));
//        createDiscountAdd.setText(nation);
        if (areaId.equals("0")) {
            areaId = "";
        }
        ImageItem shop_album = new ImageItem();
        shop_album.setSourcePath("");
        shop_album.setImageUrl(imageurl);
        mPhotoList.add(shop_album);
        mGridView = (GridView) findViewById(R.id.gridview);

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
                    startActivityForResult(intent, IntentConstants.CHECK_PHOTO);
                }
            }

        });
        if (flag.equals("see")) {
            tv_title.setText("查看活动信息");
            eventtitle.setEnabled(false);
            starTime.setOnClickListener(null);
            endTime.setOnClickListener(null);
            rlAddCity.setOnClickListener(null);
            eventaddress.setEnabled(false);
            eventdetail.setEnabled(false);
            mGridView.setOnItemClickListener(null);
            submit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        } else {
            if (mnafr != null) {

                if (mMyDialog == null) {
                    mMyDialog = new My2BDialog(mContext);
                }
                mMyDialog.setTitle("驳回原因");
                mMyDialog.setMessage(mnafr);
                mMyDialog.setYesOnclickListener("确认修改", new My2BDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        mMyDialog.dismiss();
                    }
                });
                mMyDialog.setNoOnclickListener("取消", new My2BDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        mMyDialog.dismiss();
                        finish();
                    }
                });
                mMyDialog.show();
            }
        }

        mAdapter = new CheckEventImageAdapter(this, mPhotoList);
        mGridView.setAdapter(mAdapter);
    }

    private void sendedit() {
        String urlString = "business/update_news.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        if (nid.equals("")) {
            map.put("nationID", "");//
        } else {
            map.put("nationID", areaId);//
        }
        map.put("merchantNewsId", merchantNewsId);//活动类型
        map.put("merchantNewsTitle", eventtitle.getText().toString());//活动标题
        map.put("merchantNewsContent", eventdetail.getText().toString());//活动内容
        map.put("merchantNewsValidStartDate", startDate + "");//活动有效起始时间
        map.put("merchantNewsValidEndDate", endDate + "");//活动有效结束时间
        map.put("merchantNewsPicture", fileurl);//活动图片
        map.put("merchantNewsPlace", eventaddress.getText().toString());//活动地点
        map.put("opuid", pref.getString("uid", ""));//活动地点
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
                                toast(" 修改信息成功!");
                                EventBus.getDefault().post(new OnRefreshEvent(true));
                                finish();
                            } else {
                                toast(jsonObject.getJSONObject("e").getString("desc"));
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
//        createDiscountAdd.setText(nation + " " + district);
//        areaId = AreaId;
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
            case R.id.discount_submit:
                showProgressDialogPic();
                Constant.ANDROID_UPLOADFILE_NAMEPRE = "android_shop/news/";
                uploadAli = new UploadAli(this, mPhotoList, hud);
                uploadAli.execute(1000);
                uploadAli.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(String s) {
                        fileurl = s.substring(0, s.length() - 1);
                        Log.i("urllist", fileurl);
                        sendedit();
                    }

                    @Override
                    public void onDataReceivedFailed() {
                        toast("上传图片失败");
                    }
                });
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.create_discount_start:
                final long start = DateUtil.formatDateTime2Long(c.getTime().getTime()) + 24 * 3600 * 1000L;
                im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
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
                                startDate = DateUtil.getStringToDate(startime)/1000;
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
                dpd.setCancelable(false);
                dpd.autoDismiss(true);
                dpd.show(getFragmentManager(), "startDate");
                break;
            case R.id.create_discount_end:
                String starttime = starTime.getText().toString();
                if (starttime.isEmpty()) {
                    toast("请先选择活动开始时间");
                    return;
                }
                choiseDate = DateUtil.getStringToDate(starttime);
                im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
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
                                endDate = (DateUtil.getStringToDate(endtime) + 24 * 3600 * 1000 - 1000)/1000;
                                endTime.setText(endtime);
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
