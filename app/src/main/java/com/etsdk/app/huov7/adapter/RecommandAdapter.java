package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.FuliGiftActivity;
import com.etsdk.app.huov7.ui.GameDetailActivity;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.util.RecommandAdapterHelp;
import com.etsdk.app.huov7.util.RecommandOptionRcyDivider;
import com.liang530.views.convenientbanner.ConvenientBanner;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/5.
 */

public class RecommandAdapter extends RecyclerView.Adapter implements IDataAdapter {
    public static final int MODULE_TOP = 0;
    public static final int OPTION_COLUMN = 1;
    public static final int NEWGAME_SF_LIST = 2;
    public static final int TEST_NEW_GAME = 3;
    public static final int SHOUYOUFENG = 4;
    public static final int XIN_YOU_TJ = 5;
    public static final int LIKE_GAME_HEAD = 6;
    public static final int LIKE_GAME = 7;



    private int moduleTopSize = 1;
    private int optionColumnSize = 1;
    private int testNewGameSize = 1;

    private int newGameSFSize = 1;
    private int shouyoufengSize = 1;
    private int xinYouTjSize = 1;
    private int likeGameHeadSize = 1;
    private int likeGameSize = 4;

    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        switch (viewType) {
            case MODULE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tj_module_top, parent, false);
                return new ModuleTopViewHolder(view);
            case OPTION_COLUMN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tj_option_column, parent, false);
                return new OptionColumnViewHolder(view);
            case TEST_NEW_GAME://测试表，开服表
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tj_test_new, parent, false);
                return new TestNewGameViewHolder(view);
            case LIKE_GAME_HEAD://猜你喜欢头部
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_like_game_head, parent, false);
                return new LikeGameHeadViewHolder(view);
            case LIKE_GAME://猜你喜欢Item
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_list_item, parent, false);
                return new LikeGameViewHolder(view);
            //下面三个是同一类型
            case NEWGAME_SF_LIST://新游首发
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tj_common_game, parent, false);
                return new RecommandAdapterHelp.NewGameSFViewHolder(view);
            case SHOUYOUFENG://手游风向标
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tj_common_game, parent, false);
                return new RecommandAdapterHelp.ShouYouFengViewHolder(view);
            case XIN_YOU_TJ://新游推荐
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tj_common_game, parent, false);
                return new RecommandAdapterHelp.XinYouTJViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("hongliang", "hold=" + holder);
        if (holder instanceof ModuleTopViewHolder) {
            ModuleTopViewHolder moduleTopViewHolder = (ModuleTopViewHolder) holder;
            moduleTopViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameDetailActivity.start(v.getContext());
                }
            });
            moduleTopViewHolder.mainTjSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.start(v.getContext());
                }
            });
            moduleTopViewHolder.ivTjDownManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManagerActivity.start(context);
                }
            });
            if (context instanceof MainActivity) {
                moduleTopViewHolder.llHotGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) context).switchGameFragment(1);
                    }
                });
                moduleTopViewHolder.llNewGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) context).switchGameFragment(2);
                    }
                });
                moduleTopViewHolder.llStartGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) context).switchGameTestFragment();
                    }
                });
                moduleTopViewHolder.llGift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FuliGiftActivity.start(v.getContext());
                    }
                });
            }
            moduleTopViewHolder.rlGotoMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageActivity.start(v.getContext());
                }
            });

        } else if (holder instanceof OptionColumnViewHolder) {
            OptionColumnViewHolder optionColumnViewHolder = (OptionColumnViewHolder) holder;
            optionColumnViewHolder.tjRcyOption.setLayoutManager(new GridLayoutManager(context, 2));
            optionColumnViewHolder.tjRcyOption.setNestedScrollingEnabled(false);
            optionColumnViewHolder.tjRcyOption.addItemDecoration(new RecommandOptionRcyDivider(context, LinearLayoutManager.HORIZONTAL));
            optionColumnViewHolder.tjRcyOption.addItemDecoration(new RecommandOptionRcyDivider(context, LinearLayoutManager.VERTICAL));
            optionColumnViewHolder.tjRcyOption.setAdapter(new OptionColumnAdapter(context));
            optionColumnViewHolder.tvBroadcast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.start(v.getContext(),"宣传喇叭","http://1tsdk.com");
                }
            });
        } else if (holder instanceof RecommandAdapterHelp.RecommandBaseViewHolder) {
            RecommandAdapterHelp.onBindViewHolder(holder, position);
        } else if (holder instanceof TestNewGameViewHolder) {
            final TestNewGameViewHolder testNewGameViewHolder = (TestNewGameViewHolder) holder;
            testNewGameViewHolder.tjRcyTestNew.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            testNewGameViewHolder.tjRcyTestNew.setNestedScrollingEnabled(false);
            testNewGameViewHolder.tjRcyTestNew.setAdapter(new GameGiftListAdapter());
            testNewGameViewHolder.llTestGameTab.setSelected(true);
            testNewGameViewHolder.llTestGameTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        return;
                    }
                    testNewGameViewHolder.llTestGameTab.setSelected(true);
                    testNewGameViewHolder.llNewGameTab.setSelected(false);
                }
            });
            testNewGameViewHolder.llNewGameTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        return;
                    }
                    testNewGameViewHolder.llTestGameTab.setSelected(false);
                    testNewGameViewHolder.llNewGameTab.setSelected(true);
                }
            });

        } else if (holder instanceof LikeGameHeadViewHolder) {

        } else if (holder instanceof LikeGameViewHolder) {

        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position < moduleTopSize) {
            return MODULE_TOP;
        }
        if (position < moduleTopSize + optionColumnSize) {
            return OPTION_COLUMN;
        }
        if (position < moduleTopSize + optionColumnSize + newGameSFSize) {
            return NEWGAME_SF_LIST;
        }
        if (position < moduleTopSize + optionColumnSize + newGameSFSize + testNewGameSize) {
            return TEST_NEW_GAME;
        }
        if (position < moduleTopSize + optionColumnSize + newGameSFSize + testNewGameSize + shouyoufengSize) {
            return SHOUYOUFENG;
        }
        if (position < moduleTopSize + optionColumnSize + newGameSFSize + testNewGameSize + shouyoufengSize + xinYouTjSize) {
            return XIN_YOU_TJ;
        }
        if (position < moduleTopSize + optionColumnSize + newGameSFSize
                + testNewGameSize + shouyoufengSize + xinYouTjSize + likeGameHeadSize) {
            return LIKE_GAME_HEAD;
        }
        return LIKE_GAME;
    }

    @Override
    public int getItemCount() {
        int size = moduleTopSize + optionColumnSize + newGameSFSize
                + testNewGameSize + shouyoufengSize + xinYouTjSize + likeGameHeadSize + likeGameSize;
        return size;
    }

    @Override
    public void notifyDataChanged(Object o, boolean b) {

    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class ModuleTopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_goto_msg)
        RelativeLayout rlGotoMsg;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;
        @BindView(R.id.iv_tj_downManager)
        ImageView ivTjDownManager;
        @BindView(R.id.main_tjSearch)
        TextView mainTjSearch;
        @BindView(R.id.ll_hotGame)
        LinearLayout llHotGame;
        @BindView(R.id.ll_newGame)
        LinearLayout llNewGame;
        @BindView(R.id.ll_startGame)
        LinearLayout llStartGame;
        @BindView(R.id.ll_gift)
        LinearLayout llGift;

        ModuleTopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class OptionColumnViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tj_rcy_option)
        RecyclerView tjRcyOption;
        @BindView(R.id.tv_broadcast)
        TextView tvBroadcast;

        OptionColumnViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class TestNewGameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_test_game_tab)
        LinearLayout llTestGameTab;
        @BindView(R.id.ll_new_game_tab)
        LinearLayout llNewGameTab;
        @BindView(R.id.tj_rcy_test_new)
        RecyclerView tjRcyTestNew;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;

        TestNewGameViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class LikeGameHeadViewHolder extends RecyclerView.ViewHolder {
        LikeGameHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class LikeGameViewHolder extends RecyclerView.ViewHolder {
        LikeGameViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
