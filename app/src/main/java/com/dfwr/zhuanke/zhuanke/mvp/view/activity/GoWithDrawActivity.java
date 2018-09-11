package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.bean.CheckWithDrawBean;
import com.dfwr.zhuanke.zhuanke.mvp.contract.MeWithDrawView;
import com.dfwr.zhuanke.zhuanke.mvp.event.ChooseFragmentEvent;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.MeWithDrawPresent;
import com.dfwr.zhuanke.zhuanke.util.ButtonUtils;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/7/17.
 */

public class GoWithDrawActivity extends BaseActivity<MeWithDrawView,MeWithDrawPresent<MeWithDrawView>> implements MeWithDrawView {

    @BindView(R.id.tv_my_account)
    TextView tvMyAccount;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_go_makemoney)
    TextView tvGoMakemoney;
    @BindView(R.id.activity_guide)
    LinearLayout activityGuide;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    private String[] accountSelects = {"3元(仅限首次)", "10元", "20元", "50元", "100元"};
    private Double money;
    private Double[] secletMoney = {3.0, 10.0, 20.0, 50.0, 100.0};


    private String withdrawType="";
    private Adapter adaptersex;
    private boolean isFirstWithDraw;
    private String type;
    private String payAccount;
    private String payName;
    private int choosePosition = 0;
    private String balance;//余额
    private TextView tv_title;
    private RelativeLayout rl_title;
    private boolean flag;
    private double doubleBalance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_withdraw);
        ButterKnife.bind(this);
        money = secletMoney[0];

        initView();
        initData();
        doubleBalance = Double.parseDouble(balance);
        if (doubleBalance < money) {
            tvSubmit.setText("余额不足");
            tvSubmit.setEnabled(false);
            tvSubmit.setBackgroundResource(R.drawable.round_border_gray);
        }
    }


    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        adaptersex = new Adapter(Arrays.asList(accountSelects));
        recyclerView.setAdapter(adaptersex);
        adaptersex.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFirstWithDraw) { //首次提现
                    choosePosition = position;
                    money = secletMoney[position];
                    adaptersex.notifyDataSetChanged();
                }else{  //不是首次提现
                    if (position == 0) {
                        return;
                    }else{
                        choosePosition = position;
                        money = secletMoney[position];
                        adaptersex.notifyDataSetChanged();
                    }
                }

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    private void initData() {

        balance = SharedPreferencesUtil.getStringData(this, SharedPreferencesTool.balance);
        tvMyAccount.setText(balance);
        Intent intent = getIntent();

        if (intent!=null) {  //支付宝提现
            type = intent.getStringExtra(Systems.withDrawType);
            if (type.equals(Systems.alipay)) { //支付宝提现
                payAccount = intent.getStringExtra(Systems.payAccount);
                payName = intent.getStringExtra(Systems.payName);
                // TODO: 2018/7/26
                mPresent.checkWithDraw(2);
            }else if(type.equals(Systems.wechat)){ //微信提现
                isFirstWithDraw = intent.getBooleanExtra(Systems.isFirstWithDraw, true);
                adaptersex.notifyDataSetChanged();
                if (isFirstWithDraw) { //首次提现
                    choosePosition = 0;
                }else{  //不是首次提现
                    money = 10.0;
                    choosePosition = 1;
                }
            }
        }
    }


    @OnClick({R.id.tv_submit, R.id.tv_go_makemoney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (!ButtonUtils.isFastDoubleClick()) {
                    Logger.d("点击了提现提交按钮");

                    if (doubleBalance < money) {
                        ToastUtils.showShort("余额不足");
                        return;
                    }
                    if (type.equals(Systems.alipay)) {
                        mPresent.alipayTakeMoney(money,payName,payAccount);
                    }else{
                        mPresent.weChatTakeMoney(money);
                    }
                }

                break;
            case R.id.tv_go_makemoney:
                ChooseFragmentEvent chooseFragmentEvent = new ChooseFragmentEvent();
                chooseFragmentEvent.fragmentStr = "1";
                EventBus.getDefault().post(chooseFragmentEvent);
                closeActivity(GoWithDrawActivity.class,BindAlipayActivity.class,BindPhoneActivity.class,MyProfitActivity.class);
                break;
        }
    }





    //提现校验成功
    @Override
    public void getCheckWithDrawSuccess(CheckWithDrawBean checkWithDrawBean) {
        if (checkWithDrawBean.getNum()==0) { //首次
            isFirstWithDraw = true;
            choosePosition = 0;
        }else{ //不是首次提现
            money = 10.0;
            choosePosition = 1;
            isFirstWithDraw = false;
        }
        adaptersex.notifyDataSetChanged();
    }


    @Override
    public void withdrawSuccess(Object object) {
        ToastUtils.showShort("提交成功");
        ChooseFragmentEvent chooseFragmentEvent = new ChooseFragmentEvent();
        chooseFragmentEvent.fragmentStr = "0";
        EventBus.getDefault().post(chooseFragmentEvent);
        Intent intent = new Intent();
        intent.setClass(this, MyProfitActivity.class);
        intent.putExtra(Systems.go_activity_type, Systems.activity_type_withdraw_list);
        startActivity(intent);
        closeActivity(GoWithDrawActivity.class,BindPhoneActivity.class,BindAlipayActivity.class,AttentionWechatNumberActivity.class);
    }






    public class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public Adapter(@Nullable List<String> data) {
            super(R.layout.item_account_select, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.tv_title, item);

            if (isFirstWithDraw) { //首次提现
                if (choosePosition == layoutPosition) {
                    helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.bg_white));
                    helper.setBackgroundRes(R.id.rl_title, R.drawable.bg_tv_round_gradient);
                } else {
                    helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.text_gray_new));
                    helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_no_selec_border);
                }
            }else{ //不是首次提现
                if (layoutPosition == 0) {
                    helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_selec_gray_border);
                    helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.bg_white));
                    View view = helper.getView(R.id.rl_title);
                    view.setClickable(false);

                }else{
                    if (choosePosition == layoutPosition) {
                        helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.bg_white));
                        helper.setBackgroundRes(R.id.rl_title, R.drawable.bg_tv_round_gradient);
                    } else {
                        helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.text_gray_new));
                        helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_no_selec_border);
                    }
                }
            }
        }
    }






    @Override
    protected MeWithDrawPresent<MeWithDrawView> createPresent() {
        return new MeWithDrawPresent<>(this);
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
