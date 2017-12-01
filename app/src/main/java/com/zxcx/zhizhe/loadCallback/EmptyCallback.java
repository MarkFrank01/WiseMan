package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_no_data;
    }

}
