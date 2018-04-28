package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/17.
 * 提示对话框
 */

public class HintDialogUtil {
    private Dialog dialog;
    public void showHintDialog(Context context, String title, String content,String okText,String cancelText, final HintDialogListener listener) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint_msg, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView etOk = (TextView) view.findViewById(R.id.dialog_ok);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        final TextView etContent = (TextView) view.findViewById(R.id.et_content);
        tvTitle.setText(title);
        etContent.setText(content);
        if(TextUtils.isEmpty(okText)){
            etOk.setVisibility(View.GONE);
        }else{
            etOk.setText(okText);
        }
        if(TextUtils.isEmpty(cancelText)){
            cancel.setVisibility(View.GONE);
        }else{
            cancel.setText(cancelText);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.cancel();
                }
                dismiss();
            }
        });
        etOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.ok(etContent.getText().toString());
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

    public interface HintDialogListener {
        void ok(String content);
        void cancel();
    }
}
