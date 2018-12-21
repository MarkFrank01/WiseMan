package com.zxcx.zhizhe.ui.welcome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zxcx.zhizhe.service.version_update.update_utils.AppUtils
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.startActivity
import java.util.*

/**
 * 启动闪屏页
 */

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(WelcomeActivity::class.java) {}

        val maidian = "android_" + android.os.Build.MODEL + "_" +
                android.os.Build.VERSION.SDK_INT + "_" +
                AppUtils.getVersionName(this) + "_" +
                System.currentTimeMillis() + "_" +
                getStringRanom(6)

        if (Utils.getIsFirstLaunchApp()) {
            SharedPreferencesUtil.saveData("maidian", maidian)
        }

        LogCat.e(SharedPreferencesUtil.getString("maidian",""))

        finish()
    }


    fun getStringRanom(length: Int): String {
        var num = ""
        val random = Random()

        //参数length，表示生成几位随机数
        for (i in 0 until length) {

            val charOrNum = if (random.nextInt(2) % 2 === 0) "char" else "num"
            //输出字母还是数字
            if ("char".equals(charOrNum, ignoreCase = true)) {
                //输出是大写字母还是小写字母
                val temp = if (random.nextInt(2) % 2 === 0) 65 else 97
                num += (random.nextInt(26) + temp).toChar()
            } else if ("num".equals(charOrNum, ignoreCase = true)) {
                num += random.nextInt(10)
            }
        }

        return num
    }
}