package cn.lee.cplibrary.util.dialog.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;

/**
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class BottomDialogAdapter<T extends BaseDialogBean> extends CpBaseDialogAdapter<T> {
    private CpBottomDialog dialog;

    public BottomDialogAdapter(Context context, List<T> list, CpBottomDialog dialog) {
        super(context, list, dialog);
        this.dialog = dialog;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.cp_item_bottom_dialog;
    }

    @Override
    public void onBindViewHolder(@NonNull final CpBaseDialogAdapter.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (dialog.isChangeBg()) {//支持item更换背景
            holder.tvName.setBackgroundColor(dialog.getBgColor());
        } else if (dialog.getRvHeight() != LinearLayout.LayoutParams.WRAP_CONTENT && dialog.isTopRound() && !dialog.isShowTitle()) { //高度固定  、顶部圆角、无标题

            holder.tvName.setBackground(null);
        } else if (dialog.isTopRound()) { //顶部是圆角
            if (position == 0) {
                if (dialog.isShowTitle()) {
                    holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_selector));
                } else {
                    holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_bgt10_selector));
                }
            } else {
                holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_selector));
            }

        }
        //line
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(context, 0.5f));
        params.setMargins(ScreenUtil.dp2px(context, dialog.getLineMarginLR()), 0, ScreenUtil.dp2px(context, dialog.getLineMarginLR()), 0);
        holder.line.setLayoutParams(params);
    }
}
