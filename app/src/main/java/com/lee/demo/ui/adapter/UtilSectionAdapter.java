package com.lee.demo.ui.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.demo.R;
import com.lee.demo.model.SectionUtil;
import com.lee.demo.model.UtilBean;

import java.util.List;


public class UtilSectionAdapter extends BaseSectionQuickAdapter<SectionUtil, BaseViewHolder> {
    public UtilSectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final SectionUtil item) {
        helper.setText(R.id.header, item.header);
    }


    @Override
    protected void convert(BaseViewHolder helper, SectionUtil item) {
        UtilBean video =  item.t;
        helper.setText(R.id.tv_name, video.getName());
        helper.setText(R.id.tv_content, video.getContent());
    }
}
