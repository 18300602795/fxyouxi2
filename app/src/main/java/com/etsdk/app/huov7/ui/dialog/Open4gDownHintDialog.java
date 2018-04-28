package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/8.
 */

public class Open4gDownHintDialog {
    private Dialog dialog;
    private Open4gDownHintDialog.ConfirmDialogListener mlistener;
    public void showDialog(Context context, final Open4gDownHintDialog.ConfirmDialogListener listener){
        dismiss();

        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_open_4g_down_hint, null);
        dialog = new Dialog(context,R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.getAttributes().width = BaseAppUtil.getDeviceWidth(context) - BaseAppUtil.dip2px(context, 30);
        window.setGravity(Gravity.CENTER);
        dialogview.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ok();
                dismiss();
            }
        });
        dialogview.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
                dismiss();
            }
        });
        dialog.show();
    }
    public void dismiss(){
        if(dialog !=null){
            dialog.dismiss();
            mlistener=null;
        }
    }
    public interface ConfirmDialogListener{
        void ok();
        void cancel();
    }
}
