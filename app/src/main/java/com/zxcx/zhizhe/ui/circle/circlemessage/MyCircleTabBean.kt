package com.zxcx.zhizhe.ui.circle.circlemessage

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
//写手类型 0普通用户 1审核不通过 2审核中用户 3写手用户
const val writer_status_user = 0
const val writer_status_reject = 1
const val writer_status_review = 2
const val writer_status_writer = 3

//一级评论
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_FIRST_LEVEL_COMMENT = 301
//二级评论
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_SECOND_LEVEL_COMMENT = 302
//点赞圈子
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_LIKE_CIRCLE = 303
//点赞提问
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_QUESTION = 304
//点赞回复
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_LIST_ANSWER = 305
//提问
const val MESSAGE_TYPE_CIRCLE_DYNAMIC_QUESTION = 306

data class MyCircleTabBean(
        @SerializedName("creationCount") var cardCreationCount: Int,
        @SerializedName("viewArticleCount") var cardViewCount: Int,
        @SerializedName("hasDynamicMessage") var hasDynamicMessage: Boolean,
        @SerializedName("hasSystemMessage") var hasSystemMessage: Boolean,
        @SerializedName("accountLevel") var level: Int,
        @SerializedName("followerCount") var fansCount: Int,
        @SerializedName("totalIntelligenceValue") var totalIntelligenceValue: Int,
        @SerializedName("writerStatus") var writerStatus: Int,
        @SerializedName("authenticationType") var actionType: Int,//0

        @SerializedName("hasCircleDynamicMessage")var hasCircleDynamicMessage:Boolean,
        @SerializedName("hasCircleLikeDynamicMessage")var hasCircleLikeDynamicMessage:Boolean,
        @SerializedName("hasCircleQuestionDynamicMessage")var hasCircleQuestionDynamicMessage:Boolean,

        @SerializedName("content") var content:String,
        @SerializedName("distanceTime") var distanceTime:String,
        @SerializedName("messageType") var messageType:Int,
        @SerializedName("relatedCircleId") var relatedCircleId:Int,
        @SerializedName("relatedCircleTitle") var relatedCircleTitle:String,
        @SerializedName("relatedCircleTitleImage") var relatedCircleTitleImage:String,
        @SerializedName("relatedCommentId") var relatedCommentId:Int,
        @SerializedName("relatedCommentTitle") var relatedCommentTitle:String,
        @SerializedName("relatedCommentTitleImage") var relatedCommentTitleImage:String,
        @SerializedName("relatedQaId ") var relatedQaId:Int,
        @SerializedName("relatedQaTitle") var relatedQaTitle:String,
        @SerializedName("relatedQaTitleImage") var relatedQaTitleImage:String,
        @SerializedName("relatedUserAvatar") var relatedUserAvatar:String,
        @SerializedName("relatedUserId") var relatedUserId:Int,
        @SerializedName("relatedUserName") var relatedUserName:String





) :RetrofitBean(),MultiItemEntity {

    override fun getItemType(): Int {
        return messageType
    }
}
