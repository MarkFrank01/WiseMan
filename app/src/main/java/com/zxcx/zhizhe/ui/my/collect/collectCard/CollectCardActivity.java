package com.zxcx.zhizhe.ui.my.collect.collectCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeCollectFolderNameEvent;
import com.zxcx.zhizhe.event.DeleteCollectCardEvent;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.itemDecoration.CardBagCardItemDecoration;
import com.zxcx.zhizhe.ui.my.collect.collectCard.adapter.CollectCardAdapter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectCardActivity extends MvpActivity<CollectCardPresenter> implements CollectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectCardAdapter.CollectCardCheckListener, BaseQuickAdapter.OnItemClickListener {

    private static final int ACTION_DELETE = 100;
    private static final int ACTION_CHANGE = 101;
    @BindView(R.id.et_collect_folder)
    EditText mEtCollectFolder;
    @BindView(R.id.rv_collect_card)
    RecyclerView mRvCollectCard;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.tv_item_collect_folder_title)
    TextView mTvItemCollectFolderTitle;


    private CollectCardAdapter mAdapter;
    private List<CollectCardBean> mCheckedList = new ArrayList<>();
    private int page = 0;
    private int folderId;
    private int mAction;
    private String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initRecyclerView();
        getCollectCard();

        mTvToolbarRight.setText("编辑");
        mEtCollectFolder.setOnEditorActionListener(new DoneListener());

        initLoadSir();
    }

    public void initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this);
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadService.showCallback(LoadingCallback.class);
        getCollectCard();
    }

    private void initData() {
        String name = getIntent().getStringExtra("name");
        folderId = getIntent().getIntExtra("id", 0);
        mTvItemCollectFolderTitle.setText(name);
        mEtCollectFolder.setText(name);
        mEtCollectFolder.setSelection(mEtCollectFolder.length());
    }

    @Override
    public void onBackPressed() {
        if (!"编辑".equals(mTvToolbarRight.getText().toString())) {
            mTvItemCollectFolderTitle.setVisibility(View.VISIBLE);
            mEtCollectFolder.setVisibility(View.GONE);
            mCheckedList.clear();
            mTvToolbarRight.setText("编辑");
            mAdapter.setDelete(false);
            mAdapter.setOnItemClickListener(this);
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CollectCardPresenter createPresenter() {
        return new CollectCardPresenter(this);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectCard();
    }

    @Override
    public void getDataSuccess(List<CollectCardBean> list) {
        if (page == 0) {
            mAdapter.setNewData(list);
            loadService.showSuccess();
        } else {
            mAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false);
        } else {
            mAdapter.loadMoreComplete();
        }
        if (mAdapter.getData().size() == 0) {
            //占空图
            View view = LayoutInflater.from(mActivity).inflate(R.layout.view_no_data, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_no_data);
            textView.setText(R.string.no_collect_card);
            mAdapter.setEmptyView(view);
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mAdapter.loadMoreFail();
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void postSuccess() {
        if (mAction == ACTION_DELETE) {
            mAdapter.getData().removeAll(mCheckedList);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new DeleteCollectCardEvent(folderId, mCheckedList.size()));
            mCheckedList.clear();
        } else if (mAction == ACTION_CHANGE) {
            mTvItemCollectFolderTitle.setText(newName);
            EventBus.getDefault().post(new ChangeCollectFolderNameEvent(folderId, newName));
            mTvToolbarRight.performClick();
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    private void getCollectCard() {
        mPresenter.getCollectCard(folderId, page, Constants.PAGE_SIZE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnCollectEvent event) {
        CollectCardBean bean = new CollectCardBean();
        bean.setId(event.getCardId());
        mAdapter.remove(mAdapter.getData().indexOf(bean));
        EventBus.getDefault().post(new DeleteCollectCardEvent(folderId, 1));
    }

    @Override
    public void onCheckedChanged(CollectCardBean bean, int position, boolean isChecked) {
        if (isChecked) {
            if (!mCheckedList.contains(bean)) {
                mCheckedList.add(bean);
            }
        } else {
            if (mCheckedList.contains(bean)) {
                mCheckedList.remove(bean);
            }
        }
        if (mCheckedList.size() > 0) {
            mTvToolbarRight.setText("删除");
        } else {
            mTvToolbarRight.setText("取消");
        }
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        switch (mTvToolbarRight.getText().toString()) {
            case "编辑":
                mTvItemCollectFolderTitle.setVisibility(View.GONE);
                mEtCollectFolder.setVisibility(View.VISIBLE);
                mEtCollectFolder.requestFocus();
                mEtCollectFolder.setCursorVisible(true);
                mEtCollectFolder.setSelection(mEtCollectFolder.length());
                mTvToolbarRight.setText("取消");
                mAdapter.setDelete(true);
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case "取消":
                Utils.closeInputMethod(mEtCollectFolder);
                mTvItemCollectFolderTitle.setVisibility(View.VISIBLE);
                mEtCollectFolder.setVisibility(View.GONE);
                mTvToolbarRight.setText("编辑");
                mAdapter.setDelete(false);
                mAdapter.setOnItemClickListener(this);
                mAdapter.notifyDataSetChanged();
                break;
            case "删除":
                Utils.closeInputMethod(mEtCollectFolder);
                mAction = ACTION_DELETE;
                List<Integer> idList = new ArrayList<>();
                for (CollectCardBean bean : mCheckedList) {
                    idList.add(bean.getId());
                }
                mPresenter.deleteCollectCard(folderId, idList);
                break;
        }
    }

    @OnClick(R.id.iv_toolbar_back)
    public void onMIvToolbarBackClicked() {
        onBackPressed();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CollectCardBean bean = (CollectCardBean) adapter.getData().get(position);
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra("id", bean.getId());
        intent.putExtra("name", bean.getName());
        startActivity(intent);
    }

    private void initRecyclerView() {
        mAdapter = new CollectCardAdapter(new ArrayList<CollectCardBean>(), this);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRvCollectCard);
        mAdapter.setOnItemClickListener(this);
        mRvCollectCard.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRvCollectCard.setAdapter(mAdapter);
        mRvCollectCard.addItemDecoration(new CardBagCardItemDecoration());
        mAdapter.notifyDataSetChanged();
    }

    class DoneListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                newName = mEtCollectFolder.getText().toString();
                Utils.closeInputMethod(mEtCollectFolder);
                mAction = ACTION_CHANGE;
                mPresenter.changeCollectFolderName(folderId, newName);
                return true;
            }
            return false;
        }
    }
}
