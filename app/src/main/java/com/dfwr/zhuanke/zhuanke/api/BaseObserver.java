package com.dfwr.zhuanke.zhuanke.api;

import android.accounts.NetworkErrorException;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.application.MyApplication;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.BlackNameActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.LoginActivity;
import com.orhanobut.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huang on 2018/4/16.
 */

public  abstract class BaseObserver<T> implements Observer<ApiResponse<T>> {

    protected Context mContext;

    public BaseObserver(Context cxt) {
        this.mContext = cxt;
    }

    public BaseObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ApiResponse<T> tApiResponse) {
        if (tApiResponse.getCode() == 200) {
                onSuccees(tApiResponse);
        } else {
            Logger.d("success，但是code配置错误");
            onCodeError(tApiResponse);
        }
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        Logger.d("异常信息是: "+result.toString());
        if (e instanceof ConnectException
                || e instanceof NetworkErrorException
                || e instanceof UnknownHostException) {
            onFailure("网络连接异常，请检查网络",false);
            Logger.d("SocketTimeoutException");
        }else if((e instanceof SocketTimeoutException) || (e instanceof TimeoutException)){
            onFailure("连接超时，请刷新",false);
        }else {
            ToastUtils.showShort("网络连接异常，请检查网络连接");
            onFailure("网络连接异常，请检查网络连接",false);
        }
    }


    @Override
    public void onComplete() {
    }










    // 返回成功了,但是code错误
    protected void onCodeError(ApiResponse<T> t){
        if (t.getCode() == -100) {
            startError(101, t.getMessage());
        }else if(t.getCode() == 110){ //拉入黑名单
            onFailure("账户异常!!!",false);
            showErrorDialog();
        }else if(t.getCode() == 101){  //用户不存在
            onFailure("账户异常!!!",false);
            showErrorDialog();
        } else if(t.getCode() == 500){
            onFailure("code500服务器错误，请稍后再试。",false);
        } else{
            ToastUtils.showShort(t.getMessage());
            onFailure(t.getMessage()+"",false);
        }
//        onFailure(t.getMessage()+"",false);
    }



    /**
     * 跳转登录页面
     */
    private void showErrorDialog() {
        Intent intent = new Intent(MyApplication.getContext(), BlackNameActivity.class);
        ActivityManager manager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        if (!".ui.login.LoginActivity".equals(shortClassName)) {
            intent.setClass(MyApplication.getContext(), BlackNameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getContext().startActivity(intent);
        }
    }

//    //展示错误弹窗
//    private  void showErrorDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
//        builder.setMessage("您的账号被封！！，请联系客服！");
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//
//    }


    //返回成功
    protected abstract void onSuccees(ApiResponse<T> t);

    //返回失败
    protected abstract void onFailure(String errorInfo, boolean isNetWorkError);



    /**
     * 开启登录页面
     */
    private void startError(int code, String msg) {
        if(code == 101){
            reStartLogin();
        }
//        Logger.d("需要重新登录");
//        Intent intent = new Intent("com.microdreams.timeprints.error");
//        intent.putExtra("error_code", code);
//        intent.putExtra("msg", msg);
//        MyApplication.getContext().sendBroadcast(intent);
    }


    /**
     * 跳转登录页面
     */
    private void reStartLogin() {
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        ActivityManager manager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        if (!".ui.login.LoginActivity".equals(shortClassName)) {
            intent.setClass(MyApplication.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isReLogin",true);
            intent.putExtra("isNoBack",true);
            MyApplication.getContext().startActivity(intent);
        }
    }

}
