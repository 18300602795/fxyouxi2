package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/12.
 * 用于eventbus 发送切换fragent通知
 */

public class SwitchFragmentEvent {
    public String activityClassName;
    public int[] positions;

    public SwitchFragmentEvent(String activityClassName, int ... positions) {
        this.activityClassName = activityClassName;
        this.positions = positions;
    }

}
