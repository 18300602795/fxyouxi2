package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;

import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/11.
 */

public class ShowVideoActivity extends ImmerseActivity{
    private JCVideoPlayerStandard jc_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        ButterKnife.bind(this);
        jc_video = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        jc_video.setUp("http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ShowVideoActivity.class);
        context.startActivity(starter);
    }

}
