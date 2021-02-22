package com.lee.demo.util.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lee.demo.R;

import java.util.List;

import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.video.CpVideoDialog;
import cn.lee.cplibrary.util.video.VideoRecordActivity;

/**
 * OCR识别Demo
 * 使用到的开源识别项目有：
 * 1、https://github.com/shouzhong/Scanner
 * Created by ChrisLee on 2020/12/30.
 */
public class OCRDemoActivity extends AppCompatActivity {
    private Button btn;
    private TextView tvResult;
    private OCRDemoActivity activity;
    /***********录像权限相关开始***********/
    private static PermissionUtil permissionUtil;
    public static final int REQUEST_CODE_PER = 500;//权限请求码
    private static final String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写
            Manifest.permission.READ_EXTERNAL_STORAGE,//读
            Manifest.permission.CAMERA,//相机权限
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrdemo);
        activity = this;
        initView();
        requestPermission(activity);

    }

    private void initView() {
        btn = findViewById(R.id.btn);
        tvResult = findViewById(R.id.tv_result);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OCRDemoActivity.this, ScannerActivity.class));
            }
        });

        findViewById(R.id.btn_licence_plate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//识别车牌
                SLicencePlateActivity.startActivityForResult(activity);
            }
        });
        findViewById(R.id.btn_id_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//识别身份证正面
                SIDCardActivity.startActivityForResultFront(activity);
            }
        });
        findViewById(R.id.btn_id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//识别身份证反面
                SIDCardActivity.startActivityForResultBack(activity);
            }
        });
        findViewById(R.id.btn_bank_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//识别银行卡
                SBankCardActivity.startActivityForResult(activity);


            }
        });
        findViewById(R.id.btn_driving_licence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//识别行驶证
                SDrivingLicenceActivity.startActivityForResult(activity);
                // 需要将app的build.gradle中 abiFilters 'armeabi', 'armeabi-v7a', 'arm64-v8a', 'x86_64', 'x86'
                // 换成 abiFilters 'armeabi', 'armeabi-v7a'才行，因为没有arm64-v8a库
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseScannerActivity.REQUEST_CODE_LICENSE_PLATE && resultCode == RESULT_OK) {//车牌
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            tvResult.setText("车牌:" + result);
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_BANK_CARD && resultCode == RESULT_OK) {//银行卡
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            tvResult.setText("银行卡:" + result);
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_ID_CARD_FRONT && resultCode == RESULT_OK) {//身份证正面
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            tvResult.setText("身份证正面:" + BaseScannerActivity.getIdCardFrontBean(result));
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_ID_CARD_BACK && resultCode == RESULT_OK) {//身份证反面
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            tvResult.setText("身份证反面:" + BaseScannerActivity.getIdCardBackBean(result));
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_DRIVING_LICENSE && resultCode == RESULT_OK) {//驾驶证
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            tvResult.setText("驾驶证:" + BaseScannerActivity.getDrivingLicenseBean(result));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //请求权限
    public void requestPermission(final Activity activity) {
        permissionUtil = new PermissionUtil(new PermissionProxy() {
            @Override
            public void granted(Object source, int requestCode) {

            }

            @Override
            public void denied(Object source, int requestCode, List deniedPermissions) {
            }

            @Override
            public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
                ToastUtil.showToast(activity, "需要的权限被禁止，请到设置中心设置权限");
            }

            @Override
            public void rationale(Object source, int requestCode) {

            }

            @Override
            public boolean needShowRationale(int requestCode) {
                return false;
            }
        });
        permissionUtil.requestPermissions(activity, REQUEST_CODE_PER, permissionArray);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
