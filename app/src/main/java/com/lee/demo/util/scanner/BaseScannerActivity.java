package com.lee.demo.util.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lee.demo.R;
import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.Result;
import com.shouzhong.scanner.ScannerView;

import org.json.JSONObject;

import cn.lee.cplibrary.util.JsonUtils;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.SystemBarUtils;

/**
 * OCR识别Demo
 * 1、使用到的开源识别项目有：https://github.com/shouzhong/Scanner 二维码/条码识别、身份证识别、银行卡识别、车牌识别、图片文字识别、黄图识别、驾驶证（驾照）识别
 * 1、so资源只有arm格式的，ScannerDrivingLicenseLib和ScannerIdCard2Lib无arm64-v8a格式
 * 3、车牌识别：对新能源车牌的识别率较低,对蓝牌识别率较高，建议和百度识别混合使用
 * 4、身份证识别：建议使用ScannerIdCardLib即第一种识别方式
 * 5、银行卡识别：只能识别部分银行卡（卡号平面的）eg建设银行，卡号凸起的识别不了eg：中国银行
 * 6、驾驶证识别：识别率不高，识别速度较慢
 */

public abstract class BaseScannerActivity extends AppCompatActivity implements View.OnClickListener {
    protected ScannerView scannerView;
    protected TextView tvType;
    ImageButton ibBack, ibFlash, ibCamera;
    private Vibrator vibrator;//震动器

    public static final String KEY_DATA = "key_data";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_PATH = "key_path";

    public static final int REQUEST_CODE_TYPE_CODE = 0; // 二维码/条码
    public static final int REQUEST_CODE_ID_CARD_FRONT = 1;// 身份证人头面
    public static final int REQUEST_CODE_ID_CARD_BACK = 2;// 身份证国徽面
    public static final int REQUEST_CODE_BANK_CARD = 3;// 银行卡
    public static final int REQUEST_CODE_LICENSE_PLATE = 4;// 车牌
    public static final int REQUEST_CODE_DRIVING_LICENSE = 5; // 驾驶证

    protected abstract BaseViewFinder getViewFinder();

    protected abstract BaseScannerActivity getSelfActivity();

    /**
     * 获取布局资源id
     */
    protected int getLayoutResId() {
        return R.layout.activity_base_scanner_land;
    }

    private boolean hasResult = false;//是否有识别结果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarUtils.setFullScreenNoText(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        findViews();
        initialise();
    }

    private void findViews() {
        scannerView = findViewById(R.id.sv);
        tvType = findViewById(R.id.tv_type);
        ibBack = findViewById(R.id.ib_back);
        ibFlash = findViewById(R.id.ib_flash);
        ibCamera = findViewById(R.id.ib_camera);
        ibBack.setOnClickListener(this);
        ibFlash.setOnClickListener(this);
        ibCamera.setOnClickListener(this);
    }


    private void initialise() {
        scannerView.setShouldAdjustFocusArea(true);
        scannerView.setViewFinder(getViewFinder());
        scannerView.setSaveBmp(false);
        scannerView.setRotateDegree90Recognition(true);
        //设置使用规则
//        scannerView.setEnableQrcode(true);//是否启用二维码识别
//        scannerView.setEnableBarcode(true);//是否启用条码识别
//        scannerView.setEnableBankCard(true); //是否使用银行卡识别
//        scannerView.setEnableIdCard(true);//是否使用身份证识别
//        scannerView.setEnableIdCard2(true);//是否使用身份证识别（第二种方式）//无arm64-v8a
//        scannerView.setEnableLicensePlate(true);//使用车牌识别
//        scannerView.setEnableDrivingLicense(true); //是否使用驾驶证识别//无arm64-v8a
        scannerView.setCallback(new Callback() {
            @Override
            public void result(Result result) {
                LogUtil.i("", "识别结果：\n" + result.toString());
                hasResult = true;
                startVibrator();
                scannerView.restartPreviewAfterDelay(2000);
                Intent intent = new Intent();
                intent.putExtra(KEY_DATA, result.data);//识别结果
                intent.putExtra(KEY_TYPE, result.type);//识别类型
                intent.putExtra(KEY_PATH, result.path);//识别图片保存路径
                setResult(RESULT_OK, intent);
                finish();
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
        LogUtil.i("", hasResult + "---11111-");
        if (hasResult) {//解决报错
            LogUtil.i("", hasResult + "----");
            //若不是正常返回结果时候调用本方法会报错： Found activity ActivityRecord{dbbe80d u0 com.lee.demo/.util.scanner.OCRDemoActivity t949 f} in proc activity list using null instead of expected ProcessRecord{5e45447 28552:com.lee.demo/u0a145}
            scannerView.onPause();
        }

    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }

    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                getSelfActivity().finish();
                break;
            case R.id.ib_flash:
                ibFlash.setBackgroundResource(scannerView.isFlashOn() ? R.mipmap.flash_off : R.mipmap.flash_on);
                scannerView.toggleFlash();
                break;
            case R.id.ib_camera:
                break;
        }
    }


    public static SResultBean.IdCardFrontBean getIdCardFrontBean(String json) {
        JSONObject jsonObject = JsonUtils.formatterJsonObject(json);
        SResultBean.IdCardFrontBean bean = new SResultBean.IdCardFrontBean();
        if (jsonObject != null) {
            bean.setCardNumber(jsonObject.optString("cardNumber"));
            bean.setBirth(jsonObject.optString("birth"));
            bean.setName(jsonObject.optString("name"));
            bean.setSex(jsonObject.optString("sex"));
            bean.setNation(jsonObject.optString("nation"));
            bean.setAddress(jsonObject.optString("address"));
        }
        return bean;
    }
    public static SResultBean.IdCardBackBean getIdCardBackBean(String json) {
        JSONObject jsonObject = JsonUtils.formatterJsonObject(json);
        SResultBean.IdCardBackBean bean = new SResultBean.IdCardBackBean();
        if (jsonObject != null) {
            bean.setOrganization(jsonObject.optString("organization"));
            bean.setValidPeriod(jsonObject.optString("validPeriod"));
        }
        return bean;
    }
    public static SResultBean.DrivingLicenseBean getDrivingLicenseBean(String json) {
        JSONObject jsonObject = JsonUtils.formatterJsonObject(json);
        SResultBean.DrivingLicenseBean bean = new SResultBean.DrivingLicenseBean();
        if (jsonObject != null) {
            bean.setCardNumber(jsonObject.optString("cardNumber"));
            bean.setName(jsonObject.optString("name"));
            bean.setSex(jsonObject.optString("sex"));
            bean.setNationality(jsonObject.optString("nationality"));
            bean.setAddress(jsonObject.optString("address"));
            bean.setBirth(jsonObject.optString("birth"));
            bean.setFirstIssue(jsonObject.optString("firstIssue"));
            bean.set_class(jsonObject.optString("_class"));
            bean.setValidPeriod(jsonObject.optString("validPeriod"));
        }
        return bean;
    }
}
