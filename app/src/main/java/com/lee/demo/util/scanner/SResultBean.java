package com.lee.demo.util.scanner;

/**
 * Created by chrislee on 21/1/17.
 */

public class SResultBean {

    public static class IdCardFrontBean {

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

    public static class IdCardBackBean {


        /**
         * organization : 签发机关
         * validPeriod : 20180101-20380101
         */

        private String organization;// 签发机关
        private String validPeriod;// 有效期限

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getValidPeriod() {
            return validPeriod;
        }

        public void setValidPeriod(String validPeriod) {
            this.validPeriod = validPeriod;
        }

        @Override
        public String toString() {
            return "IdCardBackBean{" +
                    "organization='" + organization + '\'' +
                    ", validPeriod='" + validPeriod + '\'' +
                    '}';
        }
    }

    public static class DrivingLicenseBean {


        /**
         * cardNumber : 43623446432
         * name : 张三
         * sex : 男
         * nationality : 中国
         * address : 地址
         * birth : 1999-01-01
         * firstIssue : 2018-01-01
         * _class : C1
         * validPeriod : 20180101-20240101
         */

        private String cardNumber;// 证号
        private String name;// 姓名
        private String sex;// 性别
        private String nationality;// 国籍
        private String address;// 地址
        private String birth;// 出生日期
        private String firstIssue;// 初次领证日期
        private String _class;// 准驾车型
        private String validPeriod;// 有效期限

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

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getFirstIssue() {
            return firstIssue;
        }

        public void setFirstIssue(String firstIssue) {
            this.firstIssue = firstIssue;
        }

        public String get_class() {
            return _class;
        }

        public void set_class(String _class) {
            this._class = _class;
        }

        public String getValidPeriod() {
            return validPeriod;
        }

        public void setValidPeriod(String validPeriod) {
            this.validPeriod = validPeriod;
        }

        @Override
        public String toString() {
            return "DrivingLicenseBean{" +
                    "cardNumber='" + cardNumber + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", nationality='" + nationality + '\'' +
                    ", address='" + address + '\'' +
                    ", birth='" + birth + '\'' +
                    ", firstIssue='" + firstIssue + '\'' +
                    ", _class='" + _class + '\'' +
                    ", validPeriod='" + validPeriod + '\'' +
                    '}';
        }
    }
}
