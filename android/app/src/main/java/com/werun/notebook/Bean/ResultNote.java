package com.cym.notebook.Bean;

import java.util.List;

public class ResultNote {
    private List<Note> noteBeanList;
    private boolean success;
    private String msg;

    public List<Note> getNoteBeanList() {
        return noteBeanList;
    }

    public void setNoteBeanList(List<Note> noteBeanList) {
        this.noteBeanList = noteBeanList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
