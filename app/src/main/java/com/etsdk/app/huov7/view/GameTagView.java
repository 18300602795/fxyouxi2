package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/1/15.
 */

public class GameTagView extends FrameLayout {
    private int[] colors = new int[]{R.color.class_color1, R.color.class_color2, R.color.class_color3, R.color.class_color4, R.color.class_color5};
    private TagFlowLayout tagFlowLayout;
    private LayoutInflater mInflater;

    public GameTagView(Context context) {
        super(context);
        initUI();
    }

    public GameTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public GameTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        mInflater = LayoutInflater.from(getContext());
        tagFlowLayout = (TagFlowLayout) mInflater.inflate(R.layout.item_game_tag_view, this, false);
        View childTag = mInflater.inflate(R.layout.item_game_tag_child, tagFlowLayout, false);
        childTag.measure(0,0);
        int measuredHeight = childTag.getMeasuredHeight();
        tagFlowLayout.getLayoutParams().height=measuredHeight;
        Log.e("hongliang","measuredHeight="+measuredHeight);
        this.addView(tagFlowLayout);
        ButterKnife.bind(this);
    }

    /**
     * 设置游戏type tag
     * @param gameType
     */
//    public void setGameType(String gameType){
//        if(TextUtils.isEmpty(gameType)){
//            tagFlowLayout.removeAllViews();
//            return;
//        }else{
//            String[] splitGameType = gameType.split(",");
//            tagFlowLayout.removeAllViews();
//            if(splitGameType==null||splitGameType.length==0){
//                return;
//            }
//            for(int i=0;i<splitGameType.length&&i<5;i++){
//                TextView tvGameType = (TextView) inflater.inflate(R.layout.item_game_tag_child, tagFlowLayout,false);
//                tagFlowLayout.addView(tvGameType);
//                tvGameType.setText(splitGameType[i]);
//                tvGameType.setBackgroundColor(getResources().getColor(colors[i]));
//            }
//        }
//    }

    /**
     * 设置游戏type tag
     *
     * @param gameType
     */
    public void setGameType(String gameType) {
        String[] splitGameType;
        if (TextUtils.isEmpty(gameType)) {
            tagFlowLayout.removeAllViews();
            splitGameType = new String[]{};
        } else {
            splitGameType = gameType.split(",");
            if (splitGameType == null) {
                splitGameType = new String[]{};
            }
        }
        List<String> maxTagList=new ArrayList();
        for (int i=0; i<splitGameType.length;i++) {
            if(i<5){
                maxTagList.add(splitGameType[i]);
            }
        }
        tagFlowLayout.setAdapter(new TagAdapter<String>(maxTagList) {
            @Override
            public View getView(FlowLayout parent, int position, String content) {
                TextView textView = (TextView) mInflater.inflate(R.layout.item_game_tag_child,
                        tagFlowLayout, false);
                textView.setText(content);
                textView.setBackgroundColor(getResources().getColor(colors[position]));
                return textView;
            }
        });
    }

    /**
     * 本层view不处理点击事件，交由父view处理
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
