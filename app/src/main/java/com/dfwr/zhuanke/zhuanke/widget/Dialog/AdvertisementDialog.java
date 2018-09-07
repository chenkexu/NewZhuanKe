package com.dfwr.zhuanke.zhuanke.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dfwr.zhuanke.zhuanke.R;

public class AdvertisementDialog extends Dialog implements View.OnClickListener {
    private  Context context;
    private  String imageUrl = "";


    public AdvertisementDialog(Context context,String imageUrl) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.imageUrl = imageUrl;
        setContentView(R.layout.view_dialog_advertisement);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(true);
    }

    public void showDialog() {
        ImageView imageView = findViewById(R.id.iv_advertisement);
        ImageView iv_close = findViewById(R.id.iv_close);
        Glide.with(context)
                .load(imageUrl)
                .crossFade()
                .placeholder(R.mipmap.dialog_error)
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存策略
                .into(imageView);

        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        //设置Dialog背景色

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        iv_close.setVisibility(View.VISIBLE);
        show();
        findViewById(R.id.iv_advertisement).setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        dismiss();
    }
}