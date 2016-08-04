package com.zhj.retrofitwithrxjavademo.http;

import android.content.Context;
import android.util.Log;

import com.zhj.retrofitwithrxjavademo.entity.HttpResult;
import com.zhj.retrofitwithrxjavademo.util.APIException;
import com.zhj.retrofitwithrxjavademo.util.AppUtil;
import com.zhj.retrofitwithrxjavademo.util.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjzhang on 2016/7/26.
 */
public class RetrofitHttpUtil {
    /**
     * 服务器地址
     */
    private static final String BASE_URL = "http://app.aishangh.com/Id_Index.asmx/";

    public APIService apiService;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private Context mContext;

    //缓存设置0不缓存
    private boolean isUseCache;
    private int maxCacheTime = 60;
    public void setMaxCacheTime(int maxCacheTime) {
        this.maxCacheTime = maxCacheTime;
    }

    public void setUseCache(boolean useCache) {
        isUseCache = useCache;
    }

    public APIService getService() {
        if (apiService == null && retrofit != null) {
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

    public void init(Context context) {
        this.mContext = context;
        initOkHttp();
        initRetrofit();
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //打印请求log日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(AppUtil.getCacheDir(mContext), "httpCache");
//        Log.d("OkHttp", "缓存目录---" + cacheFile.getAbsolutePath());
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtil.isNetworkConnected(mContext)||isUseCache) {//如果网络不可用或者设置只用网络
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
//                    Log.d("OkHttp", "网络不可用请求拦截");
                } else if(AppUtil.isNetworkConnected(mContext)&&!isUseCache){//网络可用
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
//                    Log.d("OkHttp", "网络可用请求拦截");
                }
                Response response = chain.proceed(request);
                if (AppUtil.isNetworkConnected(mContext)) {//如果网络可用
//                    Log.d("OkHttp", "网络可用响应拦截");
                    response = response.newBuilder()
                            //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                            .header("Cache-Control", "public,max-age="+maxCacheTime)
                            .removeHeader("Pragma")
                            .build();
                } else {
//                    Log.d("OkHttp","网络不可用响应拦截");
//                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                    response= response.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                            .removeHeader("Pragma")
//                            .build();
                }
                return response;

            }
        };
        builder.cache(cache);
        builder.interceptors().add(cacheInterceptor);//添加本地缓存拦截器，用来拦截本地缓存
        builder.networkInterceptors().add(cacheInterceptor);//添加网络拦截器，用来拦截网络数据
        //设置头部
//        Interceptor headerInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request.Builder requestBuilder = originalRequest.newBuilder()
//                        .header("myhead", "myhead")
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "application/json")
//                        .method(originalRequest.method(), originalRequest.body());
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        };
//        builder.addInterceptor(headerInterceptor );
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();

    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new APIException(httpResult.code, httpResult.desc);
            }
            return httpResult.content;
        }
    }
}
