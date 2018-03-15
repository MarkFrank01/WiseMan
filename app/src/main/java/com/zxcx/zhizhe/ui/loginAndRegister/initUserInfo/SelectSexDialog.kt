package com.zxcx.zhizhe.ui.loginAndRegister.initUserInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeSexEvent
import com.zxcx.zhizhe.mvpBase.CommonDialog
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.dialog_select_sex.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by anm on 2017/7/21.
 */

class SelectSexDialog : CommonDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_phone_confirm, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sex = arguments.getInt("sex",1)

        if (sex == 1){
            tv_dialog_select_sex_man.setTextColor(activity.getColorForKotlin(R.color.button_blue))
            tv_dialog_select_sex_woman.setTextColor(activity.getColorForKotlin(R.color.text_color_1))
        }else{
            tv_dialog_select_sex_man.setTextColor(activity.getColorForKotlin(R.color.text_color_1))
            tv_dialog_select_sex_woman.setTextColor(activity.getColorForKotlin(R.color.button_blue))
        }

        tv_dialog_select_sex_man.setOnClickListener {
            EventBus.getDefault().post(ChangeSexEvent(1))
            dismiss()
        }
        tv_dialog_select_sex_woman.setOnClickListener {
            EventBus.getDefault().post(ChangeSexEvent(0))
            dismiss()
        }
    }
}
