package com.zxcx.zhizhe.ui.search.result.subject

import com.zxcx.zhizhe.ui.home.hot.CardBean

/**
 * Created by anm on 2018/3/26.
 */

interface SubjectOnClickListener {
    fun cardOnClick(bean: CardBean)
    fun subjectOnClick(bean: SubjectBean)
}