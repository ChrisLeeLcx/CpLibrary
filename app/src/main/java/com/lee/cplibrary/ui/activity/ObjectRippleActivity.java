package com.lee.cplibrary.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_ripple);

        initObjectRippleView();
        findViewById(R.id.btn_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击了");
            }
        });
    }
}
