package com.etsdk.app.huov7.shop.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;

/**
 * 购买小号提示对话框
 */

public class BuyHintDialogUtil {
    private Dialog dialog;
    public void show(Context context, final Listener listener) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_buy_hint, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        final CheckBox cb_read = (CheckBox) view.findViewById(R.id.cb_read);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView tv_buy = (TextView) view.findViewById(R.id.tv_buy);


        cb_read.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tv_buy.setBackgroundResource(isChecked? R.drawable.shape_circle_rect_green_deep: R.drawable.shape_circle_rect_gray_gray_5);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.cancel();
                }
                dismiss();
            }
        });
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb_read.isChecked()){
                    T.s(v.getContext(), "请仔细阅读买家须知，并勾选");
                    return;
                }
                if(listener!=null){
                    listener.ok();
                }
                dismiss();
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

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface Listener{
        public void ok();
        public void cancel();
    }
}
