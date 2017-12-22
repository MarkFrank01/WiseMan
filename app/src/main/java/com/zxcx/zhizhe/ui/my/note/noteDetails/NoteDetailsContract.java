package com.zxcx.zhizhe.ui.my.note.noteDetails;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;

public interface NoteDetailsContract {

    interface View extends NullGetPostView<NoteDetailsBean> {

    }

    interface Presenter extends INullGetPostPresenter<NoteDetailsBean> {

    }
}

