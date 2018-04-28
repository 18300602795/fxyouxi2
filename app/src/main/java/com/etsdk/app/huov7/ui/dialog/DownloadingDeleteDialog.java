package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/8.
 */

public class DownloadingDeleteDialog {
    private static boolean noHint=false;
    private Dialog dialog;
    private DownloadingDeleteDialog.ConfirmDialogListener mlistener;
    public void showDialog(Context context, final DownloadingDeleteDialog.ConfirmDialogListener listener){
        dismiss();
        if(noHint){//不需要提示直接删除
            listener.ok();
            return;
        }
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_downloading_delete, null);
        dialog = new Dialog(context,R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
//        window.getAttributes().width = BaseAppUtil.getDeviceWidth(context) - BaseAppUtil.dip2px(context, 30);
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        windowparams.width = BaseAppUtil.getDeviceWidth(context);
        final CheckBox noHintCb = (CheckBox) dialogview.findViewById(R.id.cb_record_delete);
        dialogview.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ok();
                noHint=noHintCb.isChecked();
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
