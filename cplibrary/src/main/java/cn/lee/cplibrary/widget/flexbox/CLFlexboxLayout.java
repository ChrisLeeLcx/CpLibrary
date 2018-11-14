package cn.lee.cplibrary.widget.flexbox;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能：单击、单选、多选事件
 * @author: ChrisLee
 * @time: 2018/7/26
 */

public class CLFlexboxLayout extends FlexboxLayout {
    public CLFlexboxLayout(Context context) {
        this(context, null);
    }

    public CLFlexboxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CLFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initChildClickEvent();//子View绘制完成方可以
    }

    //多选
    List<Integer> pois = new ArrayList<>();
    //单选:选中的按钮的position,如果一个都没有选中 则为-1
    int radioPosition=-1;
    /**
     * 子控件点击事件
     */
    private void initChildClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            final Integer finalI = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {//单击事件
                        mOnItemClickListener.onItemClick(CLFlexboxLayout.this, v, finalI);
                    } else if (onItemRadioCheckListener != null) {//单选事件
                       if (radioPosition == finalI) {
                           radioPosition =-1;
                           onItemRadioCheckListener.onItemRadioCheck(CLFlexboxLayout.this,v,false,radioPosition);
                       } else {
                           radioPosition =finalI;
                           onItemRadioCheckListener.onItemRadioCheck(CLFlexboxLayout.this,v,true,radioPosition);
                       }
                    } else if (mOnItemMultipleCheckListener != null) {//多选事件
                        if (pois.contains(finalI)) {
                            pois.remove(finalI);//注意：finalI必须是Object类型 ，如果是int则会被认为是index 而导致出错
                            Collections.sort(pois);
                            mOnItemMultipleCheckListener.onItemMultipleCheck(CLFlexboxLayout.this, v, false,pois );
                        } else {
                            pois.add(finalI);
                            Collections.sort(pois);
                            mOnItemMultipleCheckListener.onItemMultipleCheck(CLFlexboxLayout.this, v, true, pois);
                        }
                    }

                }
            });
        }
    }

    /**
     * 控件的点击事件、也可以用在不可以反选的单选
     */
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CLFlexboxLayout fbl, View view, int position);
    }
    /**
     * 控件的单选事件 反选
     */
    private OnItemRadioCheckListener onItemRadioCheckListener;

    public void setOnItemRadioCheckListener(@Nullable OnItemRadioCheckListener listener) {
        onItemRadioCheckListener = listener;
    }

    public interface OnItemRadioCheckListener {
        void onItemRadioCheck(CLFlexboxLayout fbl, View view, boolean isChecked, int position);
    }

    /**
     * 控件的多选事件 反选
     */
    private OnItemMultipleCheckListener mOnItemMultipleCheckListener;

    public void setOnItemMultipleCheckListener(@Nullable OnItemMultipleCheckListener listener) {
        mOnItemMultipleCheckListener = listener;
    }

    public interface OnItemMultipleCheckListener {
        /**
         * @param pois :选中的item的position集合
         */
        void onItemMultipleCheck(CLFlexboxLayout fbl, View view, boolean isChecked, List<Integer> pois);
    }

}
