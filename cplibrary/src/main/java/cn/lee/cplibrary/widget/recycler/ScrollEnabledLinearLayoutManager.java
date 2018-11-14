package cn.lee.cplibrary.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * function: 可设置是否禁止RecycleView的滑动的LinearLayoutManager
 * @author ChrisLee
 * @date 2018/5/30
 */

public class ScrollEnabledLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public ScrollEnabledLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * 是否禁止RecycleView的滑动
     */
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
