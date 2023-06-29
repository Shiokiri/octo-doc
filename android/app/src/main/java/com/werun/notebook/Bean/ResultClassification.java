package com.cym.notebook.Bean;

public class ResultClassification {

    private Object categoryBean;
    private Object userBean;
    private Object noteBeanList;
    private boolean success;
    private String msg;

    public Object getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(Object categoryBean) {
        this.categoryBean = categoryBean;
    }

    public Object getUserBean() {
        return userBean;
    }

    public void setUserBean(Object userBean) {
        this.userBean = userBean;
    }

    public Object getNoteBeanList() {
        return noteBeanList;
    }

    public void setNoteBeanList(Object noteBeanList) {
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
