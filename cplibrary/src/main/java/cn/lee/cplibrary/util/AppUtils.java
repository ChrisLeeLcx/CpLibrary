package cn.lee.cplibrary.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import cn.lee.cplibrary.util.dialog.CpComDialog;

/**
 * APP工具类
 *
 * @author ChrisLee
 */
public class AppUtils {
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // 当前应用的版本名称
        return versionCode;
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // 当前应用的版本名称
        return versionName;

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getScale(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        TelephonyManager mTm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTm;
    }

    @SuppressLint("MissingPermission")
    public static String getImsi(Context context) {
        return getTelephonyManager(context).getSubscriberId();
    }

    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        return getTelephonyManager(context).getDeviceId();
    }

    @SuppressLint("MissingPermission")
    public static String getImsiPhone(Context context) {
        return getTelephonyManager(context).getLine1Number();
    }

    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getMacAddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }

    public static String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获得系统总内存
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
            }

            initial_memory = Integer.valueOf(arrayOfString[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 手机CPU信息
     */
    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""}; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    /**
     * 获取 applicationId
     */
    public static String getAppId(Context context) {
        //return context.getApplicationInfo().processName;
        return context.getApplicationInfo().packageName;
    }

    /**
     * 获取 applicationId
     */
    public static String getAppId(Activity context) {
        return context.getApplication().getPackageName();
    }

    /**
     * @fun： 关闭软键盘
     * @author: ChrisLee at 2017-4-21 下午3:56:02
     */
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    /**
     * 跳转到设置中心->项目应用信息界面（可以设置应用通知管理、应用权限、清楚缓存等）
     *
     * @param activity
     */
    public static void jumpAppSettingInfo(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", AppUtils.getAppId(activity), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    public static void jumpAppSettingInfo(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param activity
     * @param phoneNo
     */
    public static void callPhone(final @NonNull Activity activity, String phoneNo) {
        if (ObjectUtils.isEmpty(phoneNo)) {
            ToastUtil.showToast(activity, "您拨打的是空号");
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            CpComDialog.Builder.builder(activity).
                    setTitle("提示").setContent("是否开启拨打电话权限").setTxtCancel("取消").setSure("开启")
                    .setCancel(false)
                    .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
                @Override
                public void sure() {
                    jumpAppSettingInfo(activity);
                }

                @Override
                public void cancel() {
                }
            });
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        activity.startActivity(intent);
    }

    /**
     * 跳转到系统功能界面
     **/
    public static void jumpSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 跳转到Wifi
     **/
    public static void jumpWifi(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 跳转到移动网络设置界面
     **/
    public static void jumpMobileNet(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 通过 object获取Activity
     *
     * @param object
     */
    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        } else {
            throw new IllegalArgumentException("not supported!" + object.getClass().getName() + "is not a activity or fragment");
        }
    }

    /**
     * 判断定位服务是否开启
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
    /**
     * 判断定位服务是否开启
     */
    public static boolean checkLocationEnabled(Context context) {
        // 考虑到大部分App可能还未target到API 28，这里使用反射调用API 28开始提供的检查定位开关的方法
        if (Build.VERSION.SDK_INT >= 28) {//>=28
            boolean isLocationEnabled = true;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            try {
                Method isLocationEnabledMethod = locationManager.getClass().getMethod("isLocationEnabled", new Class[]{});
                isLocationEnabled = (boolean) isLocationEnabledMethod.invoke(locationManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isLocationEnabled;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//>=19~28
            int locationMode = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
            boolean isLocationEnabled = locationMode != Settings.Secure.LOCATION_MODE_OFF;
            return isLocationEnabled;
        } else {// <9
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            List<String> allProviders = locationManager.getAllProviders();
            if (allProviders == null || allProviders.isEmpty()) {
                return false;
            }
            String usableProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            boolean isLocationEnabled = !usableProviders.isEmpty();
            return isLocationEnabled;
        }

    }

    //---------------覆盖安装相应方法--------------------
    private static int old_verCode;//之前保存的VersionCode,
    private static final String SP_NAME = "sp_verCode";
    private static final String KEY = "verCode";

    /**
     * 1、是否是覆盖安装，调用后配合saveVerCodeToSp使用才起作用
     * 一般若方法返回true，则要在相应的进入页面的地方调用保存当前vercode到sp中
     * 2、覆盖安装功能一般在过度页面检测
     * 3、只能检测与当前版本数不同的覆盖安装
     **/
    public static boolean isCoverInstall(Context c) {
        old_verCode = SharedPreferencesUtils.getShareInteger(c, SP_NAME, KEY);
        int curVerCode = getVersionCode(c);
        boolean isCover = true;
        if (old_verCode == 0) {//首次安装，或者卸载后安装
            isCover = false;
        } else if (curVerCode == old_verCode) {//当前版本：（1）安装后版本数一致（2）未安装
            isCover = false;
        }
        return isCover;
    }

    /**
     * 将当前版本数保存到sp里面
     **/
    public static void saveVerCodeToSp(Context c) {
        SharedPreferencesUtils.putShareValue(c, SP_NAME, KEY, getVersionCode(c));
    }
}
