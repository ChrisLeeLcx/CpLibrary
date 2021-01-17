package com.lee.demo.util.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lee.demo.R;

import cn.lee.cplibrary.util.JsonUtils;
import cn.lee.cplibrary.util.ScreenUtil;

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
    private OCRDemoActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrdemo);
        activity = this;
        initView();
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
//                SLicencePlateActivity.startActivityForResult(activity);

                String s="{\"cardNumber\":\"341623199305012406\",\"birth\":\"1993-05-01\",\"name\":\"李翌侠\",\"sex\":\"女\",\"nation\":\"汉\",\"address\":\"安徽省利辛县纪王场乡永丰村西李庄31户\"}";
                IdCardFrontBean frontBean = new Gson().fromJson(s,  IdCardFrontBean.class);

                tvResult.setText("身份证正面:" + frontBean.getCardNumber() );
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
            String s="{\n" +
                    "    \"cardNumber\":\"341623199305012406\",\n" +
                    "    \"birth\":\"1993-05-01\",\n" +
                    "    \"name\":\"李翌侠\",\n" +
                    "    \"sex\":\"女\",\n" +
                    "    \"nation\":\"汉\",\n" +
                    "    \"address\":\"安徽省利辛县纪王场乡永丰村西李庄31户\"\n" +
                    "}";
             IdCardFrontBean frontBean = new Gson().fromJson(s,  IdCardFrontBean.class);

            tvResult.setText("身份证正面:" + frontBean.getCardNumber() );
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_ID_CARD_BACK && resultCode == RESULT_OK) {//身份证反面
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            SResultBean.IdCardBackBean bean = new Gson().fromJson(result, SResultBean.IdCardBackBean.class);

            tvResult.setText("身份证反面:" + bean);
        }
        if (requestCode == BaseScannerActivity.REQUEST_CODE_DRIVING_LICENSE && resultCode == RESULT_OK) {//驾驶证
            String result = data.getStringExtra(BaseScannerActivity.KEY_DATA);
            SResultBean.DrivingLicenseBean bean = new Gson().fromJson(result, SResultBean.DrivingLicenseBean.class);
            tvResult.setText("驾驶证:" + bean);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
