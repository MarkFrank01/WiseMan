package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * Created by anm on 2018/3/26.
 */

interface SubjectOnClickListener {
	fun articleOnClick(bean: CardBean)
	fun subjectOnClick(bean: SubjectBean)
}