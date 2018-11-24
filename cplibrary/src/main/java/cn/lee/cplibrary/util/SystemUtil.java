package cn.lee.cplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
     * 防止EditText输入时，软键盘将下面布局顶上去
     * @param activity activity上下文
     * @param et 对应EditText
     */
    public static void setEtFocusChangeSystemFit(final Activity activity, EditText...et) {
        for (int i=0;i<et.length;i++){
            et[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    }
                }
            });
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
}
