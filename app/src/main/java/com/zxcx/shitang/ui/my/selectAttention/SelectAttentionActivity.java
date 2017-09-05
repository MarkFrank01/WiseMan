package com.zxcx.shitang.ui.my.selectAttention;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.SelectAttentionEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.my.selectAttention.adapter.SelectAttentionAdapter;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectAttentionActivity extends MvpActivity<SelectAttentionPresenter> implements SelectAttentionContract.View,
        BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.tv_select_attention_next)
    TextView mTvSelectAttentionNext;
    @BindView(R.id.rv_select_attention)
    RecyclerView mRvSelectAttention;

    private ArrayList<SelectAttentionBean> mCheckedList = new ArrayList<>();
    private SelectAttentionAdapter mAdapter;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_attention);
        ButterKnife.bind(this);
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
        initRecyclerView();
        getAttentionList();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
    }

    private void getAttentionList() {
        mPresenter.getAttentionList(mUserId);
    }

    @Override
    public void getDataSuccess(List<SelectAttentionBean> list) {
        mAdapter.addData(list);
        if (mAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    @Override
    public void postSuccess() {
        EventBus.getDefault().postSticky(new SelectAttentionEvent());
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    protected SelectAttentionPresenter createPresenter() {
        return new SelectAttentionPresenter(this);
    }

    @OnClick(R.id.iv_select_attention_close)
    public void onMIvSelectAttentionCloseClicked() {
        finish();
    }

    @OnClick(R.id.tv_select_attention_next)
    public void onMTvSelectAttentionNextClicked() {
        List<Integer> idList = new ArrayList<>();
        for (SelectAttentionBean bean : mCheckedList) {
            idList.add(bean.getId());
        }
        mPresenter.changeAttentionList(mUserId,idList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter,View view, int position) {
        SelectAttentionBean bean = (SelectAttentionBean) adapter.getData().get(position);
        bean.setChecked(!bean.isChecked());
        mAdapter.notifyItemChanged(position);

        if (bean.isChecked()) {
            if (!mCheckedList.contains(bean)) {
                mCheckedList.add(bean);
            }
        } else {
            if (mCheckedList.contains(bean)) {
                mCheckedList.remove(bean);
            }
        }
        checkNext();
    }

    private void initRecyclerView() {
        mAdapter = new SelectAttentionAdapter(new ArrayList<SelectAttentionBean>());
        mAdapter.setOnItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        mRvSelectAttention.setAdapter(mAdapter);
        mRvSelectAttention.setLayoutManager(manager);
    }

    private void checkNext() {
        if (mCheckedList.size()>0){
            mTvSelectAttentionNext.setEnabled(true);
        }else {
            mTvSelectAttentionNext.setEnabled(false);
        }
    }
}
