package com.lee.cplibrary.base;


import android.annotation.SuppressLint;
import android.app.Application;

import com.lee.cplibrary.constant.Config;

import cn.lee.cplibrary.util.LogUtil;
/**
 * @author ChrisLee
 */
@SuppressLint("Registered")
public class BaseApplication extends Application {
    private static BaseApplication context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LogUtil.setIsDebug(Config.isDebug);
    }
    public static BaseApplication getContext() {
        return context;
    }


}
