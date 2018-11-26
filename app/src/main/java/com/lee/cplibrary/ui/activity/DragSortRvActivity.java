package com.lee.cplibrary.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;
import com.lee.cplibrary.util.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.lee.cplibrary.util.DrawableUtil;
import cn.lee.cplibrary.widget.recycler.RvDragSortItemTouchHelper;

public class DragSortRvActivity extends SwipeBackActivity {

    private RecyclerView recyclerView;
    private PhotoAddAdapter adapter;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        String[] imgs = getResources().getStringArray(
                R.array.img_src_data);
         List<PhotoBean> totaList = new ArrayList<>();
        totaList.add(new PhotoBean(true,imgs[0]));
        totaList.add(new PhotoBean(false,imgs[1]));
        totaList.add(new PhotoBean(false,imgs[2]));
        totaList.add(new PhotoBean(false,imgs[3]));
        totaList.add(new PhotoBean(false,imgs[4]));
        totaList.add(new PhotoBean(false,imgs[5]));
        totaList.add(new PhotoBean(false,imgs[6]));

        recyclerView.setLayoutManager(new GridLayoutManager(getSelfActivity(), 2));
        adapter = new PhotoAddAdapter(this, totaList, recyclerView);
        recyclerView.setAdapter(adapter);
        RvDragSortItemTouchHelper helper = RvDragSortItemTouchHelper.getInstance(getSelfActivity(), recyclerView, adapter, totaList, new RvDragSortItemTouchHelper.OnItemMovedListener() {
            @Override
            public void onItemMoved(int fromPosition, int toPosition) {
                adapter.refresh(fromPosition, toPosition);
            }
        }, 0);
        helper.attachToRecyclerView(recyclerView);
    }

    public class PhotoAddAdapter extends BaseQuickAdapter<PhotoBean, BaseViewHolder> {
        private List<PhotoBean> totalList;
        private Activity activity;
        private RecyclerView mRecyclerView;


        public PhotoAddAdapter(Activity activity, List<PhotoBean> totalList, RecyclerView mRecyclerView) {
            super(R.layout.item_add_photo, totalList);
            this.totalList = totalList;
            this.activity = activity;
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final PhotoBean bean) {
            ImageView ivSelect = helper.getView(R.id.iv_select);
            BitmapUtils.displayImageFromUrl(mContext, bean.getPath(), ivSelect);
            if (bean.isCover()) {
                helper.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.iv_cover).setVisibility(View.GONE);
            }
        }

        public void refresh(int fromPosition, int toPosition) {
            setCover();
            notifyItemMoved(fromPosition, toPosition);
        }

        public void refresh() {
            setCover();
            notifyDataSetChanged();
        }

        private void setCover() {
            if (totalList.size() > 0) {
                for (int i = 0; i < totalList.size(); i++) {
                    totalList.get(i).setCover(false);
                }
                totalList.get(0).setCover(true);
            }
        }
    }

    public class PhotoBean {
        private boolean isCover; //是否是封面
        private String path;//图片网络地址

        public PhotoBean(boolean isCover, String path) {
            this.isCover = isCover;
            this.path = path;
        }

        public boolean isCover() {
            return isCover;
        }

        public void setCover(boolean cover) {
            isCover = cover;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
