package com.lee.cplibrary.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;

import cn.lee.cplibrary.widget.ratingbar.MaterialRatingBar;

/**
 * 星星评价控件、RangSeekBar可拖动进度条
 */
public class OtherActivity extends SwipeBackActivity {

    private MaterialRatingBar rating_bar, material_rating_bar;
    private TextView tv_content;
    private Button btn;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        rating_bar = (MaterialRatingBar) findViewById(R.id.rating_bar);
        material_rating_bar = (MaterialRatingBar) findViewById(R.id.material_rating_bar);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_content.setText("上面大星星可滑动评分：分数是：" + Float.toString(rating_bar.getRating())
                        + "\n小星星用于展示评分,分数是：" + Float.toString(material_rating_bar.getRating())
                );
            }
        });
    }
}
