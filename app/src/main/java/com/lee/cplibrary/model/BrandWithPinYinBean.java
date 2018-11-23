package com.lee.cplibrary.model;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */


import java.util.List;

import cn.lee.cplibrary.widget.sidebar.BaseSideBarBean;

/**
 * 带有拼音的bean
 * @author: ChrisLee
 * @time: 2018/11/23
 */

public class BrandWithPinYinBean extends BaseSideBarBean {

    /**
     * 头部列表
     */
    private List<BrandBean> headList;
    /**
     * 品牌的Bean值
     */
    private  BrandBean bean;

    public List<BrandBean> getHeadList() {
        return headList;
    }

    public void setHeadList(List<BrandBean> headList) {
        this.headList = headList;
    }

    public BrandBean getBean() {
        return bean;
    }

    public void setBean(BrandBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "BrandWithPinYinBean{" +
                "headList=" + headList +
                ", name='" + name + '\'' +
                ", pys='" + pys + '\'' +
                ", bean=" + bean +
                ", type=" + type +
                '}';
    }

    public static class BrandBean {
        private String carName;
        private String iconUrl;
        private String id;

        public BrandBean(String id, String carName, String iconUrl) {
            this.id = id;
            this.carName = carName;
            this.iconUrl = iconUrl;
        }

        public String getId() {
            return id;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        @Override
        public String toString() {
            return "BrandBean{" +
                    "carName='" + carName + '\'' +
                    ", iconUrl='" + iconUrl + '\'' +
                    '}';
        }
    }

}