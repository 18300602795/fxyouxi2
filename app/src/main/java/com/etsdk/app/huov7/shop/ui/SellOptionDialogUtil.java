package com.etsdk.app.huov7.shop.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.shop.model.SellOptionEvent;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liu hong liang on 2016/12/17.
 * 卖号详情操作提示对话框
 */

public class SellOptionDialogUtil {
    private Dialog dialog;
    public void show(Context context, final int status) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sell_option, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        TextView tv_cancel_sell = (TextView) view.findViewById(R.id.tv_cancel_sell);
        TextView tv_delete_sell = (TextView) view.findViewById(R.id.tv_delete_sell);
        TextView tv_edit_sell = (TextView) view.findViewById(R.id.tv_edit_sell);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        View edit_line = view.findViewById(R.id.line_edit);
        //显示取消或删除
        if(status == SellOptionEvent.STATUS_VARIFY || status == SellOptionEvent.STATUS_ON) {//审核、上架
            tv_cancel_sell.setVisibility(View.VISIBLE);
            tv_delete_sell.setVisibility(View.GONE);
        }else{//已下架、已出售、审核失败
            tv_cancel_sell.setVisibility(View.GONE);
            tv_delete_sell.setVisibility(View.VISIBLE);
        }
        //编辑按钮的显示
        if(status == SellOptionEvent.STATUS_VARIFY_FAIL || status == SellOptionEvent.STATUS_ON){//上架、审核失败
            tv_edit_sell.setVisibility(View.VISIBLE);
            edit_line.setVisibility(View.VISIBLE);
        }else{//审核中、已下架、已出售
            tv_edit_sell.setVisibility(View.GONE);
            edit_line.setVisibility(View.GONE);
        }

        tv_cancel_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SellOptionEvent(SellOptionEvent.OPTION_CANCEL_SELL));
                dismiss();
            }
        });
        tv_delete_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SellOptionEvent(SellOptionEvent.OPTION_DELETE_SELL));
                dismiss();
            }
        });
        tv_edit_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SellOptionEvent(SellOptionEvent.OPTION_EDIT_SELL));
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
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
}
