package com.lee.demo.util.gaode.loc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lee.demo.R;
import com.lee.demo.base.BaseApplication;

import java.util.Arrays;
import java.util.List;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.SharedPreferencesUtils;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.timer.TimeUtils;


/**
 * 注意本类的第123456步都是需要在外部调用的
 *
 * @author: ChrisLee
 * @time: 2018/12/17
 */

public class GaoDeLBSUtil {
    private static final String SP_NAME_LOC_SERVICE = "sp_name_need_loc_service";
    private static final String KEY_LOC_SERVICE = "key_need_loc_service";
    private long httpTimeOut = 30000;
    private long interval = 1 * 60 * 1000;
    private boolean onceLocation = true;
    private static GaoDeLBSUtil lbs = null;
    private BaseApplication baseApp;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private GetLocationCallBack callBack;
    private Object object;//Activity或者Fragment
    //是否处理申请结果/是否显示进度条/是否定位成功/用户永久拒绝需要的权限时，是否显示引导对话框，使用时候可自行设置/是否显示定位服务弹窗/是否显示定位错误对话框
    private boolean isHandleResult, isLoading, isLocatedSuccess, isShowGuideDialog = true, isShowLocServiceDialog = true, isShowErrorDialog;
    //如果设置了target > 28，9.0 需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permissions.ACCESS_BACKGROUND_LOCATION";
    protected String[] needPermissions23 = {  // 6.0、23需要进行检测的权限数组
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


    /**
     * 外部第1步：
     * 1、最好 在Application的onCreate方法中调用此方法
     * 2、在Activity或者Fragment初始化的时候调用也可以
     */
    public synchronized static GaoDeLBSUtil getInstance(Context context) {
        if (lbs == null) {
            lbs = new GaoDeLBSUtil(context);
        }

        return lbs;
    }


    private GaoDeLBSUtil(Context context) {
        initLocation(context);
        LogUtil.i("", "GaoDeLBSUtil：1、构造函数初始化");
    }

    /**
     * 外部第3步：开启定位
     *
     * @param obj       Activity或者Fragment
     * @param isLoading 是否显示Loading
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GaoDeLBSUtil startLocation(final @NonNull Object obj, boolean isLoading) {
        this.object = obj;
        this.isLoading = isLoading;
        requestPer(true);
        return this;
    }

    /**
     * 外部第5步
     * 停止定位  MainActivity的onDestroy调用
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
     * 外部第6步
     * 销毁定位  ---  MainActivity的onDestroy/按退出鍵方法中调用 （4）
     * 应该在整个项目退出的时候再调用
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
     * 初始化定位client
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation(Context context) {
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = getDefaultOption();
        locationClient.setLocationOption(locationOption);  //设置定位参数
        locationClient.setLocationListener(locationListener); // 设置定位监听
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
        mOption.setHttpTimeOut(httpTimeOut);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(interval);//可选，设置定位间隔。默认为60s
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(onceLocation);//可选，设置是否单次定位。默认是false
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
            LogUtil.i("", "GaoDeLBSUtil：3、获取到的定位数据" + location);
            if (isLoading) {
                CpComDialog.closeProgressDialog();
            }
            if (baseApp != null) {
                baseApp.saveLocationInfo(location);
            }
            if (null != location && location.getErrorCode() == 0 && location.getLatitude() != 0) {//定位成功
                isLocatedSuccess = true;
                if (callBack != null) {
                    callBack.onLocationChanged(location);
                }
            } else {
                isLocatedSuccess = false;
                if (callBack != null) {
                    callBack.onLocationFail(location);
                }
                if (isLocServiceFail(location)) {
                    SharedPreferencesUtils
                            .putShareValue(getContext(),
                                    SP_NAME_LOC_SERVICE,
                                    KEY_LOC_SERVICE,
                                    true);
                }
                if (isShowLocServiceDialog && isLocServiceFail(location)) {//设置显示定并且定位失败原因是定位服务未开
                    showLocServiceDialog();
                }
            }
        }

    };

    //--------------------------------------------权限部分-------------------------------------
    private PermissionUtil permissionUtil;

    private void requestPer(boolean isHandleResult) {
        requestPer(object, isHandleResult);
    }

    /**
     * 外部调用第2步：申请权限（也可不设置）
     * 注意:requestPer与startLocation 不能同时调用，否则权限检测结果会出错
     * @param isHandleResult 是否处理申请结果
     */
    public GaoDeLBSUtil requestPer(Object obj, boolean isHandleResult) {
        this.object = obj;
        this.isHandleResult = isHandleResult;
        if (permissionUtil == null) {
            permissionUtil = new PermissionUtil(proxy);
        }
        if (obj instanceof Fragment) {
            permissionUtil.requestPermissions((Fragment) obj, CpConfig.REQUEST_CODE_PER_LOC, getLocationPermissions());
        } else if (obj instanceof Activity) {
            permissionUtil.requestPermissions((Activity) obj, CpConfig.REQUEST_CODE_PER_LOC, getLocationPermissions());
        } else {
            throw new IllegalArgumentException("not supported!" + object.getClass().getName() + "is not a activity or fragment");
        }
        return this;
    }

    private PermissionProxy proxy = new PermissionProxy() {
        @Override
        public void granted(Object source, int requestCode) {
            allPermissionsPrepared();
        }

        @Override
        public void denied(Object source, int requestCode, List deniedPermissions) {
            LogUtil.i("", "GaoDeLBSUtil:denied=" + deniedPermissions);

        }

        @Override
        public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
            LogUtil.i("", "GaoDeLBSUtil:deniedNoShow=" + noShowPermissions);
            showGuideDialog("当前应用缺少定位必要权限\n请点击\"设置\"-\"权限\"-打开所需权限。");
        }

        @Override
        public void rationale(Object source, int requestCode) {

        }

        @Override
        public boolean needShowRationale(int requestCode) {
            return false;
        }
    };

    /**
     * 定位所需权限都申请成功后 开启定位
     */
    private void allPermissionsPrepared() {
        LogUtil.i("", "GaoDeLBSUtil:：1、权限申请成功,");
        if (!isHandleResult) {
            return;
        }
        if (locationClient != null) {
            if (!isNeedLocServiceOn() || (isNeedLocServiceOn() && AppUtils.checkLocationEnabled(getContext()))) {
                if (isLoading) {
                    CpComDialog.showProgressDialog(getContext(), "定位中...");
                }
                locationClient.setLocationOption(locationOption);  // 设置定位参数
                locationClient.startLocation();   // 启动定位
            } else {
                showLocServiceDialog();
            }

        }
    }

    /**
     * 外部调用第4步：
     * 必须在需要定位的fragment 或 Activity中 调用此方法 （必须调用本方法）
     * 权限申请结果处理,在Activity或者Fragment的onRequestPermissionsResult方法中的super方法上一行调用
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        LogUtil.i("", "GaoDeLBSUtil：5、权限申请结果的一些事情");
        if (isHandleResult) {
            if (object instanceof Fragment) {
                permissionUtil.onRequestPermissionsResult((Fragment) object, requestCode, permissions, grantResults);
            } else if (object instanceof Activity) {
                permissionUtil.onRequestPermissionsResult((Activity) object, requestCode, permissions, grantResults);
            }
        }
    }

    //获取需要的权限
    private String[] getLocationPermissions() {
        if (Build.VERSION.SDK_INT > 28 && getContext().getApplicationInfo().targetSdkVersion > 28) {//9.0、28以上需要增加一个权限
            List<String> list = Arrays.asList(needPermissions23);
            list.add(BACKGROUND_LOCATION_PERMISSION);
            return (String[]) list.toArray();
        } else {
            return needPermissions23;
        }
    }

    /**
     * 显示设置权限提示信息
     *
     * @since 2.5.0
     */
    private void showGuideDialog(String msg) {
        if (!isShowGuideDialog) {
            ToastUtil.showToast(getContext(), msg);
            return;
        }
        TimeUtils.isCheckFastClick = false;
        CpComDialog.Builder.builder(getContext()).
                setTitle("提示").setContent(msg).setTxtCancel("取消").setSure("设置")
                .setTitleSize(20).setContentSize(16).setBtnSize(20)
                .setBtnCancelColor(Color.parseColor("#8d8d8d"))
                .setCancel(false)
                .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
            @Override
            public void sure() {
                AppUtils.jumpAppSettingInfo(getContext());
            }

            @Override
            public void cancel() {

            }
        });
        TimeUtils.isCheckFastClick = true;
    }

    //---------------------------------------------工具---------------------------------------------------------
    //若想将定位结果保存在Application中则调用此方法
    public GaoDeLBSUtil setBaseApp(BaseApplication baseApp) {
        this.baseApp = baseApp;
        return this;
    }

    public GaoDeLBSUtil setShowGuideDialog(boolean showGuideDialog) {
        isShowGuideDialog = showGuideDialog;
        return this;
    }

    public GaoDeLBSUtil setShowLocServiceDialog(boolean showLocServiceDialog) {
        isShowLocServiceDialog = showLocServiceDialog;
        return this;
    }

    public GaoDeLBSUtil setShowErrorDialog(boolean showErrorDialog) {
        isShowErrorDialog = showErrorDialog;
        return this;
    }

    /**
     * 是否因为定位服务未开导致定位失败
     */
    private boolean isLocServiceFail(AMapLocation location) {
        if (location.getLocationDetail().contains("定位服务")) {
            return true;
        }
        return false;
    }


    /**
     * 手机定位是否必须开启定位服务
     */
    public boolean isNeedLocServiceOn() {
        boolean isNeed = SharedPreferencesUtils.getShareBoolean(
                getContext(), SP_NAME_LOC_SERVICE,
                KEY_LOC_SERVICE, false
        );
        return isNeed;
    }

    /**
     * 显示设置地理信息
     */

    public void showLocationErrorResult(AMapLocation location) {
        if (!isLocServiceFail(location) || !isShowLocServiceDialog) {
            String msg =  location.getLocationDetail();
            if (isShowErrorDialog) {
                CpComDialog.Builder.builder(getContext()).
                        setTitle("定位失败").setContent(msg). setSure("知道了")
                        .setCancel(false)
                        .build().show1BtnDialog(new CpComDialog.Dialog1BtnCallBack() {
                    @Override
                    public void sure() {

                    }
                });
            } else {
                ToastUtil.showToast(getContext(),msg);
            }
        }
    }

    /**
     * //错误详细信息：定位服务没有开启，请在设置中打开定位服务开关#1206
     * 定位服务弹框
     */
    public void showLocServiceDialog() {
        CpComDialog.Builder.builder(getContext()).
                setTitle("提示").setContent("您的地理信息或定位服务未开启，是否开启？").setTxtCancel("暂不").setSure("去开启")
                .setCancel(false)
                .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
            @Override
            public void sure() {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                if (object instanceof Fragment) {
                    ((Fragment) object).startActivityForResult(intent, 0);
                } else {
                    ((Activity) object).startActivityForResult(intent, 0);
                }
                stopLocation();
            }

            @Override
            public void cancel() {
                stopLocation();
                ToastUtil.showToast(getContext(), "部分功能需要开启地址服务");
            }
        });
    }

    private Context getContext() {//需要在开启定位startLocation之后使用
        if (object instanceof Fragment) {
            return ((Fragment) object).getContext();
        } else if (object instanceof Activity) {
            return (Activity) object;
        } else {
            throw new IllegalArgumentException("not supported!" + object.getClass().getName() + "is not a activity or fragment");
        }
    }

    public interface GetLocationCallBack {
        void onLocationChanged(AMapLocation location);//定位成功

        void onLocationFail(AMapLocation location);//定位失败包括 不申请权限
    }

    public boolean isLocatedSuccess() {
        return isLocatedSuccess;
    }


    public GetLocationCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(GetLocationCallBack callBack) {
        this.callBack = callBack;
    }

    public void setHttpTimeOut(long httpTimeOut) {
        this.httpTimeOut = httpTimeOut;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setOnceLocation(boolean onceLocation) {
        this.onceLocation = onceLocation;
    }

//-----------------------------------------------关于后台定位------------------------------------

    /**
     * 设置后台定位，(一般onResume 关闭后台定位，onStop 开启后台定位）（6）
     * 在开启定位的Fragment和Activity中onResume方法中调用 setBgLocation(false)，onStop 方法中调用setBgLocation(true);
     *
     * @param isLocated true 开启后台定位，false 关闭后台定位
     */

    public void setBgLocation(boolean isLocated) {
        if (isLocated) {
            boolean isBackground = baseApp.isBackground();
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
                notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getContext().getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getContext().getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getContext().getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Utils.getAppName(getContext()))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }


}
