package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity

class ApplyReviewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_review)
        initToolBar("申请创作")
    }
}
