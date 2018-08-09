package com.zxcx.zhizhe.ui.my.selectAttention;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBean;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ClassifyBean extends RetrofitBean implements MultiItemEntity, Serializable {


	/**
	 * type : 分类 content : [{"imageUrl":"123","title":"标题"}]
	 */


	public static final int TYPE_CLASSIFY = 1;

	@SerializedName("title")
	private String title;
	@SerializedName("id")
	private int id;
	@SerializedName("collectionData")
	private List<ClassifyCardBean> dataList;
	
	private boolean isChecked = false;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ClassifyBean that = (ClassifyBean) o;
		return Objects.equals(id, that.id);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(id);
	}
	
	@Override
	public int getItemType() {
		return TYPE_CLASSIFY;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ClassifyCardBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<ClassifyCardBean> dataList) {
		this.dataList = dataList;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean checked) {
		isChecked = checked;
	}
}

