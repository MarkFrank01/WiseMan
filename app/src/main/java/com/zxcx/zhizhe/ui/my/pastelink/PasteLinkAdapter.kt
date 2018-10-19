package com.zxcx.zhizhe.ui.my.pastelink

import android.text.Editable
import android.text.TextWatcher
import android.util.SparseArray
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R

class PasteLinkAdapter(data: List<PastLinkBean>) : BaseQuickAdapter<PastLinkBean, BaseViewHolder>(R.layout.item_past_link, data) {

    lateinit var mListner: PasteLinkListener

    var eTextArray: SparseArray<String> = SparseArray()

    init {
        setHeaderAndEmpty(true)
    }


    override fun convert(helper: BaseViewHolder, item: PastLinkBean?) {

        helper.addOnClickListener(R.id.ll_paste_cancel)

        helper.addOnClickListener(R.id.et_pates_link)
        helper.getView<EditText>(R.id.et_pates_link).setOnClickListener {
            mListner.onSaveLink(mData.indexOf(item))
        }

        var editText = helper.getView<EditText>(R.id.et_pates_link)
        editText.addTextChangedListener(PasteLinkAdapter.MyTextChangedListener())

    }

    class MyTextChangedListener : TextWatcher {


        override fun afterTextChanged(s: Editable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }


}