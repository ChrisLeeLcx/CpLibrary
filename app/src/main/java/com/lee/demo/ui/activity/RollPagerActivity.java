package com.lee.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.ui.activity.webview.BaseWebViewActivity;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.widget.rollviewpager.RollPagerView;
import cn.lee.cplibrary.widget.rollviewpager.RollViewPagerUtil;
import cn.lee.cplibrary.widget.rollviewpager.adapter.LoopPagerAdapter;
import cn.lee.cplibrary.widget.rollviewpager.hintview.ColorPointHintView;
import cn.lee.cplibrary.widget.viewflipper.CLViewFlipper;
import cn.lee.cplibrary.widget.viewflipper.CLViewFlipperAdapter;

/**
 * 轮播图
 */
public class RollPagerActivity extends SwipeBackActivity {

    private RollPagerView rollPagerView;
    private CLViewFlipper vFlipper;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_roll_pager;
    }

    @Override
    public String getPagerTitle() {
        return "轮播图";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    protected void initData() {
        //初始化数据源
        String[] imgs = getResources().getStringArray(
                R.array.img_src_data);
        List<BannersBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new BannersBean("图片" + i, imgs[i], imgs[i]));
        }
        rollPagerView.setAdapter(new ImageLoopAdapter(getSelfActivity(), rollPagerView, list));
        //广告圆点的设置
        rollPagerView.setHintView(new ColorPointHintView(getSelfActivity(), getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.white)));
        //CLViewFlipper
        String [] array = {"戚薇","齐大友","齐天大圣","品冠","吴克群"};
        HomeViewFlipperAdapter adapter = new HomeViewFlipperAdapter(getSelfActivity(), Arrays.asList(array), vFlipper);
        vFlipper.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void initView() {
        rollPagerView = (RollPagerView) findViewById(R.id.rollPagerView);
        vFlipper = (CLViewFlipper) findViewById(R.id.vFlipper);
    }

    //轮播图的adapter
    public class ImageLoopAdapter extends LoopPagerAdapter {

        private List<BannersBean> banners;
        private BaseActivity activity;

        /**
         * 获取数据
         **/
        public ImageLoopAdapter(BaseActivity activity, RollPagerView viewPager, List<BannersBean> banners) {
            super(viewPager);
            this.activity = activity;
            this.banners = banners;
        }

        @Override
        public View getView(ViewGroup container, int position) {//数据填充
            String logoUrl = banners.get(position).getImgUrl();
            final String jumpUrl = banners.get(position).getJumpUrl();
            final String bannerName = banners.get(position).getTitle();
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setBackgroundColor(Color.BLACK);
            BitmapUtils.displayImageFromUrl(activity, logoUrl, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!RollViewPagerUtil.getNetworkIsConnected(activity)) {
                        activity.toast("网络不稳定");
                    } else if (!ObjectUtils.isEmpty(jumpUrl)) {
                        //跳转到下一个页面
                        Intent intent = new Intent(activity, BaseWebViewActivity.class);
                        intent.putExtra(BaseWebViewActivity.KEY_TITLE, bannerName);
                        intent.putExtra(BaseWebViewActivity.KEY_URL, jumpUrl);
                        activity.startActivity(intent);
                    }
                }
            });
            return view;
        }

        @Override
        public int getRealCount() {
            return banners.size();
        }
    }

    //上下滚动广告的adapter
    public class HomeViewFlipperAdapter extends CLViewFlipperAdapter {
        private List<String> totalList;
        private Context context;
        private LayoutInflater inflater;
        private CLViewFlipper myViewFlipper;


        public HomeViewFlipperAdapter(Context context, List<String> totalList, CLViewFlipper myViewFlipper) {
            this.context = context;
            this.totalList = totalList;
            this.inflater = LayoutInflater.from(context);
            this.myViewFlipper =myViewFlipper;
        }
        @Override
        protected CLViewFlipper getMyViewFlipper() {
            return myViewFlipper;
        }
        @Override
        public int getCount() {
            return totalList == null ? 0 : totalList.size();
        }

        @Override
        public Object getItem(int position) {
            return totalList == null ? null : totalList.get(position);
        }

        @Override
        public String getItemId(int position) {
            return position+"";
        }

        @Override
        public View getView(int position, View convertView) {
            ViewHolder holder;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_home_view_flipper, null,
                        false);
                holder.tv_title = convertView
                        .findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(totalList.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv_title;
        }
    }

    public static class BannersBean {

        private String title;
        private String imgUrl;//图片地址
        private String jumpUrl;

        public BannersBean(String title, String imgUrl, String jumpUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
            this.jumpUrl = jumpUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }
    }

}
