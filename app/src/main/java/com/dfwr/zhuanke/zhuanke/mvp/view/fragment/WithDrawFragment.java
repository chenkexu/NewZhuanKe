package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.WithDrawHistoryAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseTwoFragment;
import com.dfwr.zhuanke.zhuanke.bean.CheckWithDrawBean;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.bean.WithDrawHistory;
import com.dfwr.zhuanke.zhuanke.mvp.contract.HomeWithDrawView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.HomeWithDrawPresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.AttentionWechatNumberActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.BindAlipayActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.BindPhoneActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.GoWithDrawActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.PhoneWithDrawActivity;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/7/12.
 * 提现
 */
public class WithDrawFragment extends BaseTwoFragment<HomeWithDrawView, HomeWithDrawPresent<HomeWithDrawView>> implements HomeWithDrawView {




    @BindView(R.id.ll_withdraw_wechat)
    LinearLayout llWithdrawWechat;
    @BindView(R.id.ll_withdraw_alipay)
    LinearLayout llWithdrawAlipay;
    @BindView(R.id.ll_withdraw_phone)
    LinearLayout llWithdrawPhone;



    private List<WithDrawHistory> mData;
    private WithDrawHistoryAdapter newsAdapter;
    private int currentPage;
    private String type;
    private static final int PAGE_SIZE = 20;



    @Override
    protected int setLayoutId() {
        return R.layout.fragment_winthdraw_new;
    }


    @Override
    protected void initData() {
        super.initData();
    }



    @Override
    protected void initView() {
        super.initView();
    }




    @OnClick({R.id.ll_withdraw_wechat, R.id.ll_withdraw_alipay, R.id.ll_withdraw_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_withdraw_wechat:
                mPresent.checkWithDraw("1");
                break;
            case R.id.ll_withdraw_alipay:
                mPresent.checkWithDraw("2");
                break;
            case R.id.ll_withdraw_phone:
                startActivity(new Intent(getActivity(), PhoneWithDrawActivity.class));
                break;
        }
    }






    @Override
    public void getUserInfo(UserBaseInfo userBaseInfo) {
    }

    @Override
    public void getCheckWithDrawWeChatSuccess(CheckWithDrawBean checkWithDrawBean) {
        Intent intent1 = new Intent();
        intent1.putExtra(Systems.withDrawType, Systems.wechat);
        if (checkWithDrawBean.getNum() == 0) { //是否首次提现 0，没有提现记录
            intent1.putExtra(Systems.isFirstWithDraw, true);
        } else {
            intent1.putExtra(Systems.isFirstWithDraw, false);
        }
        if (checkWithDrawBean.getPhoneIsBinding() == 0) { //没绑定手机号
            intent1.setClass(getActivity(), BindPhoneActivity.class);
            startActivity(intent1);
        } else if (checkWithDrawBean.getPublicNum() == 0) { //没关注微信
            intent1.setClass(getActivity(), AttentionWechatNumberActivity.class);
            startActivity(intent1);
        } else {
            intent1.setClass(getActivity(), GoWithDrawActivity.class);
            startActivity(intent1);
        }
    }


    @Override
    public void getCheckWithDrawAlipaySuccess(CheckWithDrawBean object) {
        Intent intent2 = new Intent();
        intent2.putExtra(Systems.withDrawType, Systems.alipay);
        if (object.getPhoneIsBinding() == 0) { //没绑定手机号
            intent2.setClass(getActivity(), BindPhoneActivity.class);
        }else{
            intent2.setClass(getActivity(), BindAlipayActivity.class);
        }
        startActivity(intent2);
    }


    @Override
    public void getWithDrawHistorySuccess(List<WithDrawHistory> projectListData) {

    }




    @Override
    public void getWithDrawHistoryRefreshError(String msg) {
    }

    @Override
    public void getWithDrawHistoryMoreSuccess(List<WithDrawHistory> projectListData) {

    }

    @Override
    public void getWithDrawHistoryMoreFail(String errorMsg) {
    }









    @Override
    protected HomeWithDrawPresent<HomeWithDrawView> createPresent() {
        return new HomeWithDrawPresent<>(this);
    }


    @Override
    public void showLoading() {
        showDefaultLoading();
    }


    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }
}
