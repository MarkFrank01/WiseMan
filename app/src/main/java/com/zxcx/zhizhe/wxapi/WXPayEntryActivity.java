package com.zxcx.zhizhe.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.Constants;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/25
 * @Description :
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("SB", "onPayFinish, errCode = " + resp.errCode);
        Log.e("SB", "onPayFinish, errstr = " + resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("回调");
//            builder.setMessage(resp.errCode+"...");
//            builder.show();

            if (resp.errCode == 0){
                toastShow("支付成功");
                finish();
            }

            if (resp.errCode == -1){
                toastShow("支付错误");
                finish();
            }

            if (resp.errCode == -2){
                toastShow("取消支付");
                finish();
            }


        }
    }
}