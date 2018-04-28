package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/17.
 * 更新信息用的兑换框
 */

public class UpdateTextDialogUtil {
    private Dialog dialog;
    public void showExchangeDialog(Context context, String title, String content, final UpdateTextDialogListener listener) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_text, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView ok = (TextView) view.findViewById(R.id.dialog_ok);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        final EditText etContent = (EditText) view.findViewById(R.id.et_content);
        tvTitle.setText(title);
        etContent.setText(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.ok(etContent.getText().toString());
                    dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = BaseAppUtil.getDeviceWidth(context) - BaseAppUtil.dip2px(context, 100);
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

    public interface UpdateTextDialogListener {
        void ok(String content);
        void cancel();
    }
}
