package com.zxcx.shitang.retrofit;


import com.zxcx.shitang.App;
import com.zxcx.shitang.BuildConfig;
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.TimeStampMD5andKL;
import com.zxcx.shitang.utils.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


public class AppClient {
    private static APIService sAPIService;

    public static APIService getAPIService() {
        if (sAPIService == null) {

            //设置缓存 10M
            File httpCacheDirectory = new File(FileUtil.getAvailableCacheDir(), "responses");
            Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            Retrofit retrofit;
            if (BuildConfig.LOG) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(interceptor)
                        .cache(cache)
                        .build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.API_SERVER_URL)
                        .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build();
            } else {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .cache(cache)
                        .build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.API_SERVER_URL)
                        .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build();
            }
            sAPIService = retrofit.create(APIService.class);
        }
        return sAPIService;
    }

    /**
     * 给所有请求加上token和timeStamp
     */
    public static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            long localTimeStamp = SharedPreferencesUtil.getLong(SVTSConstants.localTimeStamp, 0);
            long serverTimeStamp = SharedPreferencesUtil.getLong(SVTSConstants.serverTimeStamp, 0);
            String token = SharedPreferencesUtil.getString(SVTSConstants.token,"");
            String timeStamp = TimeStampMD5andKL.JiamiByMiYue(localTimeStamp,serverTimeStamp);

            Request request = chain.request();
            if (!Utils.isNetworkReachable(App.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
//                LogCat.i("no network");
            }

            // 添加新的参数
            HttpUrl.Builder urlBuilder = request.url()
                    .newBuilder()
                    .scheme(request.url().scheme())
                    .host(request.url().host())
                    .addQueryParameter("timeStamp", timeStamp)
                    .addQueryParameter("token", token);

            // 新的请求
            Request newRequest = request.newBuilder()
                    .method(request.method(), request.body())
                    .url(urlBuilder.build())
                    .build();

            return chain.proceed(newRequest);
        }
    };

}
