package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AddressInfo;
import com.etsdk.app.huov7.model.AddressUpdateRequestBean;
import com.etsdk.app.huov7.model.UserAddressInfo;
import com.etsdk.app.huov7.ui.dialog.SelectAddressDialog;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/29.
 * 地址信息界面
 */
public class UpdateAddressActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_update_address)
    LinearLayout activityUpdateAddress;
    @BindView(R.id.et_consignee)
    EditText etConsignee;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.tv_show_address)
    TextView tvShowAddress;
    @BindView(R.id.et_addressDetail)
    EditText etAddressDetail;
    @BindView(R.id.et_zipcode)
    EditText etZipcode;
    UserAddressInfo userAddressInfo;
    private SelectAddressDialog selectAddressDialog;
    private Map<Integer,AddressInfo> mLevelAddressMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("地址信息");
        getAddressInfo();

    }
    private SelectAddressDialog.SelectAddressOkListener selectAddressOkListener=new SelectAddressDialog.SelectAddressOkListener() {
        @Override
        public void selectAddressInfo(Map<Integer,AddressInfo> levelAddressMap) {
            mLevelAddressMap=levelAddressMap;
            StringBuilder builder=new StringBuilder();
            for(int i=1;i<=4;i++){
                AddressInfo addressInfo = levelAddressMap.get(i);
                if(addressInfo!=null){
                    builder.append(addressInfo.getName());
                }
            }
            tvShowAddress.setText(builder.toString());
        }
    };
    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_submit,R.id.tv_show_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.tv_show_address:
                new SelectAddressDialog(mContext,selectAddressOkListener).show(userAddressInfo);
                break;
            case R.id.btn_submit:
                submitAddressInfo();
                break;
        }
    }

    private void getAddressInfo() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserAddressInfo>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserAddressInfo data) {
                if (data != null) {
                    updateAddressInfo(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.addressDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void updateAddressInfo(UserAddressInfo data) {
        this.userAddressInfo = data;
        etConsignee.setText(data.getConsignee());
        etMobile.setText(data.getMobile());
        etAddressDetail.setText(data.getAddress());
        etZipcode.setText(data.getZipcode());
        tvShowAddress.setText(data.getTopaddress());
    }

    private void submitAddressInfo() {
        String consignee = etConsignee.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String addressDetail = etAddressDetail.getText().toString().trim();
        String zipcode = etZipcode.getText().toString().trim();

        if(TextUtils.isEmpty(consignee)){
            T.s(mContext,"请填写收货人");
            return;
        }
        if(TextUtils.isEmpty(mobile)){
            T.s(mContext,"请填写电话号码");
            return;
        }
        if(TextUtils.isEmpty(addressDetail)){
            T.s(mContext,"请填写详细地址");
            return;
        }
        if(TextUtils.isEmpty(addressDetail)){
            T.s(mContext,"请填写邮编");
            return;
        }
        final AddressUpdateRequestBean addressUpdateRequestBean = new AddressUpdateRequestBean();
        addressUpdateRequestBean.setConsignee(consignee);
        addressUpdateRequestBean.setAddress(addressDetail);
        addressUpdateRequestBean.setZipcode(zipcode);
        addressUpdateRequestBean.setMobile(mobile);
        addressUpdateRequestBean.setCountry("0");
        if(mLevelAddressMap==null){//没有选择过,使用老的
            if(userAddressInfo==null){
                return;
            }
            if(!TextUtils.isEmpty(userAddressInfo.getProvince())){
                addressUpdateRequestBean.setProvince(userAddressInfo.getProvince());
            }else{
                T.s(mContext,"请先选择地址");
                return;
            }
            if(!TextUtils.isEmpty(userAddressInfo.getCity())){
                addressUpdateRequestBean.setCity(userAddressInfo.getCity());
            }else{
                addressUpdateRequestBean.setCity("0");
            }
            if(!TextUtils.isEmpty(userAddressInfo.getDistrict())){
                addressUpdateRequestBean.setDistrict(userAddressInfo.getDistrict());
            }else{
                addressUpdateRequestBean.setDistrict("0");
            }
            if(!TextUtils.isEmpty(userAddressInfo.getTown())){
                addressUpdateRequestBean.setTown(userAddressInfo.getTown());
            }else{
                addressUpdateRequestBean.setTown("0");
            }
        }else{
            if(mLevelAddressMap.get(1)!=null){
                addressUpdateRequestBean.setProvince(mLevelAddressMap.get(1).getId());
            }else{
                T.s(mContext,"请先选择地址");
                return;
            }
            if(mLevelAddressMap!=null&&mLevelAddressMap.get(2)!=null){
                addressUpdateRequestBean.setCity(mLevelAddressMap.get(2).getId());
            }else{
                addressUpdateRequestBean.setCity("0");
            }
            if(mLevelAddressMap.get(3)!=null){
                addressUpdateRequestBean.setDistrict(mLevelAddressMap.get(3).getId());
            }else{
                addressUpdateRequestBean.setDistrict("0");
            }
            if(mLevelAddressMap.get(4)!=null){
                addressUpdateRequestBean.setTown(mLevelAddressMap.get(4).getId());
            }else{
                addressUpdateRequestBean.setTown("0");
            }
        }



        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(addressUpdateRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserAddressInfo>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserAddressInfo data) {
               T.s(mContext,"保存成功");
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.addressUpdateApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, UpdateAddressActivity.class);
        context.startActivity(starter);
    }
}
