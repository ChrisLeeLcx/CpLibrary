package com.lee.demo.util.sidebar.model;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */


import com.chad.library.adapter.base.entity.SectionEntity;


/**
 * 带有拼音的bean
 * @author: ChrisLee
 * @time: 2018/11/23
 */

public class GirdSideBarWithPinYinBean extends SectionEntity<GirdSideBarWithPinYinBean.BrandBean> {
    public String pys;//name的拼音首字母的大写----必须加上作为分组的依据
    public GirdSideBarWithPinYinBean(boolean isHeader, String header) {//header 拼音索引
        super(isHeader, header);
    }
    public GirdSideBarWithPinYinBean(GirdSideBarWithPinYinBean.BrandBean t) {
        super(t);
    }

    public void setPys(String pys) {
        this.pys = pys;
    }

    public String getPys() {
        return pys;
    }

    public static class BrandBean {
        private String carName;
        private String iconUrl;
        private String id;

        public BrandBean( ) {
        }
        public BrandBean(String id, String carName, String iconUrl) {
            this.id = id;
            this.carName = carName;
            this.iconUrl = iconUrl;
        }

        public void setId(String id) {
            this.id = id;
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