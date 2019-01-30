package com.zxcx.zhizhe.ui.circle.allmycircle

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.circle.adapter.AllMyCircle2Adapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.createcircle.CreateCircleActivity
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorLongActivity
import com.zxcx.zhizhe.ui.my.pastelink.PasteLinkActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.EmptyView
import com.zxcx.zhizhe.widget.PublishDialog
import kotlinx.android.synthetic.main.fragment_my_circle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCreateFragment: RefreshMvpFragment<AlllMyCirclePresenter>(), AllMyCircleContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener,MyCircleListener {


    private var mCreatePage = 0

    private lateinit var mAllmyCircleAdapter: AllMyCircle2Adapter
    private lateinit var myCus:MyCustomPopup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_circle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    override fun createPresenter(): AlllMyCirclePresenter {
        return AlllMyCirclePresenter(this)
    }

    override fun getDataSuccess(list: MutableList<CircleBean>) {
        mRefreshLayout.finishRefresh()
        if (mCreatePage == 0){
            mAllmyCircleAdapter.setNewData(list)
        }else{
            mAllmyCircleAdapter.addData(list)
        }

        mCreatePage++
        if (list.size< Constants.PAGE_SIZE){
            mAllmyCircleAdapter.loadMoreEnd(false)
        }else{
            mAllmyCircleAdapter.loadMoreComplete()
            mAllmyCircleAdapter.setEnableLoadMore(false)
            mAllmyCircleAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onClick(click: Boolean) {



        val mHomeDialog = PublishDialog(mActivity)
        mHomeDialog.setFabuClickListener {
            //                toastShow("开始创作")
            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationEditorActivity::class.java) {}
                    }

                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.mListener = {
                            mActivity.startActivity(CreationEditorActivity::class.java) {}
                        }
                        dialog.show(mActivity.supportFragmentManager, "")
                    }
                }
            }
            mHomeDialog.outDia()
        }

        mHomeDialog.setShenDuClickListener {
            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationEditorLongActivity::class.java) {}
                    }

                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.mListener = {
                            mActivity.startActivity(CreationEditorLongActivity::class.java) {}
                        }
                        dialog.show(mActivity.supportFragmentManager, "")
                    }
                }
            }
            mHomeDialog.outDia()
        }

        mHomeDialog.setHuishouClickListener {

            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //一键转载
                        mActivity.startActivity(PasteLinkActivity::class.java) {}
                    }

                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.mListener = {
                            mActivity.startActivity(PasteLinkActivity::class.java) {}
                        }
                        dialog.show(mActivity.supportFragmentManager, "")
                    }
                }
            }

//                mActivity.startActivity(PasteLinkActivity::class.java) {}

            mHomeDialog.outDia()
        }

        mHomeDialog.show()
    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.tv_statue ->{

                XPopup.get(mActivity)
                        .asCustom(myCus)
                        .dismissOnBackPressed(true)
                        .show()
            }


        }
    }

    private fun initRecycleView() {
        mAllmyCircleAdapter = AllMyCircle2Adapter(ArrayList())

        rv_my_circle_all.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_my_circle_all.adapter = mAllmyCircleAdapter

        mAllmyCircleAdapter.onItemChildClickListener = this

//        val emptyView = EmptyView.getEmptyView(mActivity, "大咖都在这里，创建圈子一起玩", R.drawable.no_data)
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity,"大咖都在这里，创建圈子一起玩","",R.drawable.no_data,View.OnClickListener {
            mActivity.startActivity(CreateCircleActivity::class.java){}
        })
        mAllmyCircleAdapter.emptyView = emptyView

        myCus = MyCustomPopup(mActivity)
        myCus.mListener = this
    }

    fun onRefresh() {
        getMyCreate()
    }

    //获取我创建
    private fun getMyCreate() {
        mPresenter.getMyCreate(mCreatePage, Constants.PAGE_SIZE)
    }

}

class MyCustomPopup(context: Context) : CenterPopupView(context){

    lateinit var mListener:MyCircleListener


    override fun getImplLayoutId(): Int {
        return R.layout.custom_popup
    }

    override fun initPopupContent() {
        super.initPopupContent()
        findViewById<View>(R.id.tv_close).setOnClickListener {
            dismiss()
            mListener.onClick(true)
        }
    }

    override fun getMaxWidth(): Int {
        return ((ScreenUtils.getDisplayWidth() - ScreenUtils.dip2px(20f) * 4) )
    }
}