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
import com.bjut.servicedog.servicedog.adapter.AreaWheelAdapter;
import com.bjut.servicedog.servicedog.adapter.CityWheelAdapter;
import com.bjut.servicedog.servicedog.adapter.ProvinceWheelAdapter;
import com.bjut.servicedog.servicedog.model.Province;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.view.listener.OnAddressChangeListener;
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

public class ChooseAddressWheel implements MyOnWheelChangedListener {

    @Bind(R.id.province_wheel)
    MyWheelView provinceWheel;
    @Bind(R.id.city_wheel)
    MyWheelView cityWheel;
    @Bind(R.id.district_wheel)
    MyWheelView districtWheel;

    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;
    private MyWheelView myWheelView;
    private MyWheelView mycityWheel;

    private List<Province.DataEntity> provinces = null;
    private List<Province.DataEntity> city = null;
    private List<Province.DataEntity> district = null;

    private final int CITY = 0;
    private final int DISTRICT = 1;
    private int base_dimen_40 = 0;

    private OnAddressChangeListener onAddressChangeListener = null;

    public ChooseAddressWheel(Activity context) {
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
        base_dimen_40 = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_40);
    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_city_layout, null);
        ButterKnife.bind(this, parentView);
        myWheelView = (MyWheelView) parentView.findViewById(R.id.province_wheel);
        mycityWheel = (MyWheelView) parentView.findViewById(R.id.city_wheel);

        provinceWheel.setVisibleItems(7);
        cityWheel.setVisibleItems(7);
        districtWheel.setVisibleItems(7);

        provinceWheel.addChangingListener(this);
        cityWheel.addChangingListener(this);
        districtWheel.addChangingListener(this);
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
        ProvinceWheelAdapter provinceWheelAdapter = new ProvinceWheelAdapter(context, provinces);
        provinceWheelAdapter.setTextSize(base_dimen_40);
        provinceWheel.setViewAdapter(provinceWheelAdapter);
        updateCitiy();
        //updateDistrict();
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        } else if (wheel == cityWheel) {
            updateDistrict();//城市改变后地区联动
        } else if (wheel == districtWheel) {
        }
    }

    public void getCity(int nid, final int type) {

        String urlString = "nation/query.json";
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("nationPid", nid + "");
        params = sortMapByKey(map);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOST_URL
                + urlString, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result);
                String json = responseInfo.result;

                Province province = JSON.parseObject(json, Province.class);
                Log.i("zzzeeeeeeeeeer", json);
                province.getData();
                try {
                    if (province.getE().getCode() == 0) {
                        if (CITY == type) {
                            city = province.getData();
                            CityWheelAdapter cityWheelAdapter = new CityWheelAdapter(context, city);
                            cityWheelAdapter.setTextSize(base_dimen_40);
                            cityWheel.setViewAdapter(cityWheelAdapter);
                            cityWheel.setCurrentItem(0);
                            updateDistrict();
                        } else {
                            district = province.getData();
                            AreaWheelAdapter areaWheelAdapter = new AreaWheelAdapter(context, district);
                            areaWheelAdapter.setTextSize(base_dimen_40);
                            districtWheel.setViewAdapter(areaWheelAdapter);
                            districtWheel.setCurrentItem(0);
                        }
                        if (city == null && provinces == null) {
                            myWheelView.setVisibility(View.GONE);
                            mycityWheel.setVisibility(View.GONE);
                        }

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

    private void updateCitiy() {
        int index = provinceWheel.getCurrentItem();
        int nid = provinces.get(index).getNid();
        getCity(nid, CITY);

//            cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
//            cityWheel.setCurrentItem(0);
//            updateDistrict();

    }

    private void updateDistrict() {
        int index = cityWheel.getCurrentItem();
        int cid = city.get(index).getNid();
        getCity(cid, DISTRICT);
//        int provinceIndex = provinceWheel.getCurrentItem();
//        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(provinceIndex).City;
//        int cityIndex = cityWheel.getCurrentItem();
//        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> districts = citys.get(cityIndex).Area;
//        if (districts != null && districts.size() > 0) {
//            districtWheel.setViewAdapter(new AreaWheelAdapter(context, districts));
//            districtWheel.setCurrentItem(0);
//        }

    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setProvince(List<Province.DataEntity> province) {
        this.provinces = province;
        bindData();
    }

    public void setDistrict(String nid) {
        getCity(Integer.parseInt(nid), DISTRICT);
    }

    public void defaultValue(String provinceStr, String city, String arae) {
//        if (TextUtils.isEmpty(provinceStr))
//            return;
//        for (int i = 0; i < province.size(); i++) {
//            AddressDtailsEntity.ProvinceEntity provinces = province.get(i);
//            if (provinces != null && provinces.Name.equalsIgnoreCase(provinceStr)) {
//                provinceWheel.setCurrentItem(i);
//                if (TextUtils.isEmpty(city)) return;
//                List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = provinces.City;
//                for (int j = 0; j < citys.size(); j++) {
//                    AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(j);
//                    if (cityEntity != null && cityEntity.Name.equalsIgnoreCase(city)) {
//                        cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
//                        cityWheel.setCurrentItem(j);
//                        if (TextUtils.isEmpty(arae)) return;
//                        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> areas = cityEntity.Area;
//                        for (int k = 0; k < areas.size(); k++) {
//                            AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = areas.get(k);
//                            if (areaEntity != null && areaEntity.name.equalsIgnoreCase(arae)) {
//                                districtWheel.setViewAdapter(new AreaWheelAdapter(context, areas));
//                                districtWheel.setCurrentItem(k);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    @OnClick(R.id.confirm_button)
    public void confirm() {
        if (onAddressChangeListener != null) {
            int provinceIndex = provinceWheel.getCurrentItem();
            int cityIndex = cityWheel.getCurrentItem();
            int areaIndex = districtWheel.getCurrentItem();

            String provinceName = null, cityName = null, areaName = null;
            int cityId = 0, areaId = 0, tag = 0;
            if (provinces != null && provinces.size() > provinceIndex) {
                // AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                provinceName = provinces.get(provinceIndex).getName();
            }
            if (city != null && city.size() > cityIndex) {
                Province.DataEntity dataEntity = city.get(cityIndex);
                cityName = dataEntity.getName();
                cityId = dataEntity.getNid();
                Log.i("city", cityName + "---" + cityId);
                Log.i("city", dataEntity.getTag() + "");
            }

            if (district != null && district.size() > areaIndex) {
                Province.DataEntity dataEntity = district.get(areaIndex);
                areaName = dataEntity.getName();
                areaId = dataEntity.getNid();
                tag = dataEntity.getTag();

            }
//            if (tag == 0) {
            Log.i("zzzzzr1", provinceName + "");
            Log.i("zzzzzr2", cityName + "");
            Log.i("zzzzz3", areaName + "");
            onAddressChangeListener.onAddressChange(provinceName, cityName, areaName + "", cityId + "", areaId + "", tag + "");
            //  }

        }
        cancel();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
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
            Log.i("zzzzzr", signStr);
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