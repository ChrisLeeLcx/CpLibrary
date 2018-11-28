package com.lee.demo.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lee.demo.R;

import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.ViewUtil;
import cn.lee.cplibrary.widget.pwindow.CommonPopupWindow;


/**
 * 是CommonPopupWindow和FlexRadioGroup的使用案例，因为没有写布局文件，所以一些代码只能注释掉，但是用方式涵盖了
 * 可以參照奔类，写自己项目需要的PopupWindowUtil，
 * 使用时候，应该先实例化PopupWindowUtil，然后其对象调用相应的showPopWindow方法
 * 具体使用方式见新能源newenergy项目的SelectCarActivity
 */
public class PopupWindowUtil {
    private CommonPopupWindow pWindow;

    public CommonPopupWindow showPWindow(final Activity activity, View anchor, final TextView tv_click, CommonPopupWindow.ViewInterface listener) {
        ViewUtil.setDrawableRight(activity, R.drawable.buylist_chose_over, tv_click, 1);
        if (pWindow == null) {
            pWindow = new CommonPopupWindow.Builder(activity)
                    .setView(R.layout.pwindow)
                    .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener(listener).setOutsideTouchable(true)
                    .create();
            pWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewUtil.setDrawableRight(activity, R.drawable.buylist_chose_more, tv_click, 1);
                }
            });
            View contentView = pWindow.getContentView();
            final CommonPopupWindow finalPWindow = pWindow;
            final TextView tv = contentView.findViewById(R.id.tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalPWindow.dismiss();
                    ToastUtil.showToast(activity, tv.getText().toString());
                    tv_click.setText(tv.getText().toString());
                }
            });
            pWindow.showAsDropDown(anchor, 0, 0);
        } else if (!pWindow.isShowing()) {
            pWindow.showAsDropDown(anchor, 0, 0);
        }
        return pWindow;
    }

}
