package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AboutUsInfo;
import com.etsdk.app.huov7.util.AileConstants;
import com.game.sdk.util.GsonUtil;
import com.google.gson.JsonSyntaxException;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.SP;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends ImmerseActivity {
    private static final String SP_ABOUT_US_INFO = "AboutUsInfo";
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_copyright_url)
    TextView tvCopyRightUrl;
    @BindView(R.id.ll_us_weibo)
    LinearLayout llUsWeibo;
    @BindView(R.id.ll_us_website)
    TextView llUsWebsite;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.ll_us_qq)
    LinearLayout llUsQq;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.activity_about_us)
    LinearLayout activityAboutUs;
    @BindView(R.id.tv_curVersion)
    TextView tvCurVersion;
    private AboutUsInfo aboutUsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("关于我们");
        tvCopyRightUrl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvCopyRightUrl.getPaint().setAntiAlias(true);//抗锯齿
        tvCurVersion.setText("版本：" + BaseAppUtil.getAppVersionName());
        String aboutUsInfoJson = SP.getString(SP_ABOUT_US_INFO);
        if (!TextUtils.isEmpty(aboutUsInfoJson)) {
            try {
                aboutUsInfo = GsonUtil.getGson().fromJson(aboutUsInfoJson, AboutUsInfo.class);
                if(aboutUsInfo!=null){
                    updateAboutUsInfo(aboutUsInfo);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
//        if (AileConstants.helpInfo != null && AileConstants.helpInfo.getQq() != null) {
//            tvQq.setText(AileConstants.helpInfo.getQq()[0]);
//            tvPhone.setText("COPYRIGHT 2013-2016火速游戏 \\n " + AileConstants.helpInfo.getTel());
//        }
        getAboutUsInfo();
    }

    private void updateAboutUsInfo(AboutUsInfo data) {
        aboutUsInfo = data;
        SP.putString(SP_ABOUT_US_INFO, GsonUtil.getGson().toJson(data)).commit();
        if (data.getData() != null && data.getData().getQq() != null && data.getData().getQq().size() > 0) {
            tvQq.setText(data.getData().getQq().get(0).getNumber());
            tvQq.setTag(data.getData().getQq().get(0).getNumber());
        }
        if (data.getData() != null&&data.getData().getTel()!=null&&data.getData().getTel().size()>0) {
            tvPhone.setText("COPYRIGHT 2013-2016火速游戏 \n " + data.getData().getTel().get(0).getNumber());
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.ll_us_weibo, R.id.ll_us_website, R.id.ll_us_qq, R.id.tv_copyright_url})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.ll_us_weibo:
                if (aboutUsInfo != null && aboutUsInfo.getData() != null) {
                    WebViewActivity.start(mContext, "微博", aboutUsInfo.getData().getWburl());
                } else {
                    WebViewActivity.start(mContext, "微博", "http://weibo.com/");
                }
                break;
            case R.id.ll_us_website:
                if (aboutUsInfo != null && aboutUsInfo.getData() != null) {
                    WebViewActivity.start(mContext, "官网", aboutUsInfo.getData().getWebsite());
                } else {
                    WebViewActivity.start(mContext, "官网", "http://www.1tsdk.com/");
                }
                break;
            case R.id.ll_us_qq:
                if (tvQq.getTag() != null) {
                    openQq(tvQq.getTag().toString());
                }
                break;
            case R.id.tv_copyright_url:
                WebViewActivity.start(mContext, "用户协议与版权声明", AppApi.getUrl(AppApi.agreementrightUrl));
                break;
            default:
                break;
        }
    }

    private void getAboutUsInfo() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.aboutus);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.aboutus), new HttpJsonCallBackDialog<AboutUsInfo>() {
            @Override
            public void onDataSuccess(AboutUsInfo data) {
                if (data != null) {
                    updateAboutUsInfo(data);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutUsActivity.class);
        context.startActivity(starter);
    }

    public  void openQq(final String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(mContext, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
        }
    }
}
