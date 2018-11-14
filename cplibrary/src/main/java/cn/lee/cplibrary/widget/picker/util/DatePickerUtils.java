package cn.lee.cplibrary.widget.picker.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ComDialogUtil;
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

    public static int getDialogTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.style.cp_MyDialogTheme;
//            return android.R.style.Theme_NoTitleBar;
//            return android.R.style.Theme_Light_NoTitleBar;
//            id:style/Theme.Light.NoTitleBar
//            return android.R.style.Theme_Holo_Dialog_NoActionBar;
//            return android.R.style.Theme_Material_Light_Dialog_Alert;
        } else {
            return AlertDialog.THEME_HOLO_LIGHT;
        }
    }
    /**
     * 显示年月日
     */
    public static void showDate(final Context context,
                                       final DateCallBack callBack) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        View view = LayoutInflater.from(context).inflate(R.layout.cp_date_time_picker_layout, null);
        final Dialog dialog = ComDialogUtil.getBottomDialog(context, true, view);
        view.findViewById(R.id.new_hour).setVisibility(View.GONE);
        view.findViewById(R.id.new_mins).setVisibility(View.GONE);
        year =  view.findViewById(R.id.new_year);
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
    public static void showDateAndTime(final Context context,
                                       final DateAndTimeCallBack callBack) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMin = c.get(Calendar.MINUTE);

        final AlertDialog dialog = new AlertDialog.Builder(context,getDialogTheme()).create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.cp_date_time_picker_layout);
        // 设置宽高
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.cp_AnimBottomFade);

        year = (WheelView) window.findViewById(R.id.new_year);
        initYear(context);
        month = (WheelView) window.findViewById(R.id.new_month);
        initMonth(context);
        day = (WheelView) window.findViewById(R.id.new_day);
        initDay(context, curYear, curMonth);
        hour = (WheelView) window.findViewById(R.id.new_hour);
        initHour(context);
        mins = (WheelView) window.findViewById(R.id.new_mins);
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
        TextView ok = (TextView) window.findViewById(R.id.set);
        TextView cancel = (TextView) window.findViewById(R.id.cancel);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // String time = String.format(Locale.CHINA,
                // "%04d年%02d月%02d日 %02d时%02d分",
                // year.getCurrentItem() + 1950,
                // month.getCurrentItem() + 1, day.getCurrentItem() + 1,
                // hour.getCurrentItem(), mins.getCurrentItem());
                // Toast.makeText(context, time, Toast.LENGTH_LONG).show();
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
        LinearLayout cancelLayout = (LinearLayout) window
                .findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return false;
            }
        });

    }
    private static void updateDays(Context context, WheelView year,
                                   WheelView month, WheelView day) {
		String label=" 日";
//        String label = "";
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
    private static void initYear(Context context) {
        String label=" 年";
//        String label = "";
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
    private static void initMonth(Context context) {
        String label=" 月";
//        String label = "";
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
    private static void initDay(Context context, int y, int m) {
        String label=" 日";
//        String label = "";
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
    private static void initHour(Context context) {
        String label=" 时";
//        String label = "";
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
    private static void initMins(Context context) {
        String label=" 分";
//        String label = "";
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

    public static String formatDateAndTime(int year, int month, int day,
                                           int hour, int min) {
        String dateAndTime = String.format(Locale.CHINA,
                "%04d-%02d-%02d %02d:%02d", year, month, day, hour, min);
        return dateAndTime;
    }
    public static String formatDate(int year, int month, int day) {
        String date = String.format(Locale.CHINA,
                "%04d-%02d-%02d", year, month, day );
        return date;
    }
    public static String formatDateDot(int year, int month, int day) {
        String date = String.format(Locale.CHINA,
                "%04d.%02d.%02d", year, month, day );
        return date;
    }

}
