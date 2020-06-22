package cn.lee.cplibrary.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

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
     * EditText 默认是文字键盘，可以相互切换输入字母，中文等。就是输入还是没有限制的。
     */
    public static void setEtCharFirst(EditText... v) {
        EditText[] et = v;
        for (EditText view : et) {
            if (view != null) {
                view.setRawInputType(Configuration.KEYBOARD_NOKEYS);
            }
        }
    }
    /**
     * EditText失去焦点
     * 父布局或其他View添加android:focusableInTouchMode="true" 才会起作用
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
     * @param isEdit：true：可以编辑复制粘贴，否则不可以编辑不可以复制粘贴
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
                    setCopyAndPasteAble(editText, true);
                }
            }
        } else {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    setCopyAndPasteAble(editText, false);
                }
            }
        }
    }
    /**
     * 设置EditText是否可以编辑
     *
     * @param isEdit：true：可以编辑复制粘贴，否则不可以编辑不可以复制粘贴
     * @param et
     */
    public static void setEditTextNoFocusEditState(boolean isEdit, EditText... et) {
        EditText[] eTexts = et;
        if (isEdit) {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    editText.setFocusableInTouchMode(true);
                    editText.setFocusable(true);
                    setCopyAndPasteAble(editText, true);
                }
            }
        } else {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    setCopyAndPasteAble(editText, false);
                }
            }
        }
    }
    /**
     * 设置EditText是否可以编辑
     *
     * @param isEdit：true：可以编辑，否则不可以编辑，但均可以复制粘贴
     * @param et
     */
    public static void setEditTextInEditState(boolean isEdit, EditText... et) {
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
     *
     * @param activity activity上下文
     * @param et       对应EditText
     */
    public static void setEtFocusChangeSystemFit(final Activity activity, EditText... et) {
        for (int i = 0; i < et.length; i++) {
            et[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
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

    /**
     * 设置EditText是否可以长按复制粘贴
     *
     * @param isAble：true：可以复制粘贴，false不可以复制粘贴
     * @param et
     */
    public static void setCopyAndPasteAble(boolean isAble, EditText... et) {
        EditText[] eTexts = et;
        if (isAble) {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    setCopyAndPasteAble(editText, true);
                }
            }
        } else {
            for (EditText editText : eTexts) {
                if (editText != null) {
                    setCopyAndPasteAble(editText, false);
                }
            }
        }
    }

    /**
     * 设置EdiText是否有复制粘贴菜单
     *
     * @param editText
     * @param isAble   true 有复制粘贴菜单，false禁止
     */
    public static void setCopyAndPasteAble(final EditText editText, final boolean isAble) {
        try {
            if (editText == null) {
                return;
            }
            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {//false 有菜单
                    return !isAble;
                }
            });
            editText.setLongClickable(isAble);//true 可长按
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isAble) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // setInsertionDisabled when user touches the view
                            setInsertionDisabled(editText);
                        }
                    }
                    return false;
                }
            });
            if (isAble) {
                editText.setCustomSelectionActionModeCallback(null);
            } else {
                editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {//长按出来的复选框
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });
            }

//           EditText在横屏编辑的时候会出现一个新的不同的编辑界面，这个界面里还是可以复制粘贴的，因此也要取消这个额外的UI，加上下面这一句：
            // android:imeOptions="flagNoExtractUi"
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
