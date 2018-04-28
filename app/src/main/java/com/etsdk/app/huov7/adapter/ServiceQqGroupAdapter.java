package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.game.sdk.log.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/3/1.
 */

public class ServiceQqGroupAdapter extends BaseAdapter {
    private List<String> serviceQqGroupList = new ArrayList<>();
    private String[] serviceQqGroupKey;


    public List<String> getServiceQqGroupList() {
        return serviceQqGroupList;
    }

    public void setServiceQqGroupList(String[] serviceQqGroupList, String[] qqgroupkey) {
        for(String qqGroup: serviceQqGroupList){
            this.serviceQqGroupList.add(qqGroup);
        }
        this.serviceQqGroupKey = qqgroupkey;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return serviceQqGroupList==null?0:serviceQqGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceQqGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.syz_item_qq_group, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String serviceQq = serviceQqGroupList.get(position);
        viewHolder.qqGroupIndex.setText((serviceQqGroupList.size()-position)+"");
        viewHolder.tvQqGroupHint.setText("QQ群("+(position+1)+")");
        viewHolder.qqGroupTV.setText(serviceQq);
        L.i("333", "QQ群：" + serviceQqGroupKey[position]);
        viewHolder.qqGroupStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((position+1)<=serviceQqGroupKey.length){
                    joinQQGroup(v.getContext(),serviceQqGroupKey[position]);
                }
            }
        });
//        if("1".equals(serviceQq.getStatus())){
//            viewHolder.qqGroupStatus.setText("已满");
//            viewHolder.qqGroupStatus.setEnabled(false);
//            viewHolder.qqGroupStatus.setClickable(false);
//        }else{
            viewHolder.qqGroupStatus.setText("申请加入");
            viewHolder.qqGroupStatus.setEnabled(true);
            viewHolder.qqGroupStatus.setClickable(true);
//        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.qqGroupIndex)
        TextView qqGroupIndex;
        @BindView(R.id.linearLayout1)
        LinearLayout linearLayout1;
        @BindView(R.id.tv_qq_group_hint)
        TextView tvQqGroupHint;
        @BindView(R.id.qqGroupTV)
        TextView qqGroupTV;
        @BindView(R.id.qqGroupStatus)
        Button qqGroupStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    /****************
     *
     * 发起添加群流程。群号：测试群(594245585) 的 key 为： n62NA_2zzhPfmNicq-sZLioBGiN2v7Oq
     * 调用 joinQQGroup(n62NA_2zzhPfmNicq-sZLioBGiN2v7Oq) 即可发起手Q客户端申请加群 测试群(594245585)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context,String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(context,"未安装手Q或安装的版本不支持",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
