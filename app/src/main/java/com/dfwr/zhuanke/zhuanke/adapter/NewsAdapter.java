package com.dfwr.zhuanke.zhuanke.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.bean.Article;
import com.dfwr.zhuanke.zhuanke.util.DateTool;
import com.dfwr.zhuanke.zhuanke.util.GlideUtil;
import com.dfwr.zhuanke.zhuanke.util.SharedPreferencesUtil;
import com.dfwr.zhuanke.zhuanke.util.TimeUtil;

import java.util.List;


/**
 * @author quchao
 * @date 2018/2/24
 */

public class NewsAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
    private double price;

    public void setPrice(String price) {
        double v = Double.parseDouble(price);
        this.price = v * 10;
        notifyDataSetChanged();
    }

    public NewsAdapter(@Nullable List<Article> data) {

        super(R.layout.item_news_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {


        if (!TextUtils.isEmpty(item.getHeadImg())) {
            GlideUtil.getInstance().loadAdImage(mContext, (ImageView) helper.getView(R.id.item_project_list_iv), item.getHeadImg()
                    , true);
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.item_project_list_title_tv, item.getTitle());
        }

        String timeFormatText = TimeUtil.getTimeFormatText(DateTool.getStringToDate(item.getTime(),DateTool.FORMAT_DATE_TIME));

        if (timeFormatText!=null) {
            helper.setText(R.id.tv_time, timeFormatText);
        }


        helper.setText(R.id.tv_watch_sum, item.getClick() + "");
        helper.setText(R.id.tv_price, price + "毛/条");


        if (SharedPreferencesUtil.getStringData(mContext, SharedPreferencesUtil.MESSAGE_ALREADY_LOOKED).contains(item.getAid() + "")) {
            helper.setTextColor(R.id.item_project_list_title_tv, mContext.getResources().getColor(R.color.bg_gray));
        } else {
            helper.setTextColor(R.id.item_project_list_title_tv, mContext.getResources().getColor(R.color.black));
        }

    }
}
