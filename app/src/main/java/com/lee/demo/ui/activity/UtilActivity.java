package com.lee.demo.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;

import cn.lee.cplibrary.util.StringUtil;

public class UtilActivity extends BaseActivity {

    private TextView tvPhone;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_util;
    }

    @Override
    public String getPagerTitle() {
        return null;
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        tvPhone = findViewById(R.id.tv_phone);
        tvPhone.setText(StringUtil.hideMiddleFourNumber("18551815425"));
    }

    @Override
    protected void initData() {

    }


}
