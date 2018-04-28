package com.etsdk.app.huov7.shop.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.pay.ChargeActivityForWap;
import com.etsdk.app.huov7.shop.model.AccountOperationFavorRequestBean;
import com.etsdk.app.huov7.shop.model.ResultBean;
import com.etsdk.app.huov7.shop.model.SellCancelRequestBean;
import com.etsdk.app.huov7.shop.model.SellOptionEvent;
import com.etsdk.app.huov7.shop.model.ShopListBean;
import com.etsdk.app.huov7.shop.ui.BuyHintDialogUtil;
import com.etsdk.app.huov7.shop.ui.SellDetailActivity;
import com.etsdk.app.huov7.shop.ui.SellEditActivity;
import com.etsdk.app.huov7.shop.ui.ShopListFragment;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShopListAdapter extends RecyclerView.Adapter implements IDataAdapter<List<ShopListBean.DataBean.ListBean>> {

    List<ShopListBean.DataBean.ListBean> activityList = new ArrayList<>();
    private int type = 0;
    private BaseRefreshLayout baseRefreshLayout;

    public ShopListAdapter(BaseRefreshLayout baseRefreshLayout, List<ShopListBean.DataBean.ListBean> activityList, int type) {
        if (activityList != null) {
            this.activityList = activityList;
        }
        this.type = type;
        this.baseRefreshLayout = baseRefreshLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShopListBean.DataBean.ListBean data = activityList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == ShopListFragment.TYPE_MY_BOUGHT){
                    SellDetailActivity.start(v.getContext(), data.getAccount_deal_id());
                }else{
                    SellDetailActivity.start(v.getContext(), data.getId());
                }
            }
        });
        viewHolder.tvGameName.setText(data.getGamename());
        viewHolder.tvServer.setText(data.getServername());
        viewHolder.tvDesc.setText(data.getDescription());
//        GlideDisplay.display(viewHolder.ivIcon, data.getIcon(), R.mipmap.icon_load);
        Glide.with(viewHolder.context).load(data.getIcon()).placeholder(R.mipmap.icon_load).into(viewHolder.ivIcon);
        String time;
        switch (type){
            case ShopListFragment.TYPE_ALL:
                viewHolder.tvMoneyTop.setVisibility(View.GONE);
                viewHolder.tvDetail.setVisibility(View.VISIBLE);
                viewHolder.tvDetail.setText(data.getIs_self()==1?"购买":"查看");
                viewHolder.tvCancelFavor.setVisibility(View.GONE);

                time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getCreate_time()*1000L));
                viewHolder.tvInfo.setText("上架时间："+time);

                viewHolder.tvMoneyBottom.setVisibility(View.VISIBLE);
                viewHolder.tvMoneyBottom.setText("￥"+data.getTotal_price());
                viewHolder.tvDelete.setVisibility(View.GONE);
                viewHolder.tvCancel.setVisibility(View.GONE);
                viewHolder.tvEdit.setVisibility(View.GONE);
                break;
            case ShopListFragment.TYPE_MY_BOUGHT:
                viewHolder.tvMoneyTop.setVisibility(View.GONE);
                viewHolder.tvDetail.setVisibility(View.VISIBLE);
                viewHolder.tvDetail.setText("查看");
                viewHolder.tvCancelFavor.setVisibility(View.GONE);

                time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getPay_time()*1000L));
                viewHolder.tvInfo.setText("交易时间："+time);

                viewHolder.tvMoneyBottom.setVisibility(View.VISIBLE);
                viewHolder.tvMoneyBottom.setText("￥"+data.getTotal_price());
                viewHolder.tvDelete.setVisibility(View.GONE);
                viewHolder.tvCancel.setVisibility(View.GONE);
                viewHolder.tvEdit.setVisibility(View.GONE);
                break;
            case ShopListFragment.TYPE_MY_SELL:
                viewHolder.tvMoneyTop.setVisibility(View.VISIBLE);
                viewHolder.tvMoneyTop.setText("￥"+data.getTotal_price());
                viewHolder.tvDetail.setVisibility(View.GONE);
                viewHolder.tvCancelFavor.setVisibility(View.GONE);

                if(data.getStatus() == SellOptionEvent.STATUS_VARIFY){//审核中
                    viewHolder.tvInfo.setText("待审核");
                    viewHolder.tvInfo.setTextColor(Color.parseColor("#00ba9d"));
                    viewHolder.tvEdit.setVisibility(View.GONE);
                }else if(data.getStatus() == SellOptionEvent.STATUS_ON){//已上架
                    viewHolder.tvInfo.setText("已上架");
                    viewHolder.tvInfo.setTextColor(Color.parseColor("#00ba9d"));
                    viewHolder.tvEdit.setVisibility(View.VISIBLE);
                }else if(data.getStatus() == SellOptionEvent.STATUS_OFF){
                    viewHolder.tvInfo.setText("已下架");
                    viewHolder.tvInfo.setTextColor(Color.parseColor("#a2a2a2"));
                    viewHolder.tvEdit.setVisibility(View.GONE);
                }else if(data.getStatus() == SellOptionEvent.STATUS_SOLD){
                    viewHolder.tvInfo.setText("已出售");
                    viewHolder.tvInfo.setTextColor(Color.parseColor("#a2a2a2"));
                    viewHolder.tvEdit.setVisibility(View.GONE);
                }else if(data.getStatus() == SellOptionEvent.STATUS_VARIFY_FAIL){
                    viewHolder.tvInfo.setText("审核失败");
                    viewHolder.tvInfo.setTextColor(Color.parseColor("#a2a2a2"));
                    viewHolder.tvEdit.setVisibility(View.VISIBLE);
                }

                viewHolder.tvMoneyBottom.setVisibility(View.GONE);
                if(data.getStatus() == SellOptionEvent.STATUS_VARIFY || data.getStatus() == SellOptionEvent.STATUS_ON){//审核中、已上架
                    viewHolder.tvDelete.setVisibility(View.GONE);
                    viewHolder.tvCancel.setVisibility(View.VISIBLE);
                }else {//已下架、已出售、审核失败
                    viewHolder.tvDelete.setVisibility(View.VISIBLE);
                    viewHolder.tvCancel.setVisibility(View.GONE);
                }
                break;
            case ShopListFragment.TYPE_MY_FAVOR:
                viewHolder.tvMoneyTop.setVisibility(View.GONE);
                viewHolder.tvDetail.setVisibility(View.GONE);
                viewHolder.tvCancelFavor.setVisibility(View.VISIBLE);

                time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getCreate_time()*1000L));
                viewHolder.tvInfo.setText("上架时间："+time);

                viewHolder.tvMoneyBottom.setVisibility(View.VISIBLE);
                viewHolder.tvMoneyBottom.setText("￥"+data.getTotal_price());
                viewHolder.tvDelete.setVisibility(View.GONE);
                viewHolder.tvCancel.setVisibility(View.GONE);
                viewHolder.tvEdit.setVisibility(View.GONE);
                break;
        }
        viewHolder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(type == ShopListFragment.TYPE_ALL){
                    if(data.getIs_self()==1) {//不是自己的
                        new BuyHintDialogUtil().show(v.getContext(), new BuyHintDialogUtil.Listener() {
                            @Override
                            public void ok() {
                                ChargeActivityForWap.start(v.getContext(), AppApi.getUrl(AppApi.accountBuy), "确认订单", data.getId());
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }else{//自己的
                        SellDetailActivity.start(v.getContext(), data.getId());
                    }
                }else if(type == ShopListFragment.TYPE_MY_SELL){
                    SellDetailActivity.start(v.getContext(), data.getId());
                }else if(type == ShopListFragment.TYPE_MY_BOUGHT){
                    SellDetailActivity.start(v.getContext(), data.getAccount_deal_id());
                }
            }
        });
        viewHolder.tvCancelFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == ShopListFragment.TYPE_MY_FAVOR){
                    cancelFavor(v.getContext(), data.getId());
                }
            }
        });
        viewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(type == ShopListFragment.TYPE_MY_SELL){
                    //如果状态为审核或出售中
                    new AlertDialog.Builder(v.getContext()).setTitle("取消出售").setMessage("取消出售后，小号将转回到您的账号。如登录未见小号，请点悬浮球切换。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelSell(v.getContext(), data.getId());
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(type == ShopListFragment.TYPE_MY_SELL){
                    //如果状态为已取消或交易成功
                    new AlertDialog.Builder(v.getContext()).setTitle("取消出售").setMessage("删除后无法恢复，是否确认删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelSell(v.getContext(), data.getId());
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            }
        });
        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == ShopListFragment.TYPE_MY_SELL){
                    //如果状态为出售中
                    SellEditActivity.start(v.getContext(), data.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    @Override
    public void notifyDataChanged(List<ShopListBean.DataBean.ListBean> newsListBeen, boolean b) {

    }

    @Override
    public List<ShopListBean.DataBean.ListBean> getData() {
        return activityList;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void release(){
        baseRefreshLayout = null;
    }

    private void cancelSell(final Context context, int id){
        SellCancelRequestBean requestBean = new SellCancelRequestBean();
        requestBean.setId(id+"");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                if("200".equals(code)){
                    T.s(context, "操作成功");
                    baseRefreshLayout.refresh();
                }else{
                    T.s(context, "操作失败 "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(context, "操作失败 "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.dealAccountCancel), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public void cancelFavor(final Context context, int id){
        AccountOperationFavorRequestBean requestBean = new AccountOperationFavorRequestBean();
        requestBean.setId(id+"");
        requestBean.setOperation("2");//1收藏，2取消
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                if("200".equals(code)){
                    T.s(context, "已取消收藏");
                    baseRefreshLayout.refresh();
                }else{
                    T.s(context, "操作失败 "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(context, "操作失败 "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.dealAccountOperationCollect), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        RoundedImageView ivIcon;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_server)
        TextView tvServer;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_money_top)
        TextView tvMoneyTop;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.tv_cancel_favor)
        TextView tvCancelFavor;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_money_bottom)
        TextView tvMoneyBottom;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
