package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.api.ApiManager;
import com.dfwr.zhuanke.zhuanke.api.BaseObserver;
import com.dfwr.zhuanke.zhuanke.api.param.ParamsUtil;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.base.BaseLazyFragment;
import com.dfwr.zhuanke.zhuanke.base.BaseTwoFragment;
import com.dfwr.zhuanke.zhuanke.bean.ProjectClassifyData;
import com.dfwr.zhuanke.zhuanke.bean.Propertie;
import com.dfwr.zhuanke.zhuanke.mvp.contract.NewsView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.NewsPresent;
import com.dfwr.zhuanke.zhuanke.util.RxUtil;
import com.dfwr.zhuanke.zhuanke.widget.CustomViewPager;
import com.dfwr.zhuanke.zhuanke.widget.Dialog.ChannelDialog;
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.flyco.tablayout.SlidingTabLayout;
import com.hanks.htextview.rainbow.RainbowTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ckx on 2018/7/12.
 */

public class NewsFragment extends BaseTwoFragment<NewsView, NewsPresent<NewsView>>
        implements NewsView {

    @BindView(R.id.add_channel_iv)
    ImageView addChannelIv;

    @BindView(R.id.tv_price)
    RainbowTextView tv_price;

    @BindView(R.id.view_pager)
    CustomViewPager viewPager;

    @BindView(R.id.tl_5)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.ll_no_content)
    LinearLayout llNoContent;
    private List<ProjectClassifyData> mData;
    private List<Integer> channelIds = new ArrayList<>();

//    private int currentPage;

    private ArrayList<BaseLazyFragment> mFragments = new ArrayList<>();
    private int currentPage;

    private NewsListFragment projectListFragment;

    @Override
    protected int setLayoutId() {
        return R.layout.app_bar_news;
    }


    @Override
    protected void fragmentToUserVisible() {
        super.fragmentToUserVisible();
        if (mData == null) {
            setData();
        }
    }

    private void setData() {
        mPresent.getProjectClassifyData();
    }


    @Override
    protected void initData() {
        super.initData();
        // TODO: 2018/7/18 请求新闻数据 
        setData();
        llNoContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }


    @Override
    public void getProjectClassifyDataSuccess(List<ProjectClassifyData> projectClassifyDatas) {
        llNoContent.setVisibility(View.GONE);
        mData = projectClassifyDatas;
        addChannelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelDialog channelDialog = new ChannelDialog(getActivity(), mData);
                channelDialog.SetOnChannelClick(new ChannelDialog.SetOnChannelClickInterFace() {
                    @Override
                    public void itemOnclick(final int position) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(position, false);
                            }
                        }, 100);
                    }
                });
                channelDialog.show();
            }
        });
        getProperties();
//        initViewPagerAndTabLayout();
    }


    @Override
    public void getProjectClassifyDataError(String error) {
        ToastUtils.showShort(error);
        llNoContent.setVisibility(View.VISIBLE);
    }


    //获取各个单价
    public void getProperties() {
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getProperties(ParamsUtil.getParams(map))
                .compose(RxUtil.<ApiResponse<Propertie>>rxSchedulerHelper())
                .subscribe(new BaseObserver<Propertie>() {
                    @Override
                    protected void onSuccees(ApiResponse<Propertie> t) {
                        if (t != null) {
                            Propertie result = t.getResult();
                            initViewPagerAndTabLayout(result);
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });
    }


    private void initViewPagerAndTabLayout(Propertie propertie) {
        tv_price.setText("分享并阅读文章 可获得"+propertie.getShare_price() +"元/篇奖励哦");
//        Bundle arguments = getArguments();
//        Propertie propertie = (Propertie) arguments.getSerializable(Systems.propertie);

        if (propertie != null) {
            for (ProjectClassifyData data : mData) {
//                if (data.getType().equals("养生")) {
//                    projectListFragment = NewsListFragment.getInstance("健康", propertie.getShare_price(), propertie.getShare_host());
//                } else {
//                }
                projectListFragment = NewsListFragment.getInstance(data.getType(), propertie.getShare_price(), propertie.getShare_host());
                mFragments.add(projectListFragment);
                channelIds.add(data.getId());
            }

            viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mFragments.get(position);
                }

                @Override
                public int getCount() {
                    return mData == null ? 0 : mData.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mData.get(position).getType();
                }

            });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewPager.setOffscreenPageLimit(2);
            mTabLayout.setViewPager(viewPager);
            viewPager.setCurrentItem(Systems.TAB_ONE, false);
        }
    }


    @Override
    protected NewsPresent createPresent() {
        return new NewsPresent<>(this);
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
