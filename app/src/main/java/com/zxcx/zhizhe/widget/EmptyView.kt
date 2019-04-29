package com.zxcx.zhizhe.widget

import android.app.Activity
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.allmycircle.EmptyCircleAdapter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.item_other_user_creation.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*
import kotlinx.android.synthetic.main.layout_no_data_and_card.view.*
import kotlinx.android.synthetic.main.layout_no_data_and_circle.view.*

/**
 * Created by anm on 2017/12/15.
 * 获取空白占位页
 */

object EmptyView {
    @JvmStatic
    @Deprecated("不用")
    fun getEmptyView2(context: Context, str1: String, @DrawableRes imgRes: Int): View {
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data_2, null)
        emptyView.tv_no_data_1.text = str1
        emptyView.iv_no_data.setImageResource(imgRes)
        return emptyView
    }

	@JvmStatic
	fun getEmptyView(context: Context, str1: String, @DrawableRes imgRes: Int): View {
		val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null)
		emptyView.tv_no_data_1.text = str1
		emptyView.iv_no_data.setImageResource(imgRes)
		return emptyView
	}

	@JvmStatic
	fun getEmptyViewAndClick(context: Context, str1: String, str2: String, @DrawableRes imgRes: Int, listener: View.OnClickListener?): View {
		val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null)
		emptyView.tv_no_data_1.text = str1
		emptyView.tv_no_data_2.visibility = View.VISIBLE
		emptyView.tv_no_data_2.text = str2
		emptyView.iv_no_data.setImageResource(imgRes)
		emptyView.setOnClickListener(listener)
		return emptyView
	}

    @JvmStatic
    fun getEmptyViewAndClick2(context: Context, str1: String, str2: String, @DrawableRes imgRes: Int, listener: View.OnClickListener?): View {
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null)
        emptyView.tv_no_data_1.text = Html.fromHtml("大神都在这里,<font color='#0088AA'>创建圈子</font>一起玩")

        emptyView.tv_no_data_2.visibility = View.VISIBLE
        emptyView.tv_no_data_2.text = str2
        emptyView.iv_no_data.setImageResource(imgRes)
        emptyView.setOnClickListener(listener)
        return emptyView
    }

	@JvmStatic
	fun getEmptyViewAndCard(activity: Activity, str1: String, @DrawableRes imgRes: Int, bean: CardBean): View {
		val emptyView = LayoutInflater.from(activity).inflate(R.layout.layout_no_data_and_card, null)
		emptyView.tv_no_data_and_card_1.text = str1
		emptyView.iv_no_data_and_card.setImageResource(imgRes)

		val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
		ImageLoader.load(activity, imageUrl, R.drawable.default_card, emptyView.iv_item_card_icon)
		emptyView.tv_item_card_title.text = bean.name
		emptyView.tv_item_card_category.text = bean.categoryName
		emptyView.tv_item_card_label.text = bean.getLabelName()
		emptyView.tv_item_card_read.text = bean.readNum.toString()
		emptyView.tv_item_card_comment.text = bean.commentNum.toString()

		emptyView.fl_no_data_and_card_card.setOnClickListener {
			if (bean.cardType == 1) {
				activity.startActivity(SingleCardDetailsActivity::class.java) {
					it.putExtra("cardBean", bean)
				}
			} else {
				activity.startActivity(ArticleDetailsActivity::class.java) {
					it.putExtra("cardBean", bean)
				}
			}
		}

        emptyView.tv_item_card_time.text = bean.distanceTime

		return emptyView
	}

    @JvmStatic
    fun getEmptyViewAndCircle(activity: Activity,str1: String,@DrawableRes imgRes: Int,list:MutableList<CircleBean>):View{
        val emptyView = LayoutInflater.from(activity).inflate(R.layout.layout_no_data_and_circle, null)
        emptyView.tv_no_data_and_card_circle.text = str1
        emptyView.iv_no_data_and_card_circle.setImageResource(imgRes)

        var mAdapter:EmptyCircleAdapter = EmptyCircleAdapter(ArrayList())
        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as CircleBean
            activity.startActivity(CircleDetaileActivity::class.java) {
                it.putExtra("circleID", bean.id)
            }
        }

        emptyView.rv_empty_circle.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        emptyView.rv_empty_circle.adapter = mAdapter

        mAdapter.setNewData(list)

        return emptyView
    }
}
