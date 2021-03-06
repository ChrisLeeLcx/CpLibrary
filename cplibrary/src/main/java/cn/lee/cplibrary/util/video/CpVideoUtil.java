package cn.lee.cplibrary.util.video;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.FileUtils;

public class CpVideoUtil {

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void writeFile(Context context) {
        try {
            OutputStream os = getLogStream(context);
            os.write(getInformation(context).getBytes("utf-8"));
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(Context context, String str) {
        try {
            OutputStream os = getLogStream(context);
            os.write(str.getBytes("utf-8"));
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OutputStream getLogStream(Context context) throws IOException {
        //crash_log_pkgname.log
        String model = Build.MODEL.replace(" ", "_");
        String fileName = String.format("compress_" + model + ".log", context.getPackageName());
        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return new FileOutputStream(file, true);
    }

    public static String getInformation(Context context) {
        long current = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder().append('\n');
        sb.append("BOARD: ").append(Build.BOARD).append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("CPU_ABI: ").append(Build.CPU_ABI).append('\n');
        sb.append("CPU_ABI2: ").append(Build.CPU_ABI2).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("DISPLAY: ").append(Build.DISPLAY).append('\n');
        sb.append("FINGERPRINT: ").append(Build.FINGERPRINT).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n');
        sb.append("HOST: ").append(Build.HOST).append('\n');
        sb.append("ID: ").append(Build.ID).append('\n');
        sb.append("MANUFACTURER: ").append(Build.MANUFACTURER).append('\n');
        sb.append("MODEL: ").append(Build.MODEL).append('\n');
        sb.append("PRODUCT: ").append(Build.PRODUCT).append('\n');
        sb.append("SERIAL: ").append(Build.SERIAL).append('\n');
        sb.append("TAGS: ").append(Build.TAGS).append('\n');
        sb.append("TIME: ").append(Build.TIME).append(' ').append(toDateString(Build.TIME)).append('\n');
        sb.append("TYPE: ").append(Build.TYPE).append('\n');
        sb.append("USER: ").append(Build.USER).append('\n');
        sb.append("VERSION.CODENAME: ").append(Build.VERSION.CODENAME).append('\n');
        sb.append("VERSION.INCREMENTAL: ").append(Build.VERSION.INCREMENTAL).append('\n');
        sb.append("VERSION.RELEASE: ").append(Build.VERSION.RELEASE).append('\n');
        sb.append("VERSION.SDK_INT: ").append(Build.VERSION.SDK_INT).append('\n');
        sb.append("LANG: ").append(context.getResources().getConfiguration().locale.getLanguage()).append('\n');
        sb.append("APP.VERSION.NAME: ").append(getVersionName(context)).append('\n');
        sb.append("APP.VERSION.CODE: ").append(getVersionCode(context)).append('\n');
        sb.append("CURRENT: ").append(current).append(' ').append(toDateString(current)).append('\n');

        return sb.toString();
    }

    public static String toDateString(long timeMilli) {
        Calendar calc = Calendar.getInstance();
        calc.setTimeInMillis(timeMilli);
        return String.format(Locale.CHINESE, "%04d.%02d.%02d %02d:%02d:%02d:%03d",
                calc.get(Calendar.YEAR), calc.get(Calendar.MONTH) + 1, calc.get(Calendar.DAY_OF_MONTH),
                calc.get(Calendar.HOUR_OF_DAY), calc.get(Calendar.MINUTE), calc.get(Calendar.SECOND), calc.get(Calendar.MILLISECOND));
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        int version = 0;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    /**
     * 获取相册路径
     */
    public static String getAlbumStorageDir() {
//        File albumFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); //DCIM路径
        File albumFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera");
        if (!albumFile.exists()) {
            albumFile.mkdirs();
        }
        return albumFile.getAbsolutePath();
    }

    /**
     * 获取APP名下路径
     */
    public static String getAPPStorageDir(Context c) {
        File dir = new File(new CpConfig(c).getVideoCachePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 创建保存视频的路径
     * Create a File for saving an image or video
     */
    public static String getOutputMediaFile(Context c) {
//        String filePath = getAPPStorageDir(c) + File.separator + getVideoName();
        String filePath = getAlbumStorageDir() + File.separator + getVideoName();
        return filePath;
    }

    public static String getVideoName() {//名称毫秒级
        String video_name = "VID" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".mp4";
        return video_name;
    }
    //    private File getOutputMediaFile() {
////        return  new File(getContext().getExternalCacheDir().getAbsolutePath() + "/" + fileName);
//        String appName = getPackageName();
//        File dir = new File(Environment.getExternalStorageDirectory() + "/" + appName);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        url_file = dir + "/video_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
//        Log.i("filePath", url_file);
//        return new File(url_file);
//    }
    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 获取网络视频第一贞图片
     * @param videoUrl 网络视频地址
     * @return
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取视频文件截图
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getLocalVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    /**
     * 获取本地视频的时长，单位ms
     * @param path
     */
    public static int getLocalVideoDuring(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//毫秒
        int time = Integer.parseInt(duration);
        return time;
    }
}
