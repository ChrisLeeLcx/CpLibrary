package cn.lee.cplibrary.widget.keyboardview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import cn.lee.cplibrary.R;


/**
 * Created by Administrator on 2018/3/25.
 * 仿支付宝数字键盘
 * <p>
 * 1、集成系统的 KeyBoardView 类，在初始化时初始化键盘布局，设置 KeyBoard 对象。
 * 2、实现 OnKeyboardActionListener 接口，处理按键交互事件。
 * 3、根据需求绘制按键背景和按键图标。
 * 4、设置监听器，将输入的内容回调给调用方。
 * android:background="#919191"
 * android:keepScreenOn="true"
 * android:keyBackground="@drawable/selector_key_board"    <! --设置按键的背景选择器 -->
 * android:keyTextColor="@android:color/black"
 * android:keyTextSize="26sp"
 * android:shadowRadius="0" // shadowRadius = 0 ,防止按键数字显示模糊
 */

public class PwdKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = "PwdKeyboardView";


    private static final int KEY_EMPTY = -10;

    private int delKeyBackgroundColor = 0xffcccccc;

    private Rect keyIconRect;


    public PwdKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "PwdKeyboardView: two params");
        init(context);

    }

    public PwdKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "PwdKeyboardView: three params");
        init(context);
    }


    private void init(Context context) {
        Keyboard keyboard = new Keyboard(context, R.xml.cp_key_password_number);
        setKeyboard(keyboard);
        setEnabled(true);
        setFocusable(true);
        setPreviewEnabled(false);  // 设置点击按键不显示预览气泡
        setOnKeyboardActionListener(this);
    }

    /**
     * 重新绘制删除按键和空白键
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == KEY_EMPTY) {
                drawKeyBackground(key, canvas, delKeyBackgroundColor);
            }
            if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                drawKeyBackground(key, canvas, delKeyBackgroundColor);
                drawKeyIcon(key, canvas, getResources().getDrawable(R.drawable.ic_delete));
            }
        }

    }

    /**
     * 绘制按键的背景
     *
     * @param key
     * @param canvas
     * @param color
     */
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas, int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    /**
     * 绘制按键的 icon
     *
     * @param key
     * @param canvas
     * @param iconDrawable
     */
    private void drawKeyIcon(Keyboard.Key key, Canvas canvas, Drawable iconDrawable) {
        if (iconDrawable == null) {
            return;
        }
        // 计算按键icon 的rect 范围
        if (keyIconRect == null || keyIconRect.isEmpty()) {
            // 得到 keyicon 的显示大小，因为图片放在不同的drawable-dpi目录下，显示大小也不一样
            int intrinsicWidth = iconDrawable.getIntrinsicWidth();
            int intrinsicHeight = iconDrawable.getIntrinsicHeight();
            int drawWidth = intrinsicWidth;
            int drawHeight = intrinsicHeight;
            // 限制图片的大小，防止图片按键范围
            if (drawWidth > key.width) {
                drawWidth = key.width;
                // 此时高就按照比例缩放
                drawHeight = (int) (drawWidth * 1.0f / intrinsicWidth * intrinsicHeight);
            } else if (drawHeight > key.height) {
                drawHeight = key.height;
                drawWidth = (int) (drawHeight * 1.0f / intrinsicHeight * intrinsicWidth);
            }
            // 获取图片的 x,y 坐标,图片在按键的正中间
            int left = key.x + key.width / 2 - drawWidth / 2;
            int top = key.y + key.height / 2 - drawHeight / 2;
            keyIconRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        }

        if (keyIconRect != null && !keyIconRect.isEmpty()) {
            iconDrawable.setBounds(keyIconRect);
            iconDrawable.draw(canvas);
        }
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d(TAG, "onKey: primaryCode = " + primaryCode + ", keyCodes = " + Arrays.toString(keyCodes));
        if (primaryCode == KEY_EMPTY) {
            return;
        }
        if (listener != null) {
            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                listener.onDelete();
            } else {
                listener.onInput(String.valueOf((char) primaryCode));
            }
        }


    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public interface OnKeyListener {
        void onInput(String text);

        void onDelete();
    }

    private OnKeyListener listener;

    public void setOnKeyListener(OnKeyListener listener) {
        this.listener = listener;
    }

}
