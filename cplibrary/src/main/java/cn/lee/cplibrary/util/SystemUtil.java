package cn.lee.cplibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.lee.cplibrary.util.dialog.CpComDialog;

/**
 * function:系统工具类
 *
 * @author ChrisLee
 * @date 2018/7/12
 */
public class SystemUtil {
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
     * @param activity
     */
    public static void jumpAppSettingInfo(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", AppUtils.getAppId(activity), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * 打电话
     * @param activity
     * @param phoneNo
     */
    public static void callPhone(final @NonNull Activity activity, String phoneNo ) {
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
                    SystemUtil.jumpAppSettingInfo(activity);
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
}
