package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ApplyGiftRequestBean;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.BindPhoneActivity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.dialog.DialogGiftUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.utils.BaseAppUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class GiftListItemViewProvider
        extends ItemViewProvider<GiftListItem, GiftListItemViewProvider.ViewHolder> {
    private MultiTypeAdapter multiTypeAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GiftListItemViewProvider(MultiTypeAdapter multiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_gift_list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GiftListItem giftListItem) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftDetailActivity.start(v.getContext(), giftListItem.getGiftid());
            }
        });
        holder.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(giftListItem.getGiftcode())) {
                    getUserInfoData(v.getContext(), giftListItem);
                } else {
                    BaseAppUtil.copyToSystem(v.getContext(), giftListItem.getGiftcode());
                    T.s(v.getContext(), "复制成功");
                }
            }
        });
        holder.tvGiftName.setText(giftListItem.getGiftname());
        holder.tvGiftContent.setText("礼包内容:" + giftListItem.getContent());
        int total = giftListItem.getTotal();
        int remain = giftListItem.getRemain();
        int progress = 100;
        if (total != 0) {
            progress = (int) (remain * 100. / total);
        }
        holder.pbGift.setProgress(progress);
        holder.tvProgress.setText(progress + "%");
        try {
            String formatDate = simpleDateFormat.format(new Date(Long.parseLong(giftListItem.getEnttime()) * 1000));
            holder.tvGiftEndTime.setText("过期时间:" + formatDate);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.tvGiftEndTime.setText("过期时间:");
        }
//        GlideDisplay.display(holder.ivGiftImage, giftListItem.getIcon(), R.mipmap.ic_launcher);
        Glide.with(holder.context).load(giftListItem.getIcon()).placeholder(R.mipmap.ic_launcher).into(holder.ivGiftImage);
        if (!TextUtils.isEmpty(giftListItem.getGiftcode())) {
            holder.tvApply.setText("复制");
        } else {
            holder.tvApply.setText("领取");
        }
        if (isViewTypeStart(giftListItem)) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    public void applyGift(final Context context, final GiftListItem giftData) {
        final ApplyGiftRequestBean applyGiftRequestBean = new ApplyGiftRequestBean();
        applyGiftRequestBean.setGiftid(giftData.getGiftid());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyGiftRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GiftListItem>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GiftListItem data) {
                if (data != null) {
                    T.s(context, "领取成功");
                    new DialogGiftUtil().showConvertDialog(context, data.getGiftcode(), data.getGameid(), null);
                    giftData.setGiftcode(data.getGiftcode());
                    multiTypeAdapter.notifyDataSetChanged();
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userGiftAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }



    private void getUserInfoData(final Context context, final GiftListItem giftData) {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    if (!StringUtils.isEmpty(data.getMobile())){
                        applyGift(context, giftData);
                    }else {
                        T.s(context, "请先去绑定手机");
                        BindPhoneActivity.start(context);
                    }
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gift_image)
        RoundedImageView ivGiftImage;
        @BindView(R.id.tv_gift_name)
        TextView tvGiftName;
        @BindView(R.id.pb_gift)
        ProgressBar pbGift;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_gift_content)
        TextView tvGiftContent;
        @BindView(R.id.tv_gift_endTime)
        TextView tvGiftEndTime;
        @BindView(R.id.tv_apply)
        TextView tvApply;
        @BindView(R.id.v_line)
        View vLine;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}