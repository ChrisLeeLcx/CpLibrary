package com.lee.demo.ui.activity;

import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.util.sidebar.model.BrandWithPinYinBean;
import com.lee.demo.util.sidebar.dapter.BrandAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import cn.lee.cplibrary.util.DrawableUtil;
import cn.lee.cplibrary.widget.drawer.NoShadowDrawerLayout;
import cn.lee.cplibrary.widget.sidebar.BaseSideBarBean;
import cn.lee.cplibrary.widget.sidebar.ChineseToPinyinHelper;
import cn.lee.cplibrary.widget.sidebar.LetterComparator;
import cn.lee.cplibrary.widget.sidebar.PinnedHeaderDecoration;
import cn.lee.cplibrary.widget.sidebar.SideBar;

public class BrandActivity extends SwipeBackActivity {
    String url1 = "http://www.touxiang.cn/uploads/20130608/08-023618_517.jpg";
    String url2 = "http://img1.touxiang.cn/uploads/20120925/25-021153_68.jpg";
    RecyclerView recyclerView;
    TextView tvDialog;
    SideBar sidebarView;
    Button btn;
    NoShadowDrawerLayout drawerLayout;

    private List<BrandWithPinYinBean> totalList = new ArrayList<>();//品牌
    private BrandAdapter adapter;//品牌
    private ArrayList<String> letterList = new ArrayList<>();//品牌：字母列表
    private int lastPosition = 0;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_brand;
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
        btn = (Button) findViewById(R.id.btn);
        drawerLayout = (NoShadowDrawerLayout) findViewById(R.id.drawerLayout);
        tvDialog.setBackground(DrawableUtil.createSolidOvalDrawable(Color.parseColor("#EDEDED")));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sidebarView.setTextViewDialog(tvDialog);
        //设置悬浮头部
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration(true);
        decoration.registerTypePinnedHeader(BaseSideBarBean.Type.TYPE_HEADER_SECTION.ordinal(), new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                if (lastPosition != adapterPosition) {
                    sidebarView.setChoosedLetter(totalList.get(adapterPosition).pys);
                    lastPosition = adapterPosition;
                }
                return true;
            }
        });
        recyclerView.addItemDecoration(decoration);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisiblePosition = ((RecyclerView.LayoutParams) layoutManager.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                if (firstVisiblePosition == 0) {
                    sidebarView.setChoosedLetter(totalList.get(0).pys);
                    lastPosition = 0;
                }
            }
        });

        sidebarView
                .setOnLetterClickedListener(new SideBar.OnLetterClickedListener() {

                    @Override
                    public void onLetterClicked(String letter) {
                        int pos = adapter.getLetterPosition(letter);
                        //只有在在有头部的列表需要
                        if (SideBar.PYS_HEADER.equals(letter)) {
                            recyclerView.scrollToPosition(0);
                            pos = 0;
                        }
                        if (pos != -1) {
                            lastPosition = pos;
                            recyclerView.scrollToPosition(pos);
                            LinearLayoutManager mLayoutManager =
                                    (LinearLayoutManager) recyclerView.getLayoutManager();
                            mLayoutManager.scrollToPositionWithOffset(pos, 0);
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        initDrawer();
        String[] array = getResources().getStringArray(
                R.array.arrUsernames);
        String[] hot = getResources().getStringArray(
                R.array.hot_brand);
        List<String> list = new ArrayList<>(array.length);
        List<String> hots = new ArrayList<>(hot.length);
        Collections.addAll(list, array);
        Collections.addAll(hots, hot);

        totalList.clear();
        totalList.addAll(getUserList(list, hots));
        adapter = new BrandAdapter(BrandActivity.this, totalList);
        recyclerView.setAdapter(adapter);
        sidebarView.setIndexText(letterList, sidebarView.getDefaultLayoutParams(letterList.size()));
    }

    /**
     * 抽屉相关
     */
    private void initDrawer() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(btn.getText().toString());
            }
        });
    }

    /**
     * 处理方法是固定的:除了设置数据部分
     *
     * @param allList 全部品牌
     * @param hots 热门品牌
     * @return
     */
    private List<BrandWithPinYinBean> getUserList(List<String> allList, List<String> hots) {
        TreeSet<String> letterSets = new TreeSet<>();//字母自动排序
        List<BrandWithPinYinBean> list = new ArrayList<>();//全部完整数据
        //设置列表
        for (int i = 0; i < allList.size(); i++) {
            BrandWithPinYinBean bean = new BrandWithPinYinBean();
            String brand = allList.get(i);
            //注意name不能为空
            String name = brand;
            String pinyin = ChineseToPinyinHelper.getInstance().getPinyin(
                    name).toUpperCase();
            String firstLetter = pinyin.substring(0, 1);
            bean.setName(name);//设置文字显示
            bean.setType(BaseSideBarBean.Type.TYPE_ITEM);
            if (firstLetter.matches("[A-Z]")) {
                bean.setPys(pinyin);//设置拼音
            } else {
                firstLetter = "#";
                bean.setPys("#");//设置拼音#
            }
            letterSets.add(firstLetter);
            //设置bean的数据
            String url = i%2==0?url1:url2;
            bean.setBean(new BrandWithPinYinBean.BrandBean(i + "", bean.getName(), url));
            list.add(bean);
        }

        //设置字母列表
        for (String letter : letterSets) {
            BrandWithPinYinBean bean = new BrandWithPinYinBean();
            bean.setType(BaseSideBarBean.Type.TYPE_HEADER_SECTION);
            bean.setPys(letter);
            letterList.add(letter);
            //不能用 list.add(bean);会导致section_header在一组的最下面出现
            list.add(0, bean);
        }

        Collections.sort(list, new LetterComparator<BrandWithPinYinBean>() {
            @Override
            public int compare(BrandWithPinYinBean l, BrandWithPinYinBean r) {
                return super.compare(l, r);
            }
        });


        //设置不限品牌
        BrandWithPinYinBean allNameBean = new BrandWithPinYinBean();
        allNameBean.setBean(new BrandWithPinYinBean.BrandBean("", "不限品牌", null));
        allNameBean.setName("不限品牌");
        allNameBean.setType(BaseSideBarBean.Type.TYPE_ITEM);
        list.add(0, allNameBean);

        BrandWithPinYinBean allPinYinBean = new BrandWithPinYinBean();
        allPinYinBean.setPys("*");
        allPinYinBean.setType(BaseSideBarBean.Type.TYPE_HEADER_SECTION);
        letterList.add(0, "*");
        list.add(0, allPinYinBean);

        //头部list--热门品牌
        List<BrandWithPinYinBean.BrandBean> headerList = new ArrayList<>();
        BrandWithPinYinBean bean = new BrandWithPinYinBean();//含有list的头部bean
        for (int i = 0; i < hots.size(); i++) {
            String hot = hots.get(i);
            String url = i%2==0?url1:url2;
            headerList.add(new BrandWithPinYinBean.BrandBean((i - 100) + "", hot, url));
        }
        bean.setHeadList(headerList);
        bean.setType(BaseSideBarBean.Type.TYPE_HEADER);
        bean.setPys(SideBar.PYS_HEADER);
        list.add(0, bean);
        letterList.add(0, "热");
        return list;
    }


    private String lastBrandId = "";//避免多次重复点击一个品牌导致多次请求

    public void selectBrand(final BrandWithPinYinBean.BrandBean bean) {
        if ("不限品牌".equals(bean.getCarName())) {
            toast("不限品牌");
        } else {
            drawerLayout.openDrawer(GravityCompat.END);
            if (!lastBrandId.equals(bean.getId())) {//避免多次重复点击一个品牌导致多次请求
                //抽屉中的数据
                lastBrandId = bean.getId();
                btn.setText(bean.getCarName());

            }
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

}
