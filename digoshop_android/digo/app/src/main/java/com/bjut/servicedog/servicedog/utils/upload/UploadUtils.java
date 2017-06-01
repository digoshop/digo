package com.bjut.servicedog.servicedog.utils.upload;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bjut.servicedog.servicedog.model.ImageItem;
import com.bjut.servicedog.servicedog.utils.Constant;
import com.bjut.servicedog.servicedog.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by beibeizhu on 17/4/10.
 */

public class UploadUtils {

    private Context mcontext;
    private OSS oss;
    private List<ImageItem> mPhotoList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private OnUploadListener onUploadListener;
    private int index = 0;

    public UploadUtils(Context mcontext, List<ImageItem> mPhotoList, OnUploadListener onUploadListener) {
        this.mcontext = mcontext;
        this.mPhotoList = mPhotoList;
        this.onUploadListener = onUploadListener;
        initOss();
    }

    private void initOss() {

        String endpoint = Constant.ENDPOINT;
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESS_KEYID, Constant.ACCESS_KEYSECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mcontext, endpoint, credentialProvider, conf);
    }

    public void upload() {
        ImageItem imageItem = mPhotoList.get(index);
        if (StringUtils.isEmpty(imageItem.getSourcePath())) {
            urlList.add(imageItem.getImageUrl());
            Log.d("image", imageItem.getImageUrl());
            index++;
            if (index == mPhotoList.size()) {
                String urls = "";
                for (int i = 0; i < urlList.size(); i++) {
                    urls += urlList.get(i) + ",";
                }
                if (urls.length() > 0) {
                    urls = urls.substring(0, urls.length() - 1);
                }
                Log.d("image", urls);
                onUploadListener.onSuccess(urls);
            } else {
                upload();
            }
        } else {
            String path = imageItem.getSourcePath();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String str = sdf.format(date);
            String name = "";
            int start = path.lastIndexOf("/");
            if (start != -1) {
                name = path.substring(start + 1, path.length());
            }
            final String fileName = str + "/" + name;
            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(Constant.BUCKET_NAME, Constant.ANDROID_UPLOADFILE_NAMEPRE + fileName, path);

            // 异步上传时可以设置进度回调
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                }
            });

            OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    Log.d("PutObject", "UploadSuccess");
                    String url = "http://" + Constant.ENDPOINT + "/" + Constant.ANDROID_UPLOADFILE_NAMEPRE + fileName;
                    Log.d("image", url);
                    urlList.add(url);
                    index++;
                    if (index == mPhotoList.size()) {
                        String urls = "";
                        for (int i = 0; i < urlList.size(); i++) {
                            urls += urlList.get(i) + ",";
                        }
                        if (urls.length() > 0) {
                            urls = urls.substring(0, urls.length() - 1);
                        }
                        Log.d("image", urls);
                        onUploadListener.onSuccess(urls);
                    } else {
                        upload();
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                        onUploadListener.onFailure("请检查网络");
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                        onUploadListener.onFailure("服务器异常，请稍后再试");
                    }
                }
            });

            // task.cancel(); // 可以取消任务

            task.waitUntilFinished(); // 可以等待直到任务完成
        }
    }

}
