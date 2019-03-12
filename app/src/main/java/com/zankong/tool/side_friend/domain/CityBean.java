package com.zankong.tool.side_friend.domain;

import java.util.List;

public class CityBean {
    private String cityName;
    private List<String> countyList;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<String> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<String> countyList) {
        this.countyList = countyList;
    }
}
