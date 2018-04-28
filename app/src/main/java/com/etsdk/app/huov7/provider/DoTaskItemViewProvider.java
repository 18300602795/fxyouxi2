package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.DoTaskItem;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.BindPhoneActivity;
import com.etsdk.app.huov7.ui.RecommandTaskActivity;
import com.etsdk.app.huov7.ui.SelectGamePayActivity;
import com.etsdk.app.huov7.ui.SignInActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/2/8.
 */
public class DoTaskItemViewProvider
        extends ItemViewProvider<DoTaskItem, DoTaskItemViewProvider.ViewHolder> {

    private static Map<String,Intent> intentMap;
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_do_task_item, parent, false);
        initIntentMap(root.getContext());
        return new ViewHolder(root);
    }
    private void initIntentMap(Context context){
        if (intentMap == null) {
            intentMap=new HashMap<>();
            intentMap.put("sign", SignInActivity.getIntent(context));
            intentMap.put("uploadportrait", AccountManageActivity.getIntent(context));
            intentMap.put("tguser", RecommandTaskActivity.getIntent(context));
            intentMap.put("firstcharge", SelectGamePayActivity.getIntent(context));
            intentMap.put("charge", SelectGamePayActivity.getIntent(context));
            intentMap.put("bindmobile", BindPhoneActivity.getIntent(context));
        }
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final DoTaskItem doTaskItem) {
        holder.tvContent.setText(doTaskItem.getActname());
        holder.tvScore.setText("+"+doTaskItem.getIntegral());
        holder.tvRechargeHint.setVisibility(View.INVISIBLE);
        if("3".equals(doTaskItem.getTypeid())){//充值任务需要自己补文字
            holder.tvContent.setText("充值"+doTaskItem.getActname()+"元");
        }
        if("2".equals(doTaskItem.getFinishflag())){
            holder.tvTaskStatus.setText("已完成");
            int color = holder.itemView.getResources().getColor(R.color.text_gray);
            holder.tvContent.setTextColor(color);
            holder.tvScoreHint.setTextColor(color);
            holder.tvTaskStatus.setTextColor(color);
            holder.tvScore.setTextColor(color);
            holder.itemView.setClickable(false);
        }else{
            holder.tvTaskStatus.setText("去完成");
            int color = holder.itemView.getResources().getColor(R.color.text_black);
            holder.tvContent.setTextColor(color);
            holder.tvScoreHint.setTextColor(color);
            holder.tvTaskStatus.setTextColor(color);
            holder.tvScore.setTextColor(holder.itemView.getResources().getColor(R.color.text_red));
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = intentMap.get(doTaskItem.getActcode());
                    if(intent!=null){
                        v.getContext().startActivity(intent);
                    }
                }
            });
            if("3".equals(doTaskItem.getTypeid())){//充值任务
                try {
                    float money=Float.parseFloat(doTaskItem.getActname())-Float.parseFloat(doTaskItem.getMyMoney());
                    if(money<0) money=0;
                    holder.tvRechargeHint.setText("(还需充值"+ Math.ceil(money)+"元)");
                    holder.tvRechargeHint.setVisibility(View.VISIBLE);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_taskStatus)
        TextView tvTaskStatus;
        @BindView(R.id.tv_scoreHint)
        TextView tvScoreHint;
        @BindView(R.id.tv_recharge_hint)
        TextView tvRechargeHint;
        @BindView(R.id.tv_score)
        TextView tvScore;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}