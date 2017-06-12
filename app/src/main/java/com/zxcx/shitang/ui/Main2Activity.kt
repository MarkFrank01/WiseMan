package com.zxcx.shitang.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.shitang.R
import com.zxcx.shitang.widget.GetPicBottomDialog

class Main2Activity : AppCompatActivity() {


    @BindView(R.id.btn_ceshi)
    internal var mBtnCeshi: Button? = null
    private val mList = ArrayList<MultiItemEntity>()

    fun reformat(str: String,
                 normalizeCase: Boolean = true,
                 upperCaseFirstLetter: Boolean = true,
                 divideByCamelHumps: Boolean = false,
                 wordSeparator: Char = ' ') {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_ceshi)
    fun onViewClicked() {
        GetPicBottomDialog(this).show()
        data?.let {
            // 代码会执⾏到此处, 假如data不为null

        }
    }

    val data = 0

}
