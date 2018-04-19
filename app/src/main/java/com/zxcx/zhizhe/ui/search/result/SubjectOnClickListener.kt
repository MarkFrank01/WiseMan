package com.zxcx.zhizhe.ui.search.result

import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.search.result.SubjectBean

/**
 * Created by anm on 2018/3/26.
 */

interface SubjectOnClickListener {
    fun cardOnClick(bean: CardBean)
    fun subjectOnClick(bean: SubjectBean)
}