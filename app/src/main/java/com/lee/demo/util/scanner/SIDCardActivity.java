package com.lee.demo.util.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import cn.lee.cplibrary.util.ScreenUtil;

/**
 * 身份证识别
 */
public class SIDCardActivity extends BaseScannerActivity {

    @Override
    protected BaseScannerActivity getSelfActivity() {
        return this;
    }

    @Override
    protected BaseViewFinder getViewFinder() {
        BaseViewFinder finder = new BaseViewFinder(this);
        finder.setWidthRatio(0.7f);
        finder.setLeftOffset(ScreenUtil.dp2px(getSelfActivity(), 100));
        return finder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView.setEnableIdCard(true);//是否使用身份证识别
//        scannerView.setEnableIdCard2(true);//是否使用身份证识别（第二种方式）
        if (getIntent().getBooleanExtra("isFront", true)) {
            tvType.setText("居民身份证正面");
        } else {
            tvType.setText("居民身份证反面");
        }
    }

    public static void startActivityForResultFront(Activity activity) {
        Intent intent = new Intent(activity, SIDCardActivity.class);
        intent.putExtra("isFront",true);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_ID_CARD_FRONT, null);
    }

    public static void startActivityForResultBack(Activity activity) {
        Intent intent = new Intent(activity, SIDCardActivity.class);
        intent.putExtra("isFront",false);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_ID_CARD_BACK, null);
    }
}
