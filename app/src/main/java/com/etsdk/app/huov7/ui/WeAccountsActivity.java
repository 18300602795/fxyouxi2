package com.etsdk.app.huov7.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.util.ImgUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/11/15.
 */

public class WeAccountsActivity extends ImmerseActivity  {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.accept_btn)
    TextView accept_btn;
    @BindView(R.id.save_btn)
    TextView save_btn;
    @BindView(R.id.code_et)
    EditText code_et;
    Bitmap qrCode;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weaccounts);
        ButterKnife.bind(this);
        tvTitleName.setText("关注微信公众号");
        Resources r = this.getResources();
        qrCode = BitmapFactory.decodeResource(r, R.mipmap.qrcode);
    }

    @OnClick({R.id.iv_titleLeft, R.id.save_btn, R.id.accept_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.save_btn:
                autoObtainStoragePermission();
                break;
            case R.id.accept_btn:
                break;
        }
    }
    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            if (ImgUtil.saveImageToGallery(this, qrCode)){
                Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ImgUtil.saveImageToGallery(this, qrCode)){
                        Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请允许打操作SDCard！！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, WeAccountsActivity.class);
        context.startActivity(starter);
    }
}
