package cn.lee.cplibrary.util.timer;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;


/**
 * @author ChrisLee
 */
public class TimeUtils {
    public static long beginTime;
    public static long endTime;

    public static final String STAT_FORMAT = "yyyy-MM-dd HH";
    public static final String TIME_FORMAT = "H:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DOT= "yyyy.MM.dd";
    public static final String DATE_FORMAT_CH = "yyyy年M月d日";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT_DOT= "yyyy.MM.dd HH:mm:ss";
    //纯数字样式
    public static final String DATETIME_NUM_FORMAT= "yyyyMMddHHmmssSSS";
    public static final String AM_PM_TIME_FORMAT = "HH:mm";
    public static final int TODAY = 0; // 今天
    public static final int TOMORROW = 1;// 明天
    public static final int OTHER_DAY = -1;// 其他日期

    public static long beginTime() {
        beginTime = System.currentTimeMillis();
        return beginTime;
    }

    public static long endTime() {
        endTime = System.currentTimeMillis();
        return endTime;
    }

    /**
     * 返回耗时：使用之前需要先调用：TimeUtils.beginTime()和TimeUtils.endTime()方法
     */
    public static long subTime() {
       LogUtil.d("", TimeUtils.class, "subTime:" + (endTime - beginTime));
        return endTime - beginTime;
    }
    /*** 获取系统当前时间*/
    public static long getCurTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取系统当前时间
     * @return ：返回格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * 获取系统当前时间
     * @param format eg：yyyy-MM-dd HH:mm:ss 可以为Null或者""
     */
    public static String getCurTime(String format) {
        format = ObjectUtils.isEmpty(format) ? DATETIME_FORMAT : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
    /**
     * function:获取日期time时间后一天
     * time:类型有String eg：yyyy-MM-dd HH:mm:ss 或者long类型的time
     * @author: ChrisLee at 2016-7-19
     */
    public static String getAfterDay(Object time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "";
        try {
            Date date=null ;
            if (time instanceof  String) {
                date  = formatter.parse(String.valueOf(time));
            } if (time instanceof  Long) {
                date = new Date((long)time);
            } else {
                date  = formatter.parse(String.valueOf(time));
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
    /**
     * 获取今天日期
     */
    public static String getToday() {
        return formatTime(DATE_FORMAT,getCurTimeMillis());
    }
    /**
     * function:获取明天时间
     * @return：格式yyyy-MM-dd
     * @author: ChrisLee at 2016-7-19
     */
    public static String getTomorrowDay() {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }
    /**
     * 格式化时间：
     * @param format eg：yyyy-MM-dd HH:mm:ss可以为Null或者""
     * @param time   :the number of milliseconds since Jan. 1, 1970 GMT.
     */
    public static String formatTime(String format, long time) {
        format = ObjectUtils.isEmpty(format) ? DATETIME_FORMAT : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
    /**
     * 格式化时间
     * @param format eg：yyyy-MM-dd HH:mm:ss可以为Null或者""
     * @param time：被格式化的时间
     */
    public static String formatTime(String format,String time) {
        format = ObjectUtils.isEmpty(format) ? DATETIME_FORMAT : format;
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        try {
            return myFormatter.format(new Date(Long.valueOf(time)));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化时间
     * @param format eg：yyyy-MM-dd HH:mm:ss可以为Null或者""
     * @param time：被格式化的时间
     */
    public static Date formatTime2Date(String format,String time) {
        format = ObjectUtils.isEmpty(format) ? DATETIME_FORMAT : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        if(!ObjectUtils.isEmpty(time)){
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
    /**
     * 格式化时间：为格林尼治时间为标准，（比标准北京时间早8h）
     * @author: ChrisLee at 2016-7-19
     */
    public static String formatTimeZone(String format, long time) {
        format = ObjectUtils.isEmpty(format) ? DATETIME_FORMAT : format;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(time);
        return hms;
    }



    /**
     * @fun： 将时间精确到分钟：
     * @param time eg：格式2017-04-21 13:24:51.0 或2017年04月21 13:24:51.0
     * @return:2017-04-21 13:24
     */
    public static String formatTime2Min(String time) {
        if (!TextUtils.isEmpty(time)) {
            if (pattern(time,":")>1) {//包含秒
                String subStr = (String) time.subSequence(0, time.lastIndexOf(':'));
                return subStr;
            }
        }
        return time;
    }
    /**
     * function:获取两个时间差 laterTime-frontTime
     *
     * @param frontTime
     * @param laterTime
     * @return : eg:3天2.5小时
     */
    public static String getDifTime(long frontTime, long laterTime) throws ParseException {
        int time = Integer.parseInt(String.valueOf(laterTime - frontTime));
        String day = String.valueOf(time / 1000 / 60 / 60 / 24);
        String hour = String.format("%.1f", time / 1000 / 60 / 60 % 24.0);
        return day + "天" + hour + "小时";
    }
    /**
     * function:获取两个时间差（毫秒值）
     * @author: ChrisLee at 2016-7-19
     */
    public static long getTimeDif(String frontTime, String laterTime)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(frontTime);
        Date date2 = sdf.parse(laterTime);
        long difference = date2.getTime() - date1.getTime();
        return Math.abs(difference);
    }
    /**获取两个时间差 laterTime-frontTime
     * @param frontTime
     * @param laterTime
     * @return ：两个时间之前的小时差值
     * @throws ParseException
     */
    public static double getDifHour(long frontTime, long laterTime) throws ParseException {
        int time = Integer.parseInt(String.valueOf(laterTime - frontTime));
        double hour = time * 0.1f / 1000 / 60 / 60;
        return Math.abs(hour);
    }
    /**
     * 将从0开始的毫秒数转换为hh:mm:ss格式 如果没有hh那么转换为 mm:ss
     *
     * @param milliseconds
     * @return
     */
    public static String parseZeroBaseMilliseconds(int milliseconds) {
        int hh = milliseconds / 1000 / 60 / 60;
        int mm = (milliseconds - (hh * 60 * 60 * 1000)) / 1000 / 60;
        int ss = (milliseconds - (mm * 1000 * 60) - (hh * 60 * 60 * 1000)) / 1000;
        if (hh != 0) {
            return String.format("%02d:%02d:%02d", hh, mm, ss);
        }
        return String.format("%02d:%02d", mm, ss);
    }

    /**
     * function: 在标准北京时间下，以格式H:mm:ss，返回给定的时间
     * @param second
     *            ：给定的时间
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static String parseDuration(int second) {
        if (second < 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        // 时区时间：GMT是格林尼治标准时间，GMT +08:00是标准北京时间
        format.setTimeZone(TimeZone.getTimeZone("GMT +08:00, GMT +0800"));
        return format.format(new Date(second));
    }

    /**
     * 通过正则表达式的方式获取字符串中指定字符的个数
     * @param text  指定的字符串
     * @return  指定字符的个数
     */
    private static  int pattern(String text,String compile) {
        // 根据指定的字符构建正则
        Pattern pattern = Pattern.compile(compile);
        // 构建字符串和正则的匹配
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        // 循环依次往下匹配
        while (matcher.find()){ // 如果匹配,则数量+1
            count++;
        }
        return  count;
    }


    /**
     * 两次点击间隔不能少于1000ms
     */
    private static final int MIN_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 时间差，判断重复点击
     *
     * @return 为false时，正常逻辑实现。true时,说明点击过快，自定义处理
     */
    public static boolean isFastClick() {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) <= MIN_DELAY_TIME) {
            return true;
        }
        lastClickTime = currentClickTime;
        return false;
    }
}
