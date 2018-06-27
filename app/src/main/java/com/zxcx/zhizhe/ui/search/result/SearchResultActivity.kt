package com.zxcx.zhizhe.ui.search.result

import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity

class SearchResultActivity : BaseActivity() {

    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
    }
}
