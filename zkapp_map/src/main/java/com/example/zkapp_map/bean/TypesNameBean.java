package com.example.zkapp_map.bean;

import java.util.List;

public class TypesNameBean {
    private String styleName;
    private List<JSONBean.TypesBean> typesBeans;

    public TypesNameBean(String styleName, List<JSONBean.TypesBean> typesBeans) {
        this.styleName = styleName;
        this.typesBeans = typesBeans;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public List<JSONBean.TypesBean> getTypesBeans() {
        return typesBeans;
    }

    public void setTypesBeans(List<JSONBean.TypesBean> typesBeans) {
        this.typesBeans = typesBeans;
    }
}
