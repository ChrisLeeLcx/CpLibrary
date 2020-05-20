package com.lee.demo.util.gaode.loc;

/**
 * @author: ChrisLee
 * @time: 2018/12/17
 */

public class LocationBean {
    private double longitude; //经    度
    private double latitude; //纬    度
    private String country; //国    家
    private String province; //省
    private String city; //市
    private String cityCode; //城市编码
    private String district; //区
    private String address; //地    址
    private String street; //街道
    private String streetNum; //街道号牌
    private String name; //地    址
    private int errorCode = 0; //错误e码：为0 定位成功,-99 定位监听失败，其他的同AMapLocation的ErroCode
    private String errorInfo; //错误信息
    private String locationDetail ;//错误描述,详细描述
    public static  double defaultLongitude = 0;//118.89330891927084
    public static  double defaultLatitude = 0;//32.09670274522569
    //location=latitude=32.09670274522569#longitude=118.89330891927084 默认经纬度

    public double getLongitude() {
        return (isLocatedSuccess())? longitude:defaultLongitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return (isLocatedSuccess())? latitude:defaultLatitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return (isLocatedSuccess())? country:"中国";
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return (isLocatedSuccess())? province:"江苏省";
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return (isLocatedSuccess())? city:"南京市";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    private boolean isLocatedSuccess() {
        if (longitude > 0 || longitude > 0 || errorCode==0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", name='" + name + '\'' +
                ", errorCode=" + errorCode +
                ", errorInfo='" + errorInfo + '\'' +
                ", locationDetail='" + locationDetail + '\'' +
                '}';
    }
}
