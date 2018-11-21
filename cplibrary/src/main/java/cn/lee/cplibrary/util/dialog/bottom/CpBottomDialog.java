package cn.lee.cplibrary.util.dialog.bottom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;

/**
 * 底部弹出的Dialog列表 没有弧度
 *
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class CpBottomDialog<T extends BaseDialogBean> {
    private Context context;
    //设置显示参数
    private boolean isShowCancel,//最后是否显示取消按钮
            isShowTitle,//是否显示标题栏
            isChangeBg;
    private List<T> list;//数据源
    private int rvHeight, itemHeight;
    private int txtSize;
    private int txtColor, bgColor;
    private int cancelSize, cancelTxtColor, cancelBgColor, cancelHeight;
    private int titleSize, titleColor, titleHeight, titleBgColor;

    private CpBottomDialog(Context context) {
        this.context = context;
    }


    public Dialog showDialog(final DialogCertificateCallBack callback) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("参数违规,list.size()必须大于0,此时size等于0");
        }
        View view = LayoutInflater.from(context).inflate(R.layout.cp_dialog_bottom_list, null);
        setView(view);
        final Dialog dialog = CpComDialog.getBottomDialog(context, true, view);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        BottomDialogAdapter adapter = new BottomDialogAdapter(context, list, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BottomDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomDialogAdapter adapter, View rootView, int position) {
                dialog.dismiss();
                callback.sure(adapter, rootView, position);
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.cancel();
            }
        });
        return dialog;
    }

    private void setList(List<T> list) {
        this.list = list;
    }

    private void setShowCancel(boolean showCancel) {
        isShowCancel = showCancel;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public void setChangeBg(boolean changeBg) {
        isChangeBg = changeBg;
    }

    private void setRvHeight(int rvHeight) {
        this.rvHeight = rvHeight;
    }

    private void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    private void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    private void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    private void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public int getTxtSize() {
        return txtSize;
    }

    public int getTxtColor() {
        return txtColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setCancelSize(int cancelSize) {
        this.cancelSize = cancelSize;
    }

    public void setCancelTxtColor(int cancelTxtColor) {
        this.cancelTxtColor = cancelTxtColor;
    }

    public void setCancelBgColor(int cancelBgColor) {
        this.cancelBgColor = cancelBgColor;
    }

    public void setCancelHeight(int cancelHeight) {
        this.cancelHeight = cancelHeight;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void setTitleBgColor(int titleBgColor) {
        this.titleBgColor = titleBgColor;
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public boolean isChangeBg() {
        return isChangeBg;
    }

    /**
     * 设置Dialog外观
     */

    public void setView(View view) {

        //RecyclerView的设置
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        int h = rvHeight;
        if (h != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            h = ScreenUtil.dp2px(context, h);
        }
        rv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h));

        //取消按钮设置
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        if (isShowCancel) {
            tvCancel.setVisibility(View.VISIBLE);
        } else {
            tvCancel.setVisibility(View.GONE);
        }
        tvCancel.setTextSize(ScreenUtil.sp2px(context, cancelSize));
        tvCancel.setTextColor(cancelTxtColor);
        if (isChangeBg) {
            tvCancel.setBackgroundColor(cancelBgColor);
        }
        int cancelH = cancelHeight;
        if (cancelH != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            cancelH = ScreenUtil.dp2px(context, cancelH);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cancelH);
        params.setMargins(0, ScreenUtil.dp2px(context, 5), 0, 0);
        tvCancel.setLayoutParams(params);

        //标题设置
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (isShowTitle) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvTitle.setTextSize(ScreenUtil.sp2px(context, titleSize));
        tvTitle.setTextColor(titleColor);
        if (isChangeBg) {
            tvTitle.setBackgroundColor(titleBgColor);
        }
        int titleHeight = this.titleHeight;
        if (titleHeight != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            titleHeight = ScreenUtil.dp2px(context, titleHeight);
        }
        LinearLayout.LayoutParams paramsT = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, titleHeight);
        tvTitle.setLayoutParams(paramsT);
    }

    public interface DialogCertificateCallBack {
        void sure(BottomDialogAdapter adapter, View rootView, int position);

        void cancel();
    }

    public static class Builder<K extends BaseDialogBean> {
        private Context context;
        private List<K> list;//数据源
        private boolean isShowCancel,//最后是否显示取消按钮
                isShowTitle,//是否显示标题栏
                isChangeBg;//取消按钮、item、标题 是否改变背景（只有isChageBg = true时 设置其相应背景色才会改变）
        private int rvHeight = LinearLayout.LayoutParams.WRAP_CONTENT, itemHeight = 40;   //recyclerView的高，单位dp，和布局文件中一致即可;一个Item的高、
        private int txtSize = 7;   //文字大小，单位sp，效果是不居中的size的一半
        private int txtColor = Color.parseColor("#333333"); //recyclerView中文字颜色
        private int bgColor = Color.parseColor("#ffffff");   //item背景颜色
        //取消按钮单独设置---如果不设置 默认和item的参数一致
        private int cancelSize = -999,   //取消按钮文字大小
                cancelTxtColor = -999, //取消按钮文字颜色
                cancelBgColor = -999,   //取消按钮背景颜色
                cancelHeight = -999; //取消按钮高

        //单独设置Title---如果不设置 默认和item的参数一致
        private int titleSize = -999,   //Title文字大小
                titleColor = -999, //Title文字颜色
                titleHeight = -999, //Title高
                titleBgColor = -999;  //Title背景颜色

        private Builder(Context context, List<K> list) {
            this.context = context;
            this.list = list;
        }

        public static <K extends BaseDialogBean> Builder builder(Context context, List<K> list) {
            return new Builder(context, list);
        }

        public CpBottomDialog build() {
            CpBottomDialog dialog = new CpBottomDialog(context);
            dialog.setList(list);
            dialog.setShowCancel(isShowCancel);
            dialog.setShowTitle(isShowTitle);
            dialog.setChangeBg(isChangeBg);
            dialog.setRvHeight(rvHeight);
            dialog.setItemHeight(itemHeight);
            dialog.setTxtSize(txtSize);
            dialog.setTxtColor(txtColor);
            dialog.setBgColor(bgColor);
            // 取消按钮
            cancelSize = cancelSize == -999 ? txtSize : cancelSize;
            cancelTxtColor = cancelTxtColor == -999 ? txtColor : cancelTxtColor;
            cancelBgColor = cancelBgColor == -999 ? bgColor : cancelBgColor;
            cancelHeight = cancelHeight == -999 ? itemHeight : cancelHeight;
            dialog.setCancelSize(cancelSize);
            dialog.setCancelTxtColor(cancelTxtColor);
            dialog.setCancelBgColor(cancelBgColor);
            dialog.setCancelHeight(cancelHeight);
            // 标题
            titleSize = titleSize == -999 ? txtSize : titleSize;
            titleColor = titleColor == -999 ? txtColor : titleColor;
            titleHeight = titleHeight == -999 ? itemHeight : titleHeight;
            dialog.setTitleSize(titleSize);
            dialog.setTitleColor(titleColor);
            dialog.setTitleBgColor(titleBgColor);
            dialog.setTitleHeight(titleHeight);
            return dialog;
        }

        public Builder setShowCancel(boolean showCancel) {
            isShowCancel = showCancel;
            return this;
        }

        public Builder setShowTitle(boolean showTitle) {
            isShowTitle = showTitle;
            return this;
        }

        public Builder setChangeBg(boolean changeBg) {
            isChangeBg = changeBg;
            return this;
        }

        /**
         * @param rvHeight 可传入数值单位dp、LinearLayout.LayoutParams.WRAP_CONTENT
         */
        public Builder setRvHeight(int rvHeight) {
            this.rvHeight = rvHeight;
            return this;
        }

        /**
         * @param itemHeight 可传入数值单位dp、LinearLayout.LayoutParams.WRAP_CONTENT
         */
        public Builder setItemHeight(int itemHeight) {
            this.itemHeight = itemHeight;
            return this;
        }

        public Builder setTxtSize(int txtSize) {
            this.txtSize = txtSize;
            return this;
        }

        public Builder setTxtColor(int txtColor) {
            this.txtColor = txtColor;
            return this;
        }

        public Builder setBgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder setCancelSize(int cancelSize) {
            this.cancelSize = cancelSize;
            return this;
        }

        public Builder setCancelTxtColor(int cancelTxtColor) {
            this.cancelTxtColor = cancelTxtColor;
            return this;
        }

        public Builder setCancelBgColor(int cancelBgColor) {
            this.cancelBgColor = cancelBgColor;
            return this;
        }

        public Builder setCancelHeight(int cancelHeight) {
            this.cancelHeight = cancelHeight;
            return this;
        }

        public Builder setTitleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setTitleHeight(int titleHeight) {
            this.titleHeight = titleHeight;
            return this;
        }

        public Builder setTitleBgColor(int titleBgColor) {
            this.titleBgColor = titleBgColor;
            return this;
        }
    }
}
