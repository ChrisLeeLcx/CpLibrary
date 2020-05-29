package cn.lee.cplibrary.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.Gravity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.lee.cplibrary.util.dialog.CpComDialog;

/**
 * Created by ChrisLee on 2019/3/12.
 */

public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final String SP_NAME = "sp_notice";
    private static final String KEY = "is_checked";
    //检测 通知状态 是否已打开

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod =
                    appOpsClass.getMethod(
                            CHECK_OP_NO_THROW,
                            Integer.TYPE,
                            Integer.TYPE,
                            String.class
                    );
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);

            return (
                    (int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                            AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转设置页面 去设置通知权限
     *
     * @param context
     */
    public static void toNotificationSetting(Context context) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {//LEE 20
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getApplicationContext().getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
            context.startActivity(intent);
        }
    }


    /**
     * 检测是开启了通知，若未开启，则显示弹框开启
     *
     * @param c
     * @param isShowOnce true:app从安装到卸载只检测一次，false: 每次只要调用方法就检测
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void showNoticeDialog(final Context c, boolean isShowOnce, final DialogNoticeCallBack callBack) {
        if (isShowOnce && isNoticeChecked(c)) {//只检测1次，并且已经检测过，就不再进行
            return;
        }
        boolean b = isNotificationEnabled(c);
        if (b) {
            return;
        }
        CpComDialog.Builder.builder(c)
//                .setTitleSize(24).setContentSize(18).setBtnSize(20)
//                .setWidth(281)
//                .setHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
//                .setPadding(20, 20, 20, 20)
                .setTitle("提示")
                .setContent("当前应用未允许通知。\n请点击\"设置\"-\"通知\"-允许通知。")
                .setCancel(false)
                .setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                .setSure("设置")
                .setTxtCancel("取消")
                .setBtnCancelColor(Color.parseColor("#666666"))
                .build().show2BtnDialog(
                new CpComDialog.Dialog2BtnCallBack() {
                    @Override
                    public void sure() {
                        toNotificationSetting(c);
                        SharedPreferencesUtils
                                .putShareValue(c,
                                        SP_NAME,
                                        KEY,
                                        true);

                        if (callBack != null) {
                            callBack.sure();
                        }
                    }

                    @Override
                    public void cancel() {// 拒绝, 退出应用
                        SharedPreferencesUtils
                                .putShareValue(c,
                                        SP_NAME,
                                        KEY,
                                        true);
                        if (callBack != null) {
                            callBack.cancel();
                        }
                    }
                });
    }


    /**
     * app从安装到卸载的一生中是否检测过通知
     */
    public static boolean isNoticeChecked(Context c) {
        boolean isFirst = SharedPreferencesUtils.getShareBoolean(
                c, SP_NAME,
                KEY, false
        );
        return isFirst;
    }

    public interface DialogNoticeCallBack {
        void sure();

        void cancel();
    }
}
