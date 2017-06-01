package com.bjut.servicedog.servicedog.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;

import com.bjut.servicedog.servicedog.utils.ToastUtils;
import com.bjut.servicedog.servicedog.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

//import cn.finalteam.galleryfinal.CoreConfig;
//import cn.finalteam.galleryfinal.FunctionConfig;
//import cn.finalteam.galleryfinal.GalleryFinal;
//import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by Che on 15/9/14.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;

    public static MyApplication getContext() {
        return instance;
    }

    private ArrayList<Map<String, Object>> recentList;

    public ArrayList<Map<String, Object>> getRecentList() {
        return recentList;
    }

    public void setRecentList(ArrayList<Map<String, Object>> recentList) {
        this.recentList = recentList;
    }

    @Override
    public void onCreate() {
        CrashReport.initCrashReport(getApplicationContext(), "900060094", true);

        recentList = new ArrayList<Map<String, Object>>();
        super.onCreate();
        instance = this;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        String a = getAppInfo();
        ToastUtils.init(false);
        Utils.init(getContext());
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
            int versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            String md5 = getSignMd5Str();
            editor.putString("version_name", versionName);
            editor.putString("version_md5", md5);
            editor.commit();
            return pkName + " <-pkName  " + versionName + "<-versionName  " + versionCode + "  <-versionCode  " + md5 + "  <-md5  ";
        } catch (Exception e) {

        }
        return null;
    }

    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    public String getSignMd5Str() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}