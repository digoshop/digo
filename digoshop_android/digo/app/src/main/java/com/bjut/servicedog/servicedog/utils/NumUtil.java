package com.bjut.servicedog.servicedog.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by beibeizhu on 16/11/10.
 */

public class NumUtil {
    public static String NumberFormat(float f, int m) {
        return String.format("%." + m + "f", f);
    }

    public static float NumberFormatFloat(float f, int m) {
        String strfloat = NumberFormat(f, m);
        return Float.parseFloat(strfloat);
    }

    public static String Number2Format(float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static String Number2Double(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static String getFloatNum(float num) {
        NumberFormat nf = new DecimalFormat("#.##");
        return nf.format(num);
    }


}
