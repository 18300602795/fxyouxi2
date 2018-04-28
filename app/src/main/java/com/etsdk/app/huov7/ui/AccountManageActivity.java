package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AddressList;
import com.etsdk.app.huov7.model.UpdateNickNameRequest;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.HintDialogUtil;
import com.etsdk.app.huov7.ui.dialog.UpdateTextDialogUtil;
import com.game.sdk.HuosdkManager;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.control.LoginControl;
import com.liang530.log.T;
import com.liang530.photopicker.beans.SelectPhotoEvent;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountManageActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_right_arrow)
    TextView tvRightArrow;
    @BindView(R.id.activity_account_manage)
    LinearLayout activityAccountManage;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_bindMobile)
    TextView tvBindMobile;
    @BindView(R.id.ll_bind_phone)
    LinearLayout llBindPhone;
    @BindView(R.id.ll_updatePwd)
    LinearLayout llUpdatePwd;
    @BindView(R.id.iv_mineHead)
    ImageView ivMineHead;
    @BindView(R.id.rl_updateHeadImg)
    RelativeLayout rlUpdateHeadImg;
    @BindView(R.id.ll_updateName)
    LinearLayout llUpdateName;
    @BindView(R.id.ll_updateAddress)
    LinearLayout llUpdateAddress;
    @BindView(R.id.tv_phone_status)
    TextView tvPhoneStatus;
    private UpdateTextDialogUtil updateTextDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();

    }

    private void setupUI() {
        tvTitleName.setText("账号管理");
        updateTextDialogUtil = new UpdateTextDialogUtil();
        tvNickName.setTag("");//默认更改时的提示信息
        if (BuildConfig.projectCode == 177) {//177七二网络
            ivMineHead.setImageResource(R.mipmap.touxiang);
        }
        getUserInfoData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfoData();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountManageActivity.class);
        context.startActivity(starter);
    }
    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, AccountManageActivity.class);
        return starter;
    }

    private void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        int errorImage = R.mipmap.ic_launcher;
        if (BuildConfig.projectCode == 177) {//177七二网络
            errorImage = R.mipmap.touxiang;
        }
        tvNickName.setText(userInfoResultBean.getNickname());
//        GlideDisplay.display(ivMineHead, userInfoResultBean.getPortrait(), errorImage);
        Glide.with(AccountManageActivity.this).load(userInfoResultBean.getPortrait()).into(ivMineHead);
        if (TextUtils.isEmpty(userInfoResultBean.getMobile())) {
            tvBindMobile.setText("未设置");
            tvPhoneStatus.setText("绑定手机号");
            tvPhoneStatus.setTag(0);
        } else {
            tvBindMobile.setText(userInfoResultBean.getMobile());
            tvPhoneStatus.setText("修改手机号");
            tvPhoneStatus.setTag(1);
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_logout, R.id.ll_bind_phone,
            R.id.ll_updatePwd, R.id.ll_updateAddress, R.id.ll_updateName, R.id.rl_updateHeadImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.ll_bind_phone:
                if(tvPhoneStatus.getTag()!=null&&(int)tvPhoneStatus.getTag()==1){
                    AuthPhoneActivity.start(mContext,tvBindMobile.getText().toString());
                }else{
                    BindPhoneActivity.start(mContext);
                }
                break;
            case R.id.btn_logout:
                new HintDialogUtil().showHintDialog(AccountManageActivity.this, "友情提示", "是否确定退出登录", "确定", "取消", new HintDialogUtil.HintDialogListener() {
                    @Override
                    public void ok(String content) {
                        logout();
                    }

                    @Override
                    public void cancel() {
                    }
                });
                break;
            case R.id.ll_updatePwd:
                UpdatePwdActivity.start(mContext);
                break;
            case R.id.ll_updateAddress:
                UpdateAddressActivity.start(mContext);
                break;
            case R.id.ll_updateName:
                updateNickName();
                break;
            case R.id.rl_updateHeadImg:
                SelectPhotoCropActivity.startAndCropOval(mContext, "headimg", R.color.text_black, 130, 130);
                break;
        }
    }
    private void logout(){
        BaseRequestBean baseRequestBean=new BaseRequestBean();
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BaseRequestBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(BaseRequestBean data) {
            }
            @Override
            public void onFailure(String code, String msg) {
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.logoutApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);
        LoginControl.clearLogin();
        LoginActivity.start(mActivity);
        finish();
        HuosdkManager.getInstance().initSdk(this, new OnInitSdkListener() {
            @Override
            public void initSuccess(String code, String msg) {
            }

            @Override
            public void initError(String code, String msg) {
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectPhotoEvent(SelectPhotoEvent selectPhotoEvent) {
        if ("headimg".equals(selectPhotoEvent.flag) && selectPhotoEvent.imagePath != null) {
//            GlideDisplay.display(ivMineHead, new File(selectPhotoEvent.imagePath));
            Glide.with(AccountManageActivity.this).load(selectPhotoEvent.imagePath).into(ivMineHead);
            updateHeadImage(new File(selectPhotoEvent.imagePath));
        }
    }

    private void updateHeadImage(File file) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.userHeadImgApi);
        httpParams.put("portrait", file);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.userHeadImgApi), new HttpJsonCallBackDialog<AddressList>() {
            @Override
            public void onDataSuccess(AddressList data) {
                T.s(mContext, "上传成功");
            }
        });
    }

    private void updateNickName() {
        updateTextDialogUtil.showExchangeDialog(mContext, "昵称修改", tvNickName.getTag().toString(), new UpdateTextDialogUtil.UpdateTextDialogListener() {
            @Override
            public void ok(String content) {
                if (tvNickName.getText().toString().trim().equals(content.trim())) {
                    return;
                }
                tvNickName.setText(content);
                tvNickName.setTag(content);
                submitUpdateNickName(content);
            }

            @Override
            public void cancel() {
            }
        });
    }

    /**
     * 提交要修改的昵称
     *
     * @param newNickName 要修改的昵称
     */
    private void submitUpdateNickName(String newNickName) {
        final UpdateNickNameRequest updateNickNameRequest = new UpdateNickNameRequest();
        updateNickNameRequest.setNicename(newNickName);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(updateNickNameRequest));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                T.s(mContext, "修改成功");
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.nickNameUpdateApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
