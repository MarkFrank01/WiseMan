package com.zxcx.zhizhe.ui.my.setting

import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.CustomSeekbar
import kotlinx.android.synthetic.main.activity_text_size_change.*

class TextSizeChangeActivity : BaseActivity(), CustomSeekbar.ResponseOnTouch {

    private var textSizeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_size_change)

        textSizeValue = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue,1)

        val section = ArrayList<String>()
        section.add("默认")
        section.add("中号")
        section.add("大号")
        sb_text_size.initData(section)
        sb_text_size.setResponseOnTouch(this)

        sb_text_size.setProgress(textSizeValue - 1)
        when(textSizeValue){
            1 -> {
                tv_text_size_change.textSize = 17f
            }
            2 -> {
                tv_text_size_change.textSize = 20f
            }
            3 -> {
                tv_text_size_change.textSize = 24f
            }
        }
    }

    override fun onTouchResponse(volume: Int) {
        textSizeValue = volume + 1
        when(volume){
            0 -> {
                tv_text_size_change.textSize = 17f
            }
            1 -> {
                tv_text_size_change.textSize = 20f
            }
            2 -> {
                tv_text_size_change.textSize = 24f
            }
        }
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        tv_size_change_save.setOnClickListener {
            SharedPreferencesUtil.saveData(SVTSConstants.textSizeValue,textSizeValue)
            toastShow("保存成功")
            onBackPressed()
        }
    }
}
