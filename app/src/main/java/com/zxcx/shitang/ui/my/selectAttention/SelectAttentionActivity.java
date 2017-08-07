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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectAttentionActivity extends MvpActivity<SelectAttentionPresenter> implements SelectAttentionContract.View,
        BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.tv_select_attention_next)
    TextView mTvSelectAttentionNext;
    @BindView(R.id.rv_select_attention)
    RecyclerView mRvSelectAttention;

    private ArrayList<SelectAttentionBean> mList = new ArrayList<>();
    private ArrayList<SelectAttentionBean> mCheckedList = new ArrayList<>();
    private SelectAttentionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_attention);
        ButterKnife.bind(this);
        getData();
        initRecyclerView();
    }

    private void getData() {
        for (int i = 0; i < 12; i++) {
            if (i%2 == 0) {
                mList.add(new SelectAttentionBean());
            }else {
                SelectAttentionBean bean = new SelectAttentionBean();
                bean.setChecked(true);
                mList.add(bean);
                mCheckedList.add(bean);
            }
        }
        checkNext();
    }

    private void initRecyclerView() {
        mAdapter = new SelectAttentionAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        mRvSelectAttention.setAdapter(mAdapter);
        mRvSelectAttention.setLayoutManager(manager);
    }

    @Override
    protected SelectAttentionPresenter createPresenter() {
        return new SelectAttentionPresenter(this);
    }

    @Override
    public void getDataSuccess(SelectAttentionBean bean) {

    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail() {

    }

    @OnClick(R.id.iv_select_attention_close)
    public void onMIvSelectAttentionCloseClicked() {
        finish();
    }

    @OnClick(R.id.tv_select_attention_next)
    public void onMTvSelectAttentionNextClicked() {
        EventBus.getDefault().post(new SelectAttentionEvent());
        finish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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

    private void checkNext() {
        if (mCheckedList.size()>0){
            mTvSelectAttentionNext.setEnabled(true);
        }else {
            mTvSelectAttentionNext.setEnabled(false);
        }
    }
}
