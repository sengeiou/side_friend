package com.zankong.tool.side_friend.domain;

import java.util.List;

public class SieveBean {
    private String name;
    private List<SieveListBean> listBeans;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SieveListBean> getListBeans() {
        return listBeans;
    }

    public void setListBeans(List<SieveListBean> listBeans) {
        this.listBeans = listBeans;
    }
}
