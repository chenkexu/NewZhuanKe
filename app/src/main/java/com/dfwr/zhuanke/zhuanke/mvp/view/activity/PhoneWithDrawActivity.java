package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.base.BasePresenter;
import com.dfwr.zhuanke.zhuanke.mvp.event.ChooseFragmentEvent;
import com.dfwr.zhuanke.zhuanke.widget.ClearEditText;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ckx on 2018/7/17.
 */

public class PhoneWithDrawActivity extends BaseActivity {


    @BindView(R.id.tv_my_account)
    TextView tvMyAccount;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_account)
    ClearEditText etAccount;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.tv_go_makemoney)
    Button tvGoMakemoney;
    @BindView(R.id.activity_guide)
    LinearLayout activityGuide;

    private String[] accountSelects = {"10元", "30元", "50元", "100元"};
    private String withdrawType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_withdraw);
        ButterKnife.bind(this);
        initView();
        initData();
    }



    private void initView() {
        textTitle.setText("话费充值");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Adapter adaptersex = new Adapter(Arrays.asList(accountSelects));
        recyclerView.setAdapter(adaptersex);

        adaptersex.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sexPosition = position;
                String account = accountSelects[position];
                adaptersex.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();

        if (intent != null) {
            withdrawType = "0";
            String payAccount = intent.getStringExtra(Systems.payAccount);
            String payName = intent.getStringExtra(Systems.payName);
        } else {
            withdrawType = "1";
        }


    }


    @OnClick({R.id.tv_submit, R.id.tv_go_makemoney,R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_go_makemoney:
                ChooseFragmentEvent chooseFragmentEvent = new ChooseFragmentEvent();
                chooseFragmentEvent.fragmentStr = "1";
                EventBus.getDefault().post(chooseFragmentEvent);
                finish();
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra(Systems.from_withdraw, "0");
//                startActivity(intent);
                break;
        }
    }


    private int sexPosition = 0;


    public class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private int type;

        public Adapter(@Nullable List<String> data) {
            super(R.layout.item_phone_money_select, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.tv_title, item);
            if (sexPosition == layoutPosition) {
                helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.bg_white));
                helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_selec);
            } else {
                helper.setTextColor(R.id.tv_title, getResources().getColor(R.color.text_gray_new));
                helper.setBackgroundRes(R.id.rl_title, R.drawable.btn_no_selec_border);
            }
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
