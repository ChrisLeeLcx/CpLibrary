package cn.lee.cplibrary.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.timer.TimeUtils;

/**
 * 获取通用样式的Dialog，回调时间和具体设置的内容需要额外设置
 * 样式：底部弹出Dialog、 屏幕中间弹出Dialog、进度条
 * 通用1个按钮的对话框 ，2个按钮的对话框
 *
 * @author: ChrisLee
 * @time: 2018/7/30
 */

public class CpComDialog {
    private Context context;
    //配置 1个按钮、2个按钮对话框外观参数
    private String title, content, txtCancel, sure;
    private int btnColor, titleColor, contentColor;
    private int titleSize, contentSize, btnSize;
    private boolean isCancel;
    private int width, height;//对话框的宽、除去按钮后的高
    private int left=20,top=20,right=20,bottom=20; //对话框标题内容部分的上下左右边距
    private int gravity=Gravity.CENTER;
    private CpComDialog(Context context) {
        this.context = context;
    }

    public static Dialog mProgressDialog;

    /**
     * 获取通用 页面底部弹出的Dialog
     *
     * @param cancel ：是否可以点击外部取消Dialog
     * @param view   ：Dialog的布局view
     *               注意：如果点过快 连续点击 则返回null ，注意判断
     */
    public static Dialog getBottomDialog(Context context, boolean cancel, View view) {
        if (TimeUtils.isFastClick()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.cp_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.cp_AnimBottomFade);
        WindowManager.LayoutParams lp = window
                .getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setCancelable(cancel);
        dialog.show();
        return dialog;
    }

    /**
     * 获取通用 页面中间弹出的Dialog
     *
     * @param cancel ：是否可以点击外部取消Dialog
     * @param view   ：Dialog的布局view
     *               注意：如果点过快 连续点击 则返回null ，注意判断
     */
    public static Dialog getCenterDialog(Context context, boolean cancel, View view) {
        if (TimeUtils.isFastClick()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.cp_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window
                .getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setCancelable(cancel);
        dialog.show();
        return dialog;
    }

    /**
     * 获取通用从右向左弹出的页面底部Dialog
     * 注意：如果点过快 连续点击 则返回null ，注意判断
     */
    public static Dialog getRightBottomDialog(Context context, boolean cancel, View view) {
        if (TimeUtils.isFastClick()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.cp_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.cp_AnimRightFade);
        WindowManager.LayoutParams lp = window
                .getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setCancelable(cancel);
        dialog.show();
        return dialog;
    }


    /***进度Dialog*/
    public static Dialog showProgressDialog(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.cp_dialog_progress, null);
        mProgressDialog = mProgressDialog == null ? new Dialog(context, R.style.cp_dialog) : mProgressDialog;
        mProgressDialog.setContentView(v, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow()
                .getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView textView = mProgressDialog
                .findViewById(R.id.tv_info);

        if (textView == null || ObjectUtils.isEmpty(content)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(content);
        }
        mProgressDialog.show();
        return mProgressDialog;
    }

    /***通用2个按钮Dialog*/
    public Dialog show2BtnDialog(final Dialog2BtnCallBack callBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.cp_dialog_com_2btn, null);
        final Dialog dialog = getCenterDialog(context, isCancel, view);
        setView(view);
        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callBack.sure();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callBack.cancel();
            }
        });

        return dialog;
    }

    /***通用1个按钮Dialog*/
    public Dialog show1BtnDialog(final Dialog1BtnCallBack callBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.cp_dialog_com_2btn, null);
        final Dialog com1BtnDialog = getCenterDialog(context, isCancel, view);
        setView(view);
        view.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        view.findViewById(R.id.view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_sure).setBackground(context.getResources().getDrawable(R.drawable.cp_bwhite_b10_selector));
        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                com1BtnDialog.dismiss();
                callBack.sure();
            }
        });
        return com1BtnDialog;

    }

    public static void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 设置参数
     */
    public void setView(View view) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        TextView btnSure = view.findViewById(R.id.btn_sure);
        LinearLayout llTop = view.findViewById(R.id.ll_top);//设置对话框的宽和高
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            tvTitle.setTextColor(titleColor);
            tvTitle.setTextSize(ScreenUtil.sp(context, titleSize));
            tvTitle.setGravity(gravity);
        }
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
            tvContent.setTextColor(contentColor);
            tvContent.setTextSize(ScreenUtil.sp(context, contentSize));
            tvContent.setGravity(gravity);
        }
        if (!TextUtils.isEmpty(txtCancel)) {
            btnCancel.setText(txtCancel);
            btnCancel.setTextColor(btnColor);
            btnCancel.setTextSize(ScreenUtil.sp(context, btnSize));
        }
        if (!TextUtils.isEmpty(sure)) {
            btnSure.setText(sure);
            btnSure.setTextColor(btnColor);
            btnSure.setTextSize(ScreenUtil.sp(context, btnSize));
        }
        //设置宽高
        if (width != 245 || height != LinearLayout.LayoutParams.WRAP_CONTENT) {//不是默认值
            int w = width, h = height;
            if (width != LinearLayout.LayoutParams.MATCH_PARENT && width != LinearLayout.LayoutParams.WRAP_CONTENT) {
                w = ScreenUtil.dp2px(context, width);
            }
            if (height != LinearLayout.LayoutParams.MATCH_PARENT && height != LinearLayout.LayoutParams.WRAP_CONTENT) {
                h = ScreenUtil.dp2px(context, height);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            llTop.setLayoutParams(params);
        }
        llTop.setPadding(ScreenUtil.dp2px(context,left),ScreenUtil.dp2px(context,top),ScreenUtil.dp2px(context,right),ScreenUtil.dp2px(context,bottom));
    }

    public interface Dialog2BtnCallBack {
        public void sure();

        public void cancel();
    }

    public interface Dialog1BtnCallBack {
        public void sure();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTxtCancel(String txtCancel) {
        this.txtCancel = txtCancel;
    }

    public void setSure(String sure) {
        this.sure = sure;
    }

    public void setBtnColor(@ColorInt int btnColor) {
        this.btnColor = btnColor;
    }

    public void setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
    }

    public void setContentColor(@ColorInt int contentColor) {
        this.contentColor = contentColor;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public void setBtnSize(int btnSize) {
        this.btnSize = btnSize;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public static class Builder {
        private Context context;
        private String title, content, txtCancel="取消", sure="确定";//标题、内容、取消按钮、确定按钮的文案
        private int btnColor = Color.parseColor("#067CEC")//按钮颜色
                , titleColor = Color.parseColor("#000000")//标题颜色
                , contentColor = Color.parseColor("#010202");//内容颜色
        private int titleSize = 18,//标题文字大小 单位sp
                contentSize = 14, btnSize = 18;//内容 按钮 文字大小、
        private boolean isCancel = true;//是否可以取消,默认可以
        private int width = 245, height = LinearLayout.LayoutParams.WRAP_CONTENT;//对话框的宽、除去按钮后的高 单位dp
        private int left=20,top=20,right=20,bottom=20; //对话框标题内容部分的上下左右边距
        private int gravity=Gravity.CENTER;
        private Builder(Context context) {
            this.context = context;
        }

        public static Builder builder(Context context) {
            return new Builder(context);
        }

        public CpComDialog build() {
            CpComDialog util = new CpComDialog(context);
            util.setTitle(title);
            util.setContent(content);
            util.setTxtCancel(txtCancel);
            util.setSure(sure);
            util.setBtnColor(btnColor);
            util.setTitleColor(titleColor);
            util.setContentColor(contentColor);
            util.setTitleSize(titleSize);
            util.setContentSize(contentSize);
            util.setBtnSize(btnSize);
            util.setCancel(isCancel);
            util.setWidth(width);
            util.setHeight(height);
            util.setPadding(left,top,right,bottom);
            util.setGravity(gravity);
            return util;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTxtCancel(String txtCancel) {
            this.txtCancel = txtCancel;
            return this;
        }

        public Builder setSure(String sure) {
            this.sure = sure;
            return this;
        }

        public Builder setBtnColor(@ColorInt  int btnColor) {
            this.btnColor = btnColor;
            return this;
        }

        public Builder setTitleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setContentColor(@ColorInt int contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public Builder setTitleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder setContentSize(int contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public Builder setBtnSize(int btnSize) {
            this.btnSize = btnSize;
            return this;
        }

        public Builder setCancel(boolean cancel) {
            isCancel = cancel;
            return this;
        }

        /**
         * @param width:可以为数值单位dp、LinearLayout.LayoutParams.WRAP_CONTENT、LinearLayout.LayoutParams.MATCH_PARENT
         *             注意：值为LinearLayout.LayoutParams.MATCH_PARENT时候，实际上离屏幕左右还有20dp左右距离
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * @param height:可以为数值单位dp、LinearLayout.LayoutParams.WRAP_CONTENT
         *              注意：不支持LinearLayout.LayoutParams.MATCH_PARENT
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * 对话框标题内容部分的上下左右边距 单位dp
         */
        public Builder setPadding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }
    }
}
