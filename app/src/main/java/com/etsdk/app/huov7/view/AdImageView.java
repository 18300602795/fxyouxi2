package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.liang530.utils.GlideDisplay;

/**
 * Created by liu hong liang on 2017/2/22.
 * 广告图view
 */

public class AdImageView extends ImageView {
    Context context;
    public AdImageView(Context context) {
        super(context);
        this.context = context;
    }

    public AdImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setAdImage(final AdImage adImage) {
//        GlideDisplay.display(this,adImage.getImage(), R.mipmap.gg);
        Glide.with(context).load(adImage.getImage()).placeholder(R.mipmap.gg).into(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(adImage.getType())){
                    WebViewActivity.start(v.getContext(),"",adImage.getUrl());
                }else if("2".equals(adImage.getType())){
                    GameDetailV2Activity.start(v.getContext(),adImage.getTarget()+"");
                }else if("3".equals(adImage.getType())){
                    GiftDetailActivity.start(v.getContext(),adImage.getTarget()+"");
                }else if("4".equals(adImage.getType())){
                    CouponDetailActivity.start(v.getContext(),adImage.getTarget()+"");
                }
            }
        });
    }
}
