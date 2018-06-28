package com.zxcx.zhizhe.ui.my.note.noteDetails;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

public interface NoteDetailsContract {
	
	interface View extends GetView<NoteDetailsBean> {
	
	}
	
	interface Presenter extends IGetPresenter<NoteDetailsBean> {
	
	}
}

