package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/23.
 * 验证手机验证码、邮箱
 */

public class AuthSmsCodeResultBean {
    private String status;//	INT	2 绑定成功 1 绑定失败

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
