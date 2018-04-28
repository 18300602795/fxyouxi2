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
import com.etsdk.app.huov7.model.PreOrderInfoResultBean;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/22.
 * 选择支付方式对话框
 */

public class SelectPayTypeDialog {

    private Dialog dialog;

    private SelectPayTypeListener listener;
    public SelectPayTypeDialog(SelectPayTypeListener listener) {
        this.listener = listener;
    }

    public void showPayTypeDialog(Context context, PreOrderInfoResultBean data) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_pay_type, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        setupData(view,data);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    private void setupData(View rootView,PreOrderInfoResultBean data){
        View rlPayType1 = rootView.findViewById(R.id.rl_paytype1);
        View rlPayType2 = rootView.findViewById(R.id.rl_paytype2);
        View llPayType3 = rootView.findViewById(R.id.ll_paytype3);
        View rlPayType3 = rootView.findViewById(R.id.rl_paytype3);
        ImageView ivPayType1 = (ImageView) rootView.findViewById(R.id.iv_paytype1);
        TextView tvPayType1 = (TextView) rootView.findViewById(R.id.tv_paytype1);
        ImageView ivPayType2 = (ImageView) rootView.findViewById(R.id.iv_paytype2);
        TextView tvPayType2 = (TextView) rootView.findViewById(R.id.tv_paytype2);
        ImageView ivPayType3 = (ImageView) rootView.findViewById(R.id.iv_paytype3);
        TextView tvPayType3 = (TextView) rootView.findViewById(R.id.tv_paytype3);
        List<PreOrderInfoResultBean.PayTypeData> list = data.getList();
        int size = list.size();
        if(size==1){
            llPayType3.setVisibility(View.GONE);
            rlPayType2.setVisibility(View.INVISIBLE);
            setShow(ivPayType1,tvPayType1,list.get(0));
            rlPayType1.setTag(list.get(0).getPaytype());
        }else if(size==2){
            llPayType3.setVisibility(View.GONE);
            rlPayType2.setVisibility(View.VISIBLE);
            setShow(ivPayType1,tvPayType1,list.get(0));
            rlPayType1.setTag(list.get(0).getPaytype());
            setShow(ivPayType2,tvPayType2,list.get(1));
            rlPayType2.setTag(list.get(1).getPaytype());
        } else{
            llPayType3.setVisibility(View.VISIBLE);
            setShow(ivPayType1,tvPayType1,list.get(0));
            rlPayType1.setTag(list.get(0).getPaytype());
            setShow(ivPayType2,tvPayType2,list.get(1));
            rlPayType2.setTag(list.get(1).getPaytype());
            setShow(ivPayType3,tvPayType3,list.get(2));
            rlPayType3.setTag(list.get(2).getPaytype());
        }
        rlPayType1.setOnClickListener(onClickListener);
        rlPayType2.setOnClickListener(onClickListener);
        rlPayType3.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.selectPayType(v.getTag().toString());
                dismiss();
            }
        }
    };
    private void setShow(ImageView imageView, TextView textView, PreOrderInfoResultBean.PayTypeData payTypeData){
        if("alipay".equals(payTypeData.getPaytype())){
            imageView.setImageResource(R.mipmap.zfbpay);
            textView.setText("支付宝");
        }else if("spay".equals(payTypeData.getPaytype())){
            imageView.setImageResource(R.mipmap.wxpay);
            textView.setText("微信支付");
        }else if("payeco".equals(payTypeData.getPaytype())){
            imageView.setImageResource(R.mipmap.unionpay);
            textView.setText("银联支付");
        }
    }
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    public interface SelectPayTypeListener {
        void selectPayType(String paytype);
    }
}
