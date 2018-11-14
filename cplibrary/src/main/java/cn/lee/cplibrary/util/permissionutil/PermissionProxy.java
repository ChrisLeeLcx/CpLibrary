package cn.lee.cplibrary.util.permissionutil;

import java.util.List;

/**
 * Created by ChrisLee on 2018/5/22.
 */

public interface PermissionProxy<T> {

    /**授权成功**/
    void granted(T source, int requestCode);
    /**授权失败,source:Fragment或者Activity,**/
    void denied(T source, int requestCode,List<String>  deniedPermissions);


    void rationale(T source, int requestCode);

    /**
     * 是否需要显示权限申请的解释，只有返回true的时候，rationale方法才会执行，通常使用时候返回true
     */
    boolean needShowRationale(int requestCode);
}
