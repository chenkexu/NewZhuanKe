package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.base.BasePresenter;
import com.dfwr.zhuanke.zhuanke.bean.UserBean;
import com.dfwr.zhuanke.zhuanke.mvp.event.ChooseFragmentEvent;
import com.dfwr.zhuanke.zhuanke.util.ButtonUtils;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.util.UserDataManeger;
import com.dfwr.zhuanke.zhuanke.widget.MyTitle;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/7/17.
 */
public class BindAlipayActivity extends BaseActivity {


    @BindView(R.id.my_title)
    MyTitle myTitle;

    @BindView(R.id.etPayName)
    EditText etPayName;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.etPayAccount)
    EditText etPayAccount;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.tv_go_makemoney)
    Button tvGoMakemoney;
    private Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_alipay);
        ButterKnife.bind(this);
        initView();
        initData();
    }




    private void initView() {
        myTitle.setImageBack(this);
        myTitle.setTitleName("完善支付宝信息");
        String name = SharedPreferencesUtil.getStringData(this, SharedPreferencesTool.alipay_name);
        String account = SharedPreferencesUtil.getStringData(this, SharedPreferencesTool.alipay_account);
        if (name!=null && account !=null) {
            etPayName.setText(name);
            etPayAccount.setText(account);
        }
    }



    private void initData() {
        UserBean userBean = UserDataManeger.getInstance().getUserBean();
        if (userBean!=null) {
            tvName.setText("昵称："+userBean.getUser().getWxName());
        }
    }





    @OnClick({R.id.tv_submit, R.id.tv_go_makemoney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (!ButtonUtils.isFastDoubleClick()) {

                    String payAccount = etPayAccount.getText().toString();
                    String payName = etPayName.getText().toString();

                    if(TextUtils.isEmpty(payName)){
                        ToastUtils.showShort("请输入收款人姓名");
                        return;
                    }

                    if(TextUtils.isEmpty(payAccount)){
                        ToastUtils.showShort("请输入支付宝账号");
                        return;
                    }

                    if(!TextUtils.isEmpty(payAccount)){
                        if(!RegexUtils.isMobileExact(payAccount)&&!RegexUtils.isEmail(payAccount)){
                            ToastUtils.showShort("请输入有效的支付宝账号");
                            return;
                        }
                    }

                    SharedPreferencesUtil.putStringData(this, SharedPreferencesTool.alipay_account,payAccount);
                    SharedPreferencesUtil.putStringData(this, SharedPreferencesTool.alipay_name,payName);
                    intent = new Intent(this, GoWithDrawActivity.class);
                    intent.putExtra(Systems.payAccount,payAccount);
                    intent.putExtra(Systems.payName,payName);
                    intent.putExtra(Systems.withDrawType,Systems.alipay);
                    startActivity(intent);
                }

                break;
            case R.id.tv_go_makemoney:
                ChooseFragmentEvent chooseFragmentEvent = new ChooseFragmentEvent();
                chooseFragmentEvent.fragmentStr = "1";
                EventBus.getDefault().post(chooseFragmentEvent);
                closeActivity(MyProfitActivity.class,BindAlipayActivity.class);
                break;
        }
    }






    @Override
    protected BasePresenter createPresent() {
        return new BasePresenter() {
            @Override
            public void fecth() {

            }
        };
    }

}
