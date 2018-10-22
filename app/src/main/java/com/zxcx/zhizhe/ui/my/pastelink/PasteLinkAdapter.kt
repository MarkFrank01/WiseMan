package com.zxcx.zhizhe.ui.my.pastelink

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R

class PasteLinkAdapter(data: List<PastLinkBean>) : BaseQuickAdapter<PastLinkBean, BaseViewHolder>(R.layout.item_past_link, data) {

    lateinit var mListener: PasteLinkClickListener

    init {
        setHeaderAndEmpty(true)
    }


    override fun convert(helper: BaseViewHolder, item: PastLinkBean?) {

        helper.addOnClickListener(R.id.ll_paste_cancel)

        helper.addOnClickListener(R.id.et_pates_link)


        val editText = helper.getView<AppCompatEditText>(R.id.et_pates_link)
        val etcheck = helper.getView<TextInputLayout>(R.id.et_check)

        if (editText.getTag(R.id.et_pates_link) is TextWatcher) {
            editText.removeTextChangedListener(editText.getTag(R.id.et_pates_link) as TextWatcher)
        }

        editText.setText(item?.link)

        if (editText.text.toString().trim() == "") {
            etcheck.error = "请添加你的作品链接"
        }

        editText.isCursorVisible = true
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true

        val textWatcher: TextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                item?.link = s.toString()
                mListener.onClickSave(mData.indexOf(item))

                if (editText.text.toString().trim() == "") {
                    etcheck.isErrorEnabled = true
                    etcheck.error = "请添加链接"
                    mListener.onItemIsNull(true)

                } else {
                    etcheck.hint = ""
                    etcheck.error = ""
                    etcheck.isErrorEnabled = false
                    mListener.onItemIsNull(false)

                    if (s.toString().length > 20) {
                        editText.isCursorVisible = false
                        editText.isFocusable = false
                        editText.isFocusableInTouchMode = false

                        editText.setOnClickListener {
                            Toast.makeText(mContext, "Ready Go", Toast.LENGTH_SHORT).show()
                        }
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

    }


}