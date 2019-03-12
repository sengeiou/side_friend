package com.example.zkapp_map.bean;

import java.util.List;

public class JSONBean {


    /**
     * styleId : 2
     * styleName : 顺取
     * classify : 跑腿
     * types : [{"id":10,"style":2,"name":"美食","valid":1,"skillCertId":1,"hasOrigin":1,"hasDestination":1,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":15,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-03-02T02:39:51.000Z"},{"id":11,"style":2,"name":"文件","valid":1,"skillCertId":1,"hasOrigin":1,"hasDestination":1,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":12,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-03-02T02:46:46.000Z"},{"id":250,"style":2,"name":"服饰","valid":1,"skillCertId":1,"hasOrigin":0,"hasDestination":0,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":5,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-02-26T06:32:07.000Z"},{"id":12,"style":2,"name":"鲜花","valid":1,"skillCertId":1,"hasOrigin":1,"hasDestination":1,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":1,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-02-26T04:03:38.000Z"},{"id":13,"style":2,"name":"蛋糕","valid":1,"skillCertId":1,"hasOrigin":1,"hasDestination":1,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":0,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-01-15T07:17:32.000Z"},{"id":251,"style":2,"name":"电子产品","valid":1,"skillCertId":1,"hasOrigin":0,"hasDestination":0,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":0,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-01-15T07:17:32.000Z"},{"id":252,"style":2,"name":"其他","valid":1,"skillCertId":1,"hasOrigin":0,"hasDestination":0,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":0,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-01-15T07:17:32.000Z"}]
     */

    private int styleId;
    private String styleName;
    private String classify;
    private List<TypesBean> types;

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public static class TypesBean {
        /**
         * id : 10
         * style : 2
         * name : 美食
         * valid : 1
         * skillCertId : 1
         * hasOrigin : 1
         * hasDestination : 1
         * advanceMoney : 0
         * tag :
         * isNeedAdvancePay : 0
         * num : 15
         * createdAt : 2018-10-10T16:00:00.000Z
         * updatedAt : 2019-03-02T02:39:51.000Z
         */

        private int id;
        private int style;
        private String name;
        private int valid;
        private String skillCertId;
        private int hasOrigin;
        private int hasDestination;
        private int advanceMoney;
        private String tag;
        private int isNeedAdvancePay;
        private int num;
        private String createdAt;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public String getSkillCertId() {
            return skillCertId;
        }

        public void setSkillCertId(String skillCertId) {
            this.skillCertId = skillCertId;
        }

        public int getHasOrigin() {
            return hasOrigin;
        }

        public void setHasOrigin(int hasOrigin) {
            this.hasOrigin = hasOrigin;
        }

        public int getHasDestination() {
            return hasDestination;
        }

        public void setHasDestination(int hasDestination) {
            this.hasDestination = hasDestination;
        }

        public int getAdvanceMoney() {
            return advanceMoney;
        }

        public void setAdvanceMoney(int advanceMoney) {
            this.advanceMoney = advanceMoney;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getIsNeedAdvancePay() {
            return isNeedAdvancePay;
        }

        public void setIsNeedAdvancePay(int isNeedAdvancePay) {
            this.isNeedAdvancePay = isNeedAdvancePay;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
