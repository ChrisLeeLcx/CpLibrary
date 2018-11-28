package com.lee.demo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.lee.demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.lee.cplibrary.util.ToastUtil;

/**
 * function:Fragment基类
 * @author ChrisLee
 * @date 2018/7/24
 */

public abstract class BaseFragment extends Fragment {
    protected BaseActivity activity;
    protected BaseApplication baseApplication;
    protected View rootView;
    /**
     * 是否加载完成,当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;



    /**
     * 获取布局资源id
     */
    protected abstract int getLayoutResId();

    /**
     * 1、onCreateView中初始化View,和监听
     */
    protected abstract void initView();

    /**
     * 3、onCreateView 中接收到的从其他地方传递过来的参数
     */
    protected abstract void receiveData(Bundle arguments);

    protected abstract BaseFragment getSelfFragment();

    /**
     * 4 onActivityCreated中子类在此方法中实现数据的初始化
     */
    public abstract void initData();


    /*********生命周期*********/


    @Override
    public void onAttach(Context context) {//1当前Fragment与Activity关联
        super.onAttach(context);
        activity = (BaseActivity) getActivity();
        baseApplication = (BaseApplication) activity.getApplication();
//        LogUtil.e("",this,"onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {//2
        super.onCreate(savedInstanceState);
//        LogUtil.e("",this,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//3
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EventBus.getDefault().register(getSelfFragment());

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResId(), container, false);
//            ButterKnife.bind(getSelfFragment(), rootView);
            receiveData(getArguments());
            initView();
            mIsPrepare = true;
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
//        LogUtil.e("",this,"onCreateView");
//        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /*
      * 当Activity初始化之后,该方法才执行,可以在这里进行一些数据的初始化操作
      */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//4
        super.onActivityCreated(savedInstanceState);
        initData();
//        LogUtil.e("",this,"onActivityCreated");
    }
    @Override
    public void onStart() {//4-1
        super.onStart();
//        LogUtil.e("",this,"onStart");

    }

    @Override
    public void onResume() {//4-2
        super.onResume();
//        LogUtil.e("",this,"onResume");

    }

    @Override
    public void onDestroyView() {//5
        super.onDestroyView();
//        ButterKnife.unbind(getSelfFragment());
        EventBus.getDefault().unregister(getSelfFragment());
//        LogUtil.e("",this,"onDestroyView");
    }

    @Override
    public void onDestroy() {//6
        super.onDestroy();
//        LogUtil.e("",this,"onDestroy");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void testEventBus(String msg) {
    }

    /**
     * 公共初始化
     **/
//    private void init() {
//    }

    /*****************************************常用方法*******************************************************/

    public void toast(String msg) {
        ToastUtil.showToast(activity, msg);
    }

    protected View findViewById(int id) {
        if (rootView == null) {
            return null;
        }
        return rootView.findViewById(id);
    }

    /**
     * 有动画跳转
     */
    public void jumpActivity(Class cls) {
        jumpActivity(null, cls);
    }

    /**
     * 有动画跳转
     */
    public void jumpActivity(Intent intent, Class cls) {
        if (null == intent) {
            startActivity(new Intent(activity, cls));
        } else {
            intent.setClass(activity, cls);
            startActivity(intent);
        }
        activityEnterAnim();
    }

    public void activityEnterAnim() {
        activity.overridePendingTransition(R.anim.cp_push_right_in, R.anim.cp_push_left_out);
    }

}
