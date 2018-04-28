package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TjTestNew {
    public static final int TYPE_TEST=1;
    public static final int TYPE_NEW=2;
    private int type=TYPE_TEST;
    private TestNewTabSelectListener listener;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TestNewTabSelectListener getListener() {
        return listener;
    }

    public void setListener(TestNewTabSelectListener listener) {
        this.listener = listener;
    }

    public TjTestNew(TestNewTabSelectListener listener) {
        this.listener = listener;
    }

    public static interface TestNewTabSelectListener{
        void onTestNewSelect(int curAdaptPosition,int type);
    }
}