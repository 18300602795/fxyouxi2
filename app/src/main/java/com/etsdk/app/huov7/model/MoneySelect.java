package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class MoneySelect {
    private String content;
    private int money;
    private boolean selected;

    public MoneySelect(String content, int money, boolean selected) {
        this.content = content;
        this.money = money;
        this.selected = selected;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}