package com.dfwr.zhuanke.zhuanke.mvp.view.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.adapter.HomeAdapter;
import com.dfwr.zhuanke.zhuanke.api.ApiManager;
import com.dfwr.zhuanke.zhuanke.api.BaseObserver;
import com.dfwr.zhuanke.zhuanke.api.param.ParamsUtil;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.base.BaseTwoFragment;
import com.dfwr.zhuanke.zhuanke.bean.HomeBean;
import com.dfwr.zhuanke.zhuanke.bean.Propertie;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.mvp.contract.IHomeView;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.HomePresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.LoginActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyCodeActivity;
import com.dfwr.zhuanke.zhuanke.mvp.view.activity.MyStudentListActivity;
import com.dfwr.zhuanke.zhuanke.util.RxUtil;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.Dialog.ShareDialog;
import com.dfwr.zhuanke.zhuanke.widget.Systems;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dfwr.zhuanke.zhuanke.widget.Systems.link;

/**
 * Created by ckx on 2018/7/12.
 */
public class MasterFragment extends BaseTwoFragment<IHomeView,HomePresent<IHomeView>> implements IHomeView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_today_account)
    TextView tvTodayStudentNum;
    @BindView(R.id.tv_all_pupil)
    TextView tvAllPupil;
    @BindView(R.id.tv_today_pupil)
    TextView tvTodayStudentProfit;
    @BindView(R.id.tv_all__pupil_account)
    TextView tvAllStudentPofit;
    @BindView(R.id.tv_master_reward)
    TextView tvMasterReWard;
    @BindView(R.id.tv_master_tips)
    TextView tvMasterTipsStr;

    @BindView(R.id.ll_all_student)
    LinearLayout linearLayout;




    private  String imageUrl = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png";

    private List<HomeBean> imagesAndTitles = new ArrayList<>();
    private HomeAdapter taskAdapter;


    private int[] taskStatusPics = {R.mipmap.wechat_withdraw, R.mipmap.qq_withdraw,
            R.mipmap.code_withdraw, R.mipmap.link_withdraw
    };


    private String[] myStr = {"微信收徒", "QQ收徒", "二维码收徒", "链接收徒"};
    private Propertie propertie;
    private String studentLink;
    private ShareDialog shareDialog;
    private String type;




    @Override
    protected int setLayoutId() {
        return R.layout.fragment_master;
    }




    @OnClick({R.id.ll_today_student, R.id.ll_all_student,R.id.ll_today})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), MyStudentListActivity.class);
        switch (view.getId()) {
            case R.id.ll_today_student:
                intent.putExtra(Systems.my_student_type, "today");
                break;
            case R.id.ll_all_student:
                intent.putExtra(Systems.my_student_type, "all");
                break;
            case R.id.ll_today:
                intent.setClass(getActivity(), LoginActivity.class);
                break;
        }
        startActivity(intent);
    }





    @Override
    protected void fragmentToUserVisible() {
        super.fragmentToUserVisible();
        getUserData();
    }




    public void getUserData() {
        SharedPreferencesUtil.removeData(getActivity(), SharedPreferencesUtil.student_link);
//        Bundle arguments = getArguments();
//        propertie = (Propertie) arguments.getSerializable(Systems.propertie);
        mPresent.getUserInfo();
    }







    @Override
    protected void initData() {
        super.initData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getUserData();
            }
        });

        for (int i = 0; i < taskStatusPics.length; i++) {
            HomeBean homeBean = new HomeBean(myStr[i], taskStatusPics[i]);
            imagesAndTitles.add(homeBean);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        taskAdapter = new HomeAdapter(imagesAndTitles);
        recyclerView.setAdapter(taskAdapter);
        getUserData();



        taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                studentLink = SharedPreferencesUtil.getStringData(getActivity(), SharedPreferencesUtil.student_link);
                switch (position){
                    case 0:
                        type = "0";
                        break;
                    case 1:
                        type = "1";
                        break;
                    case 2:
                        type = "2";
                        break;
                    case 3:
                        type = "3";
                        break;
                }

                if (TextUtils.isEmpty(studentLink)) {
                    mPresent.getStudentLink();
                }else{
                    chooseShareWay(type);
                }
            }
        });
        getProperties();
//        initPrice();
    }







    @Override
    public void getStudentLink(String link) {
        Logger.d("接收到的studentLink是："+link);
        studentLink = link;
        SharedPreferencesUtil.putStringData(getActivity(),SharedPreferencesUtil.student_link,link);
        chooseShareWay(type);
    }



    private void chooseShareWay(String type){
        switch (type){
            case "0":
                shareDialog = new ShareDialog(getActivity());
                shareDialog.setCode(studentLink);
                shareDialog.setWeChat();
                shareDialog.show();
                break;
            case "1":
                shareDialog = new ShareDialog(getActivity());
                shareDialog.setCode(studentLink);
                shareDialog.setQQ();
                shareDialog.show();
                break;
            case "2":   //二维码收徒
                Intent intent = new Intent(getActivity(), MyCodeActivity.class);
                intent.putExtra(Systems.propertie,propertie);
                intent.putExtra(link,studentLink);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case "3":
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(studentLink);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("徒弟链接已复制，快打开浏览器访问吧!")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
        }
    }







    //获取各个单价
    public void getProperties(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getProperties(ParamsUtil.getParams(map))
                .compose(RxUtil.<ApiResponse<Propertie>>rxSchedulerHelper())
                .subscribe(new BaseObserver<Propertie>() {
                    @Override
                    protected void onSuccees(ApiResponse<Propertie> t) {
                        if (t!=null) {
                            Propertie result = t.getResult();
                            propertie = result;
                            initPrice(result);
                        }
                    }
                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });
    }







    private void initPrice(Propertie propertie) {
        if (propertie!=null) {
            String masterPrice = propertie.getStudent_reward_n_to_teacher();
            String strIntegration = getResources().getString(R.string.master_reward_tips, masterPrice);
            tvMasterReWard.setText(Html.fromHtml(strIntegration));


            String student_get_balance_to_teacher = propertie.getStudent_get_balance_to_teacher();
            String register_reward = propertie.getRegister_reward();
            String student_reward_to_teacher = propertie.getStudent_reward_to_teacher();

            int masterwithDrawProfit = Integer.parseInt(student_get_balance_to_teacher) + Integer.parseInt(register_reward) + Integer.parseInt(student_reward_to_teacher);


            String pricePercent = "40%";
            String string = getResources().getString(R.string.master_profit_tips, masterwithDrawProfit-1, pricePercent);
            tvMasterTipsStr.setText(Html.fromHtml(string));
        }
    }


    @Override
    protected HomePresent<IHomeView> createPresent() {
        return new HomePresent<>(this);
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
        tvAllPupil.setText(userBaseInfo.getStudentNum()+"");
        tvTodayStudentNum.setText(userBaseInfo.getTodayStudentNum()+"");
        tvTodayStudentProfit.setText(userBaseInfo.getTodayStudentPofit()+"");
        tvAllStudentPofit.setText(userBaseInfo.getStudentPofit()+"");
        SharedPreferencesUtil.putStringData(getActivity(), SharedPreferencesTool.balance,userBaseInfo.getAccount().getBalance()+"");
    }

}
