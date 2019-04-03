package com.zxcx.zhizhe.ui.my.money.bill

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillFristFragment : RefreshMvpFragment<BillPresenter>(), BillContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {



    override fun onLoadMoreRequested() {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun createPresenter(): BillPresenter {
        return BillPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<BillBean>?) {
    }
}