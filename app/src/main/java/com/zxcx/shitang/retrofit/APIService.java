package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsBean;
import com.zxcx.shitang.ui.card.cardBag.CardBagBean;
import com.zxcx.shitang.ui.classify.ClassifyBean;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginBean;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterBean;
import com.zxcx.shitang.ui.my.collect.collectCard.CollectCardBean;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.shitang.ui.my.selectAttention.SelectAttentionBean;
import com.zxcx.shitang.ui.search.result.SearchCardBagBean;
import com.zxcx.shitang.ui.search.result.SearchCardBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    String API_SERVER_URL = "http://120.77.180.183:8043";

    @POST("/user/PhoneRegistered")
    Flowable<BaseBean<RegisterBean>> phoneRegistered(
            @Query("phoneNumber") String phone,@Query("SMSCode") String code,
            @Query("password") String password, @Query("appType") int appType,
            @Query("appChannel") String appChannel,@Query("appVersion") String appVersion);

    @POST("/user/PhoneLogin")
    Flowable<BaseBean<LoginBean>> phoneLogin(
            @Query("phoneNumber") String phone, @Query("password") String password,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    @POST("/article/getRecommendArticle")
    Flowable<BaseArrayBean<HotCardBean>> getHotCard(@Query("pageIndex") int page, @Query("pageSize") int pageSize);

    @POST("/collection/getRecommendCollection")
    Flowable<BaseArrayBean<HotCardBagBean>> getHotCardBag();

    @POST("/article/getFollowArticle")
    Flowable<BaseArrayBean<HotCardBean>> getAttentionCard(@Query("pageIndex") int page, @Query("pageSize") int pageSize);

    @POST("/collection/getFollowCollection")
    Flowable<BaseArrayBean<HotCardBagBean>> getAttentionCardBag();

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<String>> getSearchHot();

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<SearchCardBean>> searchCard(String keyword, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<SearchCardBagBean>> searchCardBag(String keyword, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<CardBagBean>> getCardBagCardList(int id, int page, int pageSize);

    @POST("/classify/getAllClassify")
    Flowable<BaseArrayBean<ClassifyBean>> getClassify();

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<CollectFolderBean>> getCollectFolder(int userId, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> deleteCollectFolder(int userId, List<Integer> idList);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> addCollectFolder(int userId, String name);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> changeCollectFolderName(int userId, int id, String name);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<CollectCardBean>> getCollectCard(int userId, int id, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> addCollectCard(int userId, int folderId, int cardId);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> deleteCollectCard(int userId, int id, List<Integer> idList);

    @POST("user/PhoneLogin")
    Flowable<BaseArrayBean<SelectAttentionBean>> getAttentionList(int userId, int page, int pageSize);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> changeAttentionList(int userId, List<Integer> idList);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> changeUserInfo(int userId, String userIcon, String name, Integer sex, String birthday);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<CardDetailsBean>> getCardDetails(int userId, int cardId);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> likeCard(int userId, int cardId);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> removeCollectCard(int userId, int cardId);

    @POST("user/PhoneLogin")
    Flowable<BaseBean<PostBean>> feedback(int userId, String content);
}
