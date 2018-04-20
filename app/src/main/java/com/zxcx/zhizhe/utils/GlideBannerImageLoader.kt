package com.zxcx.zhizhe.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.youth.banner.loader.ImageLoader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.widget.CardRoundedImageView


/**
 * Created by anm on 2018/3/19.
 */
class GlideBannerImageLoader: ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        /**
        注意：
        1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
        2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
        传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
        切记不要胡乱强转！
         */

        //Glide 加载图片简单用法
        GlideApp.with(context).load(path).into(imageView)
    }

    override fun createImageView(context: Context?): ImageView {
        val imageView = LayoutInflater.from(context).inflate(R.layout.layout_ad_image, null) as CardRoundedImageView
        /*val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 10, 0, 0)
        imageView.layoutParams = layoutParams*/
        imageView.setPadding(0,ScreenUtils.dip2px(8f),0,0)
        return imageView
    }
}