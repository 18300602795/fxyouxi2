package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.NotProguard;

/**
 * Created by Administrator on 2017/4/21 0021.
 */
@NotProguard
public class ResultBean {

    /**
     * code : 200
     * msg : 下单成功
     * data : {}
     */

    private int code;
    private String msg;
    private DataBean data;

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
