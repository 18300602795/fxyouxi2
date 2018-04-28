package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/3/1.
 * 服务的qqbean
 */

public class ServiceQq {
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
