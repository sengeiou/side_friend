package com.zankong.tool.side_friend.domain;

import java.util.List;

public class ProvinceBean {
    private String provinceName;
    private List<CityBean> cityBeans;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<CityBean> getCityBeans() {
        return cityBeans;
    }

    public void setCityBeans(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }
}
