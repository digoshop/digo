package com.bjut.servicedog.servicedog.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.BoothWheelAdapter;
import com.bjut.servicedog.servicedog.adapter.FloorWheelAdapter;
import com.bjut.servicedog.servicedog.po.Booth;
import com.bjut.servicedog.servicedog.po.BoothList;
import com.bjut.servicedog.servicedog.po.Floor;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.ToastUtils;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.listener.OnFoorChangeListener;
import com.bjut.servicedog.servicedog.view.wheelview.MyOnWheelChangedListener;
import com.bjut.servicedog.servicedog.view.wheelview.MyWheelView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFloorWheel implements MyOnWheelChangedListener {

    @Bind(R.id.floor_wheel)
    MyWheelView floorWheel;
    @Bind(R.id.booth_wheel)
    MyWheelView boothWheel;


    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;
    private MyWheelView myWheelView;
    private MyWheelView mycityWheel;

    private String mid = null;
    private List<Floor> floors = null;
    private List<Booth> booth = null;


    private final int CITY = 0;
    private final int DISTRICT = 1;

    private OnFoorChangeListener onFoorChangeListener = null;

    private int base_dimen_40 = 0;

    public ChooseFloorWheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();

    }

    private void initView() {
        base_dimen_40 = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_40);
        parentView = layoutInflater.inflate(R.layout.choose_floor_layout, null);
        ButterKnife.bind(this, parentView);
        myWheelView = (MyWheelView) parentView.findViewById(R.id.floor_wheel);
        mycityWheel = (MyWheelView) parentView.findViewById(R.id.booth_wheel);

        floorWheel.setVisibleItems(7);
        boothWheel.setVisibleItems(7);


        floorWheel.addChangingListener(this);
        boothWheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    private void bindData() {
        FloorWheelAdapter floorWheelAdapter = new FloorWheelAdapter(context, floors);
        floorWheelAdapter.setTextSize(base_dimen_40);
        floorWheel.setViewAdapter(floorWheelAdapter);
        updateBooth();

    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == floorWheel) {
            updateBooth();
        }
    }

    private void requestBoothList(String mflid) {
        String urlString = "business/query_booths.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("merchantId", mid);
        map.put("merchantFloorId", mflid);

        params = sortMapByKey(map);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;
                try {
                    BoothList boothList = JSON.parseObject(json, BoothList.class);
                    if (boothList.getE().getCode() == 0) {
                        booth = boothList.getData();
                        BoothWheelAdapter boothWheelAdapter = new BoothWheelAdapter(context, booth);
                        boothWheelAdapter.setTextSize(base_dimen_40);
                        boothWheel.setViewAdapter(boothWheelAdapter);
                        boothWheel.setCurrentItem(0);

                    } else {

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }


    private void updateBooth() {
        int index = floorWheel.getCurrentItem();
        String mflid = floors.get(index).getMflid();
        requestBoothList(mflid);
    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setProvince(List<Floor> floors, String mid) {
        this.floors = floors;
        this.mid = mid;
        bindData();
    }

    @OnClick(R.id.confirm_btn)
    public void confirm() {
        String floorName = null, floorNumber = null, floorTag = null, boothTag = null, boothMbid = null;
        if (onFoorChangeListener != null) {
            int floorIndex = floorWheel.getCurrentItem();
            int boothIndex = boothWheel.getCurrentItem();

            int cityId = 0, areaId = 0, tag = 0;
            if (floors != null && floors.size() > floorIndex) {
                // AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                floorName = floors.get(floorIndex).getName();
                floorNumber = floors.get(floorIndex).getNumber();
                floorTag = floors.get(floorIndex).getTag();

            }
            if (booth != null && booth.size() > boothIndex) {
                Booth booths = booth.get(boothIndex);
                boothTag = booths.getTag();
                boothMbid = booths.getMbid();
                Log.i("bbbbb", "bbbbb" + boothTag);
            }
            if (floorNumber == null) {
                ToastUtils.showShortToastSafe("请选择楼层");
            } else if (boothMbid == null) {
                ToastUtils.showShortToastSafe("请选择楼层");
            } else {
                onFoorChangeListener.onFoorChange(floorName, floorNumber, floorTag, boothTag, boothMbid);
                cancel();
            }


        }


    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnFoorChangeListener(OnFoorChangeListener onFoorChangeListener) {
        this.onFoorChangeListener = onFoorChangeListener;
    }

    public RequestParams sortMapByKey(Map<String, String> map) {
        String signStr = "";
        String signMd5 = "";
        map.put("timestamp", timeStamp());
        RequestParams params = new RequestParams();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        for (Map.Entry<String, String> m : infoIds) {
            signStr += m.getKey() + m.getValue();
            params.addBodyParameter(m.getKey(), m.getValue());
            System.out.println(m.getKey() + ":" + m.getValue());
        }
        Log.i("sortkeyvalue", signStr);
        try {
            signMd5 = MD5Encryption.md5crypt(signStr + Constant.APPKEY);
            Log.i("signStr", signStr + Constant.APPKEY);
            Log.i("signMD5", signMd5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("md5", "signmd5exception");
        }

        params.addBodyParameter("sign", signMd5);
        params.addBodyParameter("token", pref.getString("token", ""));

        return params;
    }

    protected String timeStamp() {
        String a = System.currentTimeMillis() / 1000 + "";

        return a;
    }

    protected Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Log.i("startime", todayStart.getTime().getTime() / 1000 + "");
        return todayStart.getTime().getTime() / 1000;
    }
}