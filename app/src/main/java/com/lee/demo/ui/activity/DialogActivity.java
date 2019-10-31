package com.lee.demo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.SwipeBackActivity;
import com.lee.demo.model.MyBaseCityBean;
import com.lee.demo.model.MyBaseDistrictBean;
import com.lee.demo.model.MyBaseProvinceBean;
import com.lee.demo.util.PopupWindowUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.bottom.CpBottomDialog;
import cn.lee.cplibrary.util.dialog.bottomround.CpBottomRoundDialog;
import cn.lee.cplibrary.widget.picker.bean.BaseCityBean;
import cn.lee.cplibrary.widget.picker.bean.BaseDistrictBean;
import cn.lee.cplibrary.widget.picker.bean.BaseProvinceBean;
import cn.lee.cplibrary.widget.picker.bean.ProvinceBean;
import cn.lee.cplibrary.widget.picker.util.CityPickerLoadDataUtil;
import cn.lee.cplibrary.widget.picker.util.CityPickerUtil;
import cn.lee.cplibrary.widget.picker.util.DatePickerUtils;
import cn.lee.cplibrary.widget.picker.util.GetJsonDataUtil;
import cn.lee.cplibrary.widget.pwindow.CommonPopupWindow;

/**
 * 各种Dialog的Demo
 */
public class DialogActivity extends SwipeBackActivity implements View.OnClickListener {

    private TextView tvDate, tvAddr, tvAddr2, tvTime;
    private CityPickerUtil cityPickerUtil;
    private TextView tvSort;
    private PopupWindowUtil popupWindowUtil;
    private CityPickerLoadDataUtil cityPickerLoadDataUtil;

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
        tvAddr2 = (TextView) findViewById(R.id.tv_addr2);
        tvSort = (TextView) findViewById(R.id.tv_sort);
        tvSort.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAddr.setOnClickListener(this);
        tvAddr2.setOnClickListener(this);
        findViewById(R.id.btn_1btn).setOnClickListener(this);
        findViewById(R.id.btn_2btn).setOnClickListener(this);
        findViewById(R.id.btn_top_round).setOnClickListener(this);
        findViewById(R.id.btn_bottom).setOnClickListener(this);
        findViewById(R.id.btn_bottom_round).setOnClickListener(this);
        popupWindowUtil = new PopupWindowUtil();
        cityPickerUtil = new CityPickerUtil(getSelfActivity());
        cityPickerUtil.setProvinceShow(new String[]{"安徽省", "江苏省"});//只显示这两个省，默认显示所有省份
//        cityPickerUtil.setDefaultArea("江苏省","镇江市","丹徒区");  //默认显示的省份
//        cityPickerUtil.setDefaultArea("江苏省","镇江市","");  //默认显示的省份
//        cityPickerUtil.setDefaultArea("江苏省","","");  //默认显示的省份
        cityPickerUtil.settTitle("选择区域");
        cityPickerUtil.settBgColor(getResources().getColor(R.color.colorPrimaryDark));
        cityPickerUtil.settTxtColor(getResources().getColor(R.color.colorAccent));
        cityPickerUtil.settTxtSize(14);

        //数据来源于外界
        cityPickerLoadDataUtil = new CityPickerLoadDataUtil(getSelfActivity());
        cityPickerLoadDataUtil.setProvinceShow(new String[]{"安徽省", "江苏省"});//只显示这两个省，默认显示所有省份
//        cityPickerLoadDataUtil.setDefaultArea("江苏省","镇江市","丹徒区");  //默认显示的省份
//        cityPickerLoadDataUtil.setDefaultArea("江苏省","镇江市","");  //默认显示的省份
//        cityPickerLoadDataUtil.setDefaultArea("江苏省","","");  //默认显示的省份
        cityPickerLoadDataUtil.settTitle("选择区域");
        cityPickerLoadDataUtil.settBgColor(getResources().getColor(R.color.colorPrimaryDark));
        cityPickerLoadDataUtil.settTxtColor(getResources().getColor(R.color.colorAccent));
        cityPickerLoadDataUtil.settTxtSize(14);

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
            case R.id.tv_addr2:
                if (beans.size() > 0) {
                    showCityPickDialog();
                } else {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }
                break;
            case R.id.btn_1btn:
                String title1 = "清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗";
//                String title1 = "我是标题哦！";
                CpComDialog.Builder.builder(getSelfActivity())
                        .setTitle("我是标题哦！").setContent(title1).build().show1BtnDialog(
                        new CpComDialog.Dialog1BtnCallBack() {
                            @Override
                            public void sure() {
                                ToastUtil.showToast(getSelfActivity(), "确定");
                            }
                        });
                break;
            case R.id.btn_2btn:
                String title = "清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗清空历史记录吗";
//                String title = "清空历史记录 ";
                CpComDialog.Builder.builder(getSelfActivity()).
                        setTitle("提示").setContent(title).setTxtCancel("忽略").setSure("更新")
                        .setTitleSize(20).setContentSize(16).setBtnSize(20)
                        .setTitleColor(getResources().getColor(R.color.colorAccent)).setContentColor(getResources().getColor(R.color.colorPrimary)).setBtnColor(getResources().getColor(R.color.colorAccent))
                        .setBtnCancelColor( getResources().getColor(R.color.font_8d))
                        .setWidth(300).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        .setPadding(24, 10, 24, 10)
                        .setCancel(false)
                        .setGravity(Gravity.RIGHT)
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
            case R.id.btn_top_round:
                final List<MyDialogBean> list1 = new ArrayList<>();
                list1.add(new MyDialogBean("第1个", "1"));
                list1.add(new MyDialogBean("第2个", "2"));
                list1.add(new MyDialogBean("第3个", "3"));
                list1.add(new MyDialogBean("第4个", "4"));
                list1.add(new MyDialogBean("第4个", "4"));
                list1.add(new MyDialogBean("第4个", "4"));
                list1.add(new MyDialogBean("第4个", "4"));
                list1.add(new MyDialogBean("第4个", "4"));
                list1.add(new MyDialogBean("第4个", "4"));
                CpBottomDialog.Builder.builder(getSelfActivity(), list1)
                        .setChangeBg(false)//设置可以改变各自背景色（只有true时候 ，标题、取消按钮、item的设置的背景色才起作用）
                        .setBgColor(getResources().getColor(R.color.colorAccent))
                        .setCancelBgColor(getResources().getColor(R.color.colorPrimary))
                        .setTitleBgColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setTopRound(true)
//                        .setItemHeight(30)
//                         .setRvHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
//                         .setRvHeight(100)
//                        .setTxtSize(10).setTxtColor(getResources().getColor(R.color.colorPrimary))
                        .setShowCancel(true)//设置cancel按钮
                        .setCancelTxtColor(getResources().getColor(R.color.colorAccent))
                        .setCancelSize(10)
                        .setCancelHeight(50)
                        .setShowTitle(true)//设置标题
                        .setTitle("我是普通的标题！")
                        .setCancelTxt("再看看")
                        .setTitleColor(getResources().getColor(R.color.colorAccent))
//                        .setTitleHeight(50)
//                        .setTitleSize(14)
                        .setLineMarginLR(0)
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
            case R.id.btn_bottom:
                final List<MyDialogBean> list = new ArrayList<>();
                list.add(new MyDialogBean("第1个", "1"));
                list.add(new MyDialogBean("第2个", "2"));
                list.add(new MyDialogBean("第2个", "2"));
                list.add(new MyDialogBean("第2个", "2"));
                list.add(new MyDialogBean("第2个", "2"));

                CpBottomDialog.Builder.builder(getSelfActivity(), list)
                        .setChangeBg(false)//设置可以改变各自背景色（只有true时候 ，标题、取消按钮、item的设置的背景色才起作用）
                        .setBgColor(getResources().getColor(R.color.colorAccent))
                        .setCancelBgColor(getResources().getColor(R.color.colorPrimary))
                        .setTitleBgColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setTopRound(false)
//                        .setItemHeight(30).setRvHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
//                        .setTxtSize(10).setTxtColor(getResources().getColor(R.color.colorPrimary))
                        .setShowCancel(true)//设置cancel按钮
                        .setCancelTxtColor(getResources().getColor(R.color.colorAccent))
                        .setCancelSize(10)
                        .setCancelHeight(50)
                        .setShowTitle(false)//设置标题
                        .setTitle("我是普通的标题！")
                        .setCancelTxt("再看看")
                        .setTitleColor(getResources().getColor(R.color.colorAccent))
//                        .setTitleHeight(50)
//                        .setTitleSize(14)
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
                final List<MyDialogBean> list2 = new ArrayList<>();
                list2.add(new MyDialogBean("西游记", "1"));
//                list2.add(new MyDialogBean("红楼梦", "2"));
//                list2.add(new MyDialogBean("三国演义", "3"));
//                list2.add(new MyDialogBean("水浒传", "4"));
//                list2.add(new MyDialogBean("水浒传", "4"));
//                list2.add(new MyDialogBean("水浒传", "4"));
//                list2.add(new MyDialogBean("水浒传", "4"));
//                list2.add(new MyDialogBean("水浒传", "4"));
                CpBottomRoundDialog.Builder.builder(getSelfActivity(), list2)
                        .setItemHeight(30)
                        .setRvHeight(30)
//                        .setRvHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
//                        .setTxtSize(5)
                        .setTxtColor(getResources().getColor(R.color.colorPrimary))
                        .setShowCancel(true)
                        .setCancelTxtColor(getResources().getColor(R.color.colorAccent))
//                        .setRvHeight(100)
//                        .setCancelSize(8)
//                        .setCancelHeight(50)
                        .setShowTitle(false).setTitle("我是好看的标题！")
                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
//                        .setTitleHeight(30)
//                        .setTitleSize(10)
                        .setCancelTxt("再看看")
                        .setLineMarginLR(0)
                        .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {

                    @Override
                    public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
                        ToastUtil.showToast(getSelfActivity(), list2.get(position).getName() + "-" + list2.get(position).getId());
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
                            LogUtil.i("", "", "我是布局activity_state_layout");
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

    //---------------------解析城市数据--------------------

    private static final int MSG_LOAD_DATA = 0x0001;//开始解析数据
    private static final int MSG_LOAD_SUCCESS = 0x0002;//解析数据成功
    private static final int MSG_LOAD_FAILED = 0x0003;//解析数据失败
    private Thread thread;
    private List<BaseProvinceBean> beans = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA://开始解析数据
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String JsonData = new GetJsonDataUtil().getJson(getSelfActivity(), "province.json");//获取assets目录下的json文件数据
                                ArrayList<ProvinceBean> list = parseData(JsonData);//用Gson 转成实体
                                for (int i = 0; i < list.size(); i++) {
                                    MyBaseProvinceBean pBean = new MyBaseProvinceBean(i+"",list.get(i).getName());
                                    List<ProvinceBean.CityBean> city = list.get(i).getCity();
                                    List<BaseCityBean> citys = new ArrayList();
                                    for (int i1 = 0; i1 < city.size(); i1++) {
                                        MyBaseCityBean cityBean = new MyBaseCityBean(i1+"",city.get(i1).getName());
                                        List<MyBaseDistrictBean> districts = new ArrayList<>();
                                        List<String> area = city.get(i1).getArea();
                                        for (int i2 = 0; i2 < area.size(); i2++) {
                                            districts.add(new MyBaseDistrictBean(i2 + "", area.get(i2)));
                                        }
                                        cityBean.setDistricts(districts);
                                        citys.add(cityBean);
                                    }
                                    pBean.setCitys(citys);
                                    beans.add(pBean);
                                }
                                mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS://解析数据成功
                    showCityPickDialog();
                    break;

                case MSG_LOAD_FAILED://解析数据失败
                    thread = null;
                    break;
            }
        }
    };

    private void showCityPickDialog() {
        cityPickerLoadDataUtil.showDialog(beans, new CityPickerLoadDataUtil.CityPickerCallBack() {

            @Override
            public void sure(String province, String city, String district, int pPosition, int cPosition, int dPosition) {
                MyBaseProvinceBean pBean = (MyBaseProvinceBean) beans.get(pPosition);
                MyBaseCityBean cBean = (MyBaseCityBean) pBean.getCitys().get(cPosition);
                List<MyBaseDistrictBean> districts = cBean.getDistricts();
                tvAddr2.setText(province + city + district + "---" + pBean.getId()+"-"+cBean.getId()+"-"+districts.get(dPosition).getId());
            }
            @Override
            public void cancel() {

            }
        });
    }


    /**
     * 解析省市区
     *
     * @param result
     * @return
     */
    public ArrayList<ProvinceBean> parseData(String result) {//Gson 解析

        ArrayList<ProvinceBean> detail = new ArrayList<>();
        try {
            JSONArray arrayP = new JSONArray(result);
            for (int i = 0; i < arrayP.length(); i++) {
                JSONObject objP = arrayP.getJSONObject(i);
                ProvinceBean provinceBean = new ProvinceBean();
                provinceBean.setName(objP.getString("name"));//province
                JSONArray arrayCity = objP.getJSONArray("city");//city
                List<ProvinceBean.CityBean> city = new ArrayList<>();
                for (int j = 0; j < arrayCity.length(); j++) {
                    JSONObject objC = arrayCity.getJSONObject(j);
                    ProvinceBean.CityBean cityBean = new ProvinceBean.CityBean();
                    cityBean.setName(objC.getString("name"));
                    JSONArray area = objC.getJSONArray("area");
                    List<String> areasD = new ArrayList<>();
                    for (int k = 0; k < area.length(); k++) {
                        areasD.add(area.getString(k));
                    }
                    cityBean.setArea(areasD);
                    city.add(cityBean);
                }
                provinceBean.setCity(city);
                detail.add(provinceBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
            LogUtil.i("", this, e.getMessage());

        }
        return detail;
    }
}
