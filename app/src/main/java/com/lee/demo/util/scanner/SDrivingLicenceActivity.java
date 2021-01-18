package com.lee.demo.util.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import cn.lee.cplibrary.util.ScreenUtil;

/**
 * 驾驶证识别
 */
public class SDrivingLicenceActivity extends BaseScannerActivity {

    @Override
    protected BaseScannerActivity getSelfActivity() {
        return this;
    }

    @Override
    protected BaseViewFinder getViewFinder() {
        BaseViewFinder finder = new BaseViewFinder(this);
        finder.setWidthRatio(0.7f);
        finder.setLeftOffset(ScreenUtil.dp2px(getSelfActivity(),100));
        return finder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView.setEnableDrivingLicense(true);
        tvType.setText("机动车驾驶证");

    }

//    ------------开启扫描---------
    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, SDrivingLicenceActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_DRIVING_LICENSE, null);


    }
}
