package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.card.card.cardBagCardDetails.CardBagCardDetailsBean;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsBean;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagBean;
import com.zxcx.zhizhe.ui.classify.ClassifyBean;
import com.zxcx.zhizhe.ui.home.hot.HotBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.ui.home.rank.UserRankBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.my.collect.collectCard.CollectCardBean;
import com.zxcx.zhizhe.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionBean;
import com.zxcx.zhizhe.ui.my.userInfo.OSSTokenBean;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;
import com.zxcx.zhizhe.ui.search.result.card.SearchCardBean;
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean;
import com.zxcx.zhizhe.ui.search.search.SearchBean;
import com.zxcx.zhizhe.ui.welcome.WelcomeBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

//    String API_SERVER_URL = "http://120.77.180.183:8043";
//    String API_SERVER_URL = "http://119.23.18.65:8043";
//    String API_SERVER_URL = "http://192.168.1.102:8043";
    String API_SERVER_URL = App.getContext().getString(R.string.base_url);

    /**
     * 注册
     */
    @POST("/user/PhoneRegistered")
    Flowable<BaseBean<LoginBean>> phoneRegistered(
            @Query("phoneNumber") String phone,@Query("SMSCode") String code,
            @Query("password") String password, @Query("appType") int appType,
            @Query("appChannel") String appChannel,@Query("appVersion") String appVersion);

    /**
     * 登录
     */
    @POST("/user/PhoneLogin")
    Flowable<BaseBean<LoginBean>> phoneLogin(
            @Query("phoneNumber") String phone, @Query("password") String password,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    /**
     * 第三方注册
     */
    @POST("/user/thirdPartyRegistered")
    Flowable<BaseBean<LoginBean>> channelRegister(
            @Query("thirdPartyType") int channelType, @Query("uuid") String openId,
            @Query("password") String password, @Query("avatar") String userIcon,
            @Query("name") String name, @Query("gender") Integer sex,
            @Query("birth") String birthday, @Query("phoneNumber") String phone,
            @Query("SMSCode") String code, @Query("appType") int appType,
            @Query("appChannel") String appChannel, @Query("appVersion") String appVersion);

    /**
     * 第三方登录
     */
    @POST("/user/thirdPartyLogin")
    Flowable<BaseBean<LoginBean>> channelLogin(
            @Query("thirdPartyType") int channelType, @Query("uuid") String openId,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    /**
     * 获取绑定状态列表
     */
    @POST("/user/getBandingInfo")
    Flowable<BaseBean<UserInfoBean>> getBandingInfo();

    /**
     * 第三方绑定解绑
     */
    @POST("/user/thirdPartyBindingUnbinding")
    Flowable<BaseBean> channelBinding(
            @Query("thirdPartyType") int channelType, @Query("type") int type,
            @Query("openId") String openId);

    /**
     * 忘记密码
     */
    @POST("/user/ChangePassword")
    Flowable<BaseBean> forgetPassword(
            @Query("phoneNumber") String phone, @Query("SMSCode") String code, @Query("password") String password,
            @Query("appType") int appType);

    /**
     * 修改密码
     */
    @POST("/user/modifyPassword")
    Flowable<BaseBean> changePassword(
            @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    /**
     * 修改用户信息
     */
    @POST("/user/modifyProfile")
    Flowable<BaseBean<UserInfoBean>> changeUserInfo(
            @Query("avatar") String userIcon, @Query("name") String name,
            @Query("gender") Integer sex, @Query("birth") String birthday);

    /**
     * 获取推荐
     */
    @POST("/article/getRecommendArticle")
    Flowable<BaseBean<HotBean>> getHot(
            @Query("pageIndex") int page);

    /**
     * 获取关注卡片
     */
    @POST("/article/getFollowArticle")
    Flowable<BaseArrayBean<HotCardBean>> getAttentionCard(
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取我的榜单信息
     */
    @POST("/getMyRank")
    Flowable<BaseBean<UserRankBean>> getMyRank();

    /**
     * 获取前十榜单用户
     */
    @POST("/getTopTenRank")
    Flowable<BaseArrayBean<UserRankBean>> getTopTenRank();

    /**
     * 获取前百榜单用户
     */
    @POST("/getTopHundredRank")
    Flowable<BaseArrayBean<UserRankBean>> getTopHundredRank(
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取热门搜索关键词
     */
    @POST("/search/getSearchKeyword")
    Flowable<BaseArrayBean<SearchBean>> getSearchHot();

    /**
     * 获取搜索预选项
     */
    @POST("/search/getSearchAssociate")
    Flowable<BaseArrayBean<String>> getSearchPre(@Query("keyword") String keyword);

    /**
     * 搜索卡片
     */
    @POST("/search/searchArticle")
    Flowable<BaseArrayBean<SearchCardBean>> searchCard(
            @Query("keyword") String keyword, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 搜索用户
     */
    @POST("/search/searchUsers")
    Flowable<BaseArrayBean<SearchUserBean>> searchUser(
            @Query("keyword") String keyword, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 获取卡包内卡片列表
     */
    @POST("/article/getArticleByCollectionId")
    Flowable<BaseArrayBean<CardBagBean>> getCardBagCardList(
            @Query("collectionId") int id, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 获取全部分类
     */
    @POST("/classify/getAllClassify")
    Flowable<BaseArrayBean<ClassifyBean>> getClassify();

    /**
     * 获取收藏夹列表
     */
    @POST("/favorite/getFavoriteList")
    Flowable<BaseArrayBean<CollectFolderBean>> getCollectFolder(
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);
    /**
     * 删除收藏夹
     */
    @POST("/favorite/deleteCollection")
    Flowable<BaseBean> deleteCollectFolder(
            @Query("collectionList") List<Integer> idList);

    /**
     * 添加收藏夹
     */
    @POST("/favorite/addFavoriteController")
    Flowable<BaseBean> addCollectFolder(@Query("collectionTitle") String name);

    /**
     * 修改收藏夹名称
     */
    @POST("/favorite/modifyCollectionName")
    Flowable<BaseBean> changeCollectFolderName(
            @Query("collectionId") int id, @Query("collectionTitle") String name);

    /**
     * 获取收藏夹内卡片列表
     */
    @POST("/favorite/getFavoriteArticleList")
    Flowable<BaseArrayBean<CollectCardBean>> getCollectCard(
            @Query("collectionId") int id, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 添加收藏卡片
     */
    @POST("/favorite/collectArticle")
    Flowable<BaseBean> addCollectCard(
            @Query("collectionId") int folderId, @Query("articleId") int cardId);

    /**
     * 删除收藏卡片
     */
    @POST("/favorite/uncollectArticle")
    Flowable<BaseBean> deleteCollectCard(
            @Query("collectionId") int id, @Query("articleIdList") List<Integer> idList);

    /**
     * 获取兴趣列表
     */
    @POST("/collection/getInterestedCollection")
    Flowable<BaseArrayBean<SelectAttentionBean>> getAttentionList();

    /**
     * 修改兴趣选择列表
     */
    @POST("/collection/followCollection")
    Flowable<BaseBean> changeAttentionList(
            @Query("collectionList") List<Integer> idList);

    /**
     * 获取卡片详情
     */
    @POST("/article/getArticleBasicInfo")
    Flowable<BaseBean<CardDetailsBean>> getCardDetails(@Query("articleId") int cardId);

    /**
     *点赞卡片
     */
    @POST("/article/setLikeForArticle")
    Flowable<BaseBean<CardDetailsBean>> likeCard(@Query("articleId") int cardId);

    /**
     *取消点赞卡片
     */
    @POST("/article/unLikeForArticle")
    Flowable<BaseBean<CardDetailsBean>> removeLikeCard(@Query("articleId") int cardId);

    /**
     *不赞同卡片
     */
    @POST("/article/setDisagreeForArticle")
    Flowable<BaseBean<CardDetailsBean>> unLikeCard(@Query("articleId") int cardId);

    /**
     *取消不赞同卡片
     */
    @POST("/article/unDisagreeForArticle")
    Flowable<BaseBean<CardDetailsBean>> removeUnLikeCard(@Query("articleId") int cardId);

    /**
     *取消收藏卡片
     */
    @POST("/favorite/uncollectSingleArticle")
    Flowable<BaseBean> removeCollectCard(@Query("articleId") int cardId);

    /**
     *提交反馈
     */
    @POST("/feedback/sumbitFeedbadk")
    Flowable<BaseBean> feedback(
            @Query("content") String content,@Query("contact") String contact,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    /**
     *OSS秘钥获取
     */
    @POST("/ossAuth")
    Flowable<BaseBean<OSSTokenBean>> getOSS(@Query("uuid") String uuid);

    /**
     *获取广告
     */
    @POST("/ad/getAdByAdNum")
    Flowable<BaseArrayBean<WelcomeBean>> getAD(@Query("adNum") String adNum);

    /**
     *获取广告
     */
    @POST("/collection/getAllArticleIdByCollectionId")
    Flowable<BaseArrayBean<CardBagCardDetailsBean>> getAllCardId(@Query("collectionId") int cardBagId);
}
