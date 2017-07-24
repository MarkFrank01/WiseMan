package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.ui.loginAndRegister.login.LoginBean;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterBean;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    String API_SERVER_URL = "http://120.77.180.183:8043/";

    @POST("/user/PhoneRegistered")
    Flowable<BaseBean<RegisterBean>> phoneRegistered(
            @Query("phoneNumber") String phone,@Query("SMSCode") String code,
            @Query("password") String password, @Query("appType") String appType,
            @Query("appChannel") String appChannel,@Query("appVersion") String appVersion);

    @POST("/user/PhoneLogin")
    Flowable<BaseBean<LoginBean>> phoneLogin(
            @Query("phoneNumber") String phone, @Query("password") String password,
            @Query("appType") String appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);
}
