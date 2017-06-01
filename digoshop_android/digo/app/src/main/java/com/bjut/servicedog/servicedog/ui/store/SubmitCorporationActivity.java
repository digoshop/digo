package com.bjut.servicedog.servicedog.ui.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.adapter.ImagePublishAdapter;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.model.Province;
import com.bjut.servicedog.servicedog.po.Booth;
import com.bjut.servicedog.servicedog.po.BoothList;
import com.bjut.servicedog.servicedog.po.BusinessCircle;
import com.bjut.servicedog.servicedog.po.BusinessCircleList;
import com.bjut.servicedog.servicedog.po.City;
import com.bjut.servicedog.servicedog.po.Floor;
import com.bjut.servicedog.servicedog.po.FloorList;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.AsyncResponse;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.IntentConstants;
import com.bjut.servicedog.servicedog.utils.KeyboardUtils;
import com.bjut.servicedog.servicedog.utils.UploadAli;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.bjut.servicedog.servicedog.utils.photo.CustomHelper;
import com.bjut.servicedog.servicedog.view.ChooseAddressWheel;
import com.bjut.servicedog.servicedog.view.ChooseFloorWheel;
import com.bjut.servicedog.servicedog.view.ImageZoomActivity;
import com.bjut.servicedog.servicedog.view.MyGridView;
import com.bjut.servicedog.servicedog.view.MyOnlyDialog;
import com.bjut.servicedog.servicedog.view.WheelViewBooth;
import com.bjut.servicedog.servicedog.view.WheelViewBusiness;
import com.bjut.servicedog.servicedog.view.WheelViewFloor;
import com.bjut.servicedog.servicedog.view.listener.OnAddressChangeListener;
import com.bjut.servicedog.servicedog.view.listener.OnFoorChangeListener;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;


/**
 * 资质认证
 */
public class SubmitCorporationActivity extends BaseActivity implements OnAddressChangeListener, OnFoorChangeListener, TakePhoto.TakeResultListener, InvokeListener {

    private EditText shop_quan_et, shop_address, principal, principal_phoenumber, zhutimingcheng, license_number, shop_lawname, locationBooth;
    private EditText shop_name;
    private RadioGroup liansuo, operate;
    private RadioButton liansuo_yes, liansuo_no, operate_geti, operate_company;
    private int liansuo_value = 2, operate_value = 2, location_value = 1;
    private LinearLayout location_floortv;
    private TextView register, first, shop_quan, locationFloor;
    private UploadAli uploadAli;
    private String fileurl, boothMbid;
    private MyGridView mGridView;
    private ImagePublishAdapter mAdapter;
    public List<ImageItem> mPhotoList = new ArrayList<ImageItem>();
    private City secondcity = new City();
    private City thirdcity = new City();
    private BusinessCircle bc = new BusinessCircle();
    private Floor fr = new Floor();
    private Booth bt = new Booth();
    private ChooseAddressWheel chooseAddressWheel = null;
    private String myTag = null;
    private int nid = 1;
    private List<Floor> bcs;
    private ChooseFloorWheel chooseFloorWheel = null;

    private String sid = "";
    private String mba = "";
    private String mn = "";
    private View view;
    private TextView tv_title;
    private PhotoWindows photoWindows;
    private String shopkind = "";
    private String runkinds = "";

    private CustomHelper mCustomHelper;
    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakePhoto = getTakePhoto();
        mTakePhoto.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_corporation);
        ButterKnife.bind(this);
        init();
        getProvince();
        view = getLayoutInflater().inflate(R.layout.activity_submit_corporation, null);
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

    @Override
    public void onAddressChange(String province, String city, String district, String cityId, String areaId, String tag) {
        String pcd = province + " " + city + " " + district;
        if (!pcd.equals(first.getText().toString())) {
            locationFloor.setText("");
            shop_quan.setText("");
            shop_address.setText("");
        }

        if (district != null && !"null".equals(district)) {
            first.setText(province + " " + city + " " + district);
            secondcity.setNid(Integer.parseInt(cityId));
            thirdcity.setNid(Integer.parseInt(areaId));
        } else {
            first.setText(province + " " + city);
            secondcity.setNid(Integer.parseInt(cityId));
            thirdcity.setNid(0);
        }


        myTag = tag;

    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private void initWheelFloor() {
        chooseFloorWheel = new ChooseFloorWheel(this);
        chooseFloorWheel.setOnFoorChangeListener(this);

    }

    public void getProvince() {
        showProgressDialog();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        try {
                            Province province = JSON.parseObject(json, Province.class);
                            if (province.getE().getCode() == 0) {
                                chooseAddressWheel.setProvince(province.getData());
                                // getCity();

                            } else {
                                toast(province.getE().getDesc());
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTakePhoto.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode) {
                case IntentConstants.CHECK_PHOTO:
                    mPhotoList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                    mAdapter.setDate(mPhotoList);
                    break;
            }
        }
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_zzrz));

        shopkind = getIntent().getStringExtra("shopkind");
        runkinds = getIntent().getStringExtra("runkinds");
        sid = getIntent().getStringExtra("sid");

        mCustomHelper = new CustomHelper();
        mCustomHelper.setCrop(false);

        Log.i("choose", "接收：shopkind=======" + shopkind);
        Log.i("choose", "接收：runkinds=======" + runkinds);

        initWheel();
        initWheelFloor();
        // initAddressData();
        shop_quan_et = (EditText) findViewById(R.id.shop_quan_et);
        first = (TextView) findViewById(R.id.first);
        first.setOnClickListener(this);
        location_floortv = (LinearLayout) findViewById(R.id.location_floortv);
        location_floortv.setOnClickListener(this);
        locationFloor = (TextView) findViewById(R.id.location_floor);
        liansuo = (RadioGroup) findViewById(R.id.liansuo);
        liansuo_yes = (RadioButton) findViewById(R.id.liansuo_yes);
        liansuo_no = (RadioButton) findViewById(R.id.liansuo_no);
        liansuo.setOnCheckedChangeListener(mChangeRadio);
        operate = (RadioGroup) findViewById(R.id.operate);
        operate_geti = (RadioButton) findViewById(R.id.operate_geti);
        operate_company = (RadioButton) findViewById(R.id.operate_company);
        operate.setOnCheckedChangeListener(mChangeRadio);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        license_number = (EditText) findViewById(R.id.license_number);
        shop_name = (EditText) findViewById(R.id.shop_name);
        shop_quan = (TextView) findViewById(R.id.shop_quan);
        shop_quan.setOnClickListener(this);
        shop_address = (EditText) findViewById(R.id.shop_address);
        principal = (EditText) findViewById(R.id.principal);
        principal_phoenumber = (EditText) findViewById(R.id.principal_phoenumber);
        zhutimingcheng = (EditText) findViewById(R.id.zhutimingcheng);
        license_number = (EditText) findViewById(R.id.license_number);
        shop_lawname = (EditText) findViewById(R.id.shop_lawname);
        mGridView = (MyGridView) findViewById(R.id.gridview);
        mAdapter = new ImagePublishAdapter(this, mPhotoList);
        mAdapter.setFang(true);
        mGridView.setAdapter(mAdapter);
        locationBooth = (EditText) findViewById(R.id.location_booth);

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
                    KeyboardUtils.hideSoftInput(SubmitCorporationActivity.this);
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
    public void onFoorChange(String floorName, String floorNumber, String floorTag, String boothTag, String boothMbid) {
        this.boothMbid = boothMbid;
        String booth = floorTag + floorNumber + "-" + boothTag;
        locationFloor.setText(floorTag + floorNumber + "-" + boothTag);
        shop_address.setText(mba + mn + booth);
    }

    public RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == liansuo_yes.getId()) {
                liansuo_value = 1;
                operate_company.setChecked(true);
                operate_geti.setEnabled(false);
                operate_value = 1;
            } else if (checkedId == liansuo_no.getId()) {
                liansuo_value = 0;
                operate_company.setEnabled(true);
                operate_geti.setEnabled(true);
            } else if (checkedId == operate_geti.getId()) {
                operate_value = 0;
                liansuo_yes.setEnabled(false);
                liansuo_no.setEnabled(true);
            } else if (checkedId == operate_company.getId()) {
                operate_value = 1;
                liansuo_no.setEnabled(true);
                liansuo_yes.setEnabled(true);
            }
        }
    };

    public void change() {

        location_floortv.setVisibility(View.VISIBLE);

        location_value = 2;
    }

    private boolean judgenull() {

        boolean isMobile = isMobileNumber(principal_phoenumber.getText().toString());
        if (shop_name.getText().toString().equals("")) {
            toast("请输入店名");
            return false;
        } else if (first.getText().toString().equals("")) {
            toast("请选择所在区域");
            return false;
        } else if (shop_address.getText().toString().equals("")) {
            toast("请输入详细地址");
            return false;
        } else if (!shop_quan.getText().toString().equals("") && locationFloor.getText().toString().equals("")) {
            toast("请选择商铺位置");
            return false;
        } else if (principal.getText().toString().equals("")) {
            toast("填写负责人姓名");
            return false;
        } else if (principal_phoenumber.getText().toString().equals("")) {
            toast("请填写负责人的联系电话");
            return false;
        } else if (isMobile == false) {
            toast("请输入正确电话号码");
            return false;
        } else if (liansuo_value == 2) {
            toast("请选择是否连锁");
            return false;
        } else if (operate_value == 2) {
            toast("请选择经营主体");
            return false;
        } else if (shop_lawname.getText().toString().equals("")) {
            toast("请认真填写营业执照上的店铺名称");
            return false;
        } else if (zhutimingcheng.getText().toString().equals("")) {
            toast("请输入主体名称");
            return false;
        } else if (license_number.getText().toString().equals("")) {
            toast("请输入营业执照编码");
            return false;
        } else if (mPhotoList.size() == 0) {
            toast("请添加营业执照图片");
            return false;
        }
        return true;
    }

    public static boolean isMobileNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean checkEnable(Context paramContext) {
        boolean i = false;
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
            return true;
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                if (checkEnable(this)) {
                    Utils.hideKeyBoard(this);
                    chooseAddressWheel.show(v);
                } else {
                    toast("请先连接网络!");
                }

                break;
            case R.id.shop_quan:
                if (first.getText().toString().equals("")) {
                    toast("请先选择所在城市");
                } else {

                    requestBusinessList();
                }
                break;
            case R.id.location_floortv:
                if (shop_quan.getText().toString().equals("")) {
                    toast("请先选择商圈");
                } else {
                    if (checkEnable(this)) {
                        requestFloorList();
                        // chooseFloorWheel.show(v);
                    } else {
                        toast("请先连接网络!");
                    }


                }
                break;

            case R.id.register:
                if (judgenull()) {
                    if (Utils.isNotFastClick()) {
                        showProgressDialogPic();
                        uploadAli = new UploadAli(this, mPhotoList, hud);
                        uploadAli.execute(1000);
                        uploadAli.setOnAsyncResponse(new AsyncResponse() {
                            @Override
                            public void onDataReceivedSuccess(String s) {
                                fileurl = s.substring(0, s.length() - 1);
                                Log.i("urllist", fileurl);
                                requestRegister();
                            }

                            @Override
                            public void onDataReceivedFailed() {
                                toast("上传图片失败");
                            }
                        });
                    }

                }
                break;
        }
    }

    private void showBooth(List<Booth> businessCircles) {

        final View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view_booth, null);
        final WheelViewBooth wv = (WheelViewBooth) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);//上下偏移量
        wv.setItems(businessCircles);
        wv.setSeletion(0);
        wv.setOnWheelViewListener(new WheelViewBooth.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, Booth item) {
                Log.i("City", "[Dialog]selectedIndex: " + selectedIndex + ", itemname: " + item.getTag() + "itemid" + item.getMbid());
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("选择所在商圈")
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bt = wv.getSeletedItem();

                    }
                })
                .show();
    }

    private void showFloor(List<Floor> businessCircles) {

        final View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view_floor, null);
        final WheelViewFloor wv = (WheelViewFloor) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);//上下偏移量
        wv.setItems(businessCircles);
        wv.setSeletion(0);
        wv.setOnWheelViewListener(new WheelViewFloor.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, Floor item) {
                Log.i("City", "[Dialog]selectedIndex: " + selectedIndex + ", itemname: " + item.getName() + "itemid" + item.getMflid());
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("选择所在商圈")
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fr = wv.getSeletedItem();

                    }
                })
                .show();
    }

    private void showBusiness(List<BusinessCircle> businessCircles) {

        final View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view_business, null);
        final WheelViewBusiness wv = (WheelViewBusiness) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);//上下偏移量
        wv.setItems(businessCircles);
        wv.setSeletion(0);
        wv.setOnWheelViewListener(new WheelViewBusiness.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, BusinessCircle item) {
                Log.i("City", "[Dialog]selectedIndex: " + selectedIndex + ", itemname: " + item.getMn() + "itemid" + item.getMid());
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("选择所在商圈")
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bc = wv.getSeletedItem();
                        if (!bc.getMn().equals(shop_quan.getText().toString())) {
                            boothMbid = "";
                            locationFloor.setText("");
                            shop_address.setText("");
                        }
                        mba = bc.getMba();
                        mn = bc.getMn();
                        shop_quan.setText(mn);
                    }
                })
                .show();
    }

    private void requestBoothList() {
        String urlString = "business/query_booths.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("merchantId", bc.getMid() + "");
        map.put("merchantFloorId", fr.getMflid() + "");

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
                        Log.i("zzzr2222222", json);
                        try {
                            BoothList boothList = JSON.parseObject(json, BoothList.class);
                            if (boothList.getE().getCode() == 0) {
                                List<Booth> bcs = boothList.getData();
                                showBooth(bcs);
                            } else {
                                toast(boothList.getE().getDesc());
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

    private void requestFloorList() {
        String urlString = "business/query_floors.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("merchantId", bc.getMid() + "");
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
                        Log.i("zzzr111111", json);
                        try {
                            FloorList floorList = JSON.parseObject(json, FloorList.class);
                            if (floorList.getE().getCode() == 0) {
                                bcs = floorList.getData();
                                if (bcs.size() > 0) {
                                    chooseFloorWheel.setProvince(bcs, bc.getMid());
                                    chooseFloorWheel.show(view);
                                } else {
                                    toast("该商场没有楼层");
                                }

                            } else {
                                toast(floorList.getE().getDesc());
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

    private void requestBusinessList() {
        String urlString = "business/query_m.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();

//        if (myTag.equals("1")) {
//            map.put("nid", thirdcity.getNid() + "");
//        } else {
//            map.put("nid", secondcity.getNid() + "");
//        }
        map.put("nid", secondcity.getNid() + "");
        Log.i("zzzr11", myTag + "");
        Log.i("zzzr22", map.get("nid") + "");
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
                        Log.i("aaaa", json);
                        try {
                            BusinessCircleList businessCircleList = JSON.parseObject(json, BusinessCircleList.class);
                            if (businessCircleList.getE().getCode() == 0) {
                                List<BusinessCircle> bcs = businessCircleList.getData();
                                if (bcs.size() > 0) {
                                    location_floortv.setVisibility(View.VISIBLE);
                                    location_value = 2;
                                    showBusiness(bcs);
                                } else {
                                    toast("暂无商圈");
                                }

                            } else {
                                toast(businessCircleList.getE().getDesc());
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

    private void requestRegister() {

        String urlString = "business/submit_shop.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        if (null == sid) {
            map.put("sid", "");//第一次提交传空,修改传sid
        } else {
            map.put("sid", sid);
        }

        map.put("shopOperateType", shopkind);
        map.put("shopName", shop_name.getText().toString());
        if (location_value == 1) {
            map.put("boothName", locationBooth.getText().toString());//手写时合并为一个字段
            map.put("merchantName", shop_quan_et.getText().toString());//手写商圈
        } else {
            if (bc.getMid() == null) {
                map.put("merchantId", "");// 商圈ID 可空
            } else {
                map.put("merchantId", bc.getMid());// 商圈ID 可空
            }
            if (boothMbid == null) {
                map.put("merchantBoothId", "");// 店铺摊位id,  和手填的互斥 只能存在一个 
            } else {
                map.put("merchantBoothId", boothMbid);// 店铺摊位id,  和手填的互斥 只能存在一个
            }
        }
        map.put("nationId", secondcity.getNid() + ""); // 商户所属地区编码,  必选
        if (thirdcity.getNid() == 0) {
            map.put("nationAreaId", "");// 商户所属区域
        } else {
            map.put("nationAreaId", thirdcity.getNid() + "");// 商户所属区域
        }

        map.put("shopAddress", shop_address.getText().toString());
        map.put("shopCorporation", principal.getText().toString());
        map.put("shopMobile", principal_phoenumber.getText().toString());
        map.put("chainShop", liansuo_value + "");
        map.put("operators", operate_value + "");
        map.put("shopContacts", zhutimingcheng.getText().toString());
        map.put("licenceCode", license_number.getText().toString());
        map.put("licenceName", shop_lawname.getText().toString());
        map.put("shopLicence", fileurl);
        map.put("shopOperateIds", runkinds);

        Log.i("choose", "上传：shopkind=======shopkind" + shopkind);
        Log.i("choose", "上传：runkinds=======" + runkinds);

        params = sortMapByKey(map);
        params.addBodyParameter("uid", pref.getString("registeruid", ""));


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
////                                editor.putString("token",j.getJSONObject("data").getJSONObject("cookie").getString("token"));
////                                editor.putString("uid",j.getJSONObject("data").getJSONObject("cookie").getString("uid"));
                                editor.putString("Shopname", shop_name.getText().toString());
                                editor.putString("ShopPhoneNumber", principal_phoenumber.getText().toString());
                                editor.commit();

                                if (mMyOnlyDialog == null) {
                                    mMyOnlyDialog = new MyOnlyDialog(mContext);
                                }
                                mMyOnlyDialog.setTitle("温馨提示");
                                mMyOnlyDialog.setMessage("您的认证资料已提交，审核至少需要1～2个工作日，审核结果将以短信形式通知您，请耐心等待！");
                                mMyOnlyDialog.setOkOnclickListener("确定", new MyOnlyDialog.onOkOnclickListener() {
                                    @Override
                                    public void onOkClick() {
                                        ActivityCollector.finishAll();
                                        myIntentR(LoginActivity.class);
                                    }
                                });
                                mMyOnlyDialog.show();

                            } else {
                                toast(j.getJSONObject("e").getString("desc"));
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
