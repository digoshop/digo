package com.bjut.servicedog.servicedog.ui.store;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.ui.store.ChooseKindActivity;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.MD5Encryption;
import com.bjut.servicedog.servicedog.utils.RegularUtils;
import com.bjut.servicedog.servicedog.utils.TimerCount;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 注册
 */
public class MerchantCorporationActivity extends BaseActivity implements View.OnClickListener{

    private EditText account_number, account_password, account_phonenumber, input_yanzhengma;
    private TextView get_yanzheng, register;
    static boolean flag;
    private TextView tv_title;

    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{2,2}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_corporation);
        init();
        Log.i("zzzz", pref.getString("ip", ""));
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_sjhz));
        account_password = (EditText) findViewById(R.id.account_password);
        account_number = (EditText) findViewById(R.id.account_number);
        account_phonenumber = (EditText) findViewById(R.id.account_phonenumber);
        input_yanzhengma = (EditText) findViewById(R.id.input_yanzhengma);
        get_yanzheng = (TextView) findViewById(R.id.get_yanzheng);

        get_yanzheng.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    public static boolean isPassWord(CharSequence input) {
        return isMatch(REGEX_PASSWORD, input);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_yanzheng:
                if (account_phonenumber.getText().toString().equals("")) {
                    toast("请输入手机号");
                    return;
                } else if (account_password.getText().toString().equals("")) {
                    toast("密码填写不能为空");
                    return;
                } else if (account_phonenumber.getText().toString().length() != 11) {
                    toast("手机号位数不对,请重新输入");
                    return;
                } else if (!RegularUtils.isMobileNO(account_phonenumber.getText().toString())) {
                    toast("您输入的手机号格式不正确，请重新输入！");
                    return;
                } else {
                    checkaccountphonenumber();
                }


                break;
            case R.id.register:
                String account = account_number.getText().toString();
                if ("".equals(account)) {
                    toast("请输入账户名称");
                    return;
                }
                if (account_number.getText().toString().length() < 6 || account_number.getText().toString().length() > 20) {
                    toast("账户名称由6-20位组成");
                    return;
                }
                String start = account.substring(0, 1);
                String end = account.substring(account.length() - 1, account.length());
                if ("_".equals(start) || "_".equals(end)) {
                    toast("账户名称不能以下划线开头和结尾");
                    return;
                }
                if (RegularUtils.isUsername(account) == false) {
                    toast("账户名称至少包含数字字母下划线中的两种");
                    return;
                }
                if (account_password.getText().toString().equals("")) {
                    toast("请输入密码");
                    return;
                }
                if (account_password.getText().toString().length() < 6 || account_password.getText().toString().length() > 16) {
                    toast("请输入6-16位密码");
                    return;
                }
                if (account_phonenumber.getText().toString().equals("")) {
                    toast("请输入手机号码");
                    return;
                }
                if (input_yanzhengma.getText().toString().equals("")) {
                    toast("请输入验证码");
                    return;
                }
                editor.putString("sid", "");
                checkaccountnumber();
//                    requestRegister();
                editor.commit();
                break;

        }
    }

    public static boolean check(String str, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    private void sendSms() {
        String urlString = "passport/sendsms.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", account_phonenumber.getText().toString());
        map.put("check", "0");
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                toast("已发送");
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

    private void requestRegister() {
        String pass = "";
        try {
            pass = MD5Encryption.md5crypt(account_password.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String urlString = "passport/shop_register.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("ip", pref.getString("ip", ""));
        map.put("mobile", account_phonenumber.getText().toString());
        map.put("password", pass);
        map.put("login_name", account_number.getText().toString());
        map.put("smscode", input_yanzhengma.getText().toString());
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
                            JSONObject j = new JSONObject(json);
                            if (j.getJSONObject("e").getInt("code") == 0) {
                                editor.putString("token", j.getJSONObject("data").getJSONObject("cookie").getString("token"));
                                editor.putString("registeruid", j.getJSONObject("data").getJSONObject("cookie").getString("uid"));
                                editor.commit();
                                myIntentR(ChooseKindActivity.class);
                                finish();
                            } else {
                                toast("注册失败,请重新填写注册信息!");
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

    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    private void checkaccountnumber() {
        String urlString = "passport/s_exist.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("token",pref.getString("token", ""));
        map.put("passport", account_number.getText().toString());
        params = sortMapByKey(map);
        params.addBodyParameter("token", pref.getString("token", ""));
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
                            if (j.getJSONObject("e").getInt("code") == -112) {
                                Log.i("sdfsd", "sdfs");
                                toast("您输入的帐号已注册，请重新输入帐号名或返回登录页面");
                            } else if (j.getJSONObject("e").getInt("code") == 0) {

                                requestRegister();
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

    private void checkaccountphonenumber() {
        String urlString = "passport/s_exist.json";
        urlString = String.format(urlString);
        Log.i("out", urlString);
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        //map.put("token",pref.getString("token", ""));
        map.put("passport", account_phonenumber.getText().toString());
        params = sortMapByKey(map);
        params.addBodyParameter("token", pref.getString("token", ""));
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
                            if (j.getJSONObject("e").getInt("code") == -112) {
                                toast("您输入的手机号已被注册，请重新输入其他手机号或点击忘记密码，进入手机快捷登录！");
                                return;
                            } else {
                                TimerCount timerCount = new TimerCount(60000, 1000, get_yanzheng);
                                timerCount.start();
                                sendSms();
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

}
