package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

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
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog22
import kotlinx.android.synthetic.main.activity_circle_answer.*
import java.util.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerActivity : MvpActivity<CircleAnswerPresenter>(), CircleAnswerContract.View,
        OSSDialog22.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener, BGASortableNinePhotoLayout.Delegate {
    val textWatcher1: TextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {

            if (question_desc.text.toString().length > 1) {
                tv_toolbar_right.isEnabled = true
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private val RC_CHOOSE_PHOTO = 1
    private val RC_PHOTO_PREVIEW = 2

    //圈子Id
    private var circleId: Int = 0

    //qaId
    private var qaId: Int = 0

    //qaCommentId
    //没有的

    //回答
    private var description: String = ""

    //回传的图片的集合
    private var mAllImgs: ArrayList<String> = arrayListOf()

    //传递到接口的集合
    private var mPushImgs: ArrayList<String> = arrayListOf()

    //拖拽排序九宫格控件
    private lateinit var mPhotosSnpl: BGASortableNinePhotoLayout

    private lateinit var mOSSDialog: OSSDialog22


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_answer)

        mOSSDialog = OSSDialog22()
        mOSSDialog.setUploadListener(this)

        initData()
        initView()

    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            description = question_desc.text.toString().trim()

            if (mAllImgs.size>0){
                uploadImageToOSS(mAllImgs[0])
            }else{
                tv_toolbar_right.isEnabled = false
                mPresenter.createAnswer(circleId,qaId,description,mPushImgs)
            }

//            mPresenter.createAnswer(circleId,qaId,description,mPushImgs)
        }
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
    }

    override fun createPresenter(): CircleAnswerPresenter {
        return CircleAnswerPresenter(this)
    }

    override fun createAnswerSuccess() {
        toastShow("已回答")
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

        if (num == mAllImgs.size) {
            toastShow("图片全部上传完毕")
            mPresenter.createAnswer(circleId, qaId, description, mPushImgs)
        }
    }

    override fun uploadFail(message: String?) {
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType?, uri: Uri?, imagePath: String?) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == RC_CHOOSE_PHOTO && data != null) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
//            mAllImgs = BGAPhotoPickerActivity.getSelectedPhotos(data)
            for (i in BGAPhotoPickerActivity.getSelectedPhotos(data).iterator()) {
                mAllImgs.add(i)
            }
        }else if (requestCode == RC_PHOTO_PREVIEW&&data!=null){
            mPhotosSnpl.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)

            for (i in BGAPhotoPickerActivity.getSelectedPhotos(data).iterator()){
                mAllImgs.add(i)
            }
        }
    }

    private fun initData() {
        qaId = intent.getIntExtra("qaId",qaId)
        circleId = intent.getIntExtra("CircleId",circleId)
    }

    private fun initView() {
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos)
        mPhotosSnpl.maxItemCount = 6
        mPhotosSnpl.isPlusEnable = true
        mPhotosSnpl.setDelegate(this)

        tv_toolbar_right.isEnabled = false
        question_desc.addTextChangedListener(textWatcher1)


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