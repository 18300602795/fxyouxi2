package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/16.
 */

public class GameDownResult extends BaseRequestBean {
    private int downcnt;//	INT	下载数量	更新此游戏下载数量
    private int count;//	INT	下载地址数量	表示下载地址总个数
    private List<GameDown> list;

    public int getDowncnt() {
        return downcnt;
    }

    public void setDowncnt(int downcnt) {
        this.downcnt = downcnt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GameDown> getList() {
        return list;
    }

    public void setList(List<GameDown> list) {
        this.list = list;
    }

    public static class GameDown{
        private String type	;//INT	下载地址类型	1 直接app下载 2跳转webview下载
        private String name	;//STRING	下载地址名称	例如： local 360下载
        private String url	;//URL 下载地址	每个来源对应的下载地址

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
