package com.game.sdk.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.etsdk.app.huov7.R;

/**
 * Created by liu hong liang on 2017/2/6.
 * 加载等待对话框
 */
public class LoadWaitDialogUtil {
    private boolean canCancel=true;
    private String waitMsg;
    private Dialog dialog;
    public LoadWaitDialogUtil(boolean canCancel, String waitMsg) {
        this.canCancel = canCancel;
        this.waitMsg = waitMsg;
    }

    public void show(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_load_wait, null);
        dialog=new Dialog(context);
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setDimAmount(0.0F);
        dialog.setContentView(view);
        final TextView tvWaitMsg= (TextView) view.findViewById(R.id.tv_waitMsg);
        if (!TextUtils.isEmpty(waitMsg)) {
            tvWaitMsg.setText(waitMsg);
        }
        if (!canCancel) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode==KeyEvent.KEYCODE_BACK){
                        return true;
                    }
                    return false;
                }
            });
        }
        dialog.show();
    }
    public void dismiss(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public String getWaitMsg() {
        return waitMsg;
    }

    public void setWaitMsg(String waitMsg) {
        this.waitMsg = waitMsg;
    }
}
