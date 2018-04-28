package com.etsdk.app.huov7.shop.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.shop.model.ShareOptionEvent;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liu hong liang on 2016/12/17.
 * 分享操作提示对话框
 */

public class ShareOptionDialogUtil {
    private Dialog dialog;
    public void show(Context context) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_option, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        LinearLayout ll_weibo = (LinearLayout) view.findViewById(R.id.ll_weibo);
        LinearLayout ll_wx = (LinearLayout) view.findViewById(R.id.ll_wx);
        LinearLayout ll_wx_circle = (LinearLayout) view.findViewById(R.id.ll_wx_circle);
        LinearLayout ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);
        LinearLayout ll_qzone = (LinearLayout) view.findViewById(R.id.ll_qzone);
        LinearLayout ll_copy = (LinearLayout) view.findViewById(R.id.ll_copy);


        ll_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.WEIBO));
                dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.WEIXIN));
                dismiss();
            }
        });
        ll_wx_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.PENGYOUQUAN));
                dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.QQ));
                dismiss();
            }
        });
        ll_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.QQKONGJIAN));
                dismiss();
            }
        });
        ll_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ShareOptionEvent(ShareOptionEvent.COPY));
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
}
