package cn.lee.cplibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PerUtils;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.timer.TimeUtils;

/**
 * 拨打电话工具类
 * Created by ChrisLee on 2020/5/12.
 */

public class PhoneCallUtil {
    public static final String permission = Manifest.permission.CALL_PHONE;//拨打电话权限
    public static final int REQUEST_CALL = 400;
    private PermissionUtil permissionUtil;
    private boolean isShowGuideDialog = true;//用户永久拒绝需要的权限时，是否显示引导对话框，使用时候可自行设置
    private boolean isHandleResult;//是否处理申请结果
    private boolean isActivity = true;//true：Activity中调用,false:fragment中调用
    private Activity activity;
    private Fragment fragment;
    private String phoneNo;


    /**
     * @param obj：Activity或者Fragment
     * @param isShowGuideDialog
     */
    public PhoneCallUtil(final @NonNull Object obj, boolean isShowGuideDialog ) {
        this(obj,isShowGuideDialog,true);
    }
   /**
     * @param obj：Activity或者Fragment
     * @param isShowGuideDialog
     * @param isRequestPer 初始化时候是否请求权限
     */
    public PhoneCallUtil(final @NonNull Object obj, boolean isShowGuideDialog,boolean isRequestPer) {
        this.isShowGuideDialog = isShowGuideDialog;
        //activity和fragment的判断
        activity = getActivity(obj);
        if (obj instanceof Fragment) {
            fragment = (Fragment) obj;
            isActivity = false;
        } else {
            isActivity = true;
        }
        permissionUtil = new PermissionUtil(proxy);
        if(isRequestPer){
            requestPer(false);//请求权限 但不处理
        }
    }
    /**
     * 打电话
     */
    public void callPhone(String phoneNo) {
        this.phoneNo = phoneNo;
        if (ObjectUtils.isEmpty(phoneNo)) {
            ToastUtil.showToast(activity, "您拨打的是空号");
            return;
        }
        List<String> denies = PerUtils.findDeniedPermissions(activity, permission, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> showDenies = PerUtils.findShowDeniedPermissions(activity, permission, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean showGuide =( denies.size() > 0 && showDenies.size()<=0) ? true : false;//有允许的权限，并且未允许的权限都被禁止
        if (showGuide) {
            showGuideDialog(activity, "请先允许访问电话权限");
        }else{
            requestPer(true);
        }
    }

    /**
     * @param isHandleResult 是否处理申请结果
     */
    private void requestPer(boolean isHandleResult) {
        this.isHandleResult =isHandleResult;
        if (isActivity) {
            permissionUtil.requestPermissions(activity, REQUEST_CALL, permission, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            permissionUtil.requestPermissions(fragment, REQUEST_CALL, permission, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * （必须调用本方法）
     * 权限申请结果处理,在Activity或者Fragment的onRequestPermissionsResult方法中的super方法上一行调用
     * eg：
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(isHandleResult){
            if (isActivity) {
                permissionUtil.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
            } else {
                permissionUtil.onRequestPermissionsResult(fragment, requestCode, permissions, grantResults);
            }
        }

    }

    private Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        } else {
            throw new IllegalArgumentException("not supported!" + object.getClass().getName() + "is not a activity or fragment");
        }
    }

    private PermissionProxy proxy = new PermissionProxy() {
        @Override
        public void granted(Object source, int requestCode) {
            if (ObjectUtils.isEmpty(phoneNo)) {
                return;
            }
            Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
            if (isActivity) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showToast(activity, "拨打电话权限授权失败");
                    return;
                }
                activity.startActivity(call);
            } else {
                fragment.startActivity(call);
            }
        }

        @Override
        public void denied(Object source, int requestCode, List deniedPermissions) {
            LogUtil.i("", "denied=" + deniedPermissions);
        }

        @Override
        public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
            LogUtil.i("", "deniedNoShow=" + noShowPermissions);
        }

        @Override
        public void rationale(Object source, int requestCode) {

        }

        @Override
        public boolean needShowRationale(int requestCode) {
            return false;
        }
    };

    private void showGuideDialog(final Activity c, String msg) {
        if (!isShowGuideDialog) {
            ToastUtil.showToast(c, msg);
            return;
        }
        TimeUtils.isCheckFastClick = false;
        CpComDialog.Builder.builder(c).
                setTitle("提示").setContent(msg).setTxtCancel("拒绝").setSure("开启")
                .setTitleSize(20).setContentSize(16).setBtnSize(20)
                .setBtnCancelColor(Color.parseColor("#8d8d8d"))
                .setCancel(false)
                .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
            @Override
            public void sure() {
                AppUtils.jumpAppSettingInfo(c);
            }

            @Override
            public void cancel() {

            }
        });
        TimeUtils.isCheckFastClick = true;
    }
}
