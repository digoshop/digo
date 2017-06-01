package com.bjut.servicedog.servicedog.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;
import com.bjut.servicedog.servicedog.ui.base.BaseActivity;
import com.bjut.servicedog.servicedog.utils.MyImageLoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beibeizhu on 16/11/1.
 */

public class ShopQRImageActivity extends BaseActivity {

    private ImageView erwei;
    private TextView mVersion;
    private TextView tv_title;
//    private String directoryPath = Environment.getExternalStorageDirectory() + "/DCIM/DIGO/";
    private String directoryPath;
//    private String filePath = directoryPath + "QRcode_" + System.currentTimeMillis() + ".jpg";
    private String filePath;
    private final int SHOW_CODE = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_CODE:
                    MyImageLoder.getInstance().disImage(mContext,"file://" + filePath, erwei);
//                    ImageLoader.getInstance().displayImage("file://" + filePath, erwei);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_erweima_product);
        initViews();

    }

    private void initViews() {
        erwei = (ImageView) findViewById(R.id.erwei);
        mVersion = (TextView) findViewById(R.id.tv_version_text);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.title_dianpuerweima));

        directoryPath =  mContext.getExternalCacheDir().getAbsolutePath();
        filePath = directoryPath + "QRcode_" + System.currentTimeMillis() + ".jpg";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("shop_id", pref.getString("sid", ""));
                    jsonObject.put("type", "2");
                    String jsonStr = jsonObject.toString();
                    boolean isSuccess = createQRImage(jsonStr, 500, 500, filePath);
                    if (isSuccess) {
                        mHandler.sendEmptyMessage(SHOW_CODE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static boolean createQRImage(String content, int widthPix, int heightPix, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            //配置参数
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void onClick(View view) {

    }
}
