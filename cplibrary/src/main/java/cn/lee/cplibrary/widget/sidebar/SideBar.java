package cn.lee.cplibrary.widget.sidebar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lee.cplibrary.util.ScreenUtil;


/**
 * 字母索引View，常用在ListView、recycleView的右侧
 */
public class SideBar extends View {
    //配置项：可以后期更改
    private int backgroundColor_press = Color.parseColor("#CCCCCC");//手按下的背景颜色
    private int backgroundColor_normal = Color.TRANSPARENT;//正常背景颜色
    private int textColor_choosed = Color.parseColor("#FF5858");    //文字颜色：选中的
    private int textColor_normal = Color.parseColor("#4446F5");    //文字颜色：正常的#4446F5
    private int textSize = ScreenUtil.sp(getContext(), 10);//文字大小10sp
    private int padding = ScreenUtil.dp2px(getContext(), 5);//文字的内边距大小px
    public static final String PYS_HEADER = "热";//如果列表有头部，则PYS_HEADER是该头部的拼音索引
    private String[] arrLetters = new String[]{PYS_HEADER, "*", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};
    //其他
    private List<String> letterList;//字母列表
    private int choosedPosition = -1;//当前选择的字母的索引
    private TextView tv_dialog;//按住字母时，显示选择字母
    private OnLetterClickedListener listener = null;
    Paint paint = new Paint();

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setBackgroundColor(backgroundColor_normal);
        letterList = Arrays.asList(arrLetters);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取当前控件的宽度和高度
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(textSize);
        // 获取每个文字所占据空间的高度
        int singleTextHeight = viewHeight / letterList.size();
        for (int i = 0; i < letterList.size(); i++) {
            if (choosedPosition == i) {
                paint.setColor(textColor_choosed); // 选中的状态
            } else {
                paint.setColor(textColor_normal);
            }
            int textWidth = (int) paint.measureText(letterList.get(i));
            // 参数1：画的文字内容；参数2：内容的起始x坐标；参数3：内容的起始y坐标；参数4：画笔；
//			canvas.drawText(arrLetters[i], (viewWidth - textWidth) / 2,
//					singleTextHeight * (i + 1), paint);
            //ChrisLee:文字垂直居中
            canvas.drawText(letterList.get(i), (viewWidth - textWidth) / 2,
                    singleTextHeight * (i + 1) - singleTextHeight / 2 + textSize / 2, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手指点击处的y轴的坐标
        float y = event.getY();
        // 手指点击处所对应的首字母的索引下标
        int position = (int) (y / (getHeight() / letterList.size()));

        switch (event.getAction()) {
            // 手指抬起
            case MotionEvent.ACTION_UP:
                setBackgroundColor(backgroundColor_normal);
                if (tv_dialog != null) {
                    tv_dialog.setVisibility(View.GONE);
                }
                invalidate();
                break;
            // 手指按下及滑动
            default:
                setBackgroundColor(backgroundColor_press);
                if (position >= 0 && position < letterList.size()) {
                    // 触发自定义的监听器
                    if (listener != null) {
                        listener.onLetterClicked(letterList.get(position));
                    }
                    // 让屏幕中间的textView显示并加载首字母
                    if (tv_dialog != null) {
                        tv_dialog.setVisibility(View.VISIBLE);
                        tv_dialog.setText(letterList.get(position));
                    }
                    choosedPosition = position;

                    // 重新绘制view
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 为SideBar设置显示当前按下的字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextViewDialog(TextView mTextDialog) {
        this.tv_dialog = mTextDialog;
    }

    // 设置内部监听器接口
    public interface OnLetterClickedListener {
        public void onLetterClicked(String letter);
    }

    // 设置调用该内部接口对象的方法
    public void setOnLetterClickedListener(OnLetterClickedListener listener) {
        this.listener = listener;
    }

    /**
     * 重新设置sidebar内容，重新设置后必须传入layoutParams保证排列的美观性
     */
    public void setIndexText(@NonNull ArrayList<String> letters, @NonNull ViewGroup.LayoutParams layoutParams) {
        this.letterList = letters;
        setLayoutParams(layoutParams);
        invalidate();
    }

    /**
     * @param length setIndexText方法传入的数组的长度
     */
    public RelativeLayout.LayoutParams getDefaultLayoutParams(int length) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(padding * 2 + textSize, (padding * 2 + textSize) * length);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return layoutParams;
    }

    /**
     * 选中字母letter
     */
    public void setChoosedLetter(String letter) {
        if (TextUtils.isEmpty(letter)) {
            return;
        }
        for (int i = 0; i < letterList.size(); i++) {
            if (letter.equals(letterList.get(i))) {
                this.choosedPosition = i;
                invalidate();
                return;
            }
        }
    }
}