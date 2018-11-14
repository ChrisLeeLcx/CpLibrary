package cn.lee.cplibrary.widget.viewflipper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewFlipper;

/**
 * function:自定义的ViewFlipper，功能可以绑定适配器，有setOnItemClickListener功能
 ViewFlipper的XML属性
 android:autoStart：设置自动加载下一个View
 android:flipInterval：设置View之间切换的时间间隔
 android:inAnimation：设置切换View的进入动画
 android:outAnimation：设置切换View的退出动画
 使用方式：（1）给 CLViewFlipper设置adapter
 （2）一定要调用 adapter.notifyDataSetChanged();方法数据才会显示
 * @author ChrisLee
 * @date 2018/4/8
 */

public class CLViewFlipper extends ViewFlipper {
    private CLViewFlipperAdapter adapter;
    private OnItemClickListener mOnItemClickListener;
    public CLViewFlipper(Context context) {
        super(context);
    }
    public CLViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setAdapter(CLViewFlipperAdapter adapter) {
        this.adapter = adapter;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public OnItemClickListener getOnItemClickListener() {
        return  this.mOnItemClickListener;
    }
    public interface OnItemClickListener {
        /**
         * @param parent ：item 綁定的 ViewFlipper
         * @param child ：点击的item布局
         * @param position ： 点击的item的位置
         * @param id ： 点击的item的id,赋值的地方在 ViewFlipper绑定的Adapter的getItemId方法中
         */
        void onItemClick(ViewFlipper parent, View child, int position, String id);
    }

    @Override
    public View getCurrentView() {
        return super.getCurrentView();
    }
}
/**
 * function:ViewFlipper的使用，模仿淘宝首页闪屏广告
 * ViewFlipper的XML属性
 * android:autoStart：设置自动加载下一个View
 * android:flipInterval：设置View之间切换的时间间隔
 * android:inAnimation：设置切换View的进入动画
 * android:outAnimation：设置切换View的退出动画
 * <p>
 * 下面是ViewFlipper常用的方法介绍，除了可以设置上面的属性之外，还提供了其他方法
 * isFlipping： 判断View切换是否正在进行
 * setFilpInterval：设置View之间切换的时间间隔
 * startFlipping：开始View的切换，而且会循环进行
 * stopFlipping：停止View的切换
 * setOutAnimation：设置切换View的退出动画
 * setInAnimation：设置切换View的进入动画
 * showNext： 显示ViewFlipper里的下一个View
 * showPrevious：显示ViewFlipper里的上一个View
 * <p>
 * <p>
 *  自定义的ViewFlipper的Demo的使用方法:拷贝CLViewFlipper和CLViewFlipperAdapter，然后 按照本类中的使用方法即可
 * Created by ChrisLee on 2018/4/8.
 */