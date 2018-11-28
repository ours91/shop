package com.shop.app.shopactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.shop.app.shopapplication.R;
import com.shop.app.utils.BaseUtils;
import com.shop.app.utils.MyLog;
import com.shop.app.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BaseUtils.initHeaderBar(this);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
    }
}
