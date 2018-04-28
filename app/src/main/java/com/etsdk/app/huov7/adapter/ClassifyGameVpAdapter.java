package com.etsdk.app.huov7.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.etsdk.app.huov7.ui.fragment.GameListFragment;

import java.util.List;

/**
 * Created by liu hong liang on 2016/12/10.
 */

public class ClassifyGameVpAdapter extends FragmentPagerAdapter {

    List<GameClassifyListModel.GameClassify> gameClassifyList;
    public ClassifyGameVpAdapter(FragmentManager fm, List<GameClassifyListModel.GameClassify> gameClassifyList) {
        super(fm);
        this.gameClassifyList=gameClassifyList;
    }

    @Override
    public Fragment getItem(int position) {

        return GameListFragment.newInstance(false,false,0,0,0,0,0,0,gameClassifyList.get(position).getTypeid());
    }

    @Override
    public int getCount() {
        return gameClassifyList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return gameClassifyList.get(position).getTypename();
    }
}
