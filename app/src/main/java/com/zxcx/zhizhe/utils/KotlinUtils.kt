package com.zxcx.zhizhe.utils

import android.content.Context
import android.content.Intent

/**
 * Created by anm on 2018/2/26.
 */
fun <T> Context?.startActivity(clazz: Class<T>, action: (intent: Intent) -> Unit){
    if (this == null) {
        return
    }
    val intent = Intent(this, clazz)
    action(intent)
    this.startActivity(intent)
}