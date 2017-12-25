package com.zxcx.zhizhe.retrofit;


import com.readystatesoftware.chuck.ChuckInterceptor;
import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.TimeStampMD5andKL;
import com.zxcx.zhizhe.utils.Utils;

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
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(interceptor)
                    .addInterceptor(new ChuckInterceptor(App.getContext()))
                    .cache(cache)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIService.API_SERVER_URL)
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
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

    private static class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuilder mHeaderMessage = new StringBuilder();
        private StringBuilder mBodyMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mHeaderMessage.setLength(0);
                mHeaderMessage.append(message);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))) {
                mBodyMessage.setLength(0);
                mBodyMessage.append(message);
            }else {
                mHeaderMessage.append(message.concat("\n"));
            }
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                LogCat.d(mHeaderMessage.toString());
                LogCat.json(mBodyMessage.toString());
                mHeaderMessage.setLength(0);
                mBodyMessage.setLength(0);
            }
        }
    }

}
