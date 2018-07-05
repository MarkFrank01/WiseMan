package com.zxcx.zhizhe.ui.card.label;

import android.support.annotation.NonNull;
import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean;
import java.util.List;

public class LabelPresenter extends BasePresenter<LabelContract.View> implements
	LabelContract.Presenter {
	
	private final LabelModel mModel;
	
	public LabelPresenter(@NonNull LabelContract.View view) {
		attachView(view);
		mModel = new LabelModel(this);
	}

	public void getCardBagCardList(int id, int page, int pageSize) {
		mModel.getLabelList(id, page, pageSize);
	}

	@Override
	public void getDataSuccess(List<ArticleAndSubjectBean> bean) {
		mView.getDataSuccess(bean);
	}

	@Override
	public void getDataFail(String msg) {
		mView.toastFail(msg);
	}

	@Override
	public void showLoading() {
		mView.showLoading();
	}

	@Override
	public void hideLoading() {
		mView.hideLoading();
	}

	@Override
	public void startLogin() {
		mView.startLogin();
	}

	public void detachView() {
		super.detachView();
		mModel.onDestroy();
	}
}

