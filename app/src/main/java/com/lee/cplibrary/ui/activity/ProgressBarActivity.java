package com.lee.cplibrary.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.SwipeBackActivity;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.timer.ScheduledHandler;
import cn.lee.cplibrary.util.timer.ScheduledTimer;
import cn.lee.cplibrary.widget.loadbutton.LoadingButton;
import cn.lee.cplibrary.widget.loadbutton.LoadingView;
import cn.lee.cplibrary.widget.progressbar.CircleProgressBarView;
import cn.lee.cplibrary.widget.progressbar.HorizontalProgressBar;
import cn.lee.cplibrary.widget.progressbar.LoadingCircleView;
import cn.lee.cplibrary.widget.progressbar.LoadingLineView;
import cn.lee.cplibrary.widget.progressbar.ProductProgressBar;

/**
 * 1、CircleProgressBarView的方法有2租，每组组内方法必须全部使用，组间不能混用
 * 例如用了组2的setCurrentProgress方法后不能使用组1中任何一个方法
 * （1）第一组：适用立即显示当前的百分比的情况，例如：手机剩余内存
 * startProgressAnimation  开始动画
 * pauseProgressAnimation  暂停动画  onActivity的onPause方法中调用
 * stopProgressAnimation  停止动画 onActivity的onDestroy方法中调用
 * resumeProgressAnimation 开始上次暂停的动画 onActivity的onResume方法中调用
 * setProgressWithAnimation 设置进度值
 * setProgressListener 设置进度监听
 * （2）第二组：适用实时显示进入 eg：下载百分比
 * setCurrentProgress 实时进度，适用于下载进度回调时候之类的场景
 * <p>
 * 2、HorizontalProgressBar 的方法有2租 : 类似CircleProgressBarView 组间不能混用
 * (1)setProgressWithAnimation 设置进度条带动画效果
 * startProgressAnimation 开启动画
 * pauseProgressAnimation 暂停动画
 * resumeProgressAnimation  恢复动画
 * stopProgressAnimation 停止动画
 * setProgressListener 回调监听事件
 * 一般只使用 setProgressWithAnimation和setProgressListener即可
 * (2) setCurrentProgress  实时显示进度 适用下载进度等等
 * 3、ProductProgressBar 和 HorizontalProgressBar实现原理基本一样，具体可能使用不多，到时候根据具体需求
 * 再封装吧
 * <p>
 * 4、LoadingLineView
 * startLoading  开始动画
 * stopLoading  结束动画
 * 5、LoadingCircleView
 * setLoadingViewListener :动画结束后的监听
 * loadingStart：开始加载
 * loadingSuccessful ：加载成功
 * loadingFailed：加载失败
 * 6、普通有阴影的加载loading
 * CpComDialog.showProgressDialog() 显示loading
 * CpComDialog.closeProgressDialog() 关闭loading
 * 7、LoadingButton
 * setResetAfterFailed  加载失败后 按钮是否恢复到初始状态
 * setAnimationEndListener 动话结束监听（有成功、失败状态）
 * startLoading  开始动画
 * loadingSuccessful  加载成功
 * loadingFailed 加载失败
 */
public class ProgressBarActivity extends SwipeBackActivity implements View.OnClickListener {

    CircleProgressBarView circleView, circleView1;
    HorizontalProgressBar horizontalBar, horizontalBar1;
    ProductProgressBar productProgressBar;
    LoadingCircleView loadingView;
    TextView textView;
    LoadingLineView loadingLineView;
    private ScheduledTimer scheduledTimer;
    private LoadingButton btnLogin;

    @Override
    protected SwipeBackActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        initView();
        initData();
    }

    private void initData() {
        //productProgressBar
        productProgressBar.setProgress(60).setProgressListener(new ProductProgressBar.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {
                LogUtil.i("", "", "currentProgressListener：" + currentProgress);
            }
        });
        //loadingView
        loadingView.setLoadingViewListener(new LoadingCircleView.AnimationFinishListener() {
            @Override
            public void onAnimationFinished(LoadingCircleView.AnimationType animationType) {
                if (animationType == LoadingCircleView.AnimationType.SUCCESSFUL) {//成功
                    LogUtil.i("", "", "loadingView----成功");

                } else {
                    LogUtil.i("", "", "loadingView----失败");
                }
            }
        });
        //btnLogin
        btnLogin.setResetAfterFailed(true);
        btnLogin.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
            @Override
            public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                if (animationType == LoadingButton.AnimationType.SUCCESSFUL) {
                    ToastUtil.showToast(getSelfActivity(), "成功");
                } else {
                    ToastUtil.showToast(getSelfActivity(), "失败");
                }
            }
        });
    }

    private void initView() {
        circleView = (CircleProgressBarView) findViewById(R.id.circle_progress_view);
        circleView1 = (CircleProgressBarView) findViewById(R.id.circle_progress_view1);
        horizontalBar = (HorizontalProgressBar) findViewById(R.id.horizontal_progress_view);
        horizontalBar1 = (HorizontalProgressBar) findViewById(R.id.horizontal_progress_view1);
        productProgressBar = (ProductProgressBar) findViewById(R.id.product_progress_view);
        loadingView = (LoadingCircleView) findViewById(R.id.loading_view);
        loadingLineView = (LoadingLineView) findViewById(R.id.loading_line_view);
        textView = (TextView) findViewById(R.id.progress_tv);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_success).setOnClickListener(this);
        findViewById(R.id.btn_fail).setOnClickListener(this);
        btnLogin = (LoadingButton) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        setCircle();//必须在onCreate中调用否则 resumeProgressAnimation();、pauseProgressAnimation、stopProgressAnimation会报空指针
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //圆形进度条
                setCircle();
                circleView1.setCurrentProgress(10);
                //设置水平进度条
                setHorizontalBar();
                productProgressBar.setProgress(100);
                //3种loading
                loadingLineView.startLoading();
                loadingView.loadingStart();
                showProgressDialog();

                break;
            case R.id.btn_success:
                loadingView.loadingSuccessful();
                loadingLineView.stopLoading();
                btnLogin.loadingSuccessful();//加载成功
                break;
            case R.id.btn_fail:
                loadingView.loadingFailed();
                loadingLineView.stopLoading();
                btnLogin.loadingFailed();//加载失败
                break;
            case R.id.btn_login:
                btnLogin.startLoading();
                break;
            default:
                break;
        }
    }

    /**
     * 模拟网络加载3s结束，让loading显示3s后消失
     */
    private void showProgressDialog() {
        CpComDialog.showProgressDialog(getSelfActivity(), "加载中");
        scheduledTimer = new ScheduledTimer(new ScheduledHandler() {
            @Override
            public void post(int times) { //time是线程到目前为止，执行的时间，单位s

            }

            @Override
            public void end() { //线程结束
                CpComDialog.closeProgressDialog();
            }
        }, 0, 1000, 3);
        scheduledTimer.start();
    }

    private void setHorizontalBar() {
        //horizontalBar
        horizontalBar.setProgressWithAnimation(100).setProgressListener(new HorizontalProgressBar.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {
                LogUtil.i("", "", "horizontalBar的当前进度：" + currentProgress);
            }
        });
        //horizontalBar.startProgressAnimation();//可以不需要哦
        //horizontalBar1
        horizontalBar1.setCurrentProgress(50);
    }

    private void setCircle() {
        //circleView
        circleView.setProgressWithAnimation(60).startProgressAnimation();
        circleView.setProgressListener(new CircleProgressBarView.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {
                textView.setText("当前进度：" + currentProgress);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        circleView.resumeProgressAnimation();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPause() {
        super.onPause();
        circleView.pauseProgressAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        circleView.stopProgressAnimation();
        if (scheduledTimer != null) { //取消线程
            scheduledTimer.cancel();
        }

    }
}

