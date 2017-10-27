package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class DeleteCollectCardEvent {
    private int collectFolderId;
    private int count;

    public DeleteCollectCardEvent(int collectFolderId, int count) {
        this.collectFolderId = collectFolderId;
        this.count = count;
    }

    public int getCollectFolderId() {
        return collectFolderId;
    }

    public void setCollectFolderId(int collectFolderId) {
        this.collectFolderId = collectFolderId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
