package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.content.Intent;
import android.view.View;

import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseTwoFragment;
import com.dfwr.zhuanke.zhuanke.bean.CheckWithDrawBean;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.bean.WithDrawHistory;
import com.dfwr.zhuanke.zhuanke.mvp.contract.HomeWithDrawView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.HomeWithDrawPresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyProfitActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyStudentListActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.RankActivity;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import java.util.List;

import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ckx on 2018/7/12.
 * 提现
 */
public class InformationFragment extends BaseTwoFragment<HomeWithDrawView, HomeWithDrawPresent<HomeWithDrawView>> implements HomeWithDrawView {


    Unbinder unbinder;
    private String[] mTitles;
    private Intent intent;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    protected void initView() {
        super.initView();
        mTitles = getResources().getStringArray(R.array.my_profit);
        intent = new Intent();

    }

    @OnClick({R.id.rv_balance_account, R.id.rl_my_commission, R.id.rl_commission_withdraw, R.id.rl_invoice, R.id.rl_service, R.id.rl_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_balance_account: //文章收益记录
                intent.setClass(getActivity(), MyProfitActivity.class);
                intent.putExtra(Systems.my_profit_type, mTitles[0]);

                intent.putExtra(Systems.go_activity_type, Systems.activity_type_profit);
                break;
            case R.id.rl_my_commission: //收徒奖励记录
                intent.setClass(getActivity(), MyProfitActivity.class);
                intent.putExtra(Systems.my_profit_type, mTitles[1]);
                intent.putExtra(Systems.go_activity_type, Systems.activity_type_profit);
                break;
            case R.id.rl_commission_withdraw: //徒弟提成收益记录
                intent.setClass(getActivity(), MyProfitActivity.class);
                intent.putExtra(Systems.my_profit_type, mTitles[2]);
                intent.putExtra(Systems.go_activity_type, Systems.activity_type_profit);
                break;
            case R.id.rl_invoice: //收徒记录
                intent.setClass(getActivity(), MyStudentListActivity.class);
                intent.putExtra(Systems.my_student_type, "all");
                break;
            case R.id.rl_service: //提现记录
                intent.setClass(getActivity(), MyProfitActivity.class);
                intent.putExtra(Systems.go_activity_type, Systems.activity_type_withdraw_list);
                break;
            case R.id.rl_share: //排行榜
                intent.setClass(getActivity(), RankActivity.class);
                break;

        }
        startActivity(intent);
    }




    @Override
    protected void fragmentToUserVisible() {
        super.fragmentToUserVisible();
    }


    @Override
    public void getUserInfo(UserBaseInfo userBaseInfo) {

    }


    @Override
    public void getCheckWithDrawWeChatSuccess(CheckWithDrawBean checkWithDrawBean) {

    }


    @Override
    public void getCheckWithDrawAlipaySuccess(CheckWithDrawBean object) {

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
