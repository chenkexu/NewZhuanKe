package com.dfwr.zhuanke.zhuanke.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfwr.zhuanke.zhuanke.R;

import butterknife.ButterKnife;



public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    private BaseActivity mActivity;
    private View mLayoutView;
    protected LayoutInflater inflater;
    protected T mPresent;
    private Dialog progressDialog;

    /**
     * 初始化布局
     * @return 布局文件的id。
     */
    public abstract int getLayoutRes();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 如果Fragment创建需要数据，在这里接收传进来的数据。
     * 如果没有这个抽象方法就空实现。
     */
    protected abstract void managerArguments();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //20160727 修复该方法多次调用 bug
        if (mLayoutView != null) {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null) {
                parent.removeView(mLayoutView);
            }
        } else {
            mLayoutView = getCreateView(inflater, container);
            ButterKnife.bind(this, mLayoutView);
            initView();     //初始化布局
        }

        return mLayoutView;
    }



    /** Fragment当前状态是否可见 */

    protected boolean isVisible;



    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }





    /** 可见 */
    protected void onVisible() {
        lazyLoad();
    }


    /**不可见*/
    protected void onInvisible() {

    }


    /**延迟加载    子类必须重写此方法 */
    protected abstract void lazyLoad();




















    /**
     * 获取Fragment布局文件的View
     *
     * @param inflater
     * @param container
     * @return
     */
    private View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }





    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            managerArguments();
        }
        mPresent = createPresent();
        mPresent.attachView((V) this);
    }

    /**
     * 构建具体的Presenter
     * @return
     */
    protected abstract T createPresent();

    @Override
    public void onDestroy() {
        mPresent.detach();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }



    /**
     * 显示默认的进度条
     */
    protected void showDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        } else {
            progressDialog = null;
        }

        progressDialog = new Dialog(mActivity, R.style.loadingDialog);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.loading, null);
//        View view = LayoutInflater.from(baseActivity).inflate(R.layout.content__roll_loading, null);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 隐藏默认的进度条
     */
    protected void hideDefaultLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
