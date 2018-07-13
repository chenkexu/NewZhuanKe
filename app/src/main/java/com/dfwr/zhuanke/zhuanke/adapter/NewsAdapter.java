package com.dfwr.zhuanke.zhuanke.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dfwr.zhuanke.zhuanke.R;
import com.dfwr.zhuanke.zhuanke.bean.FeedArticleData;
import com.dfwr.zhuanke.zhuanke.util.GlideUtil;

import java.util.List;


/**
 * @author quchao
 * @date 2018/2/24
 */

public class NewsAdapter extends BaseQuickAdapter<FeedArticleData, BaseViewHolder> {

    public NewsAdapter(@Nullable List<FeedArticleData> data) {
        super(R.layout.item_news_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedArticleData item) {
        if (!TextUtils.isEmpty(item.getEnvelopePic())) {
            GlideUtil.getInstance().loadAdImage(mContext, (ImageView) helper.getView(R.id.item_project_list_iv),item.getEnvelopePic()
            ,true);
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.item_project_list_title_tv, item.getTitle());
        }

        helper.setText(R.id.tv_price, "点击分享赚1.5毛");
        helper.setText(R.id.tv_watch_sum, "10293");
    }


}
