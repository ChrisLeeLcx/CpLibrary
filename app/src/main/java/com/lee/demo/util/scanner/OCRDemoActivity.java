package com.lee.demo.util.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lee.demo.R;

/**
 * OCR识别Demo
 * 使用到的开源识别项目有：
 * 1、https://github.com/shouzhong/Scanner 二维码/条码识别、身份证识别、银行卡识别、车牌识别、图片文字识别、黄图识别、驾驶证（驾照）识别
 * 存在问题：
 * （1）车牌识别：对新能源车牌的识别率较低,对蓝牌识别率较高
 * Created by ChrisLee on 2020/12/30.
 */
public class OCRDemoActivity extends AppCompatActivity {
    private Button btn;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrdemo);
        initView();
    }

    private void initView() {
        btn = findViewById(R.id.btn);
        tvResult = findViewById(R.id.tv_result);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OCRDemoActivity.this,ScannerActivity.class));
            }
        });
    }
}
