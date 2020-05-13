package com.lee.demo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.CheckPermissionsActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.List;

import cn.lee.cplibrary.util.SharedPreferencesUtils;
import cn.lee.cplibrary.util.SystemBarUtils;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;


/**
 * @author ChrisLee
 */
public class SplashActivity extends CheckPermissionsActivity {
    public static final String SP_NAME_FIRSTENTER = "sp_firstenter";
    public static final String KEY_FIRSTENTER = "is_first_enter";
    RelativeLayout rlSplash;
    ViewPager viewPager;
    RelativeLayout rlEnterMian;
    private int[] images = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
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
        rlSplash = findViewById(R.id.rl_splash);
        viewPager = findViewById(R.id.guide_vp);
        rlEnterMian = findViewById(R.id.rl_enter_mian);
        if( isFirstEnter()){
            permissionUtil.requestPermissions(this, REQUEST_CODE, permissionArray);
        }else{
            initOther();
        }
    }

    @Override
    protected void init() {
        SystemBarUtils.setFullScreenNoText(getSelfActivity());
        super.init();
    }

    private void initOther() {
        rlEnterMian.setVisibility(View.GONE);
        if (isFirstEnter()) {
            rlSplash.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            initViewPager();
        } else {  // 自动登陆
            rlSplash.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            autoEnter();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
    }

    private void initViewPager() {
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView item = new ImageView(getSelfActivity());
                item.setScaleType(ImageView.ScaleType.FIT_XY);
                item.setImageBitmap(BitmapUtils.readBitMap(SplashActivity.this, images[position]));//避免出现OOM情况
                container.addView(item);
                return item;
            }

            @Override
            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }

        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (images.length - 1)) {
                    rlEnterMian.setVisibility(View.VISIBLE);
                    rlEnterMian.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferencesUtils
                                    .putShareValue(getSelfActivity(),
                                            SP_NAME_FIRSTENTER,
                                            KEY_FIRSTENTER,
                                            false);
                            jump(MainActivity.class);
                        }
                    });
                } else {
                    rlEnterMian.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 自动登录
     */
    private void autoEnter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jump(MainActivity.class);
            }
        }, 1000);

    }


    /**
     * @return :true:第一次进入，false，非第一次进入app
     */
    private boolean isFirstEnter() {
        boolean isFirst = SharedPreferencesUtils.getShareBoolean(
                getSelfActivity(), SP_NAME_FIRSTENTER,
                KEY_FIRSTENTER, true
        );
        return isFirst;
    }

    private void jump(Class cls) {
        jumpActivity(cls);
        finishCurrentActivity();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishCurrentActivity();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void granted(Object o, int i) {
        initOther();
    }

    @Override
    public void denied(Object source, int requestCode, List deniedPermissions) {
        initOther();
    }
}
