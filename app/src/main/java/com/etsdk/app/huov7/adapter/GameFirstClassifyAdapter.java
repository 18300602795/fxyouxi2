package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.etsdk.app.huov7.ui.ClassifyGameListActivity;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.game.sdk.util.GsonUtil;
import com.liang530.utils.BaseAppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/5.
 */

public class GameFirstClassifyAdapter extends RecyclerView.Adapter {
    public static final int TOP_BANNER = 0;
    public static final int COMM_ITEM = 1;

    private int topBannerSize = 1;
    private int commItemSize = 5;
    private Context context;
    private List<GameClassifyListModel.GameClassify> datas;
    private AdImage adImage;

    public GameFirstClassifyAdapter(List<GameClassifyListModel.GameClassify> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        context = parent.getContext();
        switch (viewType) {
            case TOP_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_first_classify_banner, parent, false);
                return new TopBannerViewHolder(view);
            case COMM_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_first_classify_item, parent, false);
                return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TopBannerViewHolder) {//广告
            TopBannerViewHolder topBannerViewHolder = (TopBannerViewHolder) holder;
//            GlideDisplay.display(topBannerViewHolder.ivImgae,adImage.getImage(),R.mipmap.gg);
            Glide.with(topBannerViewHolder.context).load(adImage.getImage()).placeholder(R.mipmap.gg).into(topBannerViewHolder.ivImgae);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(adImage.getType())) {
                        WebViewActivity.start(holder.itemView.getContext(), "", adImage.getUrl());
                    } else if ("2".equals(adImage.getType())) {
                        GameDetailV2Activity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    } else if ("3".equals(adImage.getType())) {
                        GiftDetailActivity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    } else if ("4".equals(adImage.getType())) {
                        CouponDetailActivity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    }
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            int index=position-topBannerSize;
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//            itemViewHolder.secondClassifyRecycler.setLayoutManager(new GridLayoutManager(context, 2));
//            itemViewHolder.secondClassifyRecycler.setNestedScrollingEnabled(false);
//            itemViewHolder.secondClassifyRecycler.addItemDecoration(new RecommandOptionRcyDivider(context, LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#e5e5e5")));
//            itemViewHolder.secondClassifyRecycler.addItemDecoration(new RecommandOptionRcyDivider(context, LinearLayoutManager.VERTICAL,2, Color.parseColor("#e5e5e5")));
            final GameClassifyListModel.GameClassify gameClassify = datas.get(index);
//            itemViewHolder.secondClassifyRecycler.setAdapter(new SecondClassifyAdapter(gameClassify.getSublist(), gameClassify.getTypename()));
            itemViewHolder.recySecondClassify.setLayoutManager(new GridLayoutManager(context,3));
            itemViewHolder.recySecondClassify.setNestedScrollingEnabled(false);
            itemViewHolder.recySecondClassify.setHasFixedSize(true);
            itemViewHolder.recySecondClassify.setAdapter(new SecondClassifyGvAdapter(gameClassify, gameClassify.getSublist(), gameClassify.getTypename()));
            itemViewHolder.tvClassifyName.setText(gameClassify.getTypename());
//            GlideDisplay.display(itemViewHolder.ivFirstClassifyImg, gameClassify.getIcon(),R.mipmap.ic_launcher);
            Glide.with(itemViewHolder.context).load(gameClassify.getIcon()).placeholder(R.mipmap.ic_launcher).into(itemViewHolder.ivFirstClassifyImg);
            ((ItemViewHolder) holder).ivFirstClassifyImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //把分类数据带过去
                    List tempList = new ArrayList();
                    GameClassifyListModel.GameClassify temp = new GameClassifyListModel.GameClassify();
                    temp.setTypeid(gameClassify.getTypeid());
                    temp.setTypename(gameClassify.getTypename());
                    tempList.add(temp);//加上父分类
                    ClassifyGameListActivity.start(v.getContext(), gameClassify.getTypename(), GsonUtil.getGson().toJson(tempList), 0);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < topBannerSize) {
            return TOP_BANNER;
        }
        return COMM_ITEM;
    }

    @Override
    public int getItemCount() {
        commItemSize = datas.size();
        topBannerSize = adImage != null ? 1 : 0;
        return topBannerSize + commItemSize;
    }

    static class TopBannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_imgae)
        ImageView ivImgae;
        Context context;
        TopBannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            setAdImageHeight(ivImgae);
            context = view.getContext();
        }
        private void setAdImageHeight(ImageView imageView){
            int height = (int) (BaseAppUtil.getDeviceWidth(ivImgae.getContext()) * AppApi.AD_IMAGE_HW_RATA);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            imageView.setLayoutParams(layoutParams);
        }
    }

    public List<GameClassifyListModel.GameClassify> getDatas() {
        return datas;
    }

    public void setDatas(List<GameClassifyListModel.GameClassify> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public AdImage getAdImage() {
        return adImage;
    }

    public void setAdImage(AdImage adImage) {
        this.adImage = adImage;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_first_classify_img)
        ImageView ivFirstClassifyImg;
        @BindView(R.id.tv_classifyName)
        TextView tvClassifyName;
//        @BindView(R.id.second_classify_recycler)
//        RecyclerView secondClassifyRecycler;
        @BindView(R.id.recy_second_classify)
        RecyclerView recySecondClassify;
        @BindView(R.id.v_bottomLine)
        View vBottomLine;
        Context context;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
