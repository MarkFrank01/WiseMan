package com.zxcx.zhizhe.ui.card.card.cardDetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zxcx.zhizhe.R

/**
 * Created by anm on 2017/10/30.
 */
class OldCardDetailsAdapter(val mList: List<OldCardDetailsBean>) : RecyclerView.Adapter<OldCardDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.view_line, p0, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(p0: ViewHolder?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text:TextView = itemView.findViewById(R.id.tv_about_us_share) as TextView
    }
}