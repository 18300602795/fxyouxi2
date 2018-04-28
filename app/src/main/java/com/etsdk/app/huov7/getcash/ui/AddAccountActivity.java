package com.etsdk.app.huov7.getcash.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.getcash.GetCaseApi;
import com.etsdk.app.huov7.getcash.model.AccountTypeListBean;
import com.etsdk.app.huov7.getcash.model.AddAccountRequestBean;
import com.etsdk.app.huov7.getcash.model.ResultBean;
import com.etsdk.app.huov7.http.AppApi;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加账户
 */
public class AddAccountActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.et_account_type)
    EditText etAccountType;
    @BindView(R.id.iv_array)
    ImageView ivArray;
    @BindView(R.id.v_bank_line)
    View vBankLine;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.tr_bank)
    TableRow trBank;
    @BindView(R.id.v_blanch_line)
    View vBlanchLine;
    @BindView(R.id.et_bank_blanch)
    EditText etBankBlanch;
    @BindView(R.id.tr_bank_blanch)
    TableRow trBankBlanch;
    @BindView(R.id.v_code_line)
    View vCodeLine;
    @BindView(R.id.et_bank_code)
    EditText etBankCode;
    @BindView(R.id.tr_bank_code)
    TableRow trBankCode;
    @BindView(R.id.v_account_id_line)
    View vAccountIdLine;
    @BindView(R.id.et_account_id)
    EditText etAccountId;
    @BindView(R.id.tr_account_id)
    TableRow trAccountId;
    @BindView(R.id.v_account_name_line)
    View vAccountNameLine;
    @BindView(R.id.et_account_name)
    EditText etAccountName;
    @BindView(R.id.tr_account_name)
    TableRow trAccountName;
    @BindView(R.id.btn_ok)
    TextView btnOk;
    @BindView(R.id.cb_set_default)
    CheckBox cbSetDefault;
    @BindView(R.id.fl_array)
    FrameLayout flArray;
    private PopupWindow popupWindow;
    String payType = "bank";
    String[] payTypeArray;
    String[] payTypeNameArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("添加账户");
        getAccountType();
    }

    /**
     * 获取账户类型
     */
    private void getAccountType() {
        BaseRequestBean requestBean =new BaseRequestBean();
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AccountTypeListBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final AccountTypeListBean data) {
                if(data!=null){
                    payTypeArray = new String[data.getType_list().size()];
                    payTypeNameArray = new String[data.getType_list().size()];
                    for(int i = 0; i < data.getType_list().size(); i++){
                        payTypeArray[i] = data.getType_list().get(i).getPayname();
                        payTypeNameArray[i] = data.getType_list().get(i).getName();
                    }
                }
            }
            @Override
            public void onFailure(String code, String msg) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_TYPE), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AddAccountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.et_account_type, R.id.fl_array, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.fl_array:
                showPop();
                break;
            case R.id.et_account_type:
                showPop();
                break;
            case R.id.btn_ok:
                commit();
                break;
        }
    }

    /**
     * 添加提交
     */
    private void commit() {
        AddAccountRequestBean requestBean =new AddAccountRequestBean();
        requestBean.setPaytype(payType);//提现类型标识
        if("bank".equals(payType)) {
            String code = etBankCode.getText().toString().trim();
            if(code.length()<9){
                T.s(mContext, "请输入正确的银行卡号");
                return;
            }
            requestBean.setAccount(etBankCode.getText().toString().trim());//第三方支付账户，提现类型为bank时传银行卡号，其它类型传第三方账号
        }else{
            requestBean.setAccount(etAccountId.getText().toString().trim());//支付宝&微信
        }
        requestBean.setBankname(etBank.getText().toString().trim());//银行名称，提现类型为bank时不能为空
        requestBean.setCurbank(etBankBlanch.getText().toString().trim());//分行名称，提现类型为bank时不能为空
        requestBean.setHolder(etAccountName.getText().toString().trim());//开户人
        requestBean.setIsdefault(cbSetDefault.isChecked()?1:0);//是否设置为默认支付，0为否，1为是
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                if("200".equals(code)){
                    T.s(mContext, "添加成功");
                    finish();
                }else{
                    T.s(mContext, msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(mContext, "添加失败 "+code);
                L.e(TAG, "添加失败 "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_ADD), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    private void showPop() {
        if(payTypeArray == null || payTypeNameArray == null
                || payTypeNameArray.length == 0 || payTypeArray.length == 0){
            T.s(mContext, "未获取到分类信息，请稍后再试");
            return;
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_account_type, null);
        ListView listView = (ListView) contentView.findViewById(R.id.lv_type);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_account_type, payTypeNameArray));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clearEditext();
                if("银行卡".equals(payTypeNameArray[position])){
                    payType = payTypeArray[position];
                    showBankInput();
                }else if("支付宝".equals(payTypeNameArray[position])){
                    payType = payTypeArray[position];
                    showZhifubaoInput();
                }else if("微信".equals(payTypeNameArray[position])){
                    payType = payTypeArray[position];
                    showWeixinInput();
                }
            }
        });
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(flArray, 0, 0);
    }

    private void showBankInput() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        etAccountType.setText("银行卡");

        vBankLine.setVisibility(View.VISIBLE);
        trBank.setVisibility(View.VISIBLE);
        vBlanchLine.setVisibility(View.VISIBLE);
        trBankBlanch.setVisibility(View.VISIBLE);
        vCodeLine.setVisibility(View.VISIBLE);
        trBankCode.setVisibility(View.VISIBLE);

        vAccountIdLine.setVisibility(View.GONE);
        trAccountId.setVisibility(View.GONE);

        vAccountNameLine.setVisibility(View.VISIBLE);
        trAccountName.setVisibility(View.VISIBLE);
        cbSetDefault.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    private void showWeixinInput() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        etAccountType.setText("微信");

        vBankLine.setVisibility(View.GONE);
        trBank.setVisibility(View.GONE);
        vBlanchLine.setVisibility(View.GONE);
        trBankBlanch.setVisibility(View.GONE);
        vCodeLine.setVisibility(View.GONE);
        trBankCode.setVisibility(View.GONE);

        vAccountIdLine.setVisibility(View.VISIBLE);
        trAccountId.setVisibility(View.VISIBLE);
        etAccountId.setHint("请输入您要提现的微信账号");

        vAccountNameLine.setVisibility(View.VISIBLE);
        trAccountName.setVisibility(View.VISIBLE);
        cbSetDefault.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    private void showZhifubaoInput() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        etAccountType.setText("支付宝");

        vBankLine.setVisibility(View.GONE);
        trBank.setVisibility(View.GONE);
        vBlanchLine.setVisibility(View.GONE);
        trBankBlanch.setVisibility(View.GONE);
        vCodeLine.setVisibility(View.GONE);
        trBankCode.setVisibility(View.GONE);

        vAccountIdLine.setVisibility(View.VISIBLE);
        trAccountId.setVisibility(View.VISIBLE);
        etAccountId.setHint("请输入您要提现的支付宝账号");

        vAccountNameLine.setVisibility(View.VISIBLE);
        trAccountName.setVisibility(View.VISIBLE);
        cbSetDefault.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    private void clearEditext(){
        etAccountId.setText("");
        etBankCode.setText("");
        etBank.setText("");
        etAccountName.setText("");
        etBankBlanch.setText("");
    }
}
