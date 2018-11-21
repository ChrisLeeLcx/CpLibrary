package cn.lee.cplibrary.widget.picker.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.widget.picker.adapter.NumericWheelAdapter;
import cn.lee.cplibrary.widget.picker.widget.OnWheelChangedListener;
import cn.lee.cplibrary.widget.picker.widget.WheelView;


/**
 * @author ChrisLee
 */
public class DatePickerUtils {

    private static WheelView year, month, day, mins, hour;
    private static int vsibleItemNum = 6;
    private static boolean isCyclic = true;

    //ui设置
    private Context context;
    private int tBgColor;
    private int tTxtColor;
    private int tTxtSize; //单位 sp ，默认值是7，相当于布局中的14sp
    private String tTitle;
    private boolean isShowLabel;

    private DatePickerUtils(Context context) {
        this.context = context;
    }

    /**
     * 显示年月日
     */
    public void showDate(
            final DateCallBack callBack) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        View view = LayoutInflater.from(context).inflate(R.layout.cp_date_time_picker_layout, null);
        final Dialog dialog = CpComDialog.getBottomDialog(context, true, view);
        setView(view);
        view.findViewById(R.id.new_hour).setVisibility(View.GONE);
        view.findViewById(R.id.new_mins).setVisibility(View.GONE);
        year = view.findViewById(R.id.new_year);
        initYear(context);
        month = view.findViewById(R.id.new_month);
        initMonth(context);
        day = view.findViewById(R.id.new_day);
        initDay(context, curYear, curMonth);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(context, year, month, day);

            }
        };
        year.addChangingListener(listener);
        month.addChangingListener(listener);

        // 设置当前时间
        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        year.setVisibleItems(vsibleItemNum);
        month.setVisibleItems(vsibleItemNum);
        day.setVisibleItems(vsibleItemNum);
        // 设置监听
        view.findViewById(R.id.set).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sure(year.getCurrentItem() + 1950,
                        month.getCurrentItem() + 1, day.getCurrentItem() + 1);
                dialog.cancel();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel();
                dialog.cancel();
            }
        });

    }

    /**
     * 显示全部日期
     */
    public void showDateAndTime(
            final DateAndTimeCallBack callBack) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMin = c.get(Calendar.MINUTE);
        View view = LayoutInflater.from(context).inflate(R.layout.cp_date_time_picker_layout, null);
        final Dialog dialog = CpComDialog.getBottomDialog(context, true, view);
        setView(view);
        year = (WheelView) view.findViewById(R.id.new_year);
        initYear(context);
        month = (WheelView) view.findViewById(R.id.new_month);
        initMonth(context);
        day = (WheelView) view.findViewById(R.id.new_day);
        initDay(context, curYear, curMonth);
        hour = (WheelView) view.findViewById(R.id.new_hour);
        initHour(context);
        mins = (WheelView) view.findViewById(R.id.new_mins);
        initMins(context);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(context, year, month, day);

            }
        };
        year.addChangingListener(listener);
        month.addChangingListener(listener);
        // 设置当前时间
        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        hour.setCurrentItem(curHour);
        mins.setCurrentItem(curMin);

        year.setVisibleItems(vsibleItemNum);
        month.setVisibleItems(vsibleItemNum);
        day.setVisibleItems(vsibleItemNum);
        hour.setVisibleItems(vsibleItemNum);
        mins.setVisibleItems(vsibleItemNum);
        // 设置监听
        TextView ok = (TextView) view.findViewById(R.id.set);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sure(year.getCurrentItem() + 1950,
                        month.getCurrentItem() + 1, day.getCurrentItem() + 1,
                        hour.getCurrentItem(), mins.getCurrentItem());
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel();
                dialog.cancel();
            }
        });
    }

    /**
     * 设置对话框外观
     */
    private void setView(View titleBar, TextView tvTitle, TextView tvLeftBtn, TextView tvRightBtn) {
        titleBar.setBackgroundColor(tBgColor);
        if (!ObjectUtils.isEmpty(tTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(tTitle);
            tvTitle.setTextColor(tTxtColor);
            tvTitle.setTextSize(ScreenUtil.sp2px(context, tTxtSize));
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvLeftBtn.setTextColor(tTxtColor);
        tvRightBtn.setTextColor(tTxtColor);
        tvLeftBtn.setTextSize(ScreenUtil.sp2px(context, tTxtSize));
        tvRightBtn.setTextSize(ScreenUtil.sp2px(context, tTxtSize));
    }

    /**
     * 设置对话框外观
     */
    private void setView(View layout) {
        View titleBar = layout.findViewById(R.id.rl_title);
        TextView tvTitle = layout.findViewById(R.id.tv_title);
        TextView tvLeftBtn = layout.findViewById(R.id.cancel);
        TextView tvRightBtn = layout.findViewById(R.id.set);
        setView(titleBar, tvTitle, tvLeftBtn, tvRightBtn);
    }

    private void updateDays(Context context, WheelView year,
                            WheelView month, WheelView day) {
        String label = isShowLabel ? " 日" : "";
        int maxDays = getDays(context, year.getCurrentItem() + 1950,
                month.getCurrentItem() + 1);
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 1, maxDays, "%02d");
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(isCyclic);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);

    }

    /**
     * 初始化年
     */
    private void initYear(Context context) {
        String label = isShowLabel ? " 年" : "";
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 1950, 2050);
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(isCyclic);
    }

    /**
     * 初始化月
     */
    private void initMonth(Context context) {
        String label = isShowLabel ? " 月" : "";
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 1, 12, "%02d");
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(isCyclic);
    }

    /**
     * 初始化天
     */
    private void initDay(Context context, int y, int m) {
        String label = isShowLabel ? " 日" : "";
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 1, getDays(context, y, m), "%02d");
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(isCyclic);
    }

    /**
     * 初始化时
     */
    private void initHour(Context context) {
        String label = isShowLabel ? " 时" : "";
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 0, 23, "%02d");
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(isCyclic);
    }

    /**
     * 初始化分
     */
    private void initMins(Context context) {
        String label = isShowLabel ? " 分" : "";
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
                context, 0, 59, "%02d");
        numericWheelAdapter.setLabel(label);
        // numericWheelAdapter.setTextSize(15); 设置字体大小
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(isCyclic);
    }

    // 获取当月天数
    @SuppressLint("SimpleDateFormat")
    private static int getDays(Context context, int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        String source = String.valueOf(year) + "年" + String.valueOf(month)
                + "月";
        Date date = null;
        try {
            date = format.parse(source);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public interface DateAndTimeCallBack {
        public void sure(int year, int month, int day, int hour, int min);

        public void cancel();
    }

    public interface DateCallBack {
        public void sure(int year, int month, int day);

        public void cancel();
    }

    /**
     * @return :格式 例如 2018-02-09 05：30
     */
    public static String format(int year, int month, int day,
                                int hour, int min) {
        String dateAndTime = String.format(Locale.CHINA,
                "%04d-%02d-%02d %02d:%02d", year, month, day, hour, min);
        return dateAndTime;
    }

    /**
     * @return :格式 例如 2018-02-09
     */
    public static String format(int year, int month, int day) {
        String date = String.format(Locale.CHINA,
                "%04d-%02d-%02d", year, month, day);
        return date;
    }

    /**
     * @return :格式 例如 2018.02.09
     */
    public static String formatDateDot(int year, int month, int day) {
        String date = String.format(Locale.CHINA,
                "%04d.%02d.%02d", year, month, day);
        return date;
    }


    private void settBgColor(int tBgColor) {
        this.tBgColor = tBgColor;
    }


    private void settTxtColor(int tTxtColor) {
        this.tTxtColor = tTxtColor;
    }

    private void settTxtSize(int tTxtSize) {
        this.tTxtSize = tTxtSize;
    }

    private void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    private void setShowLabel(boolean showLabel) {
        isShowLabel = showLabel;
    }

    public static class Builder {
        private Context context;
        private int tBgColor = Color.parseColor("#1086D1");//时间选择框标题栏背景色
        private int tTxtColor = Color.parseColor("#FFFFFF");//标题栏：文字颜色（确定、取消按钮、标题）
        private int tTxtSize = 7;//标题栏：文字大小（确定、取消按钮、标题） 单位sp
        private String tTitle;//标题栏：标题文字
        private boolean isShowLabel = true;//时间控件是否显示label 年月日等

        private Builder(Context context) {
            this.context = context;
        }

        public static Builder builder(Context context) {
            return new Builder(context);
        }

        public DatePickerUtils build() {
            DatePickerUtils util = new DatePickerUtils(context);
            util.settBgColor(tBgColor);
            util.settTxtColor(tTxtColor);
            util.settTxtSize(tTxtSize);
            util.settTitle(tTitle);
            util.setShowLabel(isShowLabel);
            return util;
        }
        public Builder settBgColor(int tBgColor) {
            this.tBgColor = tBgColor;
            return this;
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

        public Builder settTitle(String tTitle) {
            this.tTitle = tTitle;
            return this;
        }

        public Builder setShowLabel(boolean showLabel) {
            isShowLabel = showLabel;
            return this;
        }
    }
}
