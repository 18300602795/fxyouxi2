package com.etsdk.app.huov7.shop.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.shop.model.ResultBean;
import com.etsdk.app.huov7.shop.model.SelectGameEvent;
import com.etsdk.app.huov7.shop.model.SelectLittleAccountEvent;
import com.etsdk.app.huov7.shop.model.ShopListRefreshEvent;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.SelectPhotoCropActivity;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.http.LoadWaitDialogUtil;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.T;
import com.liang530.photopicker.ShowPicVPActivity;
import com.liang530.photopicker.beans.SelectPhotoEvent;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2017/6/09
 * 卖号fragment
 */

public class SellFragment extends AutoLazyFragment {
    public final static int MODE_PUBLISH = 0;//发布
    public final static int MODE_EDIT = 1;//编辑

    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_little_account)
    TextView tvLittleAccount;
    @BindView(R.id.et_server)
    EditText etServer;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_service_center)
    TextView tvServiceCenter;
    @BindView(R.id.imagebox)
    ZzImageBox imagebox;

    private int mode = MODE_PUBLISH;
    private int sellId;
    private String title;
    private String gameId;
    private String gameName;
    private String memId;
    private String accountNickName;
    private String serverName;
    private float price;
    private String desc;
    private String mg_mem_id;
    private ArrayList<String> imgs;

    public static SellFragment newInstance(int mode, String mg_mem_id, int sellId, String gameId, String gameName, String memId, String accountNickName, String server,
                                           float price, String title, String desc, ArrayList<String> imgs) {
        SellFragment newFragment = new SellFragment();
        if (mode == MODE_EDIT) {
            Bundle bundle = new Bundle();
            bundle.putInt("mode", mode);
            bundle.putInt("sellId", sellId);
            bundle.putString("gameId", gameId);
            bundle.putString("mg_mem_id", mg_mem_id);
            bundle.putString("gameName", gameName);
            bundle.putString("memId", memId);
            bundle.putString("accountNickName", accountNickName);
            bundle.putString("server", server);
            bundle.putFloat("price", price);
            bundle.putString("title", title);
            bundle.putString("desc", desc);
            bundle.putStringArrayList("imgs", imgs);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sell);
        setupUI();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
        if (imagebox != null) {
            imagebox = null;
        }
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mode = arguments.getInt("mode", 0);
            sellId = arguments.getInt("sellId", 0);
            title = arguments.getString("title");
            gameId = arguments.getString("gameId");
            mg_mem_id = arguments.getString("mg_mem_id");
            gameName = arguments.getString("gameName");
            memId = arguments.getString("memId");
            accountNickName = arguments.getString("accountNickName");
            serverName = arguments.getString("server");
            price = arguments.getFloat("price");
            desc = arguments.getString("desc");
            imgs = arguments.getStringArrayList("imgs");
        }
        //添加截图
        imagebox.setOnImageClickListener(new ZzImageBox.OnImageClickListener() {
            @Override
            public void onImageClick(int position, String filePath, ImageView iv) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(filePath);
                ShowPicVPActivity.start(mContext, list, 0, true);
            }

            @Override
            public void onDeleteClick(int position, String filePath) {
                imagebox.removeImage(position);
            }

            @Override
            public void onAddClick() {
                if (imagebox.getAllImages().size() >= 9) {
                    T.s(mContext, "最多添加9张截图");
                    return;
                }
                SelectPhotoCropActivity.startAndCrop(mContext, "screenshort", R.color.text_black,
                        1080, 1920, false, SelectPhotoCropActivity.getDefaultOption());
//                SelectPhotoCropActivity.start(mContext, "screenshort", R.color.text_black);
            }
        });
        //描述
        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    etDesc.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                } else {
                    etDesc.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mode == MODE_PUBLISH) {

        } else if (mode == MODE_EDIT) {
            downloadImg(imgs);
            tvGameName.setText(gameName);
            tvLittleAccount.setText(accountNickName);
            tvGameName.setClickable(false);
            tvLittleAccount.setClickable(false);
            etServer.setText(serverName);
            etPrice.setText((int) price + "");
            etTitle.setText(title);
            etDesc.setText(desc);
            tvCommit.setText("提 交");
        }
        //修改时加载截图测试数据
//        List<String> imgs = new ArrayList<>();
//        imgs.add("http://171static.1tsdk.com/upload/20170705/595cc1660770d.jpeg");
//        imgs.add("http://171static.1tsdk.com/upload/20170705/595cc16622e1b.jpeg");
//        imgs.add("http://171static.1tsdk.com/upload/20170705/595cc1663ce40.jpeg");
//        downloadImg(imgs);
    }

    private void downloadImg(final List<String> imgs) {
        if (imgs == null || imgs.size() == 0) {
            return;
        }
        final LoadWaitDialogUtil loadingDialogView = new LoadWaitDialogUtil(true, "正在加载截图...");
        loadingDialogView.show(mContext);
        final int[] compliteCount = {0};
        for (String url : imgs) {
            FileDownloader.getImpl().create(url).setListener(new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                }

                @Override
                protected void completed(BaseDownloadTask task) {
                    compliteCount[0] = compliteCount[0] + 1;
                    imagebox.addImage(task.getPath());
                    if (compliteCount[0] >= imgs.size()) {
                        if (loadingDialogView != null) {
                            loadingDialogView.dismiss();
                        }
                        T.s(mContext, "截图加载完毕");
                    }
                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    compliteCount[0] = compliteCount[0] + 1;
                    if (compliteCount[0] >= imgs.size()) {
                        if (loadingDialogView != null) {
                            loadingDialogView.dismiss();
                        }
                        T.s(mContext, "截图加载完毕");
                    }
                }

                @Override
                protected void warn(BaseDownloadTask task) {

                }
            }).start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectPhotoEvent(SelectPhotoEvent selectPhotoEvent) {
        if ("screenshort".equals(selectPhotoEvent.flag) && selectPhotoEvent.imagePath != null && SelectPhotoEvent.STATUS_OK == selectPhotoEvent.status) {
            imagebox.addImage(selectPhotoEvent.imagePath);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectGameEvent(SelectGameEvent selectGameEvent) {
        tvGameName.setText(selectGameEvent.getGameName());
        gameId = selectGameEvent.getGameId();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectAccountEvent(SelectLittleAccountEvent selectLittleAccountEvent) {
        tvLittleAccount.setText(selectLittleAccountEvent.getAccont());
        memId = selectLittleAccountEvent.getMenId();
    }


    /**
     * 短信验证码
     */
    private void getUserInfoBeforePublish() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    if (TextUtils.isEmpty(data.getMobile())) {
                        T.s(getContext(), "交易前前请先绑定手机");
                        AccountManageActivity.start(getContext());
                        return;
                    }
                    checkInput(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void checkInput(UserInfoResultBean data) {
        if (TextUtils.isEmpty(gameId)) {
            T.s(mContext, "请选择游戏");
            return;
        }
        if (TextUtils.isEmpty(memId)) {
            T.s(mContext, "请选择要交易的账号");
            return;
        }
        serverName = etServer.getText().toString().trim();
        if (TextUtils.isEmpty(serverName)) {
            T.s(mContext, "请选填写账号所在的区服信息");
            return;
        }
        price = Float.parseFloat(etPrice.getText().toString().trim());
        if (price < 6) {
            T.s(mContext, "请选填写至少6元的价格");
            return;
        }
        title = etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            T.s(mContext, "请选填写标题");
            return;
        }
        desc = etDesc.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            T.s(mContext, "请选填写账号的描述");
            return;
        }
        if (imagebox.getAllImages().size() < 3) {
            T.s(mContext, "至少上传3张截图");
            return;
        }
        float income = price*0.03f<=3?(price-3f):(price*0.97f);
        new SellHintDialogUtil().show(getContext(), income, data.getMobile(), mode, new SellHintDialogUtil.Listener() {
            @Override
            public void ok(String code) {
                if (mode == MODE_PUBLISH) {
                    publish(code);
                } else if (mode == MODE_EDIT) {
                    edit(code);
                }
            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 编辑
     *
     * @param code
     */
    private void edit(final String code) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在生成截图");
        progressDialog.setProgress(0);
        progressDialog.setMax(imagebox.getAllImages().size());
        progressDialog.show();
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.dealAccountEdit);
        httpParams.put("id", sellId);
        httpParams.put("mg_mem_id", mg_mem_id);
        httpParams.put("mem_id", memId);
        httpParams.put("servername", serverName);
        httpParams.put("price", price + "");
        httpParams.put("title", title);
        httpParams.put("description", desc);
        httpParams.put("smscode", code);
        int i = 1;
        for (String path : imagebox.getAllImages()) {//耗时操作
            httpParams.put("image[]", new File(path));
            progressDialog.setProgress(i);
            i++;
        }
        progressDialog.dismiss();
        NetRequest.request(this).setParams(httpParams).showDialog(true).post(AppApi.getUrl(AppApi.dealAccountEdit), new HttpJsonCallBackDialog<ResultBean>() {
            @Override
            public void onDataSuccess(ResultBean data) {
                if (data.getCode() == 200) {
                    T.s(mContext, "发布成功，请耐心等待审核通过");
                    EventBus.getDefault().post(new ShopListRefreshEvent());
                } else {
                    T.s(mContext, "提交失败 " + data.getMsg());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                T.s(mContext, "提交失败 " + strMsg);
            }
        });
    }

    /**
     * 发布
     */
    private void publish(final String code) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在生成截图");
        progressDialog.setProgress(0);
        progressDialog.setMax(imagebox.getAllImages().size());
        progressDialog.show();
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.dealAccountAdd);
        httpParams.put("gameid", gameId);
        httpParams.put("mem_id", memId);
        httpParams.put("servername", serverName);
        httpParams.put("price", price + "");
        httpParams.put("title", title);
        httpParams.put("description", desc);
        httpParams.put("smscode", code);
        int i = 1;
        for (String path : imagebox.getAllImages()) {//耗时操作
            httpParams.put("image[]", new File(path));
            progressDialog.setProgress(i);
            i++;
        }
        progressDialog.dismiss();
        NetRequest.request(this).setParams(httpParams).showDialog(true).post(AppApi.getUrl(AppApi.dealAccountAdd), new HttpJsonCallBackDialog<ResultBean>() {
            @Override
            public void onDataSuccess(ResultBean data) {
                if (data.getCode() == 200) {
                    T.s(mContext, "发布成功，请耐心等待审核通过");
                    clearInput();
                    EventBus.getDefault().post(new ShopListRefreshEvent());
                } else {
                    T.s(mContext, "提交失败 " + data.getMsg());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                T.s(mContext, "提交失败 " + strMsg);
            }
        });
    }

    private void clearInput() {
        tvGameName.setText("请选择游戏");
        tvLittleAccount.setText("请选择该游戏中的小号");
        etServer.setText("");
        etPrice.setText("");
        etTitle.setText("");
        etDesc.setText("");
        imagebox.removeAllImages();
    }

    @OnClick({R.id.tv_game_name, R.id.tv_little_account, R.id.tv_commit, R.id.tv_service_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_game_name:
                MyGameListActivity.start(mContext);
                break;
            case R.id.tv_little_account:
                MyAccountListActivity.start(mContext, gameId);
                break;
            case R.id.tv_commit:
                getUserInfoBeforePublish();
                break;
            case R.id.tv_service_center:
                ServiceActivity.start(mContext);
                break;
        }
    }

}
