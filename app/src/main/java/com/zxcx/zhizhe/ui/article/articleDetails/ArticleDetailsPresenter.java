package com.zxcx.zhizhe.ui.article.articleDetails;

import android.support.annotation.NonNull;
import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

public class ArticleDetailsPresenter extends BasePresenter<ArticleDetailsContract.View> implements
	ArticleDetailsContract.Presenter {

	private final ArticleDetailsModel mModel;

	public ArticleDetailsPresenter(@NonNull ArticleDetailsContract.View view) {
		attachView(view);
		mModel = new ArticleDetailsModel(this);
	}

	public void getCardDetails(int cardId) {
		mModel.getCardDetails(cardId);
	}

	public void readArticle(int cardId) {
		mModel.readArticle(cardId);
	}

	public void likeCard(int cardId) {
		mModel.likeCard(cardId);
	}

	public void removeLikeCard(int cardId) {
		mModel.removeLikeCard(cardId);
	}

	public void unLikeCard(int cardId) {
		mModel.unLikeCard(cardId);
	}

	public void removeUnLikeCard(int cardId) {
		mModel.removeUnLikeCard(cardId);
	}

	public void addCollectCard(int cardId) {
		mModel.addCollectCard(cardId);
	}

	public void removeCollectCard(int cardId) {
		mModel.removeCollectCard(cardId);
	}

	public void setUserFollow(int authorId, int followType) {
		mModel.setUserFollow(authorId, followType);
	}

	@Override
	public void getDataSuccess(CardBean bean) {
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

	@Override
	public void postSuccess(CardBean bean) {
		mView.postSuccess(bean);
	}

	@Override
	public void postFail(String msg) {
		mView.postFail(msg);
	}

	@Override
	public void followSuccess() {
		mView.followSuccess();
	}

	@Override
	public void likeSuccess(CardBean bean) {
		mView.likeSuccess(bean);
	}

	@Override
	public void collectSuccess(CardBean bean) {
		mView.collectSuccess(bean);
	}
}

