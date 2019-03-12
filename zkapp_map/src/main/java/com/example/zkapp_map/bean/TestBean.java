package com.example.zkapp_map.bean;

import java.util.List;

/**
 * @author Fsnzzz
 * @date 2019/1/17
 * @month 01
 * @package com.example.zkapp_map.bean
 */
public class TestBean {


    private String title;
    private List<String> childKind;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChildKind() {
        return childKind;
    }

    public void setChildKind(List<String> childKind) {
        this.childKind = childKind;
    }


    @Override
    public String toString() {
        return "TestBean{" +
                "title='" + title + '\'' +
                ", childKind=" + childKind +
                '}';
    }
}
