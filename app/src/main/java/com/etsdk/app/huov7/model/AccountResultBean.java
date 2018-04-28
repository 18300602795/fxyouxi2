package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/6/21.
 */

public class AccountResultBean  {
    private String username	;//STRING	玩家账号	test
    private String nickname	;//STRING	昵称	昵称长城
    private String email	;//STRING	玩家邮箱	111@qq.com
    private String mobile	;//STRING	玩家手机	13525555

    private List<LoginResultBean.UserName> userlist;//用户名列表

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public List<LoginResultBean.UserName> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<LoginResultBean.UserName> userlist) {
        this.userlist = userlist;
    }
}
