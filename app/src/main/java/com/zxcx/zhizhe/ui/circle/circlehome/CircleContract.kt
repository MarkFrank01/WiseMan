package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
interface CircleContract {

    interface View : GetView<MutableList<CircleBean>> {
        //获取圈子顶部的广告
        fun getADSuccess(list: MutableList<ADBean>)

        //获取圈子选项分类
        fun getClassifySuccess(list: MutableList<CircleClassifyBean>)
    }

    //获取圈子选项分类
    interface Presenter : IGetPresenter<MutableList<CircleBean>> {
        //获取圈子顶部的广告
        fun getADSuccess(list: MutableList<ADBean>)

        //获取圈子选项分类
        fun getClassifySuccess(list: MutableList<CircleClassifyBean>)
    }

}