package com.lee.demo.ui.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ZoomTutorial;
import cn.lee.cplibrary.widget.imageview.photoview.PhotoView;

public class ImageZoomActivity extends SwipeBackActivity {

    private PhotoView ivLarge;
    private ImageView  ivSmall;
    private RecyclerView recyclerView;
    private View container;
    private List<String> list = new ArrayList<>();
    public static final String url = "https://static.firefoxchina.cn/img/201711/7_5a0ba48885d170.png";


    private void initRecyclerView() {
        list.add(url);
        list.add(url);
        list.add(url);
        list.add(url);
        list.add(url);
        list.add(url);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new CarImgAdapter(list, container, ivLarge));
    }

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_zoom;
    }

    @Override
    public String getPagerTitle() {
        return "图片点击放大";
    }

    @Override
    public String getPagerRight() {
        return null;
    }
    @Override
    public void initView() {
        ivLarge = (PhotoView) findViewById(R.id.iv_large);
        ivSmall = (ImageView) findViewById(R.id.iv_small);
        container = findViewById(R.id.container);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ivSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPagerAndZoom(container, ivSmall, -1, null, ivLarge);
                BitmapUtils.displayImageFromUrl(url, ivLarge);
            }
        });
        ivLarge.enable();
    }

    @Override
    protected void initData() {
        BitmapUtils.displayImageFromUrl(url, ivSmall);
        initRecyclerView();
    }


    public class CarImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private List<String> totalList;
        private View container;
        private ImageView iv_large;

        public CarImgAdapter(final List<String> totalList, final View container, final ImageView iv_large) {
            super(R.layout.item_img, totalList);
            this.totalList = totalList;
            this.container = container;
            this.iv_large = iv_large;
            setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    setViewPagerAndZoom(container, view.findViewById(R.id.imageView), -1, null, iv_large);
                    String bean = totalList.get(position);
                    BitmapUtils.displayImageFromUrl(bean, iv_large);

                }
            });
        }

        @Override
        protected void convert(final BaseViewHolder helper, final String bean) {
            BitmapUtils.displayImageFromUrl(bean, (ImageView) helper.getView(R.id.imageView));
        }
    }
    // ---------------------------------工具方法-----------------------------

    /**
     * @param containerView :expandedView的父布局，一般是根view
     * @param sourceView    ：从sourceView开始执行动画
     * @param position      ：传－1
     * @param bitmap        ：给expandedView设置图片，可以为空
     * @param expandedView
     */
    public void setViewPagerAndZoom(View containerView, final View sourceView,
                                    final int position, Bitmap bitmap, ImageView expandedView) {
        // 实现放大缩小类，传入当前的容器和要放大展示的对象
        final ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView,
                expandedView);
        if (bitmap != null) {
            expandedView.setImageBitmap(bitmap);
        }
        // 通过传入Id来从小图片扩展到大图，开始执行动画
        mZoomTutorial.zoomImageFromThumb(sourceView);
        mZoomTutorial.setOnZoomListener(new ZoomTutorial.OnZoomListener() {

            @Override
            public void onThumbed() {
                // TODO 自动生成的方法存根
                LogUtil.i("", "", "现在是-------------------> 小图状态");
            }

            @Override
            public void onExpanded() {
                // TODO 自动生成的方法存根
                LogUtil.i("", "", "现在是-------------------> 大图状态");

            }
        });
        expandedView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mZoomTutorial.closeZoomAnim(position);
            }
        });

    }
}
