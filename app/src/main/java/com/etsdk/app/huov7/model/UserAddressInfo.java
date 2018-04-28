package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/7.
 * 地址信息
 */

public class UserAddressInfo {
   private String consignee;//	STRING	收货人	张三
   private String mobile;//	STRING	联系电话	15507501312
   private String country;//	INT	国家ID	0
   private String province;//	INT	省ID	19
   private String city	;//INT	城市ID	234
   private String district;//	INT	地区ID	2314
   private String town	;//INT	乡镇ID	0
   private String address;//	STRING	详细地址	喜洋居
   private String zipcode;//	STRING	邮编	518001
   private String topaddress;//	STRING		由country，province，city，district，town组成的地址

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTopaddress() {
        return topaddress;
    }

    public void setTopaddress(String topaddress) {
        this.topaddress = topaddress;
    }
}
