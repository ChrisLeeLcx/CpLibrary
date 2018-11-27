package cn.lee.cplibrary.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * function: 继承LinearLayoutManager
 * 当调用RecyclerView的方法smoothScrollToPosition(int position)时候：缓慢滑动position位置并置顶，
 */

public class ScrollTopPoiLinearLayoutManager extends LinearLayoutManager {
    public ScrollTopPoiLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollTopPoiLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollTopPoiLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        TopPoiLinearSmoothScroller linearSmoothScroller =
                new TopPoiLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}