package com.lee.demo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.lee.demo.R;

import java.io.IOException;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.widget.sign.view.HandWriteView;

public class SignActivity extends AppCompatActivity {


    private RelativeLayout layout_horizontal;
    private HandWriteView handWriteView;
    /**
     * 是否区域裁剪
     */
    private final boolean isCrop = true;
    /**
     * 签名保存的图片地址
     */
    private String signPath;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        layout_horizontal = findViewById(cn.lee.cplibrary.R.id.layout_horizontal);
        handWriteView = findViewById(cn.lee.cplibrary.R.id.handWriteView);
        ScreenUtil.rotate(this, layout_horizontal, 90);
        initData();
        findViewById(cn.lee.cplibrary.R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handWriteView.clear();
            }
        });
        findViewById(cn.lee.cplibrary.R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                ToastUtil.showToast(SignActivity.this, "保存");
            }

        });
    }

    private void initData() {
        //自己定义
        fileName = "sign-" + "id" + ".png";
//        signPath = CpConfig.IMG_CACHE_PATH+fileName;
        signPath =new CpConfig(this).getImgCachePath()+fileName;
    }

    /**
     * 保存签名
     */
    private void save() {
        if (handWriteView.isSign()) {
            try {
                if (isCrop) {
                    handWriteView.save(signPath, true, 10, false);
                } else {
                    handWriteView.save(signPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e("",e.getMessage());
            }
        } else {
            ToastUtil.showToast(this, "尚未签名");
        }
    }
}
