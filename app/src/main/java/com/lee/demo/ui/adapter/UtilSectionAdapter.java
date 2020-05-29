package com.lee.demo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.text.Layout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lee.demo.R;
import com.lee.demo.model.SectionUtil;
import com.lee.demo.model.UtilBean;

import java.util.List;

import cn.lee.cplibrary.util.SpanUtils;


public class UtilSectionAdapter extends BaseSectionQuickAdapter<SectionUtil, BaseViewHolder> {
    private Activity mContext;

    public UtilSectionAdapter(Activity context, int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
        this.mContext = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final SectionUtil item) {
        helper.setText(R.id.header, item.header);
    }


    @Override
    protected void convert(BaseViewHolder helper, SectionUtil item) {
        UtilBean video = item.t;
        helper.setText(R.id.tv_name, video.getName());
        if (video.getName().contains("Span")) {
            setTextDemo((TextView) helper.getView(R.id.tv_content), mContext);
        } else if (video.getName().contains("span案例")) {
            CharSequence span = new SpanUtils(mContext)
                    .append("字符串1").setFontSize(24, true).setBold().setForegroundColor(Color.YELLOW)
                    .append("字符串2").setForegroundColor(Color.GREEN).setFontSize(10, true)
                    .create();
            ((TextView) helper.getView(R.id.tv_content)).setText(span);
        } else {
            helper.setText(R.id.tv_content, video.getContent());
        }


    }

    int lineHeight;

    private void setTextDemo(TextView tvAboutSpan, Activity activity) {
        tvAboutSpan.setText(new SpanUtils(activity)
                .appendLine("SpanUtils")
                .setBackgroundColor(Color.LTGRAY)
                .setBold().setForegroundColor(Color.YELLOW)
                .setAlign(Layout.Alignment.ALIGN_CENTER)
                .appendLine("前景色")
                .setForegroundColor(Color.GREEN)
                .appendLine("背景色")
                .setBackgroundColor(Color.LTGRAY)
                .appendLine("行高顶部对齐")
                .setLineHeight(2 * lineHeight, SpanUtils.ALIGN_TOP)
                .setBackgroundColor(Color.GREEN)
                .appendLine("行高居中对齐")
                .setLineHeight(2 * lineHeight, SpanUtils.ALIGN_CENTER)
                .setBackgroundColor(Color.LTGRAY).appendLine("行高底部对齐")
                .setLineHeight(2 * lineHeight, SpanUtils.ALIGN_BOTTOM)
                .setBackgroundColor(Color.GREEN)
                .appendLine("测试段落缩，首行缩进两字，其他行不缩进")
//                .setLeadingMargin((int) textSize * 2, 10)
                .setBackgroundColor(Color.GREEN)
                .appendLine("测试引用，后面的字是为了凑到两行的效果")
                .setQuoteColor(Color.GREEN, 10, 10)
                .setBackgroundColor(Color.LTGRAY)
                .appendLine("测试列表项，后面的字是为了凑到两行的效果")
                .setBullet(Color.GREEN, 20, 10)
                .setBackgroundColor(Color.LTGRAY)
                .setBackgroundColor(Color.GREEN)
                .appendLine("测试图标文字顶部对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_high, 20, SpanUtils.ALIGN_TOP)
                .setBackgroundColor(Color.LTGRAY)
                .appendLine("测试图标文字居中对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_high, 20, SpanUtils.ALIGN_CENTER)
                .setBackgroundColor(Color.GREEN).appendLine("测试图标文字底部对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_high, 20, SpanUtils.ALIGN_BOTTOM)
                .setBackgroundColor(Color.LTGRAY).appendLine("测试图标顶部对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_low, 20, SpanUtils.ALIGN_TOP)
                .setBackgroundColor(Color.GREEN).appendLine("测试图标居中对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_low, 20, SpanUtils.ALIGN_CENTER)
                .setBackgroundColor(Color.LTGRAY).appendLine("测试图标底部对齐，后面的字是为了凑到两行的效果")
//                .setIconMargin(R.drawable.shape_spannable_block_low, 20, SpanUtils.ALIGN_BOTTOM)
                .setBackgroundColor(Color.GREEN).appendLine("32dp字体").setFontSize(32, true)
                .appendLine("2倍字体")
                .setFontProportion(2)
                .appendLine("横向2倍字体")
                .setFontXProportion(1.5f)
                .appendLine("删除线")
                .setStrikethrough()
                .appendLine("下划线")
                .setUnderline()
                .append("测试")
                .appendLine("上标")
                .setSuperscript()
                .append("测试")
                .appendLine("下标")
                .setSubscript()
                .appendLine("粗体")
                .setBold().appendLine("斜体")
                .setItalic().appendLine("粗斜体")
                .setBoldItalic().appendLine("monospace字体")
                .setFontFamily("monospace")
                .appendLine("自定义字体")
//                .setTypeface(Typeface.createFromAsset(getAssets(), "fonts/dnmbhs.ttf"))
                .appendLine("相反对齐").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .appendLine("居中对齐").setAlign(Layout.Alignment.ALIGN_CENTER)
                .appendLine("正常对齐").setAlign(Layout.Alignment.ALIGN_NORMAL)
//                .append("测试").appendLine("点击事件").setClickSpan(clickableSpan)
                .append("测试").appendLine("Url")
                .setUrl("https://github.com/Blankj/AndroidUtilCode")
                .append("测试").appendLine("模糊")
                .setBlur(3, BlurMaskFilter.Blur.NORMAL)
                .appendLine("颜色渐变")
//                .setShader(new LinearGradient(0, 0, 64 * density * 4, 0, getResources()
//                        .getIntArray(R.array.rainbow), null, Shader.TileMode.REPEAT))
                .setFontSize(64, true).appendLine("图片着色")
                .setFontSize(64, true)
//                .setShader(new BitmapShader(ImageUtils.getBitmap(R.drawable.cheetah), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT))
                .appendLine("阴影效果").setFontSize(64, true)
                .setBackgroundColor(Color.BLACK)
                .setShadow(24, 8, 8, Color.WHITE)
                .append("测试小图对齐").setBackgroundColor(Color.LTGRAY)
//                .appendImage(R.drawable.shape_spannable_block_low, SpanUtils.ALIGN_TOP)
//                .appendImage(R.drawable.shape_spannable_block_low, SpanUtils.ALIGN_CENTER)
//                .appendImage(R.drawable.shape_spannable_block_low, SpanUtils.ALIGN_BASELINE)
//                .appendImage(R.drawable.shape_spannable_block_low, SpanUtils.ALIGN_BOTTOM)
                .appendLine("end").setBackgroundColor(Color.LTGRAY).append("测试大图字体顶部对齐")
                .setBackgroundColor(Color.GREEN)
//                .appendImage(R.drawable.shape_spannable_block_high, SpanUtils.ALIGN_TOP)
                .appendLine().append("测试大图字体居中对齐").setBackgroundColor(Color.LTGRAY)
//                .appendImage(R.drawable.shape_spannable_block_high, SpanUtils.ALIGN_CENTER)
                .appendLine().append("测试大图字体底部对齐").setBackgroundColor(Color.GREEN)
//                .appendImage(R.drawable.shape_spannable_block_high, SpanUtils.ALIGN_BOTTOM)
                .appendLine().append("测试空格").appendSpace(30, Color.LTGRAY)
                .appendSpace(50, Color.GREEN)
                .appendSpace(100).appendSpace(30, Color.LTGRAY)
                .appendSpace(50, Color.GREEN)
                .create());
    }
}
