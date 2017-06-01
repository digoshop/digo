package com.bjut.servicedog.servicedog.utils;

import static com.bjut.servicedog.servicedog.ui.store.MerchantCorporationActivity.isMatch;

/**
 * Created by beibeizhu on 17/3/29.
 * <p>
 * 正则工具类
 */

public class RegularUtils {

    /**
     * 正则表达式:验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }


    public static boolean isUsername(CharSequence input) {
        return isMatch("(?![a-z]+$|[0-9]+$|_+$)^[a-zA-Z0-9_]{6,20}$", input);
    }

    /**
     * 正则表达式:验证密码(不包含特殊字符，6-16长度)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
}
