package cn.lee.cplibrary.widget.picker.gl;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.widget.picker.widget.GregorianLunarCalendarView;


/**
 * GLCPikerUtil是GregorianLunarCalendarPikerUtil的缩写,即 农历+公历日期选择模式 同时支持公历+农历的无缝切换
 * Created by ChrisLee on 2019/12/10.
 */

public class GLCPikerUtil {
    private GregorianLunarCalendarView mGLCView;
    private TextView tvTitle, tvLeftBtn, tvRightBtn;
    private RadioButton rbGregorian, rbLunar;
    private RadioGroup group;
    private int visibleItemNum = 3;
    private boolean isCyclic, showTextBgSelected, showDivider;
    private String yHint = "", mHint = "", dHint = "", hHint = "";
    private boolean mScrollAnim = true;  //true to use scroll anim when switch picker passively
    private int mThemeColorG = 0xff3388ff;//阳历主题色、选中的文字颜色、分割线线颜色
    private int mThemeColorL = 0xffee5544;//阴历主题色、选中的文字颜色、分割线线颜色
    private int mNormalTextColor = 0xFF555555;//正常文字颜色
    private int bgColorSelected = 0xFFF5F5F9;//选中文字的背景颜色
    //ui设置
    private Context context;
    private int tTxtColor, btnCancelColor, btnSureColor;
    private int tTxtSize; //单位 sp ，默认值是17sp
    private boolean isShowLabel;


    private GLCPikerUtil(Context context) {
        this.context = context;
    }


    /**
     * 显示公历+农历对话框
     */
    public void showGLCDialog(final GLCCallBack callBack) {
        final View view = LayoutInflater.from(context).inflate(R.layout.cp_dialog_glc_picker, null);
        final Dialog dialog = CpComDialog.getCenterDialog(context, false, view);
        findAndSetView(view);
        initCalendar();
        // 设置监听
        tvRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sure(mGLCView.getCalendarData(), view);
                dialog.cancel();
            }
        });
        tvLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel();
                dialog.cancel();
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_gregorian) {
                    toGregorianMode();
                } else if (checkedId == R.id.rb_lunar) {
                    toLunarMode();
                }
                setTitleTxt(mGLCView.getCalendarData());
            }
        });
        mGLCView.setOnDateChangedListener(new GregorianLunarCalendarView.OnDateChangedListener() {
            @Override
            public void onDateChanged(GregorianLunarCalendarView.CalendarData calendarData) {
                setTitleTxt(calendarData);
            }
        });
    }

    /**
     * 初始化对话框页面
     */
    private void initCalendar() {
        mGLCView.init();
        ChineseCalendar calendar = (ChineseCalendar) mGLCView.getCalendarData().getCalendar();
        tvTitle.setText(calendar.getCalendarTxt(null));
    }

    //转成阳历模式
    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    //转成阴历模式
    private void toLunarMode() {
        mGLCView.toLunarMode();
    }

    /**
     * 查找控件设置对话框外观
     */
    private void findAndSetView(View layout) {
        mGLCView = layout.findViewById(R.id.calendar_view);
        tvTitle = layout.findViewById(R.id.tv_title);
        group = layout.findViewById(R.id.group);
        rbGregorian = layout.findViewById(R.id.rb_gregorian);
        rbLunar = layout.findViewById(R.id.rb_lunar);
        tvLeftBtn = layout.findViewById(R.id.cancel);
        tvRightBtn = layout.findViewById(R.id.set);
        setView(tvTitle, tvLeftBtn, tvRightBtn);
        //以下设置需要在 mGLCView.init()之前完成
        mGLCView.setCyclic(isCyclic);//是否循环
        //选择器文本说明
        mGLCView.getNumberPickerYear().setHintText(yHint);
        mGLCView.getNumberPickerMonth().setHintText(mHint);
        mGLCView.getNumberPickerDay().setHintText(dHint);
        mGLCView.getNumberPickerHour().setHintText(hHint);
        //设置是否显示滚动公话、选择器颜色
        mGLCView.setmScrollAnim(mScrollAnim);
        mGLCView.setNormalColor(mNormalTextColor);
        mGLCView.setmThemeColorG(mThemeColorG);
        mGLCView.setmThemeColorL(mThemeColorL);

        mGLCView.setShownCount(visibleItemNum);
        mGLCView.setShowDivider(showDivider);
        mGLCView.setShowTextBgSelected(showTextBgSelected);
        mGLCView.setBgColorSelected(bgColorSelected);

    }

    /**
     * 设置对话框外观
     */
    private void setView(TextView tvTitle, TextView tvLeftBtn, TextView tvRightBtn) {
        tvTitle.setTextColor(tTxtColor);
        tvLeftBtn.setTextColor(btnCancelColor);
        tvRightBtn.setTextColor(btnSureColor);
        tvTitle.setTextSize(ScreenUtil.sp(context, tTxtSize));
        tvLeftBtn.setTextSize(ScreenUtil.sp(context, tTxtSize));
        tvRightBtn.setTextSize(ScreenUtil.sp(context, tTxtSize));
    }

    public void setTitleTxt(GregorianLunarCalendarView.CalendarData cdata) {
        ChineseCalendar calendar = (ChineseCalendar) cdata.getCalendar();
        int i = group.getCheckedRadioButtonId();
        if (i == R.id.rb_gregorian) {
            tvTitle.setText(calendar.getGregorianPickedTxt());
        } else if (i == R.id.rb_lunar) {
            tvTitle.setText(calendar.getLunarPickedTxt());
        }

    }

    private void settTxtColor(int tTxtColor) {
        this.tTxtColor = tTxtColor;
    }

    public void setBtnCancelColor(int btnCancelColor) {
        this.btnCancelColor = btnCancelColor;
    }

    public void setBtnSureColor(int btnSureColor) {
        this.btnSureColor = btnSureColor;
    }

    private void settTxtSize(int tTxtSize) {
        this.tTxtSize = tTxtSize;
    }

    private void setShowLabel(boolean showLabel) {
        isShowLabel = showLabel;
    }

    public void setVisibleItemNum(int visibleItemNum) {
        this.visibleItemNum = visibleItemNum;
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    public void setyHint(String yHint) {
        this.yHint = yHint;
    }

    public void setmHint(String mHint) {
        this.mHint = mHint;
    }

    public void setdHint(String dHint) {
        this.dHint = dHint;
    }

    public void sethHint(String hHint) {
        this.hHint = hHint;
    }

    public void setmScrollAnim(boolean mScrollAnim) {
        this.mScrollAnim = mScrollAnim;
    }

    public void setmThemeColorG(int mThemeColorG) {
        this.mThemeColorG = mThemeColorG;
    }

    public void setmThemeColorL(int mThemeColorL) {
        this.mThemeColorL = mThemeColorL;
    }

    public void setmNormalTextColor(int mNormalTextColor) {
        this.mNormalTextColor = mNormalTextColor;
    }

    public void setShowTextBgSelected(boolean showTextBgSelected) {
        this.showTextBgSelected = showTextBgSelected;
    }

    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
    }

    public void setBgColorSelected(int bgColorSelected) {
        this.bgColorSelected = bgColorSelected;
    }

    public static class Builder {
        private Context context;
        private int tTxtColor = Color.parseColor("#373737");//标题文字颜色
        private int btnCancelColor = Color.parseColor("#B5B5B5");//取消按钮颜色
        private int btnSureColor = Color.parseColor("#FFFFFF");//确定按钮颜色
        private int tTxtSize = 17;//标题栏：文字大小（确定、取消按钮、标题） 单位sp
        private boolean isShowLabel = true;//时间控件是否显示label 年月日等
        private boolean isCyclic = true;//数据是否循环显示
        private boolean showTextBgSelected = false;//是否显示选中的文字的背景
        private boolean showDivider = true;//是否显示分割线
        private int visibleItemNum = 3;
        private String yHint = "", mHint = "", dHint = "", hHint = "";//年，月，日，时的Label文字说明
        private boolean mScrollAnim = true;  //true to use scroll anim when switch picker passively
        private int mThemeColorG = 0xffae1a1e;//阳历主题色、选中的文字颜色、分割线线颜色 十六进制样式
        private int mThemeColorL = 0xffae1a1e;//阴历主题色、选中的文字颜色、分割线线颜色 十六进制样式
        private int mNormalTextColor = 0xFF555555;//正常文字颜色 十六进制样式
        private int bgColorSelected = 0xFFF5F5F9;//选中文字的背景颜色


        private Builder(Context context) {
            this.context = context;
        }

        public static Builder builder(Context context) {
            return new Builder(context);
        }

        public GLCPikerUtil build() {
            GLCPikerUtil util = new GLCPikerUtil(context);
            util.settTxtColor(tTxtColor);
            util.setBtnSureColor(btnSureColor);
            util.setBtnCancelColor(btnCancelColor);
            util.settTxtSize(tTxtSize);
            util.setShowLabel(isShowLabel);
            util.setCyclic(isCyclic);
            util.setVisibleItemNum(visibleItemNum);
            util.setyHint(yHint);
            util.setmHint(mHint);
            util.setdHint(dHint);
            util.sethHint(hHint);
            util.setmScrollAnim(mScrollAnim);
            util.setmThemeColorG(mThemeColorG);
            util.setmThemeColorL(mThemeColorL);
            util.setmNormalTextColor(mNormalTextColor);
            util.setShowDivider(showDivider);
            util.setShowTextBgSelected(showTextBgSelected);
            util.setBgColorSelected(bgColorSelected);
            return util;
        }


        public Builder settTxtColor(int tTxtColor) {
            this.tTxtColor = tTxtColor;
            return this;
        }

        /**
         * @param tTxtSize 单位 sp ，默认值是7，相当于布局中的14sp
         */
        public Builder settTxtSize(int tTxtSize) {
            this.tTxtSize = tTxtSize;
            return this;
        }

        public Builder setShowLabel(boolean showLabel) {
            isShowLabel = showLabel;
            return this;
        }

        /**
         * @param cyclic 设置数据是否循环显示
         */
        public Builder setCyclic(boolean cyclic) {
            isCyclic = cyclic;
            return this;
        }

        /**
         * @param visibleItemNum 设置显示的可见日期数目 必须是奇数
         * @return
         */
        public Builder setVisibleItemNum(int visibleItemNum) {
            this.visibleItemNum = visibleItemNum;
            return this;
        }

        /**
         * 取消按钮颜色
         */
        public Builder setBtnCancelColor(int btnCancelColor) {
            this.btnCancelColor = btnCancelColor;
            return this;
        }

        /**
         * 确定按钮颜色
         */
        public Builder setBtnSureColor(int btnSureColor) {
            this.btnSureColor = btnSureColor;
            return this;
        }

        public Builder setyHint(String yHint) {
            this.yHint = yHint;
            return this;
        }

        public Builder setmHint(String mHint) {
            this.mHint = mHint;
            return this;
        }

        public Builder setdHint(String dHint) {
            this.dHint = dHint;
            return this;
        }

        public Builder sethHint(String hHint) {
            this.hHint = hHint;
            return this;
        }

        public Builder setmScrollAnim(boolean mScrollAnim) {
            this.mScrollAnim = mScrollAnim;
            return this;

        }

        /**
         * @param mThemeColorG 十六进制样式
         * @return
         */
        public Builder setmThemeColorG(int mThemeColorG) {
            this.mThemeColorG = mThemeColorG;
            return this;

        }

        public Builder setmThemeColorL(int mThemeColorL) {
            this.mThemeColorL = mThemeColorL;
            return this;

        }
        /**
         * 十六进制样式
         */
        public Builder setmNormalTextColor(int mNormalTextColor) {
            this.mNormalTextColor = mNormalTextColor;
            return this;

        }

        public Builder setShowTextBgSelected(boolean showTextBgSelected) {
            this.showTextBgSelected = showTextBgSelected;
            return this;

        }

        public Builder setShowDivider(boolean showDivider) {
            this.showDivider = showDivider;
            return this;

        }
        /**
         * 十六进制样式
         */
        public Builder setBgColorSelected(int bgColorSelected) {
            this.bgColorSelected = bgColorSelected;
            return this;

        }
    }


    //-----------------------------工具类-------------------------------------
    public interface GLCCallBack {
        /**
         * @param calendarData 日历数据
         * @param layout       整个日历布局
         */
        void sure(GregorianLunarCalendarView.CalendarData calendarData, View layout);

        void cancel();
    }
}
