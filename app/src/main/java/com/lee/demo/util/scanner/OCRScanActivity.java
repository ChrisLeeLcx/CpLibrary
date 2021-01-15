package com.lee.demo.util.scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.demo.R;
import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.Result;
import com.shouzhong.scanner.ScannerView;
import com.shouzhong.scanner.IViewFinder;

/**
 * Created by ChrisLee on 2020/12/30.
 */
public class OCRScanActivity extends AppCompatActivity {
    ScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrscan);
        scannerView = findViewById(R.id.sv);
//        scannerView.setViewFinder(new ScannerActivity.ViewFinder(this));
        scannerView.setSaveBmp(true);
        scannerView.setEnableZXing(true);
        scannerView.setEnableZBar(true);
        scannerView.setEnableBankCard(true);
        scannerView.setEnableIdCard(true);
        scannerView.setEnableLicensePlate(true);
        scannerView.setCallback(new Callback() {
            @Override
            public void result(Result result) {
//                tvResult.setText("识别结果：\n" + result.toString());
                scannerView.restartPreviewAfterDelay(2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.onPause();
    }
}
