package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/14.
 */
public class StartServerInfo {
    private String serid;//INT	开服ID	12
    private String icon	;//图片链接	http://huosdk.com/icon.png
    private String sername;//STRING	服务器名称	71服
    private String serdesc;//STRING	服务器描述	服务器别称
    private String status;//INT	开服状态	1 预告 2 已开服
    private String starttime;//	date	开服时间	2016-08-16 09:00:00

    public StartServerInfo(String sername, String starttime,String status) {
        this.sername = sername;
        this.starttime = starttime;
        this.status=status;
    }

    public StartServerInfo() {
    }

    public String getSerid() {
        return serid;
    }

    public void setSerid(String serid) {
        this.serid = serid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSername() {
        return sername;
    }

    public void setSername(String sername) {
        this.sername = sername;
    }

    public String getSerdesc() {
        return serdesc;
    }

    public void setSerdesc(String serdesc) {
        this.serdesc = serdesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}