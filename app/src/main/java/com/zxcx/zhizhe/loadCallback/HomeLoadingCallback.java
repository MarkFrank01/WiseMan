package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

public class HomeLoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_home_loading;
    }
}
