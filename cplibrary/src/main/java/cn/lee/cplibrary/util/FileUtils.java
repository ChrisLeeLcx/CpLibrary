package cn.lee.cplibrary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.timer.ScheduledHandler;
import cn.lee.cplibrary.util.timer.ScheduledTimer;
import cn.lee.cplibrary.util.timer.TimeUtils;

public class FileUtils {

    public static final String FILE_SEPARATOR = File.separator;
    private static Object filePath;

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long availableBlocks = stat.getAvailableBlocksLong() - 4;
                // 获取单个数据块的大小（byte）
                long freeBlocks = stat.getAvailableBlocksLong();
                return freeBlocks * availableBlocks;

            } else {
                long availableBlocks = stat.getAvailableBlocks() - 4;
                // 获取单个数据块的大小（byte）
                long freeBlocks = stat.getAvailableBlocks();
                return freeBlocks * availableBlocks;
            }

        }
        return 0;
    }

    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableInternalMemorySize() {

        File path = Environment.getDataDirectory();

        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = stat.getBlockSizeLong();

            long availableBlocks = stat.getAvailableBlocksLong();

            return availableBlocks * blockSize;
        } else {
            long blockSize = stat.getBlockSize();

            long availableBlocks = stat.getAvailableBlocks();

            return availableBlocks * blockSize;

        }

    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isFileExist(File file) {
        if (null != file && file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean makeFile(String path,boolean isDelete) {
        File file = new File(path);

        if (path.endsWith(FILE_SEPARATOR)) {
            return false;
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.exists()&&isDelete) {
                file.delete();
            }
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
        return isFileExist(path);
    }

    public static boolean deleteFile(String path, FileDeleteCallback fileDeleteCallback, boolean... isDeleteDir) {

        File file = new File(path);
        if (!file.exists()) {

            if (null != fileDeleteCallback) {
                fileDeleteCallback.result(2);
            }
        }
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            String[] fileList = file.list();
            for (int i = 0; i < fileList.length; i++) {
                File delfile = new File(path + FILE_SEPARATOR + fileList[i]);
                if (!delfile.isDirectory()) {
                    delfile.delete();
                } else if (delfile.isDirectory()) {
                    deleteFile(path + FILE_SEPARATOR + fileList[i], fileDeleteCallback);
                }
            }
            if (isDeleteDir.length > 0 && isDeleteDir[0]) {
                file.delete();
            }

            if (file.getAbsolutePath() != null && file.getAbsolutePath().equals(path)) {

                if (null != fileDeleteCallback) {
                    fileDeleteCallback.result(1);
                }
            }
        }

        return true;

    }

    public static long getFileSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return getFileSize(file);
    }

    public static long getFileSize(File file) {
        if (file.isFile()) {
            return file.length();
        }
        File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (File child : children) {
                total += getFileSize(child);
            }
        }
        return total;
    }

    public static void renameFile(String srcPath, String dstpath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstpath);
        if (srcFile.exists()) {
            srcFile.renameTo(dstFile);
        }
    }

    public static String savePic(Context c, String fileName, Bitmap bitmap) {
        String filePath = new CpConfig(c).getImgCachePath() + File.separator + fileName;
//        String filePath = CpConfig.IMG_CACHE_PATH + File.separator + fileName;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        FileOutputStream fos = null;
        try {
            makeFile(filePath,true);
            File file = new File(filePath);
            fos = new FileOutputStream(file);
            fos.write(bytes);

        } catch (Exception e) {
            filePath = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    filePath = "";
                }
            }
        }
        return filePath;
    }

    /**
     * 向文件中输入信息
     *
     * @param c
     * @param fileName
     * @param msg      :eg
     * @return
     */
    private static String writeMsg(Context c, String fileName, String msg) {
//        String filePath = CpConfig.LOG_CACHE_PATH + File.separator + fileName;
        String filePath =new  CpConfig(c).getLogCachePath() + File.separator + fileName;
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            makeFile(filePath,false);
            //创建字符型的输出流
            fw = new FileWriter(filePath,true);
            //创建字符缓冲流
            bw = new BufferedWriter(fw);
            //被写出的数据
            msg = msg + ",time=" + TimeUtils.getCurTime();
            //通过字符串缓冲流把msg数据写入到filePath文件中
            bw.write(msg);
            //写出一个换行
            bw.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关流的时候，应该先关外层的流
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    public static void writeMsg2File(final Context c, final String fileName, final String msg) {
        ScheduledTimer scheduledTimer = new ScheduledTimer(
                new ScheduledHandler() {

                    @Override
                    public void post(int times) {
                        writeMsg(c, fileName, msg);
                    }

                    @Override
                    public void end() {
                    }
                }, 0, 10, 1);
        scheduledTimer.start();
    }

    public interface FileDeleteCallback {
        /**
         * @param state(1,成功;2,没有可清除)
         */
        public void result(int state);
    }


    /**
     * @fun： 删除paths里面的所有文件
     * @author: ChrisLee at 2017-6-20 下午1:35:00
     */
    public static void deleteFiles(List<String> paths) {
        for (int i = 0; i < paths.size(); i++) {
            FileUtils.deleteFile(paths.get(i), new FileDeleteCallback() { // 删除原文件

                @Override
                public void result(int state) {
                    Log.d("debuginfo", "state=" + state);
                }
            });
        }
    }

    /**
     * @fun： 删除path1和path2文件
     * @author: ChrisLee at 2017-6-20 下午1:35:37
     */
    public static void deleteFile(String path1, String path2) {
        FileUtils.deleteFile(path1, new FileDeleteCallback() {

            @Override
            public void result(int state) {

            }
        });
        FileUtils.deleteFile(path2, new FileDeleteCallback() {

            @Override
            public void result(int state) {

            }
        });
    }

    /**
     * @fun： 删除百度地图离线包
     * @author: ChrisLee at 2017-8-10 下午6:41:10
     */
    public static void deleteFileBaiduMapSDKNew() {
        String filePath = FileUtils.getSDCardPath() + "BaiduMapSDKNew";
        if (isFileExist(filePath)) {
            deleteFile(filePath, new FileDeleteCallback() {

                @Override
                public void result(int state) {
                    if (state == 1) {
                        LogUtil.i("", FileUtils.class, "清除百度离线地图成功");
                    } else {
                        LogUtil.i("", FileUtils.class, "没有百度离线地图");
                    }

                }
            }, true);
        }
    }

}
