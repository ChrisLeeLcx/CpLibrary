package com.lee.demo.ui.activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.util.gaode.loc.GaoDeLBSUtil;
import com.lee.demo.util.gaode.loc.LocationBean;

import cn.lee.cplibrary.util.system.AppUtils;

/**
 * 高德定位
 */
public class GDLocationActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_info;
    GaoDeLBSUtil lbsUtil;
    private LocationBean locBean;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gdlocation;
    }

    @Override
    public String getPagerTitle() {
        return "高德定位封装";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        tv_info = findViewById(R.id.tv_info);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
        lbsUtil = GaoDeLBSUtil.getInstance(getSelfActivity());
        lbsUtil. setBaseApp(baseApplication).setShowErrorDialog(true);
        lbsUtil.requestPer(this, false);//requestPer与startLocation 不能同时调用，否则权限检测结果会出错,此处需要注释掉
        startLocation(false);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                startLocation(true);
                break;
            case R.id.btn1:
                String msg = AppUtils.checkLocationEnabled(getSelfActivity()) + "-" + AppUtils.isLocationEnabled(getSelfActivity());
                toast(msg);
                break;
            case R.id.btn2:
                toast(lbsUtil.isLocatedSuccess()+"");
                break;
            case R.id.btn3:
                AppUtils.openGpsSettings(getSelfActivity());
                break;
            case R.id.btn4:
                toast(lbsUtil.isNeedLocServiceOn()+"");
                break;
            default:
                break;
        }

    }

    //----------------定位--------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startLocation(boolean isLoading) {
        lbsUtil.startLocation(getSelfActivity(), isLoading).setCallBack(new GaoDeLBSUtil.GetLocationCallBack() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                locBean = baseApplication.getLocationBean();
                tv_info.setText("定位数据展示\n"+locBean.getAddress());
            }

            @Override
            public void onLocationFail(AMapLocation location) {
                lbsUtil.showLocationErrorResult(location);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        lbsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GaoDeLBSUtil.getInstance(this).stopLocation();
    }

}
