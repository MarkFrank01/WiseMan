package com.zxcx.zhizhe.ui.circle.circlequestion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleSixPicBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog22
import kotlinx.android.synthetic.main.activity_circle_question.*

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description : 提问的发布页面。多图待处理
 */
class CircleQuestionActivity : MvpActivity<CircleQuestionPresenter>(), CircleQuestionContract.View,
        OSSDialog22.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener,
        BGASortableNinePhotoLayout.Delegate {

    //////////////////////////
    private val RC_CHOOSE_PHOTO = 1
    private val RC_PHOTO_PREVIEW = 2

//////////////////////////



    private lateinit var mOSSDialog: OSSDialog22

    //圈子ID
    private var circleID = 0

    //图片的数据
    private var mPickData: MutableList<CircleSixPicBean> = mutableListOf()

    //回传的图片的集合
    private var mAllImgs : ArrayList<String> = arrayListOf()

    //传递到接口的集合
    private var mPushImgs :ArrayList<String> = arrayListOf()


    //拖拽排序九宫格控件
    private lateinit var mPhotosSnpl: BGASortableNinePhotoLayout

    //监听
    val textWatcher1: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            tv_toolbar_right.isEnabled = true

            tv_check1.text = "${s.toString().length}/18"
            tv_check1.setTextColor(mActivity.getColorForKotlin(R.color.text_color_3))
            LogCat.e("${s.toString()}")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    //标题
    private var my_question:String = ""

    //描述
    private var my_desc:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_question)

        mOSSDialog = OSSDialog22()
        mOSSDialog.setUploadListener(this)

        initData()
        initView()

    }


    override fun createPresenter(): CircleQuestionPresenter {
        return CircleQuestionPresenter(this)
    }

    override fun pushQuestionSuccess() {
        toastShow("提交成功")
        val intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun postSuccess(bean: QuestionBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: QuestionBean?) {
    }

    var num = 1
    override fun uploadSuccess(url: String) {
        mPushImgs.add(url)
        LogCat.e("${num}Url is " + url)

        //待思考处理
//        if (num<mAllImgs.size){
//            toastShow("正在传一下张图")
//            uploadImageToOSS(mAllImgs[num])
//            num++
//        }

        if (num==mAllImgs.size){
            toastShow("图片全部上传完毕")
            mPresenter.pushQuestion(circleID,my_question,my_desc,mPushImgs)
        }

//        if (num == mAllImgs.size){
////            mPresenter.pushQuestion(circleID,)
//        }
//

//        if (mAllImgs.size>0){
//            uploadImageToOSS(mAllImgs[0])
//            mAllImgs.remove(mAllImgs[0])
//        }else{
//            toastShow("球球你")
//        }
    }

    override fun uploadFail(message: String?) {
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType?, uri: Uri?, imagePath: String?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK &&requestCode == RC_CHOOSE_PHOTO&&data!=null){
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
//            mAllImgs = BGAPhotoPickerActivity.getSelectedPhotos(data)
            for (i in BGAPhotoPickerActivity.getSelectedPhotos(data).iterator()){
                mAllImgs.add(i)
            }

        }else if (requestCode == RC_PHOTO_PREVIEW&&data!=null){
            mPhotosSnpl.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)

            for (i in BGAPhotoPickerActivity.getSelectedPhotos(data).iterator()){
                mAllImgs.add(i)
            }
        }
    }

    override fun setListener() {

        //暂无合适办法，待紧急处理

        tv_toolbar_right.setOnClickListener {
//            toastShow("All size is "+mAllImgs.size)
//            uploadUrlsToOSS(mAllImgs)
            my_question = question_title.text.toString().trim()
            my_desc = question_desc.text.toString().trim()

            if (mAllImgs.size>0) {
                uploadImageToOSS(mAllImgs[0])
            }else{
                tv_toolbar_right.isEnabled = false
                mPresenter.pushQuestion(circleID,my_question,my_desc,mAllImgs)
            }

//            if (mAllImgs.size>0) {
//                uploadImageToOSS(mAllImgs[0])
//                mAllImgs.remove(mAllImgs[0])
//            }


        }

        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun uploadUrlsToOSS(path: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 3)
        bundle.putStringArrayList("photoList", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
    }


    private fun initView() {
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos)
        mPhotosSnpl.maxItemCount = 6

        mPhotosSnpl.isPlusEnable = true

        mPhotosSnpl.setDelegate(this)

        tv_toolbar_right.isEnabled = false
        question_title.addTextChangedListener(textWatcher1)
    }

    private fun initData(){
        circleID = intent.getIntExtra("circleID", 0)
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
        mAllImgs.removeAt(position)
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