package com.zxcx.zhizhe.ui.my.pastelink

interface PasteLinkClickListener {
    fun onClickSave(position:Int,url:String)
    fun onItemIsNull(isNull:Boolean)
}

