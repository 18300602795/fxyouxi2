package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/6/22.
 */

public class PwdFindRequestBean extends BaseRequestBean {
    private String username	;//是	STRING	用户名
    private String email	;//否	STRING	密保邮箱 与mobile二选一 2017-06-22
    private String mobile	;//否	STRING	密保手机号 与email二选一 2017-06-22
    private String password	;//是	STRING	密码，登陆密码 可以使用smscode
    private String type	;//是	STRING	类型 此次发送邮箱类型 1 注册 2 登陆 3 修改密码 4 信息变更 5 找回密码
    private String code	;//是	STRING	校验码

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
