package cn.lee.cplibrary.util.dialog.bottom;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;

/**
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class BottomDialogAdapter<T extends BaseDialogBean> extends CpBaseDialogAdapter<T> {
    private   CpBottomDialog dialog;

    public BottomDialogAdapter(Context context, List<T> list, CpBottomDialog dialog) {
        super(context,list,dialog);
        this.dialog = dialog;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.cp_item_bottom_dialog;
    }

    @Override
    public void onBindViewHolder(@NonNull final CpBaseDialogAdapter.ViewHolder holder, final int position) {
       super.onBindViewHolder(holder,position);
        if (dialog.isChangeBg()) {//支持item更换背景
            holder.tvName.setBackgroundColor(dialog.getBgColor());
        }
    }
}
