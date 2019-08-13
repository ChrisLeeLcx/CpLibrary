package cn.lee.cplibrary.widget.picker.bean;

import java.util.List;

/**
 * 市 基类
 * Created by ChrisLee on 2019/8/13.
 */

public class BaseCityBean<K extends BaseDistrictBean> {
    private String name;
    private List<K> districts;//区

    public BaseCityBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<K> getDistricts() {
        return districts;
    }

    public void setDistricts(List<K> districts) {
        this.districts = districts;
    }
}
