package com.lee.cplibrary.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.lee.cplibrary.base.SwipeBackActivity;

import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.SystemUtil;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;

/**
 * 权限的Demo
 */
public class PermissionActivity extends AppCompatActivity implements PermissionProxy {

    private PermissionUtil permissionUtil;

    /*定位权限数组*/
    private String[] permissionArray = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.lee.cplibrary.R.layout.cp_layout_empty);
        permissionUtil = new PermissionUtil(this);
        //方式1
        permissionUtil.requestPermissions(this, 1, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
        //方式2
        if (!permissionUtil.shouldShowRequestPermissionRationale(this, 2, Manifest.permission.RECORD_AUDIO)) {
            permissionUtil.requestPermissions(this, 2, permissionArray);
        }
        LogUtil.i("", this, "Person_Demo");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void granted(Object source, int requestCode) {
        switch (requestCode) {
            case 1:
                LogUtil.i("", this, "granted-1");
                break;
            case 2:
                LogUtil.i("", this, "granted-2");
                break;
            default:
                break;
        }
    }

    @Override
    public void denied(Object source, int requestCode, List deniedPermissions) {
        switch (requestCode) {
            case 1:
                LogUtil.i("", this, "denied-1");
                for (int i = 0; i < deniedPermissions.size(); i++) {
                    if (!permissionUtil.shouldShowRequestPermissionRationale(this, 1, (String) deniedPermissions.get(i))) {
                        //说明用户拒绝了权限并且 设置 不再提示，则此时 引导用户至设置页手动授权
                        SystemUtil.jumpAppSettingInfo(this);
                        break;
                    }
                }
                break;
            case 2:
                LogUtil.i("", this, "denied-2");
                break;
            default:
                break;
        }
    }

    @Override
    public void rationale(Object source, int requestCode) {
        switch (requestCode) {
            case 1:
                LogUtil.i("", this, "rationale1");
                break;
            case 2:
                LogUtil.i("", this, "rationale2");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean needShowRationale(int requestCode) {
        return true;
    }


}
