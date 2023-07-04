package com.werun.notebook.Bean;

import java.util.List;

public class ResultUser {
    private Object userBean;
    private List<Classification> categoryBeanList;
    private boolean success;
    private String msg;

    public Object getUserBean() {
        return userBean;
    }

    public void setUserBean(Object userBean) {
        this.userBean = userBean;
    }

    public List<Classification> getCategoryBeanList() {
        return categoryBeanList;
    }

    public void setCategoryBeanList(List<Classification> categoryBeanList) {
        this.categoryBeanList = categoryBeanList;
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
