package com.lee.demo.util.gaode.loc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lee.demo.R;
import com.lee.demo.base.BaseApplication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;


/**
 * 注意本类的 （1）（2）（3）（4）（5）（6）都是需要在外部调用的
 *
 * @author: ChrisLee
 * @time: 2018/12/17
 */

public class GaoDeLBSUtil1 {
    private static GaoDeLBSUtil1 lbs = null;
    private BaseApplication baseApplication;
    private Context context = null;

    private boolean isLocatedSuccess = false;//是否定位成功

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private GetLocationCallBack callBack;
    private Fragment fragment;//在Fragment调用
    private Activity activity;//在Activity调用
    private boolean isLoading;//是否显示进度条

    private GaoDeLBSUtil1(Context context, BaseApplication baseApplication) {
        this.context = context;
        this.baseApplication = baseApplication;
        initLocation();
    }
    private GaoDeLBSUtil1(Context context) {
        this.context = context;
        initLocation();
    }

    /**
     * 在Application 的onCreate方法中调用此方法（1）
     */
    public synchronized static GaoDeLBSUtil1 getInstance(Context context, BaseApplication baseApplication) {
        if (lbs == null) {
            lbs = new GaoDeLBSUtil1(context, baseApplication);
        }

        return lbs;
    }
    /**
     * 在Application 的onCreate方法中调用此方法（1）
     */
    public synchronized static GaoDeLBSUtil1 getInstance(Context context) {
        if (lbs == null) {
            lbs = new GaoDeLBSUtil1(context);
        }

        return lbs;
    }
    public static void clearLbs() {
        GaoDeLBSUtil1.lbs= null;
    }

    public GetLocationCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(GetLocationCallBack callBack) {
        this.callBack = callBack;
    }
    /**
     * Fragment中 开始定位 在需要定位的页面 开启定位 （2）
     * @author hongming.wang
     * @isLoading 是否显示 进度条
     * @since 2.8.0
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GaoDeLBSUtil1 startLocation(Fragment fragment, boolean isLoading) {
        this.fragment = fragment;
        this.isLoading = isLoading;
        check();
        return this;
    }

    /**
     * activity 中 开始定位 在需要定位的页面 开启定位 （2）
     *
     * @author hongming.wang
     * @isLoading 是否显示 进度条
     * @since 2.8.0
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GaoDeLBSUtil1 startLocation(Activity activity, boolean isLoading) {
        this.activity = activity;
        this.isLoading = isLoading;
        check();
        return this;
    }

    public GaoDeLBSUtil1 startLocation() {
        allPermissionsPrepared();
        return this;
    }

    /**
     * 停止定位  MainActivity的onDestroy调用 （3）
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void stopLocation() {
        CpComDialog.closeProgressDialog();
        // 停止定位
        if (locationClient != null) {
            locationClient.stopLocation();
        }

    }

    /**
     * 销毁定位  ---  MainActivity的onDestroy方法中调用 （4）
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //高精度模式：会同时使用网络定位和GPS定位
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1 * 60 * 1000);//可选，设置定位间隔。默认为60s
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if(isLoading){
                CpComDialog.closeProgressDialog();
            }
            LocationBean locationBean = baseApplication.getLocationBean();
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                locationBean.setErrorCode(location.getErrorCode());

                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0 && location.getLatitude() != 0) {
                    locationBean.setLatitude(location.getLatitude());
                    locationBean.setLongitude(location.getLongitude());
                    Log.i("tag", "showAddress: location.getAddress()=" + location.getAddress());
                    locationBean.setAddress(location.getAddress());
                    locationBean.setCountry(location.getCountry());
                    locationBean.setProvince(location.getProvince());
                    locationBean.setCity(location.getCity());
                    locationBean.setCityCode(location.getCityCode());
                    locationBean.setDistrict(location.getDistrict());
                    locationBean.setName(location.getPoiName());
                    locationBean.setStreet(location.getStreet());
                    locationBean.setStreetNum(location.getStreetNum());
                    LogUtil.i("", "----location.toString()" + location.toString() + ",callBack=" + callBack);
                    if (callBack != null) {
                        callBack.onLocationChanged(location);
                    }
                    isLocatedSuccess = true;
                } else {
                    LogUtil.i("", "----location.toString()" + location.toString() + ",callBack=" + callBack);
                    if (callBack != null) {
                        callBack.onLocationFail();
                    }
                }
            } else {
                LogUtil.i("", "----location" + location);
                locationBean.setErrorCode(-99);
                if (callBack != null) {
                    callBack.onLocationFail();
                }
            }
            LogUtil.e("", GaoDeLBSUtil1.this, "获取到的定位数据=" + location);
            LogUtil.e("", GaoDeLBSUtil1.this, "locationBean=" + locationBean);

        }

    };
    //-----------------------------------------------关于后台定位------------------------------------

    /**
     * 设置后台定位，(一般onResume 关闭后台定位，onStop 开启后台定位）（6）
     * 在开启定位的Fragment和Activity中onResume方法中调用 setBgLocation(false)，onStop 方法中调用setBgLocation(true);
     *
     * @param isLocated true 开启后台定位，false 关闭后台定位
     */

    public void setBgLocation(boolean isLocated) {
        if (isLocated) {
            boolean isBackground = baseApplication.isBackground();
            if (isBackground) {    //如果app已经切入到后台，启动后台定位功能
                if (null != locationClient) {
                    locationClient.enableBackgroundLocation(2001, buildNotification());
                }
            }
        } else {
            if (null != locationClient) {    //切入前台后关闭后台定位功能
                locationClient.disableBackgroundLocation(true);
            }
        }
    }

    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = context.getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(context.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Utils.getAppName(context))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

    public interface GetLocationCallBack {
        void onLocationChanged(AMapLocation location);//定位成功

        void onLocationFail();//定位失败包括 不申请权限
    }

    public boolean isLocatedSuccess() {
        return isLocatedSuccess;
    }
    //--------------------------------------------权限部分-------------------------------------

    /**
     * 定位所需权限都申请成功后 开启定位
     */
    public void allPermissionsPrepared() {
        LogUtil.i("","1111---------"+locationClient);
        if (locationClient != null) {
            if (isLoading) {
                CpComDialog.showProgressDialog(context, "定位中...");
            }
            locationClient.setLocationOption(locationOption);  // 设置定位参数
            locationClient.startLocation();   // 启动定位
        }
    }
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    /**
     * 6.0、23需要进行检测的权限数组
     */
    protected String[] needPermissions23 = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //9.0、28以上需要的权限组
    protected String[] needPermissions28 = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
            ,BACKGROUND_LOCATION_PERMISSION
    };
    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void check() {
        //定位信息按钮打开
//        if (MapUtils.isLocationEnabled(activity)) {
            //检测权限
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M
                    && context.getApplicationInfo().targetSdkVersion >=  Build.VERSION_CODES.M) {
                if (isNeedCheck) {
                    LogUtil.i("", this, "1111");
                    if(Build.VERSION.SDK_INT > 28&& context.getApplicationInfo().targetSdkVersion > 28) {
                        checkPermissions(needPermissions28);
                    }else{
                        checkPermissions(needPermissions23);
                    }
                } else {
                    LogUtil.i("", this, "2222");
                }
            } else {
                LogUtil.i("", this, "3333");
                allPermissionsPrepared();
            }
//        } else {
//            showLocationInfoDialog();
//        }
    }


    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && context.getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Object obj = (fragment == null) ? activity : fragment;
                    Method method = obj.getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});
                    method.invoke(obj, array, PERMISSON_REQUESTCODE);
                } else {
                    LogUtil.i("", this, "4444");
                    allPermissionsPrepared();
                }
            }
        } catch (Throwable e) {
            LogUtil.i("", " e2 = " + e.getMessage());

        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23
                && context.getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = context.getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = context.getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(context, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(context, perm)) {
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {
                LogUtil.i("", "", "e1=" + e.getMessage());

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 必须在需要定位的fragment 或 Activity中 调用此方法 （5）
     *
     * @param requestCode
     * @param permissions
     * @param paramArrayOfInt
     */
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
                LogUtil.i("", this, "555");
            } else {
                allPermissionsPrepared();
                LogUtil.i("", this, "666");
            }
        }
    }

    /**
     * 显示设置权限提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callBack != null) {
                            callBack.onLocationFail();
                        }
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        isNeedCheck = true;
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示设置地理信息
     *
     * @since 2.5.0
     */
    private void showLocationInfoDialog() {
//        DialogUtils.show2BtnDialog(activity, activity.getResources().getString(R.string.notifyTitle), activity.getResources().getString(R.string.notifyLocationMsg), "暂不", "去开启", new DialogUtils.DialogCallBack() {
//            @Override
//            public void sure() {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                activity.startActivityForResult(intent, 0);
//                stopLocation();
//            }
//
//            @Override
//            public void cancel() {
//                stopLocation();
////                ToastUtil.showToast(activity, "部分功能需要开启地址服务");
//            }
//        });

        CpComDialog.Builder.builder(activity).
                setTitle("提示").setContent("您的地理信息或定位服务未开启，是否开启？").setTxtCancel("暂不").setSure("去开启")
                .setCancel(false)
                .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
            @Override
            public void sure() {
//                AppUtils.jumpAppSettingInfo(activity);

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, 0);
                stopLocation();
            }

            @Override
            public void cancel() {stopLocation();

            }
        });
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
