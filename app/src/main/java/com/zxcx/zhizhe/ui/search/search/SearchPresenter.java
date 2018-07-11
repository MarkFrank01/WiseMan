package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.NonNull;
import com.zxcx.zhizhe.mvpBase.BasePresenter;
import java.util.List;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements
	SearchContract.Presenter {

	private final SearchModel mModel;

	public SearchPresenter(@NonNull SearchContract.View view) {
		attachView(view);
		mModel = new SearchModel(this);
	}

	public void getSearchBean() {
		mModel.getSearchBean();
	}
	
	public void getSearchDefaultKeyword() {
		mModel.getSearchDefaultKeyword();
	}

	public void deleteAllSearchHistory() {
		mModel.deleteAllSearchHistory();
	}

	public void getSearchPre(String keyword) {
		mModel.getSearchPre(keyword);
	}

	@Override
	public void getDataSuccess(SearchBean bean) {
		mView.getDataSuccess(bean);
	}

	@Override
	public void getSearchPreSuccess(List<String> list) {
		mView.getSearchPreSuccess(list);
	}
	
	@Override
	public void getSearchDefaultKeywordSuccess(HotSearchBean bean) {
		mView.getSearchDefaultKeywordSuccess(bean);
	}
	
	@Override
	public void deleteHistorySuccess() {
		mView.deleteHistorySuccess();
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

