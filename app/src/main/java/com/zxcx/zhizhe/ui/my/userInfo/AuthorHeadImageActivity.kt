package com.zxcx.zhizhe.ui.my.userInfo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_head_image.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by anm on 2017/12/13.
 * 修改头像页面
 */

class AuthorHeadImageActivity : BaseActivity() {

	private var isChanged = false
	private var path: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_head_image)
//		initToolBar("修改头像")

		iv_toolbar_right.visibility = View.GONE
		iv_toolbar_right.setImageResource(R.drawable.iv_toolbar_more)

		val imageUrl =intent.getStringExtra("avatarAvatar")
		ImageLoader.load(this, imageUrl, R.drawable.iv_my_head_placeholder, iv_head_image)
	}

	override fun onBackPressed() {
		super.onBackPressed()
	}



	override fun setListener() {
		super.setListener()
		iv_toolbar_back.setOnClickListener{
			onBackPressed()
		}
	}
}
