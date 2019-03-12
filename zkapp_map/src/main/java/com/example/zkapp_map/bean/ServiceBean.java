package com.example.zkapp_map.bean;

import java.util.List;

public class ServiceBean {
    private String name;
    private List<JSONBean>list;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JSONBean> getList() {
        return list;
    }

    public void setList(List<JSONBean> list) {
        this.list = list;
    }
}
