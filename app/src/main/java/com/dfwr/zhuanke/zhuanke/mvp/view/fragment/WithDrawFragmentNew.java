package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ckx on 2018/7/12.
 * 提现列表
 */
public class WithDrawFragmentNew extends BaseTwoFragment<HomeWithDrawView, HomeWithDrawPresent<HomeWithDrawView>> implements HomeWithDrawView
        ,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{



    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;



    private List<WithDrawHistory> mData;
    private WithDrawHistoryAdapter newsAdapter;
    private int currentPage;
    private String type;
    private static final int PAGE_SIZE = 20;



    @Override
    protected int setLayoutId() {
        return R.layout.fragment_rank_list;
    }


    @Override
    protected void initData() {
        super.initData();
    }



    @Override
    protected void initView() {
        super.initView();
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new WithDrawHistoryAdapter(mData);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        newsAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(newsAdapter);
        setData();
    }



    private void setData() {
//        Logger.d("提现之和返回的。");
//        UserBean userBean = (UserBean) SharedPreferencesTool.getObjectFromShare(SharedPreferencesTool.user);
//        mPresent.getUserInfo();
        onRefresh();
    }


//
//    @OnClick({R.id.ll_withdraw_wechat, R.id.ll_withdraw_alipay, R.id.ll_withdraw_phone})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_withdraw_wechat:
//                mPresent.checkWithDraw("1");
//                break;
//            case R.id.ll_withdraw_alipay:
//                mPresent.checkWithDraw("2");
//                break;
//            case R.id.ll_withdraw_phone:
//                startActivity(new Intent(getActivity(), PhoneWithDrawActivity.class));
//                break;
//        }
//    }





//    @Override
//    protected void fragmentToUserVisible() {
//        super.fragmentToUserVisible();
//        setData();
//    }

    @Override
    public void getUserInfo(UserBaseInfo userBaseInfo) {
//        tvBalance.setText(userBaseInfo.getAccount().getBalance() + "");
//        tvAllWithDraw.setText(userBaseInfo.getAllWithDrawMoney() + "");
//        SharedPreferencesUtil.putStringData(getActivity(), SharedPreferencesTool.balance, userBaseInfo.getAccount().getBalance() + "");
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
        if (projectListData == null || projectListData.size() == 0) {
            newsAdapter.setNewData(null);
            newsAdapter.setEmptyView(R.layout.layout_no_content, (ViewGroup) recyclerView.getParent());
        }else{
            mData = projectListData;
            setData(true, mData);
            newsAdapter.setEnableLoadMore(true);
        }
        refreshLayout.setRefreshing(false);
    }




    @Override
    public void getWithDrawHistoryRefreshError(String msg) {
        newsAdapter.setEnableLoadMore(true);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getWithDrawHistoryMoreSuccess(List<WithDrawHistory> projectListData) {
        mData = projectListData;
        setData(false, mData);
    }

    @Override
    public void getWithDrawHistoryMoreFail(String errorMsg) {
        newsAdapter.loadMoreFail();
    }




    private void setData(boolean isRefresh, List data) {
        currentPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            newsAdapter.setNewData(data);
        } else {
            if (size > 0) {
                newsAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            newsAdapter.loadMoreEnd(isRefresh);
        } else {
            newsAdapter.loadMoreComplete();
        }
    }



    @Override
    public void onRefresh() {
        Logger.d("提现之和返回的onRefresh()。");
        currentPage = 1;
        newsAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mPresent.getWithDrawHistory(currentPage, PAGE_SIZE);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresent.getWithDrawHistoryLoadMore(currentPage, PAGE_SIZE);
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
