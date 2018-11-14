package cn.lee.cplibrary.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * function: 可设置是否禁止RecycleView的滑动的GridLayoutManager
 * @author ChrisLee
 * @date 2018/5/30
 */

public class ScrollEnabledGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = false;//默认不可滑动

    public ScrollEnabledGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
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
