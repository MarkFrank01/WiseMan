package com.zxcx.zhizhe.ui.my.pastelink

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.WebAddress

class PasteLinkAdapter(data: List<PastLinkBean>) : BaseQuickAdapter<PastLinkBean, BaseViewHolder>(R.layout.item_past_link, data) {

    lateinit var mListener: PasteLinkClickListener

    init {
        setHeaderAndEmpty(true)
    }


    override fun convert(helper: BaseViewHolder, item: PastLinkBean?) {

        helper.addOnClickListener(R.id.ll_paste_cancel)


        val editText = helper.getView<AppCompatEditText>(R.id.et_pates_link)
        val etcheck = helper.getView<TextInputLayout>(R.id.et_check)

        if (editText.getTag(R.id.et_pates_link) is TextWatcher) {
            editText.removeTextChangedListener(editText.getTag(R.id.et_pates_link) as TextWatcher)
        }

        editText.setText(item?.link)



        mListener.onClickSave(mData.indexOf(item))

        mListener.onItemIsNull(true)
        if (item?.link.toString() == "" || item?.link.isNullOrEmpty()) {
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

                    editText.isCursorVisible = true
                    editText.isFocusable = true
                    editText.isFocusableInTouchMode = true

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

                        var web = WebAddress(editText.text.toString().trim())
                        LogCat.e(web.toString())
                        LogCat.e(web.path)

                        WebAddress(editText.text.toString().trim()).toString()
                        if (web.path.length < 5) {
                            Toast.makeText(mContext, "链接无效", Toast.LENGTH_SHORT).show()
                        }

                    } else {
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


        helper.getView<LinearLayout>(R.id.ll_paste_cancel).setOnClickListener {
            editText.setText("")
        }
    }


}