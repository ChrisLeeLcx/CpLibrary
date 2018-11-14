package cn.lee.cplibrary.widget.drawer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：jack on 2016/5/25 22:27.
 * 功能描述：自定义DrawerLayout,调整Drawerlayout事件拦截逻辑,
 * DrawerLayout去除阴影，阴影部分点击不关闭抽屉并可以触发相关view，抽屉侧滑可响应关闭
 * 主要应用Demo:见瓜子二手车app选择品牌后，打开相应品牌的车系抽屉，继续选择品牌，抽屉不关闭
 * @author jack
 */
public class NoShadowDrawerLayout extends DrawerLayout {

    public NoShadowDrawerLayout(Context context){
        this(context, null);
    }

    public NoShadowDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoShadowDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs,defStyle);
        setListener();
        //去除阴影
        setScrimColor(Color.TRANSPARENT);

    }

    /**
     * 抽屉关闭状态下：禁用手动滑动打开功能
     * 抽屉打开状态下：可以手动滑动关闭
     * ChrisLee
     */
    private void setListener() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {
                setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final float x = ev.getX();
                final float y = ev.getY();
                final View touchedView = findTopChildUnder((int) x, (int) y);
                //左滑抽屉：禁止点击阴影区域关闭抽屉
                if (touchedView != null && isContentView(touchedView)
                        && this.isDrawerOpen(GravityCompat.START)) {
                    return false;
                }
                //右滑抽屉：禁止点击阴影区域关闭抽屉
                if (touchedView != null && isContentView(touchedView)
                        && this.isDrawerOpen(GravityCompat.END)) {
                    return false;
                }
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断点击位置是否位于相应的View内
     * @param x
     * @param y
     * @return
     */
    public View findTopChildUnder(int x, int y) {
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    /**
     * 判断点击触摸点的View是否是ContentView(即是主页面的View)
     * @param child
     * @return
     */
    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    /**
     * 判断点击触摸点的View是否是DrawerView(即是侧边栏的View)
     * @param child
     * @return
     */
    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        if ((absGravity & Gravity.LEFT) != 0) {
            // This child is a left-edge drawer
            return true;
        }
        if ((absGravity & Gravity.RIGHT) != 0) {
            // This child is a right-edge drawer
            return true;
        }
        return false;
    }



}


