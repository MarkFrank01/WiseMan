package com.zxcx.zhizhe.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.LayoutInflater
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*
import kotlinx.android.synthetic.main.layout_no_data_and_card.view.*

/**
 * Created by anm on 2017/12/15.
 */

object EmptyView {
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
    fun getEmptyViewAndCard(activity: Activity, str1: String, @DrawableRes imgRes: Int, bean: CardBean): View {
        val emptyView = LayoutInflater.from(activity).inflate(R.layout.layout_no_data_and_card, null)
        emptyView.tv_no_data_and_card_1.text = str1
        emptyView.iv_no_data_and_card.setImageResource(imgRes)

        val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
        ImageLoader.load(activity, imageUrl, R.drawable.default_card, emptyView.iv_item_card_icon)
        emptyView.tv_item_card_title.text = bean.name
        emptyView.tv_item_card_category.text = bean.cardCategoryName

        emptyView.fl_no_data_and_card_card.setOnClickListener {
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    Pair.create(emptyView.iv_item_card_icon, "cardImage"),
                    Pair.create(emptyView.tv_item_card_title, "cardTitle"),
                    Pair.create(emptyView.tv_item_card_category, "cardBag")).toBundle()
            val intent = Intent(activity, ArticleDetailsActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            intent.putExtra("imageUrl", bean.imageUrl)
            intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
            intent.putExtra("author", bean.author)
            activity.startActivity(intent, bundle)
        }

        return emptyView
    }
}
