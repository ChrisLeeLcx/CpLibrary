package cn.lee.cplibrary.util.permissionutil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    protected static List<String> findDeniedPermissions(Activity activity, String... permission){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permission){
            if(activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
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
