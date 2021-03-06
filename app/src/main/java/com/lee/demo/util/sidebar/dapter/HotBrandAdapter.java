package com.lee.demo.util.sidebar.dapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.demo.R;
import com.lee.demo.util.sidebar.model.BrandWithPinYinBean;
import com.lee.demo.ui.activity.BrandActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.List;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */


public class HotBrandAdapter extends BaseQuickAdapter<BrandWithPinYinBean.BrandBean, BaseViewHolder> {

    public HotBrandAdapter(final BrandActivity activity, final List<BrandWithPinYinBean.BrandBean> list) {
        super(R.layout.item_header_brand_rv, list);
        this.mContext = activity;
        setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                activity.selectBrand(list.get(position));
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, final BrandWithPinYinBean.BrandBean bean) {
        helper.setText(R.id.tv_header_brand_name, bean.getCarName());
        //设置图片
        BitmapUtils.displayImageFromUrl(mContext, bean.getIconUrl(), (ImageView) helper.getView(R.id.iv_header_car_icon));
    }

}