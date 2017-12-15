package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.classify.*
import kotlinx.android.synthetic.main.activity_select_card_bag.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

/**
 * 创作-卡包选择页
 */
class SelectCardBagActivity : MvpActivity<ClassifyPresenter>(), ClassifyContract.View, BaseQuickAdapter.OnItemChildClickListener{

    lateinit var selectCardBagAdapter: SelectCardBagAdapter
    private var cardBagId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_card_bag)
        initToolBar("选择卡包")

        tv_toolbar_right.text = "开始创作"
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.color_text_enable_blue))
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setOnClickListener {
            //进入编辑器页面
            val intent = Intent(mActivity, NewCreationEditorActivity::class.java)
            intent.putExtra("title", getIntent().getStringExtra("title"))
            intent.putExtra("imageUrl", getIntent().getStringExtra("imageUrl"))
            intent.putExtra("cardBagId", cardBagId)
            startActivity(intent)
        }

        initRecyclerView()
        mPresenter.getClassify()
    }

    override fun createPresenter(): ClassifyPresenter {
        return ClassifyPresenter(this)
    }

    override fun getDataSuccess(list: List<ClassifyBean>) {
        for (bean in list) {
            selectCardBagAdapter.data.add(bean)
            selectCardBagAdapter.data.addAll(bean.dataList)
        }
        selectCardBagAdapter.notifyDataSetChanged()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        val bean = adapter.data[position] as ClassifyCardBagBean
        cardBagId = bean.id
        selectCardBagAdapter.selectCardBagId = cardBagId
        selectCardBagAdapter.notifyDataSetChanged()
        tv_toolbar_right.isEnabled = true
    }

    private fun initRecyclerView() {
        selectCardBagAdapter = SelectCardBagAdapter(ArrayList())
        selectCardBagAdapter.onItemChildClickListener = this
        var manager = GridLayoutManager(mActivity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (selectCardBagAdapter.getItemViewType(position) == ClassifyCardBagBean.TYPE_CARD_BAG) {
                    1
                } else {
                    manager.spanCount
                }
            }
        }
        rv_select_card_bag.adapter = selectCardBagAdapter
        rv_select_card_bag.layoutManager = manager
        rv_select_card_bag.addItemDecoration(ClassifyItemDecoration())
    }
}
