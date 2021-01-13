package cn.lee.cplibrary.widget.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 重写使其允许设置宽高
 * 原声VideoView,它是不允许你随意的修改宽度和高度的，所以我们要！
 * Created by ChrisLee on 2021/1/13.
 */
public class CpVideoView extends VideoView {
    public CpVideoView(Context context) {
        super(context);
    }

    public CpVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CpVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
