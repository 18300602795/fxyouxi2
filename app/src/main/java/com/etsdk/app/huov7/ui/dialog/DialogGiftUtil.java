package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.game.sdk.log.T;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by liu hong liang on 2016/12/8.
 */

public class DialogGiftUtil {
    private Dialog dialog;
    private View.OnClickListener listener;

    public void showConvertDialog(Context context, final String giftCode, final String gameId, View.OnClickListener listener) {
        dismiss();
        this.listener = listener;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_gift_background, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView down = (TextView) view.findViewById(R.id.dialog_down);
        TextView tvGiftCode = (TextView) view.findViewById(R.id.tv_gift_code);
        TextView tvCopy = (TextView) view.findViewById(R.id.tv_copy);
        tvGiftCode.setText(giftCode);
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAppUtil.copyToSystem(v.getContext(),giftCode);
                T.s(v.getContext(),"复制成功");
                dismiss();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(v.getContext(),gameId);
                dismiss();
            }
        });
        LinearLayout ll_show_gift_code = (LinearLayout) view.findViewById(R.id.ll_show_gift_code);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = BaseAppUtil.getDeviceWidth(context) - BaseAppUtil.dip2px(context, 60);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            listener = null;
        }
    }

    public interface ConfirmDialogListener {

        void ok();

        void cancel();
    }
}
