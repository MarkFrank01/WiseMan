package com.zxcx.zhizhe.ui.welcome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zxcx.zhizhe.utils.startActivity

class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		startActivity(WelcomeActivity::class.java) {}
		finish()
	}
}