package com.etsdk.app.huov7.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.ui.dialog.SelectAccountLoginDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2017/6/20.
 */

public class SelectAccountView extends FrameLayout {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.lv_account_list)
    ListView lvAccountList;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ll_loginView)
    LinearLayout llLoginView;
    private Activity mActivity;
    private List<LoginResultBean.UserName> userNameList;
    private SelectAccountLoginDialog.SelectAccountListener selectAccountListener;
    private SelectAccountAdapter selectAccountAdapter;
    private Dialog dialog;

    public SelectAccountView(@NonNull Context context) {
        super(context);
        initUI();
    }

    public SelectAccountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SelectAccountView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        mActivity = (Activity) getContext();
        LayoutInflater.from(getContext()).inflate(R.layout.huo_sdk_view_select_account, this);
        ButterKnife.bind(this);
    }

    public void setUserNameList(Dialog dialog, List<LoginResultBean.UserName> userNameList, SelectAccountLoginDialog.SelectAccountListener selectAccountListener) {
        this.dialog=dialog;
        this.userNameList = userNameList;
        this.selectAccountListener = selectAccountListener;
        selectAccountAdapter = new SelectAccountAdapter();
        lvAccountList.setAdapter(selectAccountAdapter);
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if(selectAccountAdapter==null){
            ((Activity) getContext()).finish();
            return;
        }else{
            int selectPosition = selectAccountAdapter.getSelectPosition();
            LoginResultBean.UserName userName = userNameList.get(selectPosition);
            if(selectAccountListener!=null){
                selectAccountListener.onSelectAccount(userName.getUsername());
                dialog.dismiss();
            }
        }
    }
//    private void sumitLogin(final String account, final String password) {
//        if (TextUtils.isEmpty(account)) {
//            T.s(mActivity, "用户名不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(password)) {
//            T.s(mActivity, "密码不能为空");
//            return;
//        }
//        final LoginRequestBean loginRequestBean = new LoginRequestBean();
//        loginRequestBean.setUsername(account);
//        loginRequestBean.setPassword(password);
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(loginRequestBean));
//        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
//            @Override
//            public void onDataSuccess(LoginResultBean data) {
//                if (data != null) {
//                    LoginControl.saveToken(data.getUser_token());
//                    T.s(mActivity, "登陆成功");
//                    //接口回调通知
//                    //保存账号到数据库
//                    if (!UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
//                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
//                    } else {
//                        UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
//                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
//                    }
//                    dialog.dismiss();
//                    ((Activity) getContext()).finish();
//                    MainActivity.start(mActivity, 3);
//                }
//            }
//        };
//        httpCallbackDecode.setShowTs(true);
//        httpCallbackDecode.setLoadingCancel(false);
//        httpCallbackDecode.setShowLoading(true);
//        httpCallbackDecode.setLoadMsg("正在登录...");
//        RxVolley.post(AppApi.getUrl(AppApi.loginApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
//    }


    public class SelectAccountAdapter extends BaseAdapter {
        private int selectPosition=0;

        public int getSelectPosition() {
            return selectPosition;
        }

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
        }

        @Override
        public int getCount() {
            return userNameList.size();
        }

        @Override
        public Object getItem(int position) {
            return userNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.huosdk_item_select_account, parent, false);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.tvAccountNumHint.setText("账号"+(position+1)+":");
            viewHolder.tvLoginAccount.setText(userNameList.get(position).getUsername());
            viewHolder.cbSelectAccount.setChecked(position==selectPosition);
            viewHolder.cbSelectAccount.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition=position;
                    notifyDataSetChanged();
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition=position;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public  class ViewHolder {
            @BindView(R.id.tv_account_num_hint)
            TextView tvAccountNumHint;
            @BindView(R.id.tv_login_account)
            TextView tvLoginAccount;
            @BindView(R.id.cb_select_account)
            CheckBox cbSelectAccount;
            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
