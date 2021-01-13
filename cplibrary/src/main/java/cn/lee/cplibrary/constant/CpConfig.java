package cn.lee.cplibrary.constant;

import android.content.Context;

import java.io.File;

import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.FileUtils;

/**
 * @author: ChrisLee
 * @time: 2018/7/13
 */

public class CpConfig {
    private Context context;
    private String projectName; //項目名
    private String cachePath; //項目的缓存目录
    private String imgCachePath;//項目的图片件缓存目录
    private String logCachePath;//項目的日志文件目录
    private String videoCachePath;//項目的视频缓存目录

    public CpConfig(Context context) {
        this.context = context;
        projectName = AppUtils.getAppId(context);
        cachePath = FileUtils.getSDCardPath() + projectName;
        imgCachePath = cachePath + File.separator + "img" + File.separator;
        logCachePath = cachePath + File.separator + "log" + File.separator;
        videoCachePath = cachePath + File.separator + "video" + File.separator;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getCachePath() {
        return cachePath;
    }

    public String getImgCachePath() {
        return imgCachePath;
    }

    public String getLogCachePath() {
        return logCachePath;
    }

    public String getVideoCachePath() {
        return videoCachePath;
    }
}
