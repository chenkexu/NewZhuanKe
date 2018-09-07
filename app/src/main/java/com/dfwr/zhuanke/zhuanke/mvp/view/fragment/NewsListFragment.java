package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.NewsAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseLazyFragment;
import com.dfwr.zhuanke.zhuanke.bean.Article;
import com.dfwr.zhuanke.zhuanke.mvp.contract.NewsListView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.NewsListPresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.CommonWebView;
import com.dfwr.zhuanke.zhuanke.util.ButtonUtils;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ckx on 2018/6/20.
 */

public class NewsListFragment extends BaseLazyFragment<NewsListView, NewsListPresent<NewsListView>> implements
        SwipeRefreshLayout.OnRefreshListener, NewsListView, BaseQuickAdapter.RequestLoadMoreListener {


//    private  RecyclerView recyclerView;
//
//    private SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

    private NewsAdapter newsAdapter;
    private RelativeLayout activityGuide;
    private Unbinder unbinder;
    private List<Article> mData = new ArrayList<>();
    private int currentPage;
    private String type;
    private static final int PAGE_SIZE = 20;
    private String price;
    private String share_host;
    private static NewsListFragment fragment;
    private View errorView;



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



    @Override
    protected void initData() {
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new NewsAdapter(mData);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        newsAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(newsAdapter);
        Bundle bundle = getArguments();
        type = bundle.getString(Systems.ARG_PARAM1);
        price = bundle.getString(Systems.ARG_PARAM2);
        share_host = bundle.getString(Systems.ARG_PARAM3);
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String alreadyLook = SharedPreferencesUtil.getStringData(getActivity(), SharedPreferencesUtil.MESSAGE_ALREADY_LOOKED);
                List<Article> data = newsAdapter.getData();
                Article feedArticleData = data.get(position);


                if (!alreadyLook.contains(feedArticleData.getAid() + "")) {//如果不存在已读的字符串中
                    SharedPreferencesUtil.putStringData(getActivity(), SharedPreferencesUtil.MESSAGE_ALREADY_LOOKED, alreadyLook + feedArticleData.getAid() + ",");
//                    SharedPreferencesUtil.putStringData();
                    SharedPreferencesTool.getInstance().setObject(new Date(), SharedPreferencesTool.current_date);
                    //刷新适配器
                    newsAdapter.notifyDataSetChanged();
                }


                Intent intent = new Intent(getActivity(), CommonWebView.class);
                intent.putExtra(Systems.share_host, share_host);
                intent.putExtra(Systems.articleData, feedArticleData);
                if (!ButtonUtils.isFastDoubleClick()) {
                    if (!Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "shareView");
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        Logger.d("initView");
        if (getActivity() != null) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            errorView  = inflater.inflate(R.layout.view_retry, (ViewGroup) recyclerView.getParent(), false);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        }
        onRefresh();
    }


    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        setRefreshing(true);
        currentPage = 1;
        newsAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mPresent.getProjectListData(type,currentPage, PAGE_SIZE);
    }


    //加载更多
    @Override
    public void onLoadMoreRequested() {
        mPresent.getProjectListLoadMore(type,currentPage, PAGE_SIZE);
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
        newsAdapter.setPrice(price);
    }


    @Override
    public void getArticleListSuccess(List<Article> projectListData) {
        setRefreshing(false);
        Logger.d("getArticleListSuccess ");
        if (projectListData == null || projectListData.size() == 0) {
            newsAdapter.setNewData(null);
            newsAdapter.setEmptyView(R.layout.layout_no_content, (ViewGroup) recyclerView.getParent());
        }else{
            mData = projectListData;
            setData(true, mData);
            newsAdapter.setEnableLoadMore(true);
        }
    }

    @Override
    public void getArticleListRefreshError(String msg) {
        newsAdapter.setEnableLoadMore(true);
        setRefreshing(false);
        newsAdapter.setEmptyView(errorView);
    }



    @Override
    public void getArticleListMoreSuccess(List<Article> projectListData) {
        mData = projectListData;
        setData(false, mData);
    }





    @Override
    public void getArticleListMoreFail(String errorMsg) {
        newsAdapter.loadMoreFail();
    }


    public static NewsListFragment getInstance(String param1, String param2,String param3 ) {
        fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(Systems.ARG_PARAM1, param1);
        args.putString(Systems.ARG_PARAM2, param2);
        args.putString(Systems.ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
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
    protected NewsListPresent<NewsListView> createPresent() {
        return new NewsListPresent<>(this);
    }


    @Override
    protected void loadData() {

    }


}
