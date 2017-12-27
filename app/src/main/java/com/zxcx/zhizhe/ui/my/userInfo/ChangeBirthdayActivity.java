package com.zxcx.zhizhe.ui.my.userInfo;

import android.os.Bundle;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.DatePickerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by anm on 2017/7/6.
 */

public class ChangeBirthdayActivity extends BaseActivity implements IPostPresenter<UserInfoBean> {

    @BindView(R.id.dpl_date_picker)
    DatePickerLayout mDplDatePicker;
    private String mBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_birthday);
        ButterKnife.bind(this);

        mBirth = SharedPreferencesUtil.getString(SVTSConstants.birthday,"");
        if (StringUtils.isEmpty(mBirth)) {
            mBirth = DateTimeUtils.getDate();
        }
        mBirth = mBirth + " 00:00";
        /*ChangeBirthdayDialog changeBirthdayDialog = new ChangeBirthdayDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        changeBirthdayDialog.setArguments(bundle);
        changeBirthdayDialog.show(getFragmentManager(), "");*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        mDplDatePicker.setSartAndEndTime("1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        mDplDatePicker.showSpecificTime(false); // 不显示时和分
        mDplDatePicker.setIsLoop(false); // 不允许循环滚动
        mDplDatePicker.setNow(mBirth);
    }

    public void changeBirthday(String birth) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, birth, null)
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .compose(BaseRxJava.<UserInfoBean>io_main_loading(this))
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        postSuccess(bean);
                    }
                });
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        ZhiZheUtils.saveUserInfo(bean);
        toastShow(R.string.user_info_change);
        onBackPressed();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.tv_complete)
    public void onMTvCompleteClicked() {
        String date = mDplDatePicker.getSelectTime().split(" ")[0];
        changeBirthday(date);
    }
}
