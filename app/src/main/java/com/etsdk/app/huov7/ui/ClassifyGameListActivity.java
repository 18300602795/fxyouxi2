package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ClassifyGameListTabAdapter;
import com.etsdk.app.huov7.adapter.ClassifyGameVpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.flyco.tablayout.SlidingTabLayout;
import com.game.sdk.util.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.liang530.views.viewpager.SViewPager;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassifyGameListActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tab_main_game)
    SlidingTabLayout tabMainGame;
    @BindView(R.id.vp_game)
    SViewPager vpGame;
    @BindView(R.id.activity_classify_game_list)
    LinearLayout activityClassifyGameList;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.tab_classify_recy)
    RecyclerView tabClassifyRecy;
    private ClassifyGameVpAdapter classifyGameVpAdapter;
    private LayoutInflater mInflater;
    private List<GameClassifyListModel.GameClassify> gameClassifyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_game_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tabClassifyRecy.setVisibility(BuildConfig.projectCode==137?View.GONE:View.VISIBLE);
        mInflater = LayoutInflater.from(mContext);
        Intent intent = getIntent();
        final String firstClassName = intent.getStringExtra("firstClassName");
        String gameClassifyListJson = intent.getStringExtra("gameClassifyListJson");
        int position = intent.getIntExtra("position", 0);
        gameClassifyList = GsonUtil.getGson().fromJson(gameClassifyListJson, new TypeToken<List<GameClassifyListModel.GameClassify>>() {
        }.getType());

        if (TextUtils.isEmpty(firstClassName)) {
            tvTitleName.setText("游戏分类");
        } else {
            tvTitleName.setText(firstClassName);
        }
        classifyGameVpAdapter = new ClassifyGameVpAdapter(getSupportFragmentManager(), gameClassifyList);
        vpGame.setAdapter(classifyGameVpAdapter);
//        vpGame.setCurrentItem(position, false);
        vpGame.setOffscreenPageLimit(3);
        vpGame.setCanScroll(false);
        //设置分类tab
        tabClassifyRecy.setLayoutManager(new GridLayoutManager(this,5));
//        tabClassifyRecy.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        ClassifyGameListTabAdapter tabAdapter = new ClassifyGameListTabAdapter(gameClassifyList, new ClassifyGameListTabAdapter.OnItemSelectedListener() {
            @Override
            public void selected(int position) {
                vpGame.setCurrentItem(position, false);
            }
        });
        tabAdapter.setSelectPosition(position);
        tabClassifyRecy.setAdapter(tabAdapter);
        tabMainGame.setViewPager(vpGame);
        tabMainGame.setCurrentTab(position);
//        flowlayout.setAdapter(new TagAdapter<GameClassifyListModel.GameClassify>(gameClassifyList) {
//            @Override
//            public View getView(FlowLayout parent, int position, GameClassifyListModel.GameClassify gameClassify) {
//                View view = mInflater.inflate(R.layout.item_game_classify_tag,
//                        flowlayout, false);
//                TextView textView = (TextView) view.findViewById(R.id.tv_tag_name);
//                textView.setText(gameClassify.getTypename());
//                return view;
//            }
//        });
//        flowlayout.getAdapter().setSelectedList(position);
//        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                vpGame.setCurrentItem(position,false);
//                return false;
//            }
//        });
//        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                vpGame.setCurrentItem(position,false);
//                Set<Integer> selectedList = flowlayout.getSelectedList();
//                L.e(TAG,"selectTab="+selectedList.toString());
//                return false;
//            }
//        });
//        flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet) {
//
//            }
//        });
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

    public static void start(Context context, String firstClassName, String gameClassifyListJson, int position) {
        Intent starter = new Intent(context, ClassifyGameListActivity.class);
        starter.putExtra("firstClassName", firstClassName);
        starter.putExtra("position", position);
        starter.putExtra("gameClassifyListJson", gameClassifyListJson);
        context.startActivity(starter);
    }
}
