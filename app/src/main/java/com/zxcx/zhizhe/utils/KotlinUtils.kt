package com.zxcx.zhizhe.utils

import android.content.Context
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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

fun Context.getColorForKotlin(@ColorRes resId: Int): Int{
    return ContextCompat.getColor(this,resId)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}