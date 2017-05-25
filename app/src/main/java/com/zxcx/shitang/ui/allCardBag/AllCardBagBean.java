package com.zxcx.shitang.ui.allCardBag;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AllCardBagBean extends AbstractExpandableItem<AllCardBagBean.ContentBean> implements MultiItemEntity{


    /**
     * type : 分类
     * content : [{"imgUrl":"123","title":"标题"}]
     */

    private String type;
    private List<ContentBean> content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public static class ContentBean implements MultiItemEntity{
        /**
         * imgUrl : 123
         * title : 标题
         */

        private String imgUrl;
        private String title;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }
}

