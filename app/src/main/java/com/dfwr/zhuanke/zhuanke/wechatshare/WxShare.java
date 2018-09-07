package com.dfwr.zhuanke.zhuanke.wechatshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WxShare {
    private static GetResultListener localOnShareListener;
    private static SendMessageToWX.Req localReq;

    private static boolean isAppInstalled(Context paramContext, String paramString) {
        List localList = paramContext.getPackageManager().getInstalledPackages(0);
        ArrayList localArrayList = new ArrayList();
        if (localList != null) {
            for (int i = 0; i < localList.size(); i++) {
                localArrayList.add(((PackageInfo) localList.get(i)).packageName);
            }
        }
        return localArrayList.contains(paramString);
    }

    public static void sendReq(WeakReference<Activity> weakReference, GetResultListener onShareLitener, SendMessageToWX.Req req, final String appId, final String packageName) {
        Logger.e("sendReq----------");
        localOnShareListener = onShareLitener;
        try {
            final Activity localActivity = (Activity) weakReference.get();
            localReq = req;
            if (localActivity != null && localReq != null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        try {
                            Bundle localBundle = new Bundle();
                            WxShare.localReq.toBundle(localBundle);
                            Intent localIntent = new Intent();
                            localIntent.setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXEntryActivity");
                            localIntent.putExtras(localBundle);
                            localIntent.putExtra("_mmessage_sdkVersion", Build.SCAN_QRCODE_AUTH_SUPPORTED_SDK_INT);
                            localIntent.putExtra("_mmessage_appPackage", packageName);
                            localIntent.putExtra("_mmessage_content", "weixin://sendreq?appid=" + appId);
                            localIntent.putExtra("_mmessage_checksum", (byte[]) Class.forName("com.tencent.mm.opensdk.channel.a.b").getDeclaredMethod("a", new Class[]{String.class, Integer.TYPE, String.class}).invoke(null, new Object[]{"weixin://sendreq?appid=" + appId, Integer.valueOf(Build.SCAN_QRCODE_AUTH_SUPPORTED_SDK_INT), localActivity.getPackageName()}));
                            localIntent.addFlags(268435456).addFlags(134217728);
                            if (localActivity != null) {
                                localActivity.startActivity(localIntent);
                            }
                            WxShare.localOnShareListener.onSuccess("success");
                        } catch (Exception localException) {
                            Logger.e("22222");
                            Logger.e(Log.getStackTraceString(localException));
                            WxShare.localOnShareListener.onError();
                        }
                    }
                }, 0);
            }
        } catch (Exception localException) {
            Logger.e(Log.getStackTraceString(localException));
            WxShare.localOnShareListener.onError();
        }
    }

    public static void sendReq(WeakReference<Activity> weakReference, SendMessageToWX.Req req, final String appId, final String packageName) {
        Logger.e("sendReq----------");
        try {
            final Activity localActivity = (Activity) weakReference.get();
            localReq = req;
            if (localActivity != null && localReq != null) {
                Logger.e("开始分享");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        try {
                            Bundle localBundle = new Bundle();
                            WxShare.localReq.toBundle(localBundle);
                            Intent localIntent = new Intent();
                            localIntent.setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXEntryActivity");
                            localIntent.putExtras(localBundle);
                            localIntent.putExtra("_mmessage_sdkVersion", Build.SCAN_QRCODE_AUTH_SUPPORTED_SDK_INT);
                            localIntent.putExtra("_mmessage_appPackage", packageName);
                            localIntent.putExtra("_mmessage_content", "weixin://sendreq?appid=" + appId);
                            localIntent.putExtra("_mmessage_checksum", (byte[]) Class.forName("com.tencent.mm.opensdk.channel.a.b").getDeclaredMethod("a", new Class[]{String.class, Integer.TYPE, String.class}).invoke(null, new Object[]{"weixin://sendreq?appid=" + appId, Integer.valueOf(Build.SCAN_QRCODE_AUTH_SUPPORTED_SDK_INT), localActivity.getPackageName()}));
                            localIntent.addFlags(268435456).addFlags(134217728);
                            if (localActivity != null) {
                                localActivity.startActivity(localIntent);
                            }
                        } catch (Exception localException) {
                            Logger.e(Log.getStackTraceString(localException));
                        }
                    }
                }, 0);
            }
        } catch (Exception localException) {
            Logger.e(Log.getStackTraceString(localException));
        }
    }
}