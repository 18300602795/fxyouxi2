package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/17.
 * 代金券兑换兑换框
 */

public class EntityExchangeDialogUtil {
    private Dialog dialog;

    public void showExchangeDialog(Context context,String content) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_entity_exchange_ok, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public interface ConfirmDialogListener {
        void ok();
        void cancel();
    }
}
