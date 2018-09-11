package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.HomeAdapter;
import com.dfwr.zhuanke.zhuanke.base.BaseTwoFragment;
import com.dfwr.zhuanke.zhuanke.bean.HomeBean;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.bean.UserBean;
import com.dfwr.zhuanke.zhuanke.mvp.contract.HomeMeView;
import com.dfwr.zhuanke.zhuanke.mvp.event.ChooseFragmentEvent;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.HomeMePresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.BusinessHezuoActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyProfitActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyStudentListActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.RankActivity;
import com.dfwr.zhuanke.zhuanke.util.GlideUtil;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.util.UserDataManeger;
import com.dfwr.zhuanke.zhuanke.widget.Systems;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ckx on 2018/7/12.
 * <p>
 * 活动tab
 */

public class MeFragment extends BaseTwoFragment<HomeMeView, HomeMePresent<HomeMeView>> implements HomeMeView {


//    @BindView(R.id.tv_name)
//    TextView tvName;

    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_all_pupil)
    TextView tvAllPupil;
    @BindView(R.id.tv_today_profit)
    TextView tvTodayProfit;
    @BindView(R.id.tv_today_pupil)
    TextView tvTodayPupil;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    @BindView(R.id.tv_id)
    TextView tvId;
//  ll_today_student

    @BindView(R.id.tv_all_profit)
    TextView tvAllProfit;
    @BindView(R.id.tv_all_withdraw)
    TextView tvAllWithdraw;
    @BindView(R.id.ll_today_all_student)
    LinearLayout llTodayAllStudent;
    Unbinder unbinder;


    private List<HomeBean> imagesAndTitles = new ArrayList<>();
    private HomeAdapter taskAdapter;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private MyPagerAdapter mAdapter;

    private int[] taskStatusPics = {R.mipmap.icon_money,
            R.mipmap.icon_withdraw,R.mipmap.icon_rank, R.mipmap.icon_hezuo
    };

    private String[] myStr = {"开始赚钱", "提现", "排行榜",
            "商务合作"};
    private String[] mTitles;
    private Intent intent;
    private ChooseFragmentEvent chooseFragmentEvent;

    public MeFragment() {
    }



    @OnClick({R.id.ll_account, R.id.ll_today_profit, R.id.ll_today_student, R.id.ll_today_all_student})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_account: //点击账户余额，跳转到提现界面

                intent = new Intent(getActivity(), MyProfitActivity.class);
//                intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra(Systems.go_activity_type, Systems.activity_type_withdraw);
                startActivity(intent);
                break;
            case R.id.ll_today_profit:
                chooseFragmentEvent = new ChooseFragmentEvent();
                chooseFragmentEvent.fragmentStr = "3";
                EventBus.getDefault().post(chooseFragmentEvent);
                break;
            case R.id.ll_today_student:
                intent = new Intent(getActivity(), MyStudentListActivity.class);
                intent.putExtra(Systems.my_student_type, "today");
                startActivity(intent);
                break;
            case R.id.ll_today_all_student:
                intent = new Intent(getActivity(), MyStudentListActivity.class);
                intent.putExtra(Systems.my_student_type, "all");
                startActivity(intent);
                break;
        }
    }

//
//    @OnClick({R.id.ll_today_student})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_today_student:
//                Intent intent = new Intent(getActivity(), MyStudentListActivity.class);
//                intent.putExtra(Systems.my_student_type, "today");
//                startActivity(intent);
//                break;
//        }
//    }


    @Override
    public void getUserInfo(UserBaseInfo userBaseInfo) {
        EventBus.getDefault().post(userBaseInfo);
        tvAccount.setText("" + userBaseInfo.getAccount().getBalance());
        tvAllPupil.setText("" + userBaseInfo.getStudentNum());
        tvTodayProfit.setText(userBaseInfo.getTodayProfit() + "");
        tvTodayPupil.setText(userBaseInfo.getTodayStudentNum() + "");
        tvAllProfit.setText(userBaseInfo.getTodayProfit() + "");
//        tvAllWithdraw.setText(userBaseInfo.getAllWithDrawMoney() + "");

        SharedPreferencesUtil.putStringData(getActivity(), SharedPreferencesTool.balance, userBaseInfo.getAccount().getBalance() + "");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_me2;
    }


    @Override
    protected void initView() {
        super.initView();

        UserBean userBean = UserDataManeger.getInstance().getUserBean();

        if (userBean != null) {
            GlideUtil.getInstance().loadHeadImage(getActivity(), ivHead, userBean.getUser().getImgId(), true);
            tvId.setText("ID:" + userBean.getUser().getUid());
        }

        for (int i = 0; i < taskStatusPics.length; i++) {
            HomeBean homeBean = new HomeBean(myStr[i], taskStatusPics[i]);
            imagesAndTitles.add(homeBean);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        taskAdapter = new HomeAdapter(imagesAndTitles);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        ChooseFragmentEvent chooseFragmentEvent = new ChooseFragmentEvent();
                        chooseFragmentEvent.fragmentStr = "1";
                        EventBus.getDefault().post(chooseFragmentEvent);
                        break;
                    case 1:  //跳转到提现界面
                        intent = new Intent(getActivity(), MyProfitActivity.class);
                        intent.putExtra(Systems.go_activity_type, Systems.activity_type_withdraw);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), RankActivity.class));
//                        Intent intent = new Intent(getActivity(), MyWebView.class);
//                        intent.putExtra("url", HttpContants.gonglue);
//                        if (!ButtonUtils.isFastDoubleClick(R.id.item_id)) {
//                            startActivity(intent);
//                        }
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), BusinessHezuoActivity.class));
                        break;
                }

            }
        });
//        initViewPager();
    }


    @Override
    protected void initData() {
        super.initData();
        setData();
    }

    @Override
    protected void fragmentToUserVisible() {
        super.fragmentToUserVisible();
        setData();
    }

    private void setData() {
        mPresent.getUserInfo();
    }


    @Override
    protected HomeMePresent createPresent() {
        return new HomeMePresent<>(this);
    }



    public boolean joinQQGroup() {
        String key = "jPgBTG3gAQVQklkf9xEWSxpkIaO9hcDt";
        Intent intent = new Intent();
//        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtils.showShort("未安装手Q或安装的版本不支持");
            return false;
        }
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }





    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
