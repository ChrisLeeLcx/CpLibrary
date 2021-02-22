package com.lee.demo.util.scanner;

/**
 * Created by chrislee on 21/1/17.
 */

public   class IdCardFrontBean {

    /**
     * cardNumber : 21412412421
     * name : 张三
     * sex : 男
     * nation : 汉
     * birth : 1999-01-01
     * address : 地址
     */

    private String cardNumber;// 身份证号
    private String name;// 姓名
    private String sex;// 性别
    private String nation;// 民族
    private String birth;// 出生
    private String address;// 地址

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "IdCardFrontBean{" +
                "cardNumber='" + cardNumber + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}