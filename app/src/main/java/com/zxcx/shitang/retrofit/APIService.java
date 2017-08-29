package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.ui.card.cardBag.CardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginBean;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterBean;
import com.zxcx.shitang.ui.search.result.SearchCardBagBean;
import com.zxcx.shitang.ui.search.result.SearchCardBean;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    String API_SERVER_URL = "http://120.77.180.183:8043/";

    @POST("user/PhoneRegistered")
    Flowable<BaseBean<RegisterBean>> phoneRegistered(
            @Query("phoneNumber") String phone,@Query("SMSCode") String code,
            @Query("password") String password, @Query("appType") int appType,
            @Query("appChannel") String appChannel,@Query("appVersion") String appVersion);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<LoginBean>> phoneLogin(
            @Query("phoneNumber") String phone, @Query("password") String password,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<HotCardBean>> getHotCard(int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<HotCardBagBean>> getHotCardBag(int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<HotCardBean>> getAttentionCard(int userId, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<HotCardBagBean>> getAttentionCardBag(int userId, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<String>> getSearchHot();

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<SearchCardBean>> searchCard(String keyword, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<SearchCardBagBean>> searchCardBag(String keyword, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<CardBagBean>> getCardBagCardList(int id, int page, int pageSize);
}
