package com.lee.cplibrary.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.SwipeBackActivity;

import cn.lee.cplibrary.util.CpDialogUtil;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.widget.picker.util.CityPickerUtil;
import cn.lee.cplibrary.widget.picker.util.DatePickerUtils;

/**
 * 各种Dialog的Demo
 */
public class DialogActivity extends SwipeBackActivity implements View.OnClickListener {

    private TextView tvDate, tvAddr, tvTime;
    private CityPickerUtil cityPickerUtil;

    @Override
    protected SwipeBackActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddr = (TextView) findViewById(R.id.tv_addr);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAddr.setOnClickListener(this);
        findViewById(R.id.btn_1btn).setOnClickListener(this);
        findViewById(R.id.btn_2btn).setOnClickListener(this);
        cityPickerUtil = new CityPickerUtil(getSelfActivity());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                DatePickerUtils.Builder.builder(getSelfActivity())
                        .settBgColor(getResources().getColor(R.color.colorAccent))
                        .settTitle("请选择时间")
                        .settTxtColor(getResources().getColor(R.color.font_14))
                        .settTxtSize(8)
                        .setShowLabel(false)
                        .build().
                        showDate(new DatePickerUtils.DateCallBack() {
                            @Override
                            public void sure(int year, int month, int day) {
                                tvDate.setText(DatePickerUtils.format(year, month, day));
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                break;
            case R.id.tv_time:
                DatePickerUtils.Builder.builder(getSelfActivity()).build()
                        .showDateAndTime(new DatePickerUtils.DateAndTimeCallBack() {
                            @Override
                            public void sure(int year, int month, int day, int hour, int min) {
                                tvTime.setText(DatePickerUtils.format(year, month, day, hour, min));
                            }

                            @Override
                            public void cancel() {

                            }
                        });

                break;
            case R.id.tv_addr:
                cityPickerUtil.showProvince(new CityPickerUtil.CityPickerCallBack() {
                    @Override
                    public void sure(String province, String city, String district) {
                        tvAddr.setText(province + city + district);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.btn_1btn:
                CpDialogUtil.Builder.builder(getSelfActivity())
                        .setTitle("我是标题哦！").build().show1BtnDialog(
                        new CpDialogUtil.Dialog1BtnCallBack() {
                            @Override
                            public void sure() {
                                ToastUtil.showToast(getSelfActivity(), "确定");
                            }
                        });
                break;
            case R.id.btn_2btn:
                CpDialogUtil.Builder.builder(getSelfActivity()).
                        setTitle("提示").setContent("清空历史记录吗?").setTxtCancel("忽略").setSure("更新")
                        .setTitleSize(10).setContentSize(8).setBtnSize(10)
                        .setTitleColor(getResources().getColor(R.color.colorAccent)).setContentColor(getResources().getColor(R.color.colorPrimary)).setBtnColor(getResources().getColor(R.color.colorAccent))
                        .setCancel(false)
                        .build().show2BtnDialog(new CpDialogUtil.Dialog2BtnCallBack() {
                    @Override
                    public void sure() {
                        ToastUtil.showToast(getSelfActivity(), "更新");
                    }

                    @Override
                    public void cancel() {
                        ToastUtil.showToast(getSelfActivity(), "忽略");
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cityPickerUtil.destroyHandler();
    }
}
