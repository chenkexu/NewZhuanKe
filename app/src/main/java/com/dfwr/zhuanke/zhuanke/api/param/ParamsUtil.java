package com.dfwr.zhuanke.zhuanke.api.param;

import java.util.HashMap;

/**
 * 请求参数拼接
 * Created by shixhe on 2017/10/30.
 */

public class ParamsUtil {

    private static MDBasicRequestMap mdBasicRequestMap;


    public static HashMap<String,Object>  getMap(){
        mdBasicRequestMap = new MDBasicRequestMap();
        return mdBasicRequestMap;
    }


    public  static  HashMap<String,Object> getParams(HashMap<String,Object> params){
        MDSecureRequestMap map = new MDSecureRequestMap(params);
        return map;
    }


    

}
