package com.dfwr.zhuanke.zhuanke.api;

import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huang on 2018/4/16.
 */

public class ApiManager {


    private static final String TAG = "RetrofitConfig";

    //设缓存有效期为1天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static ApiManager mRetrofitManager;
    private Retrofit mRetrofit;
    private ApiService mApiService;
    private ApiManager() {
        initRetrofit();
    }



    public static ApiManager getInstence(){
        if (mRetrofitManager==null){
            synchronized (ApiManager.class) {
                if (mRetrofitManager == null)
                    mRetrofitManager = new ApiManager();
            }
        }
        return mRetrofitManager;
    }

    private final Gson mGson  = new GsonBuilder().setLenient().create();  // 设置GSON的非严格模式setLenient()


    private void initRetrofit() {
        Logger.d("initRetrofit----------");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(getHttpLoggingInterceptor());
        // TODO: 2018/8/14 添加重连次数
        builder.addInterceptor(new Retry(5));

        // TODO: 2018/8/16 添加缓存拦截器 
//        builder.addInterceptor(ApiManager.sRewriteCacheControlInterceptor);
//        builder.addNetworkInterceptor(ApiManager.sRewriteCacheControlInterceptor);
        
        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(HttpContants.HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(client)
                .build() ;
        mApiService = mRetrofit.create(ApiService.class);
    }



    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.d(message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    public ApiService getApiService()  {
        return mApiService;
    }







    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    public  static Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Log.e(TAG, "no network");
            }
            Response originalResponse = chain.proceed(request);

            if (!NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    /**
     * 自定义的，重试N次的拦截器
     * 通过：addInterceptor 设置
     */
    public static class Retry implements Interceptor {
        public int maxRetry;//最大重试次数
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
        public Retry(int maxRetry) {
            this.maxRetry = maxRetry;
        }
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Log.i("Retry","num:"+retryNum);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                Log.i("Retry","num:"+retryNum);
                response = chain.proceed(request);
            }
            return response;
        }
    }







    class NetInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Connection","close").build();
            return chain.proceed(request);
        }
    }



}
