package com.lee.cplibrary.ui.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lee.cplibrary.R;

import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.widget.statelayout.SampleActivity;
import cn.lee.cplibrary.widget.statelayout.StateLayout;

/**
 * StateLayout功能 ：是引用 第三方的项目
 * 链接：https://github.com/fingdo/stateLayout
 */
public class StateLayoutActivity extends AppCompatActivity implements View.OnClickListener{



    private StateLayout stateLayout;
    private RelativeLayout rlCustom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_layout);

        stateLayout = (StateLayout) findViewById(cn.lee.cplibrary.R.id.state_layout);
        rlCustom = (RelativeLayout) findViewById(cn.lee.cplibrary.R.id.rl_custom);


        //提前设置
        //设置空数据提示文字
        //stateLayout.setTipText(StateLayout.EMPTY, "empty tip text");
        //stateLayout.setTipText(StateLayout.EMPTY, R.string.empty_tip);
        //空数据提示图标
        //stateLayout.setTipImg(StateLayout.EMPTY, R.drawable.ic_state_empty);
        //stateLayout.setTipImg(StateLayout.EMPTY, getResources().getDrawable(R.drawable.ic_state_empty));


        //展示内容的界面
//        stateLayout.showContentView();
        //展示加载中的界面
//        stateLayout.showLoadingView();

        //展示没有网络的界面
//        stateLayout.showNoNetworkView();
        //展示超时的界面
//        stateLayout.showTimeoutView();
        //展示空数据的界面
//        stateLayout.showEmptyView();
        //展示错误的界面
//        stateLayout.showErrorView();
        //展示登录的界面
//        stateLayout.showLoginView();


        //显示加载界面
//        stateLayout.showLoadingView();

        //显示自定义界面
//        stateLayout.showCustomView();


        stateLayout.setUseAnimation(true);
//        stateLayout.setViewSwitchAnimProvider(new FadeScaleViewAnimProvider());
        stateLayout.setRefreshListener(new StateLayout.OnViewRefreshListener() {
            @Override
            public void refreshClick() {
                ToastUtil.showToast(StateLayoutActivity.this, "刷新");
            }

            @Override
            public void loginClick() {
                ToastUtil.showToast(StateLayoutActivity.this, "登录");
            }
        });

        findViewById(cn.lee.cplibrary.R.id.btn_content).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_empty).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_error).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_loading).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_loading_no_tip).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_time_out).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_not_network).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_login).setOnClickListener(this);
        findViewById(cn.lee.cplibrary.R.id.btn_custom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cn.lee.cplibrary.R.id.btn_content) {
            stateLayout.showContentView();
//                stateLayout.setTipText(StateLayout.EMPTY, "12345");
//                stateLayout.setTipImg(StateLayout.EMPTY, R.mipmap.ic_launcher);
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_empty) {
            stateLayout.showEmptyView();
//                stateLayout.setTipText(StateLayout.ERROR, "12345");
//                stateLayout.setTipImg(StateLayout.ERROR, R.mipmap.ic_launcher);
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_error) {
            stateLayout.showErrorView();
//                stateLayout.setTipText(StateLayout.LOADING, "12345");
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_loading) {
//            stateLayout.showLoadingView();//默认loading
//                stateLayout.setTipText(StateLayout.TIMEOUT, "12345");//1
//                stateLayout.setTipImg(StateLayout.TIMEOUT, R.mipmap.ic_launcher);//2
            //3
            ProgressBar progressBar = new ProgressBar(StateLayoutActivity.this);
            progressBar.setIndeterminateDrawable(getResources().getDrawable(cn.lee.cplibrary.R.drawable.cp_bg_loading));
            stateLayout.showLoadingView(progressBar, false);
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_loading_no_tip) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(cn.lee.cplibrary.R.color.cp_font6c);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 300);
            imageView.setLayoutParams(layoutParams);
            stateLayout.showLoadingView(imageView, false);
//                stateLayout.setTipText(StateLayout.TIMEOUT, "12345");
//                stateLayout.setTipImg(StateLayout.TIMEOUT, R.mipmap.ic_launcher);
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_time_out) {
            stateLayout.showTimeoutView();
//                stateLayout.setTipText(StateLayout.NOT_NETWORK, "12345");
//                stateLayout.setTipImg(StateLayout.NOT_NETWORK, R.mipmap.ic_launcher);
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_not_network) {
            stateLayout.showNoNetworkView();
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_login) {
            stateLayout.showLoginView();
        } else if (v.getId() == cn.lee.cplibrary.R.id.btn_custom) {
            View customView = LayoutInflater.from(this).inflate(cn.lee.cplibrary.R.layout.cp_layout_empty, null);
            stateLayout.showCustomView(customView);
        }
    }
}
