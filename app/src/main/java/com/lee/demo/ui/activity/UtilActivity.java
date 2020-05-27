package com.lee.demo.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.model.SectionUtil;
import com.lee.demo.model.UtilBean;
import com.lee.demo.ui.adapter.UtilSectionAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.StringUtil;

public class UtilActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<SectionUtil> mData;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_rv;
    }

    @Override
    public String getPagerTitle() {
        return null;
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        mRecyclerView =   findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mData = getData();
        UtilSectionAdapter sectionAdapter = new UtilSectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);


        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);
//        tvPhone = findViewById(R.id.tv_phone);
//        tvPhone.setText(StringUtil.hideMiddleFourNumber("18551815425"));
//        LogUtil.i("","OnCreate");
    }

    @Override
    protected void initData() {

    }




     public List<SectionUtil> getData() {
        List<SectionUtil> mData = new ArrayList<>();
         mData.add(new SectionUtil(true,"StringUtil"));
         mData.add(new SectionUtil(new UtilBean("Str2Int",""+ StringUtil.String2Int("100"))));
         mData.add(new SectionUtil(new UtilBean("是否汉字",""+ StringUtil.checkNameChese("是否汉字"))));
         mData.add(new SectionUtil(new UtilBean("0~9999汉字转化成数字",""+ StringUtil.parseChineseNumber("三千四百五十六"))));
        return mData;
    }
}
