package com.zxcx.zhizhe.ui.search.search;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.room.SearchHistory;

import java.util.List;

public interface SearchContract {

    interface View extends GetView<List<SearchBean>> {
        void getSearchHistorySuccess(List<SearchHistory> list);
        void getSearchPreSuccess(List<String> list);
    }

    interface Presenter extends IGetPresenter<List<SearchBean>> {
        void getSearchHistorySuccess(List<SearchHistory> list);
        void getSearchPreSuccess(List<String> list);
    }
}

