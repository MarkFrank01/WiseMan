package com.zxcx.shitang.retrofit;


import com.zxcx.shitang.App;
import com.zxcx.shitang.BuildConfig;
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


public class AppClient {
    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {

            //设置缓存 10M
            File httpCacheDirectory = new File(FileUtil.getAvailableCacheDir(), "responses");
            Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (BuildConfig.LOG) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(interceptor)
                        .cache(cache)
                        .build();
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(APIService.API_SERVER_URL)
                        .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build();
            } else {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .cache(cache)
                        .build();
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(APIService.API_SERVER_URL)
                        .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build();
            }


        }
        return mRetrofit;
    }

    /**
     * 先判断网络，网络好的时候，移除header后添加haunch失效时间为1小时，网络未连接的情况下设置缓存时间为4周
     */
    public static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if (!Utils.isNetworkReachable(App.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
//                LogCat.i("no network");
            }

            Response response = chain.proceed(request);
//            LogCat.i("response=" + response);

            if (response.code() == 200) {
                if (Utils.isNetworkReachable(App.getContext())) {
                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
//                    LogCat.i("has network maxAge=" + maxAge);
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")////清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
//                    LogCat.i("network error");
                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                    LogCat.i("has maxStale=" + maxStale);
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
//                    LogCat.i("response build maxStale=" + maxStale);
                }
            }

            return response;
        }
    };

}
