package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AddressInfo;
import com.etsdk.app.huov7.model.AddressList;
import com.etsdk.app.huov7.model.UserAddressInfo;
import com.etsdk.app.huov7.provider.AddressInfoViewProvider;
import com.game.sdk.log.L;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2017/2/7.
 * 选择地址对话框
 */

public class SelectAddressDialog extends Dialog {
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tab_selectAddress)
    TabLayout tabSelectAddress;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Map<String, List<AddressInfo>> addressMap;
    private Map<Integer,AddressInfo> levelAddressMap;//层级选中的id Map
    private Items addressItems = new Items();
    private String currentId = "0";
    private MultiTypeAdapter multiTypeAdapter;
    UserAddressInfo userAddressInfo;
    private SelectAddressOkListener selectAddressOkListener;
    public SelectAddressDialog(Context context,SelectAddressOkListener selectAddressOkListener) {
        super(context, R.style.dialog_bg_style);
        this.selectAddressOkListener=selectAddressOkListener;
        initUI();
    }

    public SelectAddressDialog(Context context, int themeResId) {
        super(context, themeResId);
        initUI();
    }

    protected SelectAddressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.dialog_select_address);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        window.getAttributes().width = BaseAppUtil.getDeviceWidth(getContext());
        window.setGravity(Gravity.BOTTOM);
        ButterKnife.bind(this);
        addressMap = new HashMap();
        levelAddressMap=new HashMap<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        multiTypeAdapter = new MultiTypeAdapter(addressItems);
        multiTypeAdapter.register(AddressInfo.class, new AddressInfoViewProvider(selectAddressInfoListener,levelAddressMap));
        recyclerview.setAdapter(multiTypeAdapter);
        //设置tab
        tabSelectAddress.addTab(tabSelectAddress.newTab().setText("请选择"));
        tabSelectAddress.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                0 null
//                1  sheng
                AddressInfo addressInfo = levelAddressMap.get(tab.getPosition());
//                if(addressInfo!=null){
//                    getAddressList("0",tab.getPosition()+1);
//                }else{
//                    getAddressList(addressInfo.getId(),tab.getPosition()+1);
//                }
                if(tab.getPosition()==0){// 省
                    getAddressList("0", tab.getPosition()+1);
                    currentId="0";
                }else{
                    currentId=addressInfo.getId();
                    if (addressMap.get(addressInfo.getId()) == null && addressInfo.getLevel() < 4) {//缓存中没有找到才联网获取
                        getAddressList(addressInfo.getId(), addressInfo.getLevel() + 1);
                    } else {
                        updateUI();
                    }
                }
                L.e("hongliang","选择了tab:"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("hongliang","点击了tab");
            }
        });
    }

    private SelectAddressInfoListener selectAddressInfoListener = new SelectAddressInfoListener() {
        @Override
        public void selectAddressInfo(AddressInfo addressInfo) {
            currentId = addressInfo.getId();
            levelAddressMap.put(addressInfo.getLevel(),addressInfo);
            //设置tab
            TabLayout.Tab tabAt = tabSelectAddress.getTabAt(addressInfo.getLevel() - 1);
            if(tabAt!=null){
                if(!tabAt.getText().toString().equals(addressInfo.getName())){
                    tabAt.setText(addressInfo.getName());
                }
            }else{
                tabSelectAddress.addTab(tabSelectAddress.newTab().setText(addressInfo.getName()),addressInfo.getLevel()-1,false);
            }
            multiTypeAdapter.notifyDataSetChanged();//更新当前显示选中
            //移除多余的
            while (tabSelectAddress.getTabCount()>addressInfo.getLevel()){
                tabSelectAddress.removeTabAt(tabSelectAddress.getTabCount()-1);
            }
            //添加一个请选择
            tabSelectAddress.addTab(tabSelectAddress.newTab().setText("请选择"),true);
        }
    };


    public void show(UserAddressInfo userAddressInfo) {
        super.show();
        levelAddressMap.clear();
        addressMap.clear();
        addressItems.clear();
        multiTypeAdapter.notifyDataSetChanged();
        getAddressList("0", 1);
    }

    private void getAddressList(final String pid, int level) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addressListApi);
        httpParams.put("pid", pid);
        httpParams.put("level", level);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.addressListApi), new HttpJsonCallBackDialog<AddressList>() {
            @Override
            public void onDataSuccess(AddressList data) {
                if (data == null || data.getData() == null) {//数据为null没有下一级了
                    addressMap.put(pid, null);
                } else {
                    addressMap.put(pid, data.getData().getList());
                }
                updateUI();
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
//                super.onJsonSuccess(code, msg, data);
                if(code==400) {
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {
        List<AddressInfo> addressInfos = addressMap.get(currentId);

        if (addressInfos == null||addressInfos.size()==0) {//没有下级了，选择结束
            //把这一级后面的数据清除，可能是之前选的别有下级的数据
            int level=4;
            for(int i=0;i<=4;i++){
                AddressInfo addressInfo = levelAddressMap.get(i);
                if(addressInfo!=null&&currentId==addressInfo.getId()){
                    level=addressInfo.getLevel();
                }
                if(addressInfo!=null&&addressInfo.getLevel()>level){
                    levelAddressMap.remove(addressInfo.getLevel());
                }
            }
            selectAddressOkListener.selectAddressInfo(levelAddressMap);
            dismiss();
        } else { //显示下级
            addressItems.clear();
            addressItems.addAll(addressInfos);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_close)
    public void onClick() {
        dismiss();
    }

    /**
     * adapter中选择地址监听
     */
    public interface SelectAddressInfoListener {
        void selectAddressInfo(AddressInfo addressInfo);
    }

    /**
     * 地址全部选中完毕监听
     */
    public interface  SelectAddressOkListener{
        void selectAddressInfo(Map<Integer,AddressInfo>  levelAddressMap);
    }

}
