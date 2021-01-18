package com.lee.demo.util.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import cn.lee.cplibrary.util.ScreenUtil;

/**
 * 银行卡识别
 */
public class SBankCardActivity extends BaseScannerActivity {

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
        scannerView.setEnableBankCard(true);
        tvType.setText("银行卡");
    }

//    ------------开启扫描---------
    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, SBankCardActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_BANK_CARD, null);


    }
}
