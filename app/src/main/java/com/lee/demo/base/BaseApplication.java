package com.lee.demo.base;


import android.annotation.SuppressLint;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.lee.demo.constant.Config;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.SharedPreferencesUtils;

/**
 * @author ChrisLee
 */
@SuppressLint("Registered")
public class BaseApplication extends Application {
    private static BaseApplication context = null;
    private String tel;//电话
    private String prefixMsg;//短信开头
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LogUtil.setIsDebug(Config.isDebug);
        setDayNightMode();
    }
    public static BaseApplication getContext() {
        return context;
    }


    /**
     * 切换日夜间模式: 当前日间模式 则切换成夜间模式，如果是夜间模式则切换成日间模式
     *  MODE_NIGHT_NO: 使用亮色(light)主题,不使用夜间模式
     *  MODE_NIGHT_YES:使用暗色(dark)主题,使用夜间模式
     *  MODE_NIGHT_AUTO:根据当前时间自动切换 亮色(light)/暗色(dark)主题
     *  MODE_NIGHT_FOLLOW_SYSTEM(默认选项):设置为跟随系统,通常为 MODE_NIGHT_NO
     */
    public  void switchDayNightMode(){
        //isDayMode :true 日间模式 false ：夜间模式
        boolean isDayMode = isDayMode();
        if(isDayMode){//日间模式 ->切换成夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferencesUtils.putShareValue(context,
                    "isDayMode", false);
        }else{//夜间模式 ->切换成日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferencesUtils.putShareValue(context,
                    "isDayMode", true);
        }
    }
    /**
     * 设置日夜间模式
     */
    public  void setDayNightMode(){
        boolean isDayMode = isDayMode();
        if(isDayMode){//日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate. MODE_NIGHT_NO);
        }else{//夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    public  boolean isDayMode(){
        //isDayMode :true 日间模式 false ：夜间模式
        boolean isDayMode = SharedPreferencesUtils.getShareBoolean(context,
                "isDayMode", true);
       return isDayMode;
    }

    public String getTel() {
        if (ObjectUtils.isEmpty(tel)) {
            tel = SharedPreferencesUtils
                    .getShareString(this, "tel");
        }
        return tel;
    }

    public void setTel(String tel) {
        SharedPreferencesUtils.putShareValue(this, "tel", tel);
        this.tel = tel;
    }

    public String getPrefixMsg() {
        if (ObjectUtils.isEmpty(prefixMsg)) {
            prefixMsg = SharedPreferencesUtils
                    .getShareString(this, "prefixMsg");
        }
        return prefixMsg;
    }

    public void setPrefixMsg(String prefixMsg) {
        SharedPreferencesUtils.putShareValue(this, "prefixMsg", prefixMsg);
        this.prefixMsg = prefixMsg;
    }
}
