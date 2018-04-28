package com.etsdk.app.huov7.util;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2017/1/5.
 * 首页推荐的recyclerview的滑动监听，用于滑动时处理标题栏的变化
 */

public class MainSearchRcyScrollListener extends RecyclerView.OnScrollListener{
    private static final String TAG = MainSearchRcyScrollListener.class.getSimpleName();
    RelativeLayout rlMsg;
    LinearLayout llSearch;
    private Float startScrollY;
    private float minHeight ;
    private Float followScrollY;
    public MainSearchRcyScrollListener(RelativeLayout rlMsg, LinearLayout llSearch) {
        this.rlMsg = rlMsg;
        this.llSearch = llSearch;
        minHeight=BaseAppUtil.dip2px(rlMsg.getContext(),50);//50dp标题栏高度
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) llSearch.getLayoutParams();
//        startScrollY=followScrollY= Float.valueOf(BaseAppUtil.dip2px(llSearch.getContext(), 130f));
        startScrollY=followScrollY=layoutParams.topMargin*1f;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dy==0) return;//dy=0界面还没显示，测量的高度等还获取不到
        int rlMsgWidth = rlMsg.getWidth();
        int rlMsgHeight = rlMsg.getHeight();
//        L.e(TAG,"消息view w="+rlMsgWidth);
//        L.e(TAG,"滑动距离dy="+dy);
        followScrollY -= dy;//即将滑动到的y坐标
//        L.e(TAG,"followScrollY="+followScrollY);
//        L.e(TAG,"滑动前y="+llSearch.getY());
//        L.e(TAG,"剩余距离="+ startScrollY +"  "+followScrollY);
        View parent = (View) rlMsg.getParent();
        if(followScrollY<0){//滑到顶了
            llSearch.setY(0);
            parent.setBackgroundColor(Color.argb(255,30,188,179));
            //如果不设置，快速滑动时recyclerview的过渡滑动导致，滑动距离过头，需要纠正
            llSearch.setPadding(llSearch.getPaddingLeft(),llSearch.getPaddingTop(),rlMsgWidth,llSearch.getPaddingBottom());
        }else{
            llSearch.setY(followScrollY);
            float paddingRight=(startScrollY-followScrollY)*(rlMsgWidth*1.0f/startScrollY);
//            L.e(TAG,"paddingRight="+paddingRight);
            if(startScrollY-followScrollY>0){//还在滑动范围中
                llSearch.setPadding(llSearch.getPaddingLeft(),llSearch.getPaddingTop(),(int)paddingRight,llSearch.getPaddingBottom());
            }else{//防止过渡滑动
                llSearch.setPadding(llSearch.getPaddingLeft(),llSearch.getPaddingTop(),0,llSearch.getPaddingBottom());
            }
            if(rlMsgHeight-followScrollY>0){
                float transpartColor=(rlMsgHeight-followScrollY)*(255*1.0f/rlMsgHeight);
                parent.setBackgroundColor(Color.argb((int)transpartColor,30,188,179));
            }else{
                parent.setBackgroundColor(Color.argb(0,30,188,179));
            }
        }
//        llSearch.setY(followScrollY);
//        L.e(TAG,"滑动后y="+llSearch.getY());
    }
}
