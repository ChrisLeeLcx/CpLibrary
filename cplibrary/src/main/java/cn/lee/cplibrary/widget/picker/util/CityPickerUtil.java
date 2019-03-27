package cn.lee.cplibrary.widget.picker.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.widget.picker.adapter.TextualWheelAdapter;
import cn.lee.cplibrary.widget.picker.bean.ProvinceBean;
import cn.lee.cplibrary.widget.picker.widget.OnWheelChangedListener;
import cn.lee.cplibrary.widget.picker.widget.WheelView;

/**
 * 使用方式：
 * 1、初始化对象：CityPickerUtil cityPickerUtil = new CityPickerUtil(activity);
 * 2、调用showProvince方法 cityPickerUtil.showProvince(new CityPickerUtil.CityPickerCallBack() {
 * public void sure(String province, String city, String district) {
 * toast(province+"----"+city+"---"+district);
 * }
 *
 * @Override public void cancel() {
 * }
 * });
 * 3、在Activity或者Fragment销毁的时候 调用cityPickerUtil.destroyHandler();释放内存
 * @author: ChrisLee
 * @time: 2018/11/3
 */

public class CityPickerUtil {
    private List<String> provincesAll = new ArrayList<>();//所有省份
    private String[] provinceShow; //需要显示的省份,若 provinceShow为null，或者size=0，provincesAll的内容就是所有省份,否则provincesAll内容为provinceShow
    private String defalutP = "北京市", defalutC = "北京市", defalutD = "东城区"; //默认显示的省市区
    private Map<String, List<String>> citiesAll = new HashMap<>();//所有市，key:省名字，value：市
    private Map<String, Map<String, List<String>>> districtsAll = new HashMap<>();//所有区：key1省；key2市：values2：区
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;//开始解析数据
    private static final int MSG_LOAD_SUCCESS = 0x0002;//解析数据成功
    private static final int MSG_LOAD_FAILED = 0x0003;//解析数据失败
    private boolean isLoaded = false;//数据是否加载完毕
    private Context context;
    private CityPickerCallBack callBack = null;
    private static int vsibleItemNum = 6;
    private static boolean isCyclic = false;
    //标题栏外观设置
    private int tBgColor = Color.parseColor("#1086D1");//时间选择框标题栏背景色
    private int tTxtColor = Color.parseColor("#FFFFFF");//标题栏：文字颜色（确定、取消按钮、标题）
    private String tTitle="选择地址";
    private int tTxtSize=14; //单位 sp ，


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA://开始解析数据
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initJsonData(context); // 子线程中解析省市区数据
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS://解析数据成功
                    showProvinceDialog(context, callBack);
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED://解析数据失败
                    LogUtil.i("", CityPickerUtil.this, "解析数据失败");
                    destroyHandler();
                    thread = null;
                    break;
            }
        }
    };

    public CityPickerUtil(Context context) {
        this.context = context;
    }

    /**
     * 显示省市区
     */
    public void showProvince(final CityPickerCallBack callBack) {
        if (isLoaded) {
            showProvinceDialog(context, callBack);
        } else {
            mHandler.sendEmptyMessage(MSG_LOAD_DATA);
            this.callBack = callBack;
        }
    }


//    int indexP = 0;
//    int indexC = 0;

    /**
     * 显示省市区
     */
    private void showProvinceDialog(final Context context, final CityPickerCallBack callBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.cp_date_time_picker_layout, null);
        final Dialog dialog = CpComDialog.getBottomDialog(context, true, view);
        view.findViewById(R.id.new_hour).setVisibility(View.GONE);
        view.findViewById(R.id.new_mins).setVisibility(View.GONE);
        final WheelView provinceView = view.findViewById(R.id.new_year);
        final WheelView cityView = view.findViewById(R.id.new_month);
        final WheelView districtView = view.findViewById(R.id.new_day);
        initArea(context, provinceView, provincesAll);

        initArea(context, cityView, citiesAll.get(provincesAll.get(0)));
        final Map<String, List<String>> stringListMap = districtsAll.get(provincesAll.get(0));
        initArea(context, districtView, stringListMap.get(citiesAll.get(provincesAll.get(0)).get(0)));

        try {// 设置 默认显示 省市区
            int indexOfP = provincesAll.indexOf(defalutP) < 0 ? 0 : provincesAll.indexOf(defalutP);
            provinceView.setCurrentItem(indexOfP);
            int poiC = citiesAll.get(defalutP).indexOf(defalutC);
            int indexOfC = poiC < 0 ? 0 : poiC;
            updateCity(context, cityView, citiesAll.get(defalutP), indexOfC);

            int poiD = districtsAll.get(defalutP).get(defalutC).indexOf(defalutD);
            int indexOfD = poiD < 0 ? 0 : poiD;
            updateCity(context, districtView, districtsAll.get(defalutP).get(defalutC), indexOfD);
        } catch (Exception e) {
            e.printStackTrace();
            String pp = provincesAll.get(provinceView.getCurrentItem());
            String cc = citiesAll.get(pp).get(cityView.getCurrentItem());
            updateCity(context, cityView, citiesAll.get(pp));
            updateCity(context, districtView, districtsAll.get(pp).get(cc));
        }
        setView(view);
        // 设置监听
        view.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pp = provincesAll.get(provinceView.getCurrentItem());
                String cc = citiesAll.get(pp).get(cityView.getCurrentItem());
                String dd = districtsAll.get(pp).get(cc).get(districtView.getCurrentItem());
                callBack.sure(pp, cc, dd);
                dialog.cancel();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel();
                dialog.cancel();
            }
        });

        provinceView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {//刷新市、区
                try {
                    String pp = provincesAll.get(provinceView.getCurrentItem());
                    String cc = citiesAll.get(pp).get(cityView.getCurrentItem());
//                indexP = newValue;
//                Map<String, List<String>> mapDD = districtsAll.get(provincesAll.get(indexP));
//                updateCity(context, cityView, citiesAll.get(provincesAll.get(indexP)));
//                updateCity(context, districtView, mapDD.get(citiesAll.get(provincesAll.get(indexP)).get(indexC)));
                    updateCity(context, cityView, citiesAll.get(pp));
                    updateCity(context, districtView, districtsAll.get(pp).get(cc));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }
        });
        cityView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {//刷新区
                try {
                    String pp = provincesAll.get(provinceView.getCurrentItem());
                    String cc = citiesAll.get(pp).get(cityView.getCurrentItem());
//                indexC = newValue;
//                Map<String, List<String>> mapDD = districtsAll.get(provincesAll.get(indexP));
//                updateCity(context, districtView, mapDD.get(citiesAll.get(provincesAll.get(indexP)).get(indexC)));
                    updateCity(context, districtView, districtsAll.get(pp).get(cc));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
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
    /**
     * 设置对话框外观
     */
    private void setView(View titleBar, TextView tvTitle, TextView tvLeftBtn, TextView tvRightBtn) {
        titleBar.setBackgroundColor(tBgColor);
        if (!ObjectUtils.isEmpty(tTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(tTitle);
            tvTitle.setTextColor(tTxtColor);
            tvTitle.setTextSize(tTxtSize);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvLeftBtn.setTextColor(tTxtColor);
        tvRightBtn.setTextColor(tTxtColor);
        tvLeftBtn.setTextSize(tTxtSize);
        tvRightBtn.setTextSize(tTxtSize);
    }
    /**
     * 初始化省、市、区
     */
    private void initArea(Context context, WheelView wheelView, List<String> list) {
        TextualWheelAdapter adapter = new TextualWheelAdapter(
                context, list);
        wheelView.setViewAdapter(adapter);
        wheelView.setCyclic(isCyclic);
        wheelView.setVisibleItems(vsibleItemNum);
    }

    private void updateCity(Context context,
                            WheelView cView, List<String> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        initArea(context, cView, list);
        cView.setCurrentItem(0);
    }

    private void updateCity(Context context,
                            WheelView cView, List<String> list, int poi) {
        if (list == null) {
            list = new ArrayList<>();
        }
        initArea(context, cView, list);
        cView.setCurrentItem(poi);
    }

    public interface CityPickerCallBack {
        public void sure(String province, String city, String district);

        public void cancel();
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

    //
//    private void initJsonData(Context context) {//解析数据
//        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据
//        ArrayList<ProvinceBean> beans = parseData(JsonData);//用Gson 转成实体
//        for (int i = 0; i < beans.size(); i++) {//遍历省份（第1级）
//            String pName = beans.get(i).getName();
//            List<ProvinceBean.CityBean> pCity = beans.get(i).getCity();
//            provincesAll.add(pName);//All省
//            List<String> listCitys = new ArrayList<>();//市
//            Map<String, List<String>> mapD = new HashMap<>();//市：区
//            for (int j = 0; j < pCity.size(); j++) {//遍历市级（第2级）
//                listCitys.add(pCity.get(j).getName());
//                List<String> area = pCity.get(j).getArea();
//                mapD.put(pCity.get(j).getName(), area);
//            }
//
//            citiesAll.put(pName, listCitys);//All市
//            districtsAll.put(pName, mapD);//All区
//        }
//        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
//
//    }
    private void initJsonData(Context context) {//解析数据
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据
        ArrayList<ProvinceBean> beans = parseData(JsonData);//用Gson 转成实体
        for (int i = 0; i < beans.size(); i++) {//遍历省份（第1级）
            String pName = beans.get(i).getName();
            if (!hasProvince(pName)) {
                continue;
            }
            List<ProvinceBean.CityBean> pCity = beans.get(i).getCity();
            provincesAll.add(pName);//All省
            List<String> listCitys = new ArrayList<>();//市
            Map<String, List<String>> mapD = new HashMap<>();//市：区
            for (int j = 0; j < pCity.size(); j++) {//遍历市级（第2级）
                listCitys.add(pCity.get(j).getName());
                List<String> area = pCity.get(j).getArea();
                mapD.put(pCity.get(j).getName(), area);
            }
            citiesAll.put(pName, listCitys);//All市
            districtsAll.put(pName, mapD);//All区
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    private boolean hasProvince(String pName) {
        if (provinceShow == null || Arrays.asList(provinceShow).size() == 0) { //默认显示所有
            return true;
        }
        if (Arrays.asList(provinceShow).contains(pName)) {
            return true;
        }
        return false;
    }

    /**
     * 请在Activity或者Fragment销毁的时候 调用此方法
     */
    public void destroyHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public String[] getProvinceShow() {
        return provinceShow;
    }

    public void setProvinceShow(String[] provinceShow) {
        this.provinceShow = provinceShow;
    }

    public void setDefaultArea(String defalutP, String defalutC, String defalutD) {
        this.defalutP = defalutP;
        this.defalutC = defalutC;
        this.defalutD = defalutD;
    }

    public void settBgColor(int tBgColor) {
        this.tBgColor = tBgColor;
    }

    public void settTxtColor(int tTxtColor) {
        this.tTxtColor = tTxtColor;
    }

    public void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    public void settTxtSize(int tTxtSize) {
        this.tTxtSize = tTxtSize;
    }
}

