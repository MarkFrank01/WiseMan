package com.zxcx.zhizhe.ui.my.note.noteDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.Date;

public class NoteDetailsBean extends RetrofitBaen {

    @JSONField(name = "like")
    private boolean isLike;
    @JSONField(name = "disagree")
    private boolean isUnLike;
    @JSONField(name = "collect")
    private boolean isCollect;
    @JSONField(name = "likedUsersCount")
    private int likeNum;
    @JSONField(name = "disagreeUsersCount")
    private int unLikeNum;
    @JSONField(name = "collectingCount")
    private int collectNum;
    @JSONField(name = "collectionId")
    private int NoteBagId;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "collectionName")
    private String NoteBagName;
    @JSONField(name = "authorName")
    private String authorName;
    @JSONField(name = "authorId")
    private int authorId;
    @JSONField(name = "passTime")
    private Date date;
    @JSONField(name = "authorAvatar")
    private String authorIcon;
    @JSONField(name = "authorCreateArticleCount")
    private String authorNoteNum;
    @JSONField(name = "authorFollowerCount")
    private String authorFansNum;
    @JSONField(name = "authorIntelligenceValue")
    private String authorReadNum;
    @JSONField(name = "userAurhorRelationshipType")
    private int followType;

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
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

    public boolean isUnLike() {
        return isUnLike;
    }

    public void setUnLike(boolean unLike) {
        isUnLike = unLike;
    }

    public int getUnLikeNum() {
        return unLikeNum;
    }

    public void setUnLikeNum(int unLikeNum) {
        this.unLikeNum = unLikeNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoteBagId() {
        return NoteBagId;
    }

    public void setNoteBagId(int NoteBagId) {
        this.NoteBagId = NoteBagId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteBagName() {
        return NoteBagName;
    }

    public void setNoteBagName(String NoteBagName) {
        this.NoteBagName = NoteBagName;
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

    public String getAuthorNoteNum() {
        return authorNoteNum;
    }

    public void setAuthorNoteNum(String authorNoteNum) {
        this.authorNoteNum = authorNoteNum;
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
}

