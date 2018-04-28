package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.Message;
import com.etsdk.app.huov7.model.MsgDetailRequestBean;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/16.
 */
public class MessageViewProvider
        extends ItemViewProvider<Message, MessageViewProvider.ViewHolder> {

    MessageActivity messageActivity;
    public MessageViewProvider(MessageActivity messageActivity) {
        this.messageActivity=messageActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_message_requste_bean, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Message message) {
        if("1".equals(message.getType())){//活动消息
            holder.tvMsgTitle.setText("活动消息");
            holder.ivMsgImg.setImageResource(R.mipmap.ic_msg_activity);
        }else if("2".equals(message.getType())){//系统消息
            holder.tvMsgTitle.setText("系统消息");
            holder.ivMsgImg.setImageResource(R.mipmap.ic_msg_xitong);
        }else if("3".equals(message.getType())){//卡券消息
            holder.tvMsgTitle.setText("卡券消息");
            holder.ivMsgImg.setImageResource(R.mipmap.ic_msg_car_coupon);
        }if("4".equals(message.getType())){//优惠活动
            holder.tvMsgTitle.setText("优惠活动");
            holder.ivMsgImg.setImageResource(R.mipmap.ic_msg_youhui_activity);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgDetailRequestBean msgDetailRequestBean = new MsgDetailRequestBean();
                msgDetailRequestBean.setMsgid(message.getMsgid());
                HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(msgDetailRequestBean));
                StringBuilder urlParams = httpParamsBuild.getHttpParams().getUrlParams();
                WebViewActivity.startPrivateUrl(v.getContext(), holder.tvMsgTitle.getText().toString(), AppApi.getUrl(AppApi.userMsgDetailApi), urlParams.toString());
                message.setReaded("2");
                messageActivity.getMultiTypeAdapter().notifyDataSetChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                messageActivity.hintDeleteMessage(message);
                return true;
            }
        });
        holder.tvMsgContent.setText(message.getTitle());
        if ("1".equals(message.getReaded())) {
            holder.ivRedPoint.setVisibility(View.VISIBLE);
        }else{
            holder.ivRedPoint.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_msg_img)
        ImageView ivMsgImg;
        @BindView(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @BindView(R.id.iv_redPoint)
        RoundedImageView ivRedPoint;
        @BindView(R.id.tv_msg_content)
        TextView tvMsgContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}