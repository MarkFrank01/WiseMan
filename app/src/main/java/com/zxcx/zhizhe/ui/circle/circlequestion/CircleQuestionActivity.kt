package com.zxcx.zhizhe.ui.circle.circlequestion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleQuestionAdapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleSixPicBean
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
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BGASortableNinePhotoLayout.Delegate {

    //////////////////////////
    private val RC_CHOOSE_PHOTO = 1
    private val RC_PHOTO_PREVIEW = 2

//////////////////////////

    private lateinit var mOSSDialog: OSSDialog22

    private lateinit var mAdapter: CircleQuestionAdapter

    //图片的数据
    private var mPickData: MutableList<CircleSixPicBean> = mutableListOf()



    //拖拽排序九宫格控件
    private lateinit var mPhotosSnpl: BGASortableNinePhotoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_question)

        mOSSDialog = OSSDialog22()
        mOSSDialog.setUploadListener(this)

        initView()
        initRecyclerView()

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

        if (resultCode == Activity.RESULT_OK &&requestCode == RC_CHOOSE_PHOTO&&data!=null){
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
        }else if (requestCode == RC_PHOTO_PREVIEW&&data!=null){
            mPhotosSnpl.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
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

    private fun initView() {
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos)
        mPhotosSnpl.maxItemCount = 6

        mPhotosSnpl.isPlusEnable = true

        mPhotosSnpl.setDelegate(this)
    }


    override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: java.util.ArrayList<String>?) {
        var photoPickerPreviewIntent: Intent = BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                //当前预览的图片路径集合
                .previewPhotos(models)
                //当前已选中的图片路径集合
                .selectedPhotos(models)
                //图片选择张数的最大值
                .maxChooseCount(mPhotosSnpl.maxItemCount)
                //当前预览图片的索引
                .currentPosition(position)
                .isFromTakePhoto(false)
                .build()
        startActivityForResult(photoPickerPreviewIntent,RC_PHOTO_PREVIEW)
    }

    override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, models: java.util.ArrayList<String>?) {
        choicePhotoWrapper()
    }

    override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, fromPosition: Int, toPosition: Int, models: java.util.ArrayList<String>?) {
    }

    override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: java.util.ArrayList<String>?) {
        mPhotosSnpl.removeItem(position)
    }


    private fun choicePhotoWrapper() {
        var photoPickerIntent: Intent = BGAPhotoPickerActivity.IntentBuilder(this)
                //选择图片的最大数
                .maxChooseCount(mPhotosSnpl.maxItemCount - mPhotosSnpl.itemCount)
                //当前已选中的图片路径合集
                .selectedPhotos(null)
                //滚动列表时是否暂停再加载图片
                .pauseOnScroll(false)
                .build()
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO)
    }


}