package cn.lee.cplibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * function:对一组或者一个View进行的某些操作：例如是否可以点击，设置背景，EditText是否可以编辑等
 * Created by ChrisLee on 2018/3/7.
 */

public class ViewUtil {
    private static final String TAG = "debuginfo";

    /**
     * 获取View的宽和高，代表的是view布局结束之后view所代表的宽高，例如：当view的宽为match_parent，内容宽度为60px,父布局宽为720px，则方法返回 720px，
     * 宽：是view右边边框距离父组件的距离减去左边边框距离父组件的距离
     * 高：是view下边边框距离父组件的距离减去上边边框距离父组件的距离
     * 一般用这个方法获取宽高更符合要求哦！！！
     */
    public static void getViewWHByListener(final View view, final OnViewGetWHListener listener) {
        //增加整体布局监听
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = view.getHeight();
                int width = view.getWidth();
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                listener.onViewGetWH(width, height);
            }
        });
    }

    public interface OnViewGetWHListener {
        void onViewGetWH(int width, int height);
    }


    /**
     * 获取测量的View内容的宽度，例如：当view的宽为match_parent或wrap_content时，内容宽度为60px,父布局宽为720px，则方法返回 60px，
     * 若view的宽为固定数值100px，则无论内容多少，方法返回100px
     * return:单位px
     */
    public static int getViewWidthByMeasure(final View view) {
        //测量方法
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        Log.i(TAG, "ViewUtil:" + "getViewHeightByMeasure(view)=" + view.getMeasuredWidth());
        return view.getMeasuredWidth();
    }

    /**
     * 获取测量的View内容的高度，具体解释同方法getViewWidthByMeasure
     *
     * @return：单位px
     */
    public static int getViewHeightByMeasure(final View view) {
        //测量方法
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        Log.i(TAG, "ViewUtil:" + "getViewHeightByMeasure(view)=" + view.getMeasuredHeight());
        return view.getMeasuredHeight();
    }

    /**
     * @param layoutManager：只可以是LinearLayoutManager或者GridLayoutManager
     * @return RecyclerView的第一个可视item的位置
     */
    public static int getFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstItemPosition;
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            firstItemPosition = linearManager.findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {//有header则只会返回0.奇数位置,没有header只返回0,偶数位置
            GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
            firstItemPosition = linearManager.findFirstVisibleItemPosition();
        } else {
            throw new InvalidParameterException("parameter is not LinearLayoutManager or GridLayoutManager");
        }
        return firstItemPosition;
    }

    public static int getFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstItemPosition;
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            firstItemPosition = linearManager.findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {//有header则只会返回0.奇数位置,没有header只返回0,偶数位置
            GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
            firstItemPosition = linearManager.findFirstCompletelyVisibleItemPosition();
        } else {
            throw new InvalidParameterException("parameter is not LinearLayoutManager or GridLayoutManager");
        }
        return firstItemPosition;
    }

    /**
     * 给View设置相同的背景
     *
     * @param resid
     * @param v
     */
    public static void setBackgroundForView(@DrawableRes int resid, View... v) {
        View[] views = v;
        if (resid >= 0) {
            for (View view : views) {
                if (view != null) {
                    view.setBackgroundResource(resid);
                }
            }
        }
    }

    /**
     * 给View设置相同的背景
     *
     * @param v @DrawableRes int id
     */
    @SuppressLint("ResourceAsColor")
    public static void setTextColor(Activity activity, @ColorRes int colorRes, TextView... v) {
        TextView[] views = v;
        if (colorRes >= 0) {
            for (TextView textView : views) {
                if (textView != null) {
                    textView.setTextColor(activity.getResources().getColor(colorRes));
                }
            }
        }
    }

    /**
     * 设置View是否可点击
     *
     * @param isClickable
     * @param v
     */
    public static void setClickableForView(boolean isClickable, View... v) {
        View[] views = v;
        for (View view : views) {
            if (view != null) {
                view.setClickable(isClickable);
            }
        }
    }

    /**
     * 设置View是否可见
     *
     * @param v
     */
    public static void setVisibility(boolean isVisible, View... v) {
        View[] views = v;
        for (View view : views) {
            if (view != null) {
                view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * 设置EditText是否可以编辑
     *
     * @param isEdit：true：可以编辑，否则不可以编辑
     * @param et
     */
    public static void setEditTextEditState(boolean isEdit, EditText... et) {
        EditText[] eTexts = et;
        if (isEdit) {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    editText.setFocusableInTouchMode(true);
                    editText.setFocusable(true);
                    editText.requestFocus();
                }
            }
        } else {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                }
            }
        }
    }


    /**
     * 设置EditText当inputType="passWord"的时候，密码是否可见
     *
     * @param isShow：true：密码可见，false，密码不可见
     * @param et
     */
    public static void setEditTextPassShow(boolean isShow, EditText et) {
        if (et == null) {
            return;
        }
        if (isShow) {
            et.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            et.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    /**
     * 设置view数组的点击监听
     */
    public static void setOnClickListener(@Nullable View.OnClickListener l, View... v) {
        View[] views = v;
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(l);
            }
        }
    }

    public static void setDrawableRight(Activity activity, @DrawableRes int id, TextView view, int pad_dp) {
        Drawable drawable = activity.getResources().getDrawable(id);
        setDrawableRight(activity, drawable, view, pad_dp);
    }

    public static void setDrawableRight(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }

    public static void setDrawableLeft(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }

    /**
     * @param pad_dp:单位是dp
     */
    public static void setDrawableTop(Activity activity, @DrawableRes int id, TextView view, int pad_dp) {
        Drawable drawable = activity.getResources().getDrawable(id);
        setDrawableTop(activity, drawable, view, pad_dp);
    }

    /**
     * @param pad_dp:单位是dp
     */
    public static void setDrawableTop(Activity activity, Drawable drawable, TextView view, int pad_dp) {
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
        view.setCompoundDrawablePadding(ScreenUtil.dp2px(activity, pad_dp));
    }

    /**
     * 给flexboxLayout添加子控件CheckBox
     *
     * @param filters：显示的内容
     * @param dpWidth：添加的子控件宽
     * @param dpHeight：添加的子控件高
     * @param count：水平方向                               一行添添加的子控件的个数
     * @param dpMarginlr：flexboxLayout与屏幕左边距（默认左右边距相同）
     * @param resDrawableId：子控件背景
     * @param resColorId：子控件颜色
     * @param flexboxLayout
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void createFblChilds_Cb(Activity activity, List<String> filters, int dpWidth, int dpHeight, int count, int dpMarginlr, int resDrawableId, int resColorId, final FlexboxLayout flexboxLayout) {
        int pxWidth = ScreenUtil.dp2px(activity, dpWidth);
        int pxHeight = ScreenUtil.dp2px(activity, dpHeight);
        int marginRight = (ScreenUtil.getScreenWidth(activity) - ScreenUtil.dp2px(activity, dpMarginlr) * 2 - pxWidth * count) / (count - 1);
        for (int i = 0; i < filters.size(); i++) {//
            String filter = filters.get(i);
            CheckBox cb = new CheckBox(activity);
            cb.setText(filter);
            cb.setBackground(activity.getResources().getDrawable(resDrawableId));
            cb.setGravity(Gravity.CENTER);
            cb.setTextColor(activity.getResources().getColor(resColorId));
            cb.setTextSize(14);
            cb.setChecked(false);
            cb.setButtonDrawable(null);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(pxWidth, pxHeight);
            if (i % count == (count - 1)) {//每行最后一个 不设置右边距---目的view 平局分布
                lp.setMargins(0, 0, 0, ScreenUtil.dp2px(activity, 10));
            } else {
                lp.setMargins(0, 0, marginRight, ScreenUtil.dp2px(activity, 10));
            }
            cb.setLayoutParams(lp);
            flexboxLayout.addView(cb);
        }
    }

    //车辆出售页面、颜色选择页面、城市定位页面
    public static void createFblChilds_Color(Activity activity, List<String> filters, int resDrawableId, int resColorId, final FlexboxLayout flexboxLayout) {
        createFblChilds_Cb(activity, filters, 72, 28, 4, 12, resDrawableId, resColorId, flexboxLayout);
    }

    /**
     * 重写TabLayout的点击事件（因为Android design支持库中提供的TabLayout默认把Tab的OnClickListener给写死了，开发者调用无效，即相当于没有点击事件）
     * 注意：该方法与 TabLayout.addOnTabSelectedListener是不冲突的哦！！！
     * 使用案例：TabLayout与RecyclerView组合使用，点击tab，RecyclerView滑动到相应位置，滑动RecyclerView到摸个位置，选择相应的tab。
     * 此时，如果使用addOnTabSelectedListener方法的话两边会相互影响。因为tabLayout.getTabAt会触发监听TabLayout.OnTabSelectedListener.
     * 故此使用点击事件setTabLayoutItemClickLisener阻止两边相互影响
     *
     * @param tabLayout
     * @param lisener
     */
    public static void setTabLayoutItemClickLisener(TabLayout tabLayout, final OnTabItemClickLisener lisener) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) {
                return;
            }
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    return;
                }
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        lisener.onClick(v, position);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnTabItemClickLisener {
        /**
         * @param v:点击的tab
         * @param position ：位置
         */
        public void onClick(View v, int position);
    }


    /**
     * 给TextView设置中划线
     */
    public static void setTextLineThrough(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
//        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
    }

    /**
     * 给TextView设置下划线
     */
    public static void setTextLineUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    /**
     * android EditText设置光标到内容最后
     */
    public static void setEditTextCursorLast(EditText et) {
        et.setSelection(et.getText().toString().length());
    }

}
