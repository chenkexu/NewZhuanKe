package com.dfwr.zhuanke.zhuanke.widget;

import com.dfwr.zhuanke.zhuanke.util.UIUtils;

import java.io.File;

/**
 * Created by ckx on 2018/7/12.
 */

public final class Systems {


    //mob平台
    public static final String mob_Appkey = "27dba78fdf388";
    public static final String mob_App_Secret = "27dba78fdf388";


    //微信登录
    public static final String wechat_App_id = "wxef35ece47731348d";
    public static final String wechat_App_secret = "b00ff09e07173e377f0738649720a266";


    //QQ登录和分享
    public static final String qq_App_id = "101502417";
    public static final String qq_App_secret = "7de1c1733a81d3998f3b59e1711378eb";


    //徒弟包名
    public static final String app_student_name = "chaoyuehui_";

    //bugly
    public static final String bugly_id = "6bb06490c1";


    //回复内容






//    public static final String VERSION = "7.2.2";








    public static final String VERSION = "7.2.2";

    public static final String UTF_8 = "utf-8";

    public static final String share_host = "share_host";
    public static final String url = "url";
    public static final String feedArticleData = "feedArticleData";
    public static final String articleData = "articleData";

    public static final String USER_INFO = "USER_INFO";



    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String CurrentCircleRadius = "CurrentCircleRadius";
    public static final String mCityName = "mCityName";

    public static final String from_withdraw = "from_withdraw";


    public static final String address = "address";
    public static final String newsType = "newsType";

    public static final String payAccount = "payAccount";
    public static final String payName = "payName";

    public static final String isFirstWithDraw = "isFirstWithDraw";

    public static final String withDrawType = "withDrawType";

    public static final String propertie = "propertie";

    public static final String wechat = "wechat";
    public static final String alipay = "alipay";

    public static final String link = "link";

    public static final String my_student_type = "my_student_type";


    public static final String my_profit_type = "my_profit_type";

    public static final String go_activity_type = "go_activity_type";

    public static final String activity_type_profit = "activity_type_profit";
    public static final String activity_type_withdraw_list = "activity_type_withdraw_list";
    public static final String activity_type_withdraw = "activity_type_withdraw";




    /**
     * Path
     */
    public static final String PATH_DATA = UIUtils.getContext().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    /**
     * Intent params
     */
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM3 = "param3";
    public static final String ARG_PARAM2 = "param2";    /**

     /**
     * Bottom Navigation tab classify
     */
    public static final int TAB_ONE = 0;

}
