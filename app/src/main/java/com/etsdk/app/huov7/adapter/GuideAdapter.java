package com.etsdk.app.huov7.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.MainActivity;

/**
 * Created by liu hong liang on 2017/1/16.
 * 引导页的adapter
 */

public class GuideAdapter extends PagerAdapter {
    private int[] imageIds=new int[]{R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};
    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        ImageView imageView=new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(imageIds[position]);
        if(position==2){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.start(v.getContext(),0);
                    ((Activity)container.getContext()).finish();
                }
            });
        }
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
