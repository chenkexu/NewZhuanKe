package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.MainActivity;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.api.ApiManager;
import com.dfwr.zhuanke.zhuanke.api.BaseObserver;
import com.dfwr.zhuanke.zhuanke.api.param.ParamsUtil;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.base.BasePresenter;
import com.dfwr.zhuanke.zhuanke.bean.UserBean;
import com.dfwr.zhuanke.zhuanke.bean.WechatBean;
import com.dfwr.zhuanke.zhuanke.util.AppManager;
import com.dfwr.zhuanke.zhuanke.util.ButtonUtils;
import com.dfwr.zhuanke.zhuanke.util.GsonUtils;
import com.dfwr.zhuanke.zhuanke.util.RxUtil;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.meituan.android.walle.WalleChannelReader;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.activity_guide)
    RelativeLayout activityGuide;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvVersion.setText("版本"+AppUtils.getAppVersionName());
        SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.USER_LOGOUT, true);
        SharedPreferencesTool.getInstance().setObject(null, SharedPreferencesTool.user);
    }












    @Override
    protected BasePresenter createPresent() {
        return new BasePresenter() {
            @Override
            public void fecth() {

            }
        };
    }


    private void loginWeChat() {
        showDefaultLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("AppId", "wx055b79a81a71b0cb");
        hashMap.put("AppSecret", "16bbef65347c71433ace880c00790b87");
        hashMap.put("BypassApproval", "false");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, hashMap);
        final Platform wechat = ShareSDK.getPlatform(Wechat.NAME);

        if (wechat.isClientValid()) {
            //客户端可用
        }

        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                /*platform.getDb().exportData()获取用户数据*/
                Log.d("ShareSDK", "onComplete ---->  登录成功" + platform.getDb().exportData());
                String json = platform.getDb().exportData();

                Logger.d("微信返回的个人信息: "+json);
                WechatBean wechatBean = GsonUtils.parseJsonToBean(json, WechatBean.class);
                HashMap<String, Object> map = ParamsUtil.getMap();

                map.put("wxId", wechatBean.getOpenid());

//                String stringToUnicode = ConvertCodeUtil.stringToUnicode(platform.getDb().getUserName());
                map.put("wxName", platform.getDb().getUserName() == null ? "" : platform.getDb().getUserName());
//                map.put("sex", platform.getDb().getUserGender() == null ? "m" : platform.getDb().getUserGender());


                // TODO: 2018/8/27 性别传男女
                if (platform.getDb().getUserGender()==null) {
                    map.put("sex","男");
                }else{
                    Logger.d("获取到的性别是: "+platform.getDb().getUserGender());
                    if (platform.getDb().getUserGender().equals("m")) {
                        map.put("sex", "男");
                    }else{
                        map.put("sex", "女");
                    }
                }


                map.put("imgId", platform.getDb().getUserIcon() == null ? "" : platform.getDb().getUserIcon());
                map.put("unionid", wechatBean.getUnionid());

                String channel = WalleChannelReader.getChannel(LoginActivity.this);
                Logger.d("channel = " + channel);
                if (channel == null) {
                    channel = "";
                    map.put("teacherName", channel);
                }else{
                    map.put("teacherName", "lexiangzhuan_"+channel);
                }

                ApiManager.getInstence().getApiService().login(ParamsUtil.getParams(map))
                        .compose(RxUtil.<ApiResponse<UserBean>>rxSchedulerHelper())
                        .subscribe(new BaseObserver<UserBean>() {
                            @Override
                            protected void onSuccees(ApiResponse<UserBean> t) {
                                UserBean result = t.getResult();
                                // TODO: 2018/8/15 判断返回的user的ID是否为空？
                                SharedPreferencesTool.getInstance().setObject(result, SharedPreferencesTool.user);
                                // 这里授权成功跳转到程序主界面了
                                SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.USER_LOGOUT, false);
//                                hideDefaultLoading();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            protected void onFailure(String errorInfo, boolean isNetWorkError) {
                                ToastUtils.showShort(errorInfo);
                                hideDefaultLoading();
                            }
                        });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort("登录失败");
                hideDefaultLoading();
                Log.d("ShareSDK", "onError ---->  登录失败" + throwable.toString());
                Log.d("ShareSDK", "onError ---->  登录失败" + throwable.getStackTrace().toString());
                Log.d("ShareSDK", "onError ---->  登录失败" + throwable.getMessage());

            }

            @Override
            public void onCancel(Platform platform, int i) {
                hideDefaultLoading();
                ToastUtils.showShort("登录失败");
                Log.d("ShareSDK", "onCancel ---->  登录取消");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }



    @OnClick(R.id.iv_login)
    public void onViewClicked() {
        if (!ButtonUtils.isFastDoubleClick()) {
            loginWeChat();
        }else{
            ToastUtils.showShort("请勿重复点击");
        }
    }



    @Override
    public void onBackPressed() {
        AppManager.getAppManager().AppExit(this);
    }

}
