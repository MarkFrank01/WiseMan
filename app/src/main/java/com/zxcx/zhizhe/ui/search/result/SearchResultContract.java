package com.zxcx.zhizhe.ui.search.result;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.GetView;

import java.util.List;

public interface SearchResultContract {

    interface View extends GetView<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }

    interface Presenter extends IGetPresenter<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }
}

