package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.base.BasePresenter;
import com.dfwr.zhuanke.zhuanke.widget.MyTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/7/17.
 */

public class AttentionWechatNumberActivity extends BaseActivity {


    @BindView(R.id.my_title)
    MyTitle myTitle;
    @BindView(R.id.tv_wechat_replay)
    TextView tvWechatReplay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat_number);
        ButterKnife.bind(this);
        myTitle.setTitleName("关注微信公众号");
        myTitle.setImageBack(this);
        tvWechatReplay.setText("在公众号内发送”恭喜发财“,收到“绑定成功”即可。");
    }


    @Override
    protected BasePresenter createPresent() {
        return new BasePresenter() {
            @Override
            public void fecth() {

            }
        };
    }


    @OnClick({R.id.tv_copy_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_name:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(getResources().getString(R.string.wechat_name));
                ToastUtils.showShort("公众号已复制");
                break;
        }
    }


    public boolean joinQQGroup() {
        String key = "jPgBTG3gAQVQklkf9xEWSxpkIaO9hcDt";
        Intent intent = new Intent();
//        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtils.showShort("未安装手Q或安装的版本不支持");
            return false;
        }
    }
}
