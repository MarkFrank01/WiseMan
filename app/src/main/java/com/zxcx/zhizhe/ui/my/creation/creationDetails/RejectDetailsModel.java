package com.zxcx.zhizhe.ui.my.creation.creationDetails;

import android.support.annotation.NonNull;
import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

public class RejectDetailsModel extends BaseModel<RejectDetailsContract.Presenter> {

	public RejectDetailsModel(@NonNull RejectDetailsContract.Presenter present) {
		this.mPresenter = present;
	}

	public void getRejectDetails(int RejectId) {
		mDisposable = AppClient.getAPIService().getRejectDetails(RejectId)
			.compose(BaseRxJava.INSTANCE.io_main())
			.compose(BaseRxJava.INSTANCE.handleResult())
			.subscribeWith(new BaseSubscriber<CardBean>(mPresenter) {
				@Override
				public void onNext(CardBean bean) {
					mPresenter.getDataSuccess(bean);
				}
			});
		addSubscription(mDisposable);
	}

	public void getReviewDetails(int RejectId) {
		mDisposable = AppClient.getAPIService().getRejectDetails(RejectId)
			.compose(BaseRxJava.INSTANCE.io_main())
			.compose(BaseRxJava.INSTANCE.handleResult())
			.subscribeWith(new BaseSubscriber<CardBean>(mPresenter) {
				@Override
				public void onNext(CardBean bean) {
					mPresenter.getDataSuccess(bean);
				}
			});
		addSubscription(mDisposable);
	}

	public void getDraftDetails(int RejectId) {
		mDisposable = AppClient.getAPIService().getRejectDetails(RejectId)
			.compose(BaseRxJava.INSTANCE.io_main())
			.compose(BaseRxJava.INSTANCE.handleResult())
			.subscribeWith(new BaseSubscriber<CardBean>(mPresenter) {
				@Override
				public void onNext(CardBean bean) {
					mPresenter.getDataSuccess(bean);
				}
			});
		addSubscription(mDisposable);
	}
	
	public void submitReview(int noteId, int styleType) {
		mDisposable = AppClient.getAPIService().saveFreeNode(noteId, styleType, 1)
			.compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
			.compose(BaseRxJava.INSTANCE.handlePostResult())
			.subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {

				@Override
				public void onNext(BaseBean bean) {
					mPresenter.postSuccess();
				}
			});
		addSubscription(mDisposable);
	}

	public void deleteCard(int cardId) {
		mDisposable = AppClient.getAPIService().deleteCard(cardId)
			.compose(BaseRxJava.INSTANCE.io_main_loading(mPresenter))
			.compose(BaseRxJava.INSTANCE.handlePostResult())
			.subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {

				@Override
				public void onNext(BaseBean bean) {
					mPresenter.postSuccess();
				}
			});
		addSubscription(mDisposable);
	}
}


