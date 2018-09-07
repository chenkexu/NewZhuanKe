package com.dfwr.zhuanke.zhuanke.mvp.contract;


import com.dfwr.zhuanke.zhuanke.base.BaseView;
import com.dfwr.zhuanke.zhuanke.bean.UserBaseInfo;

/**
 * Created by wy on 2017/12/10.
 */

public interface IMsgView extends BaseView {
    void getUserInfo(UserBaseInfo objec);
}
