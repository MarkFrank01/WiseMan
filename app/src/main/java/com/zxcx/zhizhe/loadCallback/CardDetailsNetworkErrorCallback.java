package com.zxcx.zhizhe.loadCallback;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

public class CardDetailsNetworkErrorCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.view_network_error;
    }

    @Override
    public void onAttach(Context context, View view) {
        super.onAttach(context, view);
        view.setBackgroundResource(R.color.white);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = ScreenUtils.getScreenWidth();
        lp.height = ScreenUtils.dip2px(347);
    }
}
