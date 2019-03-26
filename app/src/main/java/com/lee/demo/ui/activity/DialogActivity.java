package com.lee.demo.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.util.PopupWindowUtil;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.bottom.CpBottomDialog;
import cn.lee.cplibrary.util.dialog.bottomround.CpBottomRoundDialog;
import cn.lee.cplibrary.widget.picker.util.CityPickerUtil;
import cn.lee.cplibrary.widget.picker.util.DatePickerUtils;
import cn.lee.cplibrary.widget.pwindow.CommonPopupWindow;

/**
 * 各种Dialog的Demo
 */
public class DialogActivity extends SwipeBackActivity implements View.OnClickListener {

    private TextView tvDate, tvAddr, tvTime;
    private CityPickerUtil cityPickerUtil;
    private TextView tvSort;
    private PopupWindowUtil popupWindowUtil;

    @Override
    protected SwipeBackActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_dialog;
    }

    @Override
    public String getPagerTitle() {
        return "对话框";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddr = (TextView) findViewById(R.id.tv_addr);
        tvSort = (TextView) findViewById(R.id.tv_sort);
        tvSort.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAddr.setOnClickListener(this);
        findViewById(R.id.btn_1btn).setOnClickListener(this);
        findViewById(R.id.btn_2btn).setOnClickListener(this);
        findViewById(R.id.btn_bottom).setOnClickListener(this);
        findViewById(R.id.btn_bottom_round).setOnClickListener(this);
        cityPickerUtil = new CityPickerUtil(getSelfActivity());
        popupWindowUtil = new PopupWindowUtil();
    }

    @Override
    protected void initData() {

    }



    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                DatePickerUtils.Builder.builder(getSelfActivity())
                        .settBgColor(getResources().getColor(R.color.colorAccent))
                        .settTitle("请选择时间")
                        .settTxtColor(getResources().getColor(R.color.font_14))
                        .settTxtSize(16)
                        .setShowLabel(true)
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
                CpComDialog.Builder.builder(getSelfActivity())
                        .setTitle("我是标题哦！").setContent("啦啦啦啦").build().show1BtnDialog(
                        new CpComDialog.Dialog1BtnCallBack() {
                            @Override
                            public void sure() {
                                ToastUtil.showToast(getSelfActivity(), "确定");
                            }
                        });
                break;
            case R.id.btn_2btn:
                CpComDialog.Builder.builder(getSelfActivity()).
                        setTitle("提示").setContent("清空历史记录吗?").setTxtCancel("忽略").setSure("更新")
                        .setTitleSize(20).setContentSize(16).setBtnSize(20)
                        .setTitleColor(getResources().getColor(R.color.colorAccent)).setContentColor(getResources().getColor(R.color.colorPrimary)).setBtnColor(getResources().getColor(R.color.colorAccent))
                        .setWidth(300).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        .setCancel(false)
                        .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
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
            case R.id.btn_bottom:
                final List<MyDialogBean> list = new ArrayList<>();
                list.add(new MyDialogBean("第1个", "1"));
                list.add(new MyDialogBean("第2个", "2"));
                CpBottomDialog.Builder.builder(getSelfActivity(), list)
                        .setBgColor(getResources().getColor(R.color.colorAccent))
                        .setCancelBgColor(getResources().getColor(R.color.colorPrimary))
                        .setTitleBgColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setChangeBg(false)//设置可以改变各自背景色（只有true时候 ，标题、取消按钮、item的设置的背景色才起作用）
                        .setItemHeight(30).setRvHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        .setTxtSize(10).setTxtColor(getResources().getColor(R.color.colorPrimary))
                        .setShowCancel(true)//设置cancel按钮
                        .setCancelTxtColor(getResources().getColor(R.color.colorAccent))
                        .setCancelSize(10)
                        .setCancelHeight(50)
                        .setShowTitle(true)//设置标题
                        .setTitle("我是普通的标题！")
                        .setTitleColor(getResources().getColor(R.color.colorAccent))
                        .setTitleHeight(50)
                        .setTitleSize(14)
                        .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {
                    @Override
                    public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
                        ToastUtil.showToast(getSelfActivity(), list.get(position).getName() + "-" + list.get(position).getId());

                    }

                    @Override
                    public void cancel() {
                        ToastUtil.showToast(getSelfActivity(), "取消");
                    }
                });
                break;
            case R.id.btn_bottom_round:
                final List<MyDialogBean> list1 = new ArrayList<>();
                list1.add(new MyDialogBean("西游记", "1"));
                list1.add(new MyDialogBean("红楼梦", "2"));
                list1.add(new MyDialogBean("三国演义", "3"));
                list1.add(new MyDialogBean("水浒传", "4"));
                CpBottomRoundDialog.Builder.builder(getSelfActivity(), list1)
                        .setItemHeight(30).setRvHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        .setTxtSize(5).setTxtColor(getResources().getColor(R.color.colorPrimary))
                        .setShowCancel(true)
                        .setCancelTxtColor(getResources().getColor(R.color.colorAccent))
                        .setCancelSize(8)
                        .setCancelHeight(50)
                        .setShowTitle(true).setTitle("我是好看的标题！")
                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
                        .setTitleHeight(30)
                        .setTitleSize(10)
                        .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {

                    @Override
                    public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
                        ToastUtil.showToast(getSelfActivity(), list1.get(position).getName() + "-" + list1.get(position).getId());
                    }

                    @Override
                    public void cancel() {
                        ToastUtil.showToast(getSelfActivity(), "取消");
                    }
                });
                break;
            case R.id.tv_sort:
                popupWindowUtil.showPWindow(getSelfActivity(), tvSort, tvSort, new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        if (layoutResId == R.layout.pwindow) {
                           LogUtil.i("","","我是布局activity_state_layout");
                        }
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

    public class MyDialogBean extends BaseDialogBean {
        private String id;

        public MyDialogBean(String name, String id) {
            super(name);
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
