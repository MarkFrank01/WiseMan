package com.zxcx.zhizhe.ui.circle.circlequestion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleQuestionAdapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleSixPicBean
import com.zxcx.zhizhe.utils.GlideEngine
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog22
import kotlinx.android.synthetic.main.activity_circle_question.*

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionActivity : MvpActivity<CircleQuestionPresenter>(), CircleQuestionContract.View,
        OSSDialog22.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    private lateinit var mOSSDialog: OSSDialog22

    private lateinit var mAdapter: CircleQuestionAdapter

    //图片的数据
    private var mPickData: MutableList<CircleSixPicBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_question)

        mOSSDialog = OSSDialog22()
        mOSSDialog.setUploadListener(this)

        initRecyclerView()
    }

    override fun setListener() {
        ll_add_photo.setOnClickListener {
            EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
                    .setCount(6)
                    .start(101)
        }
    }

    override fun createPresenter(): CircleQuestionPresenter {
        return CircleQuestionPresenter(this)
    }

    override fun pushQuestionSuccess() {
    }

    override fun postSuccess(bean: QuestionBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: QuestionBean?) {
    }

    var num = 0
    override fun uploadSuccess(url: String) {
        LogCat.e("${num}Url is " + url)
        num++
    }

    override fun uploadFail(message: String?) {
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType?, uri: Uri?, imagePath: String?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                val resultPhotos = data.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)

                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                val resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS)

                LogCat.e("mPickData size is "+mPickData.size)
                for (i in  mPickData.indices){
                    mAdapter.notifyItemRemoved(i)
                    mAdapter.notifyItemChanged(i)
                }

                mAdapter.notifyDataSetChanged()
                mPickData.clear()
                num = 0
                for (t in resultPaths) {
                    mPickData.add(CircleSixPicBean(t))
                }
                mAdapter.addData(mPickData)
                mAdapter.notifyDataSetChanged()

                uploadUrlsToOSS(resultPaths)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun uploadUrlsToOSS(path: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 3)
        bundle.putStringArrayList("photoList", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
    }

    private fun initRecyclerView() {
        mAdapter = CircleQuestionAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        val layoutManager = GridLayoutManager(this, 2)

        rv_image.layoutManager = layoutManager
        rv_image.adapter = mAdapter
    }
}