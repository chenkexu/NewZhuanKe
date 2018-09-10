package com.dfwr.zhuanke.zhuanke.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.bean.WithDrawHistory;
import com.dfwr.zhuanke.zhuanke.util.DateTool;

import java.util.List;


/**
 * @author quchao
 * @date 2018/2/24
 */

public class WithDrawHistoryAdapter extends BaseQuickAdapter<WithDrawHistory, BaseViewHolder> {



    public WithDrawHistoryAdapter(@Nullable List<WithDrawHistory> data) {
        super(R.layout.item_make_money, data);
    }




    @Override
    protected void convert(BaseViewHolder helper, WithDrawHistory item) {
        int status = item.getStatus();
        switch (status) {
            case 1:
                helper.setText(R.id.tv_name, helper.getLayoutPosition()+1+".已提现");
                break;
            case 2:
                helper.setText(R.id.tv_name, helper.getLayoutPosition()+1+".已提现");
                break;
            case 3:
                helper.setText(R.id.tv_name, helper.getLayoutPosition()+1+".已结算");
                break;
            case 4:
                helper.setText(R.id.tv_name, helper.getLayoutPosition()+1+".提现失败");
                break;

        }
        String date = DateTool.stampToDate(item.getCreateDate());
        helper.setText(R.id.tv_date,date+"");
        helper.setText(R.id.tv_money, "+"+item.getMoney()+"元");

    }
}
