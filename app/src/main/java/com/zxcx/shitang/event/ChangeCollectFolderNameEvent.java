package com.zxcx.shitang.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeCollectFolderNameEvent {

    private int id;
    private String name;

    public ChangeCollectFolderNameEvent(int id, String newName) {
        this.id = id;
        name = newName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
