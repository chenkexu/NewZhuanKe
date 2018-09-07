package com.dfwr.zhuanke.zhuanke.mvp.presenter;


import com.blankj.utilcode.util.ToastUtils;
import com.dfwr.zhuanke.zhuanke.api.ApiManager;
import com.dfwr.zhuanke.zhuanke.api.BaseObserver;
import com.dfwr.zhuanke.zhuanke.api.param.ParamsUtil;
import com.dfwr.zhuanke.zhuanke.api.response.ApiResponse;
import com.dfwr.zhuanke.zhuanke.base.BasePresenter;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;
import com.dfwr.zhuanke.zhuanke.mvp.contract.IMsgView;
import com.dfwr.zhuanke.zhuanke.util.RxUtil;

import java.util.HashMap;

/**
 * Created by wy on 2017/12/5.
 * 消息
 */

public class MsgPresent<T> extends BasePresenter<IMsgView> {
    private IMsgView mMsgView;

    public MsgPresent(IMsgView mMsgView) {
        this.mMsgView = mMsgView;
    }

    @Override
    public void fecth() {

    }



    public void getUserInfo(){
        mMsgView.showLoading();
        HashMap<String, Object> map = ParamsUtil.getMap();
        ApiManager.getInstence().getApiService().getUserInfo(ParamsUtil.getParams(map))
                .compose(RxUtil.<ApiResponse<UserBaseInfo>>rxSchedulerHelper())
                .subscribe(new BaseObserver<UserBaseInfo>() {
                    @Override
                    protected void onSuccees(ApiResponse<UserBaseInfo> t) {
                        mMsgView.hideLoading();
                        if (t!=null) {
                            mMsgView.getUserInfo(t.getResult());
                        }
                    }

                    @Override
                    protected void onFailure(String errorInfo, boolean isNetWorkError) {
                        mMsgView.hideLoading();
                        ToastUtils.showShort(errorInfo);
                    }
                });

    }









}
