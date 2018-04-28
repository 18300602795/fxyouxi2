package com.etsdk.app.huov7.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2017/6/22.
 */

public class SelectAccountLoginPop {
    private static final String TAG = SelectAccountLoginPop.class.getSimpleName();
    private PopupWindow pw_select_user;
    private List<UserInfo> userInfoList=new ArrayList<>();
    private RecordUserAdapter recordUserAdapter;
    private Context mContext;
    private ListView lvSelectAccount;
    private View anchor;
    private SelectAccountListener selectAccountListener;
    public SelectAccountLoginPop(View anchor, final SelectAccountListener selectAccountListener) {
        mContext=anchor.getContext();
        this.anchor=anchor;
        this.selectAccountListener=selectAccountListener;
    }

    /**
     * 用户下拉选择
     */
    public void show() {
        if(pw_select_user==null){
            View view = LayoutInflater.from(mContext).inflate(R.layout.pop_select_account_login, null);
            pw_select_user=new PopupWindow(view, anchor.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
            pw_select_user.setBackgroundDrawable(new ColorDrawable(0x00000000));
            pw_select_user.setContentView(view);
            lvSelectAccount = (ListView) view.findViewById(R.id.huo_sdk_lv_select_account);
            lvSelectAccount.setCacheColorHint(0x00000000);
            recordUserAdapter = new RecordUserAdapter();
            lvSelectAccount.setAdapter(recordUserAdapter);
            pw_select_user.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if(selectAccountListener!=null){
                        selectAccountListener.dismissPop();
                    }
                }
            });
            lvSelectAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(selectAccountListener!=null){
                        selectAccountListener.selectedAccount(userInfoList.get(position));
                        pw_select_user.dismiss();
                    }
                }
            });
        }
        if(isShowing()){
            pw_select_user.dismiss();
        }
        userInfoList= UserLoginInfodao.getInstance(mContext).getUserLoginInfo();
        pw_select_user.showAsDropDown(anchor, 0,0);
        recordUserAdapter.notifyDataSetChanged();
    }
    /**
     * popupwindow显示已经登录用户的设配器
     *
     * @author Administrator
     *
     */
    private class RecordUserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return userInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (null == convertView) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.huo_sdk_item_select_acount_lv, parent,false);
                convertView = view;
            }
            TextView tv_username = (TextView) convertView.findViewById(R.id.huo_sdk_tv_account);
            ImageView iv_delete = (ImageView) convertView.findViewById(R.id.huo_sdk_iv_delectAccount);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectAccountListener!=null){
                        selectAccountListener.deletedAccount(userInfoList.get(position));
                    }
                    UserLoginInfodao.getInstance(v.getContext()).deleteUserLoginByName(userInfoList.get(position).username);
                    userInfoList.remove(position);
                    notifyDataSetChanged();
                }
            });
            tv_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectAccountListener!=null){
                        selectAccountListener.selectedAccount(userInfoList.get(position));
                        pw_select_user.dismiss();
                    }
                }
            });
            tv_username.setText(userInfoList.get(position).username);
            return convertView;
        }
    }
    public interface SelectAccountListener{
        void selectedAccount(UserInfo userInfo);
        void deletedAccount(UserInfo userInfo);
        void dismissPop();
    }
    public boolean isShowing(){
        if(pw_select_user==null) return false;
        return pw_select_user.isShowing();
    }
}
