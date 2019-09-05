package cn.lee.cplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * function:屏幕工具辅助类
 *
 * @author ChrisLee
 * @date 2018/5/25
 */

public class ScreenUtil {

    private ScreenUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static int dp2px(Context context, float dpVal) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                spValue, context.getResources().getDisplayMetrics());
    }
    public static int sp(Context context,float spValue) {
        return  (int)spValue;
    }
    /**
     * 获取view以整个页面为参照,距离页面最上端的距离.
     */
    public static int getLocationOnScreenY(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position[1];
    }

    /**
     * 获取view以整个页面为参照,距离页面最左端的距离.
     */
    public static int getLocationOnScreenX(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position[0];
    }

    /**
     * 获取view以手机屏幕为参照 ,距离屏幕最左端的距离.
     */
    public static int getLocationInWindowX(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        return position[0];
    }

    /**
     * 获取view以手机屏幕为参照 ,距离屏幕最上端的距离.
     */
    public static int getLocationInWindowY(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        return position[1];
    }

    /**
     * 顺时针旋转
     *
     * @param view：旋转的控件
     * @param rotation:旋转角度
     */
    public static void rotate(Activity activity, View view, float rotation) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        // 获取屏幕的尺寸
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 屏幕宽
        int width = displayMetrics.widthPixels;
        // 屏幕高
        int height = displayMetrics.heightPixels;
        // 设置布局的宽和高,必须要和屏幕的反过来
        if (view.getParent() instanceof LinearLayout) {
            view.setLayoutParams(new LinearLayout.LayoutParams(height, width));
        } else if (view.getParent() instanceof RelativeLayout) {
            view.setLayoutParams(new RelativeLayout.LayoutParams(height, width));
        } else if (view.getParent() instanceof FrameLayout) {
            view.setLayoutParams(new FrameLayout.LayoutParams(height, width));
        } else {
            throw new IllegalArgumentException("view 的父布局必须是LinearLayout、RelativeLayout或FrameLayout其中之一");
        }
        // 顺时针旋转90度
        view.setRotation(rotation);
        // 将布局位移到屏幕中心
        view.setY((height - width) / 2);
        view.setX((width - height) / 2);
    }
    /**
     * 设置Activity竖屏
     */
    public static void setActivityPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 变为竖屏显示
    }

    /**
     * 设置Activity横屏
     */
    public static void setActivityLandscape(Activity activity) {
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {// 无法进行画面的旋转
            return;
        } else {
            if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) { // 如果为竖屏显示
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 变为横屏显示
            }
        }
    }

}
