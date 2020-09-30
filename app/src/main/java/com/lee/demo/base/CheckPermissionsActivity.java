package com.lee.demo.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.List;

import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;

public abstract class CheckPermissionsActivity extends BaseActivity implements PermissionProxy {
    protected PermissionUtil permissionUtil;
    public static final int REQUEST_CODE = 500;
    /*定位权限数组*/
    protected String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,//读
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写
            Manifest.permission.CAMERA,//照相机
            Manifest.permission.CALL_PHONE,//打电话
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissionUtil = new PermissionUtil(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void granted(Object o, int i) {

    }

    @Override
    public void rationale(Object o, int i) {

    }

    @Override
    public boolean needShowRationale(int i) {
        return false;
    }

    @Override
    public void denied(Object source, int requestCode, List deniedPermissions) {

    }

    @Override
    public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {

    }

}
