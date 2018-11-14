package cn.lee.cplibrary.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * function: 继承GridLayoutManager
 * 当调用RecyclerView的方法smoothScrollToPosition(int position)时候：缓慢滑动position位置并置顶，
 */
public class ScrollTopPoiGridLayoutManager extends GridLayoutManager {
    public ScrollTopPoiGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollTopPoiGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ScrollTopPoiGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        TopPoiLinearSmoothScroller linearSmoothScroller =
                new TopPoiLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
