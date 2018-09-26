package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.RankListAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseLazyFragment;
import com.dfwr.zhuanke.zhuanke.bean.RankBean;
import com.dfwr.zhuanke.zhuanke.mvp.contract.RankView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.RankPresent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ckx on 2018/7/12.
 */

public class RankListFragment extends BaseLazyFragment<RankView, RankPresent<RankView>> implements
        SwipeRefreshLayout.OnRefreshListener, RankView {




    RankListAdapter newsAdapter;
    private List<RankBean> mData = new ArrayList<>();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.fragment_rank_list;
//    }


    public static RankListFragment getInstance() {
        RankListFragment sf = new RankListFragment();
        return sf;
    }



    @Override
    protected RankPresent createPresent() {
        return new RankPresent<>(this);
    }

    @Override
    protected void loadData() {

    }


//    @Override
//    protected void lazyLoad() {
//        initView();
//        onRefresh();
//    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new RankListAdapter(mData,"1");
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(newsAdapter);
        onRefresh();
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
    public void onRefresh() {
        String title = getTitle();
        if (title.equals("收入排行榜")) {
            newsAdapter.setType(title);
            mPresent.getProfitRanking();
        }else{
            newsAdapter.setType(title);
            mPresent.getStudentRanking();
        }
    }



    private void initView() {
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new RankListAdapter(mData,"1");
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(newsAdapter);
    }


    @Override
    public void getProfitRankingSuccess(List<RankBean> rankBeans) {
        if (rankBeans == null||rankBeans.size()==0) {
            // TODO: 2018/7/20 为空
        }else{
            newsAdapter.setNewData(rankBeans);
        }
        refreshLayout.setRefreshing(false);
    }




//    private boolean isContainsMe(List<RankBean> rankBeans){
//        UserBean userBean = UserDataManeger.getInstance().getUserBean();
//        if (userBean!=null) {
//            for(RankBean rankBean:rankBeans){
//                if (rankBean.getUid() == userBean.getUser().getUid()) {
//                    return true;
//                }else{
//                    return false;
//                }
//            }
//        }
//    }



    @Override
    public void getStudentRankingSuccess(List<RankBean> rankBeans) {
            if (rankBeans == null || rankBeans.size() == 0) {
                // TODO: 2018/7/20 为空
            } else {
                newsAdapter.setNewData(rankBeans);
            }

        refreshLayout.setRefreshing(false);
    }
}
