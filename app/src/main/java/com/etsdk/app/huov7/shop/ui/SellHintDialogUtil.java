package com.etsdk.app.huov7.shop.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.shop.model.ResultBean;
import com.etsdk.app.huov7.shop.model.VerifyCodeRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;

/**
 * 购买小号提示对话框
 */

public class SellHintDialogUtil {
    private Dialog dialog;
    private Handler mHandler = new Handler();
    private TextView tv_get_code;

    public void show(final Context context, float moneyHint, final String mobile, int mode, final Listener listener) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sell_hint, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        final CheckBox cb_read = (CheckBox) view.findViewById(R.id.cb_read);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setText(mode== SellFragment.MODE_PUBLISH?"放弃出售":"放弃修改");

        final TextView tv_sell = (TextView) view.findViewById(R.id.tv_sell);
        tv_sell.setText(mode== SellFragment.MODE_PUBLISH?"确定出售":"确定提交");

        tv_get_code = (TextView) view.findViewById(R.id.tv_get_code);
        TextView tv_money_hint = (TextView) view.findViewById(R.id.tv_money_hint);

        final EditText et_code = (EditText) view.findViewById(R.id.et_code);
        tv_money_hint.setText("*售出将获得" + moneyHint + "平台币，平台币可用于平台所有游戏充值，也可在个人中心提现。");

        cb_read.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tv_sell.setBackgroundResource(isChecked ? R.drawable.shape_circle_rect_green_deep : R.drawable.shape_circle_rect_gray_gray_5);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancel();
                }
                dismiss();
            }
        });
        tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_read.isChecked()) {
                    T.s(v.getContext(), "请仔细阅读卖家须知，并勾选");
                    return;
                }
                if (listener != null) {
                    if(TextUtils.isEmpty(et_code.getText().toString())){
                        T.s(v.getContext(), "请输入验证码");
                        return;
                    }
                    listener.ok(et_code.getText().toString());
                }
                dismiss();
            }
        });
        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                VerifyCodeRequestBean requestBean = new VerifyCodeRequestBean();
                requestBean.setMobile(mobile);
                requestBean.setSmstype("4");
                HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
                HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(v.getContext(), httpParamsBuild.getAuthkey()) {
                    @Override
                    public void onDataSuccess(ResultBean data) {
                    }

                    @Override
                    public void onDataSuccess(ResultBean data, String code, String msg) {
                        if("200".equals(code)){
                            startCodeTime(60);
                        }else{
                            T.s(v.getContext(), "发送验证码失败 "+msg);
                        }
                    }

                    @Override
                    public void onFailure(String code, String msg) {
                        T.s(v.getContext(), "发送验证码失败 "+msg);
                    }
                };
                httpCallbackDecode.setShowTs(true);
                httpCallbackDecode.setLoadingCancel(false);
                httpCallbackDecode.setShowLoading(true);
                httpCallbackDecode.setLoadMsg("正在发送...");
                RxVolley.post(AppApi.getUrl(AppApi.smsSendApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = BaseAppUtil.getDeviceWidth(context) - BaseAppUtil.dip2px(context, 30);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void startCodeTime(int time) {
        tv_get_code.setTag(time);
        if (time <= 0) {
            tv_get_code.setText("获取验证码");
            tv_get_code.setClickable(true);
            return;
        } else {
            tv_get_code.setClickable(false);
            tv_get_code.setText(time + "秒");
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = (int) tv_get_code.getTag();
                startCodeTime(--delayTime);

            }
        }, 1000);
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface Listener {
        public void ok(String code);

        public void cancel();
    }
}
