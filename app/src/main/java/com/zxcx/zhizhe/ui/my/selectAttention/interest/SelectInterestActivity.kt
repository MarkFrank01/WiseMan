package com.zxcx.zhizhe.ui.my.selectAttention.interest

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectInterestActivity:MvpActivity<SelectInterestPresenter>(),SelectInterestContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    //推荐的标签
    private var mCollectionList = ArrayList<ClassifyCardBean>()
    //推荐的用户
    private var mUserList = ArrayList<AttentionManBean>()

    override fun createPresenter(): SelectInterestPresenter {
        return SelectInterestPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: InterestRecommendBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

}