package cn.lee.cplibrary.util;

import android.text.TextUtils;

import cn.lee.cplibrary.constant.CpConfig;


/**
 * @author ChrisLee
 * @desc:打印Log工具
 * @time 2017-4-6 上午10:19:23
 */
public class LogUtil {
    public static final String TAG = "debuginfo";
    private static boolean isDebug = false;

    public static void d(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.d(getTag(tag), msg);
        }
    }

    public static void d(String tag, Object obj, String msg) {
        if (isDebug()) {
            android.util.Log.d(getTag(tag), getObjName(obj) + msg);
        }
    }

    /**
     * @param tag：值可为字符串、null或者"",当为null或""时，tag为本类中默认值即TAG的值。
     * @param msg
     * @fun：
     * @author: ChrisLee at 2017-4-6 上午10:52:09
     */
    public static void i(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.i(getTag(tag), msg);
        }
    }

    /**
     * @param tag ：值可为字符串、null或者"",当为null或""时，tag为本类中默认值即TAG的值。
     * @param obj ：代表某个页面或者某个类,传入对象、null、obj.class，但是null没有意义,
     * @param msg
     * @fun：
     * @author: ChrisLee at 2017-4-6 上午10:52:34
     */
    public static void i(String tag, Object obj, String msg) {
        if (isDebug()) {
            android.util.Log.i(getTag(tag), getObjName(obj) + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.w(getTag(tag), msg);
        }
    }

    public static void w(String tag, Object obj, String msg) {
        if (isDebug()) {
            android.util.Log.w(getTag(tag), getObjName(obj) + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.e(getTag(tag), msg);
        }
    }

    public static void e(String tag, Object obj, String msg) {
        if (isDebug()) {
            android.util.Log.e(getTag(tag), getObjName(obj) + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug()) {
            android.util.Log.v(getTag(tag), msg);
        }
    }

    public static void v(String tag, Object obj, String msg) {
        if (isDebug()) {
            android.util.Log.v(getTag(tag), getObjName(obj) + msg);
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static boolean isIsDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    private static String getTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        return tag;
    }


    /**
     * @param obj：类的对象或者Class对象，eg：obj=new Object(),或者obj=Object.class
     * @return
     * @fun：
     * @author: ChrisLee at 2017-4-17 上午11:16:02
     */
    private static String getObjName(Object obj) {
        String objName = "";
        if (obj != null) {
            if (obj instanceof Class) {
                objName = ((Class) obj).getSimpleName();
            } else {
                objName = obj.getClass().getSimpleName();
            }
        }
        return objName + ":";
    }

}
