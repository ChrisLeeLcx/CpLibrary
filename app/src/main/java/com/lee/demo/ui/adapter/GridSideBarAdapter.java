package com.lee.demo.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.demo.R;
import com.lee.demo.model.GirdSideBarWithPinYinBean;
import com.lee.demo.ui.activity.GridSideBarActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.List;

/**
 * Created by ChrisLee on 2020/6/10.
 */

public class GridSideBarAdapter extends BaseSectionQuickAdapter<GirdSideBarWithPinYinBean, BaseViewHolder> {
    private GridSideBarActivity activity;

    public GridSideBarAdapter(GridSideBarActivity activity, List<GirdSideBarWithPinYinBean> data) {
        super(R.layout.item_header_brand_rv, R.layout.item_pinned_header, data);
        this.activity = activity;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, GirdSideBarWithPinYinBean item) {
        helper.setText(R.id.tv_pinyin, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, final GirdSideBarWithPinYinBean item) {
        final GirdSideBarWithPinYinBean.BrandBean brandBean = item.t;
        holder.setText(R.id.tv_header_brand_name, brandBean.getCarName());
        BitmapUtils.displayImageFromUrl(activity, brandBean.getIconUrl(), (ImageView) holder.getView(R.id.iv_header_car_icon));
        holder.getView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectBrand(brandBean);
            }
        });
    }

    public int getLetterPosition(String letter) {
        for (int i = 0; i < getData().size(); i++) {
            GirdSideBarWithPinYinBean bean = getData().get(i);
            if (bean.isHeader && bean.pys.equals(letter)) {
                return i;
            }
        }
        return -1;
    }
}
