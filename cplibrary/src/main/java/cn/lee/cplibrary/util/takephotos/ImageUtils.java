package cn.lee.cplibrary.util.takephotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.LogUtil;

/**
 * Created by gaolei on 2017/12/19.
 */
public class ImageUtils {
    private static float desWidth = 500f;//图片压缩后宽高
    private static int quality = 200;//图片压缩后质量，单位KB

    /**
     * 按尺寸压缩图片
     *
     * @param srcPath  图片路径
     * @param desWidth 压缩的图片宽度
     * @return Bitmap 对象
     */

//    public static Bitmap compressImageFromFile(String srcPath, float desWidth) {
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds = true;//只读边,不读内容
//        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        float desHeight = desWidth * h / w;
//        int be = 1;
//        if (w > h && w > desWidth) {
//            be = (int) (newOpts.outWidth / desWidth);
//        } else if (w < h && h > desHeight) {
//            be = (int) (newOpts.outHeight / desHeight);
//        }
//        if (be <= 0) {
//            be = 1;
//        }
//        newOpts.inSampleSize = be;//设置采样率
//
//        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
//        newOpts.inPurgeable = true;// 同时设置才会有效
//        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
//
//        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        return bitmap;
//    }
    public static Bitmap compressImageFromFile(String srcPath, float desWidth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        LogUtil.i("","3="+bitmap);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float desHeight = desWidth * h / w;
        int be = 1;
        if (w > h && w > desWidth) {
            be = (int) (newOpts.outWidth / desWidth);
        } else if (w < h && h > desHeight) {
            be = (int) (newOpts.outHeight / desHeight);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置采样率

        LogUtil.i("","4="+bitmap);
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        LogUtil.i("","srcPath="+srcPath+",,newOpts="+newOpts);
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        LogUtil.i("","5="+bitmap);
        return bitmap;
    }
    /**
     * 压缩图片（质量压缩）
     *
     * @param image
     */

    public static File compressImage(Context c,Bitmap image, String imgName) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        while (baos.toByteArray().length / 1024 > quality) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
//        new File(CpConfig.IMG_CACHE_PATH).mkdirs();
        new File(new CpConfig(c).getImgCachePath()).mkdirs();
//        File file = new File(CpConfig.IMG_CACHE_PATH + File.separator +imgName);
        File file = new File(new CpConfig(c).getImgCachePath() + File.separator +imgName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }
    public static File compressImage(Bitmap image, String imgName,String outPath,Bitmap.CompressFormat format ) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(format, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        while (baos.toByteArray().length / 1024 > quality) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        new File(outPath).mkdirs();
        File file = new File(outPath + File.separator +imgName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null || degress != 0) {
            Matrix m = new Matrix();
            m.setRotate(degress, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            if (bitmap != null) {
                bitmap.recycle();
            }
            return bmp;
        }
        return bitmap;
    }

    public static String getFileName(Context c,String filePath,String imgName) {
        Bitmap bitmap = ImageUtils.compressImageFromFile(filePath, desWidth);// 按尺寸压缩图片
        int degree = readPictureDegree(filePath);
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bitmap = rotateBitmap(bitmap, degree);
        }
        int size = bitmap.getByteCount();
        File file = ImageUtils.compressImage(c,bitmap,imgName);  //按质量压缩图片
        return file.getAbsolutePath();
    }
    public static String getFileName(String filePath, String imgName,String outPath,Bitmap.CompressFormat format ) {
        Bitmap bitmap = ImageUtils.compressImageFromFile(filePath, desWidth);// 按尺寸压缩图片
        int degree = readPictureDegree(filePath);
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bitmap = rotateBitmap(bitmap, degree);
        }
        int size = bitmap.getByteCount();
        File file = ImageUtils.compressImage(bitmap,imgName,outPath,format);  //按质量压缩图片
        return file.getAbsolutePath();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 拼接图片
     *
     * @param firstBitmap
     * @param secondBitmap
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        int width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight() + secondBitmap.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, firstBitmap.getHeight(), null);
        return bitmap;
    }


    public static float getDesWidth() {
        return desWidth;
    }

    /**
     * 设置压缩后宽高
     * @param desWidth
     */
    public static void setDesWidth(float desWidth) {
        ImageUtils.desWidth = desWidth;
    }

    public static int getQuality() {
        return quality;
    }

    /**
     * 设置质量
     * @param quality
     */
    public static void setQuality(int quality) {
        ImageUtils.quality = quality;
    }
}
