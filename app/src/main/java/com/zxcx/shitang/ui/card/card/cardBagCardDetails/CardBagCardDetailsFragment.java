package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseFragment;
import com.zxcx.shitang.retrofit.APIService;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CardBagCardDetailsFragment extends BaseFragment {

    private static CardBagCardDetailsFragment fragment;

    @BindView(R.id.fl_card_details)
    FrameLayout mFlCardDetails;

    private WebView mWebView;
    private int cardId;
    private Unbinder unbinder;

    public static CardBagCardDetailsFragment newInstance(int cardId) {
        if (fragment == null || fragment.getArguments().getInt("id") != cardId) {
            Bundle args = new Bundle();
            args.putInt("id", cardId);
            fragment = new CardBagCardDetailsFragment();
            fragment.setArguments(args);
        }
//        CardBagCardDetailsFragment fragment = new CardBagCardDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_details, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            cardId = arguments.getInt("id", 0);
        } else {
            toastShow("获取内容出错");
            return;
        }

        mWebView = WebViewUtils.getWebView(mActivity);
        mFlCardDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        if (isNight) {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleDark/" + cardId);
        } else {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        super.onDestroy();
    }
}