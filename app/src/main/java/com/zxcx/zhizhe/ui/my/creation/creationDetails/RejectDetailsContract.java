package com.zxcx.zhizhe.ui.my.creation.creationDetails;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

public interface RejectDetailsContract {
	
	interface View extends NullGetPostView<CardBean> {
	
	}
	
	interface Presenter extends INullGetPostPresenter<CardBean> {

	}
}

