package com.zxcx.zhizhe.ui.card.label;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean;
import java.util.List;

public interface LabelContract {
	
	interface View extends GetView<List<ArticleAndSubjectBean>> {
	
	}
	
	interface Presenter extends IGetPresenter<List<ArticleAndSubjectBean>> {

	}
}

