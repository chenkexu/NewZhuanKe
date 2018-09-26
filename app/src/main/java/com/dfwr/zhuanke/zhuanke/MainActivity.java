package com.dfwr.zhuanke.zhuanke;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.api.ApiManager;
import com.dfwr.zhuanke.zhuanke.api.ApiManager2;
import com.dfwr.zhuanke.zhuanke.api.BaseObserver;
import com.dfwr.zhuanke.zhuanke.api.HttpContants;
import com.dfwr.zhuanke.zhuanke.api.param.ParamsUtil;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.base.BaseActivity;
import com.dfwr.zhuanke.zhuanke.bean.BannerBean;
import com.dfwr.zhuanke.zhuanke.bean.Propertie;
import com.dfwr.zhuanke.zhuanke.bean.UpdateBean;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.mvp.contract.IMsgView;
import com.dfwr.zhuanke.zhuanke.mvp.event.ChooseFragmentEvent;
import com.dfwr.zhuanke.zhuanke.mvp.presenter.MsgPresent;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.InformationFragment;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.MasterFragment;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.MeFragment;
import com.dfwr.zhuanke.zhuanke.mvp.view.fragment.NewsFragment;
import com.dfwr.zhuanke.zhuanke.util.AppManager;
import com.dfwr.zhuanke.zhuanke.util.CProgressDialogUtils;
import com.dfwr.zhuanke.zhuanke.util.DateTool;
import com.dfwr.zhuanke.zhuanke.util.GsonUtils;
import com.dfwr.zhuanke.zhuanke.util.OkGoUpdateHttpUtil;
import com.dfwr.zhuanke.zhuanke.util.RxUtil;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesTool;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.widget.Dialog.AdvertisementDialog;
import com.orhanobut.logger.Logger;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.listener.ExceptionHandler;
import com.vector.update_app.listener.IUpdateDialogFragmentListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<IMsgView, MsgPresent<IMsgView>> implements IMsgView {


    private static final String TAG = MainActivity.class.getSimpleName();
    private String mUpdateUrl = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json.txt";
    private String mUpdateUrl1 = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json1.txt";
    private boolean isShowDownloadProgress;
    private String mApkFileUrl = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/apk/sample-debug.apk";


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
//        switch (resultCode) {
//            case Activity.RESULT_CANCELED:
//                switch (requestCode){
//                    // 得到通过UpdateDialogFragment默认dialog方式安装，用户取消安装的回调通知，以便用户自己去判断，比如这个更新如果是强制的，但是用户下载之后取消了，在这里发起相应的操作
//                    case AppUpdateUtils.REQ_CODE_INSTALL_APP:
//                        Toast.makeText(this,"用户取消了安装包的更新", Toast.LENGTH_LONG).show();
//                        break;
//                }
//                break;
//            default:
//        }
//    }


    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;

    @BindView(R.id.red_package)
    ImageView red_package;


    private NewsFragment newsFragment;
    private MasterFragment masterFragment;
    private MeFragment meFragment;
    private InformationFragment withDrawFragment;

    private Propertie propertie;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);    //将这一行注释掉，阻止activity保存fragment的状态,解决Fragment穿透重叠现象
    }







    /**
     * 更新APP版本
     * @param
     */
    public void updateDiy() {
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(HttpContants.get_version)
                //全局异常捕获
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色。
                .setThemeColor(0xffffac5d)
                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                    @Override
                    public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                        //用户点击关闭按钮，取消了更新，如果是下载完，用户取消了安装，则可以在 onActivityResult 监听到。

                    }
                })
                //不自动，获取
                .setIgnoreDefParams(true)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        Logger.d("版本更新的json:"+json);
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            UpdateBean updateBean = GsonUtils.parseJsonToBean(json, UpdateBean.class);
                            String versionNum = updateBean.getResult().getVersionNum();
                            versionNum = versionNum.replace(".", "");
                            int versionCode = Integer.parseInt(versionNum);
                            if (AppUtils.getAppVersionCode() < versionCode)  {
                                updateAppBean.setUpdate("Yes");
                            }else{
                                updateAppBean.setUpdate("no");
                            }
                            if (updateBean.getResult().getForceUpdate() == 0) {
                                updateAppBean.setConstraint(false);
                            }else{
                                updateAppBean.setConstraint(true);
                            }
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    //（必须）新版本号，
                                    .setNewVersion(updateBean.getResult().getVersionNum())
                                    //（必须）下载地址
                                    .setApkFileUrl(updateBean.getResult().getLink())
                                    .setUpdateLog("版本更新："+updateBean.getResult().getDesc())
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(updateBean.getResult().getPackageSize()+"M");
                            //是否强制更新，可以不设置
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String error) {
//                        Toast.makeText(MainActivity.this, "没有新版本", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取全局属性
//        getProperties();
        selectedFragment(0);
        tabSelected(llHome);
        getBanner();
        updateDiy();




        //自然日更新新闻已读和未读的状态
        Date date = (Date) SharedPreferencesTool.getInstance().getObjectFromShare(SharedPreferencesTool.current_date);
        if (date!=null) {
            if (DateTool.isSameDate(date,new Date())) {

            }else{
                SharedPreferencesUtil.removeData(this,SharedPreferencesUtil.MESSAGE_ALREADY_LOOKED);
            }
        }
//        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_zoom);
//        red_package.setAnimation(mAnimation );
//        mAnimation.start();

    }


    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chooseFragment(ChooseFragmentEvent chooseStr){
        if (chooseStr.fragmentStr.equals("0")) {
            selectedFragment(0);
            tabSelected(llHome);
        }else if (chooseStr.fragmentStr.equals("1")){
            selectedFragment(1);
            tabSelected(llCategory);
        } else if (chooseStr.fragmentStr.equals("2")){
            selectedFragment(2);
            tabSelected(llService);
        } else if (chooseStr.fragmentStr.equals("3")){
            selectedFragment(3);
            tabSelected(llMine);
        }
    }






    //获取公告
    public void getBanner(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().get_home_img_url(ParamsUtil.getParams(map))
                .compose(RxUtil.<ApiResponse<BannerBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<BannerBean>() {
                    @Override
                    protected void onSuccees(ApiResponse<BannerBean> t) {
                        // TODO: 2018/8/24 接收后台提供的链接
                        if (t.getResult().getVal()!=null && (!t.getResult().getVal().equals(""))) {
                            AdvertisementDialog advertisementDialog = new AdvertisementDialog(MainActivity.this,t.getResult().getVal());
                            advertisementDialog.showDialog();
                        }
                    }
                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
//                        getTest();
                    }
                });
    }










    //获取公告
    public void getTest(){
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager2.getInstence().getApiService().get_home_img_url(ParamsUtil.getParams(map))
                .compose(RxUtil.<ApiResponse<BannerBean>>rxSchedulerHelper())
                .subscribe(new BaseObserver<BannerBean>() {
                    @Override
                    protected void onSuccees(ApiResponse<BannerBean> t) {

                    }
                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showLong("8080也请求失败,异常信息是："+errorInfo);
                    }
                });
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
                            MainActivity.this.propertie = result;
                            selectedFragment(0);
                            tabSelected(llHome);
                        }
                    }
                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        ToastUtils.showShort(errorInfo);
                    }
                });
    }






    @Override
    public void onResume() {
        super.onResume();
        //查询余额
//        mPresent.getUserInfo();
    }




    @OnClick({R.id.ll_home, R.id.ll_category, R.id.ll_service, R.id.ll_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                selectedFragment(0);
                tabSelected(llHome);
                break;
            case R.id.ll_category:
                selectedFragment(1);
                tabSelected(llCategory);
                break;
            case R.id.ll_service:
                selectedFragment(2);
                tabSelected(llService);
                break;
            case R.id.ll_mine:
                selectedFragment(3);
                tabSelected(llMine);
                break;
        }
    }




    public void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.content, meFragment);
                } else
                    transaction.show(meFragment);
                break;
            case 1:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(Systems.propertie,propertie);
//                    newsFragment.setArguments(bundle);
                    transaction.add(R.id.content, newsFragment);
                } else
                    transaction.show(newsFragment);
                break;
            case 2:
                if (masterFragment == null) {
                    masterFragment = new MasterFragment();
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(Systems.propertie,propertie);
//                    masterFragment.setArguments(bundle);
                    transaction.add(R.id.content, masterFragment);
                } else
                    transaction.show(masterFragment);
                break;
            case 3:
                if (withDrawFragment == null) {
                    withDrawFragment = new InformationFragment();
                    transaction.add(R.id.content, withDrawFragment);
                } else
                    transaction.show(withDrawFragment);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
//        if (newsFragment != null)
//            transaction.hide(newsFragment);
//        if (masterFragment != null)
//            transaction.hide(masterFragment);
//        if (withDrawFragment != null)
//            transaction.hide(withDrawFragment);
//        if (meFragment != null)
//            transaction.hide(meFragment);


        if (newsFragment != null && !newsFragment.isHidden()) {
            transaction.hide(newsFragment);
        }
        if (masterFragment != null && !masterFragment.isHidden()) {
            transaction.hide(masterFragment);
        }
        if (withDrawFragment != null && !withDrawFragment.isHidden()) {
            transaction.hide(withDrawFragment);
        }
        if (meFragment != null && !meFragment.isHidden()) {
            transaction.hide(meFragment);
        }
    }

    public void tabSelected(LinearLayout linearLayout) {
        llHome.setSelected(false);
        llCategory.setSelected(false);
        llService.setSelected(false);
        llMine.setSelected(false);
        linearLayout.setSelected(true);
    }


    private long exitTime;
    @Override
    public void onBackPressed() {
        //显示在：发现Fragment
        long nowTime = System.currentTimeMillis();
        if((nowTime - exitTime) <= 2000){
            SharedPreferencesUtil.removeData(this, SharedPreferencesUtil.student_link);
            AppManager.getAppManager().AppExit(this);
        }else{
            ToastUtils.showShort("再按一次退出APP！");
            exitTime = nowTime;
        }
    }



    @Override
    protected MsgPresent<IMsgView> createPresent() {
        return new MsgPresent<>(this);
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
        SharedPreferencesUtil.putStringData(this, SharedPreferencesTool.balance,userBaseInfo.getAccount().getBalance()+"");
    }
}