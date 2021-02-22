package com.lee.demo.util.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.lee.demo.R;

/**
 * 车牌识别
 */
public class SLicencePlateActivity extends BaseScannerActivity {

    @Override
    protected BaseScannerActivity getSelfActivity() {
        return this;
    }

    protected int getLayoutResId() {
        return R.layout.activity_base_scanner;
    }

    @Override
    protected BaseViewFinder getViewFinder() {
        BaseViewFinder finder = new BaseViewFinder(this);
        finder.setHeightRatio(0.75f);
        return finder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView.setEnableLicensePlate(true);//使用车牌识别
        tvType.setText("车辆蓝牌");
    }

    //    ------------开启扫描---------
    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, SLicencePlateActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_LICENSE_PLATE, null);


    }
}
