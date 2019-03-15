package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

/**
 * Created by anm on 2018/3/26.
 * 左滑删除监听
 */
interface SwipeMenuClickListener2 {
	fun onDeleteClick(position: Int)
	fun onContentClick(position: Int)
    fun onTopClick(position: Int)
    fun onCancelTopClick(position: Int)
}