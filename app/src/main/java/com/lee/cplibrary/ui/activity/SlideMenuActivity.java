package com.lee.cplibrary.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.widget.easyswipemenulibrary.EasySwipeMenuLayout;
import cn.lee.cplibrary.widget.recycler.ScrollTopPoiLinearLayoutManager;

/**
 * 滑动点击删除Demo
 */
public class SlideMenuActivity extends SwipeBackActivity {

    private RecyclerView recyclerView;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public String getPagerTitle() {
        return "滑动点击删除";
    }

    @Override
    public String getPagerRight() {
        return null;
    }


    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        findViewById(R.id.iv_to_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected void initData() {
        initRecyclerView();
    }


    // 1、recyclerView.setNestedScrollingEnabled(false);解决使用NestedScrollView嵌套RecyclerView时,滑动卡顿
    // 原因：setNestedScrollingEnabled(true),是支持嵌套滚动的,即当它recyclerView嵌套在NestedScrollView中时,默认会随着NestedScrollView滚动而滚动,放弃了自己的滚动.
    // 所以给我们的感觉就是滞留、卡顿.主动将该值置false可以有效解决该问题
    // 2、recyclerView.setFocusableInTouchMode(false);  解决  使用NestedScrollView嵌套RecyclerView时,每次打开界面都是定位在RecyclerView在屏幕顶端,列表上面的布局都被顶上去了.
    // 原因：RecyclerView抢占了焦点,自动滚动导致的
    private void initRecyclerView() {
        List<String> totaList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            totaList.add("三国演义" + i);
        }
        recyclerView.setLayoutManager(new ScrollTopPoiLinearLayoutManager(getSelfActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusableInTouchMode(false);
        SlideAdapter adapter = new SlideAdapter(this, totaList, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public class SlideAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private List<String> totalList;
        private RecyclerView mRecyclerView;

        public SlideAdapter(Context context, List<String> totalList, RecyclerView mRecyclerView) {
            super(R.layout.item_slide_menu, totalList);
            this.totalList = totalList;
            this.mContext = context;
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final String bean) {
            helper.setText(R.id.tv_addvice_content, "我是内容" + bean);
            final int position = helper.getLayoutPosition();
            helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(bean, position);
                    EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es_menu_layout);
                    easySwipeMenuLayout.resetStatus();
                }
            });
        }


        public void refresh(List<String> mList, boolean isClear) {
            if (isClear) {
                totalList.clear();
            }
            if (mList != null && mList.size() > 0) {
                totalList.addAll(mList);
            }
            notifyDataSetChanged();
        }

        //此处只是逻辑删除：并未从服务器上删除，存在一个bug：当全部数据都删除完时，然后再下拉刷新，则不能上拉加载了。。。。。。。未解决
        public void removeItem(String id, int position) {
            totalList.remove(position);
            notifyItemRemoved(position);  //删除动画
            notifyDataSetChanged();
            if (totalList.size() <= 0) {
                LogUtil.i("", this, "空啦拉拉");
                toast("空啦拉拉");
            }
        }
    }

}
