package com.zxcx.zhizhe.widget.flowtag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/2/16
 * @Description :
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<ClassifyCardBean> mDataList;

    public TagAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_card_new1, null);

        CheckBox checkBox = view.findViewById(R.id.cb_item_select_hot_label);
        String title = mDataList.get(position).getName();
        checkBox.setText(title);
        checkBox.setChecked(mDataList.get(position).getFollow());


        return view;
    }

    public void onlyAddAll(List<ClassifyCardBean> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (mDataList.get(position).getFollow()) {
            return true;
        } else {
            return false;
        }
    }
}
