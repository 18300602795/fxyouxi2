package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/16.
 * 初始化接口
 */

public class StartupResultBean {
    private String ip;//	STRING	玩家公网IP
    private String user_token;//	STRING	用户更新的token 请求user_token不为空时返回
    private String agentgame;//	STRING	玩家游戏渠道编号
    private String timestamp;//	STRING	服务器时间戳
    private UpdateInfo up_info;//	JSON二维	更新信息JSON
    private HelpInfo help;//	JSON二维	客服信息

    private SplashInfo splash;//	JSON二维	闪屏图

    private String newmsg;//	INT	是否有新消息	2 有 1 没有 空 没

    public String getNewmsg() {
        return newmsg;
    }

    public void setNewmsg(String newmsg) {
        this.newmsg = newmsg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_token() {
        return user_token;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public UpdateInfo getUp_info() {
        return up_info;
    }

    public void setUp_info(UpdateInfo up_info) {
        this.up_info = up_info;
    }

    public HelpInfo getHelp() {
        return help;
    }

    public void setHelp(HelpInfo help) {
        this.help = help;
    }

    public SplashInfo getSplash() {
        return splash;
    }

    public void setSplash(SplashInfo splash) {
        this.splash = splash;
    }

    public static class UpdateInfo {
        private String up_status;	//INT	0=不更新，1=强更，2=选更
        private String url;	//STRING(URL)	up_status 为1或2时必填
        private String content;//	STRING	更新内容

        public String getUp_status() {
            return up_status;
        }

        public void setUp_status(String up_status) {
            this.up_status = up_status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
    public static class HelpInfo{
        //以下两个不需要
//        private String[] qq;//	STRING	客服qq
//        private String[] qqgroup;	//STRING	客服qq群号
        private String wx;//	STRING	客服微信
        private String tel;//	STRING	客服电话
        private String service_time;//	STRING	客服时间 09:00 - 17:00

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getServicetime() {
            return service_time;
        }

        public void setServicetime(String servicetime) {
            this.service_time = servicetime;
        }
    }
    public static class SplashInfo{
        private String img;//	STRING	闪屏图图片URL
        private String gameid;//	STRING	闪屏图跳转游戏ID
        private String url;//	STRING	闪屏图跳转地址，gameid为0或空时跳转

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getGameid() {
            return gameid;
        }

        public void setGameid(String gameid) {
            this.gameid = gameid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
