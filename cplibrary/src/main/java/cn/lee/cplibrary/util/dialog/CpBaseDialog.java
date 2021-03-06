package cn.lee.cplibrary.util.dialog;

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

/**
 * Created by ChrisLee on 2019/3/26.
 */

public abstract class CpBaseDialog <T extends BaseDialogBean>{
    protected Context context;
    //设置显示参数
    protected boolean isShowCancel,//最后是否显示取消按钮
            isShowTitle;//是否显示标题栏
    protected List<T> list;//数据源
    protected int rvHeight, itemHeight;
    protected int lineMarginLR=0;
    protected int txtSize;
    protected int txtColor;
    protected int cancelSize, cancelTxtColor, cancelHeight;
    protected int titleSize, titleColor, titleHeight;
    protected String title,cancelTxt;
    public interface CpDialogBottomListCallBack {
        void sure(CpBaseDialogAdapter adapter, View rootView, int position);
        void cancel();
    }
    protected CpBaseDialog(Context context) {
        this.context = context;
    }
    protected  abstract  CpBaseDialogAdapter getAdapter();

    protected abstract int getLayoutResId();//获取布局资源id

    public Dialog showDialog(final CpDialogBottomListCallBack callback) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("参数违规,list.size()必须大于0,此时size等于0");
        }
        View view = LayoutInflater.from(context).inflate(getLayoutResId() , null);
        setView(view);
        final Dialog dialog = CpComDialog.getBottomDialog(context, true, view);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        CpBaseDialogAdapter adapter =getAdapter();

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CpBaseDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CpBaseDialogAdapter adapter, View rootView, int position) {
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
        tvCancel.setText(cancelTxt);
        tvCancel.setTextSize(ScreenUtil.sp(context, cancelSize));
        tvCancel.setTextColor(cancelTxtColor);

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
        tvTitle.setText(title);
        tvTitle.setTextSize(ScreenUtil.sp(context, titleSize));
        tvTitle.setTextColor(titleColor);

        int titleHeight = this.titleHeight;
        if (titleHeight != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            titleHeight = ScreenUtil.dp2px(context, titleHeight);
        }
        LinearLayout.LayoutParams paramsT = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, titleHeight);
        tvTitle.setLayoutParams(paramsT);

    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setShowCancel(boolean showCancel) {
        isShowCancel = showCancel;
    }

    public void setCancelTxt(String cancelTxt) {
        this.cancelTxt = cancelTxt;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public void setRvHeight(int rvHeight) {
        this.rvHeight = rvHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
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


    public void setCancelSize(int cancelSize) {
        this.cancelSize = cancelSize;
    }

    public void setCancelTxtColor(int cancelTxtColor) {
        this.cancelTxtColor = cancelTxtColor;
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

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRvHeight() {
        return rvHeight;
    }

    public void setLineMarginLR(int lineMarginLR) {
        this.lineMarginLR = lineMarginLR;
    }

    public int getLineMarginLR() {
        return lineMarginLR;
    }

    public  static class Builder<K extends BaseDialogBean> {
        protected Context context;
        private List<K> list;//数据源
        private boolean isShowCancel,//最后是否显示取消按钮
                isShowTitle;//是否显示标题栏
        private int rvHeight = LinearLayout.LayoutParams.WRAP_CONTENT, itemHeight = 40;   //recyclerView的高，单位dp，和布局文件中一致即可;一个Item的高、
        private int lineMarginLR = 0;   //Item中line据两边的距离 单位dp
        private int txtSize = 14;   //文字大小，单位sp，效果是不居中的size的一半
        private int txtColor = Color.parseColor("#333333"); //recyclerView中文字颜色
        //取消按钮单独设置---如果不设置 默认和item的参数一致
        private int cancelSize = -999,   //取消按钮文字大小
                cancelTxtColor = -999, //取消按钮文字颜色
                cancelHeight = -999; //取消按钮高
        private String cancelTxt="取消";//标题内容
        //单独设置Title---如果不设置 默认和item的参数一致
        private int titleSize = -999,   //Title文字大小
                titleColor = -999, //Title文字颜色
                titleHeight = -999; //Title高
        private String title="标题";//标题内容


        protected  CpBaseDialog getDialog(Context context){//子类必须重写
            return null;
        }

        protected Builder(Context context, List<K> list) {
            this.context = context;
            this.list = list;
        }

        public CpBaseDialog  build() {
            CpBaseDialog dialog =getDialog(context);
            dialog.setList(list);
            dialog.setShowCancel(isShowCancel);
            dialog.setShowTitle(isShowTitle);
            dialog.setRvHeight(rvHeight);
            dialog.setItemHeight(itemHeight);
            dialog.setTxtSize(txtSize);
            dialog.setTxtColor(txtColor);
            // 取消按钮
            cancelSize = cancelSize == -999 ? txtSize : cancelSize;
            cancelTxtColor = cancelTxtColor == -999 ? txtColor : cancelTxtColor;
            cancelHeight = cancelHeight == -999 ? itemHeight : cancelHeight;
            dialog.setCancelTxt(cancelTxt);
            dialog.setCancelSize(cancelSize);
            dialog.setCancelTxtColor(cancelTxtColor);
            dialog.setCancelHeight(cancelHeight);
            // 标题
            titleSize = titleSize == -999 ? txtSize : titleSize;
            titleColor = titleColor == -999 ? txtColor : titleColor;
            titleHeight = titleHeight == -999 ? itemHeight : titleHeight;
            dialog.setTitle(title);
            dialog.setTitleSize(titleSize);
            dialog.setTitleColor(titleColor);
            dialog.setTitleHeight(titleHeight);
            //line
            if(lineMarginLR>0){
                dialog.setLineMarginLR(lineMarginLR);
            }
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

        public Builder setCancelSize(int cancelSize) {
            this.cancelSize = cancelSize;
            return this;
        }

        public Builder setCancelTxtColor(int cancelTxtColor) {
            this.cancelTxtColor = cancelTxtColor;
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

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setLineMarginLR(int lineMarginLR) {
            this.lineMarginLR = lineMarginLR;
            return this;
        }

        public Builder setCancelTxt(String cancelTxt) {
            this.cancelTxt = cancelTxt;
            return this;
        }
    }
}
