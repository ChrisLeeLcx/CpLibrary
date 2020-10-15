package com.lee.demo.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.lee.demo.constant.Config;
import com.lee.demo.util.gaode.loc.ForegroundCallbacks;
import com.lee.demo.util.gaode.loc.GaoDeLBSUtil;
import com.lee.demo.util.gaode.loc.LocationBean;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.SharedPreferencesUtils;
import cn.lee.cplibrary.util.system.SIMPerUtil;
import cn.lee.cplibrary.util.system.SimStateReceiver;

/**
 * @author ChrisLee
 */
@SuppressLint("Registered")
public class BaseApplication extends Application {
    private static BaseApplication context = null;
    private String tel;//电话
    private String prefixMsg;//短信开头
    //高德地图
    private LocationBean locationBean;
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LogUtil.setIsDebug(Config.isDebug);
        setDayNightMode();
        GaoDeLBSUtil.getInstance(context);

        ForegroundCallbacks.init(this);
        ForegroundCallbacks.get().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                LogUtil.i("","当前程序切换到前台");
//                if(CacheUtils.getBoolean(getApplicationContext(), MyConst.GESTRUE_IS_LIVE)){
//                    L.d("已经开启手势锁");
//                    Intent intent = new Intent(getApplicationContext(), CheckGesPwdActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }else{
//
//                }

            }

            @Override
            public void onBecameBackground() {
                LogUtil.i("","当前程序切换到后台");

            }
        });
    }

    public static BaseApplication getContext() {
        return context;
    }


    /**
     * 切换日夜间模式: 当前日间模式 则切换成夜间模式，如果是夜间模式则切换成日间模式
     * MODE_NIGHT_NO: 使用亮色(light)主题,不使用夜间模式
     * MODE_NIGHT_YES:使用暗色(dark)主题,使用夜间模式
     * MODE_NIGHT_AUTO:根据当前时间自动切换 亮色(light)/暗色(dark)主题
     * MODE_NIGHT_FOLLOW_SYSTEM(默认选项):设置为跟随系统,通常为 MODE_NIGHT_NO
     */
    public void switchDayNightMode() {
        //isDayMode :true 日间模式 false ：夜间模式
        boolean isDayMode = isDayMode();
        if (isDayMode) {//日间模式 ->切换成夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferencesUtils.putShareValue(context,
                    "isDayMode", false);
        } else {//夜间模式 ->切换成日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferencesUtils.putShareValue(context,
                    "isDayMode", true);
        }
    }

    /**
     * 设置日夜间模式
     */
    public void setDayNightMode() {
        boolean isDayMode = isDayMode();
        if (isDayMode) {//日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {//夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public boolean isDayMode() {
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

    //------------------地图定位----------------
    public LocationBean getLocationBean() {
        if (locationBean == null) {
            locationBean = new LocationBean();
        }
        return locationBean;
    }

    public void saveLocationInfo(AMapLocation location) {
        LocationBean locLean=getLocationBean();
        if (location != null) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0 && location.getLatitude() != 0) {
                locLean.setLatitude(location.getLatitude());
                locLean.setLongitude(location.getLongitude());
                locLean.setAddress(location.getAddress());
                locLean.setCountry(location.getCountry());
                locLean.setProvince(location.getProvince());
                locLean.setCity(location.getCity());
                locLean.setCityCode(location.getCityCode());
                locLean.setDistrict(location.getDistrict());
                locLean.setName(location.getPoiName());
                locLean.setStreet(location.getStreet());
                locLean.setStreetNum(location.getStreetNum());
            } else {
                locLean.setErrorInfo(location.getErrorInfo());
                locLean.setLocationDetail(location.getLocationDetail());
            }
            locLean.setErrorCode(location.getErrorCode());
        } else {//location是空
            locLean.setErrorCode(-99);
            locLean.setErrorInfo("定位失败");
            locLean.setLocationDetail("定位失败，定位返回信息空值");
        }
        LogUtil.i("", BaseApplication.this, "存储的locationBean=" + locLean);
    }

    /**
     * 判断app是否在后台
     *
     * @return
     */
    public boolean isBackground() {
        if (count <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public void getActivityCount() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (count > 0) {
                    count--;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
    //-------------------------SIM监测广播------------------------
    private SimStateReceiver receiver;
    /**
     * 注冊SIM状态监测广播:需要在登录界面注册
     * 注意：不能在Application中调用，因为监测SIM需要权限，权限在过度页面授权，而Application先于过度页面执行，
     *      导致未授权时候打不开页面
     */
    public void registerSimStateReceiver(Activity activity) {
        unregisterSimStateReceiver();//取消以前注册过的，下面进行重新注册
//        setIccid(SIMPerUtil.getSimSerialNumber(activity));//保存此刻的ICCID
        receiver = new SimStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimStateReceiver.ACTION_SIM_STATE_CHANGED);
        filter.addAction(SimStateReceiver.ACTION_CONNECTION);
        filter.addCategory(SimStateReceiver.CATEGORY_fxj);
        registerReceiver(receiver, filter);
    }

    /**
     * 取消SIM状态监测广播接受者的注册：（应该在整个APP退出的时候取消注册）
     * MainActivity页面的OnDestroy方法中调用
     */
    public void unregisterSimStateReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
            LogUtil.i(SimStateReceiver.TAG, "BaseApplication unregisterSimStateReceiver");

        }
    }
}
