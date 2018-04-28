package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.view.SelectAccountView;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/22.
 * 选择账号登陆对话框
 */

public class SelectAccountLoginDialog {

    private Dialog dialog;
    private SelectAccountListener selectAccountListener;
    public SelectAccountLoginDialog(SelectAccountListener selectAccountListener) {
        this.selectAccountListener=selectAccountListener;
    }

    public void showAccountLoginDialog(Context context, List<LoginResultBean.UserName> userNameList) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_accout_login, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        SelectAccountView selectAccountView = (SelectAccountView) view.findViewById(R.id.selectAccountView);
        selectAccountView.setUserNameList(dialog,userNameList,selectAccountListener);
        dialog.show();
    }
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface SelectAccountListener{
        public void onSelectAccount(String username);
    }
}
