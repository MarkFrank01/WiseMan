package com.zxcx.zhizhe.widget.gridview_tj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;

import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/8
 * @Description :
 */
public class TJGridViewAdapter extends BaseAdapter {
    private List<ContentBean> mData;
    private LayoutInflater inflater;

    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    //保存一个context
    private Context mContext;

    //存一个ImageView
    private ImageView mImageView;

    //暂存一个url
    private String mImageViewUrl;

    public TJGridViewAdapter(Context context, List<ContentBean> mData, int curIndex, int pageSize){
        inflater = LayoutInflater.from(context);
        this.mData = mData;
        this.curIndex = curIndex;
        this.pageSize = pageSize;

        this.mContext = context;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mData.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mData.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mData.size() > (curIndex + 1) * pageSize ? pageSize : (mData.size() - curIndex * pageSize);

    }

    @Override
    public ContentBean getItem(int position) {
        return mData.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize，
         */
        int pos = position + curIndex * pageSize;
        viewHolder.tv.setText(mData.get(pos).getTitle());

        //通过类型展示图标

        return convertView;
    }

    private class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}
