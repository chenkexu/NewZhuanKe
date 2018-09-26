package com.dfwr.zhuanke.zhuanke.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.dfwr.zhuanke.zhuanke.bean.UserBean;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by ckx on 2018/7/12.
 */

public class MyApplication extends Application {


    private static Context applicationContext;
    private UserBean loginBean;
    public static IWXAPI mWxApi;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        initBuglyCrash();

        initOkGo();
        MobSDK.init(this,Systems.mob_Appkey,Systems.mob_App_Secret);
        applicationContext = MyApplication.this.getApplicationContext();
        //工具类库
        Utils.init(this);
        SharedPreferencesTool.getInstance().init(this);
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .tag("okgo")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//
//        });
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//
//        });

//        registerToWX();

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    private void initBuglyCrash() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(),Systems.bugly_id, false);
    }


    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.WARNING);                            //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }


    public static Context getContext() {
        return applicationContext;
    }


    /**
     * 注销用户
     */
    public void logOutLogin(Context context) {
        loginBean = null;
        SharedPreferencesUtil.removeData(context, Systems.USER_INFO);
    }

    /**
     * 保存用户信息
     *
     * @param loginBean
     */
    public void saveLoginBean(UserBean loginBean) {
        this.loginBean = loginBean;
        SharedPreferencesUtil.save(Systems.USER_INFO, loginBean);
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isMemberLogin() {
        if (getLoginBean() != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户详细信息
     *
     * @return
     */
    public UserBean getLoginBean() {
        if (loginBean != null) {
            return loginBean;
        }
        loginBean = (UserBean) SharedPreferencesUtil.get(Systems.USER_INFO);
        return loginBean;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
