package cn.lee.cplibrary.util.permissionutil;

import java.util.List;

/**
 * Created by ChrisLee on 2018/5/22.
 */

public interface PermissionProxy<T> {

    /**授权成功：所有需要的权限均被申请才调用本方法**/
    void granted(T source, int requestCode);
    /**授权失败,source:Fragment或者Activity,只要有权限未被申请，denied方法就会被调用**/
    void denied(T source, int requestCode,List<String>  deniedPermissions);

    /**请求权限时候，检测到有未授权的权限且均被禁止，本方法被调用**/
    void deniedNoShow(T source, int requestCode, List<String>  noShowPermissions);

    void rationale(T source, int requestCode);//基本无用

    /**
     * 是否需要显示权限申请的解释，只有返回true的时候，rationale方法才会执行，通常使用时候返回true
     */
    boolean needShowRationale(int requestCode);//基本无用
}
