package cn.lee.cplibrary.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by ChrisLee on 2019/3/26.
 */

public class EditTextUtil {
    /**
     * EditText 默认是数字键盘，可以相互切换输入字母，中文等。就是输入还是没有限制的。
     */
    public static void setEtNumFirst(EditText... v) {
        EditText[] et = v;
        for (EditText view : et) {
            if (view != null) {
                view.setRawInputType(Configuration.KEYBOARD_QWERTY);
            }
        }
    }
    /**
     * EditText失去焦点
     */
    public static void clearEtFocus(EditText... v) {
        EditText[] et = v;
        for (EditText view : et) {
            if (view != null) {
                view.clearFocus();
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
     * android EditText设置光标到内容最后
     */
    public static void setEditTextCursorLast(EditText et) {
        et.setSelection(et.getText().toString().length());
    }
    /**
     * 防止EditText输入时，软键盘将下面布局顶上去
     * @param activity activity上下文
     * @param et 对应EditText
     */
    public static void setEtFocusChangeSystemFit(final Activity activity, EditText...et) {
        for (int i=0;i<et.length;i++){
            et[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    }
                }
            });
        }
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
