package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.etsdk.app.huov7.model.GameClassifyListModelV1;
import com.etsdk.app.huov7.ui.ClassifyGameListActivity;
import com.game.sdk.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/2/8.
 */

public class SecondClassifyGvAdapterV1 extends RecyclerView.Adapter {
    private List<GameClassifyListModelV1.GameClassify> datas;
    private String firstClassifyName;
    private GameClassifyListModelV1.GameClassify superClassify;

    public SecondClassifyGvAdapterV1(GameClassifyListModelV1.GameClassify superClassify, List<GameClassifyListModelV1.GameClassify> datas, String firstClassifyName) {
        this.datas = datas;
        this.firstClassifyName = firstClassifyName;
        this.superClassify = superClassify;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_second_classify, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        if (datas.get(position) == null) {//额外补的
            viewHolder.tvSecondClassifyName.setText("");
            viewHolder.tvSecondClassifyName.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //把分类数据带过去
                    List tempList = new ArrayList();
//                    superClassify.setTypename("全部");
//                    tempList.add(superClassify);//加上父分类
                    for(GameClassifyListModelV1.GameClassify data: datas){
                        if(data != null){
                            tempList.add(data);
                        }
                    }
//                    ClassifyGameListActivity.start(v.getContext(), firstClassifyName, GsonUtil.getGson().toJson(tempList), position+1);
                    ClassifyGameListActivity.start(v.getContext(), firstClassifyName, GsonUtil.getGson().toJson(tempList), position);
                }
            });
            viewHolder.tvSecondClassifyName.setText(datas.get(position).getTypename());
        }
        //如果不是最后一行，需要显示下划线
        if((position/3+1)==datas.size()/3){
            viewHolder.vLine.setVisibility(View.GONE);
        }else{
            viewHolder.vLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        }
//        if (datas.size() % 2 != 0) {//需要补一个
//            datas.add(null);
//        }
        //需要补齐三的倍数
//        int i = datas.size() % 3;
//
//        if(i != 0){
//            i = 3 - i;
//            for (int j = 0; j < i; j++) {
//                datas.add(null);
//            }
//        }
        while (datas.size()<6||datas.size()%3!=0){
            datas.add(null);
        }
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_secondClassifyName)
        TextView tvSecondClassifyName;
        @BindView(R.id.v_line)
        View vLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
