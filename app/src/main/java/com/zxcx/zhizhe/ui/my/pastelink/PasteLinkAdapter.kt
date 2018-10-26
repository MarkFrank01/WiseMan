package com.zxcx.zhizhe.ui.my.pastelink

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R

class PasteLinkAdapter(data: List<PastLinkBean>) : BaseQuickAdapter<PastLinkBean, BaseViewHolder>(R.layout.item_past_link, data) {

    lateinit var mListener: PasteLinkClickListener
    var mIsClick: Boolean = false

    init {
        setHeaderAndEmpty(true)
    }


    override fun convert(helper: BaseViewHolder, item: PastLinkBean?) {

        helper.addOnClickListener(R.id.ll_paste_cancel)

//        helper.addOnClickListener(R.id.et_pates_link)

        Log.e("mIsClick","FUCK"+mIsClick)


        val editText = helper.getView<AppCompatEditText>(R.id.et_pates_link)
        val etcheck = helper.getView<TextInputLayout>(R.id.et_check)

        if (editText.getTag(R.id.et_pates_link) is TextWatcher) {
            editText.removeTextChangedListener(editText.getTag(R.id.et_pates_link) as TextWatcher)
        }

        editText.setText(item?.link)



        mListener.onClickSave(mData.indexOf(item))


//        editText.isCursorVisible = true
//        editText.isFocusable = true
//        editText.isFocusableInTouchMode = true
        mListener.onItemIsNull(true)
        if (item?.link.toString()==""||item?.link.isNullOrEmpty()){
            editText.isCursorVisible = true
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
        }

        val textWatcher: TextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                item?.link = s.toString()
                mListener.onClickSave(mData.indexOf(item))

                if (editText.text.toString().trim() == "") {
                    etcheck.isErrorEnabled = true
                    mListener.onItemIsNull(true)
                    etcheck.error = "请添加你的作品链接"
                } else {


                    if (s.toString().length > 5) {
                        etcheck.hint = ""
                        etcheck.error = ""
                        etcheck.isErrorEnabled = false
                        mListener.onItemIsNull(false)

                        helper.addOnClickListener(R.id.et_pates_link)

                        editText.isCursorVisible = false
                        editText.isFocusable = false
                        editText.isFocusableInTouchMode = false

                    }else{
//                        mListener.onItemIsNull(true)
                        etcheck.error = "链接长度不够"
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }

        editText.addTextChangedListener(textWatcher)
        editText.setTag(R.id.et_pates_link, textWatcher)

//        if (item?.link.toString().length > 20) {
//            val sbStringGet: String = item?.link.toString().trim()
//            val sbStringShow: String = sbStringGet.substring(0, 20).trim() + "..."
//            editText.setText(sbStringShow)
//        }
    }


}