package com.zankong.tool.side_friend.diy_view.mail_list.search;


import com.zankong.tool.side_friend.diy_view.mail_list.cn.CN;

/**
 * Created by you on 2017/9/11.
 */

public class Contact implements CN {
    public String name;
    public final String imgUrl;
    public final int iD;
    public boolean status;
    public int houseId;
    public Contact(String name, String imgUrl,int iD) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.iD = iD;
    }
    public Contact(String name, String imgUrl,int iD,int houseId) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.iD = iD;
        this.houseId = houseId;
    }

    @Override
    public String chinese() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", iD=" + iD +
                ", status=" + status +
                '}';
    }
}
