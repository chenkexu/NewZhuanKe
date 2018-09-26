package com.dfwr.zhuanke.zhuanke.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.MystudentAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.bean.MyStudentBean;
import com.dfwr.zhuanke.zhuanke.mvp.contract.MyStudentView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.MyStudentListPresent;
import com.dfwr.zhuanke.zhuanke.widget.MyTitle;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by ckx on 2018/7/17.
 */

public class MyStudentListActivity extends BaseActivity<MyStudentView, MyStudentListPresent<MyStudentView>> implements
        MyStudentView,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.my_title)
    MyTitle myTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    MystudentAdapter newsAdapter;

    private List<MyStudentBean> mData = new ArrayList<>();
    private int currentPage = 1;
    private static final int PAGE_SIZE = 30;
    private String type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        ButterKnife.bind(this);
        myTitle.setImageBack(this);
        Intent intent = getIntent();
        String studentType = intent.getStringExtra(Systems.my_student_type);
        if (studentType.equals("all")) {
            myTitle.setTitleName("累计收徒");
            type = "all";
        }else{
            myTitle.setTitleName("今日收徒");
            type = "today";
        }
        initView();
        onRefresh();
    }


    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(this);//刷新
        newsAdapter = new MystudentAdapter(mData);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        newsAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(newsAdapter);
    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        newsAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mPresent.getMyStudent(currentPage, PAGE_SIZE,type);
    }


    @Override
    public void onLoadMoreRequested() {
        mPresent.getMyStudentLoadMore(currentPage, PAGE_SIZE,type);
    }


    @Override
    public void getStudentList(List<MyStudentBean> projectListData) {
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
    public void getStudentListError(String msg) {
        newsAdapter.setEnableLoadMore(true);
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void getStudentListLoadMoreSuccess(List<MyStudentBean> projectListData) {
        mData = projectListData;
        setData(false, mData);
    }


    @Override
    public void getStudentListLoadMoreFail(String errorMsg) {
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
    protected MyStudentListPresent<MyStudentView> createPresent() {
        return new MyStudentListPresent<>(this);
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
