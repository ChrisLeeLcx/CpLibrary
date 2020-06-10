package com.lee.demo.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.util.sidebar.GridSideBarLetterComparator;
import com.lee.demo.util.sidebar.model.GirdSideBarWithPinYinBean;
import com.lee.demo.util.sidebar.dapter.GridSideBarAdapter;
import com.lee.demo.util.sidebar.dapter.GridSideBarHotBrandAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import cn.lee.cplibrary.util.DrawableUtil;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.widget.recycler.ScrollEnabledGridLayoutManager;
import cn.lee.cplibrary.widget.sidebar.ChineseToPinyinHelper;
import cn.lee.cplibrary.widget.sidebar.SideBar;

/**
 * 网格字母索引（不带悬浮效果）
 */
public class GridSideBarActivity extends SwipeBackActivity {
    String url1 = "http://www.touxiang.cn/uploads/20130608/08-023618_517.jpg";
    String url2 = "http://img1.touxiang.cn/uploads/20120925/25-021153_68.jpg";
    RecyclerView recyclerView, rvHeader;
    TextView tvDialog;
    SideBar sidebarView;

    private List<GirdSideBarWithPinYinBean> totalList = new ArrayList<>();//品牌
    private List<GirdSideBarWithPinYinBean.BrandBean> hotList = new ArrayList<>();//热门品牌
    private GridSideBarAdapter adapter;//品牌
    private ArrayList<String> letterList = new ArrayList<>();//品牌：字母列表
    private String lastChooseLetter;//上次选择的字母，防止列表滑动多次重复勾选字母索引

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_grid_sidebar;
    }

    @Override
    public String getPagerTitle() {
        return "品牌";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvDialog = (TextView) findViewById(R.id.tv_dialog);
        sidebarView = (SideBar) findViewById(R.id.sidebar_view);
        initSideBar();
        initRecyclerView();
    }


    @Override
    protected void initData() {
        String[] array = getResources().getStringArray(
                R.array.arrUsernames);
        String[] hot = getResources().getStringArray(
                R.array.hot_brand);
        List<String> list = new ArrayList<>(array.length);
        List<String> hots = new ArrayList<>(hot.length);
        Collections.addAll(list, array);
        Collections.addAll(hots, hot);

        totalList.clear();
        totalList.addAll(getUserList(list));
        adapter = new GridSideBarAdapter(GridSideBarActivity.this, totalList);
        recyclerView.setAdapter(adapter);
        sidebarView.setIndexText(letterList, sidebarView.getDefaultLayoutParams(letterList.size()));
        initHeader(hots);
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {//列表滑动勾选字母索引
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisiblePosition = ((RecyclerView.LayoutParams) layoutManager.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                LogUtil.i("", "firstVisiblePosition=" + firstVisiblePosition);
                //firstVisiblePosition==0是热门
                String pys = (firstVisiblePosition==0)?letterList.get(0):totalList.get(firstVisiblePosition).pys  ;
                if (!pys.equals(lastChooseLetter)) {
                    sidebarView.setChoosedLetter(pys);
                    lastChooseLetter = pys;
                }

            }
        });
    }

    private void initSideBar() {
        tvDialog.setBackground(DrawableUtil.createSolidOvalDrawable(Color.parseColor("#EDEDED")));
        sidebarView.setTextViewDialog(tvDialog);
        sidebarView.setOnLetterClickedListener(new SideBar.OnLetterClickedListener() {//字母索引滑动-》使列表滑动到相应位置

            @Override
            public void onLetterClicked(String letter) {
                int pos = adapter.getLetterPosition(letter);
                //只有在在有头部的列表需要
                if (SideBar.PYS_HEADER.equals(letter)) {
                    recyclerView.scrollToPosition(0);
                    pos = 0;
                }
                if (pos != -1) {
                    recyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) recyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }

    /**
     * 热门品牌
     */
    private void initHeader(List<String> hots) {
        View headerView = getLayoutInflater().inflate(R.layout.header_grid_sidebar, (ViewGroup) recyclerView.getParent(), false);
        rvHeader = headerView.findViewById(R.id.recyclerView);
        rvHeader.setLayoutManager(new ScrollEnabledGridLayoutManager(this, 3));
        GridSideBarHotBrandAdapter hotBrandAdapter = new GridSideBarHotBrandAdapter(this, hotList);
        rvHeader.setAdapter(hotBrandAdapter);
        adapter.addHeaderView(headerView);

        //头部list--热门品牌
        for (int i = 0; i < hots.size(); i++) {
            String hot = hots.get(i);
            String url = i % 2 == 0 ? url1 : url2;
            hotList.add(new GirdSideBarWithPinYinBean.BrandBean((i - 100) + "", hot, url));
        }
        letterList.add(0, "热");
    }

    /**
     * 处理方法是固定的:除了设置数据部分
     *
     * @param allList 全部品牌
     * @return
     */
    private List<GirdSideBarWithPinYinBean> getUserList(List<String> allList) {
        TreeSet<String> letterSets = new TreeSet<>();//字母自动排序
        List<GirdSideBarWithPinYinBean> list = new ArrayList<>();//全部完整数据
        //设置列表
        for (int i = 0; i < allList.size(); i++) {
            GirdSideBarWithPinYinBean.BrandBean brandBean = new GirdSideBarWithPinYinBean.BrandBean();
            GirdSideBarWithPinYinBean bean = new GirdSideBarWithPinYinBean(brandBean);

            String brand = allList.get(i);
            //注意name不能为空
            String name = brand;
            String pinyin = ChineseToPinyinHelper.getInstance().getPinyin(
                    name).toUpperCase();
            String firstLetter = pinyin.substring(0, 1);
            if (firstLetter.matches("[A-Z]")) {
                bean.setPys(pinyin);//设置拼音
            } else {
                firstLetter = "#";
                bean.setPys("#");//设置拼音#
            }
            letterSets.add(firstLetter);
            //设置bean的数据
            String url = i % 2 == 0 ? url1 : url2;
            list.add(bean);
            brandBean.setIconUrl(url);
            brandBean.setCarName(name);
            brandBean.setId(i + "");

        }

        //设置字母列表--设置头部
        for (String letter : letterSets) {
            GirdSideBarWithPinYinBean bean = new GirdSideBarWithPinYinBean(true, letter);
            bean.setPys(letter);
            letterList.add(letter);
            //不能用 list.add(bean);会导致section_header在一组的最下面出现
            list.add(0, bean);
        }

        Collections.sort(list, new GridSideBarLetterComparator());
        //设置不限品牌
        GirdSideBarWithPinYinBean allNameBean = new GirdSideBarWithPinYinBean(new GirdSideBarWithPinYinBean.BrandBean("", "不限品牌", null));
        allNameBean.setPys("*");
        list.add(0, allNameBean);

        GirdSideBarWithPinYinBean allPinYinBean = new GirdSideBarWithPinYinBean(true, "*");
        allPinYinBean.setPys("*");
        letterList.add(0, "*");
        list.add(0, allPinYinBean);
        return list;
    }

    public void selectBrand(final GirdSideBarWithPinYinBean.BrandBean bean) {
        toast(bean.getCarName());
    }


}
