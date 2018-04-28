package com.etsdk.app.huov7.model;

public class AddressInfo {
    private String id;//INT	区域ID
    private String name;//STRING	名称	区域名称
    private String pid;//INT	父区域ID	整数
    private int level;//	INT	地址级别	1，2,3,4
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}