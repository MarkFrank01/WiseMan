package com.zxcx.zhizhe.mvp.template

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvp.adapter.TemplateCardAdapter
import com.zxcx.zhizhe.mvp.contract.TemplateContract
import com.zxcx.zhizhe.mvp.entity.DataServer
import com.zxcx.zhizhe.mvp.presenter.TemplatePresenter
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_template_card.*

/**
 * @author : MarkFrank01
 * @Created on 2018/12/4
 * @Description :
 */
class TemplateCardActivity : MvpActivity<TemplatePresenter>(), TemplateContract.View, BaseQuickAdapter.OnItemChildClickListener {


    private lateinit var mAdapter: TemplateCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_card)
        initToolBar("选择模板")
        initView()
    }

    private fun initView() {

        val data = DataServer.getTempItemData()
        mAdapter = TemplateCardAdapter(data)
        mAdapter.onItemChildClickListener = this
        rv_temp_card.layoutManager = GridLayoutManager(this, 4)
        mAdapter.setSpanSizeLookup { _, position -> data[position].spanSize }
        rv_temp_card.adapter = mAdapter
    }

//    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
////        val contentView = view.findViewById<LinearLayout>(R.id.content_view)
//        val intent = Intent(mActivity, CreationEditorActivity::class.java)
//        intent.putExtra("position", position)
//        mActivity.startActivity(intent)
//    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
        when(view.id){
            R.id.es,R.id.content_view,R.id.temp_img ->{
                mActivity.startActivity(CreationEditorActivity::class.java){
                    it.putExtra("TempId",position)
                }
                finish()
            }
        }
    }

    override fun createPresenter(): TemplatePresenter {
        return TemplatePresenter(this)
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun getDataSuccess(bean: List<CardBean>?) {
        //若有请求返回则在此处理
    }

}