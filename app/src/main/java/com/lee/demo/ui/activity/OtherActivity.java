package com.lee.demo.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.BaseApplication;
import com.lee.demo.base.SwipeBackActivity;

import java.text.DecimalFormat;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.NotificationsUtils;
import cn.lee.cplibrary.util.PhoneCallUtil;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.widget.rangeseekbar.OnRangeChangedListener;
import cn.lee.cplibrary.widget.rangeseekbar.RangeSeekBar;
import cn.lee.cplibrary.widget.rangeseekbar.SeekBar;
import cn.lee.cplibrary.widget.ratingbar.MaterialRatingBar;

/**
 * 星星评价控件、RangSeekBar可拖动进度条
 */
public class OtherActivity extends SwipeBackActivity {
    private TextView tv_content, tvRightTxt;
    private Button btn, btn_switch, btn_call;
    private ImageView iv;
    private MaterialRatingBar rating_bar, material_rating_bar;
    private RangeSeekBar seekBar;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_other;
    }

    @Override
    public String getPagerTitle() {
        return "其他";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        findViews();
    }

    @Override
    protected void initData() {
        initAppDayNightMode();
        initRatingBar();
        initSeekBar();
        initNotice();
        initCall();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        boolean b = NotificationsUtils.isNotificationEnabled(getSelfActivity());
        iv.setBackgroundResource(b ? R.drawable.p_notice_on : R.drawable.p_notice_off);
    }

    /**
     * 功能1、APP主题切换
     */
    private void initAppDayNightMode() {
        final BaseApplication application = (BaseApplication) getApplication();
        btn_switch.setText(application.isDayMode() ? "切换夜间模式" : "切换日间模式");
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.switchDayNightMode();
                btn_switch.setText(application.isDayMode() ? "切换夜间模式" : "切换日间模式");
                recreate();
            }
        });
    }

    /**
     * 功能2、星星计分条
     */
    private void initRatingBar() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_content.setText("上面大星星可滑动评分：分数是：" + Float.toString(rating_bar.getRating())
                        + "\n小星星用于展示评分,分数是：" + Float.toString(material_rating_bar.getRating())
                );
            }
        });
    }

    /**
     * 功能3、拖拽选择金额范围
     */
    private void initSeekBar() {
        final DecimalFormat fnum = new DecimalFormat("0");
        final String unit = "万元";
        seekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                tvRightTxt.setVisibility(View.VISIBLE);
                if (rightValue == 50 && leftValue == 0) {
                    tvRightTxt.setText("0-不限");
                } else if (rightValue == 50) {
                    tvRightTxt.setText(fnum.format(leftValue) + unit + "以上");
                } else {
                    tvRightTxt.setText(fnum.format(leftValue) + "-" + fnum.format(rightValue) + unit);
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                LogUtil.i("", "", "onStopTrackingTouch");
            }
        });
        setPriceSeekBarValue(getSelfActivity(), seekBar, fnum, tvRightTxt, unit);
    }

    /**
     * 功能5、系统通知
     */
    private void initNotice() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsUtils.toNotificationSetting(getSelfActivity());
            }
        });
    }

    private PhoneCallUtil phoneCallUtil;

    /**
     * 功能6、拨打电话
     */
    private void initCall() {
        phoneCallUtil = new PhoneCallUtil(getSelfActivity(), true,true);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppUtils.callPhone(OtherActivity.this, "18551815425");
//               callPhone();
                phoneCallUtil.callPhone("18551815425");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        phoneCallUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //------------------------------各个功能分支------------------------------

    /**
     * 设置SeekBarPrice的当前值和参数
     *
     * @param activity
     * @param seekBar
     * @param fnum          0 整数
     * @param tvCustomPrice 显示当前价钱的TextView
     * @param unit          价钱的单位
     */
    public static void setPriceSeekBarValue(Activity activity, RangeSeekBar seekBar, DecimalFormat fnum, TextView tvCustomPrice, String unit) {
        CharSequence[] arry = new CharSequence[]{"0", "10", "20", "30", "40", "不限"};
        final float maxValue = 50;
        setRangeSeekBar(activity, seekBar, arry, 0, maxValue, "0", 1);
        //设置SeekBar的值
        float min = 0, max = maxValue;
        if (max == maxValue && min == 0) {
            tvCustomPrice.setText("0-不限");
        } else if (max == maxValue) {
            tvCustomPrice.setText(fnum.format(min) + unit + "以上");
        } else {
            tvCustomPrice.setText(fnum.format(min) + "-" + fnum.format(max) + unit);
        }
        seekBar.setValue(min, max);//设置当前显示---会触发监听setOnRangeChangedListener改变paramsBean的getT_priceList
    }

    /**
     * 设置通用的RangeSeekBar参数
     *
     * @param arry          刻度
     * @param min           刻度最小值，最左边
     * @param max           刻度最大值，最右边
     * @param formatPattern 刻度显示数值的类型 "0"设置为整数,"0.00"：保留两位小数
     * @param rangeInterval 设置一次滑动的间距值 eg：1
     */
    public static void setRangeSeekBar(Activity activity, RangeSeekBar seekBar, CharSequence[] arry, float min, float max, String formatPattern, float rangeInterval) {
        seekBar.setTickMarkMode(RangeSeekBar.TRICK_MARK_MODE_OTHER);//间距相等排列 TickMarkTextArray
        seekBar.setTickMarkTextColor(activity.getResources().getColor(R.color.font_8d));
        seekBar.setTickMarkTextMargin(ScreenUtil.dp2px(activity, 36));//刻度的文字间距
        seekBar.setTickMarkTextArray(arry);//设置刻度
        seekBar.setRange(min, max);//设置范围
        seekBar.setIndicatorTextDecimalFormat(formatPattern);//"0"设置为整数,"0.00"：保留两位小数
        seekBar.setRangeInterval(rangeInterval);//设置一次滑动的间距
        seekBar.setSeekBarMode(RangeSeekBar.SEEKBAR_MODE_RANGE);//设置双向滑动模式
        seekBar.setTickMarkTextSize(ScreenUtil.sp(activity, 12));//刻度的字体大小
        seekBar.setProgressColor(activity.getResources().getColor(R.color.blue));//进度条的颜色
        seekBar.setProgressDefaultColor(Color.parseColor("#DCDDDD"));//进度条默认颜色
        seekBar.setProgressHeight(ScreenUtil.dp2px(activity, 5));//进度条高度
        //左右滑动圈圈的设置
        seekBar.getLeftSeekBar().setIndicatorShowMode(SeekBar.INDICATOR_MODE_ALWAYS_HIDE);//一直隐藏当前滑动后圈圈上方显示的值
        seekBar.getLeftSeekBar().setThumbSize(ScreenUtil.dp2px(activity, 30));
        seekBar.getLeftSeekBar().setThumbDrawableId(R.drawable.thumb_inactivated);
        seekBar.getLeftSeekBar().setThumbInactivatedDrawableId(R.drawable.thumb_inactivated);
        //右
        seekBar.getRightSeekBar().setIndicatorShowMode(SeekBar.INDICATOR_MODE_ALWAYS_HIDE);
        seekBar.getRightSeekBar().setThumbSize(ScreenUtil.dp2px(activity, 30));
        seekBar.getRightSeekBar().setThumbDrawableId(R.drawable.thumb_inactivated);
        seekBar.getRightSeekBar().setThumbInactivatedDrawableId(R.drawable.thumb_inactivated);
    }

    //查找控件
    private void findViews() {
        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_call = (Button) findViewById(R.id.btn_call);
        iv = (ImageView) findViewById(R.id.iv);
        rating_bar = (MaterialRatingBar) findViewById(R.id.rating_bar);
        material_rating_bar = (MaterialRatingBar) findViewById(R.id.material_rating_bar);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn = (Button) findViewById(R.id.btn);
        tvRightTxt = (TextView) findViewById(R.id.tv_right_txt);
        seekBar = (RangeSeekBar) findViewById(R.id.rangeSeekBar);
    }


}
