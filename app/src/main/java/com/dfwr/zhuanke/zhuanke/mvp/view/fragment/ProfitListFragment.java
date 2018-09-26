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
import com.dfwr.zhuanke.zhuanke.adapter.ProfitAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseLazyFragment;
import com.dfwr.zhuanke.zhuanke.bean.MyProfit;
import com.dfwr.zhuanke.zhuanke.mvp.contract.ProfitView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.ProfitPresent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ckx on 2018/7/12.
 * 收益列表fragment
 */

public class ProfitListFragment extends BaseLazyFragment<ProfitView, ProfitPresent<ProfitView>> implements
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, ProfitView {


    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;


    private List<Integer> channelIds = new ArrayList<>();

    //    private int currentPage;


    ProfitAdapter newsAdapter;

    private List<MyProfit> mData = new ArrayList<>();
    private int currentPage;
    private static final int PAGE_SIZE = 20;
    private int type = 1;

//    @Override
//    protected int setLayoutId() {
//        return R.layout.fragment_rank_list;
//    }


    public static ProfitListFragment getInstance() {
        ProfitListFragment sf = new ProfitListFragment();
        return sf;
    }


    @Override
    protected void lazyLoad() {
        initView();
        onRefresh();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {

    }
    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }


    @Override
    protected void initData() {
//        mData = new ArrayList<>();
//        String[] mTitles = getResources().getStringArray(R.array.my_profit);
//        String title = getTitle();
//        if (title.equals(mTitles[0])) {
//            type = 1;
//        }else if(title.equals(mTitles[1])){
//            type = 2;
//        }else{
//            type = 3;
//        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        refreshLayout.setOnRefreshListener(this);//刷新
//        newsAdapter = new ProfitAdapter(mData,type);
//        newsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        newsAdapter.setOnLoadMoreListener(this, recyclerView);
//        recyclerView.setAdapter(newsAdapter);
//        onRefresh();
    }


    private void initView() {
        mData = new ArrayList<>();
        String[] mTitles = getResources().getStringArray(R.array.my_profit);
        String title = getTitle();
        if (title.equals(mTitles[0])) {
            type = 1;
        }else if(title.equals(mTitles[1])){
            type = 2;
        }else{
            type = 3;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new ProfitAdapter(mData,type);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        newsAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(newsAdapter);
    }





    @Override
    public void onRefresh() {
        setRefreshing(true);
        currentPage = 1;
        newsAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mPresent.getProfitList(currentPage, PAGE_SIZE,type);
    }



    @Override
    public void onLoadMoreRequested() {
        mPresent.getProfitListLoadMore(currentPage, PAGE_SIZE,type);
    }


    @Override
    public void getProfitList(List<MyProfit> projectListData) {
        setRefreshing(false);
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
    public void getProfitListError(String msg) {
        setRefreshing(false);
        newsAdapter.setEnableLoadMore(true);
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void getProfitListLoadMoreSuccess(List<MyProfit> projectListData) {
        mData = projectListData;
        setData(false, mData);
    }


    @Override
    public void getProfitListLoadMoreFail(String errorMsg) {
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
    protected ProfitPresent<ProfitView> createPresent() {
        return new ProfitPresent<>(this);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


}



