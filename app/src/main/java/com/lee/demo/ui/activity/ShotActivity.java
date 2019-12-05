package com.lee.demo.ui.activity;

/**
 * 各种方式截图demo
 */

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;

import cn.lee.cplibrary.util.shot.ShotUtils;
import cn.lee.cplibrary.util.timer.TimeUtils;

public class ShotActivity extends BaseActivity implements View.OnClickListener {
    ScrollView scrollView;
    RecyclerView recyclerview;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shot;
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
        findViews();

    }

    private void findViews() {
        scrollView = findViewById(R.id.scrollView);
        recyclerview = findViewById(R.id.recyclerview);
        findViewById(R.id.btn1).setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Bitmap bitmap = ShotUtils.shotScrollView(scrollView);
                ShotUtils.saveBitmapToSdCard(getSelfActivity(), "",bitmap, TimeUtils.getCurTimeMillis() + "");
                break;
        }
    }

    @Override
    protected void initData() {

    }


}
