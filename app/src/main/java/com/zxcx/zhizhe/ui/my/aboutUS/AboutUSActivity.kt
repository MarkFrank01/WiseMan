package com.zxcx.zhizhe.ui.my.aboutUS

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUSActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        ButterKnife.bind(this)

        tv_about_us_versions_top.text = getString(R.string.tv_about_us_versions, Utils.getAppVersionName(this))
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        tv_about_us_evaluate.setOnClickListener {
            try {
                val uri = Uri.parse("market://details?id=$packageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                toastShow("您没有安装应用市场")
            }
        }

        tv_about_share.setOnClickListener {
            val shareCardDialog = ShareDialog()
            val bundle = Bundle()
            bundle.putString("title", getString(R.string.app_name))
            bundle.putString("text", "只看实用知识")
            bundle.putString("url", "http://a.app.qq.com/o/simple.jsp?pkgname=com.zxcx.zhizhe")
            bundle.putString("imageUrl", "http://zhizhe-prod.oss-cn-shenzhen.aliyuncs.com/Icon_1024.png")
            shareCardDialog.arguments = bundle
            shareCardDialog.show(fragmentManager, "")
        }
    }
}
