package com.zxcx.zhizhe.ui.my.setting

import android.os.Bundle
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_image_load_mode.*

/**
 * Created by anm on 2017/12/13.
 */

class ImageLoadModeActivity : BaseActivity() {
	private var imageLoadMode: Int = 0  //0-仅WIFI下加载高清图，1-所有网络加载高清图，2-永远不加载高清图

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_image_load_mode)
		ButterKnife.bind(this)

		imageLoadMode = SharedPreferencesUtil.getInt(SVTSConstants.imageLoadMode, 1)
		when (imageLoadMode) {
			0 -> cb_image_load_mode_wifi.isChecked = true
			1 -> cb_image_load_mode_all.isChecked = true
			2 -> {
				cb_image_load_mode_wifi.isChecked = false
				cb_image_load_mode_all.isChecked = false
			}
		}
	}

	override fun setListener() {
		iv_common_close.setOnClickListener {
			onBackPressed()
		}

		ll_image_load_mode_wifi.setOnClickListener {
			cb_image_load_mode_wifi.isChecked = !cb_image_load_mode_wifi.isChecked
		}

		ll_image_load_mode_all.setOnClickListener {
			cb_image_load_mode_all.isChecked = !cb_image_load_mode_all.isChecked
		}

		cb_image_load_mode_wifi.setOnCheckedChangeListener { buttonView, isChecked ->
			if (isChecked) {
				imageLoadMode = 0
				SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode, imageLoadMode)
				cb_image_load_mode_all.isChecked = false
			} else {
				if (!isChecked && !cb_image_load_mode_all.isChecked) {
					imageLoadMode = 2
					SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode, imageLoadMode)
				}
			}
		}

		cb_image_load_mode_all.setOnCheckedChangeListener { buttonView, isChecked ->
			if (isChecked) {
				imageLoadMode = 1
				SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode, imageLoadMode)
				cb_image_load_mode_wifi.isChecked = false
			} else {
				if (!cb_image_load_mode_wifi.isChecked && !isChecked) {
					imageLoadMode = 2
					SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode, imageLoadMode)
				}
			}
		}
	}

}
