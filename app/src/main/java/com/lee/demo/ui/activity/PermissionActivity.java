package com.lee.demo.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import cn.lee.cplibrary.util.AppUtils;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.timer.TimeUtils;

/**
 * 权限的Demo
 */
public class PermissionActivity extends AppCompatActivity implements PermissionProxy {
    private PermissionUtil permissionUtil;
    /*定位权限数组*/
    protected String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,//读
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写
            Manifest.permission.CAMERA,//照相机
            Manifest.permission.CALL_PHONE,//打电话
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.lee.cplibrary.R.layout.cp_layout_empty);
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(this, 2, permissionArray);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void granted(Object source, int requestCode) {
        switch (requestCode) {
            case 2:
                LogUtil.i("", this, "granted-2");
                break;
            default:
                break;
        }
    }

    @Override
    public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
        showGuideDialog(this,"是否开启权限");
    }

    @Override
    public void denied(Object source, int requestCode, List deniedPermissions) {
        switch (requestCode) {
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

    private void showGuideDialog(final Activity c, String msg) {
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
