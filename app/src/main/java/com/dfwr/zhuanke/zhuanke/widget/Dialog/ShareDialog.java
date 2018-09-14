package com.dfwr.zhuanke.zhuanke.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.util.ShareUtil;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

public class ShareDialog extends Dialog {
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.iv_wechat_circle)
    ImageView ivWechatCircle;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_qq_zone)
    ImageView ivQqZone;
    private Context context;
    private final ShareUtil shareUtil;
    private String code;
    private final HashMap<String, Object> hashMap;


    public void setCode(String code) {
        this.code = code;
    }

    public ShareDialog(Context context) {
        super(context, R.style.RemindDialog);// 必须调用父类的构造函数
        this.context = context;
        hashMap = new HashMap<>();
        shareUtil = new ShareUtil(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);

        setContentView(contentView);
        ButterKnife.bind(this, contentView);
        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        //设置Dialog背景色
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //获取屏幕宽
        wlp.width = (int) (d.getWidth());
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.share_anim_style);
        window.setAttributes(wlp);
    }

    public void setWeChat(){
        ivWechat.setVisibility(View.VISIBLE);
        ivWechatCircle.setVisibility(View.VISIBLE);
        ivQq.setVisibility(View.GONE);
        ivQqZone.setVisibility(View.GONE);
    }


    public void setQQ(){
        ivWechat.setVisibility(View.GONE);
        ivWechatCircle.setVisibility(View.GONE);
        ivQq.setVisibility(View.VISIBLE);
        ivQqZone.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.iv_close, R.id.iv_wechat, R.id.iv_wechat_circle, R.id.iv_qq, R.id.iv_qq_zone})
    public void onViewClicked(View view) {
        hashMap.put("AppId", Systems.qq_App_id);
        hashMap.put("AppKey",Systems.qq_App_secret);
        hashMap.put("BypassApproval","false");
        hashMap.put("ShareByAppClient","true");

        switch (view.getId()) {
            case R.id.iv_close:
                break;
            case R.id.iv_wechat:
                shareUtil.shareToWeixinFriend(code,1);
                break;
            case R.id.iv_wechat_circle:
                shareUtil.shareToCircleOfFriends(context.getResources().getString(R.string.app_name),code,1);
                break;
            case R.id.iv_qq://QQ
                shareUtil.shareBitmap(code);
                ShareSDK.setPlatformDevInfo(QQ.NAME,hashMap);
                Platform.ShareParams sp1 = new Platform.ShareParams();
                sp1.setImagePath(shareUtil.share_img_path);
                Platform qzone = ShareSDK.getPlatform (QQ.NAME);
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        //分享成功的回调
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                    }
                });
                qzone.share(sp1);
                break;
            case R.id.iv_qq_zone://QQ空间
                shareUtil.shareBitmap(code);
                ShareSDK.setPlatformDevInfo(QZone.NAME,hashMap);
                Platform.ShareParams sp = new Platform.ShareParams();
                sp.setImagePath(shareUtil.share_img_path);
                Platform qzone1 = ShareSDK.getPlatform (QZone.NAME);
                qzone1.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        //分享成功的回调
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                    }
                });
                qzone1.share(sp);
                break;

        }
        dismiss();
    }
}