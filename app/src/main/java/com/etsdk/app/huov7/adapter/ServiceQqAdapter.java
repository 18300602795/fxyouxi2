package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.game.sdk.log.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/3/1.
 */

public class ServiceQqAdapter extends BaseAdapter {
    private List<String> qqList = new ArrayList<>();


    public List<String> getQqList() {
        return qqList;
    }

    public void setQqList(String[] qqList) {
        //奇数补成偶数
        for(String qq: qqList){
            this.qqList.add(qq);
        }
        if(qqList !=null&& qqList.length%2==1){
            this.qqList.add(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //1行两个  除以2
        return qqList ==null?0: qqList.size()/2;
    }

    @Override
    public Object getItem(int position) {
        return qqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item_qq, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.csQqName1.setMaxTextSize(Float.valueOf(BaseAppUtil.sp2px(parent.getContext(),14)));
//        viewHolder.csQqName2.setMaxTextSize(Float.valueOf(BaseAppUtil.sp2px(parent.getContext(),14)));
        final String serviceQqL = qqList.get(position*2);
        final String serviceQqR = qqList.get(position*2+1);

        if(serviceQqL!=null){//设置第一个
            viewHolder.csQqName1.setText(new StringBuffer("客服").append(((position+1)*2-1)).append("号"));
        }
        if(serviceQqR!=null){
//            viewHolder.csQqName2.setText(new StringBuffer("客服").append(((position+1)*2)).append(":").append(serviceQqR.getName()));
            viewHolder.csQqName2.setText(new StringBuffer("客服").append(((position+1)*2)).append("号"));
            viewHolder.llServiceqqRight.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llServiceqqRight.setVisibility(View.INVISIBLE);
        }
        viewHolder.btnContactMe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQq(v.getContext(),serviceQqL);
            }
        });
        viewHolder.btnContactMe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQq(v.getContext(),serviceQqR);
            }
        });
        return convertView;
    }
    public static void openQq(Context context,String qq){
        try {
            String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            T.s(context,"请先安装最新版qq");
        }
    }
    static class ViewHolder {
        @BindView(R.id.cs_qq_name1)
        TextView csQqName1;
        @BindView(R.id.btn_contact_me1)
        ImageView btnContactMe1;
        @BindView(R.id.ll_serviceqqLeft)
        LinearLayout llServiceqqLeft;
        @BindView(R.id.cs_qq_name2)
        TextView csQqName2;
        @BindView(R.id.btn_contact_me2)
        ImageView btnContactMe2;
        @BindView(R.id.ll_serviceqqRight)
        LinearLayout llServiceqqRight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
