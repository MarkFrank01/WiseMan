package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class CommitNoteReviewEvent {

    private int noteId;

    public CommitNoteReviewEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int id) {
        this.noteId = id;
    }
}
