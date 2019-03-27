package cn.lee.cplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.widget.TextView;

import cn.lee.cplibrary.R;

/**
 * 注意：全部是实心无边框图形（因为有边框 有背景的未能实现）
 * 获取椭圆形、矩形、扇形、扇面形状...等drawable的工具类
 *
 * @author: ChrisLee
 * @time: 2018/7/18
 */

public class DrawableUtil {

    /**
     * @param backgroundColor：背景颜色
     * @return :实心无边框椭圆的drawable
     */
    public static Drawable createSolidOvalDrawable(@ColorInt int backgroundColor) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        drawable.getPaint().setColor(backgroundColor);
        drawable.getPaint().setStyle(Paint.Style.FILL);
        return drawable;
    }

    /**
     * @param backgroundColor：背景颜色
     * @return: 实心无边框矩形的drawable
     */
    public static Drawable createSolidRectDrawable(@ColorInt int backgroundColor) {
        //矩形形状
        RectShape rectShape = new RectShape();
        ShapeDrawable drawable = new ShapeDrawable(rectShape);
        drawable.getPaint().setColor(backgroundColor);
        drawable.getPaint().setStyle(Paint.Style.FILL);
        return drawable;
    }

    /**
     * @param backgroundColor：背景颜色
     * @param radiusDp：矩形四个圆角的角度：in dp
     * @return 实心无边框矩形的drawable，有弧度
     */
    public static Drawable createSolidRectDrawable(Context context, @ColorInt int backgroundColor, float radiusDp) {
        //一个继承自ShapeDrawable更为通用、可以直接使用的形状
        PaintDrawable drawable = new PaintDrawable(backgroundColor);
        drawable.setCornerRadius(ScreenUtil.dp2px(context, radiusDp));
        return drawable;
    }

    /**
     * @param backgroundColor：背景颜色
     * @param topLeftRadiusDp      ：左上角角度，单位 dp
     * @return 实心无边框矩形的drawable，有弧度 ，可设置4个角
     */
    public static Drawable createSolidRectDrawable(Context context, @ColorInt int backgroundColor, float topLeftRadiusDp, float topRightRadiusDp, float bottomRightRadiusDp, float bottomLeftRadiusDp) {
        PaintDrawable drawable = new PaintDrawable(backgroundColor);
        float[] radius = new float[]{ScreenUtil.dp2px(context, topLeftRadiusDp), ScreenUtil.dp2px(context, topLeftRadiusDp)
                , ScreenUtil.dp2px(context, topRightRadiusDp), ScreenUtil.dp2px(context, topRightRadiusDp),
                ScreenUtil.dp2px(context, bottomRightRadiusDp), ScreenUtil.dp2px(context, bottomRightRadiusDp),
                ScreenUtil.dp2px(context, bottomLeftRadiusDp), ScreenUtil.dp2px(context, bottomLeftRadiusDp)};
        drawable.setCornerRadii(radius);
        return drawable;
    }


    /**
     * @param strokeColor：边框颜色
     * @param radiusDp：矩形四个圆角的角度：in      dp
     * @param strokeWidthDp：矩形四个圆角的角度：in dp
     * @return 透明背景有边框矩形的drawable
     */
    private Drawable createStrokeRectDrawable(Context context, @ColorInt int strokeColor, float radiusDp, int strokeWidthDp) {
        PaintDrawable drawable = new PaintDrawable();
        drawable.getPaint().setColor(strokeColor);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setStrokeWidth(ScreenUtil.dp2px(context, strokeWidthDp));
        drawable.setCornerRadius(ScreenUtil.dp2px(context, radiusDp));
        return drawable;
    }

    /**
     * @param strokeColor：边框颜色
     * @param radiusDp：矩形四个圆角的角度 in dp
     * @return 透明背景有边框矩形的drawable，默认边框宽度 2dp
     */
    public static Drawable createStrokeRectDrawable(Context context, @ColorInt int strokeColor, float radiusDp) {
        PaintDrawable drawable = new PaintDrawable();
        drawable.getPaint().setColor(strokeColor);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setStrokeWidth(ScreenUtil.dp2px(context, 2));
        drawable.setCornerRadius(ScreenUtil.dp2px(context, radiusDp));
        return drawable;
    }

    /**
     * @param backgroundColor：背景颜色
     * @param startAngle:开始位置角度（0~360）
     * @param sweepAngle：扇形弧度跨度值（0~360）
     * @return: 扇形drawable
     */
    public static Drawable createSolidArcDrawable(@ColorInt int backgroundColor, float startAngle, float sweepAngle) {
        //扇形、扇面形状 顺时针,开始角度30， 扫描的弧度跨度180
        ArcShape arcShape = new ArcShape(startAngle, sweepAngle);
        ShapeDrawable drawable = new ShapeDrawable(arcShape);
        drawable.getPaint().setColor(backgroundColor);
        drawable.getPaint().setStyle(Paint.Style.FILL);
        return drawable;
    }

    /**
     * 方向：从左下到右上
     */
    public static Drawable createGradientDrawable(Context context, @ColorInt int startColor, @ColorInt int endColor, float radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(ScreenUtil.dp2px(context, radiusDp));
//        drawable.setStroke(2, Color.BLUE);
        drawable.setOrientation(GradientDrawable.Orientation.BL_TR);
        drawable.setColors(new int[]{startColor, endColor});
        return drawable;
    }

    public static Drawable getDrawableFromBitmap(Bitmap bitmap) {
        return new BitmapDrawable(null, bitmap);
    }

    public static Bitmap getBitmapFromResource(Activity activity, int resId) {
        return BitmapFactory.decodeResource(activity.getResources(), resId);
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setDrawableRight(Activity activity, @DrawableRes int id, TextView view, int pad_dp) {
        Drawable drawable = activity.getResources().getDrawable(id);
        setDrawableRight(activity, drawable, view, pad_dp);
    }

    public static void setDrawableRight(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }

    public static void setDrawableLeft(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }

    /**
     * @param pad_dp:单位是dp
     */
    public static void setDrawableTop(Activity activity, @DrawableRes int id, TextView view, int pad_dp) {
        Drawable drawable = activity.getResources().getDrawable(id);
        setDrawableTop(activity, drawable, view, pad_dp);
    }

    /**
     * @param pad_dp:单位是dp
     */
    public static void setDrawableTop(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }
}
