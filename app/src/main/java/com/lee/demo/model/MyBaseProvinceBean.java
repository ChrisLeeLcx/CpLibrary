package com.lee.demo.model;

import cn.lee.cplibrary.widget.picker.bean.BaseProvinceBean;

/**
 * Created by ChrisLee on 2019/8/13.
 */

public class MyBaseProvinceBean extends BaseProvinceBean {

    private String id;
    public MyBaseProvinceBean(String id ,String name) {
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
