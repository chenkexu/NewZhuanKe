package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.dfwr.zhuanke.zhuanke.MainActivity;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BasePermissionActivity;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;


/**
 * 欢迎界面、快闪页面
 */
public class GuideActivity extends BasePermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isfirstenter = SharedPreferencesTool.getInstance().getBooleanValue(SharedPreferencesTool.ISFIRSTENTER, true);
        if (isfirstenter) {
            //进入引导页面
            startActivity(new Intent(GuideActivity.this, LaunchActivity.class));
            finish();
        } else {
            //取消标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_login);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestPermission(new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS

                    });
                }
            }, 20);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean loginout = SharedPreferencesTool.getInstance().getBooleanValue(SharedPreferencesTool.USER_LOGOUT, true);
                    if (loginout) {   //已经退出,进入登录界面，或者找不到该变量（默认为true）
                        startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                        finish();
                    } else { //false进入主页面
                        startActivity(new Intent(GuideActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }, 2000);

        }

    }
}
