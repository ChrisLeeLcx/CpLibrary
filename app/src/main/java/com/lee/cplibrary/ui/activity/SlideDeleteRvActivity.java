package com.lee.cplibrary.ui.activity;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;

/**
 * 滑动删除Demo
 */
public class SlideDeleteRvActivity extends SwipeBackActivity {

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slide_delete_rv;
    }

    @Override
    public String getPagerTitle() {
        return "滑动删除";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initData() {

    }


}
