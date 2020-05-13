package cn.lee.cplibrary.util.permissionutil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.lee.cplibrary.util.LogUtil;


/**
 * function:权限工具类
 * 参考鸿洋大神的博客： https://blog.csdn.net/lmj623565791/article/details/50709663
 * 权限申请的步骤
 * 1、在AndroidManifest文件中添加需要的权限。
 * 2、检查权限
 * 3、申请授权
 * 4、处理权限申请回调 在Activity 或Fragment的 onRequestPermissionsResult方法中处理权限申请成功或者失败的结果，
 * 在方法的最后再调用父类处理方法  super.onRequestPermissionsResult（...）
 * 主要涉及到的类：1、Manifest包含安卓中所有的权限的类（permission_group：是Dangerous Permissions 分组权限，permission：是包含Normal Permissions和Dangerous Permissions，是全部的权限）
 * 1、方式shouldShowRequestPermissionRationale，回到最初的解释“应不应该解释下请求这个权限的目的”。
 * 1.都没有请求过这个权限，用户不一定会拒绝你，所以你不用解释，故返回false;
 * 2.请求了但是被拒绝了，此时返回true，意思是你该向用户好好解释下了；
 * 3.请求权限被禁止了，也不给你弹窗提醒了，所以你也不用解释了，故返回false;
 * 4.请求被允许了，都给你权限了，还解释个啥，故返回false。
 * Created by ChrisLee on 2018/5/22.
 */

public class PermissionUtil {

    private PermissionProxy proxy = null;

    public PermissionUtil(@NonNull PermissionProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * 给用户一个申请权限的解释: 没有任何作用-废弃
     */
    public boolean shouldShowRequestPermissionRationale(Activity activity, int requestCode, String permission) {
        if (!proxy.needShowRationale(requestCode)) {
            return false;
        }
        //只有在用户在上一次已经拒绝过你的这个权限申请,并且没有选择不再提醒选项，才会返回true
        // 当发起第二次权限请求并且当用户选择了“不再提示”这一选项时该方法返回false
        //则：推出：当进行第二次权限请求被拒绝并且shouldShowRequestPermissionRationale（）返回false时，那么该用户一定是选择了“不再提示”这一选项。
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission)) {
            proxy.rationale(activity, requestCode);
            return true;
        }
        return false;
    }

    /**
     * 申请权限
     */
    public void requestPermissions(Activity activity, int requestCode, String... permissions) {
        _requestPermissions(activity, requestCode, permissions);
    }

    /**
     * 申请权限
     */
    public void requestPermissions(Fragment fragment, int requestCode, String... permissions) {
        _requestPermissions(fragment, requestCode, permissions);
    }


    @TargetApi(value = Build.VERSION_CODES.M)
    private void _requestPermissions(Object object, int requestCode, String... permissions) {
        if (!PerUtils.isOverMarshmallow()) {
            proxy.granted(object, requestCode);
            return;
        }
        List<String> deniedPermissions = PerUtils.findDeniedPermissions(PerUtils.getActivity(object), permissions);
        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException("not supported!" + object.getClass().getName() + "is not a activity or fragment");
            }
        } else {
            proxy.granted(object, requestCode);
        }
    }

    /**
     * 权限申请结果处理
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                           int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    /**
     * 权限申请结果处理
     */
    public void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                           int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private void requestResult(Object obj, int requestCode, String[] permissions,
                               int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();//每次被拒绝的权限列表
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            proxy.denied(obj, requestCode, deniedPermissions);
            Map<String, List<String>> map = PerUtils.groupDeniedPermissions(PerUtils.getActivity(obj), deniedPermissions);
            if (map.get("show").size() <= 0) {//可以显示的
                proxy.deniedNoShow(obj, requestCode, map.get("noShow"));
            }
        } else {
            proxy.granted(obj, requestCode);
        }
        Log.i("debuginfo", "PermissionUtil:deniedPermissions.size()=" + deniedPermissions.size());
    }
}
