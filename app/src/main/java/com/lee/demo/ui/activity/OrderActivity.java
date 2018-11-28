package com.lee.demo.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.BaseFragment;
import com.lee.demo.event.OrderTabSelectedEvent;
import com.lee.demo.ui.fragment.OrderFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.widget.viewpager.lazy.LazyFragmentPagerAdapter;
import cn.lee.cplibrary.widget.viewpager.lazy.LazyViewPager;

public class OrderActivity extends BaseActivity {

    private TabLayout tabLayout;
    private LazyViewPager viewPager;
    private String[] tabItemTextBuyCar = {"预约", "待支付", "已完成", "退款中"};

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order;
    }

    @Override
    public String getPagerTitle() {
        return "订单";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中的
                OrderTabSelectedEvent event;
                event = new OrderTabSelectedEvent(OrderFragment.BuyCarOrderType.values()[tab.getPosition()]);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//未选中的

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//复选的

            }
        });
    }

    @Override
    protected void initData() {
        ArrayList<BaseFragment> fragments = new ArrayList<>();//初始化fragment
        fragments.clear();
        for (int i = 0; i < OrderFragment.BuyCarOrderType.values().length; i++) {
            OrderFragment fragment = OrderFragment.getInstance(OrderFragment.BuyCarOrderType.values()[i]);
            fragments.add(fragment);
        }
        MyLazyPagerAdapter adapter  = new MyLazyPagerAdapter(getSupportFragmentManager(), tabItemTextBuyCar, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 懒加载ViewPager+懒加载Fragement+TabLayout模式通用
     */
    public class MyLazyPagerAdapter extends LazyFragmentPagerAdapter {
        private List<BaseFragment> fragments;
        private String[] tabTitle;

        public MyLazyPagerAdapter(FragmentManager fm, String[] tabTitle, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.tabTitle = tabTitle;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }


        @Override
        public int getCount() {
            return tabTitle.length;
        }

        @Override
        protected BaseFragment getItem(ViewGroup container, int position) {
            return fragments.get(position);
        }
    }
}
