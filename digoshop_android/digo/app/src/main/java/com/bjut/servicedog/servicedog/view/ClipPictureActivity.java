//package com.bjut.servicedog.servicedog.view;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewTreeObserver;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout;
//
//import com.bjut.servicedog.servicedog.R;
//import com.bjut.servicedog.servicedog.model.ImageItem;
//import com.bjut.servicedog.servicedog.ui.AddAuctionGoodsActivity_f;
//import com.bjut.servicedog.servicedog.ui.AddExchangeGoodsActivity_F;
//import com.bjut.servicedog.servicedog.ui.BaseActivity;
//import com.bjut.servicedog.servicedog.ui.CreateDiscountEventActivity_f;
//import com.bjut.servicedog.servicedog.ui.CustomizationReplyActivity_f;
//import com.bjut.servicedog.servicedog.ui.EditDiscountEventActivity;
//import com.bjut.servicedog.servicedog.ui.EditExchangeGoodActivity;
//import com.bjut.servicedog.servicedog.ui.NewAddProductionActivity;
//import com.bjut.servicedog.servicedog.ui.ShopSettingActivity;
//import com.bjut.servicedog.servicedog.ui.StoreManageActivity;
//import com.bjut.servicedog.servicedog.ui.SubmitCorporationActivity;
//import com.bjut.servicedog.servicedog.ui.UpdateAuctionGoodsActivity;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ClipPictureActivity extends BaseActivity implements OnTouchListener,
//        OnClickListener {
//    private ImageView srcPic;
//    /**
//     * 保存按钮
//     */
//    private LinearLayout saveLayout;
//    /**
//     * 返回
//     */
//    private LinearLayout layout_finish;
//    /**
//     * 取消
//     */
//    private LinearLayout line_cancle;
//    private ClipView clipview;
//
//    private Matrix matrix = new Matrix();
//    private Matrix savedMatrix = new Matrix();
//
//    /**
//     * 动作标志：无
//     */
//    private static final int NONE = 0;
//    /**
//     * 动作标志：拖动
//     */
//    private static final int DRAG = 1;
//    /**
//     * 动作标志：缩放
//     */
//    private static final int ZOOM = 2;
//    /**
//     * 初始化动作标志
//     */
//    private int mode = NONE;
//
//    /**
//     * 记录起始坐标
//     */
//    private PointF start = new PointF();
//    /**
//     * 记录缩放时两指中间点坐标
//     */
//    private PointF mid = new PointF();
//    private float oldDist = 1f;
//
//    private Bitmap bitmap;
//    private String mPath;
//    public int screenWidth = 0;
//    public int screenHeight = 0;
//    /**
//     * 裁剪图片地址
//     */
//    public String filename = Environment.getExternalStorageDirectory()
//            .getPath() + "/clip.png";
//    public List<ImageItem> clipPhotoList = new ArrayList<ImageItem>();
//    private int nowfile = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.clip_image);
//        // byte[] bis = getIntent().getByteArrayExtra("bitmap");
//        // bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        getWindowWH();
////		mPath = getIntent().getStringExtra("path");
////		bitmap = createBitmap(mPath, screenWidth, screenHeight);
//        clipPhotoList.addAll((List<ImageItem>) getIntent().getSerializableExtra("path"));
//
//        bitmap = createBitmap(clipPhotoList.get(nowfile).getSourcePath(), screenWidth, screenHeight);
//
//        srcPic = (ImageView) this.findViewById(R.id.src_pic);
//        srcPic.setScaleType(ScaleType.CENTER_INSIDE);
//        srcPic.setOnTouchListener(this);
//
//        ViewTreeObserver observer = srcPic.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//            @SuppressWarnings("deprecation")
//            public void onGlobalLayout() {
//                srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                initClipView(srcPic.getTop(), bitmap);
//            }
//        });
//        saveLayout = (LinearLayout) this.findViewById(R.id.sure);
//        line_cancle = (LinearLayout) this.findViewById(R.id.line_cancle);
//        line_cancle.setOnClickListener(this);
//        saveLayout.setOnClickListener(this);
//    }
//
//    /**
//     * 获取屏幕的高和宽
//     */
//    private void getWindowWH() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        screenWidth = dm.widthPixels;
//        screenHeight = dm.heightPixels;
//    }
//
//    public Bitmap createBitmap(String path, int w, int h) {
//        try {
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;
//            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
//            BitmapFactory.decodeFile(path, opts);
//            int srcWidth = opts.outWidth;// 获取图片的原始宽度
//            int srcHeight = opts.outHeight;// 获取图片原始高度
//            int destWidth = 0;
//            int destHeight = 0;
//            // 缩放的比例
//            double ratio = 0.0;
//            if (srcWidth < w || srcHeight < h) {
//                ratio = 0.0;
//                destWidth = srcWidth;
//                destHeight = srcHeight;
//            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
//                ratio = (double) srcWidth / w;
//                destWidth = w;
//                destHeight = (int) (srcHeight / ratio);
//            } else {
//                ratio = (double) srcHeight / h;
//                destHeight = h;
//                destWidth = (int) (srcWidth / ratio);
//            }
//            BitmapFactory.Options newOpts = new BitmapFactory.Options();
//            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
//            newOpts.inSampleSize = (int) ratio + 1;
//            // inJustDecodeBounds设为false表示把图片读进内存中
//            newOpts.inJustDecodeBounds = false;
//            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
//            newOpts.outHeight = destHeight;
//            newOpts.outWidth = destWidth;
//            // 获取缩放后图片
//            Log.i("w", w + "");
//            Log.i("h", h + "");
//            Log.i("srcWidth", srcWidth + "");
//            Log.i("srcHeight", srcHeight + "");
//            Log.i("destHeight", destHeight + "");
//            Log.i("destWidth", destWidth + "");
//
//            Log.i("ratio", ratio + "");
//
//            return BitmapFactory.decodeFile(path, newOpts);
//        } catch (Exception e) {
//            // TODO: handle exception
//            return null;
//        }
//    }
//
//    /**
//     * 初始化截图区域，并将源图按裁剪框比例缩放
//     *
//     * @param top
//     * @param bitmap
//     */
//    private void initClipView(int top, final Bitmap bitmap) {
//        if (getIntent().getStringExtra("sq") != null) {
//            clipview = new ClipView(ClipPictureActivity.this, 0.42);
//        } else {
//            if (getIntent().getStringExtra("squre") != null) {
//                clipview = new ClipView(ClipPictureActivity.this, 1.00);
//            } else {
//                clipview = new ClipView(ClipPictureActivity.this);
//            }
//        }
//
//
//        clipview.setCustomTopBarHeight(top);
//        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {
//            public void onDrawCompelete() {
//                clipview.removeOnDrawCompleteListener();
//                int clipHeight = clipview.getClipHeight();
//                int clipWidth = clipview.getClipWidth() + 50;
//                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
//                int midY = clipview.getClipTopMargin() + (clipHeight / 2);
//
//                int imageWidth = bitmap.getWidth();
//                int imageHeight = bitmap.getHeight();
//                Log.i("clipHeight", clipHeight + "");
//                Log.i("clipWidth", clipWidth + "");
//                Log.i("midX", midX + "");
//                Log.i("midY", midY + "");
//                Log.i("imageWidth", imageWidth + "");
//                Log.i("imageHeight", imageHeight + "");
//
//                // 按裁剪框求缩放比例
//                float scale = (clipWidth * 1.0f) / imageWidth;
//                if (imageWidth > imageHeight) {
//                    scale = (clipHeight * 1.0f) / imageHeight;
//                }
//                Log.i("scale", scale + "");
//
//                // 起始中心点
//                float imageMidX = imageWidth / 2;
//                float imageMidY = clipview.getCustomTopBarHeight()
//                        + imageHeight * scale / 2;
//                srcPic.setScaleType(ScaleType.MATRIX);
//                Log.i("imageMidX", imageMidX + "");
//                Log.i("imageMidY", imageMidY + "");
//
//                // 缩放
//                matrix.postScale(scale, scale);
//                // 平移
//                matrix.postTranslate(0, midY - imageMidY);
//
//                srcPic.setImageMatrix(matrix);
//                srcPic.setImageBitmap(bitmap);
//            }
//        });
//
//        this.addContentView(clipview, new LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//    }
//
//    public boolean onTouch(View v, MotionEvent event) {
//        ImageView view = (ImageView) v;
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                savedMatrix.set(matrix);
//                // 设置开始点位置
//                start.set(event.getX(), event.getY());
//                mode = DRAG;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                oldDist = spacing(event);
//                if (oldDist > 10f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                mode = NONE;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mode == DRAG) {
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - start.x, event.getY()
//                            - start.y);
//                } else if (mode == ZOOM) {
//                    float newDist = spacing(event);
//                    if (newDist > 10f) {
//                        matrix.set(savedMatrix);
//                        float scale = newDist / oldDist;
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                    }
//                }
//                break;
//        }
//        view.setImageMatrix(matrix);
//        return true;
//    }
//
//    /**
//     * 多点触控时，计算最先放下的两指距离
//     *
//     * @param event
//     * @return
//     */
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    /**
//     * 多点触控时，计算最先放下的两指中心坐标
//     *
//     * @param point
//     * @param event
//     */
//    private void midPoint(PointF point, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.sure:
//                if (clipPhotoList != null) {
//                    getBitmap();
//                    if (nowfile < clipPhotoList.size() - 1) {
//                        matrix.reset();
//                        nowfile++;
//                        bitmap = createBitmap(clipPhotoList.get(nowfile).getSourcePath(), screenWidth, screenHeight);
//                        initClipView(srcPic.getTop(), bitmap);
//                    } else {
//                        Intent intent = getIntent();
//                        int flag = intent.getIntExtra("data", 0);
//
//                        if (flag == 99) {
//                            intent.putExtra("photo", (Serializable) clipPhotoList);
//                        }
//
//                        if (flag == 1) {
//                            AddExchangeGoodsActivity_F.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 2) {
//                            StoreManageActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 3) {
//                            AddAuctionGoodsActivity_f.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 4) {
//                            CreateDiscountEventActivity_f.mPhotoList.clear();
//                            CreateDiscountEventActivity_f.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 5) {
//                            CustomizationReplyActivity_f.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 6) {
//                            NewAddProductionActivity.mPhotoList.clear();
//                            NewAddProductionActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 7) {
//                            SubmitCorporationActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 8) {
//                            ShopSettingActivity.mPhotoList.clear();
//                            ShopSettingActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 9) {
//                            EditDiscountEventActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 10) {
////                            StoreSettingActivity.wmPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 11) {
//                            EditExchangeGoodActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        if (flag == 12) {
//                            UpdateAuctionGoodsActivity.mPhotoList.addAll(clipPhotoList);
//                        }
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
//                break;
//            case R.id.line_cancle:
//                finish();
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    /**
//     * 获取裁剪框内截图
//     *
//     * @return
//     */
//    private Bitmap getBitmap() {
//
//        // 获取截屏
//        View view = this.getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//
//        // 获取状态栏高度.根据activity而定,可以不要
//        Rect frame = new Rect();
//        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//
//        Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
//                clipview.getClipLeftMargin(), clipview.getClipTopMargin(), clipview.getClipWidth(), clipview.getClipHeight());
//
//        savaBitmap(finalBitmap, Environment.getExternalStorageDirectory() + "/DIGO/" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//        // 释放资源
//        view.destroyDrawingCache();
//
//        return finalBitmap;
//    }
//
//    /**
//     * 保存bitmap对象到本地
//     *
//     * @param bitmap
//     */
//    public void savaBitmap(Bitmap bitmap, String filelistpath) {
//        File f = new File(filelistpath);
//        FileOutputStream fOut = null;
//        try {
//            f.createNewFile();
//            fOut = new FileOutputStream(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
//        try {
//            fOut.flush();
//            fOut.close();
//            clipPhotoList.get(nowfile).setSourcePath(filelistpath);
//            clipPhotoList.get(nowfile).setThumbnailPath(filelistpath);
//            Log.i(nowfile + "path", clipPhotoList.get(nowfile).getSourcePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
