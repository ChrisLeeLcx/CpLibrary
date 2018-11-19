package com.lee.cplibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseApplication;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import cn.lee.cplibrary.util.DrawableUtil;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author: ChrisLee
 * @time: 2018/7/26
 */

public class BitmapUtils {
    private static int imgDefault= R.mipmap.ic_launcher;
    private static int imgError=R.mipmap.ic_launcher;
    /**
     * @param url        图片的url地址
     * @param imgDefault 默认占位图片
     */
    private static DrawableRequestBuilder<String> getDefaultDrawableBuilder(Context context, String url, int imgDefault) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                //防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .dontAnimate()
                //加载失败的图片
                .error(imgError)
                //加载中的占位符
                .placeholder(imgDefault)
                /**
                 * all:缓存源资源和转换后的资源
                 none:不作任何磁盘缓存
                 source:缓存源资源
                 result：缓存转换后的资源  //默认模式
                 */
                //硬盘缓存策略
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                // 用原图的1/10作为缩略图
                .thumbnail(0.1f);
        return builder;
    }

    public static void displayImage(Context context, File file, ImageView view) {
        Glide.with(context).load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(view);
    }

    /**
     * 加载图片默认模式，使用全局的Context
     */
    public static void displayImageFromUrl(String url, ImageView ivContent) {
        displayImageFromUrl(url, ivContent,imgDefault);
    }

    /**
     * 加载图片默认模式，使用全局的Context
     */
    public static void displayImageFromUrl(String url, ImageView ivContent, int imgDefault) {
        displayImageFromUrl(BaseApplication.getContext(), url, ivContent, imgDefault);
    }


    public static void displayImageFromUrl(Context context, String url, ImageView ivContent, int imgDefault) {
        displayImageFromUrlRound(context, url, ivContent, imgDefault, 0);
    }
    public static void displayImageFromUrl(Context context, String url, ImageView ivContent) {
        displayImageFromUrlRound(context, url, ivContent, imgDefault, 0);
    }
    /**
     * @param ivContent 需要展示的ImageView
     * @param round     图片四个角的弧度
     */
    public static void displayImageFromUrlRound(Context context, String url, ImageView ivContent, int imgDefault, int round) {
        if (ObjectUtils.isEmpty(url)) {
            return;
        }
        getDefaultDrawableBuilder(context, url, imgDefault).bitmapTransform(
                new RoundedCornersTransformation(context, round, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(ivContent);
    }

    /**
     * 加载网络圆形图标
     *
     * @param ivContent 需要展示的ImageView
     */
    public static void displayImageFromUrlCircle(Context context, String url, ImageView ivContent, int imgDefault) {
        if (ObjectUtils.isEmpty(url)) {
            return;
        }
        getDefaultDrawableBuilder(context, url, imgDefault).bitmapTransform(
                new CropCircleTransformation(context))
                .into(ivContent);
    }


    /**
     * 加载本地圆形图标
     *
     * @param res :Integer resourceId
     */
    public static void displayImageFromNativeCircle(Context context, ImageView ivContent, int res, int imgDefault) {
        Glide.with(context)
                .load(res)
                .error(imgDefault)
                .placeholder(imgDefault)
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .thumbnail(0.1f)
                .into(ivContent);

    }


    /**
     * 加载图片使用自定义Context，并且定义加载优先级
     *
     * @param context   当前的Activity
     * @param url       图片的url地址
     * @param ivContent 需要展示的ImageView
     * @param priority  加载的优先级
     */
    public static void displayImageFromUrl(Context context, String url, ImageView ivContent, int imgDefault, Priority priority) {
        if (ObjectUtils.isEmpty(url)) {
            return;
        }
        getDefaultDrawableBuilder(context, url, imgDefault)
                .priority(priority)
                .into(ivContent);
    }

    public static void displayImageFromUrlNoScaleType(Context context, String url, ImageView ivContent, int imgDefault) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .error(imgDefault)
                .placeholder(imgDefault)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.1f).into(ivContent);
    }


    /**
     * 在图片下载缓存好之后获取，从网路缓存的中获取Bitmap------leeb---未测试
     *
     * @param widthDp         :图片的宽 单位：dp ，若widthDp和heightDp 均大于0，则设置相应的宽和高，否则默认原图的宽高
     * @param heightDp:图片的高
     * @param listener:Bitmap 返回成功的监听
     */
    public static void getBitmapFromCatch(Context context, String url, int widthDp, int heightDp, final OnBitmapObtainListener listener) {
        if (widthDp > 0 && heightDp > 0) {
            //  方式1：在图片下载缓存好之后获取， 简单方法
            Glide.with(context).load(url).asBitmap().override(ScreenUtil.dp2px(context, widthDp), ScreenUtil.dp2px(context, heightDp)).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    LogUtil.e("", "onResourceReady");
                    listener.onBitmapObtain(resource, glideAnimation);
                }
            }); //方法中设置<span style="font-family: Arial, Helvetica, sans-serif;">asBitmap可以设置回调类型</span>
        } else {
            //  方式1：在图片下载缓存好之后获取， 简单方法
            Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    listener.onBitmapObtain(resource, glideAnimation);
                }
            }); //方法中设置<span style="font-family: Arial, Helvetica, sans-serif;">asBitmap可以设置回调类型</span>
        }


//    方式2： 在图片下载缓存好之后获取   更全面的方法，可以完美控制
//        Glide.with(context).load(url).asBitmap().into(new Target<Bitmap>() {
//            @Override
//            public void onLoadStarted(Drawable placeholder) {
//
//            }
//
//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                //TODO set bitmap
//                listener.onBitmapObtain(resource, glideAnimation);
//            }
//
//            @Override
//            public void onLoadCleared(Drawable placeholder) {
//
//            }
//
//            @Override
//            public void getSize(SizeReadyCallback cb) {
//
//            }
//
//            @Override
//            public void setRequest(Request request) {
//
//            }
//
//            @Override
//            public Request getRequest() {
//                return null;
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onStop() {
//
//            }
//
//            @Override
//            public void onDestroy() {
//
//            }
//        });

    }


    public static void getDrawableFromUrlCatch(Context context, String url, int widthDp, int heightDp, final OnDrawableObtainListener listener) {
        getBitmapFromCatch(context, url, widthDp, heightDp, new OnBitmapObtainListener() {
            @Override
            public void onBitmapObtain(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                listener.onDrawableObtain(DrawableUtil.getDrawableFromBitmap(resource));
            }
        });
    }

    public interface OnBitmapObtainListener {
        void onBitmapObtain(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation);
    }

    public interface OnDrawableObtainListener {
        void onDrawableObtain(Drawable dawable);
    }


    /**
     * 从网路Url中获取Bitmap ------未测试
     */
    public static Bitmap getBitmapFromUrl(Context context, String url) {
//        方式3：通过url获取
        Bitmap myBitmap = null;
        try {
            myBitmap = Glide.with(context)
                    .load(url)
                    .asBitmap() //必须
                    .centerCrop()
                    .into(500, 500)
                    .get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    /**
     * 应用于SplashActivity 加载本地大图出现OOM情况
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
    /**
     * 缩放图像
     现在，对于任何图像操作，调整大小真的能让长宽比失真并且丑化图像显示。在你大多数的使用场景中，你想要避免发生这种情况。
     Glide 提供了一般变化去处理图像显示。提供了两个标准选项：centerCrop 和 fitCenter。
     CenterCrop
     CenterCrop()是一个裁剪技术，即缩放图像让它填充到 ImageView 界限内并且侧键额外的部分。ImageView

     FitCenter
     fitCenter() 是裁剪技术，即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。该图像将会完全显示，但可能不会填满整个 ImageView。

     Priority (优先级)枚举
     这个枚举给了四个不同的选项，下面是按照递增priority(优先级)的列表：
     Priority.LOW
     Priority.NORMAL
     Priority.HIGH
     Priority.IMMEDIATE
     */

    //加載方式
    /**
     * load SD卡资源：load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg")
     *load assets资源：load("file:///android_asset/f003.gif")
     *load raw资源：load("android.resource://com.frank.glide/raw/raw_1")或load("android.resource://com.frank.glide/raw/"+R.raw.raw_1)
     *load drawable资源：load("android.resource://com.frank.glide/drawable/news")或load("android.resource://com.frank.glide/drawable/"+R.drawable.news)
     *load ContentProvider资源：load("content://media/external/images/media/139469")
     *load http资源：load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg")
     *load https资源：load("https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp")
     */


}
