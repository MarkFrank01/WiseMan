package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsBean;
import com.zxcx.zhizhe.ui.article.attention.ArticleAndSubjectBean;
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean;
import com.zxcx.zhizhe.ui.card.hot.CardBean;
import com.zxcx.zhizhe.ui.classify.ClassifyBean;
import com.zxcx.zhizhe.ui.home.rank.UserRankBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.SMSCodeVerificationBean;
import com.zxcx.zhizhe.ui.my.MyTabBean;
import com.zxcx.zhizhe.ui.my.creation.creationDetails.RejectDetailsBean;
import com.zxcx.zhizhe.ui.my.creation.fragment.CreationBean;
import com.zxcx.zhizhe.ui.my.intelligenceValue.IntelligenceValueBean;
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.DynamicMessageBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList.DynamicMessageListBean;
import com.zxcx.zhizhe.ui.my.message.system.SystemMessageBean;
import com.zxcx.zhizhe.ui.my.note.NoteBean;
import com.zxcx.zhizhe.ui.my.note.noteDetails.NoteDetailsBean;
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsBean;
import com.zxcx.zhizhe.ui.my.setting.MessageModeBean;
import com.zxcx.zhizhe.ui.my.userInfo.OSSTokenBean;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;
import com.zxcx.zhizhe.ui.otherUser.OtherUserInfoBean;
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean;
import com.zxcx.zhizhe.ui.search.search.HotSearchBean;
import com.zxcx.zhizhe.ui.welcome.ADBean;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
	
	//    String API_SERVER_URL = "http://120.77.180.183:8043";
//    String API_SERVER_URL = "http://119.23.18.65:8043";
//    String API_SERVER_URL = "http://192.168.1.102:8043";
	String API_SERVER_URL = App.getContext().getString(R.string.base_url);
	
	/**
	 * 获取手机号注册状态 600:未注册 700:已注册
	 */
	@POST("/user/isPhoneAlreadyBanding")
	Flowable<BaseBean<Object>> checkPhoneRegistered(@Query("phoneNumber") String phone);
	
	/**
	 * 验证短信验证码
	 */
	@POST("/user/smsCodeVerification")
	Flowable<BaseBean<SMSCodeVerificationBean>> smsCodeVerification(
		@Query("phoneNumber") String phone, @Query("SMSCode") String code);
	
	/**
	 * 验证码登录
	 */
	@POST("/user/smsCodeLogin")
	Flowable<BaseBean<LoginBean>> smsCodeLogin(
		@Query("phoneNumber") String phone, @Query("smsCode") String smsCode,
		@Query("jpushRID") String jpushRID, @Query("appType") int appType,
		@Query("appChannel") String appChannel, @Query("appVersion") String appVersion);
	
	/**
	 * 第三方注册
	 */
	@POST("/user/thirdPartyRegistered")
	Flowable<BaseBean<LoginBean>> channelRegister(
		@Query("thirdPartyType") int channelType, @Query("uuid") String openId,
		@Query("avatar") String userIcon,
		@Query("name") String name, @Query("gender") Integer sex,
		@Query("birth") String birthday, @Query("phoneNumber") String phone,
		@Query("smsCode") String smsCode, @Query("jpushRID") String jpushRID,
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
	Flowable<BaseBean<Object>> channelBinding(
		@Query("thirdPartyType") int channelType, @Query("type") int type,
		@Query("openId") String openId);
	
	/**
	 * 修改手机号
	 */
	@POST("/user/modifyPhoneNum")
	Flowable<BaseBean<Object>> changePhone(
		@Query("phoneNumber") String phone, @Query("verifyKey") String verifyKey);
	
	/**
	 * 修改用户信息
	 */
	@POST("/user/modifyProfile")
	Flowable<BaseBean<UserInfoBean>> changeUserInfo(
		@Query("avatar") String userIcon, @Query("name") String name,
		@Query("gender") Integer sex, @Query("birth") String birthday,
		@Query("sign") String sign);
	
	/**
	 * 获取推荐卡片
	 */
	@POST("/article/getRecommendContent")
	Flowable<BaseArrayBean<CardBean>> getHotCard(
		@Query("lastRefreshTime") String date, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取关注卡片
	 */
	@POST("/article/getFollowContent")
	Flowable<BaseArrayBean<CardBean>> getAttentionCard(
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 获取卡片类别
	 */
	@POST("/article/getCardListTabClassifyList")
	Flowable<BaseArrayBean<CardCategoryBean>> getCardCategory();
	
	/**
	 * 获取长文类别
	 */
	@POST("/article/getLongTextListTabClassifyList")
	Flowable<BaseArrayBean<CardCategoryBean>> getArticleCategory();
	
	/**
	 * 获取卡片类别下卡片列表
	 */
	@POST("/article/getCardListByClassifyId")
	Flowable<BaseArrayBean<CardBean>> getCardListForCategory(
		@Query("classifyId") int cardCategoryId, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取长文类别下长文列表
	 */
	@POST("/article/getCardListByClassifyId")
	Flowable<BaseArrayBean<ArticleAndSubjectBean>> getArticleListForCategory(
		@Query("classifyId") int cardCategoryId, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
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
	Flowable<BaseArrayBean<HotSearchBean>> getSearchHot();
	
	/**
	 * 获取搜索预选项
	 */
	@POST("/search/getSearchAssociate")
	Flowable<BaseArrayBean<String>> getSearchPre(@Query("keyword") String keyword);
	
	/**
	 * 搜索卡片，长文
	 */
	@POST("/search/searchArticle")
	Flowable<BaseArrayBean<CardBean>> searchCard(
		@Query("keyword") String keyword, @Query("cardType") int cardType,
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 搜索卡片，长文，专题
	 */
	@POST("/search/searchUsers")
	Flowable<BaseArrayBean<SearchUserBean>> searchUser(
		@Query("keyword") String keyword, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取专题内卡片列表
	 */
	@POST("/collection/getTopicArticleList")
	Flowable<BaseArrayBean<CardBean>> getSubjectCardList(
		@Query("topicId") int id, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取卡包内内容列表
	 */
	@POST("/article/getContentByCollectionId")
	Flowable<BaseArrayBean<CardBean>> getCardBagCardList(
		@Query("collectionId") int id, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取创作列表
	 *
	 * @param passType 文章状态 0审核中 1未通过 2通过
	 */
	@POST("/article/getCreationList")
	Flowable<BaseArrayBean<CreationBean>> getCreation(
		@Query("stateType") int passType, @Query("orderType") int sortType,
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 获取被拒卡片详情
	 */
	@POST("/article/getArticleBasicInfo")
	Flowable<BaseBean<RejectDetailsBean>> getRejectDetails(
		@Query("articleId") int cardId, @Query("viewType") int passType);
	
	/**
	 * 获取笔记详情
	 *
	 * @param noteType 预览类型 0审核通过（默认）1审核中 2审核未通过 3草稿箱 4卡片笔记
	 */
	@POST("/article/getArticleBasicInfo")
	Flowable<BaseBean<NoteDetailsBean>> getNoteDetails(
		@Query("articleId") int noteId, @Query("viewType") int noteType);
	
	/**
	 * 获取笔记列表
	 */
	@POST("/note/getMemoList")
	Flowable<BaseArrayBean<NoteBean>> getNoteList(
		@Query("orderType") int sortType, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 获取阅读，点赞，收藏空白页面推荐卡片
	 *
	 * @param tabType 标签类型 0阅读 1点赞 3收藏
	 */
	@POST("/mytabinfo/getRecommendArticleWhenNoContent")
	Flowable<BaseBean<CardBean>> getEmptyRecommendCard(
		@Query("tabType") int tabType);
	
	/**
	 * 获取收藏卡片列表
	 */
	@POST("/favorite/getFavoriteArticleList")
	Flowable<BaseArrayBean<MyCardsBean>> getCollectCard(
		@Query("orderType") int sortType,
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 获取点赞卡片列表
	 */
	@POST("/article/getLikeArticleList")
	Flowable<BaseArrayBean<MyCardsBean>> getLikeCard(
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
	 * 获取全部分类
	 */
	@POST("/classifyCollection/getInterestedCollection")
	Flowable<BaseArrayBean<ClassifyBean>> getClassify();
	
	/**
	 * 修改兴趣选择列表
	 */
	@POST("/classifyCollection/followCollection")
	Flowable<BaseBean<Object>> changeAttentionList(
		@Query("collectionList") List<Integer> idList);
	
	/**
	 * 获取卡片详情
	 */
	@POST("/article/getArticleBasicInfo")
	Flowable<BaseBean<ArticleDetailsBean>> getCardDetails(@Query("articleId") int cardId);
	
	/**
	 * 卡片阅读30秒
	 */
	@POST("/article/readArticle")
	Flowable<BaseBean<Object>> readArticle(@Query("articleId") int cardId);
	
	/**
	 * 删除阅读卡片
	 */
	@POST("/article/deleteRead")
	Flowable<BaseBean<Object>> removeReadArticle(
		@Query("relationshipKeyId") int realId, @Query("articleId") int cardId);
	
	/**
	 * 添加收藏卡片
	 */
	@POST("/favorite/collectArticle")
	Flowable<BaseBean<ArticleDetailsBean>> addCollectArticle(@Query("articleId") int cardId);
	
	/**
	 * 取消收藏卡片
	 */
	@POST("/favorite/uncollectSingleArticle")
	Flowable<BaseBean<ArticleDetailsBean>> removeCollectArticle(@Query("articleId") int cardId);
	
	/**
	 * 点赞卡片
	 */
	@POST("/article/setLikeForArticle")
	Flowable<BaseBean<ArticleDetailsBean>> likeArticle(@Query("articleId") int cardId);
	
	/**
	 * 取消点赞卡片
	 */
	@POST("/article/unLikeForArticle")
	Flowable<BaseBean<ArticleDetailsBean>> removeLikeArticle(@Query("articleId") int cardId);
	
	/**
	 * 不赞同卡片
	 */
	@POST("/article/setDisagreeForArticle")
	Flowable<BaseBean<ArticleDetailsBean>> unLikeArticle(@Query("articleId") int cardId);
	
	/**
	 * 取消不赞同卡片
	 */
	@POST("/article/unDisagreeForArticle")
	Flowable<BaseBean<ArticleDetailsBean>> removeUnLikeArticle(@Query("articleId") int cardId);
	
	/**
	 * 卡片阅读30秒
	 */
	@POST("/article/readArticle")
	Flowable<BaseBean<Object>> readCard(@Query("articleId") int cardId);
	
	/**
	 * 删除阅读卡片
	 */
	@POST("/article/deleteRead")
	Flowable<BaseBean<Object>> removeReadCard(
		@Query("relationshipKeyId") int realId, @Query("articleId") int cardId);
	
	/**
	 * 添加收藏卡片
	 */
	@POST("/favorite/collectArticle")
	Flowable<BaseBean<CardBean>> addCollectCard(@Query("articleId") int cardId);
	
	/**
	 * 取消收藏卡片
	 */
	@POST("/favorite/uncollectSingleArticle")
	Flowable<BaseBean<CardBean>> removeCollectCard(@Query("articleId") int cardId);
	
	/**
	 * 点赞卡片
	 */
	@POST("/article/setLikeForArticle")
	Flowable<BaseBean<CardBean>> likeCard(@Query("articleId") int cardId);
	
	/**
	 * 取消点赞卡片
	 */
	@POST("/article/unLikeForArticle")
	Flowable<BaseBean<CardBean>> removeLikeCard(@Query("articleId") int cardId);
	
	/**
	 * 不赞同卡片
	 */
	@POST("/article/setDisagreeForArticle")
	Flowable<BaseBean<CardBean>> unLikeCard(@Query("articleId") int cardId);
	
	/**
	 * 取消不赞同卡片
	 */
	@POST("/article/unDisagreeForArticle")
	Flowable<BaseBean<CardBean>> removeUnLikeCard(@Query("articleId") int cardId);
	
	/**
	 * 删除笔记
	 */
	@POST("/note/deleteNode")
	Flowable<BaseBean<Object>> removeNote(@Query("articleId") int noteId);
	
	/**
	 * 提交反馈
	 */
	@FormUrlEncoded
	@POST("/feedback/sumbitFeedbadk")
	Flowable<BaseBean<Object>> feedback(
		@Field("content") String content, @Query("appType") int appType,
		@Query("appChannel") String appChannel, @Query("appVersion") String appVersion);
	
	/**
	 * OSS秘钥获取
	 */
	@POST("/ossAuth")
	Flowable<BaseBean<OSSTokenBean>> getOSS(@Query("uuid") String uuid);
	
	/**
	 * 获取广告
	 */
	@POST("/ad/getAdByAdNum")
	Flowable<BaseArrayBean<ADBean>> getAD(@Query("adNum") String adNum);
	
	/**
	 * 获取其他用户信息
	 */
	@POST("/user/getAuthorInfo")
	Flowable<BaseBean<OtherUserInfoBean>> getAuthorInfo(@Query("authorId") int id);
	
	/**
	 * 获取其他用户创作列表
	 */
	@POST("/article/getCreationListByAuthorId")
	Flowable<BaseArrayBean<CardBean>> getOtherUserCreation(
		@Query("authorId") int userId, @Query("orderType") int sortType,
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 关注取消关注其他用户
	 *
	 * @param followType 0关注 1取消关注
	 */
	@POST("/user/setUserFollowUser")
	Flowable<BaseBean<SearchUserBean>> setUserFollow(
		@Query("userId") int userId, @Query("followType") int followType);
	
	/**
	 * 获取我关注的和关注我的
	 *
	 * @param followType 0该用户关注的人 1该用户的粉丝
	 */
	@POST("/user/getFollowUserList")
	Flowable<BaseArrayBean<SearchUserBean>> getFollowUser(
		@Query("followType") int followType, @Query("orderType") int sortType,
		@Query("pageIndex") int page, @Query("pageSize") int pageSize);
	
	/**
	 * 获取我关注的和关注我的
	 */
	@POST("/mytabinfo/getRecommendUserWhenNoContent")
	Flowable<BaseArrayBean<SearchUserBean>> getEmptyFollowUser();
	
	/**
	 * 获取用户消息开关
	 */
	@POST("/user/getMessageSetting")
	Flowable<BaseBean<MessageModeBean>> getMessageSetting();
	
	/**
	 * 设置用户消息开关
	 *
	 * @param systemMessageSetting 系统消息开关 0开 1关
	 * @param dynamicMessageSetting 动态消息开关 0开 1关
	 */
	@POST("/user/messageSetting")
	Flowable<BaseBean<Object>> setMessageSetting(
		@Query("systemMessageSetting") int systemMessageSetting,
		@Query("dynamicMessageSetting") int dynamicMessageSetting);
	
	/**
	 * 申请创作
	 */
	@POST("/user/applyWriterQualification")
	Flowable<BaseBean<Object>> applyCreation();
	
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
		@Query("messageType") int messageType, @Query("pageIndex") int page,
		@Query("pageSize") int pageSize);
	
	/**
	 * 清空动态消息列表
	 */
	@POST("/message/deleteDynamicMessage")
	Flowable<BaseBean<Object>> deleteDynamicMessageList(
		@Query("messageType") int messageType);
	
	/**
	 * 获取红点状态
	 */
	@POST("/mytabinfo/getMyTabInfo")
	Flowable<BaseBean<MyTabBean>> getMyTabInfo();
	
	/**
	 * 删除笔记文章
	 */
	@POST("/note/deleteNode")
	Flowable<BaseBean<Object>> deleteCard(@Query("articleId") int cardId);
	
	/**
	 * 提交自由笔记或审核
	 *
	 * @param submitType 提交类型 0保存笔记 1提交审核
	 */
	@FormUrlEncoded
	@POST("/note/submitFreeNode")
	Flowable<BaseBean<Object>> saveFreeNode(
		@Query("articleId") Integer cardId, @Query("title") String title,
		@Query("titleImage") String imageUrl, @Query("collectionId") Integer classifyId,
		@Field("content") String content, @Query("submitType") Integer submitType);
	
	/**
	 * 保存笔记
	 */
	@FormUrlEncoded
	@POST("/note/saveMemo")
	Flowable<BaseBean<Object>> saveNote(
		@Query("articleId") Integer cardId, @Query("title") String title,
		@Field("content") String content);
	
	/**
	 * 提交卡片笔记
	 */
	@FormUrlEncoded
	@POST("/note/submitCardNode")
	Flowable<BaseBean<Object>> saveCardNode(
		@Query("articleId") Integer cardId, @Query("title") String title,
		@Query("titleImage") String imageUrl, @Query("relatedArticleId") Integer withCardId,
		@Field("content") String content);
	
	/**
	 * 获取智力值详情
	 */
	@POST("/user/getIntelligenceValueData")
	Flowable<BaseBean<IntelligenceValueBean>> getIntelligenceValue();
}
