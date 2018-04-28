package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class LoginResultBean {
    private String mem_id;	//STRING	用户在平台的用户ID
    private String cp_user_token;//	STRING	CP用user_token
    private String agentgame;//	STRING	渠道游戏编号
    private String user_token;//用户token
    private String is_reg;//是否是注册，第一次注册为1 登陆为0 is_reg 20170107添加
    private List<UserName> userlist;//用户名列表
    public String getUser_token() {
        return user_token;
    }

    public List<UserName> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<UserName> userlist) {
        this.userlist = userlist;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getAgentgame() {
        return agentgame;
    }

    public void setAgentgame(String agentgame) {
        this.agentgame = agentgame;
    }

    public String getCp_user_token() {
        return cp_user_token;
    }

    public void setCp_user_token(String cp_user_token) {
        this.cp_user_token = cp_user_token;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public static class UserName{
        private String username;//用户名
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public UserName(String username) {
            this.username = username;
        }

    }
}
