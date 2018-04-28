package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/3/2.
 * 关于我们信息
 */

public class AboutUsInfo extends BaseModel {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

   public static class DataBean{
       private String wburl	;//URL 微博链接地址
       private String website;//	URL	官网地址
       private List<QqInfo> qq;//	二维数组	客服
       private List<QqInfo> qqgroup;//	二维数组	客服qq群
       private List<TelInfo> tel;//	二维数组	电话
       private List<ServiceTime> servicetime;//	STRING	服务时间

        public String getWburl() {
            return wburl;
        }

        public void setWburl(String wburl) {
            this.wburl = wburl;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public List<QqInfo> getQq() {
            return qq;
        }

        public void setQq(List<QqInfo> qq) {
            this.qq = qq;
        }

        public List<QqInfo> getQqgroup() {
            return qqgroup;
        }

        public void setQqgroup(List<QqInfo> qqgroup) {
            this.qqgroup = qqgroup;
        }

        public List<TelInfo> getTel() {
            return tel;
        }

        public void setTel(List<TelInfo> tel) {
            this.tel = tel;
        }

        public List<ServiceTime> getServicetime() {
            return servicetime;
        }

        public void setServicetime(List<ServiceTime> servicetime) {
            this.servicetime = servicetime;
        }
    }
    public static class ServiceTime{
        private String weekday;//	STRING	工作日客服时间
        private String holiday;//	STRING	节假日客服时间

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getHoliday() {
            return holiday;
        }

        public void setHoliday(String holiday) {
            this.holiday = holiday;
        }
    }
    public static class TelInfo{
        private String number;//	STRING	客服电话

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
    public static class QqInfo{
        private String name	;//STRING	客服qq群名
        private String number;//	STRING	客服qq群号
        private String key;//	STRING	客服qq群KEY
        private String status;//	INT	是否满员	1 满员 2 不满元

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
