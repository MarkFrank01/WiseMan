package com.zxcx.zhizhe.ui.my.feedback.feedback

import android.os.Bundle
import butterknife.ButterKnife
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : MvpActivity<FeedbackPresenter>(), FeedbackContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        ButterKnife.bind(this)
    }

    override fun onDestroy() {
        Utils.closeInputMethod(this)
        super.onDestroy()
    }

    override fun createPresenter(): FeedbackPresenter {
        return FeedbackPresenter(this)
    }

    override fun postSuccess() {
        toastShow("反馈已提交，感谢你的支持")
        finish()
    }

    override fun postFail(msg: String) {
        toastShow(msg)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        tv_feedback_commit.setOnClickListener {
            val content = et_feedback_content.text.toString()
            val appType = Constants.APP_TYPE
            val appChannel = WalleChannelReader.getChannel(this)
            val appVersion = Utils.getAppVersionName(this)
            mPresenter.feedback(content, appType, appChannel, appVersion)
        }

        et_feedback_content.afterTextChanged {
            tv_feedback_commit.isEnabled = it.isNotEmpty()
            tv_feedback_residue.text = getString(R.string.tv_feedback_residue,it.length,600-it.length)
            if(it.length == 600){
                tv_feedback_residue.setTextColor(mActivity.getColorForKotlin(R.color.red))
            }else{
                tv_feedback_residue.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
            }
        }
    }
}
