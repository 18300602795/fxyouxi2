package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.etsdk.app.huov7.R;

/**
 * Created by liu hong liang on 2016/12/24.
 * 布局状态view，避免在用户看到数据加载过程
 */

public class LoadStatusView extends FrameLayout implements View.OnClickListener{
    public static final int LOADING=0;
    public static final int LOAD_FAIL=-1;
    public static final int LOAD_SUCCESS=1;
    private OnLoadRefreshListener onLoadRefreshListener;
    private View loadingView;
    private View failView;
    private View contentView;
    private  int currentStatus=0;
    private ViewGroup.LayoutParams layoutParams;
    public LoadStatusView(Context context) {
        super(context);
        initUI();
    }
    public LoadStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }
    public LoadStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }
    private void initUI(){
        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.include_load_loading, this, false);
        failView = LayoutInflater.from(getContext()).inflate(R.layout.include_load_fail, this, false);
        View refreshView = failView.findViewById(R.id.refresh);
        refreshView.setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count=getChildCount();
        for(int i=0;i<count;i++){
            if(getChildAt(i)!=loadingView&&getChildAt(i)!=failView){
                contentView=getChildAt(i);
                this.layoutParams=contentView.getLayoutParams();
            }
        }
        if(layoutParams==null){
            layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        showSuccess();
    }

    public void setOnLoadRefreshListener(OnLoadRefreshListener onLoadRefreshListener) {
        this.onLoadRefreshListener = onLoadRefreshListener;
    }

    public void showSuccess(){
        if(currentStatus!=LOAD_SUCCESS){//当前不是成功页面才重新加载
            currentStatus=LOAD_SUCCESS;
            removeAllViews();
            if(contentView!=null){
                addView(contentView,layoutParams);
            }
        }
    }
    public void showLoading(){
        currentStatus=LOADING;
        removeAllViews();
        addView(loadingView,layoutParams);
    }

    public void showFail(){
        currentStatus= LOAD_FAIL;
        removeAllViews();
        addView(failView,layoutParams);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                if(onLoadRefreshListener!=null){
                    onLoadRefreshListener.onLoadRefresh();
                    showLoading();
                }
                break;
        }
    }

    public int getCurrentStatus() {
        return currentStatus;
    }
    /**
     * 加载失败时的刷新回调
     */
    public interface OnLoadRefreshListener{
        void onLoadRefresh();
    }
}
