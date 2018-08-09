package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * Created by anm on 2018/3/26.
 * 长文与专题列表混杂时，专题Item点击事件
 */

interface SubjectOnClickListener {
	fun articleOnClick(bean: CardBean)
	fun subjectOnClick(bean: SubjectBean)
}