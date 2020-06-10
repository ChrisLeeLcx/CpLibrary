package com.lee.demo.util.sidebar.dapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.util.sidebar.model.BrandWithPinYinBean;
import com.lee.demo.ui.activity.BrandActivity;
import com.lee.demo.util.BitmapUtils;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.widget.recycler.ScrollEnabledGridLayoutManager;
import cn.lee.cplibrary.widget.sidebar.BaseSideBarBean;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */

public class BrandAdapter extends BaseTurboAdapter<BrandWithPinYinBean, BaseViewHolder> {
    private BrandActivity activity;

    public BrandAdapter(Context context) {
        super(context);
    }

    public BrandAdapter(BrandActivity activity, List<BrandWithPinYinBean> data) {
        super(activity, data);
        this.activity = activity;
    }

    @Override
    protected int getDefItemViewType(int position) {
        BrandWithPinYinBean bean = getItem(position);

        return bean.type.ordinal();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {//列表
            return new BrandHolder(inflateItemView(R.layout.item_brand_lr, parent));
        } else if (viewType == 1) {//字母头部
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
        } else {//这个列表头部：热门品牌
//            return new HeaderHolder(inflateItemView(R.layout.header_brand, parent));
            return new HeaderHolder(inflateItemView(R.layout.header_brand_rv, parent));

        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final BrandWithPinYinBean item) {
        if (holder instanceof BrandHolder) {
            ((BrandHolder) holder).tv_brand_name.setText(item.getBean().getCarName());
            //设置图片
            if (ObjectUtils.isEmpty(item.getBean().getIconUrl())) {
                ((BrandHolder) holder).iv_car_icon.setVisibility(View.GONE);
                ((BrandHolder) holder).line.setVisibility(View.GONE);
            } else {
                ((BrandHolder) holder).iv_car_icon.setVisibility(View.VISIBLE);
                ((BrandHolder) holder).line.setVisibility(View.VISIBLE);
                BitmapUtils.displayImageFromUrl(activity,item.getBean().getIconUrl(), ((BrandHolder) holder).iv_car_icon);
            }
            //设置监听
            ((BrandHolder) holder).ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.selectBrand(item.getBean());
                }
            });
        } else if (holder instanceof PinnedHolder) {
            String letter = item.pys.substring(0, 1);
            ((PinnedHolder) holder).tv_pinyin.setText(letter);
        } else if (holder instanceof HeaderHolder) {
            //第2种方式实现头布局
            RecyclerView rv = ((HeaderHolder) holder).rv;
            rv.setLayoutManager(new ScrollEnabledGridLayoutManager(activity, 4));
            rv.setAdapter(new HotBrandAdapter(activity, item.getHeadList()));
        }
    }

    public int getLetterPosition(String letter) {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).type.ordinal() == BaseSideBarBean.Type.TYPE_HEADER_SECTION.ordinal() && getData().get(i).pys.equals(letter)) {
                return i;
            }
        }
        return -1;
    }

    class BrandHolder extends BaseViewHolder {

        TextView tv_brand_name;
        ImageView iv_car_icon;
        View line;
        LinearLayout ll_item;

        public BrandHolder(View view) {
            super(view);
            iv_car_icon = findViewById(R.id.iv_car_icon);
            tv_brand_name = findViewById(R.id.tv_brand_name);
            line = findViewById(R.id.line);
            ll_item = findViewById(R.id.ll_item);
        }
    }

    class PinnedHolder extends BaseViewHolder {
        TextView tv_pinyin;

        public PinnedHolder(View view) {
            super(view);
            tv_pinyin = findViewById(R.id.tv_pinyin);
        }
    }
    class HeaderHolder extends BaseViewHolder {
        RecyclerView rv;

        public HeaderHolder(View view) {
            super(view);
            rv = findViewById(R.id.recyclerView);
        }
    }

}
