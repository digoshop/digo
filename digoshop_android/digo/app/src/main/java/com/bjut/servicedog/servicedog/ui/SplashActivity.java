package com.bjut.servicedog.servicedog.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.po.PhoneInfo;
import com.bjut.servicedog.servicedog.po.Update;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.LoginActivity;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.view.MyOnlyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private int iflogin;
    private static final int SHOW_TIME_MIN = 3000;
    private String ip;
    private int Phone_width, Phone_height;
    AlertDialog.Builder builder;
    Dialog dialog;
    String version = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref.edit().putString("sid", "");
        pref.edit().commit();

        if (checkEnable(this)) {
//            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (isWiFiActive(this)) {
                ip = getLocaIpAddress();
                Log.i("wifiip", ip);
            } else {
                ip = getIpAddress();
                Log.i("ydip", ip);
            }
            editor.putString("ip", ip);
            editor.commit();

            String token = pref.getString("token", "");
            if (token.equals("")) {
                getPhoneInfo();
            } else {
                try {
                    version = getVersionName();
                    requestupdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            toast("当前网络不可用");

            if (mMyOnlyDialog == null) {
                mMyOnlyDialog = new MyOnlyDialog(mContext);
            }
            mMyOnlyDialog.setTitle("连接失败");
            mMyOnlyDialog.setMessage("请检查网络在进行重试");
            mMyOnlyDialog.setOkOnclickListener("确定", new MyOnlyDialog.onOkOnclickListener() {
                @Override
                public void onOkClick() {
                    ActivityCollector.finishAll();
                    System.exit(0);
                }
            });
            mMyOnlyDialog.show();
        }

    }

    public void getPhoneInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Phone_width = dm.widthPixels;
        Phone_height = dm.heightPixels;
        Log.i("Phone_width", Phone_width + "");
        Log.i("Phone_height", Phone_height + "");
        Log.i("model", android.os.Build.MODEL);
        Log.i("os", android.os.Build.VERSION.RELEASE);
        // Log.i("app_version", getAppVersionName(LoginActivity.this));
        Log.i("sdk", android.os.Build.VERSION.SDK);
        String version = null;

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.i("ip", ip);
        try {
            version = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.setApp_version(version);
        phoneInfo.setChannel("zdht");
        phoneInfo.setType(0);
        phoneInfo.setOs(android.os.Build.VERSION.RELEASE);
        phoneInfo.setDid(androidId);
        phoneInfo.setBrand(android.os.Build.BRAND);
        phoneInfo.setModel(android.os.Build.MODEL);
        phoneInfo.setSh(Phone_height);
        phoneInfo.setSw(Phone_width);
        String jstr = JSON.toJSONString(phoneInfo);
        sendPhoneInfo(jstr);
        //sendPhoneInfo(jstr);
    }

    //发起接口请求
    public void sendPhoneInfo(String s) {
        String urlString = "passport/register_by_device.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<String, String>();//准备参数
        map.put("ip", ip);//ip:192.168.0.102
        map.put("device_info", s);//device_info:{"app_version":"1.0","brand":"Xiaomi","channel":"bjut","did":"","model":"Redmi Note 2","os":"5.0.2","sh":1920,"sw":1080,"type":0}
        RequestParams params = new RequestParams();
        params = sortMapByKey(map);//参数排序 ,此方法在BaseActivity中
//        String urlString = "pc/getNurse?phone=%s&personPwd=%s&personId=%s";
//        urlString = String.format(urlString, phone, personPwd, personId);
        //发起请求
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
                            //拿到token
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                editor.putString("token", j.getJSONObject("data").getJSONObject("cookie").getString("token"));
                                editor.putString("uid", j.getJSONObject("data").getJSONObject("cookie").getString("uid"));
                                editor.commit();
                                requestupdate();
                            } else {
                                toast("设备注册失败请重新启动！");
                            }

                        } catch (JSONException e) {
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


    private String getLocaIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean checkEnable(Context paramContext) {
        boolean i = false;
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
            return true;
        return false;
    }

    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //检查更新
    public void requestupdate() {
        String urlString = "setting/version.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appkey", "digo");
        map.put("version_code", version);
        map.put("md5", pref.getString("version_md5", ""));
        map.put("v", "1");
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
                        try {
                            String json = responseInfo.result;
                            Update update = JSON.parseObject(json, Update.class);
                            if (update.getE().getCode() == 0 && update.getData().isUpdate()) {
                                editor.putString("update_json", json);
                                editor.putString("update_if", "true");
                                editor.commit();
                            } else {
                                editor.putString("update_json", json);
                                editor.putString("update_if", "false");
                                editor.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
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

    private String getVersionName() throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public void onClick(View view) {

    }
}



