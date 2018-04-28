package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.liang530.utils.BaseAppUtil;

import java.util.List;

/**
 * Created by liu hong liang on 2016/10/7.
 */

public class DownAddressSelectDialogUtil {
    public static void showAddressSelectDialog(final Context activity, final List<GameDownResult.GameDown> downAddressList, final SelectAddressListener selectAddressListener){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_down_address, null);
        final Dialog dialog=new Dialog(activity,R.style.dialog_bg_style);
        ListView lvSelectAddress = (ListView) view.findViewById(R.id.lv_selectAddress);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        lvSelectAddress.setAdapter(new SelectAddressAdapter(downAddressList));
        lvSelectAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameDownResult.GameDown downAddress = downAddressList.get(position);
                if("1".equals(downAddress.getType())){
                    selectAddressListener.downAddress(downAddress.getUrl());
                    dialog.dismiss();
                }else{
                    WebViewActivity.start(activity,"游戏下载",downAddress.getUrl());
                }
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width= BaseAppUtil.getDeviceWidth(activity)-300;
        dialog.show();
    }
    static class SelectAddressAdapter extends BaseAdapter{
        List<GameDownResult.GameDown> downAddressList;

        public SelectAddressAdapter(List<GameDownResult.GameDown> downAddressList) {
            this.downAddressList = downAddressList;
        }

        @Override
        public int getCount() {
            return downAddressList==null?0:downAddressList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_select_address,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.btn_down_name=(TextView)convertView.findViewById(R.id.btn_down_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.btn_down_name.setText(downAddressList.get(position).getName());
            return convertView;
        }
    }
    public interface SelectAddressListener{
        void downAddress(String url);
    }
    public static class ViewHolder{
        TextView btn_down_name;
    }
}
