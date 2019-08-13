package cn.lee.cplibrary.widget.picker.bean;

import java.util.List;

/**
 * 省 基类
 * Created by ChrisLee on 2019/8/13.
 */

public class BaseProvinceBean<E extends BaseCityBean> {
    private String name;//对话框上显示的文字
    private List<E> citys;//市

    public BaseProvinceBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<E> getCitys() {
        return citys;
    }

    public void setCitys(List<E> citys) {
        this.citys = citys;
    }
}
