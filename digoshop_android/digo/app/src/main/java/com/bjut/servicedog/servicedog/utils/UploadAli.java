package com.bjut.servicedog.servicedog.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Xran on 16/7/12.
 */
public class UploadAli extends AsyncTask<Integer, Integer, String> {
    private OSS oss;
    private Context mcontext;
    private String mfilepath;
    private List<ImageItem> mPhotoList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private boolean ifback = false;
    protected KProgressHUD hud;
    private String fileUrl = "";
    public AsyncResponse asyncResponse;

    public void setOnAsyncResponse(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    public UploadAli(Context context, String filepath, KProgressHUD hud) {
        this.mcontext = context;
        this.mfilepath = filepath;
        this.hud = hud;

        initOss();

    }


    public UploadAli(Context context, List<ImageItem> PhotoList, KProgressHUD hud) {
        this.mcontext = context;
        this.mPhotoList = PhotoList;
        this.hud = hud;
        initOss();

    }

    public UploadAli(Context context, String filepath) {
        this.mcontext = context;
        this.mfilepath = filepath;
        initOss();

    }

    public String getRandom() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);        // 8位日期
        // 四位随机数
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }

        String strRandom = random * num + "";
        // 12位序列号,可以自行调整。
        String strReq = s + strRandom.substring(0, 4);
        Log.i("random", random + "");

        Log.i("strRandom", strRandom);
        Log.i("strReq", strReq);
        Log.i("s", s);
        return strReq;
    }

    public void initOss() {

        String endpoint = Constant.ENDPOINT;
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESS_KEYID, Constant.ACCESS_KEYSECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mcontext, endpoint, credentialProvider, conf);
    }

    public void up(String path) {//存储路径为endpoint+"/"+objectKey
        Log.i("path in up", path);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String str = sdf.format(date);
        String name = "";
        int start = path.lastIndexOf("/");
        if (start != -1) {
            name = path.substring(start + 1, path.length());
        }
        Log.i("urlllllllllllllll", Constant.ANDROID_UPLOADFILE_NAMEPRE);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKET_NAME, Constant.ANDROID_UPLOADFILE_NAMEPRE + str + "/" + name, path);


        try {

            PutObjectResult putResult = oss.putObject(put);
            String url = "http://" + Constant.ENDPOINT + "/" + Constant.ANDROID_UPLOADFILE_NAMEPRE + str + "/" + name;
            Log.d("image", url);
            urlList.add(url);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }

//        // 异步上传时可以设置进度回调
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//            }
//        });
////
//        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                urlList.set(num, Constant.ENDPOINT + "/" + Constant.ANDROID_UPLOADFILE_NAMEPRE + "/" + random);
//                Log.i("urlist" + num, Constant.ENDPOINT + "/" + Constant.ANDROID_UPLOADFILE_NAMEPRE + "/" + random);
//                if (num<mPhotoList.size()){
//                    num++;
//                }else {
//                    ifback=true;
//                }
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ETag", result.getETag());
//                Log.d("RequestId", result.getRequestId());
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//            }
//        });

    }

    @Override
    protected String doInBackground(Integer... integers) {

        for (int i = 0; i < mPhotoList.size(); i++) {
            if (mPhotoList.get(i).getImageUrl().equals("")) {
                up(mPhotoList.get(i).getSourcePath());
            } else {
                String url = mPhotoList.get(i).getImageUrl();
                Log.d("image", url);
                urlList.add(url);
            }
        }
        for (int i = 0; i < urlList.size(); i++) {
            fileUrl += urlList.get(i) + ",";
        }
        Log.i("image", fileUrl);
        return fileUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("in post", "true");
        if (hud != null) {
            hud.dismiss();
        }
        if (null != s && !"".equals(s)) {
            asyncResponse.onDataReceivedSuccess(s);
        } else {
            Toast.makeText(mcontext, "请上传图片", Toast.LENGTH_SHORT).show();
            asyncResponse.onDataReceivedFailed();
        }
    }
}
