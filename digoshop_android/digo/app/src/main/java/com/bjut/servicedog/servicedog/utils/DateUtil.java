package com.bjut.servicedog.servicedog.utils;

import android.annotation.SuppressLint;
import android.util.Log;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    static SimpleDateFormat format;

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String D_YYYY_MM_DD = "yyyy.MM.dd";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * Log输出标识
     **/
    private static final String TAG = DateUtil.class.getSimpleName();

    public DateUtil() {

    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(dateL);
        return sdf.format(date);
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化转成当天凌晨的时间戳
     *
     * @param dateL 日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long formatDateTime2Long(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD);
        Date date = new Date(dateL);
        String dataStr = sdf.format(date);
        return strToDate(dataStr).getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD);
        Date date = new Date(dateL);
        return sdf.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long dateL,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(dateL);
        return sdf.format(date);
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(long dateL, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(new Date(dateL));
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(Date date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(date);
    }

    /**
     * 将字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseDate(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");
        }
        return returnDate;

    }

    /**
     * 将字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseDateYMD(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");

        }
        return returnDate;

    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static Date gainCurrentDate() {
        return new Date();
    }

    /**
     * 验证日期是否比当前日期早
     *
     * @param target1 比较时间1
     * @param target2 比较时间2
     * @return true 则代表target1比target2晚或等于target2，否则比target2早
     */
    public static boolean compareDate(Date target1, Date target2) {
        boolean flag = false;
        try {
            String target1DateTime = formatDateTime(target1,
                    DF_YYYY_MM_DD_HH_MM_SS);
            String target2DateTime = formatDateTime(target2,
                    DF_YYYY_MM_DD_HH_MM_SS);
            if (target1DateTime.compareTo(target2DateTime) <= 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("比较失败，原因：" + e.getMessage());
        }
        return flag;
    }

    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 获取系统时间的方法:月/日 时:分:秒
     */
    public static String getFormateDate() {
        Calendar calendar = Calendar.getInstance();
        int month = (calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String systemTime = (month < 10 ? "0" + month : month) + "/"
                + (day < 10 ? "0" + day : day) + "  "
                + (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute) + ":"
                + (second < 10 ? "0" + second : second);
        return systemTime;
    }

    /**
     * 获取系统时间的方法:时:分:秒
     */
    public static String getHourAndMinute() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 获取系统时间的方法:时
     */
    public static String getHour() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return ((hour < 10 ? "0" + hour : hour) + "");
    }

    /**
     * 将2014-09-10 00:00:00 换2014-09-10
     *
     * @param strDate
     * @return
     */
    public static String strFormatStr(String strDate) {
        if (strDate.equals("")) {
            return "";
        }
        return dateToStr(strToDate(strDate));
    }

    /**
     * 2015-01-07 15:05:34
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDateHHMMSS(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 2015-01-07
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * 2015.01.07
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDateDorp(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 传入一个String转化为long
     */
    @SuppressLint("SimpleDateFormat")
    public static Long stringParserLong(String param) throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(param).getTime();
    }

    /**
     * 传入一个String转化为long
     */
    @SuppressLint("SimpleDateFormat")
    public static Long stringParserLongYMD(String param) throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(param).getTime();
    }

    /**
     * 当前时间转换为long
     */
    @SuppressLint("SimpleDateFormat")
    public static Long currentDateParserLong() throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(format.format(Calendar.getInstance().getTime()))
                .getTime();
    }

    public static Long currentDateParserLongYMD() throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(format.format(Calendar.getInstance().getTime()))
                .getTime();
    }

    /**
     * 当前时间转换为long
     */
    @SuppressLint("SimpleDateFormat")
    public static Long currentLong() throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Calendar.getInstance().getTime().getTime();

    }
    /**
     * 当前时间 如: 2013-04-22 10:37:00
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 当前时间 如: 10:37
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateHHMM() {
        format = new SimpleDateFormat("HH:mm");
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 当前时间 如: 10:37
     *
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateHHMMSS() {
        format = new SimpleDateFormat("HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 当前时间 如: 20130422
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateString() {
        format = new SimpleDateFormat("yyyyMMddHHmm");
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 当前时间 如: 2013-04-22
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(Calendar.getInstance().getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getSWAHDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static Long stringToLongD(String param) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(param.substring(0, param.length() - 4)).getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public static Long stringToLong(String param) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        return format.parse(param).getTime();
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 日期转换成Java字符串
     *
     * @param date
     * @return str
     */
    @SuppressLint("SimpleDateFormat")
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成年
     *
     * @param date
     * @return str
     */
    @SuppressLint("SimpleDateFormat")
    public static String DateToYear(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成月
     *
     * @param date
     * @return str
     */
    @SuppressLint("SimpleDateFormat")
    public static String DateToMouth(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MM");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成日
     *
     * @param date
     * @return str
     */
    @SuppressLint("SimpleDateFormat")
    public static String DateToDay(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    @SuppressLint("SimpleDateFormat")
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    @SuppressLint("SimpleDateFormat")
    public static Date StrToDateDrop(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getLongTime(String time) {
        long ct = 0;
        try {
            format = new SimpleDateFormat("HH:mm:ss");
            ct = format.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ct;
    }

    /**
     * 判断两日期是否同一天
     *
     * @param str1
     * @param str2
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameDay(String str1, String str2) {

        Date day1 = null, day2 = null;
        day1 = DateUtil.strToDate(str1);
        day2 = DateUtil.strToDate(str2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String ds1 = sdf.format(day1);

        String ds2 = sdf.format(day2);

        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取两个日期的时间差
     */
    @SuppressLint("SimpleDateFormat")
    public static int getTimeInterval(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        int interval = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date beginTime = dateFormat.parse(date);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差
            // 单位秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }

    /**
     * 获取两个日期的时间差 yyyy.MM.dd HH.mm.ss
     */
    @SuppressLint("SimpleDateFormat")
    public static int getInterval(String bDate, String eDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        int interval = 0;
        try {
            Date currentTime = dateFormat.parse(eDate);// 获取现在的时间
            Date beginTime = dateFormat.parse(bDate);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()));// 时间差
            // 单位秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }

    /**
     * 两个时间之差 求出一个long Time
     *
     * @param dt1
     * @param dt2
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getTime(String date) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date getdate = df.parse(date);
            diff = getdate.getTime() - currentTime.getTime();

        } catch (Exception e) {
        }
        return diff;
    }

    /**
     * 日期转换成Java字符串
     *
     * @param date
     * @return str
     */
    @SuppressLint("SimpleDateFormat")
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 传入时间 算出星期几
     *
     * @param str  2014年1月3日
     * @param days 1:2014年1月4日 类推
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(String str, int days) {

        String dateStr = "";
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
            Date date = df.parse(str);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date d = dateFormat.parse(dateFormat.format(date));
            c.setTime(d);
            c.add(Calendar.DAY_OF_MONTH, days);
            switch (c.get(Calendar.DAY_OF_WEEK) - 1) {
                case 0:
                    dateStr = "周日";
                    break;
                case 1:
                    dateStr = "周一";
                    break;
                case 2:
                    dateStr = "周二";
                    break;
                case 3:
                    dateStr = "周三";
                    break;
                case 4:
                    dateStr = "周四";
                    break;
                case 5:
                    dateStr = "周五";
                    break;
                case 6:
                    dateStr = "周六";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    /* 将字符串转为时间戳 */
    public static long getStringToDate(String time) {
        format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static boolean isLeapYear(int year) {
        if ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0)))
            return true;
        else
            return false;
    }

}
