package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.mvp.contract.IMsgView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.MsgPresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.ProfitListFragment;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.WithDrawFragment;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.WithDrawFragmentNew;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.MyTitle;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfitActivity extends BaseActivity<IMsgView, MsgPresent<IMsgView>> implements IMsgView {


    private static final String TAG = MyProfitActivity.class.getSimpleName();


    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.my_title)
    MyTitle myTitle;


//    private ProfitListFragment newsFragment;
    private String my_profit_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        ButterKnife.bind(this);
        Intent intent = getIntent();


        my_profit_type = intent.getStringExtra(Systems.my_profit_type);

        String go_withDraw = intent.getStringExtra(Systems.go_activity_type);
        switch (go_withDraw){
            case Systems.activity_type_profit:
                myTitle.setTitleName(my_profit_type);
                selectedFragment(0);
                break;
            case Systems.activity_type_withdraw:
                myTitle.setTitleName("提现");
                selectedFragment(2);
                break;
            case Systems.activity_type_withdraw_list:
                myTitle.setTitleName("提现记录");
                selectedFragment(1);
                break;
        }

//        if (my_profit_type == null) {
//            myTitle.setTitleName("提现记录");
//            selectedFragment(1);
//        }else if(go_withDraw == null){
//            myTitle.setTitleName(my_profit_type);
//            selectedFragment(0);
//        }else{
//            myTitle.setTitleName("提现");
//            selectedFragment(2);
//        }
        myTitle.setImageBack(this);

        mPresent.getUserInfo();
    }


    public void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (position == 0) {
            ProfitListFragment newsFragment = ProfitListFragment.getInstance();
            newsFragment.setTitle(my_profit_type);
            transaction.add(R.id.content, newsFragment);
        }else if(position == 1){
            WithDrawFragmentNew withDrawFragment = new WithDrawFragmentNew();
            withDrawFragment.setTitle(my_profit_type);
            transaction.add(R.id.content, withDrawFragment);
        }else if(position == 2){
            WithDrawFragment withDrawFragment = new WithDrawFragment();
            withDrawFragment.setTitle(my_profit_type);
            transaction.add(R.id.content, withDrawFragment);
    }
        transaction.commitAllowingStateLoss();
    }


    @Override
    protected MsgPresent<IMsgView> createPresent() {
        return new MsgPresent<>(this);
    }


    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void getUserInfo(UserBaseInfo userBaseInfo) {
        SharedPreferencesUtil.putStringData(this, SharedPreferencesTool.balance,userBaseInfo.getAccount().getBalance()+"");
    }
}
