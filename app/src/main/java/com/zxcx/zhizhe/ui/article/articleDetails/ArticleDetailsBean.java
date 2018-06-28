package com.zxcx.zhizhe.ui.article.articleDetails;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;
import com.zxcx.zhizhe.ui.welcome.ADBean;

import java.util.Date;

public class ArticleDetailsBean extends RetrofitBaen {

	@SerializedName("like")
	private boolean isLike;
	@SerializedName("disagree")
	private boolean isUnLike;
	@SerializedName("collect")
	private boolean isCollect;
	@SerializedName("type")
	private int cardType;
	@SerializedName("pv")
	private int readNum;
	@SerializedName("likedUsersCount")
	private int likeNum;
	@SerializedName("disagreeUsersCount")
	private int unLikeNum;
	@SerializedName("collectingCount")
	private int collectNum;
	@SerializedName("collectionId")
	private int cardBagId;
	@SerializedName("topicId")
	private int subjectId;
	@SerializedName("id")
	private int id;
	@SerializedName("titleImage")
	private String imageUrl;
	@SerializedName("title")
	private String name;
	@SerializedName("collectionName")
	private String cardBagName;
	@SerializedName("topicName")
	private String subjectName;
	@SerializedName("authorName")
	private String authorName;
	@SerializedName("authorId")
	private int authorId;
	@SerializedName("passTime")
	private Date date;
	@SerializedName("authorAvatar")
	private String authorIcon;
	@SerializedName("authorCreateArticleCount")
	private String authorCardNum;
	@SerializedName("authorFollowerCount")
	private String authorFansNum;
	@SerializedName("authorIntelligenceValue")
	private String authorReadNum;
	@SerializedName("userAurhorRelationshipType")
	private int followType;//0为未关注，1为已关注，2为已相互关注&
	@SerializedName("adVO")
	private ADBean ad;

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean like) {
		isLike = like;
	}

	public boolean isUnLike() {
		return isUnLike;
	}

	public void setUnLike(boolean unLike) {
		isUnLike = unLike;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean collect) {
		isCollect = collect;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getReadNum() {
		return readNum;
	}

	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public int getUnLikeNum() {
		return unLikeNum;
	}

	public void setUnLikeNum(int unLikeNum) {
		this.unLikeNum = unLikeNum;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public int getCardBagId() {
		return cardBagId;
	}

	public void setCardBagId(int cardBagId) {
		this.cardBagId = cardBagId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardBagName() {
		return cardBagName;
	}

	public void setCardBagName(String cardBagName) {
		this.cardBagName = cardBagName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthorIcon() {
		return authorIcon;
	}

	public void setAuthorIcon(String authorIcon) {
		this.authorIcon = authorIcon;
	}

	public String getAuthorCardNum() {
		return authorCardNum;
	}

	public void setAuthorCardNum(String authorCardNum) {
		this.authorCardNum = authorCardNum;
	}

	public String getAuthorFansNum() {
		return authorFansNum;
	}

	public void setAuthorFansNum(String authorFansNum) {
		this.authorFansNum = authorFansNum;
	}

	public String getAuthorReadNum() {
		return authorReadNum;
	}

	public void setAuthorReadNum(String authorReadNum) {
		this.authorReadNum = authorReadNum;
	}

	public int getFollowType() {
		return followType;
	}

	public void setFollowType(int followType) {
		this.followType = followType;
	}

	public ADBean getAd() {
		return ad;
	}

	public void setAd(ADBean ad) {
		this.ad = ad;
	}
}

