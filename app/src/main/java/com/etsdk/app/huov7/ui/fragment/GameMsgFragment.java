package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class GameMsgFragment extends AutoLazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_game_msg);
    }
}
