package cn.lee.cplibrary.widget.picker.adapter;

import android.content.Context;

/**
 * 公历+农历日历Adapter
 * Created by ChrisLee on 2019/12/11.
 */

public class GLCNumericWheelAdapter extends NumericWheelAdapter {
    public GLCNumericWheelAdapter(Context context, int minValue, int maxValue) {
        super(context, minValue, maxValue);
    }
    public GLCNumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        super(context, minValue, maxValue, format);
    }
}