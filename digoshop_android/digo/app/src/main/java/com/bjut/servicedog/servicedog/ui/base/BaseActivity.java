package com.bjut.servicedog.servicedog.ui.base;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.utils.ActivityCollector;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.KeyboardUtils;
import com.bjut.servicedog.servicedog.utils.LogUtil;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.SystemBarTintManager;
import com.bjut.servicedog.servicedog.utils.ToastUtils;
import com.bjut.servicedog.servicedog.view.My2BDialog;
import com.bjut.servicedog.servicedog.view.MyOnlyDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lidroid.xutils.http.RequestParams;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseActivity extends Activity implements EasyPermissions.PermissionCallbacks,View.OnClickListener {// EasyPermissions.PermissionCallbacks
    protected final String TAG = this.getClass().getSimpleName();
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    private long exitTime = 0;
    protected View noDataView;
    protected KProgressHUD hud;
    protected TextView tv_empty;
    private int width = 0;
    protected Context mContext;
    protected My2BDialog mMyDialog;
    protected MyOnlyDialog mMyOnlyDialog;

    protected static final int REQUEST_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        //顶部颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.title_background);//通知栏所需颜色
        }



        width = getResources().getDimensionPixelOffset(R.dimen.base_dimen_80);
        ActivityCollector.addActivity(this);
        mContext = this;
        mMyDialog = new My2BDialog(this);
        mMyOnlyDialog = new MyOnlyDialog(this);

        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_nodate, null, false);
        tv_empty = (TextView) noDataView.findViewById(R.id.tv_empty);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hud != null) {
            hud.dismiss();
            hud = null;
        }
        if (mMyDialog != null) {
            mMyDialog.dismiss();
            mMyDialog = null;
        }
        if (mMyOnlyDialog != null) {
            mMyOnlyDialog.dismiss();
            mMyOnlyDialog = null;
        }
        ActivityCollector.removeActivity(this);
    }

    protected void setEmptyText(String text) {
        tv_empty.setText(text);
    }

    //顶部颜色
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showProgressDialog() {//加载数据进度条
        LogUtil.d("trace", "showProgressDialog()");
        if (hud == null) {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(width, width)
                    .setLabel("正在加载...")
                    .setCancellable(true);
        }
        hud.show();
    }

    protected void showProgressDialogDelete() {//加载数据进度条
        LogUtil.d("trace", "showProgressDialog()");
        if (hud == null) {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("正在清理...")
                    .setCancellable(true);

        }
        hud.show();
    }

    protected void showProgressDialogPic() {//加载数据进度条
        LogUtil.d("trace", "showProgressDialog()");
        if (hud == null) {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("正在上传...")
                    .setCancellable(true);

        }
        hud.show();
    }

    protected void closeProgressDialog() {
        LogUtil.d("trace", "closeProgressDialog()");
        if (hud != null) {
            hud.dismiss();
            hud = null;
        }
    }

    protected String timeStamp() {
        String a = System.currentTimeMillis() / 1000 + "";

        return a;
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
            Log.i(m.getKey(), m.getValue());
            //System.out.println(m.getKey() + ":" + m.getValue());
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
        Log.i("sign",signMd5);
        params.addBodyParameter("token", pref.getString("token", ""));
        Log.i("token", pref.getString("token", ""));
        return params;
    }

    // 判断是否最后一个Activity并再按一次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && isTaskRoot()) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void myIntent(Class<?> cls) {//跳转
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);

    }

    public void myIntentR(Class<?> cls) {//跳转
        Intent intent = new Intent();
        intent.setClass(this, cls);
//		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

    }

    public void myIntentRtoTop(Class<?> cls) {//跳转
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

    }

    public void myIntentD(Class<?> cls) {//跳转
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

    }

    public void myIntentT(Class<?> cls) {//跳转
        Intent intent = new Intent();
        intent.setClass(this, cls);
//		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.push_top_in, R.anim.push_down_out);

    }

    public void myIntentSign(Class<?> cls, String string) {//跳转
        Intent intent = new Intent();
        intent.putExtra("from", string);
        intent.setClass(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    public Spinner initSpinner(int id, String[] data, String prompt) {//初始化spinner
        List<String> list = null;
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setPrompt(prompt);// 设置Prompt
        list = Arrays.asList(data);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);// 实例化ArrayAdapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置列表项显示风格
        spinner.setAdapter(adapter);// 设置显示信息
        return spinner;
    }

    public Spinner initSpinnerByList(int id, List data, String prompt) {//初始化spinner
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setPrompt(prompt);// 设置Prompt
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, data);// 实例化ArrayAdapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置列表项显示风格
        spinner.setAdapter(adapter);// 设置显示信息
        return spinner;
    }


    protected void toast(String str) {
        ToastUtils.showShortToastSafe(str);
    }

    public void OnMySelfClick(View v) {//返回按钮
        KeyboardUtils.hideSoftInput(this);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    /**
     * [绑定控件]
     *
     * @param resId
     * @return
     */
    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }


}
