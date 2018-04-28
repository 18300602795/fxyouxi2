package com.etsdk.app.huov7.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.util.EncodingUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/11/15.
 */

public class InviteActivity extends ImmerseActivity {
    private String url;
    private String text;
    @BindView(R.id.qr_iv)
    ImageView qr_iv;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    private UserInfoResultBean resultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        tvTitleName.setText("邀请好友");
        text = "纷享游戏_花最少的钱玩最爽的游戏";
        getUserInfoData();
    }

    private void setQr(String str) {
        Bitmap qrCode = EncodingUtils.createQRCode(str, 200, 200,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        qr_iv.setImageBitmap(qrCode);
    }

    @OnClick({R.id.iv_titleLeft, R.id.wechat_ll, R.id.moments_ll, R.id.weibo_ll, R.id.qq_ll, R.id.qzone_ll, R.id.copy_ll})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_titleLeft) {
            finish();
        }
        if (resultBean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.wechat_ll:
                share(Wechat.NAME);
                break;
            case R.id.moments_ll:
                share(WechatMoments.NAME);
                break;
            case R.id.weibo_ll:
                share(SinaWeibo.NAME);
                break;
            case R.id.qq_ll:
                share(QQ.NAME);
                break;
            case R.id.qzone_ll:
                share(QZone.NAME);
                break;
            case R.id.copy_ll:
                ClipboardManager copy = (ClipboardManager) InviteActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(url);
                Toast.makeText(this, "链接复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void share(String name) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl("http://gm.idielian.com/upload/20171227/5a4376ac19412.png");
        oks.setTitleUrl(url);
        oks.setUrl(url);
        oks.setSiteUrl(url);
        oks.setText(text);
        oks.setTitle("纷享游戏");
        oks.setPlatform(name);
        oks.show(this);
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(InviteActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    resultBean = data;
                    share();
                }
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void share() {
        url = "http://tui.idielian.com/agent.php/Front/Appdown/prelink/name/";
        if (resultBean.getName().equals("官网")) {
            url = "http://fxyouxi.com/index.html#/home";
        } else {
            String name_code = Base64.encodeToString(resultBean.getName().getBytes(), Base64.DEFAULT);
            url = url + name_code;
        }
        L.i("333", "downlink：" + url);
        setQr(url);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, InviteActivity.class);
        context.startActivity(starter);
    }
}
