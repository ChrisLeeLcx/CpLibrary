package com.lee.demo.ui.activity;

import android.graphics.Paint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;

import cn.lee.cplibrary.widget.rippleview.ObjectRippleView;

/**
 * 水波纹效果
 */
public class ObjectRippleActivity extends SwipeBackActivity {
    ObjectRippleView rippleview;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_object_ripple;
    }

    @Override
    public String getPagerTitle() {
        return "水波纹效果";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        initObjectRippleView();
        findViewById(R.id.btn_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击了");
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void initObjectRippleView() {
        rippleview = (ObjectRippleView) findViewById(R.id.rippleview);
        rippleview.setDuration(10000);
        rippleview.setStyle(Paint.Style.STROKE);
        rippleview.setColor(getResources().getColor(R.color.colorAccent));
        rippleview.setInterpolator(new DecelerateInterpolator());
        rippleview.start();
        rippleview.setSpeed(1000);
        rippleview.setInitialRadius(180);
        rippleview.setMaxRadius(400);
    }

}
