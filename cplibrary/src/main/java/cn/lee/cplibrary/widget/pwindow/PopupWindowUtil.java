package cn.lee.cplibrary.widget.pwindow;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.flexbox.FlexboxLayout;

import java.util.HashMap;
import java.util.Map;

import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.widget.flexbox.FlexRadioGroup;


/**
 * 是CommonPopupWindow和FlexRadioGroup的使用案例，因为没有写布局文件，所以一些代码只能注释掉，但是用方式涵盖了
 *  可以參照奔类，写自己项目需要的PopupWindowUtil，
 *  使用时候，应该先实例化PopupWindowUtil，然后其对象调用相应的showPopWindow方法
 *  具体使用方式见新能源newenergy项目的SelectCarActivity
 */
class PopupWindowUtil {
    private CommonPopupWindow pWindowFilter;
    //筛选Window种的内容
    //车辆级别
    private String filterCarLevels[] = {"微型车", "小型车", "紧凑型车", "中型车", "中大型车", "大型车", "SUV", "MVP", "跑车"};
    //续航
    private String filterEndurances[] = {"100公里以下", "100-200公里", "200-300公里", "300-500公里", "500公里以上"};
    //动力
    private String filterPowers[] = {"纯动力", "插电式混合动力", "燃料电池", "增程式混合动力"};

//    public CommonPopupWindow showFilterPWindow(final Activity activity, View anchor, final TextView tv_click, CommonPopupWindow.ViewInterface listener, OnFilterCheckedChangeListener filterListener) {
//        ViewUtil.setDrawableRight(activity, R.drawable.tab_xiala, tv_click, 4);
//        if (pWindowFilter == null) {
//            pWindowFilter = new CommonPopupWindow.Builder(activity)
//                    .setView(R.layout.home_pwindow_filter)
//                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT)
//                    .setViewOnclickListener(listener).setOutsideTouchable(true)
//                    .create();
//            pWindowFilter.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//                @Override
//                public void onDismiss() {
//                    ViewUtil.setDrawableRight(activity, R.drawable.tab_shangla, tv_click, 4);
//                }
//            });
//            View contentView = pWindowFilter.getContentView();
//            contentView.findViewById(R.id.ll_close_pwindow).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pWindowFilter.dismiss();
//                }
//            });
//            createRadioButton(activity, filterCarLevels, 100, 26, 3, (FlexRadioGroup) contentView.findViewById(R.id.frg_car_level), filterListener);
//            createRadioButton(activity, filterEndurances, 164, 26, 2, (FlexRadioGroup) contentView.findViewById(R.id.frg_car_endurances), filterListener);
//            createRadioButton(activity, filterPowers, 164, 26, 2, (FlexRadioGroup) contentView.findViewById(R.id.frg_car_powers), filterListener);
//            pWindowFilter.showAsDropDown(anchor);
//        } else if (!pWindowFilter.isShowing()) {
//            pWindowFilter.showAsDropDown(anchor);
//        }
//        return pWindowFilter;
//    }

    /**
     * @param activity
     * @param filters
     * @param dpWidth
     * @param count    每行子控件个数
     * @param group
     */
    boolean mProtectFromCheckedChange = false;
    String msg = "";
    Map<Integer, String> map = new HashMap<>();

    private void createRadioButton(final Activity activity, String[] filters, int dpWidth, int dpHeight, int count, final FlexRadioGroup group, final OnFilterCheckedChangeListener filterListener) {
        int pxWidth = ScreenUtil.dp2px(activity, dpWidth);
        int pxHeight = ScreenUtil.dp2px(activity, dpHeight);
        final int marginRight = (ScreenUtil.getScreenWidth(activity) - ScreenUtil.dp2px(activity, 12) * 2 - pxWidth * count) / (count - 1);

        for (int i = 0; i < filters.length; i++) {//
            String filter = filters[i];
            RadioButton rb = null;
//            rb = (RadioButton) activity.getLayoutInflater().inflate(R.layout.home_car_filter_label, null);---因为没有写布局 R.layout.home_car_filter_label见文件最后注释掉的布局

            rb.setText(filter);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(pxWidth, pxHeight);
            if (i % count == (count - 1)) {//每行最后一个 不设置右边距---目的view 平局分布
                lp.setMargins(0, 0, 0, ScreenUtil.dp2px(activity, 10));
            } else {
                lp.setMargins(0, 0, marginRight, ScreenUtil.dp2px(activity, 10));
            }
            rb.setLayoutParams(lp);
            group.addView(rb);
            /**
             * 下面两个监听器用于点击两次可以清除当前RadioButton的选中
             * 点击RadioButton后，{@link FlexRadioGroup#OnCheckedChangeListener}先回调，然后再回调{@link View#OnClickListener}
             * 如果当前的RadioButton已经被选中时，不会回调OnCheckedChangeListener方法，故判断没有回调该方法且当前RadioButton确实被选中时清除掉选中
             */
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rb = (RadioButton) v;
                    if (!mProtectFromCheckedChange && ((RadioButton) v).isChecked()) {
                        group.clearCheck();
                        msg = "";
                    } else {
                        mProtectFromCheckedChange = false;
                        msg = rb.getText().toString();
                    }
                    map.put(group.getId(), msg);
                    filterListener.onFilterCheckedChanged(map);
                }
            });

        }//for
        group.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                mProtectFromCheckedChange = true;
            }
        });
    }

    //筛选条件PopWindow 3组任意一个选中或者取消的监听
    public interface OnFilterCheckedChangeListener {
        //key：为FlexRadioGroup的id：即R.id.frg_car_level、R.id.frg_car_endurances、R.id.frg_car_powers
        //Value：为相应一组FlexRadioGroup中的选中或取消的RadioButton的文案内容
        public void onFilterCheckedChanged(Map<Integer, String> filterMap);
    }
}

//布局文件：home_pwindow_filter
//<?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:orientation="vertical">
//
//<LinearLayout
//        android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:background="@color/home_white"
//                android:orientation="vertical"
//                android:paddingLeft="12dp"
//                android:paddingRight="12dp">
//
//<LinearLayout
//            android:id="@+id/ll_car_level"
//                    android:layout_width="match_parent"
//                    android:layout_height="wrap_content"
//                    android:orientation="vertical"
//                    android:paddingBottom="4dp"
//                    android:paddingTop="14dp">
//
//<TextView
//                android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        android:layout_marginBottom="12dp"
//                        android:text="车辆级别"
//                        android:textColor="@color/home_font_3"
//                        android:textSize="12sp" />
//
//<com.sidu.cool.home.view.FlexRadioGroup.FlexRadioGroup
//        android:id="@+id/frg_car_level"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:flexWrap="wrap" />
//
//
//</LinearLayout>
//
//<LinearLayout
//            android:id="@+id/ll_car_endurances"
//                    android:layout_width="match_parent"
//                    android:layout_height="wrap_content"
//                    android:orientation="vertical"
//                    android:paddingBottom="4dp">
//
//<TextView
//                android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        android:layout_marginBottom="12dp"
//                        android:text="续航"
//                        android:textColor="@color/home_font_3"
//                        android:textSize="12sp" />
//
//<com.sidu.cool.home.view.FlexRadioGroup.FlexRadioGroup
//        android:id="@+id/frg_car_endurances"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:flexWrap="wrap" />
//
//
//</LinearLayout>
//
//<LinearLayout
//            android:id="@+id/ll_car_powers"
//                    android:layout_width="match_parent"
//                    android:layout_height="wrap_content"
//                    android:orientation="vertical"
//                    android:paddingBottom="4dp">
//
//<TextView
//                android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        android:layout_marginBottom="12dp"
//                        android:text="动力"
//                        android:textColor="@color/home_font_3"
//                        android:textSize="12sp" />
//
//<com.sidu.cool.home.view.FlexRadioGroup.FlexRadioGroup
//        android:id="@+id/frg_car_powers"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:flexWrap="wrap" />
//
//
//</LinearLayout>
//<LinearLayout
//            android:layout_width="match_parent"
//                    android:layout_height="44dp"
//                    android:layout_marginBottom="20dp"
//                    android:layout_marginTop="8dp"
//                    android:orientation="horizontal">
//
//<Button
//                android:id="@+id/btn_reset"
//                        android:layout_width="0dp"
//                        android:layout_height="match_parent"
//                        android:layout_weight="1"
//                        android:background="@drawable/home_shape_car_filter_left"
//                        android:text="重置"
//                        android:textColor="@color/home_white"
//                        android:textSize="14sp" />
//
//<Button
//                android:id="@+id/btn_submit"
//                        android:layout_width="0dp"
//                        android:layout_height="match_parent"
//                        android:layout_weight="1"
//                        android:background="@drawable/home_shape_car_filter_right"
//                        android:text="确定"
//                        android:textColor="@color/home_white"
//                        android:textSize="14sp" />
//</LinearLayout>
//
//
//</LinearLayout>
//
//<LinearLayout
//        android:id="@+id/ll_close_pwindow"
//                android:layout_width="match_parent"
//                android:layout_height="match_parent"
//                android:background="@color/home_translucency"
//                android:clickable="true"
//                android:orientation="horizontal">
//
//</LinearLayout>
//</LinearLayout>


//布局文件：home_car_filter_label
//<?xml version="1.0" encoding="utf-8"?>
//<RadioButton xmlns:android="http://schemas.android.com/apk/res/android"
//        android:layout_width="wrap_content"
//        android:layout_height="28dp"
//        android:background="@drawable/home_car_filter_select"
//        android:button="@null"
//        android:gravity="center"
//        android:textSize="11sp"
//        android:checked="false"
//        android:text="ssssss"
//        android:textColor="@color/home_car_filter_select" />
//
