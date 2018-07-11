package com.zxcx.zhizhe.ui.search.search;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import java.util.List;

public interface SearchContract {

	interface View extends GetView<SearchBean> {

		void getSearchPreSuccess(List<String> list);
		
		void getSearchDefaultKeywordSuccess(HotSearchBean bean);

		void deleteHistorySuccess();
	}

	interface Presenter extends IGetPresenter<SearchBean> {

		void getSearchPreSuccess(List<String> list);
		
		void getSearchDefaultKeywordSuccess(HotSearchBean bean);

		void deleteHistorySuccess();
	}
}

