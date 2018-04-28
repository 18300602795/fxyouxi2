package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;

/**
 * Created by liu hong liang on 2016/12/8.
 */

public class SignInOkDialogUtil {
    public static void showDialog(Context context, final String signScore) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_ok, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        TextView tvAwardScore = (TextView) view.findViewById(R.id.tv_award_score);
        tvAwardScore.setText(signScore+"积分");
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
