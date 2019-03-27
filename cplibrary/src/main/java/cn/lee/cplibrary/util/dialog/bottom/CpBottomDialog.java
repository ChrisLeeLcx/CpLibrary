package cn.lee.cplibrary.util.dialog.bottom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;

/**
 * 底部弹出的Dialog列表 没有弧度
 *
 * @author: ChrisLee
 * @time: 2018/11/21
 * 多了是否可以改变背景的选项
 */

public class CpBottomDialog<T extends BaseDialogBean> extends CpBaseDialog<T> {
    private boolean isChangeBg;//是否可以改变背景
    private boolean isTopRound;//顶部是否有圆角
    private int bgColor;
    private int cancelBgColor;
    private int titleBgColor;


    private CpBottomDialog(Context context) {
        super(context);
    }

    @Override
    protected CpBaseDialogAdapter getAdapter() {
        return new BottomDialogAdapter(context, list, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.cp_dialog_bottom_list;
    }

    public void setChangeBg(boolean changeBg) {
        isChangeBg = changeBg;
    }

    public boolean isChangeBg() {
        return isChangeBg;
    }

    public boolean isTopRound() {
        return isTopRound;
    }

    public void setTopRound(boolean topRound) {
        isTopRound = topRound;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setCancelBgColor(int cancelBgColor) {
        this.cancelBgColor = cancelBgColor;
    }

    public void setTitleBgColor(int titleBgColor) {
        this.titleBgColor = titleBgColor;
    }


    /**
     * 设置Dialog外观
     */

    public void setView(View view) {
        super.setView(view);
        //取消按钮背景设置
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        if (isChangeBg) {
            tvCancel.setBackgroundColor(cancelBgColor);
        }
        //标题按钮设置
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (isChangeBg) {
            tvTitle.setBackgroundColor(titleBgColor);
        } else if (isTopRound) {
            tvTitle.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_bgt10_selector));
        }
        //RecyclerView的背景设置:---针对顶部是圆角情况 并且无标题情况
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        if (rvHeight != LinearLayout.LayoutParams.WRAP_CONTENT && isTopRound && !isShowTitle) { //高度固定  、顶部圆角、无标题
            rv.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_bgt10_normal));
        } else { //高度自适应
            rv.setBackground(null);
        }

    }

    public static class Builder<K extends BaseDialogBean> extends CpBaseDialog.Builder<K> {
        private boolean isChangeBg;//是否改变取消按钮、item、标题的背景颜色（只有isChageBg = true时 设置其相应背景色才会改变）
        private boolean isTopRound;//顶部是否有圆角，在isChangeBg为false的时候起作用
        private int bgColor = Color.parseColor("#ffffff");   //item背景颜色
        private int cancelBgColor = -999;   //取消按钮背景颜色
        private int titleBgColor = -999;  //Title背景颜色

        @Override
        protected CpBaseDialog getDialog(Context context) {
            return new CpBottomDialog(context);
        }

        private Builder(Context context, List<K> list) {
            super(context, list);
        }

        public static <K extends BaseDialogBean> Builder builder(Context context, List<K> list) {
            return new Builder(context, list);
        }

        @Override
        public CpBottomDialog build() {
            CpBottomDialog myDialog = (CpBottomDialog) super.build();
            myDialog.setChangeBg(isChangeBg);
            myDialog.setTopRound(isTopRound);
            myDialog.setBgColor(bgColor);
            cancelBgColor = cancelBgColor == -999 ? bgColor : cancelBgColor;
            myDialog.setCancelBgColor(cancelBgColor);
            myDialog.setTitleBgColor(titleBgColor);
            return myDialog;
        }

        public Builder setChangeBg(boolean changeBg) {//位置 必须在父类其他设置方法的前面设置
            isChangeBg = changeBg;
            return this;
        }


        public Builder setTopRound(boolean topRound) {
            isTopRound = topRound;
            return this;
        }

        public Builder setBgColor(int bgColor) { // 位置:同setChangeBg
            this.bgColor = bgColor;
            return this;
        }

        public Builder setCancelBgColor(int cancelBgColor) {// 位置:同setChangeBg
            this.cancelBgColor = cancelBgColor;
            return this;
        }

        public Builder setTitleBgColor(int titleBgColor) {// 位置:同setChangeBg
            this.titleBgColor = titleBgColor;
            return this;
        }
    }
}
