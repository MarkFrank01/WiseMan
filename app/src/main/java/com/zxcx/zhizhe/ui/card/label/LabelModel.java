package com.zxcx.zhizhe.ui.card.label;

import android.support.annotation.NonNull;
import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean;
import java.util.List;

public class LabelModel extends BaseModel<LabelContract.Presenter> {
	
	public LabelModel(@NonNull LabelContract.Presenter present) {
		this.mPresenter = present;
	}
	
	public void getLabelList(int id, int page, int pageSize) {
		mDisposable = AppClient.getAPIService().getLabelList(id, page, pageSize)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
			.subscribeWith(new BaseSubscriber<List<ArticleAndSubjectBean>>(mPresenter) {
                    @Override
                    public void onNext(List<ArticleAndSubjectBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
		addSubscription(mDisposable);
	}

}


