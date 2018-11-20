package cn.lee.cplibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.sql.Time;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.timer.TimeUtils;

/**
 * 获取通用样式的Dialog，回调时间和具体设置的内容需要额外设置
 * 样式：底部弹出Dialog、 屏幕中间弹出Dialog、进度条
 *
 * @author: ChrisLee
 * @time: 2018/7/30
 */

public class ComDialogUtil {
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

    public static void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
