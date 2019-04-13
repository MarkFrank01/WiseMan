package com.zxcx.zhizhe.service.version_update.request;


import com.zxcx.zhizhe.service.version_update.entity.VersionInfo;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiService {

    /**
     * 版本检测
     */
    @GET("version.json")
    Observable<Response<VersionInfo>> checkVersion();

}
