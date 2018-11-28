package com.lee.demo.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.lee.cplibrary.util.ActivityHelper;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.SystemBarUtils;
import cn.lee.cplibrary.util.SystemUtil;
import cn.lee.cplibrary.util.ToastUtil;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */


/**
 * 1. 处理公共逻辑
 * 2. 简化代码
 *
 * @author ChrisLee
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract BaseActivity getSelfActivity();

    /**
     * 获取布局资源id
     */
    protected abstract int getLayoutResId();

    /**
     * 获取页面标题
     */
    public abstract String getPagerTitle();

    /**
     * 获取页面标题 右侧按钮文案
     */
    public abstract String getPagerRight();

    /**
     * 2 初始化View : 查找控件,设置监听器
     */
    public abstract void initView();

    /**
     * 4 初始化数据: 给控件设置显示的内容
     */
    protected abstract void initData();

    protected BaseApplication baseApplication;
    public LayoutInflater inflater;




    /**
     * 两次点击间隔不能少于1000ms
     */
    private static final int MIN_DELAY_TIME= 1000;
    private static long lastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtil.d("", this, "onCreate");
        //1、初始化公共的设置
        init();
        //2. 查找控件  initView
        initView();
        //3. 设置数据  initData
        initData();
        // 4 处理公共逻辑
        dealCommon();
    }


//---------------------------------------------------标题栏的处理----------------------------------------------

    /**
     * 5处理相同逻辑
     * 比如返回键(注意判空)
     */
    protected void dealCommon() {
        // 左侧返回按钮
        LinearLayout backLl = getSelfActivity().findViewById(
                R.id.ll_title_back);
        ImageView ivTitleLeft = getSelfActivity().findViewById(
                R.id.iv_title_left);
        if (backLl != null) {
            if (isShowLeftBackBtn()) {
                backLl.setVisibility(View.VISIBLE);
                if (getTitleLeftDrawableResId() > 0) {
                    ivTitleLeft.setBackground(getResources().getDrawable(getTitleLeftDrawableResId()));
                }
                backLl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finishCurrentActivityWhithAnim();
                        SystemUtil.closeKeyboard(getSelfActivity());
                    }
                });
            } else {
                backLl.setVisibility(View.GONE);
            }
        }
//        中间标题
        TextView titleTv = getSelfActivity().findViewById(
                R.id.tv_title);
        if (titleTv != null && !ObjectUtils.isEmpty(getPagerTitle())) {
            titleTv.setText(getPagerTitle());
        }

        //右侧标题
        TextView tvRight = getSelfActivity().findViewById(
                R.id.tv_right);
        if (tvRight != null && !ObjectUtils.isEmpty(getPagerRight())) {
            tvRight.setText(getPagerRight());
            tvRight.setVisibility(View.VISIBLE);
        } else {
            if (tvRight != null) {
                tvRight.setVisibility(View.GONE);
            }
        }
        //右侧按钮
        LinearLayout llTitleRight = getSelfActivity().findViewById(
                R.id.ll_title_right);
        ImageView ivTitleRight = getSelfActivity().findViewById(
                R.id.iv_title_right);
        if (llTitleRight != null) {
            if (isShowRightBtn()) {
                llTitleRight.setVisibility(View.VISIBLE);
                if (getTitleRightDrawableResId() > 0) {
                    ivTitleRight.setBackground(getResources().getDrawable(getTitleRightDrawableResId()));
                }
            } else {
                llTitleRight.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 标题栏左侧是否显示返回按钮
     */
    protected boolean isShowLeftBackBtn() {
        return true;
    }

    /**
     * 标题栏右侧是否显示返回按钮
     */
    protected boolean isShowRightBtn() {
        return false;
    }

    /**
     * 获取标题栏右侧按钮的背景图
     * 子类重写本方法的充分不必要条件是isShowRightBtn()返回true，
     *
     * @return :类型@DrawableRes， -1说明没有设置图片，
     */
    protected int getTitleRightDrawableResId() {
        return -1;
    }

    /**
     * 获取标题栏左侧按钮的背景图
     * 子类重写本方法的充分不必要条件是isShowLeftBackBtn()返回true，
     *
     * @return :类型@DrawableRes， -1说明没有设置图片，
     */
    protected int getTitleLeftDrawableResId() {
        return -1;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityHelper.getInstance().showAllActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void testEventBus(String msg) {
    }
    public BaseApplication getBaseApplication() {
        return baseApplication;
    }

    protected void init() {
        EventBus.getDefault().register(this);
        baseApplication = (BaseApplication) getApplication();
        ActivityHelper.getInstance().addActivity(getSelfActivity());
        SystemBarUtils.myStatusBar(getSelfActivity(), true);
        setContentView(getLayoutResId());
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 键盘：隐藏，标题栏不动，中间部分顶上去

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    }
    /*****************************************常用方法*******************************************************/

    public void toast(String msg) {
        ToastUtil.showToast(getSelfActivity(), msg);
    }


    /*****************************************页面跳转方法*******************************************************/
    /**
     * 有动画跳转
     */
    public void  jumpActivity(Class cls) {
        jumpActivity(null, cls);
    }

    public void jumpActivity(Intent intent) {
        jumpActivity(intent, null);
    }

    /**
     * 有动画跳转
     */
    public void jumpActivity(Intent intent, Class cls) {
        if (null == intent) {
            startActivity(new Intent(getSelfActivity(), cls));
        } else {
            if (null != cls) {
                intent.setClass(getSelfActivity(), cls);
            }
            startActivity(intent);
        }
        activityEnterAnim();
    }


    /**
     * 适配5.0之后转场动画
     *
     * @param cls
     * @param options 不能为null
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void jumpActivity(Class cls, @Nullable Bundle options) {
        jumpActivity(null, cls, options);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void jumpActivity(Intent intent, Class cls, @Nullable Bundle options) {
        if (null == intent) {
            startActivity(new Intent(getSelfActivity(), cls), options);
        } else {
            intent.setClass(getSelfActivity(), cls);
            startActivity(intent, options);
        }
    }

    /**
     * finish当前activity，没有动画，用在进入下个页面，本页面消失
     */
    public void finishCurrentActivity() {
        ActivityHelper.getInstance().finishActivity(
                getSelfActivity().getClass());
    }

    /**
     * @fun： finish当前activity，有动画，用来退出当前页面，回到上一个页面
     * @author: ChrisLee at 2017-5-8 上午10:48:12
     */
    public void finishCurrentActivityWhithAnim() {
        ActivityHelper.getInstance().finishActivity(
                getSelfActivity().getClass());
        activityExitAnim();
    }

    public void activityEnterAnim() {
        overridePendingTransition(R.anim.cp_push_right_in, R.anim.cp_push_left_out);
    }

    public void activityExitAnim() {
        overridePendingTransition(R.anim.cp_push_left_in, R.anim.cp_push_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishCurrentActivityWhithAnim();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
