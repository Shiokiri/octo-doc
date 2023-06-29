package com.cym.notebook.Bean;

import java.io.Serializable;

public class Note implements Serializable {
    private String nid;
    private String noteTitle;
    private String noteCreateTime;
    private boolean noteCompletedState;
    private String noteContent;
    private String uid;
    private String description;
    private String cname;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public boolean isNoteCompletedState() {
        return noteCompletedState;
    }

    public void setNoteCompletedState(boolean noteCompletedState) {
        this.noteCompletedState = noteCompletedState;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
