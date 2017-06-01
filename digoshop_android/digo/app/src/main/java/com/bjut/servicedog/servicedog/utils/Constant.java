package com.bjut.servicedog.servicedog.utils;


/**
 * Created by Xran on 16/6/3.
 */
public class Constant {

//    public static final int LOAD_ANIMATION = BaseQuickAdapter.ALPHAIN;


    // uid   589d86050cf25e377d3ca2af
    // token   036hbUD+wBxvUM3KuBY1MDS1MD3yUAD8p6GbfjaDQlGxFtdBkgGF+P/yYWk10/StXO2ikMRMJ+CMmtMTMynepQ==
    // ip   192.168.0.102

    public static final int TAKE_PICTURE = 0x000000;
    public static final int ADD_PRODUCT = 0x000002;
    public static final int UPDATE_PRODUCT = 0x000003;
    public static final int ADD_PROMOTION = 0x000004;
    public static final int CHOISE_PRODUCT = 0x000005;
    public static final int INTENT_SCAN = 0x000006;

    public static final boolean PHOTO_TAKE = false;
    public static final boolean PHOTO_CHOISE = true;

    public static final int AUCTION_PRODUCT = 1;
    public static final int EXCHANGE_PRODUCT = 2;
    public static final int PRODUCT = 3;


    public static final int AUTO_TIME = 5000;

    public static final String COUPON_ALL = "-1";
    public static final String COUPON_DAI = "1000000";
    public static final String COUPON_ZHE = "1000001";
    public static final String COUPON_MAN = "1000002";

    public static final String COUPON_STATUS_NO = "1";
    public static final String COUPON_STATUS_YES = "2";
    public static final String COUPON_STATUS_END= "3";


    public static final int LIMIT_NO = 1;
    public static final int LIMIT_YES = 2;

    public static final int PAYMENT_NORMAL = 1;
    public static final int PAYMENT_ZENG = 2;

    public static final int RANGE_ONLY_SHOP = 0;
    public static final int RANGE_ALL_SHOP = 1;

    public static final int TUI_NO = 1;
    public static final int TUI_YES = 2;

    public static final String  TUI_NO_STR = "取消";
    public static final String  TUI_YES_STR = "推荐";


    public static final int PRODUCT_STATUS_AUDIT = 1;//审核中
    public static final int PRODUCT_STATUS_NOT_PASSED = 2;//未通过
    public static final int PRODUCT_STATUS_PASSED = 3;//已通过
    public static final int PRODUCT_STATUS_EXCHANGE = 4;//兑换中
    public static final int PRODUCT_STATUS_SHELVES = 4;//上架中
    public static final int PRODUCT_STATUS_AUCTION_NOTSTART = 4;//竞拍未开始
    //    public static final int PRODUCT_STATUS_OUT_OF_STOCK = 5;//缺货
    public static final int PRODUCT_STATUS_SHELF = 5;//下架
    public static final int PRODUCT_STATUS_EXCHANGE_STOP = 5;//兑换结束
    public static final int PRODUCT_STATUS_AUCTION = 5;//竞拍中
    public static final int PRODUCT_STATUS_AUCTION_STOP = 6;//竞拍结束
    public static final int PRODUCT_STATUS_EXCHANGE_NOTSTART = 8;//兑换未开始
    public static final int PRODUCT_STATUS_EXCHANGE_SHELF = 99;//兑换下架
    public static final int PRODUCT_STATUS_DELETE = 99;//删除

    public static final int PRODUCT_OPERATION_SHELVES = 1;//上架
    public static final int PRODUCT_OPERATION_SHELF = 2;//下架
    public static final int PRODUCT_OPERATION_DELETE = 99;//删除

    public static final int PAGE_SIZE = 15;

    //正式
//    public static String HOST_URL = "http://api.digoshop.com/communityunion/";
//    public static String APPKEY = "561f89ea5004ba3fa95b8a888fddae73";
//    public static String MSG = "msg";
//    public static String CHECK_NETWORK = "网络连接错误";
//    public static String ENDPOINT = "image.digoshop.com";
//    public static String ACCESS_KEYID = "grPghY7hpHuc9PA2";
//    public static String ACCESS_KEYSECRET = "wI0aMyLcP9eZUEPEWJrNmjOZNkbGb8";
//    public static String BUCKET_NAME = "digoimg";
//    public static String ANDROID_UPLOADFILE_NAMEPRE = "android_shop/";

    //测试
    public static String HOST_URL = "http://digo.digoshop.com/communityunion/";
    public static String APPKEY = "561f89ea5004ba3fa95b8a888fddae73";
    public static String MSG = "msg";
    public static String CHECK_NETWORK = "网络连接错误";
    public static String ENDPOINT = "img.digoshop.com";
    public static String ACCESS_KEYID = "grPghY7hpHuc9PA2";
    public static String ACCESS_KEYSECRET = "wI0aMyLcP9eZUEPEWJrNmjOZNkbGb8";
    public static String BUCKET_NAME = "zksxapp";
    public static String ANDROID_UPLOADFILE_NAMEPRE = "android_shop/";


//    public static String HOST_URL = "http://tcapi.digoshop.com/communityunion/";
//    public static String APPKEY = "561f89ea5004ba3fa95b8a888fddae73";
//    public static String MSG = "msg";
//    public static String CHECK_NETWORK = "网络连接错误";
//    //    public static  String ENDPOINT ="http://ANDROID_UPLOADFILE_NAMEPREimg.slw01.com";
//    public static String ENDPOINT = "tcimg.digoshop.com";
//    public static String ACCESS_KEYID = "LTAI5rDtS6aDUXhM";
//    public static String ACCESS_KEYSECRET = "EBDfSMpbZb99KqJJcP3wfWQAlyDe99";
//    public static String BUCKET_NAME = "digoapp";
//    public static String ANDROID_UPLOADFILE_NAMEPRE = "merchant/";


}
