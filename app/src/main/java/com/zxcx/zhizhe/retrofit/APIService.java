package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsBean;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagBean;
import com.zxcx.zhizhe.ui.classify.ClassifyBean;
import com.zxcx.zhizhe.ui.home.hot.HotBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.ui.home.rank.UserRankBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;
import com.zxcx.zhizhe.ui.my.RedPointBean;
import com.zxcx.zhizhe.ui.my.collect.CollectCardBean;
import com.zxcx.zhizhe.ui.my.creation.rejectDetails.RejectDetailsBean;
import com.zxcx.zhizhe.ui.my.likeCards.LikeCardsBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.DynamicMessageBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList.DynamicMessageListBean;
import com.zxcx.zhizhe.ui.my.message.system.SystemMessageBean;
import com.zxcx.zhizhe.ui.my.note.noteDetails.NoteDetailsBean;
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsBean;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionBean;
import com.zxcx.zhizhe.ui.my.setting.MessageModeBean;
import com.zxcx.zhizhe.ui.my.userInfo.OSSTokenBean;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;
import com.zxcx.zhizhe.ui.otherUser.OtherUserInfoBean;
import com.zxcx.zhizhe.ui.search.result.card.CreationBean;
import com.zxcx.zhizhe.ui.search.result.card.FollowUserBean;
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean;
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
     * 获取手机号注册状态
     * 600:未注册
     * 700:已注册
     */
    @POST("/user/isPhoneAlreadyBanding")
    Flowable<BaseBean> checkPhoneRegistered(@Query("phoneNumber") String phone);

    /**
     * 验证短信验证码
     */
    @POST("/user/smsCodeVerification")
    Flowable<BaseBean<SMSCodeVerificationBean>> smsCodeVerification(
            @Query("phoneNumber") String phone,@Query("SMSCode") String code);

    /**
     * 注册
     */
    @POST("/user/PhoneRegistered")
    Flowable<BaseBean<LoginBean>> phoneRegistered(
            @Query("phoneNumber") String phone,@Query("verifyKey") String verifyKey,
            @Query("jpushRID") String jpushRID, @Query("password") String password,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    /**
     * 登录
     */
    @POST("/user/PhoneLogin")
    Flowable<BaseBean<LoginBean>> phoneLogin(
            @Query("phoneNumber") String phone, @Query("password") String password,
            @Query("jpushRID") String jpushRID, @Query("appType") int appType,
            @Query("appChannel") String appChannel, @Query("appVersion") String appVersion);

    /**
     * 第三方注册
     */
    @POST("/user/thirdPartyRegistered")
    Flowable<BaseBean<LoginBean>> channelRegister(
            @Query("thirdPartyType") int channelType, @Query("uuid") String openId,
            @Query("password") String password, @Query("avatar") String userIcon,
            @Query("name") String name, @Query("gender") Integer sex,
            @Query("birth") String birthday, @Query("phoneNumber") String phone,
            @Query("verifyKey") String verifyKey, @Query("jpushRID") String jpushRID,
            @Query("appType") int appType, @Query("appChannel") String appChannel,
            @Query("appVersion") String appVersion);

    /**
     * 第三方登录
     */
    @POST("/user/thirdPartyLogin")
    Flowable<BaseBean<LoginBean>> channelLogin(
            @Query("thirdPartyType") int channelType, @Query("uuid") String openId,
            @Query("jpushRID") String jpushRID, @Query("appType") int appType,
            @Query("appChannel") String appChannel, @Query("appVersion") String appVersion);

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
    Flowable<BaseBean<LoginBean>> forgetPassword(
            @Query("phoneNumber") String phone, @Query("verifyKey") String verifyKey,
            @Query("jpushRID") String jpushRID, @Query("password") String password,
            @Query("appType") int appType);

    /**
     * 修改密码
     */
    @POST("/user/modifyPassword")
    Flowable<BaseBean> changePassword(
            @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    /**
     * 修改密码
     */
    @POST("/user/modifyPhoneNum")
    Flowable<BaseBean> changePhone(
            @Query("phoneNumber") String phone, @Query("SMSCode") String code);

    /**
     * 修改用户信息
     */
    @POST("/user/modifyProfile")
    Flowable<BaseBean<UserInfoBean>> changeUserInfo(
            @Query("avatar") String userIcon, @Query("name") String name,
            @Query("gender") Integer sex, @Query("birth") String birthday,
            @Query("sign") String sign);

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
     * 获取创作列表
     * @param passType 文章状态 0审核中 1未通过 2通过
     */
    @POST("/article/getCreationList")
    Flowable<BaseArrayBean<CreationBean>> getCreation(
            @Query("stateType") int passType,@Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取创作未通过详情
     */
    @POST("/article/getArticleBasicInfo")
    Flowable<BaseBean<RejectDetailsBean>> getRejectDetails(@Query("articleId") int cardId);

    /**
     * 获取笔记详情
     * @param noteType 预览类型 0审核通过（默认）1审核中 2审核未通过 3自由笔记 4卡片笔记
     */
    @POST("/article/getArticleBasicInfo")
    Flowable<BaseBean<NoteDetailsBean>> getNoteDetails(
            @Query("articleId") int noteId, @Query("viewType") int noteType);

    /**
     * 获取笔记列表
     * @param noteType 0自由笔记 1卡片笔记
     */
    @POST("/article/getNoteArticleList")
    Flowable<BaseArrayBean<NoteBean>> getNoteList(
            @Query("noteType") int noteType, @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取收藏卡片列表
     */
    @POST("/favorite/getFavoriteArticleList")
    Flowable<BaseArrayBean<CollectCardBean>> getCollectCard(
            @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取点赞卡片列表
     */
    @POST("/article/getLikeArticleList")
    Flowable<BaseArrayBean<LikeCardsBean>> getLikeCard(
            @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取阅读卡片列表
     */
    @POST("/user/getViewArticleList")
    Flowable<BaseArrayBean<ReadCardsBean>> getReadCard(
            @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

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
     * 添加收藏卡片
     */
    @POST("/favorite/collectArticle")
    Flowable<BaseBean<CardDetailsBean>> addCollectCard(@Query("articleId") int cardId);

    /**
     *取消收藏卡片
     */
    @POST("/favorite/uncollectSingleArticle")
    Flowable<BaseBean<CardDetailsBean>> removeCollectCard(@Query("articleId") int cardId);

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
     *获取其他用户信息
     */
    @POST("/user/getAuthorInfo")
    Flowable<BaseBean<OtherUserInfoBean>> getAuthorInfo(@Query("authorId") int id);

    /**
     * 获取其他用户创作列表
     */
    @POST("/article/getCreationListByAuthorId")
    Flowable<BaseArrayBean<CreationBean>> getOtherUserCreation(
            @Query("authorId") int userId,@Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 关注取消关注其他用户
     * @param userId
     * @param followType  0关注 1取消关注
     * @return
     */
    @POST("/user/setUserFollowUser")
    Flowable<BaseBean<FollowUserBean>> setUserFollow(
            @Query("userId") int userId,@Query("followType") int followType);

    /**
     * 获取我关注的和关注我的
     * @param followType 0该用户关注的人 1该用户的粉丝
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @POST("/user/getFollowUserList")
    Flowable<BaseArrayBean<FollowUserBean>> getFollowUser(
            @Query("followType") int followType,@Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取用户消息开关
     * @return
     */
    @POST("/user/getMessageSetting")
    Flowable<BaseBean<MessageModeBean>> getMessageSetting();

    /**
     * 设置用户消息开关
     * @param systemMessageSetting 系统消息开关 0开 1关
     * @param dynamicMessageSetting 动态消息开关 0开 1关
     * @return
     */
    @POST("/user/messageSetting")
    Flowable<BaseBean> setMessageSetting(
            @Query("systemMessageSetting") int systemMessageSetting,
            @Query("dynamicMessageSetting") int dynamicMessageSetting);

    /**
     * 申请创作
     * @param name 姓名
     * @param phone 手机号
     * @param idCard 身份证
     * @return
     */
    @POST("/user/applyWriterQualification")
    Flowable<BaseBean> applyCreation(
            @Query("realName") String name,@Query("phoneNum") String phone
            ,@Query("identityId") String idCard);

    /**
     * 获取系统消息
     */
    @POST("/message/getSystemMessageList")
    Flowable<BaseArrayBean<SystemMessageBean>> getSystemMessage(
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取动态消息预览
     */
    @POST("/message/getDynamicMessageFaceInfo")
    Flowable<BaseBean<DynamicMessageBean>> getDynamicMessage();

    /**
     * 获取动态消息列表
     */
    @POST("/message/getDynamicMessageList")
    Flowable<BaseArrayBean<DynamicMessageListBean>> getDynamicMessageList(
            @Query("messageType") int messageType,@Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 清空动态消息列表
     */
    @POST("/message/deleteDynamicMessage")
    Flowable<BaseBean> deleteDynamicMessageList(
            @Query("messageType") int messageType);

    /**
     * 获取红点状态
     */
    @POST("/mytabinfo/getMyTabInfo")
    Flowable<BaseBean<RedPointBean>> getRedPointStatus();

    /**
     * 提交自由笔记或审核
     * @param submitType 提交类型 0保存笔记 1提交审核
     */
    @POST("/note/submitFreeNode")
    Flowable<BaseBean> saveFreeNode(
            @Query("articleId") Integer cardId, @Query("title") String title,
            @Query("titleImage") String imageUrl, @Query("collectionId") Integer classifyId,
            @Query("content") String content, @Query("submitType") Integer submitType);

    /**
     * 提交卡片笔记
     */
    @POST("/note/submitCardNode")
    Flowable<BaseBean> saveCardNode(
            @Query("articleId") Integer cardId, @Query("title") String title,
            @Query("titleImage") String imageUrl, @Query("relatedArticleId") Integer withCardId,
            @Query("content") String content);
}
