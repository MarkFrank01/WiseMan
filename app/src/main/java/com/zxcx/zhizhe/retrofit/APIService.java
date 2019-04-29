package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.pay.wx.WXBean;
import com.zxcx.zhizhe.service.version_update.entity.UpdateApk;
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean;
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean;
import com.zxcx.zhizhe.ui.card.hot.CardBean;
import com.zxcx.zhizhe.ui.circle.bean.CheckBean;
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean;
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean;
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean;
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean;
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean;
import com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext.BalanceBean;
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean;
import com.zxcx.zhizhe.ui.circle.circlequestiondetail.CircleCommentBean;
import com.zxcx.zhizhe.ui.comment.CommentBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.SMSCodeVerificationBean;
import com.zxcx.zhizhe.ui.my.MyTabBean;
import com.zxcx.zhizhe.ui.my.daily.DailyBean;
import com.zxcx.zhizhe.ui.my.invite.InviteBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.DynamicMessageBean;
import com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList.DynamicMessageListBean;
import com.zxcx.zhizhe.ui.my.message.system.SystemMessageBean;
import com.zxcx.zhizhe.ui.my.money.MoneyBean;
import com.zxcx.zhizhe.ui.my.money.bill.BillBean;
import com.zxcx.zhizhe.ui.my.note.NoteBean;
import com.zxcx.zhizhe.ui.my.note.noteDetails.NoteDetailsBean;
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean;
import com.zxcx.zhizhe.ui.my.selectAttention.interest.InterestRecommendBean;
import com.zxcx.zhizhe.ui.my.setting.MessageModeBean;
import com.zxcx.zhizhe.ui.my.userInfo.OSSTokenBean;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;
import com.zxcx.zhizhe.ui.otherUser.OtherUserInfoBean;
import com.zxcx.zhizhe.ui.rank.UserRankBean;
import com.zxcx.zhizhe.ui.search.result.label.SearchLabelBean;
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean;
import com.zxcx.zhizhe.ui.search.search.HotSearchBean;
import com.zxcx.zhizhe.ui.welcome.ADBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 所有网络请求的接口
 */

public interface APIService {

    //正式服务器
    String API_SERVER_URL = "http://mb.zhi-zhe.com";

//    String API_SERVER_URL = "http://120.77.180.183:8043";

//    String API_SERVER_URL = "http://120.78.189.141:8043";
//		String API_SERVER_URL = "http://www.zhi-zhe.com:8043";
//    String API_SERVER_URL = "http://192.168.1.8:8043";
//	String API_SERVER_URL = App.getContext().getString(R.string.base_url);

//    String API_SERVER_URL = "http://192.168.1.153:8043";

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
            @Query("thirdPartyType") int channelType, @Query("uuid") String uid,
            @Query("jpushRID") String jpushRID, @Query("appType") int appType,
            @Query("appChannel") String appChannel, @Query("appVersion") String appVersion,
            @Query("openId") String openId
    );

    /**
     * 获取绑定状态列表
     */
    @POST("/user/getBandingInfo")
    Flowable<BaseBean<UserInfoBean>> getBandingInfo();

    /**
     * 第三方绑定解绑
     */
//	@POST("/user/thirdPartyBindingUnbinding")
//	Flowable<BaseBean<Object>> channelBinding(
//		@Query("thirdPartyType") int channelType, @Query("type") int type,
//		@Query("openId") String openId);
    @POST("/user/thirdPartyBindingAndUnbinding")
    Flowable<BaseBean<Object>> channelBinding(
            @Query("thirdPartyType") int channelType, @Query("type") int type,
            @Query("uuid") String openId
    );

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

    @POST("/article/getRecommendContentForCard")
    Flowable<BaseArrayBean<CardBean>> getHotCardNew(
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
    @POST("/article/getLongTextListByClassifyId")
    Flowable<BaseArrayBean<ArticleAndSubjectBean>> getArticleListForCategory(
            @Query("classifyId") int cardCategoryId, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 获取评论
     */
    @POST("/article/getArticleComment")
    Flowable<BaseArrayBean<CommentBean>> getComment(
            @Query("articleId") int articleId, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 发表评论
     */
    @POST("/article/commentArticle")
    Flowable<BaseBean<CommentBean>> sendComment(
            @Query("articleId") int articleId, @Query("parentCommentId") Integer parentCommentId,
            @Query("commentContent") String content);

    /**
     * 点赞评论
     */
    @POST("/article/likeComment")
    Flowable<BaseBean<Object>> likeComment(
            @Query("commentId") int articleId);

    /**
     * 取消点赞评论
     */
    @POST("/article/unlikeComment")
    Flowable<BaseBean<Object>> unlikeComment(
            @Query("commentId") int articleId);

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
    @POST("/getThirtyRank")
    Flowable<BaseArrayBean<UserRankBean>> getTopHundredRank(
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取搜索默认关键词
     */
    @POST("/search/getSearchKeywordInInputBox")
    Flowable<BaseBean<HotSearchBean>> getSearchDefaultKeyword();

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
     * 搜索标签
     */
    @POST("/search/searchCollection")
    Flowable<BaseArrayBean<SearchLabelBean>> searchLabel(
            @Query("keyword") String keyword, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 搜索圈子
     */
    @POST("/search/searchCircle")
    Flowable<BaseArrayBean<CircleBean>> searchCircle(
            @Query("keyword") String keyword, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);
    //此处的搜索均为搜索圈内
    /**
     * 搜索圈内 （卡片/长文）
     */
    @POST("/circleSearch/searchCircleArticle")
    Flowable<BaseArrayBean<CardBean>> searchCircleArticle(
        @Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize,
        @Query("circleId") int circleId,@Query("cardType") int cardType,@Query("keyword") String keyword
    );


    /**
     * 搜索圈内话题
     */
    @POST("/circleSearch/searchCircleQA")
    Flowable<BaseArrayBean<CircleDetailBean>> searchCircleQA(
            @Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize,
            @Query("circleId") int circleId,@Query("keyword") String keyword
    );

    /**
     * 搜索圈内用户
     */
    @POST("/circleSearch/searchCircleUsers")
    Flowable<BaseArrayBean<SearchUserBean>> searchCircleUsers(
            @Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize,
            @Query("circleId") int circleId,@Query("keyword") String keyword);

    /**
     * 获取专题内卡片列表
     */
    @POST("/article/getLongTextListByTopicId")
    Flowable<BaseArrayBean<CardBean>> getSubjectCardList(
            @Query("topicId") int id, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 获取卡包内内容列表
     */
    @POST("/classifyCollection/getContentByCollectionId")
    Flowable<BaseArrayBean<ArticleAndSubjectBean>> getLabelList(
            @Query("collectionId") int id, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

//	/**
//	 * 获取创作列表
//	 *
//	 * @param passType 文章状态 0审核中 1未通过 2通过
//	 */
//	@POST("/article/getCreationList")
//	Flowable<BaseArrayBean<CardBean>> getCreation(
//		@Query("stateType") int passType, @Query("orderType") int sortType,
//		@Query("pageIndex") int page, @Query("pageSize") int pageSize);

    ///////////////////////////////////////////////////////////

    /**
     * 注：更换后地址
     * 获取创作列表
     *
     * @param passType 文章状态 0审核中 1未通过 2通过
     */
    @POST("/article/getAllCreationList")
    Flowable<BaseArrayBean<CardBean>> getCreation(
            @Query("stateType") int passType, @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);
    /////////////////////////////////////////////////////////////

    /**
     * 获取被拒卡片详情
     */
    @POST("/article/getArticleBasicInfo")
    Flowable<BaseBean<CardBean>> getRejectDetails(
            @Query("articleId") int cardId);

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
    @POST("/article/getMemoList")
    Flowable<BaseArrayBean<NoteBean>> getNoteList(
            @Query("orderType") int sortType, @Query("pageIndex") int page,
            @Query("pageSize") int pageSize);

    /**
     * 获取阅读，点赞，收藏空白页面推荐卡片
     *
     * @param tabType 标签类型 0阅读 1点赞 3收藏
     */
    @POST("/myTabInfo/getRecommendArticleWhenNoContent")
    Flowable<BaseBean<CardBean>> getEmptyRecommendCard(
            @Query("tabType") int tabType);

    /**
     * 获取收藏卡片列表
     */
    @POST("/article/getFavoriteArticleList")
    Flowable<BaseArrayBean<CardBean>> getCollectCard(
            @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取点赞卡片列表
     */
    @POST("/article/getLikeArticleList")
    Flowable<BaseArrayBean<CardBean>> getLikeCard(
            @Query("orderType") int sortType,
            @Query("pageIndex") int page, @Query("pageSize") int pageSize);

    /**
     * 获取阅读卡片列表
     */
    @POST("/article/getViewArticleList")
    Flowable<BaseArrayBean<CardBean>> getReadCard(
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
    Flowable<BaseBean<CardBean>> getCardDetails(@Query("articleId") int cardId);

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
     * 点赞卡片
     */
    @POST("/article/setLikeForArticle")
    Flowable<BaseBean<CardBean>> likeArticle(@Query("articleId") int cardId);

    /**
     * 取消点赞卡片
     */
    @POST("/article/unLikeForArticle")
    Flowable<BaseBean<CardBean>> removeLikeArticle(@Query("articleId") int cardId);

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
    @POST("/article/collectArticle")
    Flowable<BaseBean<Object>> addCollectCard(@Query("articleId") int cardId);

    /**
     * 取消收藏卡片
     */
    @POST("/article/uncollectArticle")
    Flowable<BaseBean<Object>> removeCollectCard(@Query("articleId") int cardId);

    /**
     * 点赞卡片
     */
    @POST("/article/setLikeForArticle")
    Flowable<BaseBean<Object>> likeCard(@Query("articleId") int cardId);

    /**
     * 取消点赞卡片
     */
    @POST("/article/unLikeForArticle")
    Flowable<BaseBean<Object>> removeLikeCard(@Query("articleId") int cardId);

    /**
     * 删除笔记
     */
    @POST("/article/deleteArticle")
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

    @POST("/ad/getAdByAdNum")
    Flowable<BaseArrayBean<ADBean>> getAD(@Query("adNum") String adNum, @Query("listStyleType") int listStyleType, @Query("classifyId") int classifyId);

    @POST("/ad/getAdByAdNum")
    Flowable<BaseArrayBean<ADBean>> getAD(@Query("adNum") String adNum, @Query("lastOpenedTime") long lastOpenedTime,
                                          @Query("lastOpenedAdId") long lastOpenedAdId);

    /**
     * 获取其他用户信息
     */
    @POST("/user/getAuthorInfo")
    Flowable<BaseBean<OtherUserInfoBean>> getAuthorInfo(@Query("authorId") int id);

    @POST("/user/getAuthorInfo")
    Flowable<BaseBean<CircleUserBean>> getAuthorInfo2(@Query("authorId") int id);

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

    @POST("/user/setUserFollowUser")
    Flowable<BaseBean<UserRankBean>> setUserFollow_New(
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
    @POST("/myTabInfo/getRecommendUserWhenNoContent")
    Flowable<BaseArrayBean<SearchUserBean>> getEmptyFollowUser();

    /**
     * 获取用户消息开关
     */
    @POST("/user/getNotificationSetting")
    Flowable<BaseBean<MessageModeBean>> getMessageSetting();

    /**
     * 设置用户消息开关
     *
     * @param systemMessageSetting  系统消息开关 0开 1关
     * @param dynamicMessageSetting 动态消息开关 0开 1关
     */
    @POST("/user/notificationSetting")
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
     * 获取系统消息 新版本
     */
    @POST("/message/getSystemMessageListV16")
    Flowable<BaseArrayBean<SystemMessageBean>> getSystemMessageV16(
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
     * 获取个人信息,获取红点状态
     */
    @POST("/myTabInfo/getMyTabInfo")
    Flowable<BaseBean<MyTabBean>> getMyTabInfo();

    /**
     * 删除笔记文章
     */
    @POST("/article/deleteArticle")
    Flowable<BaseBean<Object>> deleteCard(@Query("articleId") int cardId);

    @POST("/article/deleteLinkArticle")
    Flowable<BaseBean<Object>> deleteLink(@Query("linkArticleId") int linkId);

    /**
     * 提交审核
     */
    @POST("/article/uploadArticle")
    Flowable<BaseBean<Object>> saveFreeNode(
            @Query("articleId") Integer cardId, @Query("styleType") Integer styleType,
            @Query("submitType") Integer submitType);

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
    @POST("/article/uploadArticle")
    Flowable<BaseBean<Object>> saveCardNode(
            @Query("title") String title,
            @Query("titleImage") String imageUrl, @Query("relatedArticleId") Integer withCardId,
            @Query("styleType") Integer styleType, @Query("submitType") Integer submitType,
            @Field("content") String content);

    ///////////////////////新版本功能分隔线/////////////////////////////

    /**
     * 上传创作链接
     *
     * @param articleLinks 链接数组
     * @return 状态信息
     */
    @FormUrlEncoded
    @POST("/article/uploadArticleLink")
    Flowable<BaseBean<Object>> pushArticleLink(
            @Field("linkList") List<String> articleLinks,
            @Query("title") String title);


    /**
     * 每日刷卡
     *
     * @param termIndex 期下标（每3期为一个下标）
     */
    @Deprecated
    @FormUrlEncoded
    @POST("/article/dailyCreditCard")
    Flowable<BaseArrayBean<DailyBean>> getDailyList(
            @Field("termIndex") int termIndex
    );

    @FormUrlEncoded
    @POST("/article/dailyCreditCard")
    Flowable<BaseArrayBean<CardBean>> getDaliyList1(
            @Field("termIndex") int termIndex
    );


    @FormUrlEncoded
    @POST("/version/getNewVersion")
    Flowable<BaseBean<UpdateApk>> getUpdateApk(
            @Field("platformType") int platformType
    );


    /**
     * 感兴趣推荐数据
     */
    @POST("/interestRecommend/getInterestRecommend")
    Flowable<BaseBean<InterestRecommendBean>> getInterestRecommend();


    ///////////////////////////////////////////////////
    /**
     * 圈子模块接口
     */


    /**
     * 获取圈子的分类
     *
     * @param page
     * @param pageSize
     * @return
     */
    @POST("/circle/getRecommendClassifyList")
    Flowable<BaseArrayBean<CircleClassifyBean>> getCircleClassify(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * 通过分页获取推荐圈子
     *
     * @param page
     * @param pageSize
     * @return
     */
    @POST("/circle/getRecommendCircleListByPage")
    Flowable<BaseArrayBean<CircleBean>> getRecommendCircleListByPage(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取推荐圈子，当加入的圈子为空的时候
     */
    @POST("/circle/getRecommendCircleListWhenNoData")
    Flowable<BaseArrayBean<CircleBean>> getRecommendCircleListWhenNoData();

    /**
     * 获取我加入的圈子
     */
    @POST("/circle/getMyJoinCircleList")
    Flowable<BaseArrayBean<CircleBean>> getMyJoinCircleList(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取我创建的圈子
     */
    @POST("/circle/getMyCreateCircleList")
    Flowable<BaseArrayBean<CircleBean>> getMyCreateCircleList(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );


    /**
     * 通过分类获取的圈子
     */
    @POST("/circle/getCircleListByClassifyId")
    Flowable<BaseArrayBean<CircleBean>> getCircleListByClassifyId(
            @Query("classifyId") int classifyId,
            @Query("orderType") int orderType,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 加入或退出圈子
     * 加入类型 0加入 1退出
     */
    @POST("/circle/joinCircle")
    Flowable<BaseArrayBean<CircleBean>> setjoinCircle(
            @Query("circleId") int circleId,
            @Query("joinType") int joinType
    );

    /**
     * 创建圈子
     */
    @POST("/circle/createCircle")
    Flowable<BaseBean<CircleBean>> createCircle(
            @Query("title") String title,
            @Query("titleImage") String titleImage,
            @Query("classifyId") int classifyId,
            @Query("sign") String sign,
            @Query("price") String price,
            @Query("auditArticleList") List<Integer> articleList,
            @Query("levelType") int levelType
    );

    /**
     * 新的创建圈子的接口，加入是否是保存字段
     * @param title
     * @param titleImage
     * @param classifyId
     * @param sign
     * @param levelType
     * @param limitedTimeType
     * @param isSave 0 1
     * @return
     */
    @POST("/circle/createCircle")
    Flowable<BaseBean<CircleBean>> createCircleNew(
            @Query("title") String title,
            @Query("titleImage") String titleImage,
            @Query("classifyId") int classifyId,
            @Query("sign") String sign,
            @Query("levelType") int levelType,
            @Query("limitedTimeType") int limitedTimeType,
            @Query("isSave") int isSave
    );

    /**
     * 编辑圈子资料（用于未通过编辑和通过后的再编辑）
     */
    @POST("/circle/editCircle")
    Flowable<BaseBean<CircleBean>> editCircle(
            @Query("title") String title,
            @Query("titleImage") String titleImage,
            @Query("classifyId") int classifyId,
            @Query("sign") String sign,
            @Query("levelType") int levelType,
            @Query("limitedTimeType") int limitedTimeType,

            @Query("circleId") int circleId
    );

    /**
     * 获取圈子可供发布的文章,先卡片后长文
     */
    @POST("/circle/getPublishableArticle")
    Flowable<BaseArrayBean<CardBean>> getPublishableArticle(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取圈子可供锁定的文章,先卡片后长文
     */
    @POST("/circle/getLockableArticle")
    Flowable<BaseArrayBean<CardBean>> getLockableArticle(
            @Query("pageIndex") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * 通过圈子id获取圈子详情
     */
    @POST("/circle/getCircleBasicInfo")
    Flowable<BaseBean<CircleBean>> getCircleBasicInfo(
            @Query("circleId") int circleId
    );

    /**
     * 通过圈子id 获取圈子成员
     *
     * @param orderType
     * @param circleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @POST("/circle/getCircleMemberByCircleId")
    Flowable<BaseArrayBean<CircleBean>> getCircleMemberByCircleId(
            @Query("orderType") int orderType,
            @Query("circleId") int circleId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @POST("/circle/getCircleMemberByCircleId")
    Flowable<BaseArrayBean<SearchUserBean>> getCircleMemberByCircleId2(
            @Query("orderType") int orderType,
            @Query("circleId") int circleId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 通过圈子id 获取圈子话题
     */
    @POST("/circle/getCircleQAByCircleId")
    Flowable<BaseArrayBean<CircleBean>> getCircleQAByCircleId(
            @Query("orderType") int orderType,
            @Query("circleId") int circleId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    //获取外层的简介
    @POST("/circle/getCircleQAByCircleId")
    Flowable<BaseArrayBean<CircleDetailBean>> getCircleQAByCircleId2(
            @Query("orderType") int orderType,
            @Query("circleId") int circleId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );


    /**
     * 检查圈子名
     */
    @POST("/circle/checkCircleName")
    Flowable<BaseBean<CheckBean>> getCheckName(
            @Query("circleName") String circleName
    );

    /**
     *
     */
    @POST("/circle/createQuestion")
    Flowable<BaseBean<QuestionBean>> pushQuestion(
            @Query("circleId") int circleId,
            @Query("title") String title,
            @Query("description") String description,
            @Query("askImage") List<String> askImage
    );

    /**
     * 获取圈子可供审核的文章列表（用于圈子首次提交）
     */
    @POST("/circle/getLockableArticleForCreate")
    Flowable<BaseArrayBean<CardBean>> getLockableArticleForCreate(
            @Query("styleType") int styleType,
            @Query("classifyId") int classifyId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     *获取圈子可供审核的文章列表（用于圈子添加文章）
     */
    @POST("/circle/getLockableArticleForAdd")
    Flowable<BaseArrayBean<CardBean>> getLockableArticleForAdd(
            @Query("styleType") int styleType,
            @Query("circleId") int circleId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 设置圈子文章与圈内可见的文章
     */
    @POST("/circle/setCircleArticle")
    Flowable<BaseArrayBean<CardBean>> setCircleArticle(
            @Query("circleId") int circleId,
            @Query("auditArticleList") List<Integer> auditArticleList,
            @Query("privateArticleList") List<Integer> privateArticleList
    );

    /**
     * 通过圈子id获取圈子文章
     */
    @POST("/circle/getArticleByCircleId")
    Flowable<BaseArrayBean<CardBean>> getArticleByCircleId(
            @Query("circleId") int circleId,
            @Query("orderType") int orderType,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取提问详情
     */
    @POST("/circle/getQuestionInfo")
    Flowable<BaseBean<CircleDetailBean>> getQuestionInfo(
           @Query("qaId") int qaId
    );

    /**
     * 获取回答列表
     */
    @POST("/circle/getAnswerList")
    Flowable<BaseArrayBean<CircleCommentBean>> getAnswerList(
       @Query("qaId") int qaId,
       @Query("pageIndex") int page,
       @Query("pageSize") int pageSize
    );

    /**
     * 检查圈子文章是否平衡
     */
    @POST("/circle/checkCircleArticleBalance")
    Flowable<BaseBean<BalanceBean>> checkCircleArticleBalance(
       @Query("circleId") int circleId
    );

    /**
     * 设置话题置顶&不置顶
     */
    @POST("/circle/setQAFixTop")
    Flowable<BaseBean<CardBean>> setQAFixTop(
        @Query("qaId") int qaId,
        @Query("fixType") int fixType
    );

    /**
     * 内容文章置顶
     */
    @POST("/circle/setArticleFixTop")
    Flowable<BaseBean<CardBean>> setArticleFixTop(
         @Query("circleId") int circleId,
         @Query("articleId") int articleId,
         @Query("fixType") int fixType
    );

    /**
     * 内容文章文章删除
     */
    @POST("/circle/removeArticle")
    Flowable<BaseBean<HintBean>> removeArticle(
         @Query("circleId") int circleId,
         @Query("articleId") int articleId
    );

    /**
     * 评价圈子
     */
    @POST("/circle/evaluationCircle")
    Flowable<BaseBean<HintBean>> evaluationCircle(
        @Query("circleId") int circleId,
        @Query("contentQuality") int contentQuality,
        @Query("topicQuality") int topicQuality
    );

    /**
     * 举报圈子
     */
    @POST("/circle/reportCircle")
    Flowable<BaseBean<HintBean>> reportCircle(
        @Query("circleId") int circleId,
        @Query("reportType") int reportType
    );

    /**
     * 删除话题
     */
    @POST("/circle/deleteQa")
    Flowable<BaseBean<HintBean>> deleteQa(
          @Query("qaId") int qaId
    );

    /**
     * 回答提问&回复回答
     */
    @POST("/circle/createAnswer")
    Flowable<BaseBean<QuestionBean>> createAnswer(
         @Query("circleId") int circleId,
         @Query("qaId") int qaId,
         @Query("qaCommentId") int qaCommentId,
         @Query("description") String description,
         @Query("askImage") List<String> askImage
    );

    //接上面无子ID
    @POST("/circle/createAnswer")
    Flowable<BaseBean<QuestionBean>> createAnswerNokid(
            @Query("circleId") int circleId,
            @Query("qaId") int qaId,
            @Query("description") String description,
            @Query("askImage") List<String> askImage
    );

    //接上面无图
    @POST("/circle/createAnswer")
    Flowable<BaseBean<QuestionBean>> createAnswerHaveKid(
            @Query("circleId") int circleId,
            @Query("qaId") int qaId,
            @Query("qaCommentId") int qaCommentId,
            @Query("description") String description
    );


    /**
     * 获取我的圈子的数据(获取我的圈子数据（圈子动态消息）)
     */
    @POST("/circle/getCircleTabInfo")
    Flowable<BaseBean<MyCircleTabBean>> getCircleTabInfo();


    /**
     * 获取圈子评论消息
     */
    @POST("/circleNotification/getCommentMessageList")
    Flowable<BaseArrayBean<MyCircleTabBean>> getCommentMessageList(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取圈子提问消息（仅圈主）
     */
    @POST("/circleNotification/getQuestionMessageList")
    Flowable<BaseArrayBean<MyCircleTabBean>> getQuestionMessageList(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取圈子点赞消息
     */
    @POST("/circleNotification/getLikeMessageList")
    Flowable<BaseArrayBean<MyCircleTabBean>> getLikeMessageList(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取首页我的圈子
     */
    @POST("/circle/getIndexCircleList")
    Flowable<BaseArrayBean<CircleBean>> getIndexCircleList();

    /**
     * 给话题或回复点赞
     */
    @POST("/circle/likeQAOrQAComment")
    Flowable<BaseBean<Object>> likeQAOrQAComment_qa(
           @Query("qaId") int qaId,
           @Query("likeType") int likeType
    );

    @POST("/circle/likeQAOrQAComment")
    Flowable<BaseBean<Object>> likeQAOrQAComment_comment(
            @Query("qaId") int qaId,
            @Query("commentId") int commentId,
            @Query("likeType") int likeType
    );


    /**
     * 检测是否上榜 获取本周前三奖励
     */
    @POST("/activity/checkEnterTheTopThree")
    Flowable<BaseBean<Object>> checkEnterTheTopThree();

    /**
     * 活动-邀请码活动-获取邀请历史记录
     */
    @POST("/activity/getActivityInvitationHistory")
    Flowable<BaseArrayBean<InviteBean>> getActivityInvitationHistory(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("aiId") int aiId
    );

    /**
     * 常规-邀请码活动-获取邀请历史记录
     */
    @POST("/activity/getInvitationHistory")
    Flowable<BaseArrayBean<InviteBean>> getInvitationHistory();

    /**
     * 邀请码活动-获取邀请码详情信息
     */
    @POST("/activity/getInvitationInfo")
    Flowable<BaseBean<InviteBean>> getInvitationInfo();

    /**
     * 邀请码活动-填写邀请码
     */
    @POST("/activity/inputInvitationCode")
    Flowable<BaseBean<InviteBean>> inputInvitationCode(
            @Query("invitationCode") String invitationCode
    );

    /**
     * 邀请码活动-获取邀请我的人
     */
    @POST("/activity/receiveMineInvitationUser")
    Flowable<BaseBean<InviteBean>> receiveMineInvitationUser();

    /**
     * 邀请码活动-领取邀请码奖励
     */
    @POST("/activity/receiveInvitationCodeReward")
    Flowable<BaseBean<InviteBean>> receiveInvitationCodeReward(
        @Query("subjectUserId") int subjectUserId
    );

    /**
     * 设置中奖后的 收货信息
     */
    @POST("/activity/submitWinningInfo")
    Flowable<BaseBean<Object>> submitWinningInfo(
           @Query("consigneeName") String consigneeName,
           @Query("consigneeAddress") String consigneeAddress,
           @Query("consigneeMobile") String consigneeMobile
    );

    /**
     * 通过作者id获取个人创建的圈子
     */
    @POST("/circle/getCircleListByAuthorId")
    Flowable<BaseArrayBean<CircleBean>> getCircleListByAuthorId(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("userId") int userId
    );

    //    /**
    //     * 获取全部分类
    //     */
    //    @POST("/classifyCollection/getInterestedCollection")
    //    Flowable<BaseArrayBean<ClassifyBean>> getClassify();
    /**
     * 关注分类
     */
    @POST("/interestRecommend/followClassify")
    Flowable<BaseBean<ClassifyBean>> followClassify(
            @Query("classifyList") List<Integer> classifyList
    );

    /**
     * 获取兴趣选择的所有分类
     */
    @POST("/interestRecommend/getInterestRecommendForClassify")
    Flowable<BaseArrayBean<ClassifyBean>> getInterestRecommendForClassify();

    /**
     * 获取感兴趣选择的用户
     */
    @POST("/interestRecommend/getInterestRecommendForUser")
    Flowable<BaseArrayBean<SearchUserBean>> getInterestRecommendForUser(

    );

    /**
     * 用户设置顶部分类
     */
    @POST("/classifyCollection/setClassifyMenu")
    Flowable<BaseBean<Object>> setClassifyMenu(
            @Query("classifyIdList") List<Integer> classifyIdList
    );

    /**
     * 获取导航所有分类列表
     */
    @POST("/classifyCollection/getAllNavClassify")
    Flowable<BaseArrayBean<ClassifyBean>> getAllNavClassify();


    //支付!!!
    /**
     * 微信统一下单接口
     */
    @POST("/WxPay/getWxOrderPay")
    Flowable<BaseBean<WXBean>> getWxOrderPay(
            @Query("circleId") int circleId
    );

    /**
     * 阿里获取签名
     */
    @POST("/alipay/getAlOrderPayForJoinCircle")
    Flowable<BaseBean<String>> getAlOrderPayForJoinCircle(
            @Query("circleId") int circleId
    );

    /**
     * 绑定支付宝账号
     */
    @POST("/account/bindingAlipay")
    Flowable<BaseBean<Object>> bindingAlipay(
            @Query("withdrawalAmount") String withdrawalAmount,
            @Query("contact") String contact,
            @Query("phone") String phone
    );

    /**
     * 获取账号信息(see money)
     */
    @POST("/account/getAccountDetails")
    Flowable<BaseBean<MoneyBean>> getAccountDetails();

    /**
     * 申请提现
     */
    @POST("/account/applyForWithdrawal")
    Flowable<BaseBean<Object>> applyForWithdrawal(
            @Query("withdrawalAmount") String withdrawalAmount
    );

    /**
     * 获取账单明细
     */
    @POST("/account/getBillingDetails")
    Flowable<BaseArrayBean<BillBean>> getBillingDetails(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取提现明细
     */
    @POST("/account/getCashWithdrawalDetails")
    Flowable<BaseArrayBean<BillBean>> getCashWithdrawalDetails(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /**
     * 智力币进圈 安卓
     */
    @POST("/circle/joinCircleByZzbForAndroid")
    Flowable<BaseBean<Object>> joinCircleByZzbForAndroid(
            @Query("circleId") int circleId
    );
}
