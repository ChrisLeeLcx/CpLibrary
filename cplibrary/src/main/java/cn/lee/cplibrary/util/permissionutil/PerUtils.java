package cn.lee.cplibrary.util.permissionutil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lee.cplibrary.util.LogUtil;

/**
 * function:供PermissionUtil使用的类
 * Created by ChrisLee on 2018/5/22.
 */

final public class PerUtils {
    private PerUtils(){}
    /**
     * 版本是否大于23 棉花糖 6.0
     */
    protected static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @return permission数组中未被授权的权限组成的数组
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permission){
            if(activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }
    /**
     *返回权限组permission中未被授权并且允许询问的的权限列表
     * 允许询问:1、包含首次请求权限 或者 2、请求了但是被拒绝了但未被禁止
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findShowDeniedPermissions(Activity activity, String... permission){
        List<String> deniedPermissions = PerUtils.findDeniedPermissions(activity, permission);//被拒绝的权限
        List<String> ps = new ArrayList<>();//被拒绝并且允许询问的权限
        for(String value : deniedPermissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,value)){
                ps.add(value);
            }
        }
        return ps;
    }

    /**
     * 给被拒绝的权限分组：1、询问 2、禁止询问
     * deniedPermissions：被拒绝过的权限
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    protected static  Map<String,List<String>> groupDeniedPermissions(Activity activity, List<String> deniedPermissions){
        Map<String,List<String>> group = new HashMap<>();
        List<String> psShow = new ArrayList<>();//被拒绝并且允许询问的权限
        List<String> psNoShow = new ArrayList<>();//被拒绝并且禁止询问的权限
        for(String value : deniedPermissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,value)){
                psShow.add(value);
            }else{
                psNoShow.add(value);
            }
        }
        group.put("show",psShow);
        group.put("noShow",psNoShow);
        LogUtil.i("","group="+group);
        return group;
    }
    protected static List<Method> findAnnotationMethods(Class clazz, Class<? extends Annotation> clazz1){
        List<Method> methods = new ArrayList<>();
        for(Method method : clazz.getDeclaredMethods()){
            if(method.isAnnotationPresent(clazz1)){
                methods.add(method);
            }
        }
        return methods;
    }



    protected static Activity getActivity(Object object){
        if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        } else if(object instanceof Activity){
            return (Activity) object;
        }
        return null;
    }
}
